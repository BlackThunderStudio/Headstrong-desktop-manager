package com.headstrongpro.desktop.modelCollections;

import com.headstrongpro.desktop.model.Payment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 1062085 on 15-May-17.
 */
public class DBPaymentTest {
    DBPayment paymentDAO;

    @Before
    public void setUp() throws Exception {
        paymentDAO = new DBPayment();
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
    }

    @Test
    public void update() throws Exception {
        Payment payment = paymentDAO.getById(12);
        double oldValue = payment.getValue(), newValue = 115;

        payment.setValue(newValue);
        paymentDAO.update(payment);
        assertNotEquals(oldValue, paymentDAO.getById(1).getValue(), 0);
        payment.setValue(oldValue);
        paymentDAO.update(payment);
        assertEquals(oldValue, paymentDAO.getById(1).getValue(), 0);
    }

    @Test
    public void getBySubscriptionId() throws Exception {
        assertNotEquals("Could not fetch any payments", 0, paymentDAO.getBySubscriptionId(1));
    }

}