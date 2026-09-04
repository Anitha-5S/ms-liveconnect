package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.PlayStoreDetailsBo;
import com.c2.lc.ms.customer.entities.customer.TSPinCodeReqEntity;
import com.c2.lc.ms.customer.entities.customer.TSPlayStoreDetailsEntity;
import com.c2.lc.ms.customer.entities.customer.TSSettingDetailEntity;
import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import com.c2.lc.ms.customer.repos.customer.TSPinCodeReqRepo;
import com.c2.lc.ms.customer.repos.customer.TSPlayStoreDetailsRepo;
import com.c2.lc.ms.customer.repos.customer.TSSettingDetailRepo;
import com.c2.lc.ms.customer.repos.customer.TouchStoreRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.TouchStoreService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TouchStoreServiceImpl extends LcBaseServiceImpl implements TouchStoreService {

    @Autowired
    TouchStoreRepo touchStoreRepo;
    @Autowired
    TSSettingDetailRepo settingDetailRepo;
    @Autowired
    TSPlayStoreDetailsRepo tsPlayStoreDetailsRepo;
    @Autowired
    private TSPinCodeReqRepo pinCodeReqRepo;

    @Override
    public List<TSStoreRegisterEntity> getRegisteredStores(LcHeaderBO headerBO) throws RecordNotFoundException {
        List<TSStoreRegisterEntity> entityList = touchStoreRepo.findAll();

        if(entityList.size() == 0){
            throw new RecordNotFoundException("Record Not Found!");
        }
        return entityList;
    }

    @Override
    public void save(LcHeaderBO header, TSStoreRegisterEntity tsStoreRegisterEntity) {
        tsStoreRegisterEntity.setIdTime(header.getUserId(), helper.getCurrentTime());
        tsStoreRegisterEntity.setC2Code(header.getC2Code());
        tsStoreRegisterEntity.setNUserId(header.getUserId());
        tsStoreRegisterEntity.setNFirmId(header.getFirmId());
        tsStoreRegisterEntity.setCBrCode(header.getBrCode());
        tsStoreRegisterEntity.setStatus(Constants.STATUS_ACTIVE);
        touchStoreRepo.save(tsStoreRegisterEntity);
    }

    @Override
    public void saveSettingDetail(LcHeaderBO header, TSSettingDetailEntity settingDetail) {
        settingDetail.setC2Code(header.getC2Code());
        settingDetail.setCBrcode(header.getBrCode());
        settingDetail.setIdTime(header.getUserId(), helper.getCurrentTime());
        settingDetailRepo.save(settingDetail);
    }

    @Override
    public TSSettingDetailEntity getSettingDetail(String c2Code) throws RecordNotFoundException {
        Optional<TSSettingDetailEntity> entity = settingDetailRepo.findById(c2Code);
        if (entity.isEmpty()) {
            throw new RecordNotFoundException("Record Not Found!");
        }
        return entity.get();
    }

    @Override
    public String storeRegStatus(String c2Code) {
        String status = Constants.STATUS_NO;
        List<TSStoreRegisterEntity> entityList = touchStoreRepo.getByC2Code(c2Code);
        if (entityList.size() > 0) {
            status = Constants.STATUS_YES;
        }
        return status;
    }

    @Override
    public void savePsDetails(LcHeaderBO headerBO, PlayStoreDetailsBo playStoreDetailsBo) throws DuplicateRecordException {
        TSPlayStoreDetailsEntity tsPlayStoreDetailsEntity1 = tsPlayStoreDetailsRepo.getByAppId(playStoreDetailsBo.getAppId());
        if(tsPlayStoreDetailsEntity1 !=null){
            throw new DuplicateRecordException("Duplicate record");
        }
        TSPlayStoreDetailsEntity tsPlayStoreDetailsEntity = new TSPlayStoreDetailsEntity();
        setPlayStoreDetails(tsPlayStoreDetailsEntity,playStoreDetailsBo);
        tsPlayStoreDetailsEntity.setCApplicationId(playStoreDetailsBo.getAppId());
        tsPlayStoreDetailsEntity.setCC2code(headerBO.getC2Code());
        tsPlayStoreDetailsEntity.setIdTime(headerBO.getUserId(),helper.getCurrentTime());
        tsPlayStoreDetailsRepo.save(tsPlayStoreDetailsEntity);
    }

    @Override
    public void updatePsDetails(Long userId, PlayStoreDetailsBo playStoreDetailsBo) throws RecordNotFoundException {
        TSPlayStoreDetailsEntity tsPlayStoreDetailsEntity = tsPlayStoreDetailsRepo.getByAppId(playStoreDetailsBo.getAppId());
      if(tsPlayStoreDetailsEntity == null){
          throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
      }
        setPlayStoreDetails(tsPlayStoreDetailsEntity,playStoreDetailsBo);
        tsPlayStoreDetailsEntity.setNLastUpdatedBy(userId);
        tsPlayStoreDetailsEntity.setTLastUpdatedAt(helper.getCurrentTime());
        tsPlayStoreDetailsRepo.save(tsPlayStoreDetailsEntity);
    }

    @Override
    public TSPlayStoreDetailsEntity retrievePsDetails(String c_application_id) throws RecordNotFoundException {
        TSPlayStoreDetailsEntity tsPlayStoreDetailsEntity = tsPlayStoreDetailsRepo.getByAppId(c_application_id);
        if(tsPlayStoreDetailsEntity == null){
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return tsPlayStoreDetailsEntity;
    }

    private void setPlayStoreDetails(TSPlayStoreDetailsEntity tsPlayStoreDetailsEntity, PlayStoreDetailsBo playStoreDetailsBo) {
        if(playStoreDetailsBo.getShopName()!=null) {
            tsPlayStoreDetailsEntity.setShopName(playStoreDetailsBo.getShopName());
        }
        if(playStoreDetailsBo.getEmail()!=null){
            tsPlayStoreDetailsEntity.setEmail(playStoreDetailsBo.getEmail());
        }
        if(playStoreDetailsBo.getSecEmail()!=null){
            tsPlayStoreDetailsEntity.setSecEmail(playStoreDetailsBo.getSecEmail());
        }

        if(playStoreDetailsBo.getMobileNumber()!=null){
            tsPlayStoreDetailsEntity.setMobileNumber(playStoreDetailsBo.getMobileNumber());
        }

        if(playStoreDetailsBo.getLocation()!= null){
            tsPlayStoreDetailsEntity.setLocation(playStoreDetailsBo.getLocation());
        }

        if(playStoreDetailsBo.getState()!=null){
            tsPlayStoreDetailsEntity.setState(playStoreDetailsBo.getState());
        }

        if(playStoreDetailsBo.getAppIcon()!=null){
            tsPlayStoreDetailsEntity.setAppIcon(playStoreDetailsBo.getAppIcon());
        }

        if(playStoreDetailsBo.getAppVersionCode()!=null){
            tsPlayStoreDetailsEntity.setAppVersionCode(playStoreDetailsBo.getAppVersionCode());
        }
        if(playStoreDetailsBo.getAppVersionName()!=null){
            tsPlayStoreDetailsEntity.setAppVersionName(playStoreDetailsBo.getAppVersionName());
        }

        if(playStoreDetailsBo.getApk()!=null){
            tsPlayStoreDetailsEntity.setApk(playStoreDetailsBo.getApk());
        }

        if(playStoreDetailsBo.getKeyFIle()!=null){
            tsPlayStoreDetailsEntity.setKeyFIle(playStoreDetailsBo.getKeyFIle());
        }

        if(playStoreDetailsBo.getBundle()!=null){
            tsPlayStoreDetailsEntity.setBundle(playStoreDetailsBo.getBundle());
        }

        if(playStoreDetailsBo.getP12File()!=null){
            tsPlayStoreDetailsEntity.setP12File(playStoreDetailsBo.getP12File());
        }

        if(playStoreDetailsBo.getApplicationName()!=null){
            tsPlayStoreDetailsEntity.setApplicationName(playStoreDetailsBo.getApplicationName());
        }

    }

    @Override
    public TSSettingDetailEntity getSettingDetailForCustomer(String applicationId) throws RecordNotFoundException {
        TSPlayStoreDetailsEntity entity = retrievePsDetails(applicationId);
        return getSettingDetail(entity.getCC2code());
    }

    @Override
    public List<JsonObject> getReqPinCodeList(LcHeaderBO header, SearchBO searchBO) {
        List<JsonObject> list = new ArrayList<>();
        JsonObject obj;
        Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit());
        List<Object[]> entityList =  pinCodeReqRepo.getAllReq(header.getC2Code(), pageable);

        if (entityList.size() > 0) {

            if (searchBO.getSearchTerm() != null) {
                entityList = entityList.stream().filter(x->helper.getString(x[0]).contains(searchBO.getSearchTerm())).collect(Collectors.toList());
            }

            for (Object[] objList : entityList) {
                obj = new JsonObject();

                List<Object[]> res = pinCodeReqRepo.getStatusByPinCode(helper.getString(objList[0]));

                obj.addProperty("c_pincode", helper.getString(objList[0]));
                obj.addProperty("d_date", helper.getString(objList[1]));
                for (Object[] arr : res) {
                    obj.addProperty("c_active_status", helper.getString(arr[0]));
                    obj.addProperty("n_count", res.size());
                }
                list.add(obj);
            }
        }
        return list;
    }

    @Override
    public List<JsonObject> getReqPinCodeCustList(LcHeaderBO header, SearchBO searchBO, String pinCode) {
        List<JsonObject> list = new ArrayList<>();
        JsonObject json;
        List<Long> userIdList = new ArrayList<>();
        Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit(), Sort.by("tCreatedAt").descending());
        List<TSPinCodeReqEntity> entityList = pinCodeReqRepo.getByPin(header.getC2Code(), pinCode,  pageable);

        if (entityList.size() > 0) {

            if (searchBO.getSearchTerm() != null) {
                for (TSPinCodeReqEntity entity : entityList) {
                    userIdList.add(entity.getUserDetailEntity().getNUserId());
                }

                String sql = "SELECT ud.n_user_id, ud.c_first_name, cd.c_mobile_no, cd.c_email_id, ud.d_date_of_birth, " +
                        "   ud.c_gender " +
                        "   FROM user_detail ud " +
                        "   JOIN contact_detail cd ON cd.n_contact_id = ud.n_contact_id " +
                        "   WHERE ud.n_user_id IN :userIdList AND (LOWER(ud.c_first_name) LIKE LOWER('" + searchBO.getSearchTerm() + "%') OR cd.c_mobile_no LIKE '" + searchBO.getSearchTerm() + "%')";
                Query query = this.getQuery(sql);
                query.setParameter("userIdList", userIdList);
                List<Object[]> objList = this.getResultList(query);

                if (!objList.isEmpty()) {
                    for (Object[] obj : objList) {
                        json = new JsonObject();
                        json.addProperty("c_customer_code", helper.getString(obj[0]));
                        json.addProperty("c_customer_name", helper.getString(obj[1]));
                        json.addProperty("c_mobile_no", helper.getString(obj[2]));
                        json.addProperty("c_email_id", helper.getString(obj[3]));
                        json.addProperty("d_dob", helper.getString(obj[4]));
                        json.addProperty("c_gender", helper.getString(obj[5]));
                        list.add(json);
                    }
                }
            } else {
                for (TSPinCodeReqEntity entity : entityList) {
                    userIdList.add(entity.getUserDetailEntity().getNUserId());
                    json = new JsonObject();
                    json.addProperty("c_customer_code", entity.getUserDetailEntity().getNUserId());
                    json.addProperty("c_customer_name", entity.getUserDetailEntity().getCFirstName());
                    json.addProperty("c_mobile_no", entity.getUserDetailEntity().getContactDetail().getCMobileNo());
                    json.addProperty("c_email_id", entity.getUserDetailEntity().getContactDetail().getCEmailId());
                    json.addProperty("d_dob", helper.getString(entity.getUserDetailEntity().getDateOfBirth()));
                    json.addProperty("c_gender", entity.getUserDetailEntity().getGender());
                    list.add(json);
                }
            }
        }
        return list;
    }

    @Override
    public List<JsonObject> getServicePinCodeList(String c2Code, SearchBO searchBO) {
        Set<String> pinCodeSet = new HashSet<>();
        List<JsonObject> list = new ArrayList<>();
        JsonObject obj;
        Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit(), Sort.by("tCreatedAt").descending());
        List<TSPinCodeReqEntity> entityList =  pinCodeReqRepo.getServicePin(c2Code, pageable);

        if (entityList.size() > 0) {

            for (TSPinCodeReqEntity entity :entityList) {
                pinCodeSet.add(entity.getCPin());
            }

            if (searchBO.getSearchTerm() != null) {
                pinCodeSet = pinCodeSet.stream().filter(x->x.contains(searchBO.getSearchTerm())).collect(Collectors.toSet());
            }

            for (String pinCode : pinCodeSet) {
                obj = new JsonObject();
                obj.addProperty("c_pincode", pinCode);
                list.add(obj);
            }
        }
        return list;
    }

    @Override
    public long getReqPinCodeListCount(LcHeaderBO header, SearchBO searchBO) {
        Set<String> pinCodeSet = new HashSet<>();
        List<TSPinCodeReqEntity> entityList = pinCodeReqRepo.getAllReqCount(header.getC2Code());

        if (entityList.size() > 0) {

            for (TSPinCodeReqEntity entity : entityList) {
                pinCodeSet.add(entity.getCPin());
            }

            if (searchBO.getSearchTerm() != null) {
                pinCodeSet = pinCodeSet.stream().filter(x -> x.contains(searchBO.getSearchTerm())).collect(Collectors.toSet());
            }
        }
        return pinCodeSet.size();
    }

    @Override
    public long getReqPinCodeCustListCount(LcHeaderBO header, SearchBO searchBO, String pincode) {
        long count = 0;
        List<Long> userIdList = new ArrayList<>();
        List<TSPinCodeReqEntity> entityList = pinCodeReqRepo.getByPin(header.getC2Code(), pincode);

        if (entityList.size() > 0) {

            if (searchBO.getSearchTerm() != null) {
                for (TSPinCodeReqEntity entity : entityList) {
                    userIdList.add(entity.getUserDetailEntity().getNUserId());
                }

                String sql = "SELECT COUNT(*) " +
                        "   FROM user_detail ud " +
                        "   JOIN contact_detail cd ON cd.n_contact_id = ud.n_contact_id " +
                        "   WHERE ud.n_user_id IN :userIdList AND (LOWER(ud.c_first_name) LIKE LOWER('" + searchBO.getSearchTerm() + "%') OR cd.c_mobile_no LIKE '" + searchBO.getSearchTerm() + "%')";
                Query query = this.getQuery(sql);
                query.setParameter("userIdList", userIdList);
                Object result = this.getSingleResult(query);
                count = Long.parseLong(String.valueOf(result));
            } else
                count = entityList.size();
        }
        return count;
    }

    @Override
    public long getServicePinCodeListCount(String c2Code, SearchBO searchBO) {
        Set<String> pinCodeSet = new HashSet<>();
        List<TSPinCodeReqEntity> entityList = pinCodeReqRepo.getServicePin(c2Code);

        if (entityList.size() > 0) {

            for (TSPinCodeReqEntity entity : entityList) {
                pinCodeSet.add(entity.getCPin());
            }

            if (searchBO.getSearchTerm() != null) {
                pinCodeSet = pinCodeSet.stream().filter(x -> x.contains(searchBO.getSearchTerm())).collect(Collectors.toSet());
            }

        }
        return pinCodeSet.size();
    }

    @Override
    public void activatePinCodeReq(String c2Code, String pin, String status) throws RecordNotFoundException {
        List<TSPinCodeReqEntity> list = pinCodeReqRepo.getByPin(c2Code, pin);
        if (list.size() > 0) {
            pinCodeReqRepo.activatePinCodeReq(c2Code, pin, status);
        } else {
            throw new RecordNotFoundException("Record Not found!");
        }
    }
}

