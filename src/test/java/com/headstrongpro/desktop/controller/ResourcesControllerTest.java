package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.model.resource.ImageResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.model.resource.TextResource;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 31-May-17.
 */
public class ResourcesControllerTest {
    ResourcesController rc;

    @Before
    public void setUp() throws Exception {
        rc = new ResourcesController();
    }

    @Test
    public void refreshGetAll() throws Exception {
        assertNotNull(rc.getAll());
    }

    @Test
    public void searchByPhrase() throws Exception {
        assertNotNull(rc.searchByPhrase("morning"));
    }

    @Test
    public void filterSearch() throws Exception {
        assertNotNull(rc.filterSearch("duck", "Image"));
    }

    @Test
    public void filterByType() throws Exception {
        assertNotNull(rc.filterByType(ResourceType.IMAGE));
    }

    @Test
    public void editResource() throws Exception {
        String oldName , newName = "random name";
        ImageResource test = (ImageResource) rc.getResourceById(66);
        oldName = test.getName();
        test.setName(newName);
        rc.editResource(test);
        assertNotEquals(oldName, rc.getResourceById(66).getName());
        test.setName(oldName);
        rc.editResource(test);
        assertEquals(oldName, rc.getResourceById(66).getName());
    }

    @Ignore
    @Test
    public void delete() throws Exception {
        //TextResource test = new TextResource("really random text", "test", false);
    }

    @Test
    public void getResourceById() throws Exception {
        assertEquals("duck pic", rc.getResourceById(66).getName());
    }
}