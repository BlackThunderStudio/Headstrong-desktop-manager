package com.headstrongpro.desktop.model.resource;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rajmu on 17.05.08.
 */
public class ResourceFactoryTest {
    @Test
    public void getResource() throws Exception {
        Resource resource = ResourceFactory.getResource(2, "asd", "sdfsdf", false, 3);

        assertNotNull(resource);
    }

}