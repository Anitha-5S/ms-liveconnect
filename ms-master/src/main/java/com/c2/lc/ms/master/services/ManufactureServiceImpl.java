package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.master.bos.ImageUpdateBo;
import com.c2.lc.ms.master.bos.ManufactureImageBo;
import com.c2.lc.ms.master.entities.mongo.LcItem;
import com.c2.lc.ms.master.entities.mongo.LcManufacture;
import com.c2.lc.ms.master.entities.mongo.LcSupplier;
import com.c2.lc.ms.master.repos.mongo.ManufactureRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.ManufactureService;
import com.c2.lc.ms.master.utils.BlobFolder;
import com.google.gson.JsonArray;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ManufactureServiceImpl extends MasterBaseServiceImpl implements ManufactureService {
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ManufactureRepository manufactureRepository;

    @Override
    public JsonArray uploadMfgImage(MultipartFile[] files) throws InvalidRequestException, StorageException, IOException, URISyntaxException {

        if (files.length <= 0) {
            throw new InvalidRequestException("file", "empty");
        }
        JsonArray jsonArray = new JsonArray();
        for (MultipartFile file : files) {
            String imageUrl = this.uploadToBlob(BlobFolder.C2_FOLDER + "/" + BlobFolder.MANUFACTURE_FOLDER + "/" + file.getOriginalFilename(), file);
            jsonArray.add(imageUrl);
        }
        return jsonArray;
    }

    @Override
    public void updateMfgImage(ImageUpdateBo imageUpdateBo) throws RecordNotFoundException {
        Optional<LcManufacture> lcManufacture = manufactureRepository.findById(imageUpdateBo.getCCode());
        if (lcManufacture == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        Criteria criteria = Criteria.where("_id").is(imageUpdateBo.getCCode());
        org.springframework.data.mongodb.core.query.Query query = org.springframework.data.mongodb.core.query.Query.query(criteria);
        Update update = new Update();
        update.set("ac_images", imageUpdateBo.getAcImages());
        mongoOperations.upsert(query, update, LcManufacture.class);
    }
}
