/*
 * CommandMenu.java
 * 07/12/2006
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.util.Calendar;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * Class responsible to manage the commands added to a given screen.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see br.framework.jme.ui.Screen
 */
public class CommandMenu {
	/**
	 * <p>
	 * Count of items that can be displayed at the command bar without a list.
	 * </p>
	 */
	private static final int CMDS_BAR_COUNT = 2;

    /**
     * <p>
     * Offset.
     * </p>
     */
    private final int offset = 2;
    
    /**
     * <p>
     * Flag used to stop the thread in charge to refresh the clock.
     * </p>
     */
    private boolean stopClock = true;
    
    /**
     * <p>
     * Command list.
     * </p>
     */
    Vector cmdList;
    
    /**
     * <p>
     * Parent screen.
     * </p>
     */
    Screen parent;
    
    /**
     * <p>
     * Command listener. Object resposible to be notified when a command is
     * triggered.
     * </p>
     */
    CommandListener listener;
    
    /**
     * <p>
     * Index of the selected item.
     * </p>
     */
    int selectedIndex;
    
    /**
     * <p>
     * List item height.
     * </p>
     */
    int itemHeight;

    /**
     * <p>
     * Body font.
     * </p>
     */
    Font font;
    
    /**
     * <p>
     * Font color.
     * </p>
     */
    int fontColor;
    
    /**
     * <p>
     * Bar color.
     * </p>
     */
    int barColor;

    /**
     * <p>
     * List color.
     * </p>
     */
    int listColor;

    /**
     * <p>
     * Selection color.
     * </p>
     */
    int selectionColor;
    
    /**
     * <p>
     * Negative font color.
     * </p>
     */
    int negativeFontColor;
    
    /**
     * <p>
     * If the command list is expanded or not.
     * </p>
     */
    boolean isExpanded;
    
    /**
     * <p>
     * If the commands are sorted already or not.
     * </p>
     */
    boolean sortedList;
    
	/**
	 * <p>
	 * Command bar are has been pressed.
	 * </p>
	 */
	boolean commandBarAreaPressed;
    
    /**
     * <p>
     * Command menu rectangle.
     * </p>
     */
    Rect barRect;

    /**
     * <p>
     * Command menu list rectangle.
     * </p>
     */
    Rect listRect;
    
    /**
     * <p>
     * </p>
     */
    boolean showClock;
    
    /**
     * <p>
     * Current hour.
     * </p>
     */
    int clockHour;

    /**
     * <p>
     * Current minute.
     * </p>
     */
    int clockMinute;
    
