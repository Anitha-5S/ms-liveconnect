package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseServicesImpl;
import com.c2.lc.ms.master.models.Advertisement;
import com.c2.lc.ms.master.repos.mongo.AdvertisementRepository;
import com.c2.lc.ms.master.services.interfaces.AdvertisementService;
import com.c2.lc.ms.master.utils.BlobFolder;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class AdvertisementServiceImpl extends BaseServicesImpl implements AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;


    @Override
    public List<Advertisement> getAddByState(String state) throws RecordNotFoundException {
        List<Advertisement> advertisements = advertisementRepository.getByState(state);
        if (advertisements.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return advertisements;
    }


    @Override
    public List<Advertisement> getAddByCity(String city) throws RecordNotFoundException {
        List<Advertisement> advertisements = advertisementRepository.getByCity(city);
        if (advertisements.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return advertisements;
    }

    @Override
    public List<Advertisement> getAllAdd() throws RecordNotFoundException {
        List<Advertisement> advertisements = advertisementRepository.findAll();
        if (advertisements.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return advertisements;
    }

    @Override
    public void saveAdd(Advertisement entity) throws IOException, URISyntaxException, StorageException {

       /* File convFile = new File(entity.getImageUrl());
        String fileName = convFile.getName().replaceFirst("[.][^.]+$", "");
        BufferedImage bufferedImage = ImageIO.read(convFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, MasterConstants.IMAGE_FORMAT, baos);
        byte[] bytes = baos.toByteArray();
        String data = Base64.getMimeEncoder().encodeToString(bytes);*/
        String fileName = entity.getFileName();
        String url =this.uploadToBlob(BlobFolder.C2_FOLDER+"/"+BlobFolder.BANNER_FOLDER+"/"+fileName,entity.getFileData());
        entity.setAwsUrl(url);
        entity.setImageType(BlobFolder.IMAGE_FORMAT);
        entity.setFileName(fileName);
        advertisementRepository.save(entity);
    }

    @Override
    public void deleteAdd(String id) throws RecordNotFoundException {
        getByAddId(id);
        advertisementRepository.deleteById(id);
    }

    @Override
    public Advertisement getByAddId(String id) throws RecordNotFoundException {
        Advertisement advertisement = advertisementRepository.getById(id);
        if (advertisement == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return advertisement;
    }
}
