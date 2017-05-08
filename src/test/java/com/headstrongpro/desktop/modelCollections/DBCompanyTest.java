package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.entity.Company;
import com.headstrongpro.desktop.modelCollections.DBCompany;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for company collection~
 */
public class DBCompanyTest {
    private DBCompany companies;

    @Before
    public void setCompanies() throws Exception {
        companies = new DBCompany();
    }

    @Test
    public void getAllTest() throws Exception {
        List<Company> c = companies.getAll();

        assertNotNull("list is null", c);
        assertNotEquals("List size equals 0", 0, c.size());
    }

    @Test
    public void getByIdTest() throws Exception {
        Company c = companies.getById(1);

        assertNotNull(c);
        assertNotEquals(c.getName(), "");
    }
}
