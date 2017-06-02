package com.headstrongpro.desktop.model.resource;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;

import java.io.File;

/**
 * Created by rajmu on 17.05.24.
 */
public interface IResourceConnector {
    String uploadMediaServer(File file, String remote) throws ConnectionException;
    Resource uploadDataBase(Resource resource) throws ModelSyncException;
    String uploadCdnServer(File file, boolean useHttps, ResourceType type);
}
