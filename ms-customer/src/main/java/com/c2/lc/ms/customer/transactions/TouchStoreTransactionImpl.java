package com.c2.lc.ms.customer.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.bos.PlayStoreDetailsBo;
import com.c2.lc.ms.customer.entities.customer.TSPlayStoreDetailsEntity;
import com.c2.lc.ms.customer.entities.customer.TSSettingDetailEntity;
import com.c2.lc.ms.customer.entities.customer.TSStoreRegisterEntity;
import com.c2.lc.ms.customer.services.interfaces.TouchStoreService;
import com.c2.lc.ms.customer.transactions.base.LcBaseTransactionImpl;
import com.c2.lc.ms.customer.transactions.interfaces.TouchStoreTransaction;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TouchStoreTransactionImpl extends LcBaseTransactionImpl implements TouchStoreTransaction {

    @Autowired private TouchStoreService touchStoreService;

    @Override
    public List<TSStoreRegisterEntity> getRegisteredStores(LcHeaderBO headerBO) throws RecordNotFoundException {
        return touchStoreService.getRegisteredStores(headerBO);
    }

    @Override
    public void save(LcHeaderBO header, TSStoreRegisterEntity tsStoreRegisterEntity) {
        touchStoreService.save(header, tsStoreRegisterEntity);
    }

    @Override
    public void saveSettingDetail(LcHeaderBO header, TSSettingDetailEntity settingDetail) {
        touchStoreService.saveSettingDetail(header, settingDetail);
    }

    @Override
    public TSSettingDetailEntity getSettingDetail(String c2Code) throws RecordNotFoundException {
        return touchStoreService.getSettingDetail(c2Code);
    }

    @Override
    public String storeRegStatus(String c2Code) {
        return touchStoreService.storeRegStatus(c2Code);
    }

    @Override
    public void savePsDetails(LcHeaderBO headerBO, PlayStoreDetailsBo playStoreDetailsBo) throws DuplicateRecordException {
         touchStoreService.savePsDetails(headerBO,playStoreDetailsBo);
    }

    @Override
    public void UpdatePsDetails(Long userId, PlayStoreDetailsBo playStoreDetailsBo) throws RecordNotFoundException {
        touchStoreService.updatePsDetails(userId,playStoreDetailsBo);
    }

    @Override
    public TSPlayStoreDetailsEntity retrievePsDetails(String c_application_id) throws RecordNotFoundException {
        return touchStoreService.retrievePsDetails(c_application_id);
    }

    @Override
    public TSSettingDetailEntity getSettingDetailForCustomer(String applicationId) throws RecordNotFoundException {
        return touchStoreService.getSettingDetailForCustomer(applicationId);
    }

    @Override
    public long getReqPinCodeListCount(LcHeaderBO header, SearchBO searchBO) {
        return touchStoreService.getReqPinCodeListCount(header,searchBO);
    }

    @Override
    public List<JsonObject> getReqPinCodeList(LcHeaderBO header, SearchBO searchBO) {
        return touchStoreService.getReqPinCodeList(header, searchBO);
    }

    @Override
    public List<JsonObject> getReqPinCodeCustList(LcHeaderBO header, SearchBO searchBO, String pinCode) {
        return touchStoreService.getReqPinCodeCustList(header, searchBO, pinCode);
    }

    @Override
    public List<JsonObject> getServicePinCodeList(String c2Code, SearchBO searchBO) {
        return touchStoreService.getServicePinCodeList(c2Code, searchBO);
    }

    @Override
    public long getReqPinCodeCustListCount(LcHeaderBO header, SearchBO searchBO, String c_pincode) {
        return touchStoreService.getReqPinCodeCustListCount(header,searchBO,c_pincode);
    }

    @Override
    public long getServicePinCodeListCount(String c2Code, SearchBO searchBO) {
        return touchStoreService.getServicePinCodeListCount(c2Code,searchBO);
    }

    @Override
    public void activatePinCodeReq(String c2Code, String pin, String status) throws RecordNotFoundException {
        touchStoreService.activatePinCodeReq(c2Code, pin, status);
    }
}
