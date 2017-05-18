package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.CourseCategory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * course category tests
 */
public class DBCourseCategoryTest {
    private DBCourseCategory ccDAO;

    @Before
    public void setUp() throws Exception {
        ccDAO = new DBCourseCategory();
    }

    @Test
    public void getAll() throws Exception {
        List<CourseCategory> courseCategories = ccDAO.getAll();

        assertNotNull("Course categories object is null", courseCategories);
        assertNotEquals("Course categories table empty", 0, courseCategories);
    }

    @Test
    public void getById() throws Exception {
        CourseCategory cc = ccDAO.getById(1);

        assertNotNull("Course category object is null", cc);
    }

    @Test
    public void createDelete() throws Exception {
        int oldSize = ccDAO.getAll().size(), newSize;
        CourseCategory newCat = new CourseCategory("dummy category");
        ccDAO.create(newCat);
        assertNotEquals("Course category was not created", oldSize, ccDAO.getAll().size());
        ccDAO.delete(newCat);
        assertEquals("Course category was not deleted", oldSize, ccDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        CourseCategory cc = ccDAO.getById(1);
        String oldName = cc.getName();
        String newName = "dummy";
        cc.setName(newName);
        ccDAO.update(cc);
        assertNotEquals("Name has not been changed", oldName, ccDAO.getById(1).getName());
        cc.setName(oldName);
        ccDAO.update(cc);
        assertEquals("Name was not reversed", oldName, ccDAO.getById(1).getName());
    }

    @Test(expected = ModelSyncException.class)
    public void emptyNameCourse() throws Exception{
        ccDAO.create(new CourseCategory(""));
    }

}