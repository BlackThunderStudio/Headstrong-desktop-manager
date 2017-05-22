package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.09.
 */
public class DBClientTest {

    private DBClient clientDAO;

    @Before
    public void setUp() throws Exception {
        clientDAO = new DBClient();
    }

    @Test
    public void getAll() throws Exception {
        List<Person> people = clientDAO.getAll();

        assertNotNull(people);
        assertNotEquals(0, people.size());
    }

    @Test
    public void getById() throws Exception {
        Client p = (Client) clientDAO.getById(4);
        assertEquals("Tom Christiansen", p.getName());
    }

    @Test
    public void createDelete() throws Exception {
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        Client client = new Client("dummy dumson", "dummy@testing.dk", "332", "true", "dummy", "124", date, 1);
        int oldSize = clientDAO.getAll().size();
        clientDAO.persist(client);
        assertNotEquals("Client has not been added", oldSize, clientDAO.getAll().size());
        clientDAO.delete(client);
        assertEquals("Client has not been removed", oldSize, clientDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        Client client = (Client) clientDAO.getById(1);
        String oldName, newName = "dummy";
        oldName = client.getName();
        client.setName(newName);
        clientDAO.update(client);
        assertNotEquals("Client's name has not been changed", oldName, clientDAO.getById(1).getName());
        client.setName(oldName);
        clientDAO.update(client);
        assertEquals("Client's name has not been reverted", oldName, clientDAO.getById(1).getName());
    }

    @Test
    public void getByCompanyId() throws Exception {
        List<Person> clients =  clientDAO.getByCompanyId(1);
        assertNotEquals("No clients were found for the company specified", 0, clients.size());
    }

}