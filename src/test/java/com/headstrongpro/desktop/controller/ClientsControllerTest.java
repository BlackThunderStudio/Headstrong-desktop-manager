package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * clients controler test
 */
public class ClientsControllerTest {
    private ClientsController clientsController;
    private DBClient clientDAO;

    @Before
    public void setUp() throws Exception {
        clientsController = new ClientsController();
        clientDAO = new DBClient();
        clientsController.refresh();
    }

    @Test
    public void getClients() throws Exception {
        assertNotNull(clientsController.getAll());
    }

    @Test
    public void search() throws Exception {
        assertEquals("johnnn99@neasenergy.com", clientsController.searchByPhrase("john").get(0).getEmail());
    }

}