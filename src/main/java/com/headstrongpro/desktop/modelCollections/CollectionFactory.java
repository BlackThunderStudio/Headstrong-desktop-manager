package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.CourseCategory;
import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.model.Subscription;
import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.model.entity.Person;
import com.headstrongpro.desktop.model.resource.Resource;

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
        if (type.equals(CollectionType.DEPARTMENT)){
            return (IDataAccessObject<T>) new DBDepartments();
        }
        if (type.equals(CollectionType.USER)) {
            return (IDataAccessObject<T>) new DBUser();
        }
        if (type.equals(CollectionType.CLIENT)) {
            return (IDataAccessObject<T>) new DBClient();
        }
        if (type.equals(CollectionType.SUBSCRIPTION)) {
            return (IDataAccessObject<T>) new DBSubscriptions();
        }
        return null;
    }


    //alternate initializer
    public static IDataAccessObject<Company> getCompanyDAO(){
        return new DBCompany();
    }

    public static IDataAccessObject<CourseCategory> getCourseCategoryDAO(){
        return new DBCourseCategory();
    }

    public static IDataAccessObject<Resource> getResourceDAO(){
        return new DBResources();
    }

    public static IDataAccessObject<Department> getDepartmentDAO(){
        return new DBDepartments();
    }

    public static IDataAccessObject<Person> getUserDAO(){
        return new DBUser();
    }

    public static IDataAccessObject<Person> getClientDAO(){
        return new DBClient();
    }

    public static IDataAccessObject<Subscription> getSubscriptionDAO(){
        return new DBSubscriptions();
    }
}

enum CollectionType {
    COMPANY,
    COURSE_CATEGORY,
    RESOURCE,
    DEPARTMENT,
    USER,
    CLIENT,
    SUBSCRIPTION
}
