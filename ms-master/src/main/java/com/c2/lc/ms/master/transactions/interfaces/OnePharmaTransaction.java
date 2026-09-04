package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.ms.master.bos.OnePharmaBo;
import com.c2.lc.ms.master.transactions.interfaces.base.MasterBaseTransaction;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntity;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.text.ParseException;

public interface OnePharmaTransaction extends MasterBaseTransaction {
    void insert(OnePharmaEmailsEntity onePharma) throws RecordNotFoundException, DuplicateRecordException;

    String getByEmailId(String emailId) throws RecordNotFoundException;

    void deleteByEmailId(String emailId) throws RecordNotFoundException;

    JsonObject getLatestItemCodeBasedOnDescription(JsonObject requestBody) throws InvalidRequestException;

    JsonObject getBlockListedEmail(JsonObject requestBody);

    JsonArray getInvoiceList(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    long getInvoiceListCount(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonObject getInvoiceRecord(OnePharmaBo onePharmaBo) throws RecordNotFoundException, DocumentException, IOException;

    JsonArray getItemDetails(OnePharmaBo onePharmaBo);

    JsonObject getInvoiceExcel(OnePharmaBo onePharmaBo) throws RecordNotFoundException, DocumentException, IOException, InvalidRequestException, ParseException;

}
