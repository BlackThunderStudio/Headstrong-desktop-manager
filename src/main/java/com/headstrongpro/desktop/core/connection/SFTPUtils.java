package com.headstrongpro.desktop.core.connection;

import com.jcraft.jsch.*;

import java.io.File;

/**
 * Created by rajmu on 17.05.21.
 */
public class SFTPUtils {

    private String host, user, pass, path;
    private int bufferSize, port;
    private JSch jsch;

    public SFTPUtils(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        jsch = new JSch();
        path = "/";
        port = 22;
        bufferSize = 4096;
    }

    public SFTPUtils(String host, String user, String pass, String path) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.path = path;
        jsch = new JSch();
        port = 22;
        bufferSize = 4096;
    }

    public SFTPUtils(String host, String user, String pass, String path, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.path = path;
        this.port = port;
        jsch = new JSch();
        bufferSize = 4096;
    }

    public SFTPUtils(String host, String user, String pass, String path, int bufferSize, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.path = path;
        this.bufferSize = bufferSize;
        this.port = port;
        jsch = new JSch();
    }

    public void upload(File localFile, String remoteFile){
        try {
            Session session = jsch.getSession(user, host, port);
            session.setPassword(pass);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp c = (ChannelSftp)channel;

            //do stuff here
            //TODO: to be implemented or Ondrej enables FTP/FTPS support to his server

            c.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
