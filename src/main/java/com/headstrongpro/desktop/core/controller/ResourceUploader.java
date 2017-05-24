package com.headstrongpro.desktop.core.controller;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.resource.IResourceUploader;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceUploadAdapter;

/**
 * Created by rajmu on 17.05.24.
 */
public class ResourceUploader implements IResourceUploader{
    @Override
    public Object upload(Resource resource, ResourceUploadAdapter.Destination destination) throws ModelSyncException, ConnectionException {
        if(destination.equals(ResourceUploadAdapter.Destination.DATABASE) || destination.equals(ResourceUploadAdapter.Destination.MEDIA_SERVER)){
            ResourceUploadAdapter adapter = new ResourceUploadAdapter(destination);
            return adapter.upload(resource, destination);
        } else {
            //possible other upload destinations
            return null;
        }
    }
}
