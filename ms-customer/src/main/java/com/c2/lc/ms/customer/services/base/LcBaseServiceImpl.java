package com.c2.lc.ms.customer.services.base;

import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.base.LcBaseService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class LcBaseServiceImpl extends BaseDBServiceImpl implements LcBaseService {
    @Autowired CloudBlobContainer cloudBlobContainer;

    protected String blobUpload(String path, String data) throws URISyntaxException, StorageException, IOException {
        byte[] imageData = Base64.getMimeDecoder().decode(data.split(",")[1]);
        CloudBlockBlob blob;
        blob = cloudBlobContainer.getBlockBlobReference(path);
        blob.uploadFromByteArray(imageData, 0, imageData.length);
        URI uri = blob.getUri();
        return uri.toString();
    }

    protected boolean blobDelete(String path) throws URISyntaxException, StorageException {
        CloudBlockBlob blob;
        blob = cloudBlobContainer.getBlockBlobReference(path);
        return blob.deleteIfExists();
    }

    protected List<Object> blobList(String path) throws URISyntaxException {
        List<Object> uris = new ArrayList<>();
        for (ListBlobItem blobItem : cloudBlobContainer.listBlobs()) {
            uris.add(blobItem.getUri());
        }
        return uris;
    }

    protected JsonObject stringToJson(String data){
        Gson gson = new Gson();
        return gson.fromJson(data,JsonObject.class);
    }

}
