package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.model.Course;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 16-May-17.
 */
public class DBCourseTest {
    private DBCourse courseDAO;

    @Before
    public void setUp() throws Exception {
        courseDAO = new DBCourse();
    }

    @Test
    public void getAll() throws Exception {
        assertNotNull("Courses object is null", courseDAO.getAll());
        assertNotEquals("Courses object is empty", 0, courseDAO.getAll().size());
    }

    @Test
    public void getById() throws Exception {
        assertEquals("Wrong course name", "The Work Week", courseDAO.getById(4).getName());
    }

    @Ignore
    @Test
    public void create() throws Exception {
        /*Course newSess = new Course(402, "test course", "how to dummy for courses");
        int oldSize = courseDAO.getAll().size();
        courseDAO.persist(newSess);
        assertNotEquals("Course has not been added", oldSize, courseDAO.getAll().size());
        courseDAO.delete(newSess);
        assertEquals("Course has not been removed", oldSize, courseDAO.getAll().size());
    */
    }

    @Test
    public void update() throws Exception {
        Course sess = courseDAO.getById(4);
        String oldName = sess.getName(), newName = "Test purpose training";
        sess.setName(newName);
        courseDAO.update(sess);
        assertNotEquals("Name has not been changed", oldName, courseDAO.getById(4).getName());
        sess.setName(oldName);
        courseDAO.update(sess);
        assertEquals("Name has not been reverted back", oldName, courseDAO.getById(4).getName());
    }

}