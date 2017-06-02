package com.headstrongpro.desktop.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 31-May-17.
 */
public class CourseControllerTest {
    CourseController courseController;

    @Before
    public void setUp() throws Exception {
        courseController = new CourseController();
    }

    @Test
    public void getAllCoursesTest() throws Exception{
        assertNotNull(courseController.getAllCourses());
    }

}