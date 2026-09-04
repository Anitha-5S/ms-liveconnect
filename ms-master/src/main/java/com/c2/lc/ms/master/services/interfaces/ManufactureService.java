package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.bos.ManufactureImageBo;
import com.google.gson.JsonArray;
import com.microsoft.azure.storage.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ManufactureService {

    JsonArray uploadMfgImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException;

    void updateMfgImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException;

}
