package com.headstrongpro.desktop.model.resource;

import com.headstrongpro.desktop.core.exception.ConnectionException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;

/**
 * Resource Uploader Interface
 */
public interface IResourceUploader {
    Object upload(Resource resource, ResourceUploadAdapter.Destination destination) throws ModelSyncException, ConnectionException;
}
