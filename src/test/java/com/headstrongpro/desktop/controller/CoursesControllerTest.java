package com.headstrongpro.desktop.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by 1062085 on 31-May-17.
 */
public class CoursesControllerTest {
    CoursesController coursesController;

    @Before
    public void setUp() throws Exception {
        coursesController = new CoursesController();
    }

    @Test
    public void getAllCoursesTest() throws Exception {
        assertNotNull(coursesController.getAll());
    }

}