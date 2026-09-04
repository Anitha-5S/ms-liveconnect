package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.master.bos.OnePharmaBo;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.DocumentService;
import com.c2.lc.ms.master.services.interfaces.OnePharmaService;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntity;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntityPK;
import com.c2.lc.ms.master.repos.mysql.OnePharmaEmailsRepository;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.utils.MsMessages;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class OnePharmaServiceImpl extends MasterBaseServiceImpl implements OnePharmaService {

    @Autowired
    OnePharmaEmailsRepository onePharmaEmailsRepository;
    @Autowired private DocumentService documentService;
    @Autowired
    private SearchServiceImpl searchService;

    @Value("${ms.cust.seller.service.api.url}")
    private String sellerUrl;

    @Override
    public void insert(OnePharmaEmailsEntity onePharma) throws DuplicateRecordException {
        OnePharmaEmailsEntity onePharmaEmail = onePharmaEmailsRepository.findByEmailId(onePharma.getcEmail());
        if (onePharmaEmail != null) {
            throw new DuplicateRecordException(onePharma.getcEmail(), "Email already exists");
        }
        onePharma.setcStatus(Constants.STATUS_ACTIVE);
        onePharmaEmailsRepository.save(onePharma);
    }

    @Override
    public OnePharmaEmailsEntity getByEmailId(String emailId) throws RecordNotFoundException {
        OnePharmaEmailsEntity onePharma = onePharmaEmailsRepository.findByEmailId(emailId);
        if (onePharma == null) {
            throw new RecordNotFoundException(emailId, "Record not found! ");
        }
        return onePharma;
    }

    @Override
    public void deleteById(OnePharmaEmailsEntityPK onePharmaPK) {
        onePharmaEmailsRepository.deleteById(onePharmaPK);
    }

    @Override
    public JsonObject getLatestItemCode(String c2code, JsonObject data) {

        JsonObject response = new JsonObject();

        JsonArray itemDescriptionArray = (data.get("c_description").getAsJsonArray());
        String itemDescription = getStringArray(data.get("c_description").getAsJsonArray());
        int totalItemCount = itemDescriptionArray.size();

        String selectSql = " SELECT c_code, c_name,COALESCE(n_mrp,0) as mrp,COALESCE( n_sale_rate,0) as saleRate " +
                " FROM cust_item_mst mst " +
                " WHERE mst.c_name in (" + itemDescription + " )" +
                " AND mst.c_c2code = :c_c2code";

        Query query = this.getQuery(c2code, selectSql);

        query.setParameter("c_c2code", data.get("c_c2code").getAsString());

        List<Object[]> resultList = this.getResultList(query);

        int mappedItemsCount = resultList.size();
        int umMappedItemsCount = totalItemCount - mappedItemsCount;

        JsonArray resultJsonArray = new JsonArray();

        for (Object[] dataObjects : resultList) {
            int i = -1;
            JsonObject dataJsonObject = new JsonObject();
            dataJsonObject.addProperty("c_code", helper.getString(dataObjects[++i]));
            dataJsonObject.addProperty("c_name", helper.getString(dataObjects[++i]));
            dataJsonObject.addProperty("mrp", helper.getBigDecimal(dataObjects[++i]));
            dataJsonObject.addProperty("saleRate", helper.getBigDecimal(dataObjects[++i]));
            resultJsonArray.add(dataJsonObject);
        }
        response.add("c_mapped_items", resultJsonArray);
        response.addProperty("n_mapped_items_count", mappedItemsCount);
        response.addProperty("n_unmapped_items_count", umMappedItemsCount);
        response.addProperty("n_item_count", totalItemCount);
        return response;
    }

    @Override
    public String getBlockListedEmail(JsonObject data) {

        String selectSql = " SELECT c_status " +
                " FROM one_pharma_emails  " +
                " WHERE c_c2code = :c_c2code " +
                " AND c_email = :email ";

        Query query = this.getQuery(selectSql);

        query.setParameter("c_c2code", data.get("c_c2code").getAsString());
        query.setParameter("email", data.get("email").getAsString());

        String response = (String) this.getSingleResult(query);
        return response;
    }

    @Override
    public JsonArray getInvoiceList(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = null;
        Query query = null;
        JsonArray jsonArray = new JsonArray();

        List<String> sellerBuyerCode = getSellerCodeAndBuyerCode(lcHeaderBO);

        LocalDateTime current = LocalDate.now().atTime(LocalTime.MAX);
        if (onePharmaBo.getToDate() != null) {
            current = helper.convertStringToDate(onePharmaBo.getToDate()).atTime(23,59,59);
        }
        LocalDateTime minusMonths = current.minusMonths(3);
        if (onePharmaBo.getFromDate() != null) {
            minusMonths = helper.convertStringToDate(onePharmaBo.getFromDate()).atStartOfDay();
        }
        if(!helper.isEmpty(onePharmaBo.getSellerName()) && (helper.isEmpty(onePharmaBo.getInvoiceNumber()))){
            sql= getInvoiceListBySellerName();
            query = this.getQuery(sql);
            query.setParameter("sellerName", helper.getLikeQueryString(onePharmaBo.getSellerName()));
            query.setParameter("list", sellerBuyerCode);
            query.setParameter("fromDate",minusMonths);
            query.setParameter("toDate",current);
        }
        else if(!helper.isEmpty(onePharmaBo.getInvoiceNumber()) && (helper.isEmpty(onePharmaBo.getSellerName()))){
            sql= getInvoiceListByInvNumber();
            query = this.getQuery(sql);
            query.setParameter("invNumber", helper.getLikeQueryString(onePharmaBo.getInvoiceNumber()));
            query.setParameter("list", sellerBuyerCode);
            query.setParameter("fromDate",minusMonths);
            query.setParameter("toDate",current);
        }
        else{
            sql= getInvoiceList();
            query = this.getQuery(sql);
            query.setParameter("list", sellerBuyerCode);
            query.setParameter("fromDate",minusMonths);
            query.setParameter("toDate",current);
        }

        List<Object[]> resultList = this.getResultList(query, searchBO.getPage(), searchBO.getLimit());
        if (resultList == null) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            //jsonObject.addProperty("n_sr_no", 0);
            jsonObject.addProperty("c_seller_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_seller_code", helper.getString(objects[++i]));
            jsonObject.addProperty("n_inv_number", helper.getBigDecimal(objects[++i]));
            jsonObject.addProperty("c_inv_date", helper.getString(objects[++i]));
            jsonObject.addProperty("n_inv_amt", helper.getBigDecimal(objects[++i]));
            jsonObject.addProperty("c_inv_prefix", helper.getString(objects[++i]));
            jsonObject.addProperty("c_inv_year", helper.getString(objects[++i]));

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private List<String> getSellerCodeAndBuyerCode(LcHeaderBO lcHeaderBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        List<String> sellerBuyerCode = new ArrayList<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-csquare-c2-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-br-code", helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-firm-id",helper.toString(lcHeaderBO.getFirmId()));
        headers.put("x-csquare-terminal-id", helper.toString(lcHeaderBO.getUserId()));
        JsonArray jsonArray1 = searchService.callCustomerService(headers, sellerUrl);
        if (jsonArray1.size() > 0) {
            for (int i = 0; i < jsonArray1.size(); i++) {
                JsonObject jsonObject = jsonArray1.get(i).getAsJsonObject();
                sellerBuyerCode.add(jsonObject.get("c_seller_code").getAsString() + jsonObject.get("c_buyer_code").getAsString());
            }
        }
        if(!(sellerBuyerCode.size() > 0)) {
            throw new RecordNotFoundException("No Sellers Found!");
        }
        return sellerBuyerCode;
    }

    @Override
    public JsonArray getItems(OnePharmaBo onePharmaBo) {
        String sql = null;
        Query query = null;
        JsonArray jsonArray = new JsonArray();
        sql= getItems();
        query = this.getQuery(sql);
        query.setParameter("invNumber", onePharmaBo.getInvoiceNumber());
        query.setParameter("sellerCode",onePharmaBo.getSellerCode());
        query.setParameter("date",onePharmaBo.getFromDate());

        List<Object[]> resultList = this.getResultList(query);
        if (resultList == null) {
            return jsonArray;
        }
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            //jsonObject.addProperty("n_sr_no", 0);
            jsonObject.addProperty("c_item_code", helper.getString(objects[++i]));
            jsonObject.addProperty("c_item_name", helper.getString(objects[++i]));
            jsonObject.addProperty("n_qty", helper.getBigDecimal(objects[++i]));
            jsonObject.addProperty("n_sale_rate", helper.getBigDecimal(objects[++i]));
            jsonObject.addProperty("n_amount", helper.getBigDecimal(objects[++i]));

            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    private String getItems() {
        return  "SELECT DISTINCT cid.c_item_code AS ItemCode,  "  +
                 " REPLACE(REPLACE(REPLACE(REPLACE(itmst.c_name,'#',''), '$', ''), '*', ''), '~','') AS ItemName,   "  +
                 " cid.n_qty AS ItemQty,  "  +
                 " cid.n_sale_rate AS SellerRate,  "  +
                 " cid.n_amount AS Amount  "  +
                 " FROM cust_inv_det cid   "  +
                 " LEFT JOIN cust_item_mst itmst on itmst.c_code = cid.c_item_code  "  +
                 " AND cid.c_c2code = itmst.c_c2code and itmst.c_br_code=cid.c_br_code  "  +
                 " WHERE cid.n_srno = :invNumber  "  +
                 " AND cid.c_c2code = :sellerCode  "  +
                 " AND cid.d_date = :date "+
                 " ORDER BY itmst.c_name asc ";
    }

    private String getInvoiceListByInvNumber() {
        return  "SELECT lc.c_name as sellerName,lc.c_code as sellerCode,inv.n_srno as invNumber,inv.d_date as invDate,inv.n_total as invamt, inv.c_prefix as invPrefix, inv.c_year as invYear "+
                 " FROM cust_inv_mst inv force index (c2codeCustCode) " +
                 " LEFT JOIN lc_c2code_mst lc on inv.c_c2code=lc.c_code " +
                 " LEFT JOIN lc_supp_chem_comb lscc on lscc.c_c2code = inv.c_c2code and lscc.c_chem_code = inv.c_cust_code and inv.n_srno like :invNumber "+
                 " WHERE lscc.c_supp_chem IN :list AND inv.d_date >= :fromDate and inv.d_date <= :toDate " +
                 " ORDER BY inv.d_date desc ";
    }

    private String getInvoiceListBySellerName() {
        return "SELECT lc.c_name as sellerName,lc.c_code as sellerCode,inv.n_srno as invNumber,inv.d_date as invDate,inv.n_total as invamt, inv.c_prefix as invPrefix, inv.c_year as invYear "+
                " FROM cust_inv_mst inv force index (c2codeCustCode) " +
                " LEFT JOIN lc_c2code_mst lc on inv.c_c2code=lc.c_code "+
                " LEFT JOIN lc_supp_chem_comb lscc on lscc.c_c2code = inv.c_c2code and lscc.c_chem_code = inv.c_cust_code and lc.c_name like :sellerName "+
                " WHERE lscc.c_supp_chem IN :list AND inv.d_date >= :fromDate AND inv.d_date <= :toDate " +
                " ORDER BY inv.d_date desc  ";
    }

    private String getInvoiceList() {
        return "SELECT lc.c_name as sellerName,lc.c_code as sellerCode,inv.n_srno as invNumber,inv.d_date as invDate,inv.n_total as invamt, inv.c_prefix as invPrefix, inv.c_year as invYear"+
                " FROM cust_inv_mst inv FORCE INDEX (c2codeCustCode) " +
                " LEFT JOIN lc_c2code_mst lc ON inv.c_c2code=lc.c_code " +
                " LEFT JOIN lc_supp_chem_comb lscc ON lscc.c_c2code = inv.c_c2code AND lscc.c_chem_code = inv.c_cust_code "+
                " WHERE lscc.c_supp_chem IN :list AND inv.d_date >= :fromDate AND inv.d_date <= :toDate " +
                " ORDER BY inv.d_date desc " ;
    }

    @Override
    public long getInvoiceListCount(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        String sql = null;
        Query query = null;
        LocalDateTime current = LocalDate.now().atTime(LocalTime.MAX);
        List<String> sellerBuyerCode = getSellerCodeAndBuyerCode(lcHeaderBO);
        if (onePharmaBo.getToDate() != null) {
            current = helper.convertStringToDate(onePharmaBo.getToDate()).atTime(23,59,59);
        }
        LocalDateTime minusMonths = current.minusMonths(3);
        if (onePharmaBo.getFromDate() != null) {
            minusMonths = helper.convertStringToDate(onePharmaBo.getFromDate()).atStartOfDay();
        }
        if(!helper.isEmpty(onePharmaBo.getSellerName()) && (helper.isEmpty(onePharmaBo.getInvoiceNumber()))){
            sql= "SELECT COUNT(*) FROM (" +getInvoiceListBySellerName()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("sellerName", onePharmaBo.getSellerName() + "%");
            query.setParameter("list", sellerBuyerCode);
            query.setParameter("fromDate",minusMonths);
            query.setParameter("toDate",current);
        }
        else if(!helper.isEmpty(onePharmaBo.getInvoiceNumber()) && (helper.isEmpty(onePharmaBo.getSellerName()))){
            sql= "SELECT COUNT(*) FROM (" +getInvoiceListByInvNumber()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("invNumber", onePharmaBo.getInvoiceNumber()+"%");
            query.setParameter("list", sellerBuyerCode);
            query.setParameter("fromDate",minusMonths);
            query.setParameter("toDate",current);
        }
        else{
            sql= "SELECT COUNT(*) FROM (" +getInvoiceList()+ ") DUMMY";
            query = this.getQuery(sql);
            query.setParameter("list", sellerBuyerCode);
            query.setParameter("fromDate",minusMonths);
            query.setParameter("toDate",current);
        }
        Object result = this.getSingleResult(query);
        assert result != null;
        return new BigInteger(String.valueOf(result)).longValue();
    }

    @Override
    public JsonArray getInvoiceRecord(OnePharmaBo onePharmaBo) throws RecordNotFoundException, DocumentException, IOException {
        JsonArray jsonArray = new JsonArray();
        String sellerCode =  onePharmaBo.getSellerCode();
        String invNumber  =  onePharmaBo.getInvoiceNumber();
        String invYear = onePharmaBo.getYear();
        String prefix = onePharmaBo.getPrefix();
        String sql =preparedStatementForInvoiceRecord(sellerCode,invNumber,invYear,prefix);
        Query query = this.getQuery(sql);

        List<Object[]> resultList = this.getResultList(query);
        if (resultList == null) {
            throw new RecordNotFoundException(MsMessages.RECORD_NOT_FOUND);
        }
        List<String> columnList = Lists.newArrayList("C2Code" , "Br" , "Yr" , "Pfx" , "InvNo" , "InvDate" , "InvDay" , "InvMonth" , "InvYear" , "CustCode"
                , "CustC2id" , "OtherChg" , "InvFrght" , "shipcode" , "CrntPfx" , "CrntNo" , "CrntAmt" , "DbntPfx" , "DbntNo" , "DbntAmt"
                , "AdvPfx" , "AdvNo" , "AdvAmt" , "ReplPfx" , "ReplNo" , "ReplAmt" , "InvAmt" , "gstEnabled" , "fromGstNo" , "fromGstType"
                , "toGstNo" , "cgstTotal" , "sgstTotal" , "igstTotal" , "cessAmount" , "ItemCode" , "ItemC2id"
                , "ItemName" , "ItemName2" , "PackName" , "BatchNo" , "ExpDate" , "ExpDay" , "ExpMonth" , "ExpYear" , "InvQty" , "InvScQty"
                , "InvScDis" , "SchPer" , "InvDisc" , "SaleRate" , "VatPer" , "TSPer" , "LoclSale" , "CSTPer" , "CessPer" , "VatOnMrp"
                , "VatMrp" , "ItemMRP" , "TaxOnSch" , "DCNo" , "Repl" , "RefOrdNo" , "RefDate" , "MfgComp" , "MktgComp" , "ConvFact"
                , "CuItemId" , "CuItemCF" , "lostitem" , "Rack" , "MfCode" , "MrpIncl" , "CrDays" , "SmanCode" , "LRNo"
                , "LRDay" , "LRMonth" , "LRyear" , "itmTotal" , "hsnCode" , "cgstPer" , "cgstAmt" , "sgstPer" , "sgstAmt" , "igstPer"
                , "igstAmt" , "cessPer" , "cessAmt" , "MfacDate" , "BatchKey" , "Seller" , "Buyer" , "ItemPTR" , "TCSPer" , "TCSAmt"
                , "totalDisc" , "sellerAdd1" , "sellerAdd2" , "sellerAdd3" , "sellerCity" , "sellerPin" , "buyerAdd1" , "buyerAdd2"
                , "buyerAdd3" , "buyerCity" , "buyerPin" , "itemTaxableAmt" , "taxItemTotal" , "itemDiscAmt"
                , "cases" , "sellerLogo" , "totalItemAmt", "tax_summ_0_total" , "tax_summ_0_schAmt" , "tax_summ_0_discAmt" , "tax_summ_5_total"
                , "tax_summ_5_schAmt" , "tax_summ_5_discAmt" , "tax_summ_5_sgstAmt" , "tax_summ_5_cgstAmt" , "tax_summ_5_igstAmt" , "tax_summ_12_total"
                , "tax_summ_12_schAmt" , "tax_summ_12_discAmt" , "tax_summ_12_sgstAmt" , "tax_summ_12_cgstAmt" , "tax_summ_12_igstAmt" , "tax_summ_18_total"
                , "tax_summ_18_schAmt" , "tax_summ_18_discAmt" , "tax_summ_18_sgstAmt" , "tax_summ_18_cgstAmt" , "tax_summ_18_igstAmt", "tax_summ_28_total"
                , "tax_summ_28_schAmt" , "tax_summ_28_discAmt" , "tax_summ_28_sgstAmt" , "tax_summ_28_cgstAmt" , "tax_summ_28_igstAmt"
                , "free" , "DueDate", "OrderDate");

        JsonObject jsonObj;
        for (Object[] objects : resultList) {
            jsonObj = new JsonObject();
            for (int i = 0; i < columnList.size(); i++) {
                jsonObj.addProperty(columnList.get(i), helper.getString(objects[i]));
            }
            jsonArray.add(jsonObj);
        }
        /*JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data",documentService.generatePdf(jsonArray));*/

        return jsonArray;
    }

    private String preparedStatementForInvoiceRecord(String sellerCode, String invNumber, String invYear, String prefix) {
        String sql = "SELECT trim(id.c_c2code) as C2Code,trim(id.c_br_code) as Br, trim(id.c_year) as Yr,trim(id.c_prefix) as Pfx, " +
                "  id.n_srno as InvNo, im.d_date as InvDate,date_format(im.d_date,'%d') as InvDay,date_format(im.d_date, '%m') as InvMonth, " +
                "  date_format(im.d_date, '%Y') as InvYear,trim(im.c_cust_code) as CustCode,'' as CustC2id,im.n_other_charge as OtherChg,im.n_freight as InvFrght,trim(im.c_ship_code) as shipcode, " +
                "  trim(im.c_crnt_prefix) as CrntPfx,im.n_crnt_no as CrntNo,im.n_crnt_amt as CrntAmt,trim(im.c_dbnt_prefix) as DbntPfx,im.n_dbnt_no as DbntNo, " +
                "  im.n_dbnt_amt as DbntAmt,trim(im.c_adv_prefix) as AdvPfx,im.n_adv_no as AdvNo,im.n_adv_amt as AdvAmt,trim(im.c_crnt_repl_prefix) as ReplPfx, " +
                "  im.n_crnt_repl_no as ReplNo,im.n_crnt_repl_amt as ReplAmt,im.n_total as InvAmt,im.n_gst_enabled gstEnabled,im.c_from_gst_no fromGstNo, " +
                "  im.n_from_gst_type fromGstType,im.c_to_gst_no toGstNo,coalesce(im.n_cgst_amt,0) cgstTotal,coalesce(im.n_sgst_amt,0) sgstTotal,coalesce(im.n_igst_amt,0) igstTotal, " +
                "  coalesce(im.n_cess_amt,0) cessAmount,trim(id.c_item_code) as ItemCode,map.c_ucode as ItemC2id ,  " +
                "  REPLACE(REPLACE(REPLACE(REPLACE(itmst.c_name,'#',''), '$', ''), '*', ''), '~','') AS ItemName,  " +
                "  '' as ItemName2 , coalesce(pk.c_name,itmst.c_pack_code) as PackName ,    trim(id.c_batch_no) as BatchNo,id.d_print_exp_dt  as ExpDate,date_format(id.d_print_exp_dt, '%d') as ExpDay ,  " +
                "  date_format(id.d_print_exp_dt, '%m') as ExpMonth , date_format(id.d_print_exp_dt, '%Y') as ExpYear , id.n_qty as InvQty,id.n_sch_qty as InvScQty,id.n_sch_disc as InvScDis,ROUND(((id.n_sch_disc / id.n_qty / id.n_sale_rate) *100),2) as SchPer, " +
                "  id.n_disc_per as InvDisc,id.n_sale_rate as SaleRate,id.n_st_per as VatPer,0 TSPer,id.n_2nd_sale LoclSale,id.n_cst_per as CSTPer,0 as CessPer,0 as VatOnMrp,id.n_vatts_mrp as VatMrp , " +
                "  if(coalesce(id.n_stock_mrp,0)!=0,coalesce(id.n_stock_mrp,0),round(id.n_mrp+(id.n_mrp*((id.n_sgst_per+id.n_cgst_per+id.n_igst_per)/100)),3)) as ItemMRP, " +
                "  id.n_tax_on_sch_qty as TaxOnSch,id.n_dc_srno as DCNo ,0 as Repl , im.n_order_no RefOrdNo,im.d_ref_dt as RefDate, " +
                "  trim(id.c_mf_code) as MfgComp ,replace(replace(mf.c_name,'.',''),',','') as MktgComp , map.n_supp_qb as ConvFact,'' CuItemId ,'' as CuItemCF,0 as lostitem,'' as Rack,c_mf_code as MfCode, " +
                "  0 as MrpIncl,0 CrDays,im.c_sman_code SmanCode,im.c_lr_no LRNo,date_format(im.d_lr_date,'%d') LRDay,date_format(im.d_lr_date,'%m') LRMonth,date_format(im.d_lr_date,'%Y') LRyear,coalesce(id.n_amount,0) itmTotal, " +
                "  id.c_hsn_sac_code hsnCode,coalesce(id.n_cgst_per,0) cgstPer,coalesce(id.n_cgst_amt,0) cgstAmt,coalesce(id.n_sgst_per,0) sgstPer,coalesce(id.n_sgst_amt,0) sgstAmt, " +
                "  coalesce(id.n_igst_per,0) igstPer,coalesce(id.n_igst_amt,0) igstAmt,coalesce(id.n_cess_per,0) cessPer,coalesce(id.n_cess_amt,0) cessAmt,id.d_print_mfg_dt as MfacDate, " +
                "  coalesce(batchkey.c_batch_key,'-') BatchKey,cmst.c_name as Seller,amst.c_name as Buyer,ist.n_sale_rate AS ItemPTR, " +
                "  im.n_tcs_per as TCSPer, " +
                "  im.n_tcs_amt as tcsAmt, " +
                "  im.n_disc_rs as totalDisc," +
                "  cmst.c_add_1 as sellerAdd1,cmst.c_add_2 as sellerAdd2,cmst.c_add_3 as sellerAdd3,cmst.c_city as sellerCity,cmst.c_pin as sellerPin, " +
                "  amst.c_add_1 as buyerAdd1,amst.c_add_2 as buyerAdd2,amst.c_add_3 as buyerAdd3,amst.c_city as buyerCity,amst.c_pin as buyerPin, " +
                "  round(id.n_amount * (1 - id.n_disc_per / 100), 2) as itemTaxableAmt, " +
                "  round(id.n_sale_rate * id.n_qty, 2) as taxItemTotal, " +
                "  round(id.n_amount * id.n_disc_per / 100, 2) as itemDiscAmt, " +
                "  im.n_cases as cases, " +
                "  lid.t_aws_url as sellerLogo, " +
                "  sum(id.n_amount) over() as totalItemAmt, " +
                "  sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 0, round(id.n_amount * (1 - id.n_disc_per / 100), 2), 0)) over() as tax_summ_0_total,  " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 0, id.n_sch_disc, 0)) over() as tax_summ_0_schAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 0, round(id.n_amount * id.n_disc_per / 100, 2), 0)) over() as tax_summ_0_discAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 5, round(id.n_amount * (1 - id.n_disc_per / 100), 2), 0)) over() as tax_summ_5_total,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 5, id.n_sch_disc, 0)) over() as tax_summ_5_schAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 5, round(id.n_amount * id.n_disc_per / 100, 2), 0)) over() as tax_summ_5_discAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 5, coalesce(id.n_sgst_amt,0), 0)) over() as tax_summ_5_sgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 5, coalesce(id.n_cgst_amt,0), 0)) over() as tax_summ_5_cgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 5, coalesce(id.n_igst_amt,0), 0)) over() as tax_summ_5_igstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 12, round(id.n_amount * (1 - id.n_disc_per / 100), 2), 0)) over() as tax_summ_12_total,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 12, id.n_sch_disc, 0)) over() as tax_summ_12_schAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 12, round(id.n_amount * id.n_disc_per / 100, 2), 0)) over() as tax_summ_12_discAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 12, coalesce(id.n_sgst_amt,0), 0)) over() as tax_summ_12_sgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 12, coalesce(id.n_cgst_amt,0), 0)) over() as tax_summ_12_cgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 12, coalesce(id.n_igst_amt,0), 0)) over() as tax_summ_12_igstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 18, round(id.n_amount * (1 - id.n_disc_per / 100), 2), 0)) over() as tax_summ_18_total,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 18, id.n_sch_disc, 0)) over() as tax_summ_18_schAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 18, round(id.n_amount * id.n_disc_per / 100, 2), 0)) over() as tax_summ_18_discAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 18, coalesce(id.n_sgst_amt,0), 0)) over() as tax_summ_18_sgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 18, coalesce(id.n_cgst_amt,0), 0)) over() as tax_summ_18_cgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 18, coalesce(id.n_igst_amt,0), 0)) over() as tax_summ_18_igstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 28, round(id.n_amount * (1 - id.n_disc_per / 100), 2), 0)) over() as tax_summ_28_total,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 28, id.n_sch_disc, 0)) over() as tax_summ_28_schAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 28, round(id.n_amount * id.n_disc_per / 100, 2), 0)) over() as tax_summ_28_discAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 28, coalesce(id.n_sgst_amt,0), 0)) over() as tax_summ_28_sgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 28, coalesce(id.n_cgst_amt,0), 0)) over() as tax_summ_28_cgstAmt,   " +
                "sum(if(id.n_cgst_per + id.n_sgst_per + id.n_igst_per = 28, coalesce(id.n_igst_amt,0), 0)) over() as tax_summ_28_igstAmt,  " +
                " id.n_sch_qty as free, " +
                " co.d_duedate as DueDate, "+
                " com.d_upload_datetime as OrderDate "+
                "  FROM cust_inv_det id join cust_inv_mst im on id.n_srno = im.n_srno and im.c_c2code = id.c_c2code  " +
                "  and im.c_br_code = id.c_br_code and im.c_year = id.c_year and im.c_prefix = id.c_prefix " +
                "  left join lc_c2code_mst cmst on im.c_c2code = cmst.c_code " +
                "  left join lc_images_mst lim  on lim.c_c2code = cmst.c_code and lim.c_type='LOGO' and lim.c_c2code = lim.c_code  " +
                "  left join lc_images_det lid  on lim.n_id = lid.n_mstId and lid.n_status = 1 " +
                "  left join cust_act_mst amst on im.c_c2code = amst.c_c2code and im.c_cust_code = amst.c_code " +
                "  left join u_stockiest_item map on map.c_stockiest_item_code = id.c_item_code and map.c_stockiest_code = '" + sellerCode + "' " +
                "  left join lc_item_mst lcitm on lcitm.c_c2code = id.c_c2code and lcitm.c_item_code = id.c_item_code " +
                "  left join cust_item_stock st on st.c_c2code = im.c_c2code and st.c_br_code = id.c_br_code and id.c_item_code = st.c_item_code and id.c_batch_no = st.c_batch_no " +
                "  left join cust_item_mst itmst on itmst.c_code = id.c_item_code and itmst.c_c2code = im.c_c2code and itmst.c_br_code=id.c_br_code " +
                "  left join cust_pack_mst pk on pk.c_c2code = im.c_c2code and itmst.c_pack_code = pk.c_code " +
                "  left join cust_mfac_mst mf on mf.c_c2code = itmst.c_c2code and mf.c_code = itmst.c_mfac_code " +
                "  left join cust_branch_item_rack ir on ir.c_c2code = id.c_c2code and ir.c_br_code = id.c_br_code and ir.c_item_code = id.c_item_code " +
                "  left join " +
                "      lc_item_batch_ucode batchkey on  " +
                "          batchkey.c_item_ucode = map.c_ucode and  " +
                "          batchkey.c_batch_no = id.c_batch_no and " +
                "          batchkey.n_mrp = id.n_stock_mrp and " +
                "          batchkey.d_exp_date = id.d_print_exp_dt " +
                "  LEFT JOIN " +
                "  cust_branch_item_stock ist ON ist.c_item_code=id.c_item_code  " +
                "      AND ist.c_c2code = id.c_c2code  AND ist.c_br_code=id.c_br_code " +
                " left join cust_outstanding co on co.n_srno = im.n_srno and co.c_c2_code = im.c_c2code and co.c_prefix = im.c_prefix and co.c_year = im.c_year "+
                " left join cust_transaction_mst ctm on ctm.n_srno = im.c_order_id and ctm.c_c2code = im.c_c2code and ctm.c_cust_code = im.c_cust_code " +
                " left join cust_order_mst com on com.n_srno = ctm.n_ord_srno and com.c_c2code = im.c_c2code and com.c_cust_code =im.c_cust_code "+
                "  where im.c_c2code = '" + sellerCode + "' and im.n_srno = '" + invNumber + "' and im.c_year = '" + invYear + "' ";
        if (!helper.isEmpty(prefix) || !Objects.equals(prefix, "")) {
            sql += "   and im.c_prefix = '" + prefix + "' " ;
        }
        sql += "  order by  ir.c_rack_code asc,mf.c_name desc ,ItemName asc  ";
        return sql;
    }

    private String preparedStatementForInvoiceListCount() {
        return "select count(lc.c_code) from cust_inv_mst inv left join lc_c2code_mst lc on inv.c_c2code=lc.c_code  " +
                "   where inv.c_c2code=:sellerCode and inv.c_cust_code=:buyerCode and inv.d_date >= :fromDate and inv.d_date <= current_date() ";
    }

    private String getStringArray(JsonArray array) {
        if (array == null)
            return null;

        String arr = "'";
        for (JsonElement json : array) {
            arr += json.getAsString() + "','";
        }
        return arr.substring(0, arr.length() - 2);
    }
}
