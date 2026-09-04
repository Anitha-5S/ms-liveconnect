package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.ms.master.bos.OnePharmaBo;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntity;
import com.c2.lc.ms.master.entities.mysql.OnePharmaEmailsEntityPK;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;

import java.io.IOException;

public interface OnePharmaService extends MasterBaseService {
    void insert(OnePharmaEmailsEntity onePharma) throws DuplicateRecordException;

    OnePharmaEmailsEntity getByEmailId(String emailId) throws RecordNotFoundException;

    void deleteById(OnePharmaEmailsEntityPK onePharmaPK);

    JsonObject getLatestItemCode(String c2code, JsonObject data);

    String getBlockListedEmail(JsonObject data);

    JsonArray getInvoiceRecord(OnePharmaBo onePharmaBo) throws RecordNotFoundException, DocumentException, IOException;

    long getInvoiceListCount(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray getInvoiceList(LcHeaderBO lcHeaderBO, OnePharmaBo onePharmaBo, SearchBO searchBO) throws RecordNotFoundException, CommunicationErrorException, InvalidRequestException;

    JsonArray getItems(OnePharmaBo onePharmaBo);
}