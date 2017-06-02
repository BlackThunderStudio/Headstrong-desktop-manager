package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.DbLayer.DBResources;
import com.headstrongpro.desktop.DbLayer.DBCourse;

import java.util.List;

/**
 * Created by rajmu on 17.05.14.
 */
public class CourseController {

    private DBCourse dbCourse;
    private DBResources dbResources;

    public CourseController(){
        dbCourse = new DBCourse();
        dbResources = new DBResources();
    }

    public List<Course> getAllCourses() throws ModelSyncException {
        List<Course> courses = dbCourse.getAll();
        courses.forEach(e -> {
            try {
                e.setResources(dbResources.getByCourseID(e.getId()));
            } catch (ModelSyncException e1) {
                e1.printStackTrace();
            }
        });
        return courses;
    }
}
