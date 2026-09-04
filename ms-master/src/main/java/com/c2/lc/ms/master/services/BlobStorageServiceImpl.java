package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.services.BaseServicesImpl;
import com.c2.lc.ms.master.services.interfaces.BlobStorageService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.microsoft.azure.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@Slf4j
@Service
public class BlobStorageServiceImpl extends BaseServicesImpl implements BlobStorageService {

    @Override
    public JsonArray upload(Long userId, Long firmId, MultipartFile[] files) throws URISyntaxException, IOException, StorageException, InvalidRequestException {
        if (files.length<=0){
            throw new InvalidRequestException("file","empty");
        }
        JsonArray jsonArray = new JsonArray();
        for (MultipartFile file:files) {
            String path = userId.toString() + "/" + firmId.toString() + "/" + helper.getCurrentTime() + "/" + file.getContentType() + "/" + file.getName() + "/" + file.getOriginalFilename();
            String uri = this.uploadToBlob(path, file);
            jsonArray.add(uri);
        }
        return jsonArray;
    }

    @Override
    public void delete(Long userId, Long firmId, JsonElement path) throws URISyntaxException, IOException, StorageException, RecordNotFoundException {
        String uri = path.getAsString();
        String[] arr = uri.split(userId.toString());
        String deletePath = userId + arr[1];
        if (this.checkBlobExist(deletePath)) {
            log.debug("Delete path:{}", deletePath);
            this.deleteBlob(deletePath);
        } else {
            throw new RecordNotFoundException(path.toString(), "Record not found!");
        }
    }
}
