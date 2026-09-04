package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.RoadBlockResponse;
import com.c2.lc.ms.master.models.RoadBlock;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface RoadBlockTransaction {

    List<RoadBlock> getRoadBlockByState(String state) throws RecordNotFoundException;

    List<RoadBlock> getRoadBlockByCity(String city) throws RecordNotFoundException;

    List<RoadBlockResponse> getRoadBlock() throws RecordNotFoundException;

    void saveRoadBlock(RoadBlock roadBlock) throws IOException, URISyntaxException, StorageException;

    void deleteRoadBlock(String bannerId) throws RecordNotFoundException;

    RoadBlock getById(String bannerId) throws RecordNotFoundException;

}
