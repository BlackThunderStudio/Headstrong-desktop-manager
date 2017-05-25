package com.headstrongpro.desktop.view;

/**
 * ContextView
 */
public abstract class ContextView<T> {

    protected T contextItem = null;

    public void changeContextItem(T t) {
        this.contextItem = t;
        setFields();
    }

    public abstract void setFields();
}
