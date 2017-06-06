package com.headstrongpro.desktop.DbLayer;

import org.junit.Before;
import org.junit.Test;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.31.
 */
public class DBStatsTest {

    private DBStats stats;

    @Before
    public void setUp() throws Exception {
        stats = new DBStats();
    }

    @Test
    public void getAmountOfRecords() throws Exception {
        System.out.println("Resources: " + stats.getAmountOfRecords("resources"));
    }

    @Test
    public void getOverduePaymentsCount() throws Exception {
        System.out.println("Overdue payments: " + stats.getOverduePaymentsCount());
    }

    @Test
    public void getActiveSubscriptionsCount() throws Exception {
        System.out.println("Subs: " + stats.getActiveSubscriptionsCount());
    }

}