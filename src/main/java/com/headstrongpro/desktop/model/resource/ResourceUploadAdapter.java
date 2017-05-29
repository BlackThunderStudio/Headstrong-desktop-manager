package com.headstrongpro.desktop.model.resource;

import com.headstrongpro.desktop.core.connection.Configurable;
import com.headstrongpro.desktop.core.connection.SFTPKotlin;
import com.headstrongpro.desktop.core.connection.SFTPUtils;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.DbLayer.DBResources;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajmu on 17.05.24.
 */
public class ResourceUploadAdapter extends Configurable implements IResourceUploader {
    private IResourceConnector resourceConnector;

    public ResourceUploadAdapter(Destination destination){
        if(destination.equals(Destination.DATABASE)){
            resourceConnector = new DBResources();
        } else if(destination.equals(Destination.MEDIA_SERVER)){
            List<Object> ftpData = getConfig();
            resourceConnector = new SFTPKotlin(
                    (String) ftpData.get(0),
                    (String) ftpData.get(1),
                    (String) ftpData.get(2),
                    (String) ftpData.get(3),
                    (String) ftpData.get(4),
                    (String) ftpData.get(5)
            );
        }
    }

    @Override
    public Object upload(Resource resource, Destination destination) throws ModelSyncException, ConnectionException {
        if(destination.equals(Destination.DATABASE)){
            return resourceConnector.uploadDataBase(resource);
        } else if(destination.equals(Destination.MEDIA_SERVER)){
            return resourceConnector.uploadMediaServer(resource.getFile(), resource.getRemoteFileName());
        }
        return null;
    }

    @Override
    protected List<Object> getConfig() {
        JSONObject credentials = parseJsonConfig("/config.json", "SFTP server");
        List<Object> results = new ArrayList<>();
        results.add(credentials.get("host"));
        results.add(credentials.get("user"));
        results.add(credentials.get("pass"));
        results.add(credentials.get("root"));
        results.add(credentials.get("path"));
        results.add(credentials.get("sub"));
        return results;
    }

    public enum Destination{
        DATABASE,
        MEDIA_SERVER
    }
}
