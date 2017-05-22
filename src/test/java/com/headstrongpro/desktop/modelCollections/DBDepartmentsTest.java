package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.Department;
import com.headstrongpro.desktop.model.entity.Company;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBDepartmentsTest {
    private DBDepartments departmentDAO;
    private DBCompany companyDAO;

    @Before
    public void setUp() throws Exception {
        departmentDAO = new DBDepartments();
        companyDAO = new DBCompany();
    }

    @Test
    public void getAll() throws Exception {
        List<Department> departments = departmentDAO.getAll();

        assertNotNull(departments);
        assertNotEquals(0, departments.size());
    }

    @Test
    public void getById() throws Exception {
        Department department = departmentDAO.getById(2);

        assertNotNull(department);
    }

    @Test
    public void createDelete() throws Exception {
        int oldSize = departmentDAO.getAll().size();
        Department dummy = new Department("dummy department", "test purposes", 1);
        departmentDAO.persist(dummy);
        int newSize = departmentDAO.getAll().size();
        assertNotEquals("Department was not added", oldSize, newSize);
        departmentDAO.delete(dummy);
        assertEquals("Department was not deleted", oldSize, departmentDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        Department dummy = departmentDAO.getById(1);
        int oldCompanyId = dummy.getCompanyID();
        int newCompanyId = 2;
        dummy.setCompanyID(newCompanyId);
        departmentDAO.update(dummy);
        assertEquals("Department's company Id was not updated", newCompanyId, departmentDAO.getById(1).getCompanyID());
        dummy.setCompanyID(oldCompanyId);
        departmentDAO.update(dummy);
        assertEquals("Changes were not reverted", oldCompanyId, departmentDAO.getById(1).getCompanyID());
    }

    @Test
    public void getByCompanyID() throws Exception {
        List<Department> departments = departmentDAO.getByCompanyID(1);
        assertNotEquals("No departments have been found", 0 , departments.size());

    }

    //@Ignore("needs to be fixed")
    @Test
    public void deleteByCompanyID() throws Exception {
        Company company = new Company("test company", String.valueOf(new Random().nextInt()), "test street", "123", "dummyville", "dummyland");
        company = companyDAO.persist(company);
        Department department = new Department("test department", "test", company.getId());
        int oldSize = departmentDAO.getAll().size();
        department = departmentDAO.persist(department);
        departmentDAO.deleteByCompanyID(department.getId());
        assertNotEquals("Departments not deleted by company ID", oldSize, departmentDAO.getAll().size());
        companyDAO.delete(company);
    }

}