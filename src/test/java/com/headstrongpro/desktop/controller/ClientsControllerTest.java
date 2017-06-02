package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * clients controler test
 */
public class ClientsControllerTest {
    ClientsController clientsController;
    DBClient clientDAO;

    @Before
    public void setUp() throws Exception {
        clientsController = new ClientsController();
        clientDAO = new DBClient();
    }

    @Test
    public void getClients() throws Exception {
        assertNotNull(clientsController.getClients());
    }

    @Test
    public void search() throws Exception {
        assertEquals("johnnn99@neasenergy.com", clientsController.search("john").get(0).getEmail());
    }

}