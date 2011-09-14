///*
// * GroupedItem.java
// * 03/12/2006
// * JME Framework
// * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
// * All rights reserved
// */
//package br.framework.jme.ui;
//
//import java.util.Vector;
//
///**
// * <p>
// * Class that represent an item that can be appended into a Grouped List.
// * </p>
// * 
// * @author Ernandes Mourao Junior (ernandes@gmail.com)
// * @version 1.0
// * @since 1.0
// * @see br.framework.jme.ui.GroupedList
// */
//public class GroupedItem {
//    /**
//     * Null value representation used by the grouped item.
//     */
//	//TODO: tratar compatibilidade com MIDP1.
//    protected static final float NULL_VALUE = Float.MIN_VALUE;
//    //
//
//	/**
//	 * Vector that contains all the children items.
//	 */
//	protected Vector children;
//	
//	/**
//	 * Item description.
//	 */
//	protected String description;
//	
//	/**
//	 * Item detail.
//	 */
//	protected String detail;
//
//	/**
//	 * Item value.
//	 */
//	protected float value;
//	
//	/**
//	 * If the item is expanded or not.
//	 */
//	protected boolean expanded;
//	
//	/**
//	 * Item parent.
//	 */
//	protected GroupedItem parent;
//
//	/**
//	 * <p>
//	 * Constructor.
//	 * </p>
//	 * @param description Item description.
//	 * @param value Item value.
//	 */
//	public GroupedItem(String description, float value) {
//		this(description, "", value);
//	}
//
//	/**
//	 * <p>
//	 * Constructor.
//	 * </p>
//	 * @param description Item description.
//	 * @param detail Item detail.
//	 * @param value Item value.
//	 */
//	public GroupedItem(String description, String detail, float value) {
//		this.description = description;
//		this.value = value;
//		this.detail = detail;
//	}
//
//	/**
//	 * <p>
//	 * Constructor.
//	 * </p>
//	 * @param description Item description.
//	 * @param detail Item detail.
//	 */
//	public GroupedItem(String description, String detail) {
//		this(description, detail, NULL_VALUE);
//	}
//
//	/**
//	 * <p>
//	 * Constructor.
//	 * </p>
//	 * @param description Item description.
//	 */
//	public GroupedItem(String description) {
//		this(description, "", NULL_VALUE);
//	}
//	
//	/**
//	 * <p>
//	 * Get the item description.
//	 * </p>
//	 * @return Description.
//	 */
//	public String getDescription() {
//		return description;
//	}
//
//	/**
//	 * <p>
//	 * Get the item detail.
//	 * </p>
//	 * @return Detail.
//	 */
//	public String getDetail() {
//		return detail;
//	}
//
//	/**
//	 * <p>
//	 * Add a given item.
//	 * </p>
//	 * @param item Item.
//	 */
//	public void add(GroupedItem item) {
//		if (children == null) {
//			children = new Vector();
//		}
//		item.parent = this;
//		children.addElement(item);
//	}
//
//	/**
//	 * <p>
//	 * Get all the children items.
//	 * </p>
//	 * @return Items.
//	 */
//	public GroupedItem[] getChildren() {
//		if (children != null) {
//			GroupedItem[] items = new GroupedItem[children.size()];
//			children.copyInto(items);
//			return items;
//		} else {
//			return new GroupedItem[0];
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Get the item parent.
//	 * </p>
//	 * @return Parent.
//	 */
//	public GroupedItem getParent() {
//		return parent;
//	}
//	
//	/**
//	 * <p>
//	 * Verify if a given item is a child.
//	 * </p>
//	 * @param item Item.
//	 * @return Child or not.
//	 */
//	public boolean isChild(GroupedItem item) {
//		if (children != null) {
//			return children.indexOf(item) != -1;
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * <p>
//	 * If the item is expanded or not.
//	 * </p>
//	 * @return Expanded or not.
//	 */
//	public boolean isExpanded() {
//		return children == null || expanded;
//	}
//	
//	/**
//	 * <p>
//	 * Set the item as expanded or not.
//	 * </p>
//	 * @param enabled Enabled or not.
//	 */
//	public void setExpanded(boolean enabled) {
//		expanded = enabled;
//	}
//
//	/**
//	 * <p>
//	 * Get the item value. If the item has children, the value returned is the
//	 * sum of value from all the children.
//	 * </p>
//	 * @return Value.
//	 */
//	public float getValue() {
//		if (children != null && children.size() > 0) {
//			int ignoredValues = 0;
//            float v;
//            float total = 0;
//			GroupedItem item = null;
//			for (int i = children.size() -1; i >= 0; i--) {
//				item = (GroupedItem)children.elementAt(i);
//                v = item.getValue();
//                if (v != NULL_VALUE) {
//                    total += v;
//                } else {
//                    ignoredValues++;
//                }
//			}
//            if (ignoredValues != children.size()) {
//                return total;
//            } else {
//                return NULL_VALUE;
//            }
//		} else {
//			return value;
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Remove a given item from the children list.
//	 * </p>
//	 * @param item Item.
//	 * @return If the item was removed or not.
//	 */
//	public boolean remove(GroupedItem item) {
//		if (children != null) {
//			return children.removeElement(item);
//		} else {
//			return false;
//		}
//	}
//
//	/**
//	 * @inheritDoc
//	 * @see java.lang.Object#toString()
//	 */
//	public String toString() {
//		return description;
//	}
//}