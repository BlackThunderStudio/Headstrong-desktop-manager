package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.entity.Client;
import com.headstrongpro.desktop.model.entity.Person;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.09.
 */
public class DBClientTest {

    private IDataAccessObject dao;

    @Before
    public void setUp() throws Exception {
        dao = CollectionFactory.getClientDAO();
    }

    @Test
    public void getAll() throws Exception {
        List<Client> people = dao.getAll();

        assertNotNull(people);
        assertNotEquals(0, people.size());
    }

    @Test
    public void getById() throws Exception {
        Client p = (Client) dao.getById(4);
        assertEquals("Tom Christiansen", p.getName());
    }

    @Ignore(value = "To be implemented")
    @Test
    public void create() throws Exception {

    }

    @Ignore(value = "To be implemented")
    @Test
    public void update() throws Exception {

    }

    @Ignore(value = "To be implemented")
    @Test
    public void delete() throws Exception {

    }

    @Ignore(value = "To be implemented")
    @Test
    public void getByCompanyId() throws Exception {

    }

}