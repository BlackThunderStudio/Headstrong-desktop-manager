package com.headstrongpro.desktop.DbLayer;

import com.headstrongpro.desktop.model.Subscription;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 15-May-17.
 */
public class DBSubscriptionsTest {
    DBSubscriptions subscriptionsDAO;
    DBCompany companyDAO;

    @Before
    public void setUp() throws Exception {
        subscriptionsDAO = new DBSubscriptions();
        companyDAO = new DBCompany();
    }

    @Test
    public void getAll() throws Exception {
        assertNotNull("Subscriptions object is null", subscriptionsDAO.getAll());
        assertNotEquals("No subscriptions available", 0, subscriptionsDAO.getAll().size());
    }

    @Test
    public void getPaymentRateByID() throws Exception {
        assertEquals("Wrong payment rate", "monthly", subscriptionsDAO.getPaymentRateByID(1).getName());
    }

    @Test
    public void getById() throws Exception {
        assertEquals("Wrong numbers of users for this subscription", 13, subscriptionsDAO.getById(1).getNoOfUsers());
    }

    @Test
    public void createDelete() throws Exception {
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        Subscription sub = new Subscription(300, true, date, date, subscriptionsDAO.getPaymentRateByID(1), companyDAO.getById(2));
        int oldSize = subscriptionsDAO.getAll().size();
        subscriptionsDAO.persist(sub);
        assertNotEquals("Subscription was not creatod", oldSize, subscriptionsDAO.getAll().size());
        subscriptionsDAO.delete(sub);
        assertEquals("Subscription was not deleted", oldSize, subscriptionsDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        Subscription sub = subscriptionsDAO.getById(1);
        int oldNo = sub.getNoOfUsers(), newNo = 300;
        sub.setNoOfUsers(newNo);
        subscriptionsDAO.update(sub);
        assertNotEquals("Number of users not modified", oldNo, subscriptionsDAO.getById(1).getNoOfUsers());
        sub.setNoOfUsers(oldNo);
        subscriptionsDAO.update(sub);
        assertEquals("Number of users did not revert back", oldNo, subscriptionsDAO.getById(1).getNoOfUsers());
    }

    @Test
    public void getbyCompanyId() throws Exception {
        assertNotNull("Subscriptions list is null", subscriptionsDAO.getbyCompanyId(1));
        assertEquals("Subcriptions count wrong", 1, subscriptionsDAO.getbyCompanyId(1).size());
    }

}