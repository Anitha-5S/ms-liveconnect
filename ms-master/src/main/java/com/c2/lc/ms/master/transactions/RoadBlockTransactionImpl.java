package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.bos.BannerResponseBo;
import com.c2.lc.ms.master.bos.RoadBlockResponse;
import com.c2.lc.ms.master.models.BannerModel;
import com.c2.lc.ms.master.models.RoadBlock;
import com.c2.lc.ms.master.services.interfaces.RoadBlockService;
import com.c2.lc.ms.master.transactions.interfaces.RoadBlockTransaction;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoadBlockTransactionImpl extends BaseTransactionImpl implements RoadBlockTransaction {

    @Autowired
    private RoadBlockService roadBlockService;

    @Override
    public List<RoadBlock> getRoadBlockByState(String state) throws RecordNotFoundException {
        return roadBlockService.getRoadBlockByState(state);
    }

    @Override
    public List<RoadBlock> getRoadBlockByCity(String city) throws RecordNotFoundException {
        return roadBlockService.getRoadBlockByCity(city);
    }

    @Override
    public List<RoadBlockResponse> getRoadBlock() throws RecordNotFoundException {
        List<RoadBlock> roadBlocks = roadBlockService.getRoadBlock();

        List<RoadBlockResponse> responseBos = new ArrayList<>();
        if (roadBlocks.size()==0){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (RoadBlock model:roadBlocks){
            RoadBlockResponse responseBo = new RoadBlockResponse();
            responseBo.setRoadblockId(model.getId());
            responseBo.setImageUrl(model.getAwsUrl());
            responseBo.setReDirectUrl(model.getRedirectUrl());
            responseBo.setType(model.getType());
            responseBo.setHeightWidth(model.getHeightWidth());
            responseBos.add(responseBo);
        }
        return responseBos;
    }

    @Override
    public void saveRoadBlock(RoadBlock roadBlock) throws IOException, URISyntaxException, StorageException {
            roadBlockService.saveRoadBlock(roadBlock);
    }

    @Override
    public void deleteRoadBlock(String roadBlockId) throws RecordNotFoundException {
            roadBlockService.deleteRoadBlock(roadBlockId);
    }

    @Override
    public RoadBlock getById(String roadBlockId) throws RecordNotFoundException {
        return roadBlockService.getById(roadBlockId);
    }
}
