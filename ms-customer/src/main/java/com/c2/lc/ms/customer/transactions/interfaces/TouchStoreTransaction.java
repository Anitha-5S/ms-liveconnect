package com.c2.lc.ms.customer.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.PlayStoreDetailsBo;
import com.c2.lc.ms.customer.entities.customer.TSPlayStoreDetailsEntity;
import com.c2.lc.ms.customer.entities.customer.TSSettingDetailEntity;
import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransaction;
import com.google.gson.JsonObject;

import java.util.List;

public interface TouchStoreTransaction extends LcBaseTransaction {
    List<TSStoreRegisterEntity> getRegisteredStores(LcHeaderBO headerBO) throws RecordNotFoundException;

    void save(LcHeaderBO header, TSStoreRegisterEntity tsStoreRegisterEntity);

    void saveSettingDetail(LcHeaderBO header, TSSettingDetailEntity settingDetail);

    TSSettingDetailEntity getSettingDetail(String c2Code) throws RecordNotFoundException;

    String storeRegStatus(String c2Code);

    void savePsDetails(LcHeaderBO headerBO, PlayStoreDetailsBo playStoreDetailsBo) throws DuplicateRecordException;

    void UpdatePsDetails(Long userId, PlayStoreDetailsBo playStoreDetailsBo) throws RecordNotFoundException;

    TSPlayStoreDetailsEntity retrievePsDetails(String c_application_id) throws RecordNotFoundException;

    TSSettingDetailEntity getSettingDetailForCustomer(String applicationId) throws RecordNotFoundException;

    List<JsonObject> getReqPinCodeList(LcHeaderBO header, SearchBO searchBO);

    List<JsonObject> getReqPinCodeCustList(LcHeaderBO header, SearchBO searchBO, String pinCode);

    List<JsonObject> getServicePinCodeList(String c2Code, SearchBO searchBO);

    void activatePinCodeReq(String c2Code, String pin, String status) throws RecordNotFoundException;

    long getReqPinCodeListCount(LcHeaderBO header, SearchBO searchBO);

    long getReqPinCodeCustListCount(LcHeaderBO header, SearchBO searchBO, String c_pincode);

    long getServicePinCodeListCount(String c2Code, SearchBO searchBO);
}
