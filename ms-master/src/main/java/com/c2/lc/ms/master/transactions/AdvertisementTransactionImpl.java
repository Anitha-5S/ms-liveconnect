package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.models.Advertisement;
import com.c2.lc.ms.master.services.interfaces.AdvertisementService;
import com.c2.lc.ms.master.transactions.interfaces.AdvertisementTransaction;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class AdvertisementTransactionImpl extends BaseTransactionImpl implements AdvertisementTransaction {

    @Autowired
    private AdvertisementService advertisementService;

    @Override
    public List<Advertisement> getAddByState(String state) throws RecordNotFoundException {
        return advertisementService.getAddByState(state);
    }

    @Override
    public List<Advertisement> getAddByCity(String city) throws RecordNotFoundException {
        return advertisementService.getAddByCity(city);
    }

    @Override
    public List<Advertisement> getAllAdd() throws RecordNotFoundException {
        return advertisementService.getAllAdd();
    }

    @Override
    public void saveAdd(Advertisement advertisement) throws IOException, URISyntaxException, StorageException {
        advertisementService.saveAdd(advertisement);
    }

    @Override
    public void deleteAdd(String id) throws RecordNotFoundException {
            advertisementService.deleteAdd(id);
    }

    @Override
    public Advertisement getByAddId(String id) throws RecordNotFoundException {
        return advertisementService.getByAddId(id);
    }
}
