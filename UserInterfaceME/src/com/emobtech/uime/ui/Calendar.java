/*
 * Calendar.java
 * 06/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import java.util.Date;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.DateUtil;
import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.Locale;
import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a calendar.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Calendar extends Screen {
	/**
	 * Count of days in a week.
	 */
	static final int DAY_COUNT_WEEK = 7;

	/**
	 * Count of weeks in a month.
	 */
	static final int WEEK_COUNT_MOUNTH = 6;

	/**
	 * <p>
	 * Matrix of days in a month.
	 * </p>
	 */
	int[][] days;

	/**
	 * <p>
	 * X coordinate of the selected day in the matrix.
	 * </p>
	 */
	int dayX;

	/**
	 * <p>
	 * Y coordinate of the selected day in the matrix.
	 * </p>
	 */
	int dayY;

	/**
	 * <p>
	 * Initials of the days.
	 * </p>
	 */
	String[] daysInitial;

	/**
	 * <p>
	 * Names of the months.
	 * </p>
	 */
	String[] monthsName;

	/**
	 * <p>
	 * Header rectangle.
	 * </p>
	 */
	Rect headerRect;

	/**
	 * <p>
	 * Frame header rectangle.
	 * </p>
	 */
	Rect frameHeaderRect;

	/**
	 * <p>
	 * Frame rectangle.
	 * </p>
	 */
	Rect frameRect;

	/**
	 * <p>
	 * Square rectangle.
	 * </p>
	 */
	Rect squareRect;

	/**
	 * <p>
	 * Frame color.
	 * </p>
	 */
	int frameColor;

	/**
	 * <p>
	 * Frame header color.
	 * </p>
	 */
	int frameHeaderColor = -1;

	/**
	 * <p>
	 * Frame header font color.
	 * </p>
	 */
	int frameHeaderFontColor;

	/**
	 * <p>
	 * Header font color.
	 * </p>
	 */
	int headerFontColor;

	/**
	 * <p>
	 * Selection color.
	 * </p>
	 */
	int selectionColor;

	/**
	 * <p>
	 * Selected day.
	 * </p>
	 */
	int selDay;

	/**
	 * <p>
	 * Selected month.
	 * </p>
	 */
	int selMonth;

	/**
	 * <p>
	 * Selected year.
	 * </p>
	 */
	int selYear;

	/**
	 * <p>
	 * Current day.
	 * </p>
	 */
	int curDay;

	/**
	 * <p>
	 * Current month.
	 * </p>
	 */
	int curMonth;

	/**
	 * <p>
	 * Current year.
	 * </p>
	 */
	int curYear;

	/**
	 * <p>
	 * Simple date view mode.
	 * </p>
	 */
	boolean simpleDateMode;

	/**
	 * <p>
	 * Year rectangle.
	 * </p>
	 */
	Rect yearRect;

	/**
	 * <p>
	 * Month rectangle.
	 * </p>
	 */
	Rect monthRect;

	/**
	 * <p>
	 * Day rectangle.
	 * </p>
	 */
	Rect dayRect;

	/**
	 * <p>
	 * Up triangle rectangle.
	 * </p>
	 */
	Rect upTriangleRect;

	/**
	 * <p>
	 * Down triangle rectangle.
	 * </p>
	 */
	Rect downTriangleRect;

	/**
	 * <p>
	 * Simple date font.
	 * </p>
	 */
	Font simpleDateFont;

	/**
	 * <p>
	 * Date format.
	 * </p>
	 */
	String[] dateFormat;

	/**
	 * <p>
	 * Index of the selected date part.
	 * </p>
	 */
	int sDateIdxFocus;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title Title.
	 */
	public Calendar(String title) {
		super(title);
		//
		days = new int[WEEK_COUNT_MOUNTH][DAY_COUNT_WEEK];
		String prop = 
			I18N.getFramework("framework.jme.ui.calendar.daysinitial");
		daysInitial = StringUtil.split(prop, ',');
		prop = I18N.getFramework("framework.jme.ui.calendar.monthnames"); 
		monthsName = StringUtil.split(prop, ',');
		//
		headerRect = new Rect();
		frameHeaderRect = new Rect();
		frameRect = new Rect();
		squareRect = new Rect();
		//
		java.util.Calendar c = java.util.Calendar.getInstance();
		// getting the current date.
		curDay = c.get(java.util.Calendar.DAY_OF_MONTH);
		curMonth = c.get(java.util.Calendar.MONTH);
		curYear = c.get(java.util.Calendar.YEAR);
		selDay = curDay;
		selMonth = curMonth;
		selYear = curYear;
		//
		loadDays(selMonth, selYear);
		setDayMatrixCoords(selDay);
		//
		skin.applyCalendarSkin(this);
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
	}

	/**
	 * <p>
	 * Set the frame color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setFrameColor(int r, int g, int b) {
		frameColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the header font color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setHeaderFontColor(int r, int g, int b) {
		headerFontColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the frame header color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setFrameHeaderColor(int r, int g, int b) {
		frameHeaderColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the frame header font color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setFrameHeaderFontColor(int r, int g, int b) {
		frameHeaderFontColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the calendar date.
	 * </p>
	 * @param date Date.
	 */
	public synchronized void setDate(Date date) {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
		}
		//
		curDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);
		curMonth = calendar.get(java.util.Calendar.MONTH);
		curYear = calendar.get(java.util.Calendar.YEAR);
		selDay = curDay;
		selMonth = curMonth;
		selYear = curYear;
		//
		loadDays(selMonth, selYear);
		setDayMatrixCoords(selDay);
		//
		requestRepaint();
	}

	/**
	 * <p>
	 * Get the calendar date.
	 * </p>
	 * @return Date.
	 */
	public synchronized Date getDate() {
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(java.util.Calendar.DAY_OF_MONTH, selDay);
		calendar.set(java.util.Calendar.MONTH, selMonth);
		calendar.set(java.util.Calendar.YEAR, selYear);
		//
		return calendar.getTime();
	}

	/**
	 * <p>
	 * Get the day.
	 * </p>
	 * @return Day.
	 */
	public synchronized int getDay() {
		return selDay;
	}

	/**
	 * <p>
	 * Get the month.
	 * </p>
	 * @return Month.
	 */
	public synchronized int getMonth() {
		return selMonth;
	}

	/**
	 * <p>
	 * Get the year.
	 * </p>
	 * @return Year.
	 */
	public synchronized int getYear() {
		return selYear;
	}

	/**
	 * <p>
	 * Enable the simple date view mode.
	 * </p>
	 * @param enabled Enabled or not.
	 */
	public synchronized void setSimpleDateMode(boolean enabled) {
		simpleDateMode = enabled;
		//
		if (enabled) {
			yearRect = new Rect();
			monthRect = new Rect();
			dayRect = new Rect();
			upTriangleRect = new Rect();
			downTriangleRect = new Rect();
			//
			simpleDateFont = Font.getFont(bodyFont.getFace(), Font.STYLE_BOLD,
					Font.SIZE_LARGE);
			//
			dateFormat = StringUtil.split(Locale.getDateFormat(), Locale
					.getDateSeparator());
			//
			headerRect = null;
			frameHeaderRect = null;
			frameRect = null;
			squareRect = null;
		} else {
			yearRect = null;
			monthRect = null;
			dayRect = null;
			upTriangleRect = null;
			downTriangleRect = null;
			//
			simpleDateFont = null;
			//
			dateFormat = null;
			//
			headerRect = new Rect();
			frameHeaderRect = new Rect();
			frameRect = new Rect();
			squareRect = new Rect();
			//
			setDayMatrixCoords(selDay);
		}
		//
		System.gc();
		//
		requestRepaint();
	}

	/**
	 * <p>
	 * Event triggered when the selected day changes.
	 * </p>
	 */
	protected void onDayChange() {
	}

	/**
	 * <p>
	 * Event triggered when the selected month changes.
	 * </p>
	 */
	protected void onMonthChange() {
	}

	/**
	 * <p>
	 * Event triggered when the selected year changes.
	 * </p>
	 */
	protected void onYearChange() {
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyHold(int)
	 */
	protected void keyHold(int keyCode) {
		final int key = getGameAction(keyCode);
		//
		if (key == UP || key == DOWN || key == LEFT || key == RIGHT) {
			keyDown(keyCode);
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyDown(int)
	 */
	protected void keyDown(int keyCode) {
		keyCode = getGameAction(keyCode);
		//
		if (simpleDateMode) {
			if (keyCode == LEFT) {
				sDateIdxFocus--;
				if (sDateIdxFocus == -1) {
					sDateIdxFocus = 0;
				} else {
					requestRepaint();
				}
			} else if (keyCode == RIGHT) {
				sDateIdxFocus++;
				if (sDateIdxFocus == 3) {
					sDateIdxFocus = 2;
				} else {
					requestRepaint();
				}
			} else if (keyCode == UP || keyCode == DOWN) {
				scrollSimpleDate(keyCode);
			}
		} else {
			if (keyCode == UP || keyCode == DOWN || keyCode == LEFT
					|| keyCode == RIGHT) {
				moveSelectedDay(keyCode);
			}
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDown(int, int)
	 */
	protected void penDown(int x, int y) {
		if (simpleDateMode) {
			if (upTriangleRect.contains(x, y)) {
				scrollSimpleDate(UP);
			} else if (downTriangleRect.contains(x, y)) {
				scrollSimpleDate(DOWN);
			} else if (yearRect.contains(x, y)) {
				sDateIdxFocus = getDatePartPos("yyyy");
				//
				requestRepaint();
			} else if (monthRect.contains(x, y)) {
				sDateIdxFocus = getDatePartPos("MM");
				//
				requestRepaint();
			} else if (dayRect.contains(x, y)) {
				sDateIdxFocus = getDatePartPos("dd");
				//
				requestRepaint();
			}
		} else {
			if (frameRect.contains(x, y)) {
				x = (x - frameRect.x) / squareRect.w;
				y = (y - frameRect.y) / squareRect.h;
				//
				int day = days[y][x];
				if (day != -1) {
					selDay = day;
					setDayMatrixCoords(day);
					//
					requestRepaint();
				}
			}
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics,
	 *      int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w, int h) {
		if (simpleDateMode) {
			drawSimpleDate(g, x, y, w, h);
			//
			return;
		}
		//
		squareRect.w = w / DAY_COUNT_WEEK;
		squareRect.h = bodyFont.getHeight() + OFFSET;
		//
		frameHeaderRect.w = squareRect.w * DAY_COUNT_WEEK;
		frameHeaderRect.h = squareRect.h;
		//
		frameRect.w = frameHeaderRect.w;
		frameRect.h = frameHeaderRect.h * WEEK_COUNT_MOUNTH;
		//
		headerRect.w = frameHeaderRect.w;
		headerRect.h = squareRect.h;
		//
		headerRect.x = x;
		headerRect.y = y;
		//
		final int hh = h - (frameHeaderRect.h + frameRect.h);
		if (hh < headerRect.h) { // is there space for the header?
//			headerRect.h = 0;
			setSimpleDateMode(true);
			return;
		} else {
			if (hideTitle) {
				headerRect.y = y; // used when the screen is a control.
			} else {
				headerRect.y = y + hh / 4;
			}
		}
		//
		frameHeaderRect.x = headerRect.x + (w - frameHeaderRect.w) / 2;
		frameHeaderRect.y = headerRect.y + headerRect.h;
		//
		frameRect.x = frameHeaderRect.x;
		frameRect.y = frameHeaderRect.y + frameHeaderRect.h - 1;
		//
		if (headerRect.h > 0) {
			g.setClip(headerRect.x, headerRect.y, headerRect.w, headerRect.h);
			drawHeader(g, headerRect.x, headerRect.y, headerRect.w,
					headerRect.h);
		}
		//
		g.setClip(frameHeaderRect.x, frameHeaderRect.y, frameHeaderRect.w + 1,
				frameHeaderRect.h + 1);
		drawFrameHeader(g, frameHeaderRect.x, frameHeaderRect.y,
				frameHeaderRect.w, frameHeaderRect.h);
		//
		g.setClip(frameRect.x, frameRect.y, frameRect.w + 1, frameRect.h + 1);
		drawFrame(g, frameRect.x, frameRect.y, frameRect.w, frameRect.h);
		drawDaysOfMonth(g, frameRect.x, frameRect.y, frameRect.w, frameRect.h);
	}

	/**
	 * <p>
	 * Draw the calendar header.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawHeader(Graphics g, int x, int y, int w, int h) {
		g.setFont(bodyFont);
		//
		final String dateStr = monthsName[selMonth] + " / " + selYear;
		x += UIUtil.alignString(dateStr, w, UIUtil.ALIGN_CENTER,
				bodyFont);
		y += UIUtil.alignString(dateStr, h, UIUtil.ALIGN_MIDDLE,
				bodyFont);
		//
		g.setColor(190, 190, 190); // dark gray color.
		g.drawString(dateStr, x - 1, y + 1, Graphics.TOP | Graphics.LEFT);
		//
		g.setColor(headerFontColor);
		g.drawString(dateStr, x, y, Graphics.TOP | Graphics.LEFT);
	}

	/**
	 * <p>
	 * Draw the calendar frame header.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawFrameHeader(Graphics g, int x, int y, int w, int h) {
		g.setFont(bodyFont);
		//
		if (frameHeaderColor != -1) {
			g.setColor(frameHeaderColor);
			g.fillRect(x, y, w, h); // draw the background.
		}
		//
		g.setColor(frameColor);
		g.drawRect(x, y, w, h);
		//
		String initial = null;
		//
		for (int i = 0; i < DAY_COUNT_WEEK; i++) {
			g.setColor(frameHeaderFontColor);
			initial = daysInitial[i];
			//
			g.drawString( // draw the initial.
					initial, x
							+ UIUtil.alignString(initial, squareRect.w,
									UIUtil.ALIGN_CENTER, bodyFont), y
							+ UIUtil.alignString(initial, squareRect.h,
									UIUtil.ALIGN_MIDDLE, bodyFont),
					Graphics.TOP | Graphics.LEFT);
			//
			if (i < DAY_COUNT_WEEK - 1) {
				x += squareRect.w;
				g.setColor(frameColor);
				//
				g.drawLine(x, y, x, y + h);
			}
		}
	}

	/**
	 * <p>
	 * Draw the calendar frame.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawFrame(Graphics g, int x, int y, int w, int h) {
		g.setColor(255, 255, 255); // white color.
		g.fillRect(x, y, w, h); // draw the background.
		//
		g.setColor(frameColor);
		g.drawRect(x, y, w, h);
		//
		int xx = x;
		int yy = y;
		//
		for (int i = 0; i < DAY_COUNT_WEEK - 1; i++) {
			yy += squareRect.h;
			xx += squareRect.w;
			//
			g.drawLine(x, yy, x + w, yy);
			g.drawLine(xx, y, xx, y + h);
		}
	}

	/**
	 * <p>
	 * Draw the days of the month.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawDaysOfMonth(Graphics g, int x, int y, int w, int h) {
		g.setFont(bodyFont);
		g.setColor(bodyFontColor);
		//
		String day = null;
		int dayi;
		int xx;
		//
		for (int i = 0; i < WEEK_COUNT_MOUNTH; i++) {
			xx = x;
			//
			for (int j = 0; j < DAY_COUNT_WEEK; j++) {
				dayi = days[i][j];
				//
				if (dayi != -1) {
					if (dayi == selDay) {
						// highlight the selected day.
						g.setColor(selectionColor);
						g.fillRect(xx + 1, y + 1, squareRect.w - 1,
								squareRect.h - 1);
						//
						g.setColor(bodyFontColor); // restore the font color.
					} else if (dayi == curDay && selMonth == curMonth
							&& selYear == curYear) {
						// highlight the current day.
						g.setColor(frameColor);
						g.fillRect(xx, y, squareRect.w, squareRect.h);
						//
						g.setColor(bodyFontColor); // restore the font color.
					}
					//
					day = dayi + "";
					//
					g.drawString( // draw the day.
							day, xx
									+ UIUtil.alignString(day,
											squareRect.w, UIUtil.ALIGN_CENTER,
											bodyFont), y
									+ UIUtil.alignString(day,
											squareRect.h, UIUtil.ALIGN_MIDDLE,
											bodyFont), Graphics.TOP
									| Graphics.LEFT);
				}
				//
				xx += squareRect.w;
			}
			//
			y += squareRect.h;
		}
	}

	/**
	 * <p>
	 * Draw the simple date view mode.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawSimpleDate(Graphics g, int x, int y, int w, int h) {
		final int off = OFFSET * 6;
		//
		yearRect.h = simpleDateFont.getHeight() + OFFSET * 4;
		yearRect.y = y
				+ UIUtil.align(-1, yearRect.h, h,
						UIUtil.ALIGN_MIDDLE);
		yearRect.w = simpleDateFont.stringWidth("0000") + off;
		//
		monthRect.h = yearRect.h;
		monthRect.y = yearRect.y;
		monthRect.w = simpleDateFont.stringWidth("00") + off;
		//
		dayRect.h = yearRect.h;
		dayRect.y = yearRect.y;
		dayRect.w = simpleDateFont.stringWidth("00") + off;
		//
		int xx = x
				+ UIUtil.align(yearRect.w + monthRect.w
						+ dayRect.w + (OFFSET * 2) * 2, -1, w,
						UIUtil.ALIGN_CENTER);
		//
		for (int i = 0; i < dateFormat.length; i++) {
			if (StringUtil.equalsIgnoreCase(dateFormat[i], "yyyy")) {
				yearRect.x = xx;
				//
				drawSimpleDatePart(g, yearRect.x, yearRect.y, yearRect.w,
						yearRect.h, selYear + "", i == sDateIdxFocus);
				//
				xx += yearRect.w + OFFSET * 2;
			} else if (StringUtil.equalsIgnoreCase(dateFormat[i], "MM")) {
				monthRect.x = xx;
				//
				drawSimpleDatePart(g, monthRect.x, monthRect.y, monthRect.w,
						monthRect.h, StringUtil.zeroPad(selMonth + 1, 2),
						i == sDateIdxFocus);
				//
				xx += monthRect.w + OFFSET * 2;
			} else { // if (StringUtil.equalsIgnoreCase(dateFormat[i], "dd")) {
				dayRect.x = xx;
				//
				drawSimpleDatePart(g, dayRect.x, dayRect.y, dayRect.w,
						dayRect.h, StringUtil.zeroPad(selDay, 2),
						i == sDateIdxFocus);
				//
				xx += dayRect.w + OFFSET * 2;
			}
		}
		//
		g.setFont(bodyFont);
		g.setColor(bodyFontColor);
		//
		final String df = '(' + Locale.getDateFormat() + ')';
		g.drawString(
			df,
			x + UIUtil.alignString(df, w, UIUtil.ALIGN_CENTER, bodyFont), 
			y + (h / 2 + h / 4), //75%.
			Graphics.TOP | Graphics.LEFT);
	}

	/**
	 * <p>
	 * Draw a part date of the simple date view mode.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param part Date part.
	 * @param focus Has the focus or not.
	 */
	void drawSimpleDatePart(Graphics g, int x, int y, int w, int h,
			String part, boolean focus) {
		if (focus) {
			final int side = h / 2;
			//
			upTriangleRect.x = x + (w - side) / 2;
			upTriangleRect.y = y - OFFSET - side;
			upTriangleRect.w = side;
			upTriangleRect.h = side;
			//
			downTriangleRect.x = upTriangleRect.x;
			downTriangleRect.y = y + h + OFFSET;
			downTriangleRect.w = side;
			downTriangleRect.h = side;
			//
			g.setColor(frameHeaderColor);
			UIUtil.drawTriangle( // up triangle.
				g, upTriangleRect.x, upTriangleRect.y, side, false);
			UIUtil.drawTriangle( // down triangle.
				g, downTriangleRect.x, downTriangleRect.y, side, true);
			//
			g.setColor(bodyFontColor);
			g.fillRect(x, y, w, h); // background.
		}
		//
		g.setColor(frameHeaderColor);
		g.drawRect(x, y, w, h);
		g.drawRect(x + 1, y + 1, w - 2, h - 2);
		//
		g.setColor(focus ? backgroundColor : bodyFontColor);
		g.setFont(simpleDateFont);
		g.drawString(part, x
			+ UIUtil.alignString(part, w, UIUtil.ALIGN_CENTER,
					simpleDateFont), y
			+ UIUtil.alignString(part, h, UIUtil.ALIGN_MIDDLE,
					simpleDateFont), Graphics.TOP | Graphics.LEFT);
	}

	/**
	 * <p>
	 * Load the days of a given month and year.
	 * </p>
	 * @param month Month.
	 * @param year Year.
	 */
	void loadDays(int month, int year) {
		cleanupDayMatrix();
		//
		int day = 1;
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
		calendar.set(java.util.Calendar.MONTH, month);
		calendar.set(java.util.Calendar.YEAR, year);
		//
		int firstDay = -1;
		switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
		case java.util.Calendar.SUNDAY:
			firstDay = 0;
			break;
		case java.util.Calendar.MONDAY:
			firstDay = 1;
			break;
		case java.util.Calendar.TUESDAY:
			firstDay = 2;
			break;
		case java.util.Calendar.WEDNESDAY:
			firstDay = 3;
			break;
		case java.util.Calendar.THURSDAY:
			firstDay = 4;
			break;
		case java.util.Calendar.FRIDAY:
			firstDay = 5;
			break;
		case java.util.Calendar.SATURDAY:
			firstDay = 6;
			break;
		}
		//
		fillupDayMatrix(firstDay, day, month, year);
	}

	/**
	 * <p>
	 * Scroll the date on simple date view mode.
	 * </p>
	 * @param dir Direction
	 * @see javax.microedition.lcdui.Canvas#UP
	 * @see javax.microedition.lcdui.Canvas#DOWN
	 */
	void scrollSimpleDate(int dir) {
		for (int i = 0; i < dateFormat.length; i++) {
			if (StringUtil.equalsIgnoreCase(dateFormat[i], "yyyy") && i == sDateIdxFocus) {
				selYear += dir == UP ? 1 : -1;
				break;
			} else if (StringUtil.equalsIgnoreCase(dateFormat[i], "MM")
					&& i == sDateIdxFocus) {
				selMonth += dir == UP ? 1 : -1;
				break;
			} else if (i == sDateIdxFocus) { // &&
												// dateFormat[i].equalsIgnoreCase("dd")
				selDay += dir == UP ? 1 : -1;
				break;
			}
		}
		//
		if (selYear > 2070) {
			selYear = 2070;
		} else if (selYear < 1970) {
			selYear = 1970;
		}
		//
		if (selMonth == 12) {
			selMonth = 0;
		} else if (selMonth == -1) {
			selMonth = 11;
		}
		//
		if (selDay > 28) {
			if (selMonth == 1) { // february
				if (!DateUtil.isLeapYear(selYear) || selDay > 29) {
					selDay = 1;
				}
			} else if (selDay == 31 && !DateUtil.is31DaysMonth(selMonth) ||
					   selDay == 32) {
				selDay = 1;
			}
		} else if (selDay == 0) {
			if (selMonth == 1) { // february
				selDay = DateUtil.isLeapYear(selYear) ? 29 : 28;
			} else {
				selDay = DateUtil.is31DaysMonth(selMonth) ? 31 : 30;
			}
		}
		//
		requestRepaint();
	}

	/**
	 * <p>
	 * Move the selected day on calendar view.
	 * </p>
	 * @param dir Direction.
	 * @see javax.microedition.lcdui.Canvas#UP
	 * @see javax.microedition.lcdui.Canvas#DOWN
	 */
	void moveSelectedDay(int dir) {
		boolean reachBounds = false;
		boolean dayChange = false;
		boolean monthChange = false;
		boolean yearChange = false;
		//
		if (dir == UP) {
			if (dayY - 1 >= 0) {
				dayY--; // move to line above.
			} else {
				reachBounds = true;
			}
		} else if (dir == DOWN) {
			if (dayY + 1 < days.length) {
				dayY++; // move to line bellow.
			} else {
				reachBounds = true;
			}
		} else if (dir == LEFT) {
			if (dayX - 1 >= 0) {
				dayX--; // move to previous column.
			} else if (dayY - 1 >= 0) {
				// move to previous line.
				dayX = days[0].length - 1;
				dayY--;
			} else {
				reachBounds = true;
			}
		} else if (dir == RIGHT) {
			if (dayX + 1 < days[0].length) {
				dayX++; // move to next column.
			} else if (dayY + 1 < days.length) {
				// move to next line.
				dayX = 0;
				dayY++;
			} else {
				reachBounds = true;
			}
		}
		//
		dayChange = true;
		//
		selDay = days[dayY][dayX];
		if (selDay == -1 || reachBounds) {
			if (dir == UP || dir == LEFT) {
				selMonth--;
			} else {
				selMonth++;
			}
			//
			monthChange = true;
			//
			if (selMonth == -1) {
				selMonth = 11;
				selYear--;
				//
				yearChange = true;
			} else if (selMonth == 12) {
				selMonth = 0;
				selYear++;
				//
				yearChange = true;
			}
			//
			loadDays(selMonth, selYear);
			//
			switch (dir) {
			case UP:
				dayY = days.length - 1;
				while (days[dayY][dayX] == -1) {
					dayY--;
				}
				selDay = days[dayY][dayX];
				break;
			case DOWN:
				dayY = 0;
				while (days[dayY][dayX] == -1) {
					dayY++;
				}
				selDay = days[dayY][dayX];
				break;
			case LEFT:
				selDay = 27;
				while (!DateUtil.isLastDayOfMonth(++selDay, selMonth, selYear));
				break;
			case RIGHT:
				selDay = 1;
				break;
			}
			//
			setDayMatrixCoords(selDay);
		}
		//
		//
		if (dayChange) {
			onDayChange(); // notify day change.
		}
		if (monthChange) {
			onMonthChange(); // notify month change.
		}
		if (yearChange) {
			onYearChange(); // notify year change.
		}
		//
		requestRepaint();
	}

	/**
	 * <p>
	 * Set the day's coordinates in the days matrix.
	 * </p>
	 * @param day Day.
	 * @return Valid day or not.
	 */
	boolean setDayMatrixCoords(int day) {
		for (int i = days.length - 1; i >= 0; i--) {
			for (int j = days[0].length - 1; j >= 0; j--) {
				if (days[i][j] == day) {
					dayY = i;
					dayX = j;
					return true;
				}
			}
		}
		//
		return false;
	}

	/**
	 * <p>
	 * Fill up the days matrix with the days of a fiven month and year.
	 * </p>
	 * @param firstDay First day in month.
	 * @param day Day.
	 * @param month Month.
	 * @param year Year.
	 */
	void fillupDayMatrix(int firstDay, int day, int month, int year) {
		//filling up the matrix with the days of month.
		days_month_loop: {
			for (int i = 0; i < days.length; i++) {
				for (int j = firstDay; j < days[0].length; j++) {
					days[i][j] = day;
					if (day >= 28 && 
						DateUtil.isLastDayOfMonth(day, month, year)) {
						break days_month_loop;
					}
					//
					day++;
				}
				//
				firstDay = 0;
			}
		}
	}

	/**
	 * <p>
	 * Clean up the days matrix.
	 * </p>
	 */
	void cleanupDayMatrix() {
		for (int i = days.length - 1; i >= 0; i--) {
			for (int j = days[0].length - 1; j >= 0; j--) {
				days[i][j] = -1;
			}
		}
	}

	/**
	 * <p>
	 * Get the preferred height.
	 * </p>
	 * @return Height.
	 */
	int getPreferredHeight() {
		final int header = bodyFont.getHeight() + OFFSET;
		final int frame = header * (WEEK_COUNT_MOUNTH + 1); // +1 for the frame header.
		//
		return header + frame;
	}

	/**
	 * <p>
	 * Get the preferred width.
	 * </p>
	 * @return Width.
	 */
	int getPreferredWidth() {
		final int square = getWidth() / DAY_COUNT_WEEK;
		//
		return square * DAY_COUNT_WEEK;
	}

	/**
	 * <p>
	 * Get the date part position.
	 * </p>
	 * @param part Date part.
	 * @return Position.
	 */
	int getDatePartPos(String part) {
		for (int i = dateFormat.length - 1; i >= 0; i--) {
			if (StringUtil.equalsIgnoreCase(dateFormat[i], part)) {
				return i;
			}
		}
		//
		throw new IllegalArgumentException("Invalid date part: " + part);
	}
}