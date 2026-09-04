package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.master.bos.OnePharmaBo;
import com.c2.lc.ms.master.services.interfaces.DocumentService;
import com.c2.lc.ms.master.services.interfaces.OnePharmaService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.OnePharmaTransaction;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntity;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntityPK;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.*;
//import org.apache.commons.collections4.CollectionUtils;
import com.itextpdf.text.DocumentException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Component
public class OnePharmaTransactionImpl extends MasterBaseTransactionImpl implements OnePharmaTransaction {

    @Autowired
    OnePharmaService onePharmaService;

    @Autowired private DocumentService documentService;

    @Override
    public void insert(OnePharmaEmailsEntity onePharma) throws DuplicateRecordException {
        onePharmaService.insert(onePharma);
    }

    @Override
    public String getByEmailId(String emailId) throws RecordNotFoundException {
        OnePharmaEmailsEntity onePharma = onePharmaService.getByEmailId(emailId);
        return onePharma.getcC2Code();
    }

    @Override
    public void deleteByEmailId(String emailId) throws RecordNotFoundException {
        OnePharmaEmailsEntity onePharma = onePharmaService.getByEmailId(emailId);
        OnePharmaEmailsEntityPK onePharmaPK = new OnePharmaEmailsEntityPK(onePharma.getcC2Code(), onePharma.getcEmail());
        onePharmaService.deleteById(onePharmaPK);
    }

    @Override
    public JsonObject getLatestItemCodeBasedOnDescription(JsonObject requestBody) throws InvalidRequestException {

        JsonObject response;
        String c2code = requestBody.get("c_c2code").getAsString();
        JsonArray description = requestBody.get("c_description").getAsJsonArray();

        if (helper.isEmpty(c2code) || helper.isEmpty(description)) {
            throw new InvalidRequestException("response", "Invalid requestBody");
        }

        response = onePharmaService.getLatestItemCode(c2code, requestBody);

        int mappedItemsCount = response.get("n_mapped_items_count").getAsInt();
        int totalItemCount = response.get("n_item_count").getAsInt();
        JsonArray itemDescription = response.get("c_mapped_items").getAsJsonArray();
        JsonArray mappedItem = new JsonArray();

        for (JsonElement element : itemDescription) {
            mappedItem.add((helper.getNotJsonNullString(element.getAsJsonObject().get("c_name"))));
        }

        List<String> difference = new ArrayList(CollectionUtils.subtract(description, mappedItem));
        JsonArray unMappedItems = new Gson().toJsonTree(difference).getAsJsonArray();

        if (totalItemCount == mappedItemsCount) {
            response.addProperty("c_response", "true");
        } else {
            response.addProperty("c_response", "false");
        }
        response.add("c_unmapped_items", unMappedItems);

        return response;
    }

    @Override
    public JsonObject getBlockListedEmail(JsonObject requestBody) {
        JsonObject response = new JsonObject();

        String emailStatus = onePharmaService.getBlockListedEmail(requestBody);
        response.addProperty("emailStatus", emailStatus);
        return response;
    }

    @Override
    public JsonArray getInvoiceList(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
        return onePharmaService.getInvoiceList(lcHeaderBO,onePharmaBo,searchBO);
    }

    @Override
    public long getInvoiceListCount(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException {
       return onePharmaService.getInvoiceListCount(lcHeaderBO,onePharmaBo,searchBO);
    }

    @Override
    public JsonObject getInvoiceRecord(OnePharmaBo onePharmaBo) throws RecordNotFoundException, DocumentException, IOException {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("data",documentService.generatePdf(onePharmaService.getInvoiceRecord(onePharmaBo)));
        return jsonObject;
    }

    @Override
    public JsonArray getItemDetails(OnePharmaBo onePharmaBo) {
        return onePharmaService.getItems(onePharmaBo);
    }

    @Override
    public JsonObject getInvoiceExcel(OnePharmaBo onePharmaBo) throws RecordNotFoundException, DocumentException, IOException, InvalidRequestException, ParseException {
        JsonObject jsonObject = new JsonObject();
        if(onePharmaBo.getExcelFileType() == 1 ) {
            jsonObject.addProperty("data", documentService.generateExcel(onePharmaService.getInvoiceRecord(onePharmaBo), onePharmaBo.getTableHeaders()));
        }
        else if(onePharmaBo.getExcelFileType() == 2){
            jsonObject.addProperty("data", documentService.generateExcel2(onePharmaService.getInvoiceRecord(onePharmaBo), onePharmaBo.getTableHeaders()));
        }
        else if(onePharmaBo.getExcelFileType() == 3){
            jsonObject.addProperty("data", documentService.generateExcel3(onePharmaService.getInvoiceRecord(onePharmaBo), onePharmaBo.getTableHeaders()));
        }
        else
            throw new InvalidRequestException("'n_fileType', File type is missing", Messages.INVALID_REQUEST);
        return jsonObject;
    }

}
