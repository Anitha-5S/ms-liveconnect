package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.services.interfaces.BlobStorageService;
import com.c2.lc.ms.master.transactions.interfaces.BlobStorageTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class BlobStorageTransactionImpl extends BaseTransactionImpl implements BlobStorageTransaction {

    @Autowired private BlobStorageService blobStorageService;

    @Override
    public JsonArray upload(Long userId, Long firmId, MultipartFile[] img) throws URISyntaxException, IOException, StorageException, InvalidRequestException {
        return blobStorageService.upload(userId, firmId, img);
    }

    @Override
    public void delete(Long userId, Long firmId, JsonElement path) throws URISyntaxException, IOException, StorageException, RecordNotFoundException {
        blobStorageService.delete(userId, firmId, path);
    }

}
