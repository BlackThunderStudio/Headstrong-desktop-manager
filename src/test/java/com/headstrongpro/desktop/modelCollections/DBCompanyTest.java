package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.entity.Company;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for company collection~
 */
public class DBCompanyTest {
    private DBCompany companyDAO;

    @Before
    public void setCompanies() throws Exception {
        companyDAO = new DBCompany();
    }

    @Test
    public void getAllTest() throws Exception {
        List<Company> c = companyDAO.getAll();

        assertNotNull("list is null", c);
        assertNotEquals("List size equals 0", 0, c.size());
    }

    @Test
    public void getByIdTest() throws Exception {
        Company c = companyDAO.getById(2);

        assertNotNull(c);
        assertEquals(c.getName(), "Maersk");
    }

    @Ignore
    @Test
    public void createDelete() throws  Exception{
        Company company = new Company("test company", "1234567890", "test street", "123", "dummyville", "dummyland");
        int oldSize = companyDAO.getAll().size();
        companyDAO.create(company);
        assertNotEquals("Company has not been created", oldSize, companyDAO.getAll().size());
        companyDAO.delete(company);
        assertEquals("Company has not been removed", oldSize, companyDAO.getAll().size());
    }

    @Test
    public void update() throws Exception{
        Company company = companyDAO.getById(1);
        String oldName = company.getName(), newName = "dummy industries";
        company.setName(newName);
        companyDAO.update(company);
        assertNotEquals("Company has not been renamed", oldName, companyDAO.getById(1).getName());
        company.setName(oldName);
        companyDAO.update(company);
        assertEquals("Company renaming has not been reverted", oldName, companyDAO.getById(1).getName());
    }
}
