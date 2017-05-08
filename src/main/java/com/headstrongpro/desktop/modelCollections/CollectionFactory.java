package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.entity.Company;

/**
 * Created by Rajmund Staniek on 08-May-17.
 */
public class CollectionFactory<T> {

    public IDataAccessObject<T> getCollection(CollectionType type) {
        if (type == null) return null;
        if (type.equals(CollectionType.COMPANY)) {
            return (IDataAccessObject<T>) new DBCompany();
        }
        if (type.equals(CollectionType.COURSE_CATEGORY)) {
            return (IDataAccessObject<T>) new DBCourseCategory();
        }
        if (type.equals(CollectionType.RESOURCE)) {
            return (IDataAccessObject<T>) new DBResources();
        }
        return null;
    }
}

enum CollectionType {
    COMPANY,
    COURSE_CATEGORY,
    RESOURCE
}
