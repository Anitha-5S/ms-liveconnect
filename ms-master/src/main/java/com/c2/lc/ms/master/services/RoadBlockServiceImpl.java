package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.ms.master.models.RoadBlock;
import com.c2.lc.ms.master.repos.mongo.RoadBlockRepository;
import com.c2.lc.ms.master.services.interfaces.RoadBlockService;
import com.c2.lc.ms.master.utils.BlobFolder;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class RoadBlockServiceImpl extends BaseDBServiceImpl implements RoadBlockService {

    @Autowired
    private RoadBlockRepository roadBlockRepository;

    @Override
    public List<RoadBlock> getRoadBlockByState(String state) throws RecordNotFoundException {
        List<RoadBlock> roadBlocks = roadBlockRepository.getByState(state);
        if (roadBlocks.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return roadBlocks;
    }

    @Override
    public List<RoadBlock> getRoadBlockByCity(String city) throws RecordNotFoundException {
        List<RoadBlock> roadBlocks = roadBlockRepository.getByCity(city);
        if (roadBlocks.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return roadBlocks;
    }

    @Override
    public List<RoadBlock> getRoadBlock() throws RecordNotFoundException {
        List<RoadBlock> roadBlocks = roadBlockRepository.findAll();
        if (roadBlocks.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return roadBlocks;
    }

    @Override
    public void saveRoadBlock(RoadBlock roadBlock) throws IOException, URISyntaxException, StorageException {

       // File convFile = new File(roadBlock.getImageUrl());
       // String fileName = convFile.getName().replaceFirst("[.][^.]+$", "");
        String fileName = roadBlock.getFileName();
       /* BufferedImage bufferedImage = ImageIO.read(convFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, MasterConstants.IMAGE_FORMAT, baos);
        byte[] bytes = baos.toByteArray();
        String data = Base64.getMimeEncoder().encodeToString(bytes);*/
        String url = this.uploadToBlob(BlobFolder.C2_FOLDER+"/"+BlobFolder.BANNER_FOLDER+"/"+fileName,roadBlock.getFileData());
        roadBlock.setAwsUrl(url);
        roadBlock.setImageType(BlobFolder.IMAGE_FORMAT);
        roadBlock.setFileName(fileName);
        roadBlockRepository.save(roadBlock);
    }

    @Override
    public void deleteRoadBlock(String bannerId) throws RecordNotFoundException {
            getById(bannerId);
            roadBlockRepository.deleteById(bannerId);
    }

    @Override
    public RoadBlock getById(String bannerId) throws RecordNotFoundException {
        RoadBlock roadBlock = roadBlockRepository.getById(bannerId);
        if (roadBlock == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return roadBlock;
    }
}
