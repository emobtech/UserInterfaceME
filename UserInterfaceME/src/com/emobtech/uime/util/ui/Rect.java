/*
 * Rect.java
 * 05/12/2006
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util.ui;

/**
 * <p>
 * Define a rectangle structure.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Rect {
	/**
	 * <p>
	 * X coordinate.
	 * </p>
	 */
	public int x;

	/**
	 * <p>
	 * Y coordinate.
	 * </p>
	 */
	public int y;

	/**
	 * <p>
	 * Width.
	 * </p>
	 */
	public int w;

	/**
	 * <p>
	 * Height.
	 * </p>
	 */
	public int h;

	/**
	 * <p>
	 * Default constructor.
	 * </p>
	 */
	public Rect() {
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * 
	 * @param rect Rectangle.
	 */
	public Rect(Rect rect) {
		this.x = rect.x;
		this.y = rect.y;
		this.w = rect.w;
		this.h = rect.h;
	}

	/**
	 * <p>
	 * Verify if a given point(x,y) is inside of the rectangle.
	 * </p>
	 * 
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Point is contained in the rectangle.
	 */
	public boolean contains(int x, int y) {
		return x >= this.x && x <= this.x + w && y >= this.y && y <= this.y + h;
	}

	/**
	 * <p>
	 * Verify if a given rectangle is inside of the rectangle.
	 * </p>
	 * 
	 * @param rect Rectangle.
	 * @return Rectangle is contained in the rectangle.
	 */
	public boolean contains(Rect rect) {
		if (contains(rect.x, rect.y)) {
			return rect.x + rect.w <= x + w && rect.y + rect.h <= y + h;
		} else {
			return false;
		}
	}
}