/*
 * Form.java
 * 10/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * A Form is a Screen that contains an arbitrary mixture of controls: images, 
 * text fields, comboboxes, listboxes, checkboxes etc. In general, any subclass
 * of the Control class may be contained within a form. The implementation 
 * handles layout, traversal, and scrolling. The entire contents of the Form 
 * scrolls together.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
//TODO: implementar auto scroll quando apos o controle ser editado.
public class Form extends Screen {
	/**
	 * <p>
     * Space between the controls.
     * </p>
	 */
	private static final int SPACE_BETWEEN_CONTROLS = OFFSET;
    
    /**
     * <p> 
     * Command Edit.
     * </p>
     */
    private Command cmdEdit;
	
	/**
     * <p>
     * Flag that controls the vertical scroll. It specifies that the form must
     * check if it needs a scroll or not.
     * </p>
	 * <ul>
	 * <li>0 - recalculate flag.</li>
	 * <li>1 - scroll not necessary.</li>
	 * <li>2 - scroll necessary.</li>
	 * </ul>
	 */
	private int vertScrollFlag;
    
    /**
     * <p>
     * Edit command. It is used to trigger the control's edit action.
     * </p>
     */
    private boolean editCmdEnabled;
    
    /**
     * <p>
     * Command listener object.
     * </p>
     */
    private CommandListener cmdListener;
    
    /**
     * <p>
     * Hash that holds all the radio button managers.
     * </p>
     */
    private Hashtable hashRadioManagers;
    
	/**
	 * <p>
	 * Controls the time elapsed since the user kept the pen pressed until it is
	 * released.
	 * </p>
	 */
	private long penHoldTimeElapsed = -1;

    /**
	 * <p>
	 * Y coordinate where the pen was pressed initially for the vertical
	 * scrollbar drag.
	 * </p>
	 */
	private int penDraggedSquareY = -1;

    /**
	 * <p>
	 * Controls area has been pressed or not.
	 * </p>
	 */
	boolean controlsAreaPressed;
	
	/**
     * <p>
     * Control state listener object.
     * </p>
	 */
	ControlStateListener cListener;
	
	/**
     * <p>
     * Control list.
     * </p>
	 */
	Vector controlList;
	
	/**
     * <p>
     * Index of the first visible control.
     * </p>
	 */
	int idxFirstVisibleControl;
	
	/**
     * <p>
     * Index of the selected control.
     * </p>
	 */
	int selectedControlIndex = -1;
	
	/**
	 * <p>
	 * Scrollbar color.
	 * </p>
	 */
	int scrollBarColor;
	
	/**
     * <p>
     * Vertical scrollbar rectangle.
     * </p>
	 */
	Rect vertScrollBarRect;
	
    /**
     * <p>
     * Vertical scrollbar square rectangle.
     * </p>
     */
    Rect vertScrollBarSquareRect;
	
	/**
     * <p>
     * Contructor.
     * </p>
	 * @param title Title.
	 */
	public Form(String title) {
		this(title, null);
	}

	/**
     * <p>
     * Constructor.
     * </p>
	 * @param title Title.
	 * @param controls Controls to be appended to the form.
	 */
	public Form(String title, Control[] controls) {
		super(title);
		controlList = new Vector(10);
		//
		if (controls != null) {
			for (int i = 0; i < controls.length; i++) {
				if (controls[i] != null) {
					append(controls[i]);
				}
			}
		}
		//
		vertScrollBarRect = new Rect();
		vertScrollBarSquareRect = new Rect();
		//
        super.setCommandListener(new CommandListener() {
           public void commandAction(Command c, Displayable d) {
               performCommandAction(c, d);
           } 
        });
        //
		skin.applyFormSkin(this);
	}

	/**
     * <p>
     * Append a given control to the form.
     * </p>
	 * @param control Control.
	 */
	public synchronized void append(Control control) {
		if (control != null) {
			vertScrollFlag = 0; //recalculate flag.
            //
            control.setParent(this);
            controlList.addElement(control);
            //
			if (selectedControlIndex == -1 && !isFocuslessControl(control)) {
				//select the first focusable control.
                setSelectedIndex(controlList.size() -1);
			}
			//
			requestRepaint();
		}
	}
	
	/**
     * <p>
     * Append a given String control to the form.
     * </p>
	 * @param text Text.
	 */
	public synchronized void append(String text) {
		append(new StringControl(text, StringControl.PLAIN));
	}
	
    /**
     * <p>
     * Append a radio button to the form and assign it to a given radio mananger
     * group. All the radio buttons that are assigned to the same group are
     * managed so that only one of them can be set as checked.
     * </p>
	 * @param group Radio manager group.
	 * @param radio Radio control.
	 */
	public synchronized void append(String group, RadioButton radio) {
		if (hashRadioManagers == null) {
			hashRadioManagers = new Hashtable(5);
		}
		//
		group = group.toLowerCase();
		//
		RadioManager rm = (RadioManager)hashRadioManagers.get(group);
		if (rm == null) {
			rm = new RadioManager(group);
			hashRadioManagers.put(group, rm);
		}
		//
		rm.addRadio(radio);
		//
		append(radio);
	}
	
	/**
     * <p>
     * Append a given screen to the form.
     * </p>
	 * @param screen Screen.
	 */
	public synchronized void append(Screen screen) {
		append(new ScreenToControlAdapter(screen));
	}

    /**
     * <p>
     * Set a given control at a given index in the control list. Replacing the 
     * previous control.
     * </p>
	 * @param controlIndex Control index.
	 * @param control New control.
	 * @throws IllegalArgumentException controlIndex is an invalid index.
	 */
	public synchronized void set(int controlIndex, Control control) {
		checkControlsBounds(controlIndex);
		//
		vertScrollFlag = 0; //recalculate flag.
		//
		final Control oldControl = get(controlIndex);
		//
		if (isFocuslessControl(control)
				&& controlIndex == selectedControlIndex) {
			//try to select the next focusable control.
			if (!scrollNextFocusableControl(controlIndex, false)) {
				if (!scrollNextFocusableControl(controlIndex, true)) {
                    setSelectedIndex(-1);
				}
			}
		}
		//
		oldControl.setParent(null);
		//
		removeCommand(oldControl.getDefaultCommand()); //removes the default command.
        //
        if (oldControl instanceof RadioButton) {
            deattachRadioFromManager((RadioButton)oldControl);
        }
		//
		control.setParent(this);
		controlList.setElementAt(control, controlIndex);
		//
		requestRepaint();
	}

    /**
     * <p>
     * Insert a given control at a given position in the control list. It pushes
     * the that control at this given position one position ahead.
     * </p>
	 * @param controlIndex Control index.
	 * @param control New control.
	 * @throws IllegalArgumentException controlIndex is an invalid index.
	 */
	public synchronized void insert(int controlIndex, Control control) {
		checkControlsBounds(controlIndex);
		//
		vertScrollFlag = 0; //recalculate flag.
		//
		control.setParent(this);
		controlList.insertElementAt(control, controlIndex);
		//
		if (controlIndex <= selectedControlIndex) {
			//set the index of the shifted control.
			setSelectedIndex(selectedControlIndex +1);
		}
		//
		requestRepaint();
	}

    /**
     * <p>
     * Deletes the control referenced by the given index.
     * </p>
	 * @param controlIndex Control index.
	 * @throws IllegalArgumentException controlIndex is an invalid index.
	 */
	public synchronized void delete(int controlIndex) {
		checkControlsBounds(controlIndex);
		//
		vertScrollFlag = 0; //recalculate flag.
		//
		final Control delControl = get(controlIndex);
		//
		delControl.setParent(null);
        //
		removeCommand(delControl.getDefaultCommand()); //removes the default command.
		//
        if (delControl instanceof RadioButton) {
            deattachRadioFromManager((RadioButton)delControl);
        }
        //
		controlList.removeElementAt(controlIndex);
		//
		if (controlIndex <= selectedControlIndex) {
			//try to select the next focusable control.
			if (!scrollNextFocusableControl(controlIndex -1, false)) {
				if (!scrollNextFocusableControl(controlIndex +1, true)) {
                    setSelectedIndex(-1);
				}
			}
		}
		//
		requestRepaint();
	}

    /**
     * <p>
     * Deletes all the controls of the form.
     * </p>
	 */
	public synchronized void deleteAll() {
		vertScrollFlag = 0; //recalculate flag.
		//
		Control control = null;
		for (int i = controlList.size() -1; i >= 0; i--) {
			control = (Control)controlList.elementAt(i);
			control.setParent(null);
            //
            if (control instanceof RadioButton) {
                deattachRadioFromManager((RadioButton)control);
            }
            //
            removeCommand(control.getDefaultCommand()); //removes the default command.
		}
		//
		controlList.removeAllElements();
        setSelectedIndex(-1);
		//
		requestRepaint();
	}

    /**
     * <p>
     * Gets the control that is referenced by the given index.
     * </p>
	 * @param controlIndex Control index.
	 * @throws IllegalArgumentException controlIndex is an invalid index.
	 */
	public synchronized Control get(int controlIndex) {
		checkControlsBounds(controlIndex);
		//
		return (Control)controlList.elementAt(controlIndex);
	}
	
    /**
     * <p>
     * Gets the number of controls in the Form.
     * </p>
	 * @return Number of controls.
	 */
	public synchronized int size() {
		return controlList.size();
	}

    /**
     * <p>
     * Get the index of the selected control.
     * </p>
	 * @return Index.
	 */
	public synchronized int getSelectedIndex() {
		return selectedControlIndex;
	}
	
    /**
     * <p>
     * Set as selected the control that is referenced by the given index.
     * </p>
	 * @param controlIndex Control index.
	 * @throws IllegalArgumentException controlIndex is an invalid index.
	 */
	public synchronized void setSelectedIndex(int controlIndex) {
        //
        if (controlIndex == -1) {
            idxFirstVisibleControl = 0;
            selectedControlIndex = -1;
            //
            removeCommand(cmdEdit);
            //
            requestRepaint();
            return;
        }
        //
		checkControlsBounds(controlIndex);
		//
		Control oldControl = null;
        if (selectedControlIndex != -1) {
            oldControl = (Control)controlList.elementAt(selectedControlIndex);
        }
		Control newControl = (Control)controlList.elementAt(controlIndex);
		//
		if (isFocuslessControl(newControl)) {
			return; //the selected control cannot receive focus.
		}
		//
		if (!bodyRect.contains(newControl.getRect())) { //is control visible?
			idxFirstVisibleControl =
				getBestIndexFirstVisibleControl(controlIndex);
		}
		//
		selectedControlIndex = controlIndex;
        //
		//handling the edit commands.
        if (editCmdEnabled) {
    		if (hasCommand(cmdEdit)) {
    			if (!isEditableControl(newControl)) {
        			removeCommand(cmdEdit);
    			}
    		} else {
    			if (isEditableControl(newControl)) {
        			addCommand(cmdEdit);
    			}
    		}
        }
        //
        //handling the default commands.
    	if (oldControl != null && oldControl.getDefaultCommand() != null) {
    		removeCommand(oldControl.getDefaultCommand());
    	}
    	if (newControl.getDefaultCommand() != null) {
    		addCommand(newControl.getDefaultCommand());
    	}
		//
		onFocusChange();
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Set the scrollbar color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setScrollBarColor(int r, int g, int b) {
		scrollBarColor = UIUtil.getHexColor(r, g, b);
	}
	
    /**
     * <p>
     * Sets the control state listener object.
     * </p>
	 * @param cListener Listener object.
	 */
	public void setControlStateListener(ControlStateListener cListener) {
		this.cListener = cListener;
	}
    
    /**
     * <p>
     * Sets enabled the default edit command that is added to the form when an
     * editable control receives de focus.
     * </p>
     * @param enabled Enable or disable the default edit command.
     */
    public synchronized void enableEditCommand(boolean enabled) {
        if (enabled) {
        	if (cmdEdit == null) {
                cmdEdit = 
                	new Command(
                		I18N.getFramework("framework.jme.ui.form.edit"), 
                		Command.OK, 
                		-100);
        	}
            //
            if (selectedControlIndex != -1
            		&& isEditableControl(get(selectedControlIndex))) {
            	if (!hasCommand(cmdEdit)) {
                    addCommand(cmdEdit);
            	}
            }
        } else {
        	if (cmdEdit != null) {
        		if (hasCommand(cmdEdit)) {
        			removeCommand(cmdEdit);
        		}
        		cmdEdit = null;
        	}
        }
        //
        editCmdEnabled = enabled;
    }
    
    /**
     * @inheritDoc
     * @see javax.microedition.lcdui.Displayable#setCommandListener(javax.microedition.lcdui.CommandListener)
     */
    public void setCommandListener(CommandListener listener) {
        cmdListener = listener;
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Screen#getCommandListenerObject()
     */
    public CommandListener getCommandListenerObject() {
        return cmdListener;
    }
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#showNotify()
	 */
	protected void showNotify() {
		super.showNotify();
		//
		for (int i = controlList.size() -1; i >= 0; i--) {
			((Control)controlList.elementAt(i)).showNotify();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#hideNotify()
	 */
	protected void hideNotify() {
		super.hideNotify();
		//
		for (int i = controlList.size() -1; i >= 0; i--) {
			((Control)controlList.elementAt(i)).hideNotify();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#sizeChanged(int, int)
	 */
	protected void sizeChanged(int w, int h) {
		super.sizeChanged(w, h);
		//
		for (int i = controlList.size() -1; i >= 0; i--) {
			((Control)controlList.elementAt(i)).sizeChanged(w, h);
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w,
		int h) {
		final int size = controlList.size();
		//
		if (size == 0) {
			return; //there is no control to be drawn.
		}
		//
		if (hasVerticalScrollBar()) {
			vertScrollBarRect.w = hasPointerEvents() ? 7 : 5;
			vertScrollBarRect.x = x + w - vertScrollBarRect.w;
			vertScrollBarRect.y = y;
			vertScrollBarRect.h = h;
			//
			g.setClip(
				vertScrollBarRect.x,
				vertScrollBarRect.y,
				vertScrollBarRect.w,
				vertScrollBarRect.h);
			drawVerticalScrollbar(
				g,
				vertScrollBarRect.x,
				vertScrollBarRect.y,
				vertScrollBarRect.w,
				vertScrollBarRect.h);
			//
			bodyRect.w -= vertScrollBarRect.w;
			w = bodyRect.w;
		} else {
			vertScrollBarRect.x = 0;
			vertScrollBarRect.y = 0;
			vertScrollBarRect.w = 0;
			vertScrollBarRect.h = 0;
			//
			bodyRect.w = w;
		}
		//
		Control control = null;
		final int bodyBound = y + h; //bottom bound of the visible area.
		final int ox; //original x value;
		int cw;
		int ch;
		int lh;
		//
		//adjusting the body's margins.
		x += OFFSET;
		y += OFFSET;
		w -= OFFSET * 2;
		h -= OFFSET;
		ox = x;
		//
		for (int i = 0; i < size; i++) {
			control = (Control)controlList.elementAt(i);
			lh = 0;
			//
			x = ox; //restoring x value.
			//
			if (i < idxFirstVisibleControl || y >= bodyBound) {
				control.setPosition(-1, -1); //setting the controls out of visible area bounds.
				continue;
			}
			//
			if (!control.isVisible()) {
				continue;
			}
			//
			cw = control.getWidth();
			if (cw > w) {
				//in case of the control width is greater than the screen width.
				cw = w - OFFSET -1;
				control.setWidth(cw);
			}
			//
			//calculating the offset value of the alignment operation.
			if ((control.layout & UIUtil.ALIGN_CENTER) != 0) {
				x += (w - cw) / 2; //offset for center alignment.
			} else if ((control.layout & UIUtil.ALIGN_RIGHT) != 0) {
				x += w - cw; //offset for right alignment.
			}
			//
			if (control.isLabelEntered()) {
				lh = control.getLabelHeight();
				//defining the clipe area for the control's label.
				if (y + lh > bodyBound) {
					g.setClip(x, y, w +1, bodyBound - y); //avoding the control's label to cross the visible area bounds.
				} else {
					g.setClip(x, y, w +1, lh +1);
				}
				//
				control.drawLabel(g, x, y, w, lh); //drawing the control label.
				y += lh + SPACE_BETWEEN_CONTROLS;
			} else {
				y += SPACE_BETWEEN_CONTROLS;
			}
			//
			if ((control.layout & UIUtil.LAYOUT_VEXPAND) != 0) {
				control.setHeight((bodyRect.y + h) - y); //expands the control's height to fill the available screen space.
			}
			//
			ch = control.getHeight();
			if (ch + lh + SPACE_BETWEEN_CONTROLS > h) {
				//in case of the control height is greater than the screen height.
				ch = h - SPACE_BETWEEN_CONTROLS - lh - OFFSET -1;
				control.setHeight(ch);
			}
			//
			control.setPosition(x, y);
			//
			if (y >= bodyBound) { //is there no space for the body?
				continue;
			}
			//
			//defining the clipe area for the control's body.
			if (y + ch > bodyBound) {
				g.setClip(x, y, cw +1, bodyBound - y); //avoding the control to cross the visible area bounds.
			} else {
				g.setClip(x, y, cw +1, ch +1);
			}
			//
            control.drawBackground(g, x, y, cw, ch); //drawing the control's background.
			control.drawBody(g, x, y, cw, ch); //drawing the control's body.
			//
			if (i == selectedControlIndex && !control.isFocusless()) {
				control.drawFocus(g, x, y, cw, ch); //drawing the focus.
			}
            //
			y += ch + SPACE_BETWEEN_CONTROLS;
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyHold(int)
	 */
	protected void keyHold(int keyCode) {
		if (selectedControlIndex != -1) {
			Control control = get(selectedControlIndex);
			if (control.isEnabled() && control.isTrappingEvent()) {
				control.keyHold(keyCode);
				return;
			}
		}
		//
		keyCode = getGameAction(keyCode);
		//
		if (keyCode == UP) {
			scrollNextFocusableControl(selectedControlIndex, true);
		} else if (keyCode == DOWN) {
			scrollNextFocusableControl(selectedControlIndex, false);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyDown(int)
	 */
	protected void keyDown(int keyCode) {
		Control control = null;
		//
		if (selectedControlIndex != -1) {
			control = get(selectedControlIndex);
			if (control.isEnabled() && control.isTrappingEvent()) {
				control.keyDown(keyCode);
				return;
			}
		}
		//
		keyCode = getGameAction(keyCode);
		//
		if (keyCode == UP) {
			scrollNextFocusableControl(selectedControlIndex, true);
		} else if (keyCode == DOWN) {
			scrollNextFocusableControl(selectedControlIndex, false);
		} else if (!editCmdEnabled && keyCode == FIRE && control != null
				&& isEditableControl(control)) {
			control.edit();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyUp(int)
	 */
	protected void keyUp(int keyCode) {
		if (selectedControlIndex != -1) {
			Control control = get(selectedControlIndex);
			if (control.isEnabled() && control.isTrappingEvent()) {
				control.keyUp(keyCode);
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penUp(int, int)
	 */
	protected void penUp(int x, int y) {
		if (selectedControlIndex != -1) {
			Control control = get(selectedControlIndex);
			if (control.isEnabled() && control.isTrappingEvent()) {
				control.penUp(x, y);
				return;
			}
		}
		//
		if (bodyRect.contains(x, y) && controlsAreaPressed) {
			final int index = getControlIndex(x, y);
			if (index != -1) {
				if (index != selectedControlIndex) {
	                setSelectedIndex(index);
				}
				//
				if (System.currentTimeMillis() - penHoldTimeElapsed >= 250) {
					Control c = get(selectedControlIndex);
					if (isEditableControl(c)) {
						c.edit();
					}
				}
				penHoldTimeElapsed = -1;
                //
				requestRepaint();
			}
		} else if (!vertScrollBarSquareRect.contains(x, y) &&
				   vertScrollBarRect.contains(x, y)) {
			if (y < vertScrollBarSquareRect.y) {
				scrollNextFocusableControl(idxFirstVisibleControl, true);
			} else {
				scrollNextFocusableControl(getLastVisibleControl(), false);
			}
		}
		//
		//reset control variables.
		penDraggedSquareY = -1;
		controlsAreaPressed = false;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDragged(int, int)
	 */
	protected void penDragged(int x, int y) {
		if (selectedControlIndex != -1) {
			Control control = get(selectedControlIndex);
			if (control.isEnabled() && control.isTrappingEvent()) {
				control.penDragged(x, y);
				return;
			}
		}
		//
		if (vertScrollBarSquareRect.contains(x, y)) {
			penDraggedSquareY = y;
		} else if (vertScrollBarRect.contains(x, y)) {
			if (penDraggedSquareY != -1) {
			    //pen left the square area but it is still in the scrollbar area.
				if (y < penDraggedSquareY) {
					scrollNextFocusableControl(idxFirstVisibleControl, true);
				} else {
					scrollNextFocusableControl(getLastVisibleControl(), false);
				}
				penDraggedSquareY = -1;
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDown(int, int)
	 */
	protected void penDown(int x, int y) {
		if (selectedControlIndex != -1) {
			Control control = get(selectedControlIndex);
			if (control.isEnabled() && control.isTrappingEvent()) {
				control.penDown(x, y);
				return;
			}
		}
		//
		if (bodyRect.contains(x, y)) {
			controlsAreaPressed = true;
			penHoldTimeElapsed = System.currentTimeMillis();
		}
	}
	
	/**
	 * <p>
	 * Trigger the event notifying that the focus has been changed.
	 * </p>
	 */
	protected void onFocusChange() {
	}
	
    /**
     * <p>
     * This method is called when a given control notifies that his internal
     * state has been changed.
     * </p>
	 * @param control Control.
	 */
	void notifyStateChanged(Control control) {
		if (cListener != null) {
			cListener.controlStateChanged(control);
		}
	}
    
    /**
     * <p>
     * This method is called when a command is triggered.
     * </p>
     * @param c Command.
     * @param d Displayable screen.
     */
    void performCommandAction(Command c, Displayable d) {
        if (c == cmdEdit) {
            get(selectedControlIndex).edit();
        } else {
            if (cmdListener != null) {
                cmdListener.commandAction(c, d);
            }
        }
    }
	
    /**
     * <p>
     * Draw the vertical scroll bar.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
	 */
	protected void drawVerticalScrollbar(Graphics g, int x, int y, int w,
		int h) {
		UIUtil.drawVerticalScrollbar(
			g, 
			x, 
			y, 
			w, 
			h, 
			controlList.size(),
			getVisibleControlCount(idxFirstVisibleControl),
			selectedControlIndex, 
			vertScrollBarSquareRect,
			scrollBarColor,
			skin.getVerticalScrollBarStyle());
	}
	
    /**
     * <p>
     * Scroll the controls in the screen.
     * </p>
	 * @param startIdx Start index.
	 * @param goup Scroll up or down.
	 * @return
	 */
	boolean scrollNextFocusableControl(int startIdx, boolean goup) {
		startIdx += goup ? -1 : +1;
		//
		if (startIdx < 0 || startIdx == controlList.size()) {
			return false;
		}
		//
		final Control control = (Control)controlList.elementAt(startIdx);
		//
		if (!isFocuslessControl(control)) {
			setSelectedIndex(startIdx);
			return true;
		}
        //
		if (startIdx == 0) { //is the first control?
            //in case of the first control is a foculess one, it forces a
            //scrolling up.
			idxFirstVisibleControl = 0;
            //
            requestRepaint();
		} else if (startIdx == controlList.size() -1) { //is the last control?
            //in case of the last control is a foculess one, it forces a
            //scrolling down.
            idxFirstVisibleControl = getBestIndexFirstVisibleControl(startIdx);
            //
            requestRepaint();
        }
		//
		return scrollNextFocusableControl(startIdx, goup);
	}
	
	/**
	 * <p>
	 * Verify if the form needs a vertical scrollbar.
	 * <p>
	 * @return Need or not.
	 */
	boolean hasVerticalScrollBar() {
		if (vertScrollFlag == 0) { //need recalculate?
			if (getVisibleControlCount(0) != controlList.size()) {
				vertScrollFlag = 2; //need scroll.
				return true;
			} else {
				vertScrollFlag = 1; //does not need scroll.
				return false;
			}
		} else {
			return vertScrollFlag == 2 ? true : false;
		}
	}
	
    /**
     * <p>
     * Gets the best index to be the first visible control based on a given
     * control index.
     * </p>
	 * @param controlIndex Control index.
	 * @return Best index.
	 */
	int getBestIndexFirstVisibleControl(int controlIndex) {
		final int cio = controlIndex;
		Control control = null;
		int h = bodyRect.h;
		//
		for (; controlIndex >= 0; controlIndex--) {
			control = (Control)controlList.elementAt(controlIndex);
			//
			if (!control.isVisible()) {
				continue; //invisible control is not counted.
			}
			//
			if (control.isLabelEntered()) {
				h -= control.getLabelHeight() + SPACE_BETWEEN_CONTROLS;
			} else {
                h -= SPACE_BETWEEN_CONTROLS;
            }
			h -= control.getHeight() + SPACE_BETWEEN_CONTROLS;
			//
			if (h < 0) {
				return controlIndex != cio ? controlIndex +1 : controlIndex;
			}
		}
        //
		return controlIndex +1;
	}
	
	/**
	 * <p>
	 * Gets the index of the control that is positioned at the given 
     * coordinates.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Control index.
	 */
	int getControlIndex(int x, int y) {
		final int size = controlList.size();
		Control control = null;
		//
		for (int i = idxFirstVisibleControl; i < size; i++) {
			control = (Control)controlList.elementAt(i);
			//
			if (control.getRect().contains(x, y)) {
				return i;
			}
		}
        //
		return -1;
	}

    /**
     * <p>
     * Gets the count of visible controls from a given control index.
     * </p>
	 * @param startIndex Control index.
	 * @return Count.
	 */
	int getVisibleControlCount(int startIndex) {
		final int size = controlList.size();
		Control control = null;
		int sumH = 0;
		int count = 0; //visible controls count.
		//
		for (; startIndex < size; startIndex++) {
			control = (Control)controlList.elementAt(startIndex);
			//
			if (!control.isVisible()) {
				continue; //invisible control is not counted.
			}
			//
			sumH += control.getHeight() + SPACE_BETWEEN_CONTROLS;
			if (control.isLabelEntered()) {
				sumH += control.getLabelHeight() + SPACE_BETWEEN_CONTROLS;
			} else {
                sumH += SPACE_BETWEEN_CONTROLS;
            }
			//
			if (sumH > bodyRect.h) { //control is not totally visible?
				break;
			}
			//
			count++;
		}
		//
		return count;
	}
	
    /**
     * <p>
     * Gets the index of the last visible control.
     * </p>
	 * @return Index.
	 */
	int getLastVisibleControl() {
		final int size = controlList.size();
		Control control = null;
		int lastIndex = -1;
		//
		for (int i = idxFirstVisibleControl; i < size; i++) {
			control = (Control)controlList.elementAt(i);
			//
			if (!control.isVisible()) {
				continue;
			}
			if (bodyRect.contains(control.getRect())) {
				lastIndex = i;
			} else {
				break;
			}
		}
        //
		return lastIndex;
	}
	
    /**
     * <p>
     * Verifies if a given control is focusless.
     * </p>
	 * @param control Control.
	 * @return Focusless or not.
	 */
	boolean isFocuslessControl(Control control) {
		return /*control.isFocusless() || */!control.isVisible();
	}
	
    /**
     * <p>
     * Verifies if a given control is editable.
     * </p>
	 * @param control Control.
	 * @return Editable or not.
	 */
	boolean isEditableControl(Control control) {
		return control.isEnabled() && control.isEditable()
			&& !control.isTrappingEvent();
	}

	/**
     * <p>
     * Method that is called when a given control notifies that its state has
     * been changed.
     * </p>
     * @param stateKey State key.
	 * @param control Control.
	 */
	void onControlStateChanges(String stateKey, Control control) {
		final int controlIndex = controlList.indexOf(control);
		//
		if (selectedControlIndex == controlIndex) {
			if ("visibility".equals(stateKey)) {
				vertScrollFlag = 0; //recalculate flag.
				//
				//try to select the next focusable control.
				if (!scrollNextFocusableControl(controlIndex, false)) {
					if (!scrollNextFocusableControl(controlIndex, true)) {
	                    setSelectedIndex(-1);
					}
				}
			} else if ("enabled".equals(stateKey)) {
				setSelectedIndex(controlIndex);
			}
		}
	}
	
    /**
     * <p>
     * Dettaches the radio manager of the given radio button.
     * </p>
	 * @param radio Radio.
	 */
	void deattachRadioFromManager(RadioButton radio) {
        RadioManager rm = radio.getManager();
        //
        if (rm != null) {
            rm.removeRadio(radio);
            //
            if (hashRadioManagers != null) {
                //
                String group = rm.getName().toLowerCase();
                RadioManager m = (RadioManager) hashRadioManagers.get(group);
                //
                if (m.size() == 0) {
                    hashRadioManagers.remove(group);
                }
                //
                if (hashRadioManagers.size() == 0) {
                    hashRadioManagers = null;
                }
            }
        }
	}
    
    /**
     * <p>
     * Method that is called when a given control notifies that its dimensions
     * has been changed.
     * </p>
     * @param control Control.
     */
    void notifySizeChanged(Control control) {
        setSelectedIndex(controlList.indexOf(control));
    }
	
	/**
	 * <p>
	 * Check if the given index respect the controls list bounds.
	 * </p>
	 * @param controlIndex Control index.
	 * @throws IllegalArgumentException Invalid index.
	 */
	void checkControlsBounds(int controlIndex) {
		if (controlIndex < 0 || controlIndex >= controlList.size()) {
			throw new IllegalArgumentException(
			    "Control index must be between 0 and " +
			        (controlList.size() -1) + ": " + controlIndex);
		}
	}
}