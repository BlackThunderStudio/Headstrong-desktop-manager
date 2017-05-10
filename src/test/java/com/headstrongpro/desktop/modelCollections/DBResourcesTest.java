package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceFactory;
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
        assertEquals("PhotoResource", resource.getClass().getSimpleName());
    }

    @Test
    public void create() throws Exception {
        int tablesize = resourcesDAO.getAll().size();

        AudioResource resource = Resource.as(ResourceFactory.getResource("some test resource", "desc", false, 3), AudioResource.class);
        resource.setUrl("htttp://google.com/xxx.mp3");
        resource.setDuration(Time.valueOf("12:00:00"));
        resourcesDAO.create(resource);

        int newsize = resourcesDAO.getAll().size();

        assertNotEquals(tablesize, newsize);
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
    public void getByTypeTest() throws Exception {

    }

}