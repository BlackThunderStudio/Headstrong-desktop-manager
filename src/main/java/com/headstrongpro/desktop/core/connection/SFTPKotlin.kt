package com.headstrongpro.desktop.core.connection

import com.headstrongpro.desktop.model.resource.IResourceConnector
import com.headstrongpro.desktop.model.resource.Resource
import com.jcraft.jsch.*
import javafx.scene.control.Alert
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLEncoder

/**
 *
 * desktop-manager
 *
 *
 * Created by rajmu on 17.05.29.
 */
class SFTPKotlin(host: String, user: String, pass: String, path: String, subDomain: String) : IResourceConnector{

    var host = ""
    var user = ""
    var pass = ""
    var root = ""
    var path = ""
    var subDomain = ""
    val port = 22
    var jsch: JSch

    //primary constructor
    init {
        this.host = host
        this.user = user
        this.pass = pass
        this.path = path
        this.subDomain = subDomain
        jsch = JSch()
    }

    //secondary constructors
    constructor(host: String, user: String, pass: String, root: String, path: String, subDomain: String)
            : this(host, user, pass, path, subDomain) {
        this.root = root
        jsch = JSch()
    }

    fun upload(localFile: File, remote: String?): Unit{
        val c = connect()
        val stream: InputStream = FileInputStream(localFile)
        c.cd(root)
        c.put(stream, remote, ChannelSftp.OVERWRITE)
        c.disconnect()
    }

    override fun uploadMediaServer(file: File?, remote: String?): String {
        val ext = FilenameUtils.getExtension(file?.absolutePath)
        val encodedURL = URLEncoder.encode(remote, "UTF-8")
        upload(file!!, "$remote.$ext")
        return "http://$subDomain.$host$path$encodedURL.$ext"
    }

    override fun uploadDataBase(resource: Resource?): Resource? {
        //do nothing
        return null
    }

    protected fun connect(): ChannelSftp {
        val sftp: ChannelSftp
        val session = jsch.getSession(user, host, port)
        session.userInfo = MyUserInfo(pass)
        session.connect()
        val channel: Channel = session.openChannel("sftp")
        channel.connect()
        sftp = channel as ChannelSftp
        return sftp
    }

    data class MyUserInfo(val pwd: String)
        : UserInfo{
        override fun promptPassphrase(p0: String?): Boolean = true

        override fun getPassphrase(): String? = null

        override fun getPassword(): String = pwd

        override fun promptYesNo(p0: String?): Boolean = true

        override fun showMessage(p0: String?) {
            val a = Alert(Alert.AlertType.CONFIRMATION)
            a.headerText = p0
            a.show()
        }

        override fun promptPassword(p0: String?): Boolean = true

    }

}