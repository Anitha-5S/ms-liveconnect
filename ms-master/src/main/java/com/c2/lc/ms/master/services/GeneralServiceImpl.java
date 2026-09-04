package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.master.entities.mysql.LoGstTypeEntity;
import com.c2.lc.ms.master.entities.mysql.UGeoStateMstEntity;
import com.c2.lc.ms.master.models.MasterModel;
import com.c2.lc.ms.master.repos.mysql.GstTypeRepository;
import com.c2.lc.ms.master.repos.mysql.StateRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.GeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeneralServiceImpl extends MasterBaseServiceImpl implements GeneralService {

    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private GstTypeRepository gstTypeRepository;

    @Override
    public List<MasterModel> getStateList() {
        List<UGeoStateMstEntity> stateList = stateRepository.findAll(Sort.by("cName").ascending());
        List<MasterModel> list = new ArrayList<>();
        for (UGeoStateMstEntity state : stateList) {
            MasterModel masterModel = new MasterModel();
            masterModel.setCCode(state.getcCode());
            masterModel.setCName(state.getcName());
            masterModel.setCShName(state.getcShName());

            list.add(masterModel);
        }
        return list;
    }

    @Override
    public List<MasterModel> getCityList(String code) {
        String sql = "SELECT c.c_code,c.c_name, c.c_sh_name FROM u_geo_city_mst c JOIN u_geo_district_mst d ON c.c_geo_district_code = d.c_code WHERE " +
                " d.c_geo_state_code = :code ORDER BY c.c_name ASC";
        Query query = this.getQuery(sql);
        query.setParameter("code", code);
        List<Object[]> resultSet = this.getResultList(query);

        List<MasterModel> list = new ArrayList<>();
        if (!helper.isEmpty(resultSet)) {
            for (Object[] obj : resultSet) {
                int i = -1;
                MasterModel mm = new MasterModel();
                mm.setCCode(helper.getString(obj[++i]));
                mm.setCName(helper.getString(obj[++i]));
                mm.setCShName(helper.getString(obj[++i]));
                list.add(mm);
            }
        }
        return list;
    }

    @Override
    public List<MasterModel> getAreaList(String code) {
        String sql = "SELECT a.c_code, a.c_name, a.c_sh_name FROM u_geo_area_mst a " +
                "       WHERE a.c_geo_city_code = :code  ORDER BY a.c_name ASC";
        Query query = this.getQuery(sql);
        query.setParameter("code", code);
        List<Object[]> resultSet = this.getResultList(query);

        List<MasterModel> list = new ArrayList<>();
        if (!helper.isEmpty(resultSet)) {
            for (Object[] obj : resultSet) {
                int i = -1;
                MasterModel mm = new MasterModel();
                mm.setCCode(helper.getString(obj[++i]));
                mm.setCName(helper.getString(obj[++i]));
                mm.setCShName(helper.getString(obj[++i]));
                list.add(mm);
            }
        }
        return list;
    }

    @Override
    public List<LoGstTypeEntity> geGstTypeList() {
        return gstTypeRepository.findAll();
    }

    @Override
    public List<MasterModel> getStateSearch(String search) throws RecordNotFoundException {
        List<UGeoStateMstEntity> stateList = stateRepository.findByName(search);
        if (stateList == null || stateList.size() < 1) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        List<MasterModel> list = new ArrayList<>();
        for (UGeoStateMstEntity state : stateList) {
            MasterModel masterModel = new MasterModel();
            masterModel.setCCode(state.getcCode());
            masterModel.setCName(state.getcName());
            masterModel.setCShName(state.getcShName());

            list.add(masterModel);
        }
        return list;
    }

    @Override
    public List<MasterModel> getCityListSearch(String code, String search) throws RecordNotFoundException {
        String sql = "SELECT c.c_code, c.c_name, c.c_sh_name FROM u_geo_city_mst c, u_geo_district_mst d, u_geo_state_mst s " +
                "WHERE s.c_code = :code AND s.c_code = d.c_geo_state_code AND d.c_code = c.c_geo_district_code AND c.c_name LIKE :search ORDER BY c.c_name ASC";
        Query query = this.getQuery(sql);
        query.setParameter("code", code);
        query.setParameter("search", search + "%");
        List<Object[]> resultSet = this.getResultList(query);

        if (resultSet == null || resultSet.size() < 1) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }

        List<MasterModel> list = new ArrayList<>();
        if (!helper.isEmpty(resultSet)) {
            for (Object[] obj : resultSet) {
                int i = -1;
                MasterModel mm = new MasterModel();
                mm.setCCode(helper.getString(obj[++i]));
                mm.setCName(helper.getString(obj[++i]));
                mm.setCShName(helper.getString(obj[++i]));
                list.add(mm);
            }
        }
        return list;
    }

    @Override
    public List<MasterModel> getAreaSearch(String code, String search) throws RecordNotFoundException {
        String sql = "SELECT a.c_code, a.c_name, a.c_sh_name FROM u_geo_area_mst a " +
                "       WHERE a.c_geo_city_code = :code AND a.c_name LIKE :search ORDER BY a.c_name ASC";
        Query query = this.getQuery(sql);
        query.setParameter("code", code);
        query.setParameter("search", search + "%");
        List<Object[]> resultSet = this.getResultList(query);

        if (resultSet == null || resultSet.size() < 1) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        List<MasterModel> list = new ArrayList<>();
        if (!helper.isEmpty(resultSet)) {
            for (Object[] obj : resultSet) {
                int i = -1;
                MasterModel mm = new MasterModel();
                mm.setCCode(helper.getString(obj[++i]));
                mm.setCName(helper.getString(obj[++i]));
                mm.setCShName(helper.getString(obj[++i]));
                list.add(mm);
            }
        }
        return list;
    }
}

