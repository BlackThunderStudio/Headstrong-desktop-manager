package com.headstrongpro.desktop.core.connection;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.resource.IResourceConnector;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.jcraft.jsch.*;
import javafx.scene.control.Alert;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by rajmu on 17.05.21.
 */
public class SFTPUtils implements IResourceConnector {

    private String host, user, pass, root, path, subDomain;
    private int port;
    private JSch jsch;

    public SFTPUtils(String host, String user, String pass, String path, String subDomain) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        jsch = new JSch();
        root = "";
        port = 22;
        this.path = path;
        this.subDomain = subDomain;
    }

    public SFTPUtils(String host, String user, String pass, String root, String path, String subDomain) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.root = root;
        jsch = new JSch();
        port = 22;
        this.path = path;
        this.subDomain = subDomain;
    }

    public SFTPUtils(String host, String user, String pass, String root, int port, String path, String subDomain) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.root = root;
        this.port = port;
        jsch = new JSch();
        this.path = path;
        this.subDomain = subDomain;
    }

    /***
     * untested, might not work
     * @param remoteFile
     * @param localFile
     * @throws ConnectionException
     */
    @Deprecated
    public void download(String remoteFile, File localFile) throws ConnectionException {
        try {
            ChannelSftp sftp = connect();
            SftpProgressMonitor monitor = new MyProgressMonitor();
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
            sftp.cd(root);
            sftp.get(remoteFile, outputStream, monitor);
            sftp.disconnect();
        } catch (SftpException | FileNotFoundException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        }
    }

    /***
     * Uploads a file to the server
     * @param localFile File object of a local existing file
     * @param remoteFile name of a remote file in the root root
     * @throws ConnectionException When the shit goes south
     */
    public void upload(File localFile, String remoteFile) throws ConnectionException {
        try {
            ChannelSftp c = connect();
            SftpProgressMonitor monitor = new MyProgressMonitor();
            InputStream inputStream = new FileInputStream(localFile);
            c.cd(root);
            c.put(inputStream, remoteFile, monitor, ChannelSftp.OVERWRITE);
            c.disconnect();
        } catch (IOException | SftpException e) {
            e.printStackTrace();
            throw new ConnectionException(e);
        }
    }

    /***
     * Starts a connection over SSH to a remote server
     * @return ChannelSftp object
     */
    protected ChannelSftp connect() {
        ChannelSftp sftp = null;
        try {
            Session session = jsch.getSession(user, host, port);
            session.setUserInfo(new MyUserInfo(pass));
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return sftp;
    }

    //adapter implementation
    @Override
    public String uploadMediaServer(File file, String remote) throws ConnectionException {
        String extension = FilenameUtils.getExtension(file.getAbsolutePath());
        String encodedUrl = "";
        try {
            encodedUrl = URLEncoder.encode(remote, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //not so likely to happen with utf-8 as it's always compatible, hence handling this exception here
        }
        String url = "http://" + subDomain + "." + host + path + encodedUrl + "." + extension;
        upload(file, remote + "." + extension);
        return url;
    }

    @Override
    public Resource uploadDataBase(Resource resource) throws ModelSyncException {
        return null; //do nothing
    }

    @Override
    public String uploadCdnServer(File file, boolean useHttps, ResourceType type) {
        //do nothing
        return null;
    }

    public static class MyProgressMonitor implements SftpProgressMonitor {
        ProgressMonitor monitor;
        long count = 0;
        long max = 0;
        private long percent = -1;

        @Override
        public void init(int i, String s, String s1, long l) {
            this.max = l;
            monitor = new ProgressMonitor(null,
                    ((i == SftpProgressMonitor.PUT) ?
                            "put" : "get") + ": " + s,
                    "", 0, (int) max);
            count = 0;
            percent = -1;
            monitor.setProgress((int) this.count);
            monitor.setMillisToDecideToPopup(1000);
        }

        @Override
        public boolean count(long l) {
            count += l;
            if (percent >= count * 100 / max) return true;
            percent = count * 100 / max;
            monitor.setNote("Completed " + count + "(" + percent + "%) out of " + max + ".");
            monitor.setProgress((int) count);

            return !monitor.isCanceled();
        }

        @Override
        public void end() {
            monitor.close();
        }
    }

    public class MyUserInfo implements UserInfo {

        private String password;

        public MyUserInfo(String pwd) {
            password = pwd;
        }

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public boolean promptPassword(String s) {
            return true;
        }

        @Override
        public boolean promptPassphrase(String s) {
            return true;
        }

        @Override
        public boolean promptYesNo(String s) {
            /*Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setHeaderText(s);
            Optional<ButtonType> okResponse = a.showAndWait();
            return okResponse.isPresent() && ButtonType.OK.equals(okResponse.get());*/
            /*Object[] options = {"yes", "no"};
            int foo = JOptionPane.showOptionDialog(null,
                    s,
                    "Warning",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            return foo == 0;*/
            return true;
        }

        @Override
        public void showMessage(String s) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(s);
            alert.show();
        }
    }
}
