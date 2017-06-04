package com.headstrongpro.desktop.controller;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.model.resource.IResourceUploader;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceUploadAdapter;

import static com.headstrongpro.desktop.model.resource.ResourceUploadAdapter.Destination;

/**
 * ResourceUploader
 */
public class ResourceUploader implements IResourceUploader {
    @Override
    public Object upload(Resource resource, Destination destination) throws ModelSyncException, ConnectionException {
        if (destination.equals(Destination.DATABASE) || destination.equals(Destination.MEDIA_SERVER) || destination.equals(Destination.CDN_SERVER)) {
            ResourceUploadAdapter adapter = new ResourceUploadAdapter(destination);
            return adapter.upload(resource, destination);
        } else {
            //possible other upload destinations
            return null;
        }
    }
}
