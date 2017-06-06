package com.headstrongpro.desktop.core.connection;

import com.cloudinary.Cloudinary;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.resource.IResourceConnector;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.06.
 */
public class CdnConneect implements IResourceConnector{

    private Cloudinary cloudinary;

    public CdnConneect(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "headstrongpro-com");
        config.put("api_key", "373434186787348");
        config.put("api_secret", "vzql3EvyPYMnQ_2cbTl0CIuRYg8");
        cloudinary = new Cloudinary(config);
    }

    @Override
    public String uploadMediaServer(File file, String remote) throws ConnectionException {
        return null;
    }

    @Override
    public Resource uploadDataBase(Resource resource) throws ModelSyncException {
        return null;
    }

    @Override
    public String uploadCdnServer(File file, boolean useHttps, ResourceType type) {
        Map<String, String> options = new HashMap<>();
        if(type.equals(ResourceType.VIDEO) || type.equals(ResourceType.AUDIO)){
            options.put("resource_type", "video");
        }
        try {
            Map<String, String> uploadResult = cloudinary.uploader().upload(file, options);
            if(useHttps) return (String)uploadResult.get("secure_url");
            else return (String)uploadResult.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
