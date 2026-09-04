package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.bos.ManufactureImageBo;
import com.c2.lc.ms.master.services.interfaces.ManufactureService;
import com.c2.lc.ms.master.transactions.interfaces.ManufactureTransaction;
import com.google.gson.JsonArray;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class ManufactureTransactionImpl implements ManufactureTransaction {

    @Autowired
    private ManufactureService manufactureService;
    @Override
    public JsonArray uploadMfgImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException {
       return manufactureService.uploadMfgImage(files);
    }

    @Override
    public void updateMfgImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException {
        manufactureService.updateMfgImage(imageUpdateBo);
    }
}
