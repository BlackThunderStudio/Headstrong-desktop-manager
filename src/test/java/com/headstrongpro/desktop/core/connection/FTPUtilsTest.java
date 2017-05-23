package com.headstrongpro.desktop.core.connection;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Rajmund Staniek on 22-May-17.
 */
public class FTPUtilsTest {
    @Test
    public void connect() throws Exception {
        FTPUtils utils = new FTPUtils("213.216.114.12", "adam", "adam1234", "/", 21);
        utils.connect();
    }

}