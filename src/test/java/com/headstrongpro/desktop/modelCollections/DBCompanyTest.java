package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.model.entity.Company;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for company collection~
 */
public class DBCompanyTest {
    private DBCompany companyDAO;
    private DBDepartments departmentsDAO;

    @Before
    public void setCompanies() throws Exception {
        companyDAO = new DBCompany();
        departmentsDAO = new DBDepartments();
    }

    @Test
    public void getAll() throws Exception {
        List<Company> c = companyDAO.getAll();

        assertNotNull("list is null", c);
        assertNotEquals("List size equals 0", 0, c.size());
    }

    @Test
    public void getById() throws Exception {
        Company c = companyDAO.getById(2);

        assertNotNull(c);
        assertEquals(c.getName(), "Maersk");
    }

    //@Ignore ("needs fix")
    @Test
    public void createDelete() throws  Exception{
        Company company = new Company("test company", String.valueOf(new Random().nextInt()), "test street", "123", "dummyville", "dummyland");
        Department department = new Department(12, "depo", "yest", 26);

        int oldSize = companyDAO.getAll().size();

        company = companyDAO.create(company);
        department.setCompanyID(company.getId());
        departmentsDAO.create(department);
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

    @Test
    public void getCompanyNameByIdTest() throws Exception {
        String name = companyDAO.getCompanyNameByID(1);
        String expected = "Neas Energy A/S";

        assertEquals(expected, name);
    }
}
