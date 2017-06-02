package com.headstrongpro.desktop.core.connection

import com.cloudinary.*
import com.headstrongpro.desktop.model.resource.IResourceConnector
import com.headstrongpro.desktop.model.resource.Resource
import com.headstrongpro.desktop.model.resource.ResourceType
import java.io.File
import java.util.*


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
        val config = HashMap<String, String>()
        config.put("cloud_name", "headstrongpro-com")
        config.put("api_key", "373434186787348")
        config.put("api_secret", "vzql3EvyPYMnQ_2cbTl0CIuRYg8")

        cloudinary = Cloudinary(config)
    }

    override fun uploadMediaServer(file: File?, remote: String?): String? {
        //do nothing
        return null
    }

    override fun uploadCdnServer(file: File?, useHttps: Boolean, type: ResourceType): String {
        val options = HashMap<String, String>()
        if(type == ResourceType.AUDIO || type == ResourceType.VIDEO){
            options.put("resource_type", "video")
        }
        val uploadResult = cloudinary.uploader().upload(file, options)
        System.out.println(uploadResult)
        if (useHttps) return uploadResult["secure_url"] as String
        else return uploadResult["url"] as String
    }

    override fun uploadDataBase(resource: Resource?): Resource? {
        //doNothing
        return null
    }
}