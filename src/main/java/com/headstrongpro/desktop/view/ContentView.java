package com.headstrongpro.desktop.view;

/**
 * ContentView
 */
public abstract class ContentView {

    protected ContextView contextView;

    void setContextView(ContextView contextView) {
        this.contextView = contextView;
    }
}
