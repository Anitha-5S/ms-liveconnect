package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.models.Advertisement;
import com.c2.lc.ms.master.models.BannerModel;
import com.microsoft.azure.storage.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface AdvertisementTransaction {

    List<Advertisement> getAddByState(String state) throws RecordNotFoundException;

    List<Advertisement> getAddByCity(String city) throws RecordNotFoundException;

    List<Advertisement> getAllAdd() throws RecordNotFoundException;

    void saveAdd(Advertisement advertisement) throws IOException, URISyntaxException, StorageException;


    void deleteAdd(String id) throws RecordNotFoundException;

    Advertisement getByAddId(String id) throws RecordNotFoundException;

}
