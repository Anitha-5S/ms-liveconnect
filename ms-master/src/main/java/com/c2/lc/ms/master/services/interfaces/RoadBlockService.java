package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.RoadBlock;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface RoadBlockService {

    List<RoadBlock> getRoadBlockByState(String state) throws RecordNotFoundException;

    List<RoadBlock> getRoadBlockByCity(String city) throws RecordNotFoundException;

    List<RoadBlock> getRoadBlock() throws RecordNotFoundException;

    void saveRoadBlock(RoadBlock roadBlock) throws IOException, URISyntaxException, StorageException;

    void deleteRoadBlock(String bannerId) throws RecordNotFoundException;

    RoadBlock getById(String bannerId) throws RecordNotFoundException;
}
