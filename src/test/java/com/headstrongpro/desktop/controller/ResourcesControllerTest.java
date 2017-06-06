package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.model.resource.ImageResource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import org.junit.Before;
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
        String oldName, newName = "random name";
        ImageResource test = (ImageResource) rc.getById(2);
        oldName = test.getName();
        test.setName(newName);
        rc.edit(test);
        assertNotEquals(oldName, rc.getById(2).getName());
        test.setName(oldName);
        rc.edit(test);
        assertEquals(oldName, rc.getById(2).getName());
    }

    @Test
    public void getById() throws Exception {
        assertEquals("get_focused_part1", rc.getById(15).getName());
    }
}