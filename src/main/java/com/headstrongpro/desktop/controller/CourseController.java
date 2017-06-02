package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.DbLayer.DBResources;
import com.headstrongpro.desktop.DbLayer.DBCourse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by rajmu on 17.05.14.
 */
public class CourseController implements Refreshable, IContentController<Course> {

    private DBCourse dbCourse;
    private DBResources dbResources;

    public CourseController(){
        dbCourse = new DBCourse();
        dbResources = new DBResources();
    }

    @Override
    public ObservableList<Course> getAll() throws ModelSyncException {
        List<Course> courses = dbCourse.getAll();
        courses.forEach(e -> {
            try {
                e.setResources(dbResources.getByCourseID(e.getId()));
            } catch (ModelSyncException e1) {
                e1.printStackTrace();
            }
        });
        return FXCollections.observableArrayList(courses);
    }

    @Override
    public Course createNew(Course course) throws ModelSyncException{
        if(course != null) throw new IllegalArgumentException("Cannot be null");
        return dbCourse.persist(course);
    }

    @Override
    public void edit(Course course){

    }

    @Override
    public void delete(Course course){

    }

    @Override
    public Course getByID(int id){
        return null;
    }

    @Override
    public ObservableList<Course> searchByPhrase(String phrase){
        return null;
    }

    @Override
    public void refresh(){

    }
}
