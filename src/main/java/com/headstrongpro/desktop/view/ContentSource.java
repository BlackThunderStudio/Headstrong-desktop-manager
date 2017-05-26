package com.headstrongpro.desktop.view;

/**
 * Content Sources
 */
public enum ContentSource {

    RESOURCES("Resources", "/layout/resources/resourcesContentPane.fxml", "/layout/resources/resourcesContextDefault.fxml"),

    ABOUT("About", "/layout/about.fxml", "'layout/contextDefault.fxml"),

    COMPANIES("Companies", "/layout/companies/companiesContentPane.fxml", "/layout/companies/companiesContextPane.fxml"),
    COMPANIES_NEW("New company", "/layout/companies/companiesContentPane.fxml", "/layout/companies/companiesNewPane.fxml"),
    CLIENTS("Clients", "/layout/clients/clientsContentPane.fxml", "/layout/clients/clientsContextPane.fxml");

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
