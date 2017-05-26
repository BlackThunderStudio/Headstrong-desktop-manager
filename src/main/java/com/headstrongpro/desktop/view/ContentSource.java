package com.headstrongpro.desktop.view;

/**
 * Content Sources
 */
public enum ContentSource {

    COMPANIES("Companies", "/layout/companies/companiesContentPane.fxml", "/layout/companies/companiesContextPane.fxml"),
    COMPANIES_NEW("New company", "/layout/companies/companiesContentPane.fxml", "/layout/companies/companiesNewPane.fxml");

    private String name, contentView, contextView;

    ContentSource(String name, String contentView, String contextView) {
        this.name = name;
        this.contentView = contentView;
        this.contextView = contextView;
    }

    public String getName() {
        return name;
    }

    public String getContentView() {
        return contentView;
    }

    public String getContextView() {
        return contextView;
    }
}
