package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.entity.Company;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Rajmund Staniek on 08-May-17.
 */
public class CollectionFactoryTest {
    @Test
    public void getCollection() throws Exception {
        CollectionFactory<Company> dbCompany = new CollectionFactory<>();
        IDataAccessObject<Company> c = dbCompany.getCollection(CollectionType.COMPANY);
        List<Company> companies = c.getAll();

        assertNotEquals(0, companies.size());
    }

}