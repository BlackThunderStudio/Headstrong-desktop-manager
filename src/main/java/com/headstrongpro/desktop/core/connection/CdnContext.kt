package com.headstrongpro.desktop.core.connection

import com.cloudinary.*
import com.headstrongpro.desktop.model.resource.IResourceConnector
import com.headstrongpro.desktop.model.resource.Resource
import java.io.File


/**
 *
 * desktop-manager
 *
 *
 * Created by rajmu on 17.06.02.
 */
class CdnContext : IResourceConnector {

    var cloudinary: Cloudinary

    init {
        val map = HashMap<String, String>()
        map.put("cloud_name", "headstrongpro-com")
        map.put("api_key", "373434186787348")
        map.put("api_secret", "vzql3EvyPYMnQ_2cbTl0CIuRYg8")

        cloudinary = Cloudinary(map)
    }

    override fun uploadMediaServer(file: File?, remote: String?): String? {
        //do nothing
        return null
    }

    override fun uploadCdnServer(file: File?, useHttps: Boolean): String {
        val uploadResult = cloudinary.uploader().upload(file, HashMap<String, String>())
        System.out.println(uploadResult)
        if (useHttps) return uploadResult["secure_url"] as String
        else return uploadResult["url"] as String
    }

    override fun uploadDataBase(resource: Resource?): Resource? {
        //doNothing
        return null
    }
}