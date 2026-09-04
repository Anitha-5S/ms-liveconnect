package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.azure.storage.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface BlobStorageTransaction {
    JsonArray upload(Long userId, Long firmId, MultipartFile[] img) throws URISyntaxException, IOException, StorageException, InvalidRequestException;

    void delete(Long userId, Long firmId, JsonElement path) throws URISyntaxException, IOException, StorageException, RecordNotFoundException;
}
