package com.headstrongpro.desktop.view;

/**
 * Content Sources
 */
public enum ContentSource {

    DASHBOARD("Dashboard", "/dashboardPane.fxml", null),

    RESOURCES("Resources", "/resources/resourcesContentPane.fxml", "/resources/resourcesContextDefault.fxml"),
    RESOURCES_IMAGE("Image resources", "/resources/resourcesContentPane.fxml", "/resources/resourcesImagePane.fxml"),
    RESOURCES_AUDIO("Audio resources", "/resources/resourcesContentPane.fxml", "/resources/resourcesAudioPane.fxml"),
    RESOURCES_TEXT("Text resources", "/resources/resourcesContentPane.fxml", "/resources/resourcesTextPane.fxml"),
    RESOURCES_NEW_TEXT("New text resource", "/resources/resourcesContentPane.fxml", "/resources/resourcesNewTextPane.fxml"),
    RESOURCES_NEW_FILE("New resource", "/resources/resourcesContentPane.fxml", "/resources/resourcesNewFilePane.fxml"),

    COMPANIES("Companies", "/companies/companiesContentPane.fxml", "/companies/companiesContextPane.fxml"),
    COMPANIES_NEW("New company", "/companies/companiesContentPane.fxml", "/companies/companiesNewPane.fxml"),

    PAYMENTS("Payments", "/payments/paymentsContentPane.fxml", "/payments/paymentsContextPane.fxml"),

    CLIENTS("Clients", "/clients/clientsContentPane.fxml", "/clients/clientsContextPane.fxml"),
    CLIENTS_NEW("Clients", "/clients/clientsContentPane.fxml", "/clients/clientsContextNewPane.fxml"),

    SUBSCRIPTIONS("Subscriptions", "/subscriptions/subscriptionsContentPane.fxml", "/subscriptions/subscriptionsContextPane.fxml"),
    SUBSCRIPTIONS_NEW("New subscription", "/subscriptions/subscriptionsContentPane.fxml", "/subscriptions/subscriptionsNewContextPane.fxml"),

    COURSES("Courses", "/courses/coursesContentPane.fxml", "/courses/coursesContextPane.fxml"),
    COURSES_CATEGORY("Courses", "/courses/coursesContentPane.fxml", "/courses/coursesCategoryContextPane.fxml"),
    COURSES_NEW("Courses", "/courses/coursesContentPane.fxml", "/courses/coursesNewCourseContextPane.fxml"),
    COURSES_NEW_CATEGORY("Courses", "/courses/coursesContentPane.fxml", "/courses/coursesNewCategoryContextPane.fxml"),
    COURSES_RES_AUDIO("Courses", "/courses/coursesContentPane.fxml", "/courses/coursesResContext.fxml"),
    COURSES_RES_INSPECT("Course Inspection", "/courses/coursesContentPane.fxml", "/courses/coursesInspectContext.fxml"),

    DEPARTMENTS("Departments", "/departments/departmentsContentPane.fxml", "/departments/departmentsContextPane.fxml"),

    ABOUT("About", "/about.fxml", null),

    SETTINGS("Settings", "/settings/settingsContent.fxml", null);

    private String name, contentViewPath, contextViewPath;

    ContentSource(String name, String contentViewPath, String contextViewPath) {
        this.name = name;
        this.contentViewPath = contentViewPath;
        this.contextViewPath = contextViewPath;
    }

    public String getName() {
        return name;
    }

    public String getContentViewPath() {
        return (contentViewPath != null) ? "/layout" + contentViewPath : null;
    }

    public String getContextViewPath() {
        return (contextViewPath != null) ? "/layout" + contextViewPath : null;
    }
}
