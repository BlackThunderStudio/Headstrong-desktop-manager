package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.Payment;
import com.headstrongpro.desktop.model.Subscription;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 15-May-17.
 */
public class DBPaymentTest {
    DBPayment paymentDAO;
    DBSubscriptions subDAO;

    @Before
    public void setUp() throws Exception {
        paymentDAO = new DBPayment();
        subDAO = new DBSubscriptions();
    }

    @Test
    public void getAll() throws Exception {
        assertNotNull("Payment is null", paymentDAO.getAll());
        assertNotEquals("No payments available", 0, paymentDAO.getAll().size());
    }

    @Test
    public void getById() throws Exception {
        assertEquals(2600, paymentDAO.getById(10).getValue(), 0);
    }

    @Test
    public void createDelete() throws Exception {
        java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
        Payment newPayment = new Payment(1000, 500, null, date, false, subDAO.getById(1));
        int oldSize = paymentDAO.getAll().size();
        paymentDAO.create(newPayment);
        assertNotEquals("Subscription not created", oldSize, paymentDAO.getAll().size());
        paymentDAO.delete(newPayment);
        assertEquals("Subcription not deleted", oldSize, paymentDAO.getAll().size());
    }

    @Test
    public void update() throws Exception {
        Payment payment = paymentDAO.getById(12);
        double oldValue = payment.getValue(), newValue = 115;

        payment.setValue(newValue);
        paymentDAO.update(payment);
        assertNotEquals("Payment not changed", 0, oldValue - paymentDAO.getById(12).getValue());
        payment.setValue(oldValue);
        paymentDAO.update(payment);
        assertTrue("Payment not reset", 0 == oldValue - paymentDAO.getById(12).getValue());
    }

    @Test
    public void getBySubscriptionId() throws Exception {
        assertNotEquals("Could not fetch any payments", 0, paymentDAO.getBySubscriptionId(1));
    }

}