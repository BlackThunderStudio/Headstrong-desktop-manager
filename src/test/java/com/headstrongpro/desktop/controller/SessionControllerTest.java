package com.headstrongpro.desktop.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 31-May-17.
 */
public class SessionControllerTest {
    SessionController sessionController;

    @Before
    public void setUp() throws Exception {
        sessionController = new SessionController();
    }

    @Test
    public void getAllSessionsTest() throws Exception{
        assertNotNull(sessionController.getAllSessions());
    }

}