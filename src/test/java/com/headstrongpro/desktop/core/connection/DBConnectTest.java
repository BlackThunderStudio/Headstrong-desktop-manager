package com.headstrongpro.desktop.core.connection;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rajmu on 17.04.06.
 */
public class DBConnectTest {
    private static DBConnect connect;

    @BeforeClass
    public static void setUp() throws Exception {
        connect = new DBConnect();
    }

    @Test
    public void getConnection() throws Exception {
        assertNotNull(connect.getConnection());
    }

}