package com.headstrongpro.desktop.core.connection;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

/**
 * Created by Rajmund Staniek on 17.05.21.
 */
public class FTPUtils {
    private String host, user, pass, path;
    private int bufferSize, port;
    private FTPClient ftpClient;

    public FTPUtils(String host, String user, String pass) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        path = "/";
        port = 22;
        bufferSize = 4096;
        ftpClient = new FTPClient();
    }

    public FTPUtils(String host, String user, String pass, String path) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.path = path;
        port = 22;
        bufferSize = 4096;
        ftpClient = new FTPClient();
    }

    public FTPUtils(String host, String user, String pass, String path, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.path = path;
        this.port = port;
        bufferSize = 4096;
        ftpClient = new FTPClient();
    }

    public FTPUtils(String host, String user, String pass, String path, int bufferSize, int port) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.path = path;
        this.bufferSize = bufferSize;
        this.port = port;
        ftpClient = new FTPClient();
    }

    public void connect() throws ConnectionException {
        try {
            ftpClient.connect(host, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    public void disconnect() throws ConnectionException {
        try {
            if(ftpClient.isConnected()){
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e){
            throw new ConnectionException(e);
        }
    }

    public boolean download(String remoteFile, File localFile) throws ConnectionException {
        return download(remoteFile, localFile, false);
    }

    public boolean download(String remoteFile, File localFile, boolean keepConnection) throws ConnectionException {
        boolean success = false;
        try {
            if(!ftpClient.isConnected()){
                connect();
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(localFile)
            );
            InputStream inputStream = ftpClient.retrieveFileStream(path + remoteFile);
            byte[] bytesArray = new byte[bufferSize];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1){
                outputStream.write(bytesArray, 0, bytesRead);
            }
            success = ftpClient.completePendingCommand();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        } finally {
            if(!keepConnection){
                disconnect();
            }
        }
        return success;
    }

    public boolean upload(File localFile, String remoteFile) throws ConnectionException {
        return upload(localFile, remoteFile, false);
    }

    public boolean upload(File localFile, String remoteFile, boolean keepConnection) throws ConnectionException {
        boolean success = false;
        try {
            if(!ftpClient.isConnected()){
                connect();
            }
            InputStream inputStream = new FileInputStream(localFile);
            OutputStream outputStream = ftpClient.storeFileStream(path + remoteFile);
            byte[] bytesIn = new byte[bufferSize];
            int read = 0;
            while ((read = inputStream.read(bytesIn)) != -1){
                outputStream.write(bytesIn, 0, read);
            }
            inputStream.close();
            outputStream.close();
            success = ftpClient.completePendingCommand();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        } finally {
            if(!keepConnection){
                disconnect();
            }
        }
        return success;
    }
}
