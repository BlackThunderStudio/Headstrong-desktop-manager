package com.headstrongpro.desktop.core.connection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.21.
 */
public class SFTPUtilsTest {

    private SFTPUtils sftp;

    @Before
    public void setUp() throws Exception {
        sftp = new SFTPUtils("osoukup.com", "andrewskp", "sefujupico420.", "/");
    }

    @Test
    public void upload() throws Exception {
        sftp.upload(null, "");
    }

}