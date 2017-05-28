package com.headstrongpro.desktop.core.connection;

import com.jcraft.jsch.ChannelSftp;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by rajmu on 17.05.21.
 */
public class SFTPUtilsTest {

    private SFTPUtils sftp;

    @Before
    public void setUp() throws Exception {
        sftp = new SFTPUtils("osoukup.com", "andrewskp", "sefujupico420.", "sudomains/cdn/media/", "/media/", "cdn");
    }

    @Test
    public void upload() throws Exception {
        sftp.upload(new File("C:\\Users\\Domestos Maximus\\Pictures\\stoner overs\\17796611_1306753259403352_783009970528827924_n.jpg"), "yourmom.jpg");
    }

    @Test
    public void download() throws Exception {
        File file = new File("C:\\Users\\Domestos Maximus\\Pictures\\stoner overs\\index.txt");
        file.createNewFile();
        sftp.download("index.html", file);
    }
}