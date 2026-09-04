package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.services.interfaces.DistrictService;
import com.c2.lc.ms.master.entities.mysql.UGeoDistrictMstEntity;
import com.c2.lc.ms.master.repos.mysql.DistrictRepository;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired private DistrictRepository districtRepository;

    @Override
    public List<UGeoDistrictMstEntity> getByStateCode(String stateCode) throws RecordNotFoundException {
        List<UGeoDistrictMstEntity> list = districtRepository.findByStateCode(stateCode);
        if(list.isEmpty()){
            throw new RecordNotFoundException(stateCode, "Record not found!");
        }
        return list;
    }
}
