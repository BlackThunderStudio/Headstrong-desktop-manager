package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.resource.Resource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBResourcesTest {
    private DBConnect connect;
    private CollectionFactory<Resource> resourceCollectionFactory;
    private IDataAccessObject<Resource> resourcesDAO;
    @Before
    public void setUp() throws Exception {
        connect = new DBConnect();
        resourceCollectionFactory = new CollectionFactory<>();
        resourcesDAO = resourceCollectionFactory.getCollection(CollectionType.RESOURCE);
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
    public void getByTypeTest() throws Exception {

    }

}