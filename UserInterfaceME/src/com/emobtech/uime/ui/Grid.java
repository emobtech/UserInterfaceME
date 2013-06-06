/*
 * Grid.java
 * 28/02/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.util.QSort;
import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a grid.
 * </p>
 * 
 * <pre>
 * Grid g = new Grid("Phone Book");
 * g.addColumn("Name", 50, UIUtil.ALIGN_LEFT, Grid.STRING);
 * g.addColumn("Mobile", 50, UIUtil.ALIGN_CENTER, Grid.STRING);
 * g.append(new Object[] {"Jorge Adams", "+1-202-203-8765"}, null);
 * g.append(new Object[] {"Michael James", "+1-402-802-1832"}, null);
 * </pre>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Grid extends List {
	/**
	 * <p>
	 * String column type. <b>Values for this kind of column must be String
	 * objects.</b>
	 * </p>
	 */
	public static final int STRING = 0;

	/**
	 * <p>
	 * Numeric column type. <b>Values for this kind of column must be Integer
	 * or Long objects.</b>
	 * </p>
	 */
	public static final int NUMERIC = 1;

	/**
	 * <p>
	 * Date column type. <b>Values for this kind of column must be Date
	 * objects.</b>
	 * </p>
	 */
	public static final int DATE = 3;

	/**
	 * <p>
	 * Object column type. <b>Values for this kind of column can be of any
	 * type.</b>
	 * </p>
	 */
	public static final int OBJECT = 4;

	/**
	 * <p>
	 * Controls the time elapsed since the user kept the pen pressed until it is
	 * released.
	 * </p>
	 */
	private long penHoldTimeElapsed = -1;
	
	/**
	 * <p>
	 * Width which the column border is dragged.
	 * </p>
	 */
	private int penDraggedWidth = -1;

	/**
	 * <p>
	 * Columns header is pressed or not.
	 * </p>
	 */
	private boolean isColsHeaderPressed;
	
	/**
	 * <p>
	 * Display live column resizing.
	 * </p>
	 */
	private boolean liveResizing;

	/**
	 * <p>
	 * Triangle width used to simbolyze sorted column.
	 * </p>
	 */
	final int triangleSize = 10;

	/**
	 * <p>
	 * Columns list.
	 * </p>
	 */
	Vector columns;

    /**
	 * <p>
	 * Columns header color.
	 * </p>
	 */
	protected int columnsHeaderColor;
	
	/**
	 * <p>
	 * Columns header font color.
	 * </p>
	 */
	int columnsHeaderFontColor;

	/**
	 * <p>
	 * Selected column index.
	 * </p>
	 */
	int selectedColumnIndex;
	
	/**
	 * <p>
	 * Cursor color.
	 * </p>
	 */
	int cursorColor;
	
	/**
	 * <p>
	 * Draw grid grade.
	 * </p>
	 */
	boolean drawGrade;
	
	/**
	 * <p>
	 * Grade color.
	 * </p>
	 */
	int gradeColor;
	
	/**
	 * <p>
	 * First visible column index.
	 * </p>
	 */
	int idxFirstVisibleColumn;
	
	/**
	 * <p>
	 * Odd line color.
	 * </p>
	 */
	int oddLineColor = -1;
	
	/**
	 * <p>
	 * Even line color.
	 * </p>
	 */
	int evenLineColor = -1;
	
	/**
	 * <p>
	 * Last sorted column index.
	 * </p>
	 */
	int lastSortedColumn = -1;
	
	/**
	 * <p>
	 * Var responsible for ordering orientation.
	 * </p>
	 */
	boolean ascendingOrder = true;
	
	/**
	 * <p>
	 * Hold the wrapper objects of the customized cells.
	 * </p>
	 */
	Vector listCustomCells;
	
	/**
	 * <p>
	 * Columns header rectangle.
	 * </p>
	 */
	Rect columnsHeaderRect;
	
	/**
	 * <p>
	 * Columns header visibility state.
	 * </p>
	 */
	boolean colsHeaderVisible = true;
	
	/**
	 * <p>
	 * Cursor visibility state.
	 * </p>
	 */
	boolean drawCursor = true;
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title Grid title.
	 */
	public Grid(String title) {
		super(title);
		columns = new Vector(2);
		cursorColor = negativeItemFontColor;
		drawGrade = true;
		columnsHeaderRect = new Rect();
		//
       	skin.applyGridSkin(this);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#append(java.lang.Object, javax.microedition.lcdui.Image)
	 */
	public synchronized void append(Object values, Image img) {
		super.append(createGridItem(values), img);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#set(int, java.lang.Object, javax.microedition.lcdui.Image)
	 */
	public synchronized void set(int itemIndex, Object values, Image img) {
		super.set(itemIndex, createGridItem(values), img);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#insert(int, java.lang.Object, javax.microedition.lcdui.Image)
	 */
	public synchronized void insert(int itemIndex, Object values, Image img) {
		super.insert(itemIndex, createGridItem(values), img);
	}
	
	/**
	 * <p>
	 * Set a given item value in a given item index and column index.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @param value Item value.
	 * @throws IllegalArgumentException itemIndex or columnIndex is an invalid
	 * 									index.
	 */
	public synchronized void set(int itemIndex, int columnIndex, Object value) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		//
		Object[] values = (Object[])items.elementAt(itemIndex);
		//
		if (values == null || values.length < columns.size()) {
			//in case of the array os items is smaller than columns count.
			Object[] aux = new Object[columns.size()];
			if (values != null) {
				System.arraycopy(values, 0, aux, 0, values.length);
			}
			values = aux;
			items.setElementAt(values, itemIndex);
		}
		values[columnIndex] = value;
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Get an item value that is placed in a given item index and column index.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @return Item value.
	 * @throws IllegalArgumentException itemIndex or columnIndex is an invalid
	 * 									index.
	 */
	public synchronized Object get(int itemIndex, int columnIndex) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		//
		Object[] values = (Object[])items.elementAt(itemIndex);
		if (values != null && columnIndex < values.length) {
			return values[columnIndex];
		} else {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Add a column into the grid.
	 * </p>
	 * @param name Column name.
	 * @param percWidth Column percentage (0-100 based on the screen size).
	 * @param align Column alignment.
	 * @param type Column type.
	 * @throws IllegalArgumentException Invalid column percentage.
	 * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
	 * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
	 * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
	 * @see br.framework.jme.ui.Grid#STRING
	 * @see br.framework.jme.ui.Grid#NUMERIC
	 * @see br.framework.jme.ui.Grid#DATE
	 * @see br.framework.jme.ui.Grid#OBJECT
	 */
	public synchronized void addColumn(String name, int percWidth, int align,
		int type) {
		if (percWidth < 0 || percWidth > 100) {
			throw new IllegalArgumentException(
				"Width percentage must be between 0 and 100: " + percWidth);
		}
		Column col = new Column();
		col.name = name;
		col.width = (int)((getWidth() * percWidth) / 100);
		col.align = align;
		col.type = type;
		col.percentage = percWidth;
		columns.addElement(col);
	}
	
	/**
	 * <p>
	 * Remove a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public synchronized void removeColumn(int columnIndex) {
		checkColumnsBounds(columnIndex);
		//
		columns.removeElementAt(columnIndex);
	}
	
	/**
	 * <p>
	 * Get the column count.
	 * </p>
	 * @return Count.
	 */
	public synchronized int getColumnCount() {
		return columns.size();
	}
	
	/**
	 * <p>
	 * Get the selected column index.
	 * </p>
	 * @return Index.
	 */
	public synchronized int getSelectedColumn() {
		return selectedColumnIndex;
	}
	
	/**
	 * <p>
	 * Set columns header color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setColumnsHeaderColor(int r, int g, int b) {
		columnsHeaderColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set columns header font color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setColumnsHeaderFontColor(int r, int g, int b) {
		columnsHeaderFontColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the header color of a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void setColumnHeaderColor(int columnIndex, int r, int g, int b) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		col.headerColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the header font color of a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void setColumnHeaderFontColor(int columnIndex, int r, int g, int b) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		col.headerFontColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set the color of a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void setColumnColor(int columnIndex, int r, int g, int b) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		col.bodyColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set the font color of a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void setColumnFontColor(int columnIndex, int r, int g, int b) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		col.bodyFontColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set the color of a given cell.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @throws IllegalArgumentException Item index is invalid.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void setCellColor(int itemIndex, int columnIndex, int r, int g,
		int b) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		//
		Cell cell = findCustomCell(itemIndex, columnIndex);
		//
		if (cell == null) {
			addCustomCell(cell = new Cell(itemIndex, columnIndex));
		}
		//
		cell.bodyColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set the font color of a given cell.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @throws IllegalArgumentException Item index is invalid.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void setCellFontColor(int itemIndex, int columnIndex, int r, int g,
		int b) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		//
		Cell cell = findCustomCell(itemIndex, columnIndex);
		//
		if (cell == null) {
			addCustomCell(cell = new Cell(itemIndex, columnIndex));
		}
		//
		cell.bodyFontColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * @see br.framework.jme.ui.List#setSelectionColor(int, int, int)
	 */
	public void setSelectionColor(int r, int g, int b) {
		super.setSelectionColor(r, g, b);
		cursorColor = UIUtil.getNegativeColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set odd line color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setOddLineColor(int r, int g, int b) {
		oddLineColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set even line color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setEvenLineColor(int r, int g, int b) {
		evenLineColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set the grade color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setGradeColor(int r, int g, int b) {
		gradeColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the drawing of the grid grade enabled.
	 * </p>
	 * @param enabled Enable or disable.
	 */
	public void setGrade(boolean enabled) {
		drawGrade = enabled;
	}
	
	/**
	 * <p>
	 * Set live column resizing enabled.
	 * </p>
	 * @param enabled Enable or disable.
	 */
	public void setLiveColumnResizing(boolean enabled) {
		liveResizing = enabled;
	}
	
	/**
	 * <p>
	 * Sets the columns header visible or not.
	 * </p>
	 * @param visible Visible or not.
	 */
	public void setColumnsHeaderVisible(boolean visible) {
		colsHeaderVisible = visible;
		//
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Set the drawing of the cursor enabled.
	 * </p>
	 * @param enabled Enable or disable.
	 */
	public void setCursor(boolean enabled) {
		drawCursor = enabled;
		//
		requestRepaint();
	}

	/**
	 * <p>
	 * Maximize a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void maximizeColumn(int columnIndex) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		if (!col.isMaximized || columnIndex == lastSortedColumn) {
			col.prevWidth = col.width;
			col.width = getBestMaxWidth(columnIndex);
			//positioning the maximized column.
			while (!isColumnVisible(selectedColumnIndex)) {
				idxFirstVisibleColumn++;
			}
			col.isMaximized = true;
			requestRepaint();
		}
	}

	/**
	 * <p>
	 * Minimize a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public void minimizeColumn(int columnIndex) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		if (col.isMaximized) {
			int bestWidth = getBestMaxWidth(columnIndex);
			if (col.prevWidth > bestWidth) {
				col.width = bestWidth;
			} else {
				col.width = col.prevWidth;
			}
			col.isMaximized = false;
			//positioning the minimized column.
//			while (isColumnVisible(selectedColumnIndex)) {
//				if (idxFirstVisibleColumn == 0) {
//					break;
//				}
//				idxFirstVisibleColumn--;
//			}
//			if (idxFirstVisibleColumn > 0) {
//				idxFirstVisibleColumn++;
//			}
			requestRepaint();
		}
	}

	/**
	 * <p>
	 * Set a given column as editable.
	 * </p>
	 * @param columnIndex Column index.
	 * @param enabled Enable or disable.
	 * @throws IllegalStateException Column index is invalid.
	 */
	public void setColumnAsEditable(int columnIndex, boolean enabled) {
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		if (col.type == OBJECT) {
			throw new IllegalStateException(
				"OBJECT type column cannot be set as editable: " + columnIndex);
		}
		//
		col.isEditable = enabled;
	}
	
	/**
	 * <p>
	 * Set a given cell as editable.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @param enabled Enable or disable.
	 * @throws IllegalArgumentException Item index or column index is invalid.
	 * @throws IllegalStateException Column index is of a OBJECT type column.
	 */
	public void setCellAsEditable(int itemIndex, int columnIndex,
		boolean enabled) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		//
		Column col = (Column)columns.elementAt(columnIndex);
		if (enabled && col.type == OBJECT) {
			throw new IllegalStateException(
				"OBJECT type column cannot be set as editable: " + columnIndex);
		}
		//
		Cell cell = findCustomCell(itemIndex, columnIndex);
		//
		if (cell == null) {
			addCustomCell(cell = new Cell(itemIndex, columnIndex));
		}
		//
		cell.isEditable = enabled;
	}
	
	/**
	 * <p>
	 * Open the selected cell for edition.
	 * </p>
	 */
	public void editSelectedCell() {
		editCell(getSelectedIndex(), getSelectedColumn());
	}
	
	/**
	 * <p>
	 * Open a given cell for edition.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @throws IllegalArgumentException Item index or column index is invalid.
	 * @throws IllegalStateException Cell is not editable or columns is a OBJECT
	 *                               type column.
	 */
	public void editCell(final int itemIndex, final int columnIndex) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		if (!isEditableCell(itemIndex, columnIndex)) {
			throw new IllegalStateException(
				"The cell "+itemIndex+"x"+columnIndex+" is not editable.");
		}
		//
		final Column col = (Column)columns.elementAt(columnIndex);
		final Object value = get(itemIndex, columnIndex);
		final TextBox txtBox =
			new TextBox(
				"Novo Valor:",
				value != null ?
					(col.type == DATE ?
						formatCell(columnIndex, value) : value.toString()) :
					null,
				col.type == DATE ? 10 : (col.type != STRING ? 20 : 500),
				getInputConstraint(columnIndex));
		//
		CommandListener obj = new CommandListener() {
			Command cmdOK;
			Command cmdCancel;
			Command cmdAlertOK;
			{
				cmdOK = new Command("OK", Command.OK, 0);
				cmdCancel = new Command("Cancelar", Command.CANCEL, 0);
				txtBox.addCommand(cmdOK);
				txtBox.addCommand(cmdCancel);
			}
			public void commandAction(Command c, Displayable d) {
				if (c == cmdOK) {
					String msgError;
					Object newValue = null;
					try {
						newValue = getValue(txtBox.getString(), columnIndex);
						msgError =
							validateCell(itemIndex, columnIndex, newValue);
					}catch (NumberFormatException e) {
						msgError = "Valor inválido!";
						if (col.type == DATE) {
							msgError +=
								"\nExemplos: 1/02/2007, 1/1/2007 ou 12/1/2007."; 
						} else if (col.type != STRING) {
							msgError +=
								"\nO valor está fora da faixa."; 
						}
					}
					//
					if (msgError == null) {
						set(itemIndex, columnIndex, newValue);
						onCellEdit(itemIndex, columnIndex, newValue);
						MIDlet.getDisplayInstance().setCurrent(Grid.this);
					} else {
						//display error alert.
						cmdAlertOK = new Command("OK", Command.OK, 0);
						Alert errorAlert =
							new Alert("Erro", msgError, null,AlertType.WARNING);
						errorAlert.setTimeout(Alert.FOREVER);
						errorAlert.addCommand(cmdAlertOK);
						errorAlert.setCommandListener(this);
						MIDlet.getDisplayInstance().setCurrent(errorAlert);
					}
				} else if (c == cmdCancel) {
					MIDlet.getDisplayInstance().setCurrent(Grid.this);
				} else if (c == cmdAlertOK) {
					//call the edit screen again.
					editCell(itemIndex, columnIndex);
				}
			}
		};
		txtBox.setCommandListener(obj);
		MIDlet.getDisplayInstance().setCurrent(txtBox);
	}
	
	/**
	 * <p>
	 * Verify if a given cell is editable.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @return Editable or not.
	 * @throws IllegalArgumentException Item index or column index is invalid.
	 */
	public boolean isEditableCell(int itemIndex, int columnIndex) {
		checkItemsBounds(itemIndex);
		checkColumnsBounds(columnIndex);
		//
		Cell cell = findCustomCell(itemIndex, columnIndex);
		//
		if (cell != null) {
			return cell.isEditable;
		} else {
			return ((Column)columns.elementAt(columnIndex)).isEditable;
		}
	}

	/**
	 * <p>
	 * Sort the items of the first column.
	 * </p>
	 */
	public synchronized void sort() {
		sort(0);
	}

	/**
	 * <p>
	 * Sort the items of a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @throws IllegalArgumentException Column index is invalid.
	 */
	public synchronized void sort(int columnIndex) {
		checkColumnsBounds(columnIndex);
		//
		if (lastSortedColumn != columnIndex) {
			ascendingOrder = true;
		} else {
			ascendingOrder = !ascendingOrder;
		}
		lastSortedColumn = columnIndex;
		//
		sort(new GridQSort(columnIndex, ascendingOrder));
		//
        maximizeColumn(columnIndex);
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w,
		int h) {
		columnsHeaderRect.x = x;
		columnsHeaderRect.y = y;
		columnsHeaderRect.w = w;
		//
		if (colsHeaderVisible) {
			columnsHeaderRect.h = getItemHeight();
	        drawColumnsHeader(g, x, y, columnsHeaderRect.w,columnsHeaderRect.h);
		} else {
			columnsHeaderRect.h = 0;
		}
        //
        bodyRect.y += columnsHeaderRect.h;
        bodyRect.h -= columnsHeaderRect.h;
		super.drawBody(g, x, bodyRect.y, w, bodyRect.h);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyDown(int)
	 */
	protected void keyDown(int keyCode) {
		super.keyDown(keyCode);
		keyCode = getGameAction(keyCode);
		if (keyCode == LEFT) {
			moveCursorLeft();
		} else if (keyCode == RIGHT) {
			moveCursorRight();
		} else if (keyCode == FIRE) {
			if (isEditableCell(getSelectedIndex(), getSelectedColumn())) {
				editSelectedCell();
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDragged(int, int)
	 */
	protected void penDragged(int x, int y) {
		super.penDragged(x, y);
		//
		int dragCol;
		if (isColumnsHeaderArea(x, y) &&
			(dragCol = getPressedColumnToDrag(x, y)) != -1) {
			//resing the selected column.
			if (penDraggedWidth != -1) {
				if (selectedColumnIndex != dragCol) {
					return; //avoid to invade the are of other close column.
				}
				int diff = x - penDraggedWidth;
				//
				Column col = (Column)columns.elementAt(dragCol);
				col.width += diff;
				if (col.width < 0) {
					col.width = 0;
				}
				//
				if (liveResizing) {
					requestRepaint();
				}
			} else {
				selectedColumnIndex = dragCol;
			}
			penDraggedWidth = x;
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDown(int, int)
	 */
	protected void penDown(int x, int y) {
		super.penDown(x, y);
		if (itemsAreaPressed) {
			penHoldTimeElapsed = System.currentTimeMillis();
		} else if (isColumnsHeaderArea(x, y)) {
			isColsHeaderPressed = true;
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penUp(int, int)
	 */
	protected void penUp(int x, int y) {
		final boolean isItemAreaPressed = itemsAreaPressed;
		//
		super.penUp(x, y);
		//
		if (penDraggedWidth != -1) {
			penDraggedWidth = -1; //columns have just been resized.
			if (!liveResizing) {
				requestRepaint(); //refresh the resized column after draggring.
			}
			return;
		}
		//
		if (isItemsArea(x, y) && isItemAreaPressed) {
			int selCol = getPressedColumn(x, y);
			if (selCol != -1) {
				selectedColumnIndex = selCol;
				if (System.currentTimeMillis() - penHoldTimeElapsed >= 250) {
					//user kept the stylus pressed during at least 250 mili.
					if (isEditableCell(
							getSelectedIndex(), getSelectedColumn())) {
						//opening edit box.
						editSelectedCell();
					}
				}
				penHoldTimeElapsed = -1; //reset.
				//
				requestRepaint();
			}
		} else if (isColumnsHeaderArea(x, y) && isColsHeaderPressed) {
			//ordering the clicked column.
			final int selCol = getPressedColumn(x, y);
			//
			if (selCol != -1) {
				if (!isColumnVisible(selCol)) {
					idxFirstVisibleColumn++;
				}
				selectedColumnIndex = selCol;
				isColsHeaderPressed = false; //reset.
				sort(selCol);
				//
				requestRepaint();
			}
		}
	}

	/**
	 * <p>
	 * Format the value of a given item. This method is invoked to format the
	 * items when they are about to be displayed.
	 * </p>
	 * <p>
	 * Override this method to customize your items represetation.
	 * </p>
	 * @param columnIndex Column index.
	 * @param value Item value.
	 * @return Formatted value.
	 */
	protected String formatCell(int columnIndex, Object value) {
		if (value instanceof Date) {
			return StringUtil.formatDate((Date)value);
		} else {
			return value.toString();
		}
	}

	/**
	 * <p>
	 * Validate a given value right after its edition. At this moment the new
	 * value has not beed insert into the list yet. The new value is just
	 * inserted if this method returns true, otherwise the value is considered
	 * invalid and will not be inserted.
	 * </p>
	 * <p>
	 * Override this method and provide some customized validations.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @param newValue New item value.
	 * @return Valid item value or not.
	 */
	protected String validateCell(int itemIndex, int columnIndex,
		Object newValue) {
		return null;
	}
	
	/**
	 * <p>
	 * This method is invoked as soon as an item is edited.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Colum index.
	 * @param newValue New item value.
	 */
	protected void onCellEdit(int itemIndex, int columnIndex, Object newValue) {
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#sizeChanged(int, int)
	 */
	protected void sizeChanged(int w, int h) {
		super.sizeChanged(w, h);
		//
        bodyRect.h -= columnsHeaderRect.h; //recalculate the body height.
		//
		//reorganizing the selected item.
		if (items.size() > 0) {
			int lastIdx = selectedIndex;
			setSelectedIndex(0);
			setSelectedIndex(lastIdx);
		}
        //
		Column col;
		//
		for (int i = columns.size() -1; i >= 0; i--) {
			col = (Column)columns.elementAt(i);
			//
			col.width = (int)((w * col.percentage) / 100);
			col.prevWidth = -1;
			col.isMaximized = false;
		}
	}
    
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#drawList(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawList(Graphics g, int x, int y, int w, int h) {
		if (drawGrade) {
			//saving the clip area.
			final int cx = g.getClipX();
			final int cy = g.getClipY();
			final int cw = g.getClipWidth();
			final int ch = g.getClipHeight();
			super.drawList(g, x, y, w, h);
			//restauring the clip area.
			g.setClip(cx, cy, cw, ch);
			//drawing the grade.
			drawGrade(g, x, y, w, h);
		} else {
			super.drawList(g, x, y, w, h);
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#drawItem(javax.microedition.lcdui.Graphics, int, int, int, int, int, boolean)
	 */
	protected void drawItem(Graphics g, int x, int y, int w, int h,
		int itemIndex, boolean selected) {
		final int textColor;
		final int size = columns.size();
		//
		if (selected) {
			//drawing the selection bar.
			drawSelection(g, x, y, getColumnsWidth(), h);
			textColor = negativeItemFontColor;
		} else {
			if ((itemIndex % 2) == 0) { //even line.
				if (evenLineColor != -1) {
					g.setColor(evenLineColor);
					g.fillRect(x, y +1, getColumnsWidth(), h);
				}
			} else { //odd line.
				if (oddLineColor != -1) {
					g.setColor(oddLineColor);
					g.fillRect(x, y +1, getColumnsWidth(), h);
				}
			}
			textColor = bodyFontColor;
		}
		//drawing the text of the columns.
		g.setFont(bodyFont);
		Object item = items.elementAt(itemIndex);
		Object[] values = (Object[])item;
		String value;
		String orgValue;
		int offAlign;
		Column col;
		int sum = 0;
		boolean selCell;
		//
		for (int i = idxFirstVisibleColumn; i < size; i++) {
			col = (Column)columns.elementAt(i);
			selCell = selected && i == selectedColumnIndex;
			//
			if (i < values.length && values[i] != null) {
				value = formatCell(i, values[i]);
				orgValue = value;
				//
				if (selCell && rollerEnabled) {
					//rolling the text.
		    		roller.setText(value, bodyFont, col.width - OFFSET * 2);
		    		value = roller.getText();
				} else {
					value = UIUtil.shrinkString(value, col.width, bodyFont);
				}
				//
				if (value.equals(orgValue)) {
					//calculating the alignment offset.
					offAlign =
						UIUtil.alignString(
							value, col.width, col.align, bodyFont);
					if (offAlign < OFFSET) {
						offAlign = OFFSET;
					}
				} else {
					offAlign = 1; //no aligment for text that does not fit on the screen.
				}
				//
				if (selected) {
					g.setColor(textColor);
				} else {
					boolean paint = false;
					//
					//deciding the column color.
					//
					if (col.bodyColor != -1) {
						g.setColor(col.bodyColor);
						paint = true;
					}
					//
					Cell cell = findCustomCell(itemIndex, i);
					//
					if (cell != null && cell.bodyColor != -1) {
						g.setColor(cell.bodyColor);
						paint = true;
					}
					//
					if (paint) {
						g.fillRect(x, y, col.width, h);
					}
					//
					//deciding the font color.
					//
					paint = false;
					//
					if (col.bodyFontColor != -1) {
						g.setColor(col.bodyFontColor);
						paint = true;
					}
					if (cell != null && cell.bodyFontColor != -1) {
						g.setColor(cell.bodyFontColor);
						paint = true;
					}
					//
					if (!paint) {
						g.setColor(textColor);
					}
				}
				//
				g.drawString(
					value,
					x + offAlign,
					y + UIUtil.align(
						-1, bodyFont.getHeight(), h, UIUtil.ALIGN_MIDDLE), 
					Graphics.TOP | Graphics.LEFT);
			}
			//
			x += col.width;
			sum += col.width;
			//
			//drawing the cursor.
			if (selCell) {
				drawCursor(g, x - col.width, y, col.width - OFFSET, h - OFFSET);
			}
			if (sum > w) {
				break; //next column is not visible, so do not draw it.
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#drawHorizontalScrollBar(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawHorizontalScrollBar(Graphics g, int x, int y, int w,
		int h) {
		UIUtil.drawHorizontalScrollbar(
			g, 
			x, 
			y, 
			w, 
			h, 
			columns.size(), 
			getVisibleColumnsCount(), 
			idxFirstVisibleColumn, 
			horiScrollBarSquareRect, 
			scrollBarColor, 
			skin.getHorizontalScrollBarStyle());
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#hasHorizontalScrollBar()
	 */
	boolean hasHorizontalScrollBar() {
		return getVisibleColumnsCount() != columns.size();
	}
	
	/**
	 * <p>
	 * Draw the grid cursor used for navigation.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawCursor(Graphics g, int x, int y, int w, int h) {
		if (drawCursor) {
			g.setColor(cursorColor);
			g.drawRect(x +1, y +1, w, h);
		}
	}
	
	/**
	 * <p>
	 * Draw the columns header.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawColumnsHeader(Graphics g, int x, int y, int w, int h) {
		g.setFont(bodyFont);
		//
		//drawing the background.
		g.setColor(columnsHeaderColor);
		g.fillRect(x, y, getColumnsWidth(), h);
		//drawing a top line.
//		g.setColor(255, 255, 255); //white color.
//		g.drawLine(x, y, w, y);
		//drawing a bottom line.
//		g.setColor(gradeColor);
//		g.drawLine(x, y + h, x + w, y + h);
		//
		//drawing the columns header.
		final int size = columns.size();
		Column col;
		String colName;
		int fontColor;
		int offAlign;
		int sum = 0;
		//
		for (int i = idxFirstVisibleColumn; i < size; i++) {
			col = (Column)columns.elementAt(i);
			//painting the column header color.
			if (col.headerColor != -1) {
				g.setColor(col.headerColor);
				g.fillRect(x +1, y, col.width, h);
				if (drawGrade) {
					//redrawing a top line of the current column.
					g.setColor(255, 255, 255); //white color.
					g.drawLine(x, y, w, y);
				}
			}
			//drawing the column name.
			if (col.headerFontColor != -1) {
				fontColor = col.headerFontColor;
			} else {
				fontColor = columnsHeaderFontColor;
			}
			//
			if (col.name != null) {
				colName = UIUtil.shrinkString(col.name, col.width, bodyFont);
				offAlign =
					UIUtil.alignString(
						colName, col.width, col.align, bodyFont);
				if (offAlign == 0) {
					offAlign = OFFSET;
				}
				if (col.align == UIUtil.ALIGN_RIGHT) {
					offAlign--;
				}
				//drawing the shadow.
				g.setColor(0, 0, 0); //black color.
				g.drawString(
					colName, x + offAlign +1, y +1, Graphics.TOP|Graphics.LEFT);
				//
				g.setColor(fontColor);
				g.drawString(
					colName, x + offAlign, y, Graphics.TOP | Graphics.LEFT);
			} else {
				offAlign = OFFSET;
				colName = null;
			}
			//
			//drawing the triangle simbolizing the ordered column.
			if (i == lastSortedColumn) {
				if (col.align == UIUtil.ALIGN_RIGHT &&
					offAlign >= triangleSize + OFFSET) {
					UIUtil.drawTriangle(
						g,
						x + OFFSET,
						y + OFFSET,
						triangleSize,
						!ascendingOrder);
				} else {
					int offTri;
					if (colName != null) {
						offTri =
							offAlign + bodyFont.stringWidth(colName) + OFFSET;
					} else {
						offTri = offAlign + OFFSET;
					}
					if (col.width - offTri >= triangleSize) {
						UIUtil.drawTriangle(
							g,
							x + col.width - triangleSize - OFFSET,
							y + OFFSET,
							triangleSize,
							!ascendingOrder);
					}
				}
			}
			//
			x += col.width;
			sum += col.width;
			//
			if (drawGrade) {
				//draw the vertical line.
				g.setColor(gradeColor);
				g.drawLine(x -1, y, x -1, y + h);
				g.setColor(255, 255, 255); //white color.
				g.drawLine(x, y, x, y + h -1);
			}
			//
			if (sum > w) {
				break; //next column is not visible, so do not draw it.
			}
		}
	}
	
	/**
	 * <p>
	 * Draw the grid grade.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawGrade(Graphics g, int x, int y, int w, int h) {
		g.setColor(gradeColor);
		//drawing the horizontal lines.
		int prevY = y;
		final int itemHeight = getItemHeight();
		final int itemsPerPage = getItemsPerPage();
		int size = items.size();
		if (size > itemsPerPage) { //more items than can fit in the screen?
			if (idxFirstVisibleItem + itemsPerPage == size) {
				size = itemsPerPage;
			} else {
				//draw one more item after the last one.
				size = itemsPerPage +1;
			}
		}
		int i = 0;
		for (; i < size; i++) {
			prevY += itemHeight;
			g.drawLine(x, prevY, x + w,  prevY);
		}
		//drawing the vertical lines.
		size = columns.size();
		i = idxFirstVisibleColumn;
		int sum = 0;
		Column col;
		//
		for (; i < size; i++) {
			col = (Column)columns.elementAt(i);
			x += col.width;
			sum += col.width;
			//draw the vertical line.
			g.drawLine(x, y, x, prevY);
			if (sum > w) {
				break; //next column is not visible, so do not draw it.
			}
		}
	}
	
	/**
	 * <p>
	 * Move navigation cursor to the left.
	 * </p>
	 */
	void moveCursorLeft() {
		if (selectedColumnIndex > 0) {
			selectedColumnIndex--;
			while (!isColumnVisible(selectedColumnIndex)) {
				idxFirstVisibleColumn--;
			}
			requestRepaint();
		}
	}
	
	/**
	 * <p>
	 * Move navigation cursor to the right.
	 * </p>
	 */
	void moveCursorRight() {
		if (selectedColumnIndex +1 < columns.size()) {
			selectedColumnIndex++;
			while (!isColumnVisible(selectedColumnIndex)) {
				idxFirstVisibleColumn++;
			}
			requestRepaint();
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#isItemsArea(int, int)
	 */
	boolean isItemsArea(int x, int y) {
		int pw = bodyRect.w;
		try {
			int cw = getColumnsWidth();
			if (cw < bodyRect.w) {
				bodyRect.w = cw; //last column is being displayed.
			}
			return super.isItemsArea(x, y);
		} finally {
			bodyRect.w = pw;
		}
	}
	
	/**
	 * <p>
	 * Verify if the given point is inside the columns header area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is inside or not.
	 */
	boolean isColumnsHeaderArea(int x, int y) {
		return columnsHeaderRect.contains(x, y);
	}
	
	/**
	 * <p>
	 * Get the pressed column index based on a given point.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Column index.
	 */
	int getPressedColumn(int x, int y) {
		final int size = columns.size();
		Column col;
		int offset_x = 0;
		//
		for (int i = idxFirstVisibleColumn; i < size; i++) {
			col = (Column)columns.elementAt(i);
			offset_x += col.width;
			if (offset_x > x) {
				if (!isColumnVisible(i)) {
					idxFirstVisibleColumn++;
				}
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * <p>
	 * Get the pressed column index elegible for dragging based on a given
	 * point.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Column index.
	 */
	int getPressedColumnToDrag(int x, int y) {
		final int OFFSET_X = 5;
		final int size = columns.size();
		Column col;
		int sum_w = 0;
		//
		for (int i = idxFirstVisibleColumn; i < size; i++) {
			col = (Column)columns.elementAt(i);
			sum_w += col.width;
			if (Math.abs(x - sum_w) <= OFFSET_X) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#scrollLeftPage()
	 */
	void scrollLeftPage() {
		if (idxFirstVisibleColumn != 0) {
			idxFirstVisibleColumn--;
			while (!isColumnVisible(selectedColumnIndex)) {
				selectedColumnIndex--;
			}
			requestRepaint();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#scrollRightPage()
	 */
	void scrollRightPage() {
		if (idxFirstVisibleColumn + getVisibleColumnsCount() < columns.size()) {
			idxFirstVisibleColumn++;
			while (!isColumnVisible(selectedColumnIndex)) {
				selectedColumnIndex++;
			}
			requestRepaint();
		}
	}
	
	/**
	 * <p>
	 * Verify if a given column is visible.
	 * </p>
	 * @param columnIndex Column index.
	 * @return Visible or not.
	 */
	boolean isColumnVisible(int columnIndex) {
		if (columnIndex < idxFirstVisibleColumn) {
			return false;
		} else if (columnIndex == idxFirstVisibleColumn) {
			return true;
		}
		int sum = 0;
		Column col;
		//
		for (int i = idxFirstVisibleColumn; i <= columnIndex; i++) {
			col = (Column)columns.elementAt(i);
			sum += col.width;
		}
		return sum <= bodyRect.w;
	}
	
	/**
	 * <p>
	 * Get the sum of the visible columns width. 
	 * </p>
	 * @return Width.
	 */
	int getColumnsWidth() {
		final int size = columns.size();
		Column col;
		int sum = 0;
		//
		for (int i = idxFirstVisibleColumn; i < size; i++) {
			col = (Column)columns.elementAt(i);
			sum += col.width;
		}
		return sum;
	}
	
	/**
	 * <p>
	 * Check if the given index respects the columns list bounds.
	 * </p>
	 * @param columnIndex Column index.
	 * @throws IllegalArgumentException Column invalid index.
	 */
	void checkColumnsBounds(int columnIndex) {
		if (columnIndex < 0 || columnIndex >= columns.size()) {
			throw new IllegalArgumentException(
			    "Column index must be between 0 and " +
			        (columns.size() -1) + ": " + columnIndex);
		}
	}
	
	/**
	 * <p>
	 * Get the best max width for a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @return Best width.
	 */
	int getBestMaxWidth(int columnIndex) {
		Column col = (Column)columns.elementAt(columnIndex);
		int maxWidth = -1;
		Object[] values;
		Object value;
		//
		for (int i = items.size() -1; i >= 0; i--) {
			values = (Object[])items.elementAt(i);
			if (columnIndex < values.length) {
				value = values[columnIndex];
				if (value != null) {
					maxWidth =
						Math.max(
						    maxWidth,
						    bodyFont.stringWidth(
						    	formatCell(columnIndex, value)));
					if (maxWidth > bodyRect.w) {
						//max width larger than screen width.
						return bodyRect.w;
					}
				}
			}
		}
		if (col.name != null) {
			if (maxWidth < bodyFont.stringWidth(col.name)) {
				//column width is same as column name width.
				maxWidth = bodyFont.stringWidth(col.name);
				if (maxWidth > bodyRect.w) {
					//max width larger than screen width.
					return bodyRect.w;
				}
			}
		}
		final int offTri =
			(triangleSize + OFFSET) *
			(col.align == UIUtil.ALIGN_CENTER ? 2 : 1); //column aligned to the CENTER needs an extra width.
		if (columnIndex == lastSortedColumn && maxWidth + offTri <= bodyRect.w){
			//adding the extra width in order to fit the triangle.
			maxWidth += offTri;
		}
		return maxWidth == -1 ? 0 : maxWidth + OFFSET;
	}

	/**
	 * <p>
	 * Get visible column count.
	 * </p>
	 * @return Count.
	 */
	int getVisibleColumnsCount() {
		final int size = columns.size();
		final int ipp = getItemsPerPage();
		final int w = //verify if there is vertical scrollbar, in order to decrease its width from body's width.
			bodyRect.w - (ipp != -1 && items.size() > ipp 
							? UIUtil.getScrollbarSize(
								bodyRect.w, bodyRect.h, hasPointerEvents()) 
							: 0);
		//
		Column col;
		int sum = 0;
		int c = 0;
		//
		for (int i = idxFirstVisibleColumn; i < size; i++) {
			col = (Column)columns.elementAt(i);
			sum += col.width;
			//
			if (sum > w) {
				break;
			}
			//
			c++;
		}
		//
		return c;
	}
	
	/**
	 * <p>
	 * Finds a custom cell object located at a given coordinate. If the object
	 * is not found, <code>null</code> is returned.
	 * </p>
	 * @param itemIndex Item index.
	 * @param columnIndex Column index.
	 * @return Custom object.
	 */
	Cell findCustomCell(int itemIndex, int columnIndex) {
		if (listCustomCells != null) {
			Cell cell = null;
			//
			for (int i = listCustomCells.size() -1; i >= 0; i--) {
				cell = (Cell)listCustomCells.elementAt(i);
				//
				if (cell.itemIndex == itemIndex &&
					cell.columnIndex == columnIndex) {
					return cell;
				}
			}
		}
		//
		return null;
	}
	
	/**
	 * <p>
	 * Adds a given custom cell object into the objects list.
	 * </p>
	 * @param cell Custom cell object.
	 */
	void addCustomCell(Cell cell) {
		if (listCustomCells == null) {
			listCustomCells = new Vector(5);
		}
		//
		listCustomCells.addElement(cell);
	}
	
	/**
	 * <p>
	 * Get the input constraint based on the type of a given column.
	 * </p>
	 * @param columnIndex Column index.
	 * @return Input constraint.
	 * @see javax.microedition.lcdui.TextField#ANY
	 * @see javax.microedition.lcdui.TextField#NUMERIC
	 */
	private int getInputConstraint(int columnIndex) {
		Column col = (Column)columns.elementAt(columnIndex);
		switch (col.type) {
			case STRING:
			case DATE:    return TextField.ANY;
			case NUMERIC: return TextField.NUMERIC;
			default:      return TextField.ANY;
		}
	}
	
	/**
	 * <p>
	 * Get the an object that holds a given value based on the type of a given
	 * column.
	 * </p>
	 * @param value Value in a text representation.
	 * @param columnIndex Column index.
	 * @return Item.
	 */
	private Object getValue(String value, int columnIndex) {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		Class type = null;
		Column col = (Column)columns.elementAt(columnIndex);
		switch (col.type) {
			case STRING:
				return value;
			case NUMERIC:
				type = getTypeUsed(columnIndex);
				if (type == new Long(1).getClass()) {
					return new Long(Long.parseLong(value));
				} else {
					return Integer.valueOf(value);
				}
			case DATE:
				String[] dateParts = StringUtil.split(value, '/');
				if (dateParts.length != 3) {
					throw new NumberFormatException();
				}
				try {
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
					c.set(Calendar.MONTH, Integer.parseInt(dateParts[1]) -1);
					c.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));
					c.set(Calendar.HOUR, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					return c.getTime();
				} catch (IllegalArgumentException e) {
					throw new NumberFormatException();
				}
			default:
				return value;
		}
	}
	
	/**
	 * <p>
	 * Get the class object of a given column based on its type.
	 * </p>
	 * @param columIndex Column index.
	 * @return Class object.
	 */
	private Class getTypeUsed(int columnIndex) {
		Object value;
		for (int i = items.size() -1; i >= 0; i--) {
			value = get(i, columnIndex);
			if (value != null) {
				return value.getClass();
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * Create an grid item represetation from a given object.
	 * </p>
	 * @param item Item.
	 * @return Grid item.
	 */
	private Object createGridItem(Object item) {
		if (!(item instanceof Object[])) {
			item = new Object[] {item};
		}
		return item;
	}

	/**
	 * <p>
	 * Define a column structure.
	 * </p>
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	class Column {
		/**
		 * <p>
		 * Column name. 
		 * </p>
		 */
		public String name;
		
		/**
		 * <p>
		 * Percentage of the screen width.
		 * </p>
		 */
		public int percentage;
		
		/**
		 * <p>
		 * Column width.
		 * </p>
		 */
		public int width;
		
		/**
		 * <p>
		 * Column previous width. Hold the orignal width when the column is
		 * resized.
		 * </p>
		 */
		public int prevWidth = -1;
		
		/**
		 * <p>
		 * Column alignment.
		 * </p>
		 */
		public int align = UIUtil.ALIGN_LEFT;
		
		/**
		 * <p>
		 * Column is maximized or not.
		 * </p>
		 */
		public boolean isMaximized;
		
		/**
		 * <p>
		 * Column header color.
		 * </p>
		 */
		public int headerColor = -1;
		
		/**
		 * <p>
		 * Column body color.
		 * </p>
		 */
		public int bodyColor = -1;
		
		/**
		 * <p>
		 * Column header font color.
		 * </p>
		 */
		public int headerFontColor = -1;
		
		/**
		 * <p>
		 * Column body font color.
		 * </p>
		 */
		public int bodyFontColor = -1;
		
		/**
		 * <p>
		 * Column is editable or not.
		 * </p>
		 */
		public boolean isEditable;
		
		/**
		 * <p>
		 * Column type.
		 * </p>
		 */
		public int type = STRING;
	}
	
	/**
	 * <p>
	 * Define a cell structure.
	 * </p>
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	class Cell {
		/**
		 * <p>
		 * Item index.
		 * </p>
		 */
		public int itemIndex;
		
		/**
		 * <p>
		 * Columns index.
		 * </p>
		 */
		public int columnIndex;

		/**
		 * <p>
		 * Cell body color.
		 * </p>
		 */
		public int bodyColor = -1;

		/**
		 * <p>
		 * Cell body font color.
		 * </p>
		 */
		public int bodyFontColor = -1;

		/**
		 * <p>
		 * Celll is editable or not.
		 * </p>
		 */
		public boolean isEditable;
		
		/**
		 * <p>
		 * Constructor.
		 * </p>
		 * @param itemIndex Item index.
		 * @param columnIndex Column index.
		 */
		public Cell(int itemIndex, int columnIndex) {
			this.itemIndex = itemIndex;
			this.columnIndex = columnIndex;
		}
	}
	
	/**
	 * <p>
	 * Extend the quicksort compoent in order to work with the grid items.
	 * </p>
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	class GridQSort extends QSort {
		/**
		 * <p>
		 * Column index being sorted.
		 * </p>
		 */
		protected int columnIndex;
		
		/**
		 * <p>
		 * Constructor.
		 * </p>
		 * @param columnIndex Column index to be sorted.
		 * @param ascending Ascending order or not.
		 */
		public GridQSort(int columnIndex, boolean ascending) {
			this.columnIndex = columnIndex;
			setAscendingSortEnabled(ascending);
		}
		
		/**
		 * @inheritDoc
		 * @see com.emobtech.uime.util.QSort#lesser(java.lang.Object, java.lang.Object)
		 */
		protected boolean lesser(Object i1, Object i2) {
			if (i1 != null && i2 == null) {
				return true;
			} else if (i1 == null) {
				return false;
			} else {
				if (i1 instanceof String) {
					return super.lesser(i1, i2);
				} else if (i1 instanceof Integer) {
					return ((Integer)i1).intValue() < ((Integer)i2).intValue();
				} else if (i1 instanceof Date) {
					return ((Date)i1).getTime() < ((Date)i2).getTime();
				} else if (i1 instanceof Long) {
					return ((Long)i1).longValue() < ((Long)i2).longValue();
				} else {
					return super.lesser(i1, i2);
				}
			}
		}
		
		/**
		 * @inheritDoc
		 * @see com.emobtech.uime.util.QSort#greater(java.lang.Object, java.lang.Object)
		 */
		protected boolean greater(Object i1, Object i2) {
			if (i1 != null && i2 == null) {
				return true;
			} else if (i1 == null) {
				return false;
			} else {
				if (i1 instanceof String) {
					return super.greater(i1, i2);
				} else if (i1 instanceof Integer) {
					return ((Integer)i1).intValue() > ((Integer)i2).intValue();
				} else if (i1 instanceof Date) {
					return ((Date)i1).getTime() > ((Date)i2).getTime();
				} else if (i1 instanceof Long) {
					return ((Long)i1).longValue() > ((Long)i2).longValue();
				} else {
					return super.greater(i1, i2);
				}
			}
		}
		
		/**
		 * @inheritDoc
		 * @see com.emobtech.uime.util.QSort#getElement(java.lang.Object)
		 */
		protected Object getElement(Object ob) {
			Object[] items = (Object[])ob;
			if (columnIndex < items.length) {
				return items[columnIndex];
			} else {
				return null;
			}
		}
	}
}