    /**
     * <p>
     * Constructor.
     * </p>
     * @param parent Parent screen.
     * @param listener Listener.
     */
    public CommandMenu(Screen parent, CommandListener listener) {
        this.parent = parent;
        this.listener = listener;
        cmdList = new Vector();
        font = Font.getFont(Font.FACE_SYSTEM,Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        setBarColor(255, 255, 255); //white color.
        setFontColor(0, 0, 0); //black color.
        setSelectionColor(0, 0, 0); //black color.
        //
        barRect = new Rect();
        barRect.w = parent.getWidth();
        barRect.h = font.getHeight() + offset * 2 +1;
        //
        listRect = new Rect();
    }
    
    /**
     * <p>
     * Set the font.
     * </p>
     * @param font Font.
     */
    public void setFont(Font font) {
        this.font = font;
        //
        barRect.h = font.getHeight() + offset * 2 +1;
    }
    
    /**
     * <p>
     * Set the color to be used to paint the command bar.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setBarColor(int r, int g, int b) {
        barColor = UIUtil.getHexColor(r, g, b);
    }

    /**
     * <p>
     * Set the color to be used to paint the command list.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setListColor(int r, int g, int b) {
        listColor = UIUtil.getHexColor(r, g, b);
    }

    /**
     * <p>
     * Set the font color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setFontColor(int r, int g, int b) {
        fontColor = UIUtil.getHexColor(r, g, b);
    }
    
    /**
     * <p>
     * Set the selection color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setSelectionColor(int r, int g, int b) {
        selectionColor = UIUtil.getHexColor(r, g, b);
        negativeFontColor = UIUtil.getNegativeColor(r, g, b);
    }
    
    /**
     * <p>
     * Method used to enable or disable the clock to be displayed on the
     * command menu bar.
     * </p>
     * @param enabled Enable or disable.
     */
    public void setClock(boolean enabled) {
    	if (enabled && !showClock) {
			Calendar c = Calendar.getInstance();
			clockHour = c.get(Calendar.HOUR_OF_DAY);
			clockMinute = c.get(Calendar.MINUTE);
			//
    		if (parent.isShown()) {
       			startClock();
    		}
    	} else if (!enabled && showClock) {
    		stopClock();
    	}
    	showClock = enabled;
    }

    /**
     * <p>
     * Get the command count.
     * </p>
     * @return Count.
     */
    public synchronized int getCommandCount() {
        return cmdList.size();
    }
    
    /**
     * <p>
     * Get command menu rectangle.
     * </p>
     * @return Rectangle.
     */
    public Rect getRect() {
    	return barRect;
    }
    
    /**
     * <p>
     * Method invoked immediately prior to the parent screen is made visible on
     * the display.
     * </p>
     */
    void showNotify() {
    	if (showClock) {
    		startClock();
    	}
    }
    
    /**
     * <p>
     * Method invoked shortly after the parent screen is removed from the
     * display.
     * </p>
     */
    void hideNotify() {
    	if (showClock) {
    		stopClock();
    	}
    }

    /**
     * <p>
     * Draw the component.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    final synchronized void paint(Graphics g, int x, int y) {
        if (!sortedList && getCommandCount() > 1) {
            cmdList = sortCommands(cmdList);
            sortedList = true;
        }
        //
        barRect.x = x;
        barRect.y = y;
        //
        g.setClip(barRect.x, barRect.y, barRect.w, barRect.h);
        drawBar(g, x, y, barRect.w, barRect.h);
        //
        g.setClip(barRect.x, 0, parent.getWidth(), parent.getHeight());
        drawList(g);
    }
    
    /**
     * <p>
     * Triggered when a key is pressed.
     * </p>
     * @param keyCode Key code.
     * @return If the key pressed was handled by the component or not.
     */
	final boolean keyPressed(int keyCode) {
		boolean handled = false;
		if (getCommandCount() > 0) {
			final int gameKey = parent.getGameAction(keyCode);
			if (gameKey == Canvas.FIRE || isLeftSK(keyCode) ||
				isRightSK(keyCode)) {
				handled = manageAction(keyCode);
			} else if (gameKey == Canvas.UP) {
				handled = moveSelection(false);
			} else if (gameKey == Canvas.DOWN) {
				handled = moveSelection(true);
			} else if (isBackKey(keyCode) && isExpanded) {
				isExpanded = false;
				handled = true;
				repaint();
			}
		}
		return handled;
    }

    /**
     * <p>
     * Triggered when a key is hold.
     * </p>
     * @param keyCode Key code.
     * @return If the key pressed was handled by the component or not.
     */
    final boolean keyRepeated(int keyCode) {
    	final int gameKey = parent.getGameAction(keyCode);
    	if (gameKey == Canvas.UP || gameKey == Canvas.DOWN) {
       		return keyPressed(keyCode);
    	} else {
    		return false;
    	}
    }
    
	/**
     * <p>
     * Triggered when a key is released.
     * </p>
     * @param keyCode Key code.
     * @return If the key pressed was handled by the component or not.
	 */
	final boolean keyReleased(int keyCode) {
		return false;
	}
	
	/**
     * <p>
     * Triggered when the pen is dragged.
     * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
     * @return If the pen dragged was handled by the component or not.
	 */
	final boolean pointerDragged(int x, int y) {
		if (isExpanded) {
			if (isListCommandArea(x, y)) {
				return pointerPressed(x, y);
			}
		}
		return false;
	}

	/**
     * <p>
     * Triggered when the pen is pressed.
     * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
     * @return If the pen pressed was handled by the component or not.
	 */
	final boolean pointerPressed(int x, int y) {
		if (isListCommandArea(x, y)) {
			int selItem = (y - listRect.y) / itemHeight;
			if (selItem != selectedIndex) {
				selectedIndex = selItem;
				repaint();
			}
			return true;
		} else if (isCommandBarArea(x, y)) {
			commandBarAreaPressed = true;
			return true;
		}
		return isExpanded;
	}
	
	/**
     * <p>
     * Triggered when the pen is released.
     * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
     * @return If the pen released was handled by the component or not.
	 */
	final boolean pointerReleased(int x, int y) {
		if (isCommandBarArea(x, y) && commandBarAreaPressed) {
			int middle = barRect.x + barRect.w / 2;
			if (x < middle) { //left command.
				return triggerLeftCommand();
			} else if (x > middle) { //right command.
				return triggerRightCommand();
			}
		} else if (isListCommandArea(x, y)) {
			return manageAction(parent.getKeyCode(Canvas.FIRE));
		} else {
			if (isExpanded) {
				//hit a point out of command list bounds, just close it.
				isExpanded = false;
				repaint();
				return true;
			}
		}
		//
		commandBarAreaPressed = false; //reset control variable.
		return false;
	}
    
	/**
	 * <p>
	 * Verify if a given key code represents the left softkey.
	 * </p>
	 * @param keyCode Key code.
	 * @return Is left SK.
	 */
	boolean isLeftSK(int keyCode) {
		String keyName = parent.getKeyName(keyCode);
		//
		if (keyName != null) {
			keyName = keyName.toUpperCase();
			//
			if (keyName.indexOf("SOFT") != -1) {
				if (keyName.indexOf("SOFT1") != -1 ||
					keyName.indexOf("LEFT") != -1) {
					return true;
				}
			}
			//
			if (keyName.indexOf("SELECTION") != -1) {
				if (keyName.indexOf("LEFT") != -1) {
					return true;
				}
			}
		}
		//
		return false;
	}

	/**
	 * <p>
	 * Verify if a given key code represents the right softkey.
	 * </p>
	 * @param keyCode Key code.
	 * @return Is right SK.
	 */
	boolean isRightSK(int keyCode) {
		String keyName = parent.getKeyName(keyCode);
		//
		if (keyName != null) {
			keyName = keyName.toUpperCase();
			//
			if (keyName.indexOf("SOFT") != -1) {
				if (keyName.indexOf("SOFT2") != -1 ||
					keyName.indexOf("RIGHT") != -1) {
					return true;
				}
			}
			//
			if (keyName.indexOf("SELECTION") != -1) {
				if (keyName.indexOf("RIGHT") != -1) {
					return true;
				}
			}
		}
		//
		return false;
	}

	/**
	 * <p>
	 * Verify if a given key code represents the back physical key.
	 * </p>
	 * @param keyCode Key code.
	 * @return Is back key.
	 */
	boolean isBackKey(int keyCode) {
		String keyName = parent.getKeyName(keyCode);
		//
		if (keyName != null) {
			return StringUtil.equalsIgnoreCase(keyName, "BACK");
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * Add a given command into the commands list.
	 * </p>
	 * @param cmd Command.
	 */
	synchronized void addCommand(Command cmd) {
	    cmdList.addElement(cmd);
	    sortedList = false; //request re-sort of the commands.
	    repaint();
	}

	/**
	 * <p>
	 * Remove a given command from the commands list.
	 * </p>
	 * @param cmd Command.
	 * @return If the command was in the list or not.
	 */
	synchronized boolean removeCommand(Command cmd) {
	    if (cmdList.removeElement(cmd)) {
	        repaint();
	        return true;
	    } else {
	        return false;
	    }
	}
	
	/**
	 * <p>
	 * Verifies if a given command is present in the commands list.
	 * </p>
	 * @param cmd Command.
	 * @return Present or not.
	 */
	synchronized boolean hasCommand(Command cmd) {
		return cmdList.contains(cmd);
	}
	
	/**
     * <p>
     * Remove all commands of the screen.
     * </p>
	 */
	synchronized void removeAllCommands() {
		cmdList.removeAllElements();
		repaint();
	}

	/**
	 * <p>
	 * Set the command listener. Object to be notified when an command is
	 * triggered.
	 * </p>
	 * @param listener Object.
	 */
	void setCommandListener(CommandListener listener) {
	    this.listener = listener;
	}
	
	/**
	 * <p>
	 * Get the command listener.
	 * </p>
	 * @return Listener.
	 */
	CommandListener getCommandListener() {
		return listener;
	}
    
    /**
     * <p>
     * Get all the commands.
     * </p>
     * @return Commands.
     */
    synchronized Command[] getAllCommands() {
        Command[] cmds = new Command[cmdList.size()];
        cmdList.copyInto(cmds);
        //
        return cmds;
    }

	/**
	 * <p>
	 * Request to repaint the component.
	 * </p>
	 */
	final void repaint() {
	    parent.requestRepaint();
	}

	/**
	 * <p>
	 * If the list of commands is expanded or not.
	 * </p>
	 * @return Expanded or not.
	 */
	boolean isExpanded() {
		return isExpanded;
	}

	/**
	 * <p>
	 * Verify if the given coordinates are in the command bar area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isCommandBarArea(int x, int y) {
		return barRect.contains(x, y);
	}
	
	/**
	 * <p>
	 * Verify if the given coordinates are in the list of commands area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isListCommandArea(int x, int y) {
		return listRect.contains(x, y);
	}

	/**
     * <p>
     * Draw the commands bar.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
     */
    void drawBar(Graphics g, int x, int y, int w, int h) {
        int count = getCommandCount();
    	if (count > 0) {
            //drawing the background.
            g.setColor(190, 190, 190); //dark gray color.
            g.drawLine(x, y, w, y);
            UIUtil.fillRect(
            	g,
            	x,
            	y +1,
            	w,
            	h,
            	barColor,
            	Screen.skin.getCommandMenuBarStyle());
            //
            g.setColor(fontColor);
            g.setFont(font);
            final int fh = font.getHeight();
            int off_x = 0;
            String time = null;
            //
            final int yy = y + offset +1;
            //
            if (showClock) {
            	//displaying the clock.
            	time = formatClock(clockHour, clockMinute);
                off_x = (w - font.stringWidth(time)) / 2;
                g.setClip(x + off_x, yy, font.stringWidth(time), fh);
                //
                g.drawString(time, x + off_x, yy, Graphics.TOP | Graphics.LEFT);
            }
            //
            final int ww = w / 2;
            //
            String label = getBestLabel((Command)cmdList.elementAt(0), ww);
            if (isExpanded) {
            	label = 
            		I18N.getFramework("framework.jme.ui.commandmenu.select");
            }
            if (label != null) {
            	//displaying left command.
            	if (showClock) {
                    g.setClip(x, yy, ww - (ww - off_x) - offset, fh);
                    //
            		//aligning to the right.
            		off_x = offset;
            	} else {
            		//aligning to the center.
                	off_x = (ww - font.stringWidth(label)) / 2;
                	//
                    g.setClip(x + offset, yy, ww - offset * 2, fh);
            	}
            	//
                g.drawString(
                	label, x + off_x +1, yy, Graphics.TOP | Graphics.LEFT);
            }
            //
            if (count > 1) {
            	//displaying right command.
            	if (count > CMDS_BAR_COUNT) {
                	label = 
                		I18N.getFramework("framework.jme.ui.commandmenu.more");
            	} else {
                    label = getBestLabel((Command)cmdList.elementAt(1), ww);
                }
                if (label != null) {
                	if (showClock) {
                		final int l = font.stringWidth(time) / 2 + offset;
                		//aligning to the right.
                    	off_x = ww - font.stringWidth(label) - offset;
                    	if (off_x < l) {
                    		off_x = l;
                    	}
                    	//
                    	g.setClip(x + ww + off_x, yy, ww - off_x - offset, fh);
                	} else {
                		//aligning to the center.
                    	off_x = (ww - font.stringWidth(label)) / 2;
                    	if (off_x < 0) {
                    		off_x = offset +1;
                    	}
                    	//
                        g.setClip(x + ww + off_x, yy, ww - offset * 2, fh);
                	}
                	//
                	g.drawString(
                	    label,
                	    x + ww + off_x,
                	    yy,
                	    Graphics.TOP | Graphics.LEFT);
                }
            }
            //
            if (!showClock) {
            	//black line separating the commands.
            	g.setClip(x, y, w, h);
                g.setColor(190, 190, 190); //dark gray color.
                g.drawLine(x + ww, y, x + ww, y + h);
            }
        }
    }
    
    /**
     * <p>
     * Draw the commands list.
     * </p>
     * @param g Graphics object.
     */
    void drawList(Graphics g) {
    	int size = getCommandCount();
    	if (isExpanded && size > CMDS_BAR_COUNT) {
    		final int fHeight = font.getHeight();
    		itemHeight = fHeight + offset;
            listRect.h = (size -1) * itemHeight;
            String[] labels = new String[size -1];
            listRect.w = -1;
    		final int maxW = barRect.w;
    		Command cmd = null;
    		String label = null;
    		int i = 1;
    		//
    		//figure out the list width based on the largest label width.
    		for (; i < size; i++) {
    			cmd = (Command)cmdList.elementAt(i);
    			label = getBestLabel(cmd, maxW);
    			if (label != null) {
    				if (listRect.w != -1) {
    					listRect.w =
    						Math.max(listRect.w, font.stringWidth(label));
    				} else {
    					listRect.w = font.stringWidth(label);
    				}
    				labels[i -1] = label;
    			}
			}
    		if (listRect.w == -1) {
    			listRect.w = maxW / 2;
    		} else {
    			listRect.w += offset * 4;
    		}
    		listRect.x = maxW - listRect.w;
    		listRect.y = barRect.y - listRect.h;
    		int x = listRect.x;
    		int y = listRect.y;
    		//
    		//draw the background.
        	UIUtil.fillRect(
        		g,
        		x,
        		y,
        		listRect.w,
        		listRect.h,
        		listColor,
        		Screen.skin.getCommandMenuListStyle());
        	g.setColor(190, 190, 190); //dark gray color.
        	g.drawRect(x, y, listRect.w, listRect.h);
        	g.setColor(fontColor);
        	x += offset * 2;
        	i = 0;
        	//
        	for (; i < labels.length; i++) {
        		y += offset;
        		if (i == selectedIndex) {
        			//draw selection bar.
        			UIUtil.fillRect(
        				g,
        				x - offset -1,
        				y -1,
        				listRect.w,
        				fHeight +1,
        				selectionColor,
        				Screen.skin.getSelectionStyle());
        			//
                	g.setColor(negativeFontColor);
        		} else {
        			g.setColor(fontColor);
        		}
        		if (labels[i] != null) {
        			g.drawString(labels[i], x, y, Graphics.TOP | Graphics.LEFT);
        		}
        		y += fHeight;
        	}
    	}
    }
    
    /**
     * <p>
     * Manage the actions to be executed when a given key event is triggered.
     * </p>
     * @param keyCode Key code.
     * @return Event handled by the component or not.
     */
    boolean manageAction(int keyCode) {
    	boolean handled = false;
    	int gameKey = parent.getGameAction(keyCode);
    	//
    	if (gameKey == Canvas.FIRE && isExpanded) {
			notifyEvent(
				(Command)cmdList.elementAt(selectedIndex +1));
			isExpanded = false;
			handled = true;
		} else if (isLeftSK(keyCode)) {
			handled = triggerLeftCommand();
		} else if (isRightSK(keyCode)) {
			handled = triggerRightCommand();
		}
    	//
    	if (handled) {
    		repaint();
    	}
    	//
    	return handled;
    }
    
    /**
     * <p>
     * Executes the left command.
     * </p>
     * @return Handled.
     */
    boolean triggerLeftCommand() {
		if (isExpanded) {
			notifyEvent(
				(Command)cmdList.elementAt(selectedIndex +1));
			isExpanded = false;
		} else {
			notifyEvent((Command)cmdList.elementAt(0));
		}
		//
		repaint();
		//
		return true;
    }
    
    /**
     * <p>
     * Executes the right command.
     * </p>
     * @return Handled.
     */
    boolean triggerRightCommand() {
    	final int count = getCommandCount();
    	//
		if (count > CMDS_BAR_COUNT) {
            isExpanded = !isExpanded;
            selectedIndex = 0;
		} else if (count > 1) {
			notifyEvent((Command)cmdList.elementAt(1));
		} else {
			return false;
		}
		//
		repaint();
		//
		return true;
    }

    /**
     * <p>
     * Move the selection of the commands list.
     * </p>
     * @param forward Move forward or backward.
     * @return Event handled by the component or not.
     */
    boolean moveSelection(boolean forward) {
    	int count = getCommandCount();
    	if (count > CMDS_BAR_COUNT && isExpanded) {
    		count -= 1;
    		if (forward) {
    			if (selectedIndex +1 < count) {
    				selectedIndex++;
    			} else {
    				selectedIndex = 0;
    			}
    			repaint();
    		} else {
    			if (selectedIndex -1 >= 0) {
    				selectedIndex--;
    			} else {
    				selectedIndex = count -1;
    			}
    			repaint();
    		}
    		return true;
    	} else {
    		return false;
    	}
	}
    
    /**
     * <p>
     * Sort the commands in the list, in order to figure out the right order in
     * which they will be presented.
     * </p>
     * @param cmdList Commands list.
     * @return Sorted list.
     */
    Vector sortCommands(Vector cmdList) {
        final int size = cmdList.size();
        ComparableCommand[] l = new ComparableCommand[size];
        int i = 0;
        for (; i < size; i++) {
            l[i] = new ComparableCommand((Command)cmdList.elementAt(i));
        }
        int j;
        ComparableCommand compCmd = null;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size -1; j++) {
                if (l[j].getValue() > l[j +1].getValue()) {
                    compCmd = l[j];
                    l[j] = l[j +1];
                    l[j +1] = compCmd;
                }
            }
        }
        cmdList.removeAllElements();
        for (i = 0; i < size; i++) {
            cmdList.addElement(l[i].cmd);
        }
        return cmdList;
    }
    
    /**
     * <p>
     * Notify the command list that a given command was pressed.
     * </p>
     * @param triggeredEvent Command pressed.
     */
    void notifyEvent(Command triggeredEvent) {
        if (listener != null) {
            listener.commandAction(triggeredEvent, parent);
        }
    }
    
    /**
     * <p>
     * Method that turns clock on.
     * </p>
     */
    void startClock() {
		stopClock = false;
		//
		Runnable clock = new Runnable() {
			public void run() {
				Calendar c;
				boolean refresh;
				int sleepTime = 1000; //1seg.
				boolean isTimeSync = false; //time synchronized.
				//
				while (!stopClock) {
					try {
						Thread.sleep(sleepTime);
					} catch (Exception e) {
					}
					//
					c = Calendar.getInstance();
					refresh = false;
					//
					if (c.get(Calendar.HOUR_OF_DAY) != clockHour) {
						clockHour = c.get(Calendar.HOUR_OF_DAY);
						refresh = true;
					}
					if (c.get(Calendar.MINUTE) != clockMinute) {
						clockMinute = c.get(Calendar.MINUTE);
						refresh = true;
					}
					//
					if (refresh) {
						if (isTimeSync) {
							//time just changed. So next change will happen only
							//in the next minute, therefore the interval between
							//changes is now about 1min.
							sleepTime = 60000; //1min.
						}
						repaint();
					} else {
						//time remaining to the next minute change.
						sleepTime = 60000 - c.get(Calendar.SECOND) * 1000;
						isTimeSync = true; //synchronizing...
					}
				}
			};
		};
		Thread t = new Thread(clock);
		t.setPriority(Thread.MIN_PRIORITY);
		t.start();
    }
    
    /**
     * <p>
     * Method that turns clock off.
     * </p>
     */
    void stopClock() {
    	stopClock = true;
    }
    
    /**
     * <p>
     * Get the best label to be used by a given command.
     * </p>
     * @param cmd Command.
     * @param maxWidth Width.
     * @return Best label.
     */
    private String getBestLabel(Command cmd, int maxWidth) {
//		//this implementation works only for midp2.
//    	String label = cmd.getLongLabel();
//    	//
//		if (label == null || font.stringWidth(label) > maxWidth) {
//			label = cmd.getLabel();
//		}
//		return label;
		//
		return cmd.getLabel();
    }
    
    /**
     * <p>
     * Format a given time.
     * </p>
     * @param hour Hour.
     * @param minute Minute.
     * @return Time formatted.
     */
    private String formatClock(int hour, int minute) {
    	return (hour < 10 ? "0" + hour : "" + hour) + ":" +
    		(minute < 10 ? "0" + minute : "" + minute);
    }
    
    /**
     * <p>
     * Wrapper class used to return the priority of a given command, based on
     * its type and priority.
     * </p>
     * 
     * @author Ernandes Mourao Junior (ernandes@gmail.com)
     * @version 1.0
     * @since 1.0
     */
    class ComparableCommand {
        /**
         * <p>
         * Command.
         * </p>
         */
        Command cmd;
        
        /**
         * <p>
         * Constructor.
         * </p> 
         * @param cmd Command.
         */
        public ComparableCommand(Command cmd) {
           this.cmd = cmd; 
        }
        
        /**
         * <p>
         * Get the priority value of the command.
         * </p>
         * @return Priority.
         */
        public int getValue() {
            final int OK_PRIORITY = 1000000;
            final int CANCEL_PRIORITY = 2000000;
            final int STOP_PRIORITY = 3000000;
            final int HELP_PRIORITY = 4000000;
            final int ITEM_PRIORITY = 5000000;
            final int SCREEN_PRIORITY = 6000000;
            final int BACK_PRIORITY = 7000000;
            final int EXIT_PRIORITY = 8000000;
            //
            int type = cmd.getCommandType();
            int priority = cmd.getPriority();
            //
            switch (type) {
                case Command.OK:
                    type = OK_PRIORITY;
                    break;
                case Command.BACK:
                    type = BACK_PRIORITY;
                    break;
                case Command.CANCEL:
                    type = CANCEL_PRIORITY;
                    break;
                case Command.STOP:
                    type = STOP_PRIORITY;
                    break;
                case Command.HELP:
                    type = HELP_PRIORITY;
                    break;
                case Command.ITEM:
                    type = ITEM_PRIORITY;
                    break;
                case Command.SCREEN:
                    type = SCREEN_PRIORITY;
                    break;
                case Command.EXIT:
                    type = EXIT_PRIORITY;
                    break;
                default:
                    throw new IllegalArgumentException(
                        "Invalid command type: " + type);
            }
//            priority = priority < 0 ? 0 : priority;
            return type + priority;
        }
    }
}