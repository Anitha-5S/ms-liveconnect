package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

public interface DocumentService extends MasterBaseService {

    String generatePdf(JsonArray jsonArray) throws RecordNotFoundException, IOException, DocumentException;

    String generateExcel(JsonArray jsonArray, String headers) throws RecordNotFoundException, IOException, DocumentException, ParseException;

    String generateExcel2(JsonArray invoiceRecord, String headers) throws IOException, RecordNotFoundException, ParseException;

    String generateExcel3(JsonArray invoiceRecord, String headers) throws IOException, RecordNotFoundException, ParseException;

    String generateExpiryReturn(JsonObject object, LcHeaderBO lcHeaderBO) throws IOException, RecordNotFoundException, DocumentException;

}
