package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.resource.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Time;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBResourcesTest {
    private DBConnect connect;
    private DBResources resourcesDAO;
    @Before
    public void setUp() throws Exception {
        connect = new DBConnect();
        resourcesDAO = new DBResources();
    }

    @Test
    public void getAll() throws Exception {
        List<Resource> resources = resourcesDAO.getAll();

        assertNotEquals(0, resources.size());
    }

    @Test
    public void getById() throws Exception {
        Resource resource = resourcesDAO.getById(3);
        assertEquals("ImageResource", resource.getClass().getSimpleName());
    }

    @Test
    public void createDelete() throws Exception {
        int tablesize = resourcesDAO.getAll().size();

        AudioResource resource = Resource.as(ResourceFactory.getResource("some test resource", "desc", false, 3));
        resource.setUrl("htttp://google.com/xxx.mp3");
        resource.setDuration(Time.valueOf("12:00:00"));
        resourcesDAO.create(resource);

        int newsize = resourcesDAO.getAll().size();

        assertNotEquals(tablesize, newsize);

        resourcesDAO.delete(resource);

        assertEquals(tablesize, resourcesDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        String oldVal = "https://randomlink.sleeppart1.wav";
        Resource r = resourcesDAO.getById(10);
        AudioResource ar = Resource.as(r);
        String expected = "http://www.example.com/qwerty.mp3";
        ar.setUrl(expected);

        resourcesDAO.update(ar);
        AudioResource ar2 = Resource.as(resourcesDAO.getById(10));

        assertEquals(expected, ar2.getUrl());

        ar2.setUrl(oldVal);
        resourcesDAO.update(ar2);
    }

    @Test
    public void getByTypeTest() throws Exception {
        List<Resource> resources = resourcesDAO.getbyType(2);

        assertNotNull(resources);
        assertNotEquals(0, resources.size());
    }

    @Test
    public void getBySessionId() throws Exception {
        List<Resource> resources = resourcesDAO.getBySessionID(4);

        assertNotNull(resources);
        assertEquals(8, resources.size());
    }
}