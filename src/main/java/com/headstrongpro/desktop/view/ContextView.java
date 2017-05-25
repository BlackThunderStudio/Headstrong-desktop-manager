package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.fxControls.Footer;

/**
 * ContextView
 */
public abstract class ContextView<T> {

    protected T contextItem = null;
    protected Footer footer;

    public void changeContextItem(T t) {
        this.contextItem = t;
        setFields();
    }

    public void setFooter(Footer footer){
        this.footer = footer;
    }

    public abstract void setFields();
}
