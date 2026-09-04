package com.c2.lc.ms.master.services;

import com.c2.lc.ms.master.repos.mysql.*;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.SchemeService;
import com.c2.lc.ms.master.entities.mysql.CustSchemeDetEntity;
import com.c2.lc.ms.master.entities.mysql.CustSchemeMstEntity;
import com.c2.lc.ms.master.entities.mysql.LiveSchemeMstEntity;
import com.c2.lc.ms.master.models.ItemSellersList;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Slf4j
@Service
public class SchemeServiceImpl extends MasterBaseServiceImpl implements SchemeService {

    @Autowired
    CustActMstRepository custActMstRepository;
    @Autowired
    CustCategoryMstRepository custCategoryMstRepository;
    @Autowired
    CustSchemeDetRepository custSchemeDetRepository;
    @Autowired
    CustSchemeMstRepository custSchemeMstRepository;
    @Autowired
    LiveSchemeMstRepository liveSchemeMstRepository;
    @Autowired
    LcLoC2CodeRepository lcLoC2CodeRepository;

    @Override
    public JsonObject getSellerScheme(ItemSellersList itemSellersList) {
        String cSCheme = "";

        int inOutFlag = itemSellersList.getN_in_out_flag();

        CustSchemeDetEntity custSchemeDetEntity = null;
        CustSchemeMstEntity custSchemeMstEntity = null;
        LiveSchemeMstEntity liveSchemeMstEntity = null;


        //get cust_scheme_det using c2code ie seller,seller item code and cust_cat_code
        custSchemeDetEntity = custSchemeDetRepository.findByItemCustCatCode(itemSellersList.getC_seller_code(),
                itemSellersList.getC_seller_item_code(), itemSellersList.getC_cat_code());

        //get max ldate from cust_scheme_mst using seller code and seller item to get latest record from cust_scheme_mst
        Date maxLDate = custSchemeMstRepository.getMaxDate(itemSellersList.getC_seller_code(), itemSellersList.getC_seller_item_code());
        //get latest scheme
        if (maxLDate != null) {
            custSchemeMstEntity = custSchemeMstRepository.findByCustItemCode(itemSellersList.getC_seller_code(), itemSellersList.getC_seller_item_code(), maxLDate);
        }
        //get lve scheme using sellercode as c2code and seller iemcode
        liveSchemeMstEntity = liveSchemeMstRepository.findByFirmItemCode(itemSellersList.getC_seller_code(), itemSellersList.getC_seller_item_code());
        log.info("inoutFlag {}",inOutFlag);
        Double discountValue = 0.00; //will used for sorting
        Double max_scheme_val = 0.00 ;
        if (inOutFlag == 1) {
            if(custSchemeMstEntity != null){
                String cSChemeCloseDate = helper.getLocalDate(helper.getString(custSchemeMstEntity.getdSchCloseDate())).toString();
                if (helper.isEmpty(cSChemeCloseDate) || cSChemeCloseDate.equals("0000-00-00") || cSChemeCloseDate.equals("1900-01-01") || helper.getLocalDate(cSChemeCloseDate).isAfter(helper.getCurrentDate())) {
                    String cCatCode = itemSellersList.getC_cat_code();
                    showLog("check scheme category",cCatCode);
                    if (cCatCode.equals("RETAIL")) {
                        showLog(cCatCode,custSchemeMstEntity.toString());
                        if (custSchemeMstEntity.getnSchQty1() != 0) {
                            cSCheme = helper.getString(custSchemeMstEntity.getnSchQty1());
                            cSCheme += (custSchemeMstEntity.getnFreeQty1() != 0) ? custSchemeMstEntity.getnFreeQty1() : "";
                            cSCheme += (custSchemeMstEntity.getnSchDiscPerc1().doubleValue()) > 0.00 ? custSchemeMstEntity.getnSchDiscPerc1() : "";
                            discountValue =  getSchemeDiscValue(itemSellersList.getN_rate(),custSchemeMstEntity.getnSchQty1(),
                                    custSchemeMstEntity.getnFreeQty1(),custSchemeMstEntity.getnSchDiscPerc1().doubleValue());
                            max_scheme_val = discountValue;
                        }
                        if (custSchemeMstEntity.getnSchQty2() != 0) {
                            cSCheme += ",";
                            cSCheme += helper.getString(custSchemeMstEntity.getnSchQty2());
                            cSCheme += (custSchemeMstEntity.getnFreeQty2() != 0) ? custSchemeMstEntity.getnFreeQty2() : "";
                            cSCheme += (custSchemeMstEntity.getnSchDiscPerc2().doubleValue()) > 0.00 ? custSchemeMstEntity.getnSchDiscPerc2() : "";
                            discountValue =  getSchemeDiscValue(itemSellersList.getN_rate(),custSchemeMstEntity.getnSchQty2(),
                                    custSchemeMstEntity.getnFreeQty2(),custSchemeMstEntity.getnSchDiscPerc2().doubleValue());
                            max_scheme_val = getMaxOfTwo(max_scheme_val,discountValue);
                        }
                        if (custSchemeMstEntity.getnSchQty3() != 0) {
                            cSCheme += ",";
                            cSCheme += helper.getString(custSchemeMstEntity.getnSchQty3());
                            cSCheme += (custSchemeMstEntity.getnFreeQty3() != 0) ? custSchemeMstEntity.getnFreeQty3() : "";
                            cSCheme += (custSchemeMstEntity.getnSchDiscPerc3().doubleValue()) > 0.00 ? custSchemeMstEntity.getnSchDiscPerc3() : "";
                            discountValue =  getSchemeDiscValue(itemSellersList.getN_rate(),custSchemeMstEntity.getnSchQty3(),
                                    custSchemeMstEntity.getnFreeQty3(),custSchemeMstEntity.getnSchDiscPerc3().doubleValue());
                            max_scheme_val = getMaxOfTwo(max_scheme_val,discountValue);
                        }
                        showLog(cCatCode,cSCheme);
                    } else {
                        showLog(cCatCode,custSchemeMstEntity.toString());
                        if (custSchemeDetEntity != null) {
                            if (custSchemeDetEntity.getnSchQty1() != 0) {
                                cSCheme = helper.getString(custSchemeDetEntity.getnSchQty1());
                                cSCheme += (custSchemeDetEntity.getnFreeQty1() != 0) ? custSchemeDetEntity.getnFreeQty1() : "";
                                cSCheme += (custSchemeDetEntity.getnSchDiscPerc1().doubleValue()) > 0.00 ? custSchemeDetEntity.getnSchDiscPerc1() : "";
                                discountValue =  getSchemeDiscValue(itemSellersList.getN_rate(),custSchemeDetEntity.getnSchQty1(),
                                        custSchemeDetEntity.getnFreeQty1(),custSchemeDetEntity.getnSchDiscPerc1().doubleValue());
                                max_scheme_val = discountValue;
                            }
                            if (custSchemeDetEntity.getnSchQty2() != 0) {
                                cSCheme += ",";
                                cSCheme += helper.getString(custSchemeDetEntity.getnSchQty2());
                                cSCheme += (custSchemeDetEntity.getnFreeQty2() != 0) ? custSchemeDetEntity.getnFreeQty2() : "";
                                cSCheme += (custSchemeDetEntity.getnSchDiscPerc2().doubleValue()) > 0.00 ? custSchemeDetEntity.getnSchDiscPerc2() : "";
                                discountValue =  getSchemeDiscValue(itemSellersList.getN_rate(),custSchemeDetEntity.getnSchQty2(),
                                        custSchemeDetEntity.getnFreeQty2(),custSchemeDetEntity.getnSchDiscPerc2().doubleValue());
                                max_scheme_val = getMaxOfTwo(max_scheme_val,discountValue);
                            }
                            if (custSchemeDetEntity.getnSchQty3() != 0) {
                                cSCheme += ",";
                                cSCheme += helper.getString(custSchemeDetEntity.getnSchQty3());
                                cSCheme += (custSchemeDetEntity.getnFreeQty3() != 0) ? custSchemeMstEntity.getnFreeQty3() : "";
                                cSCheme += (custSchemeDetEntity.getnSchDiscPerc3().doubleValue()) > 0.00 ? custSchemeDetEntity.getnSchDiscPerc3() : "";
                                discountValue =  getSchemeDiscValue(itemSellersList.getN_rate(),custSchemeDetEntity.getnSchQty3(),
                                        custSchemeDetEntity.getnFreeQty3(),custSchemeDetEntity.getnSchDiscPerc3().doubleValue());
                                max_scheme_val = getMaxOfTwo(max_scheme_val,discountValue);
                            }
                        }
                        showLog(cCatCode,cSCheme);
                    }
                }
                showLog("cust_scheme_mst","PROCESS ENDED, Scheme Closing Date Expired !");
            }
            showLog("cust_scheme_mst","PROCESS ENDED, No Records Found! ");
        } else{
            cSCheme = (liveSchemeMstEntity != null ) ? liveSchemeMstEntity.getcScheme() : "NA";

            if (itemSellersList.getC_seller_code().equals("001066")) {
                cSCheme = cSCheme;
            }
            if ( !cSCheme.equals("NA")) discountValue = liveSchemeValue(itemSellersList.getN_rate(),cSCheme,discountValue);

            max_scheme_val = discountValue;
            showLog("Live Scheme ",cSCheme);

        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("c_scheme",helper.isEmpty(cSCheme) ? "NA" : cSCheme);
        jsonObject.addProperty("d_scheme_max_value",max_scheme_val);
        return jsonObject;
    }

    private Double liveSchemeValue(Double n_rate, String cSCheme, Double discountValue) {
        //cSCheme.indexOf("+") > 0 ? Double.parseDouble(cSCheme.substring(cSCheme.indexOf("+") + 1, cSCheme.length())) : 0;
        String[] schemes = cSCheme.split(",");
        for (String str : schemes) {
            discountValue = getMaxOfTwo(discountValue,getSchemeFromString(n_rate,str));
        }
        return discountValue;
    }

    private double getSchemeFromString(Double n_rate, String str) {
        //identify if its qty scheme or percentage
        int index =str.indexOf("+");
        double discValue = 0.00;
        if (index > 0) {
            try{
                if (str.indexOf("%") > 0) {
                    discValue= getSchemeDiscValue(n_rate,Integer.parseInt(str.substring(0,index)),
                            0,Double.parseDouble(str.substring(index +1,str.indexOf("%") -1)));
                } else {
                    discValue= getSchemeDiscValue(n_rate,Integer.parseInt(str.substring(0,index)),
                            Integer.parseInt(str.substring(index + 1, str.length())),0);
                }
            } catch (Exception e){
                showLog("Live SCheme - Exception",e.getMessage());
            }
        }
        return discValue;
    }

    private Double getSchemeDiscValue(Double saleRate,Integer schQty, Integer freeQty, double schDiscPer) {
        double schemeValue = 0.00;

        if (freeQty > 0) {
            schemeValue = saleRate * freeQty;
        } else if (schDiscPer > 0.00) {
            schemeValue = (saleRate * (schDiscPer / 100))/schQty;
        }
        return schemeValue;
    }

    private double getMaxOfTwo(double val1, double val2) {
        return Math.max(val1,val2);
    }

    private void showLog(String schemSection,String discountValue){
        log.info("Section : {} ,Value : {}",schemSection,discountValue);
    }
}