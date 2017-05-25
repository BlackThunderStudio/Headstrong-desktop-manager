package com.headstrongpro.desktop.view;

/**
 * ContextView
 */
public abstract class ContextView<T> {

    protected T contextItem = null;
    protected MainWindowView mainWindowView;

    public void changeContextItem(T t) {
        this.contextItem = t;
        setFields();
    }

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    public abstract void setFields();
}
