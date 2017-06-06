package com.headstrongpro.desktop.model.resource;

import com.headstrongpro.desktop.DbLayer.DBResources;
import com.headstrongpro.desktop.core.connection.CdnConneect;
import com.headstrongpro.desktop.core.connection.Configurable;
import com.headstrongpro.desktop.core.connection.SFTPUtils;
import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource Upload Adapter
 */
public class ResourceUploadAdapter extends Configurable implements IResourceUploader {
    private IResourceConnector resourceConnector;

    public ResourceUploadAdapter(Destination destination) {
        if (destination.equals(Destination.DATABASE)) {
            resourceConnector = new DBResources();
        } else if (destination.equals(Destination.MEDIA_SERVER)) {
            List<Object> ftpData = getConfig();
            resourceConnector = new SFTPUtils(
                    (String) ftpData.get(0),
                    (String) ftpData.get(1),
                    (String) ftpData.get(2),
                    (String) ftpData.get(3),
                    (String) ftpData.get(4),
                    (String) ftpData.get(5)
            );
        } else if (destination.equals(Destination.CDN_SERVER)) {
            resourceConnector = new CdnConneect();
        }
    }

    @Override
    public Object upload(Resource resource, Destination destination) throws ModelSyncException, ConnectionException {
        if (destination.equals(Destination.DATABASE)) {
            return resourceConnector.uploadDataBase(resource);
        } else if (destination.equals(Destination.MEDIA_SERVER)) {
            return resourceConnector.uploadMediaServer(resource.getFile(), resource.getRemoteFileName());
        } else if (destination.equals(Destination.CDN_SERVER)) {
            return resourceConnector.uploadCdnServer(resource.getFile(), true, resource.getType());
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

    public enum Destination {
        DATABASE,
        MEDIA_SERVER,
        CDN_SERVER
    }
}
