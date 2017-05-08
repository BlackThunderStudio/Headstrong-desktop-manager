package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.core.connection.DBConnect;
import com.headstrongpro.desktop.core.connection.IDataAccessObject;
import com.headstrongpro.desktop.model.Department;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.08.
 */
public class DBDepartmentsTest {
    private IDataAccessObject<Department> departmentDAO;

    @Before
    public void setUp() throws Exception {
        CollectionFactory<Department> collectionFactory = new CollectionFactory<>();
        departmentDAO = collectionFactory.getCollection(CollectionType.DEPARTMENT);
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

    @Test
    public void getByCompanyID() throws Exception {

    }

    @Ignore(value = "To be implemented")
    @Test
    public void deleteByCompanyID() throws Exception {

    }

}