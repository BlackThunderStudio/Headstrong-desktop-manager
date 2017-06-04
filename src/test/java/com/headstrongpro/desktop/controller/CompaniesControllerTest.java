package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.DbLayer.DBCompany;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * tests for companiesController
 */
public class CompaniesControllerTest {

    private CompaniesController companiesController;
    private DBCompany companyDAO;

    @Before
    public void setUp() throws Exception {
        companiesController = new CompaniesController();
        companyDAO = new DBCompany();
    }

    @Test
    public void search() throws Exception {
        assertNotNull(companiesController.searchByPhrase("neas"));
    }

    @Test
    public void validCompany() {
        assertTrue(companiesController.validCompany("testcomp", String.valueOf(new Random().nextInt(500000) + 10000000), "teststreet", "123", "dummyville", "dummyland"));
    }

    @Test
    public void createDeleteCompany() throws Exception {
        int oldSize = companiesController.getAll().size();
        companiesController.createCompany("testcomp", String.valueOf(new Random().nextInt(500000) + 10000000), "teststreet", "123", "dummyville", "dummyland");
        assertNotEquals(oldSize, companiesController.getAll().size());
        companiesController.delete(companiesController.searchByPhrase("testcomp").get(0));
        assertEquals(oldSize, companiesController.getAll().size());
    }

    @Test
    public void getCompanies() throws Exception {
        assertTrue(companiesController.getAll().size() > 0);
    }

    @Test
    public void updateCompany() throws Exception {
        String oldPostal = companyDAO.getById(2).getPostal();
        companiesController.updateCompany(2, "Maersk", "22756214", "Esplanaden 50", "5555", "Copenhagen", "Denmark");
        assertEquals("5555", companyDAO.getById(2).getPostal());
        companiesController.updateCompany(2, "Maersk", "22756214", "Esplanaden 50", oldPostal, "Copenhagen", "Denmark");
        assertEquals(oldPostal, companyDAO.getById(2).getPostal());
    }

    @Test
    public void getDepartmentsByCompanyId() throws Exception {
        assertEquals(4, companiesController.getDepartmentsByCompanyId(1).size());
    }

    @Test
    public void getGroupsByCompanyId() throws Exception {
        assertEquals(3, companiesController.getGroupsByCompanyId(2).size());
    }

    @Test
    public void getSubscriptionByCompanyId() throws Exception {
        assertEquals(1, companiesController.getSubscriptionByCompanyId(1).size());
    }

    @Test
    public void getClientByCompanyId() throws Exception {
        assertEquals(8, companiesController.getClientByCompanyId(1).size());
    }

    @Test
    public void getPaymentsByCompanyId() throws Exception {
        assertEquals(3, companiesController.getPaymentsByCompanyId(2).size());
    }

}