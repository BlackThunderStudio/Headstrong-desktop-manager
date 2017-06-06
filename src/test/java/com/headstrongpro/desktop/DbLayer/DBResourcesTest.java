package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceFactory;
import org.junit.Before;
import org.junit.Test;

import java.sql.Time;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBResourcesTest {
    private DBResources resourcesDAO;

    @Before
    public void setUp() throws Exception {
        new DBConnect();
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

        AudioResource resource = Resource.ofType(ResourceFactory.getResource("some test resource", "desc", false, 3));
        resource.setUrl("htttp://google.com/xxx.mp3");
        resource.setDuration(Time.valueOf("12:00:00"));
        resourcesDAO.persist(resource);

        int newsize = resourcesDAO.getAll().size();

        assertNotEquals(tablesize, newsize);

        resourcesDAO.delete(resource);

        assertEquals(tablesize, resourcesDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        String oldVal = "https://randomlink.sleeppart1.wav";
        Resource r = resourcesDAO.getById(10);
        AudioResource ar = Resource.ofType(r);
        String expected = "http://www.example.com/qwerty.mp3";
        assert ar != null;
        ar.setUrl(expected);

        resourcesDAO.update(ar);
        AudioResource ar2 = Resource.ofType(resourcesDAO.getById(10));

        assert ar2 != null;
        assertEquals(expected, ar2.getUrl());

        ar2.setUrl(oldVal);
        resourcesDAO.update(ar2);
    }

    @Test
    public void getByTypeTest() throws Exception {
        List<Resource> resources = resourcesDAO.getByType(2);

        assertNotNull(resources);
        assertNotEquals(0, resources.size());
    }

    @Test
    public void getBySessionId() throws Exception {
        List<Resource> resources = resourcesDAO.getByCourseID(4);

        assertNotNull(resources);
        assertEquals(8, resources.size());
    }

    @Test
    public void getBySessionID2() throws Exception {
        List<Resource> resources = resourcesDAO.getByCourseID(5);

        assertNotNull(resources);
        assertEquals(4, resources.size());
    }
}