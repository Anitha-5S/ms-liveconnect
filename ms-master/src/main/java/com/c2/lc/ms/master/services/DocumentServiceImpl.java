package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.DocumentService;
import com.c2.lc.ms.master.services.interfaces.OnePharmaService;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class DocumentServiceImpl extends MasterBaseServiceImpl implements DocumentService {

    @Autowired
    private
    OnePharmaService onePharmaService;

    static Font large = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.BOLD);

    @Override
    public String generatePdf(JsonArray jsonArray) throws RecordNotFoundException, IOException, DocumentException {
        //JsonArray json = onePharmaService.getInvoiceRecord(onePharmaBo);
        if(jsonArray.size() == 0){
            throw new RecordNotFoundException("Record not Found");
        }
        Document pdf = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(pdf, new FileOutputStream("invoice.pdf"));
        pdf.open();
        PdfPTable pdfPTable = new PdfPTable(19);
        pdfPTable.setWidths(new int[]{1,2,2,4,2,4,3,3,3,2,2,2,2,2,2,2,2,2,2});
        PdfPTable headPdf = new PdfPTable(3);
        PdfPTable botTable = new PdfPTable(2);
        botTable.setWidths(new int[]{80, 20});
        headPdf.setWidths(new int[]{85, 135, 75});
        headPdf.setSpacingAfter(20f);
        topTable(headPdf, jsonArray);
        pdf.add(headPdf);
        tableHeader(pdfPTable);
        getRowData(pdfPTable, jsonArray);
        pdfPTable.setSpacingAfter(40f);

        //topTable();
        pdf.add(pdfPTable);
        botTable(botTable, jsonArray);
        pdf.add(botTable);
        pdf.close();
        byte[] fileContent = FileUtils.readFileToByteArray(new File("invoice.pdf"));

        return Base64.getEncoder().encodeToString(fileContent);
    }

    @Override
    public String generateExcel(JsonArray jsonArray, String headers) throws RecordNotFoundException, IOException, DocumentException, ParseException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        if(jsonArray.size() == 0){
            throw new RecordNotFoundException("Record not found");
        }
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
        XSSFSheet sheet = workbook.createSheet(jsonObject.get("InvNo").getAsString());

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Calibri");
        //font.setItalic(true);
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        List<String> jsonKey = Lists.newArrayList("C2Code" , "Br" , "Yr" , "Pfx" , "InvNo" , "InvDate" , "InvDay" , "InvMonth" , "InvYear" , "CustCode"
                , "CustC2id" , "OtherChg" , "InvFrght" , "shipcode" , "CrntPfx" , "CrntNo" , "CrntAmt" , "DbntPfx" , "DbntNo" , "DbntAmt"
                , "AdvPfx" , "AdvNo" , "AdvAmt" , "ReplPfx" , "ReplNo" , "ReplAmt" , "InvAmt" , "gstEnabled" , "fromGstNo" , "fromGstType"
                , "toGstNo" , "cgstTotal" , "sgstTotal" , "igstTotal" , "cessAmount" , "ItemCode" , "ItemC2id"
                , "ItemName" , "ItemName2" , "PackName" , "BatchNo" , "ExpDate" , "ExpDay" , "ExpMonth" , "ExpYear" , "InvQty" , "InvScQty"
                , "InvScDis" , "SchPer" , "InvDisc" , "SaleRate" , "VatPer" , "TSPer" , "LoclSale" , "CSTPer" , "CessPer" , "VatOnMrp"
                , "VatMrp" , "ItemMRP" , "TaxOnSch" , "DCNo" , "Repl" , "RefOrdNo" , "RefDate" , "MfgComp" , "MktgComp" , "ConvFact"
                , "CuItemId" , "CuItemCF" , "lostitem" , "Rack" , "MfCode" , "MrpIncl" , "CrDays" , "SmanCode" , "LRNo"
                , "LRDay" , "LRMonth" , "LRyear" , "itmTotal" , "hsnCode" , "cgstPer" , "cgstAmt" , "sgstPer" , "sgstAmt" , "igstPer"
                , "igstAmt" , "cessPer" , "cessAmt" , "MfacDate" , "BatchKey" , "Seller" , "Buyer" , "ItemPTR" , "TCSPer" , "TCSAmt");
        if (headers.equals(Constants.STATUS_YES)) {
            Row header = sheet.createRow(0);
            List<String> keys1 = jsonKey;
            int headCell = 0;
            for (String entry : keys1) {
                Cell cell = header.createCell(headCell++);
                cell.setCellStyle(style);
                cell.setCellValue(entry);
            }
        }
        int rowCount = 1;
        for (int i = 0; i < jsonArray.size(); ++i) {
            Row row = sheet.createRow(rowCount++);
            int cell = 0;
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
           /* for (String entry : jsonKey) {
                row.createCell(cell++).setCellValue(jsonObject1.get(entry).getAsString());
            }*/
            row.createCell(cell++).setCellValue(jsonObject1.get("C2Code").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("Br").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("Yr").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("Pfx").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvNo").getAsLong());
            row.createCell(cell++).setCellValue(dateFormatChange(jsonObject1.get("InvDate").getAsString()));//date format
            row.createCell(cell++).setCellValue(jsonObject1.get("InvDay").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvMonth").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvYear").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("CustCode").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("CustC2id").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("OtherChg").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvFrght").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("shipcode").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("CrntPfx").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("CrntNo").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("CrntAmt").getAsDouble());//amt_col
            row.createCell(cell++).setCellValue(jsonObject1.get("DbntPfx").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("DbntNo").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("DbntAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("AdvPfx").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("AdvNo").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("AdvAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("ReplPfx").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("ReplNo").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("ReplAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("gstEnabled").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("fromGstNo").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("fromGstType").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("toGstNo").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("cgstTotal").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("sgstTotal").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("igstTotal").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("cessAmount").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemCode").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemC2id").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemName").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemName2").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("PackName").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("BatchNo").getAsString());
            row.createCell(cell++).setCellValue(dateFormatChange(jsonObject1.get("ExpDate").getAsString()));
            row.createCell(cell++).setCellValue(jsonObject1.get("ExpDay").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("ExpMonth").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("ExpYear").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvQty").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScQty").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScDis").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("SchPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvDisc").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("SaleRate").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("VatPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("TSPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("LoclSale").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("CSTPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("CessPer").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("VatOnMrp").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("VatMrp").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemMRP").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("TaxOnSch").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("DCNo").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("Repl").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("RefOrdNo").getAsString());
            row.createCell(cell++).setCellValue(dateFormatChange(jsonObject1.get("RefDate").getAsString()));
            row.createCell(cell++).setCellValue(jsonObject1.get("MfgComp").getAsString()); //LC it's null
            row.createCell(cell++).setCellValue(jsonObject1.get("MktgComp").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("ConvFact").getAsString());//need to chk
            row.createCell(cell++).setCellValue(jsonObject1.get("CuItemId").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("CuItemCF").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("lostitem").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("Rack").getAsString());//need to chk
            row.createCell(cell++).setCellValue(jsonObject1.get("MfCode").getAsLong());//need to chk
            row.createCell(cell++).setCellValue(jsonObject1.get("MrpIncl").getAsLong());//need to chk
            row.createCell(cell++).setCellValue(jsonObject1.get("CrDays").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("SmanCode").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("LRNo").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("LRDay").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("LRMonth").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("LRyear").getAsInt());
            row.createCell(cell++).setCellValue(jsonObject1.get("itmTotal").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("hsnCode").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("cgstPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("cgstAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("sgstPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("sgstAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("igstPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("igstAmt").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("cessPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("cessAmt").getAsDouble()) ;
            row.createCell(cell++).setCellValue((jsonObject1.get("MfacDate").getAsString().equals("") ? "0000-00-00" : dateFormatChange(jsonObject1.get("MfacDate").getAsString())));
            row.createCell(cell++).setCellValue(jsonObject1.get("BatchKey").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("Seller").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("Buyer").getAsString());
            if(jsonObject1.get("ItemPTR").getAsString().equals(""))
                row.createCell(cell++).setCellValue("");
            else
                row.createCell(cell++).setCellValue(jsonObject1.get("ItemPTR").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("TCSPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("TCSAmt").getAsDouble());
        }

        FileOutputStream fileOut = new FileOutputStream("test.xlsx");
        workbook.write(fileOut);
        byte[] fileContent = FileUtils.readFileToByteArray(new File("test.xlsx"));

        return Base64.getEncoder().encodeToString(fileContent);
    }

    private String dateFormatChange(String invDate) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
        Date date = format1.parse(invDate);
        String date1 = format2.format(date);
        //Date changedDate = format2.parse(date1);
        return date1;
    }

    private long raipurDateFormatChange(String invDate) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("ddMMyyyy");
        Date date = format1.parse(invDate);
        long date1 = Long.parseLong(format2.format(date));
        //Date changedDate = format2.parse(date1);
        return date1;
    }


    @Override
    public String generateExcel2(JsonArray invoiceRecord, String headers) throws IOException, RecordNotFoundException, ParseException {
        if(invoiceRecord.size() == 0){
            throw new RecordNotFoundException("Record not found");
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        JsonObject jsonObject = invoiceRecord.get(0).getAsJsonObject();
        XSSFSheet sheet = workbook.createSheet(jsonObject.get("Seller").getAsString());

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Calibri");
        //font.setItalic(true);
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        int value = 1;
        int rowNo = 0;
        for (int i = 1; i < invoiceRecord.size(); i++) {
            Row row = sheet.createRow(rowNo);
            int cell = 0;
            JsonObject jsonObject1 = invoiceRecord.get(i).getAsJsonObject();
            String invoiceNo = jsonObject1.get("InvNo").getAsString();
            invoiceNo = invoiceNo.substring(invoiceNo.length() - 5);
            row.createCell(cell++).setCellValue("H");
            row.createCell(cell++).setCellValue(value);
            row.createCell(cell++).setCellValue(helper.getLong(invoiceNo));
            row.createCell(cell++).setCellValue(raipurHeaderDate(jsonObject1.get("InvDate").getAsString()));
            row.createCell(cell++).setCellValue(jsonObject1.get("CustCode").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("Buyer").getAsString());
            row.createCell(cell++).setCellValue("RAIPUR (C.G.)");
        }

        List<String> jsonKey = Lists.newArrayList("", "Mfg Code", "Mfg Name", "Item Code", "HSN Code", "Item Name", "Packing",
                "Total Credit Note Amount", "BatchNo", "ExpDate", "", "", "Tax%", "Sale Rate Per Unit", "", "", "Mrp", "", "", "",
                "Qty", "Free Qty", "Dis%", "Discount Amt", "Sch Dis%", "Sch Dis Amount", "");//keys
        int temp = 0;
        if (headers.equals(Constants.STATUS_YES)) {
            temp = 1;
            Row header1 = sheet.createRow(1);
            List<String> keys1 = jsonKey;
            int headCell1 = 0;
            for (String entry : keys1) {
                Cell cell = header1.createCell(headCell1++);
                cell.setCellStyle(style);
                cell.setCellValue(entry);
            }
        }
        int rowNo1 = 2;
        for (int i = 0; i < invoiceRecord.size(); i++) {
            Row row = sheet.createRow(rowNo1++);
            int cell = 0;
            JsonObject jsonObject1 = invoiceRecord.get(i).getAsJsonObject();
            row.createCell(cell++).setCellValue("T");
            row.createCell(cell++).setCellValue(jsonObject1.get("MfCode").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("MfgComp").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemCode").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("hsnCode").getAsLong());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemName").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("PackName").getAsString());
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("BatchNo").getAsString());
            row.createCell(cell++).setCellValue(raipurDateFormatChange(jsonObject1.get("ExpDate").getAsString()));//chk
            row.createCell(cell++).setCellValue(1);
            row.createCell(cell++).setCellValue(0);
            double tax = jsonObject.get("cgstPer").getAsDouble() +
                    jsonObject.get("igstPer").getAsDouble() + jsonObject.get("sgstPer").getAsDouble();
            row.createCell(cell++).setCellValue(tax);
            row.createCell(cell++).setCellValue(jsonObject1.get("SaleRate").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("SaleRate").getAsDouble());
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemMRP").getAsDouble());
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("InvQty").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScQty").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("VatPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvDisc").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("SchPer").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScDis").getAsDouble());
        }
        int rowNo2 = 1 + temp + invoiceRecord.size();
            Row row = sheet.createRow(rowNo2++);
            int cell = 0;
        JsonObject jsonObject1 = invoiceRecord.get(0).getAsJsonObject();
        double invTotal = 0.00;
        double totSchDisc = 0.00;
        for (int i = 0; i < invoiceRecord.size(); i++) {
            JsonObject jsonObject2 = invoiceRecord.get(i).getAsJsonObject();
                invTotal  += jsonObject2.get("itmTotal").getAsDouble();
            }
            totSchDisc += jsonObject1.get("cgstTotal").getAsDouble() + jsonObject1.get("sgstTotal").getAsDouble() + jsonObject1.get("igstTotal").getAsDouble();
            row.createCell(cell++).setCellValue("F");
            row.createCell(cell++).setCellValue(invTotal);
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScDis").getAsDouble());
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(totSchDisc);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(totSchDisc);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("InvAmt").getAsDouble());
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");

    FileOutputStream fileOut = new FileOutputStream("testR.xlsx");
        workbook.write(fileOut);
    byte[] fileContent = FileUtils.readFileToByteArray(new File("testR.xlsx"));

        return Base64.getEncoder().encodeToString(fileContent);
    }

    private String raipurHeaderDate(String invDate) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy");
        Date date = format1.parse(invDate);
        String date1 = format2.format(date);
        //Date changedDate = format2.parse(date1);
        return date1;
    }

    @Override
    public String generateExcel3(JsonArray invoiceRecord, String headers) throws IOException, RecordNotFoundException, ParseException {
        if(invoiceRecord.size() == 0){
            throw new RecordNotFoundException("Record not found");
        }
        XSSFWorkbook workbook = new XSSFWorkbook();
        JsonObject jsonObject = invoiceRecord.get(0).getAsJsonObject();
        XSSFSheet sheet = workbook.createSheet(jsonObject.get("Seller").getAsString());

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setFontName("Calibri");
        //font.setItalic(true);
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        int value = 1;
        int rowNo = 0;
        for (int i = 1; i < invoiceRecord.size(); i++) {
            Row row = sheet.createRow(rowNo);
            int cell = 0;
            JsonObject jsonObject1 = invoiceRecord.get(i).getAsJsonObject();
            row.createCell(cell++).setCellValue("H");
            row.createCell(cell++).setCellValue(value);
            row.createCell(cell++).setCellValue(jsonObject1.get("InvNo").getAsLong());
            row.createCell(cell++).setCellValue(swilDateFormat(jsonObject1.get("InvDate").getAsString()));
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(swilDateFormat(jsonObject1.get("InvDate").getAsString()));
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue("Direct");
            row.createCell(cell++).setCellValue(1);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue("JAIPUR");
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
        }
        List<String> jsonKey = Lists.newArrayList("", "Cust_code", "Cust_name", "Item-code"," ", "Item_name", "pack",
                "Mfac name", "Batchno", "Expiry date", "", "", "Tax", "sale rate", "sale rate", "0", "Mrp", "", "", "",
                "Qty", "Sch_qty", "Discount","", "HSN Code", "Sch_Dis%", "Sch_DisAmt", "");//keys
        int temp = 0;
        if (headers.equals(Constants.STATUS_YES)) {
            temp = 1;
            Row header1 = sheet.createRow(1);
            List<String> keys1 = jsonKey;
            int headCell1 = 0;
            for (String entry : keys1) {
                Cell cell = header1.createCell(headCell1++);
                cell.setCellStyle(style);
                cell.setCellValue(entry);
            }
        }
        int rowNo1 = 2;
        for (int i = 0; i < invoiceRecord.size(); i++) {
            Row row = sheet.createRow(rowNo1++);
            int cell = 0;
            JsonObject jsonObject1 = invoiceRecord.get(i).getAsJsonObject();
            row.createCell(cell++).setCellValue("T");
            row.createCell(cell++).setCellValue(jsonObject1.get("CustCode").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("Buyer").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemCode").getAsLong());
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemName").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("PackName").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("MktgComp").getAsString());
            row.createCell(cell++).setCellValue(jsonObject1.get("BatchNo").getAsString());
            row.createCell(cell++).setCellValue(swilDateFormat(jsonObject1.get("ExpDate").getAsString()));
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue("");
            double tax = 0;
            if(jsonObject.get("cgstPer").getAsDouble() > 0 &&  jsonObject.get("sgstPer").getAsDouble() > 0){
                tax = jsonObject.get("cgstPer").getAsDouble();
            }else if( jsonObject.get("igstPer").getAsDouble() > 0){
                tax =  jsonObject.get("igstPer").getAsDouble();
            }else{
                tax = 0.00;
            }
            row.createCell(cell++).setCellValue(tax);
            row.createCell(cell++).setCellValue(jsonObject1.get("SaleRate").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("SaleRate").getAsDouble());
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(jsonObject1.get("ItemMRP").getAsDouble());
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(0);
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("InvQty").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScQty").getAsDouble());
            row.createCell(cell++).setCellValue(jsonObject1.get("InvDisc").getAsDouble());
            row.createCell(cell++).setCellValue(" ");
            row.createCell(cell++).setCellValue(jsonObject1.get("hsnCode").getAsLong());
            row.createCell(cell++).setCellValue(0.0);
            row.createCell(cell++).setCellValue(jsonObject1.get("InvScDis").getAsDouble());
        }

        int rowNo2 = 1 + temp + invoiceRecord.size();
        Row row = sheet.createRow(rowNo2++);
        int cell = 0;
        JsonObject jsonObject1 = invoiceRecord.get(0).getAsJsonObject();
        row.createCell(cell++).setCellValue("F");
        row.createCell(cell++).setCellValue(1);
        double secondSaleRate = 0.00;
        for (int i = 0; i < invoiceRecord.size(); i++) {
                JsonObject jsonObject2 = invoiceRecord.get(i).getAsJsonObject();
            secondSaleRate  += jsonObject2.get("SaleRate").getAsDouble() * jsonObject2.get("InvQty").getAsDouble();
        }
        row.createCell(cell++).setCellValue(secondSaleRate);
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(" ");
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(0);
        row.createCell(cell++).setCellValue(jsonObject1.get("InvAmt").getAsDouble());

        FileOutputStream fileOut = new FileOutputStream("testS.xlsx");
        workbook.write(fileOut);
        byte[] fileContent = FileUtils.readFileToByteArray(new File("testS.xlsx"));

        return Base64.getEncoder().encodeToString(fileContent);
    }

    private long swilDateFormat(String invDate) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("ddMMyyyy");
        Date date = format1.parse(invDate);
        long date1 = Long.parseLong(format2.format(date));
        //Date changedDate = format2.parse(date1);
        return date1;
    }

    @Override
    public String generateExpiryReturn(JsonObject object, LcHeaderBO lcHeaderBO) throws IOException, RecordNotFoundException, DocumentException {

        Document pdf = new Document();
        PdfWriter.getInstance(pdf, new FileOutputStream("expiry.pdf"));
        pdf.open();
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        Font expFont = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.BOLD);

        PdfPTable pdfPTable = new PdfPTable(10);
        pdfPTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        pdfPTable.setWidths(new int[]{2,9,2,2,3,2,3,3,2,5});
        expiryHeader(pdfPTable);
        getEspiryEmpty(pdfPTable);

        PdfPTable title = new PdfPTable(1);
        Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                Font.BOLD);
        title.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        title.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        title.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        title.addCell(new Phrase("EXPIRY/BREAKAGE/DAMAGE GOODS RETURN ", fTitle));
        table.addCell(title);

        PdfPTable info = new PdfPTable(1);
        Font cont = new Font(Font.FontFamily.TIMES_ROMAN, 9,
                Font.BOLD);
        info.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        info.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        info.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        info.addCell(new Phrase("This document contains details of expiry goods sent for " +
                "reimbursement arrangement as per the",cont ));
        info.addCell(new Phrase("provisions of Rule 65(17) of Drugs & " +
                "Cosmetics Rule 1945 and NOTE A SALE INVOICE", cont));
        //table.addCell(info);



        PdfPTable info1 = new PdfPTable(1);
        info1.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        info1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        info1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        info1.addCell(new Phrase("Doc. No. :                                             " +
                                        "                                                       Salesman Code :", expFont));
        info1.addCell(new Phrase("Doc. Date :                                            " +
                                        "                                                      Salesman Name :", expFont));
       /* PdfPCell info1Cell = new PdfPCell();
        info1Cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        info1Cell.setVerticalAlignment(Element.ALIGN_CENTER);
        info1Cell.setBorder(Rectangle.BOTTOM);*/
        info1.addCell(new Phrase("                                                         " +
                                        "                                                            Mobile No. :", expFont));
     //   info1.addCell(info1Cell);

        PdfPTable merge = new PdfPTable(1);
        merge.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        merge.addCell(info);
        merge.addCell(info1);
        table.addCell(merge);

        PdfPTable buySel = new PdfPTable(2);
        buySel.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        buySel.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        buySel.getDefaultCell().setBorder(Rectangle.LEFT);
        buySel.addCell(new Phrase("FROM",expFont));
        buySel.addCell(new Phrase("To", expFont));
        buySel.addCell(new Phrase(object.get("buyerName").getAsString(), expFont));
        buySel.addCell(new Phrase(object.get("sellerName").getAsString(), expFont));
        buySel.addCell(new Phrase(object.get("buyerAddress").getAsString(), expFont));
        buySel.addCell(new Phrase(object.get("sellerAddress").getAsString(), expFont));
        buySel.addCell(new Phrase("   ", expFont));
        buySel.addCell(new Phrase("   ", expFont));
        buySel.addCell(new Phrase("Drug License No. :"+object.get("buyerDlNo").getAsString(), expFont));
        buySel.addCell(new Phrase("Drug License No. :"+object.get("sellerDlNo").getAsString(), expFont));
        buySel.addCell(new Phrase("GST No. : "+object.get("buyerGst").getAsString(), expFont));
        buySel.addCell(new Phrase("GST No. : "+object.get("sellerGst").getAsString(), expFont));
        table.addCell(buySel);

        table.addCell(pdfPTable);
        //table.addCell(totCell);

        PdfPTable bottom = new PdfPTable(1);
        bottom.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        formBottom(bottom, expFont);
        table.addCell(bottom);
        pdf.add(table);
        pdf.close();
        byte[] fileContent = FileUtils.readFileToByteArray(new File("expiry.pdf"));
        return Base64.getEncoder().encodeToString(fileContent);
    }

    private void getRowData(PdfPTable table, JsonArray json){

        for (int i = 0; i<json.size(); i++) {
            JsonObject object = json.get(i).getAsJsonObject();
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOTTOM);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                    Font.NORMAL);

            table.addCell(new Phrase(String.valueOf(i+1),normal));
            table.addCell(new Phrase(String.valueOf((int) object.get("InvQty").getAsDouble()),normal));
            table.addCell(new Phrase(String.valueOf((int) object.get("free").getAsDouble()),normal));
            table.addCell(new Phrase(object.get("MktgComp").getAsString(),normal));
            table.addCell(new Phrase(object.get("PackName").getAsString(),normal));
            table.addCell(new Phrase(object.get("ItemName").getAsString(),normal));
            table.addCell(new Phrase(object.get("BatchNo").getAsString(),normal));
            table.addCell(new Phrase(object.get("ExpDate").getAsString(),normal));
            table.addCell(new Phrase(object.get("hsnCode").getAsString(),normal));
            table.addCell(new Phrase((String.format("%.2f",object.get("ItemMRP").getAsDouble())),normal));
            table.addCell(new Phrase((String.format("%.2f",object.get("SaleRate").getAsDouble())),normal));
            table.addCell(new Phrase(object.get("InvDisc").getAsString(),normal));
            table.addCell(new Phrase(object.get("sgstPer").getAsString(),normal));
            table.addCell(new Phrase((String.format("%.2f",object.get("sgstAmt").getAsDouble())),normal));
            table.addCell(new Phrase(object.get("cgstPer").getAsString(),normal));
            table.addCell(new Phrase((String.format("%.2f",object.get("cgstAmt").getAsDouble())),normal));
            table.addCell(new Phrase(object.get("igstPer").getAsString(),normal));
            table.addCell(new Phrase((String.format("%.2f",object.get("igstAmt").getAsDouble())),normal));
            table.addCell(new Phrase((String.format("%.2f",object.get("itmTotal").getAsDouble())),normal));

        }
    }

    private void tableHeader(PdfPTable table){
        Stream.of("SI", "Qty", "Free", "Mfr","Pack","Product Name","Batch", "Exp", "HSN",
                        "M.R.P", "Rate", "Dis", "SGST", "Value", "CGST", "Value", "IGST",
                        "Value",  "Amount")
                .forEach(columnTitle -> {
                    Font normal = large;
                    PdfPCell header = new PdfPCell();
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle,normal));
                    table.addCell(header);
                });
    }
    private void topTable(PdfPTable table, JsonArray jsonArray) throws DocumentException, IOException {

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.setWidthPercentage(100);
        JsonObject object = jsonArray.get(0).getAsJsonObject();
        PdfPTable ft = new PdfPTable(2);
        ft.setWidths(new int[]{50,100});
        // seller info
        Font head = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.BOLD);
        Font data = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                Font.NORMAL);
        if (!helper.isEmpty(object.get("sellerLogo").getAsString())) {
            Image img = Image.getInstance(object.get("sellerLogo").getAsString());
            img.setAlignment(Image.ALIGN_LEFT);
            img.setBorder(Rectangle.NO_BORDER);
            ft.addCell(img);
        }
        else
            ft.addCell(new Phrase(""));
        PdfPCell address = new PdfPCell();
        address.setBorder(Rectangle.NO_BORDER);
        address.addElement(new Phrase(object.get("Seller").getAsString(),head));
        address.addElement(new Phrase(object.get("sellerAdd1").getAsString(),data));
        address.addElement(new Phrase(object.get("sellerAdd2").getAsString(),data));
        address.addElement(new Phrase(object.get("sellerAdd3").getAsString(),data));
        address.addElement(new Phrase(object.get("sellerCity").getAsString()+","+object.get("sellerPin").getAsString(),data));
        address.addElement(new Phrase("PHONE: "+object.get("Seller").getAsString(),data));
        address.addElement(new Phrase("GSTIN: "+object.get("fromGstNo").getAsString(),data));
        address.setRowspan(7);
        ft.addCell(address);
        table.addCell(ft);

        //invoice info

        PdfPTable fRow = new PdfPTable(4);
        fRow.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        fRow.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        fRow.getDefaultCell().setBorder(Rectangle.BOX);
        fRow.addCell(new Phrase("Invoice No:", data));
        fRow.addCell(new Phrase(object.get("InvNo").getAsString(),data));
        PdfPCell tem = new PdfPCell();
        tem.addElement(new Phrase("Order No: "+object.get("RefOrdNo").getAsString(),data));
        tem.addElement(new Phrase("Order Date: "+object.get("OrderDate").getAsString().substring(0,10),data));
        tem.setRowspan(2);
        fRow.addCell(tem);
        fRow.addCell(new Phrase("Cases: "+object.get("cases").getAsString(),data));
        PdfPTable sRow = new PdfPTable(4);
        PdfPCell tem1 = new PdfPCell();
        tem1.addElement(new Phrase("Invoice Date:",data));
        tem1.addElement(new Phrase("Due Date: ", data));
        tem1.setRowspan(2);
        sRow.addCell(tem1);
        PdfPCell tem2 = new PdfPCell();
        tem2.addElement(new Phrase(object.get("InvDate").getAsString(),data));
        tem2.addElement(new Phrase(object.get("DueDate").getAsString().substring(0,10),data));
        tem2.setRowspan(2);
        sRow.addCell(tem2);
        PdfPCell tem3 = new PdfPCell();
        tem3.addElement(new Phrase("LR No:",data));
        tem3.addElement(new Phrase("LR Date: ", data));
        tem3.setRowspan(2);
        sRow.addCell(tem3);
        sRow.addCell(new Phrase("Transport: ", data));
        PdfPTable sCol = new PdfPTable(1);
        sCol.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        sCol.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        sCol.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        sCol.addCell(new Phrase("INVOICE COPY",head));
        sCol.addCell(new Phrase("Credit", data));
        sCol.addCell(fRow);
        sCol.addCell(sRow);
        table.addCell(sCol);

        // buyer info

        PdfPTable buyAdd = new PdfPTable(1);
        buyAdd.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        buyAdd.addCell(new Phrase("Party Name:",data));
        buyAdd.addCell(new Phrase(object.get("Buyer").getAsString(),head));
        buyAdd.addCell(new Phrase(object.get("buyerAdd1").getAsString(),data));
        buyAdd.addCell(new Phrase(object.get("buyerAdd2").getAsString(),data));
        buyAdd.addCell(new Phrase(object.get("buyerAdd3").getAsString(),data));
        buyAdd.addCell(new Phrase(object.get("buyerCity").getAsString()+","+object.get("buyerPin").getAsString(),data));
        buyAdd.addCell(new Phrase("PHONE: "+object.get("Seller").getAsString(),data));
        buyAdd.addCell(new Phrase("GSTIN: "+object.get("toGstNo").getAsString(),data));
        table.addCell(buyAdd);
        //table.addCell();

    }
    private void botTableHeader(PdfPTable table){
        Stream.of("CLASS", "TOTAL", "SCHEME", "DISCOUNT","SGST","CGST","IGST", "TOTAL GST", "")
                .forEach(columnTitle -> {
                    Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                            Font.BOLD);
                    PdfPCell header = new PdfPCell();
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle,normal));
                    table.addCell(header);
                });
    }
    private void botTable(PdfPTable table, JsonArray jsonArray){

        table.setWidthPercentage(100);
         table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        PdfPTable sum = new PdfPTable(9);
        botTableHeader(sum);
        botDataTable(sum, jsonArray);
        PdfPTable sig = new PdfPTable(2);
        sign(sig, jsonArray);
        PdfPTable join = new PdfPTable(1);
        join.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        join.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        join.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        join.addCell(sum);
        join.addCell(sig);
        PdfPTable tot = new PdfPTable(2);
        tot.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        tot.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tot.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        //tot.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        totTable(tot, jsonArray);
        table.addCell(join);
        PdfPCell totColor = new PdfPCell();
        totColor.addElement(tot);
        totColor.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(totColor);
    }
    private void botDataTable(PdfPTable table, JsonArray jsonArray){

        JsonObject obj = jsonArray.get(0).getAsJsonObject();
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.LEFT);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);

        Font head = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.BOLD);

        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                Font.NORMAL);
       // table.getDefaultCell().addHeader(botTableHeader(table));
        PdfPCell gst5 = new PdfPCell();
        gst5.setBackgroundColor(BaseColor.LIGHT_GRAY);
        gst5.addElement(new Phrase("GST 5.00% :",normal));
        gst5.setBorder(Rectangle.NO_BORDER);
        gst5.setHorizontalAlignment(Element.ALIGN_CENTER);
        gst5.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(gst5);
        table.addCell(new Phrase(obj.get("tax_summ_5_total").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_5_schAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_5_discAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_5_sgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_5_cgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_5_igstAmt").getAsString(),normal));
        double tot5 = obj.get("tax_summ_5_sgstAmt").getAsDouble()+
                obj.get("tax_summ_5_cgstAmt").getAsDouble()+
                obj.get("tax_summ_5_igstAmt").getAsDouble();
        table.addCell(new Phrase(String.format("%.2f",tot5),normal));
        double invQty = 0.0;
        for (int i=0; i<jsonArray.size(); i++ ){
            invQty += jsonArray.get(i).getAsJsonObject().get("InvQty").getAsDouble();
        }

        double itemTot = obj.get("tax_summ_5_total").getAsDouble()
                +obj.get("tax_summ_12_total").getAsDouble()
                +obj.get("tax_summ_18_total").getAsDouble()
                +obj.get("tax_summ_28_total").getAsDouble();

        double schTot = obj.get("tax_summ_5_schAmt").getAsDouble()
                +obj.get("tax_summ_12_schAmt").getAsDouble()
                +obj.get("tax_summ_18_schAmt").getAsDouble()
                +obj.get("tax_summ_28_schAmt").getAsDouble();

        double disTot = obj.get("tax_summ_5_discAmt").getAsDouble()
                +obj.get("tax_summ_12_discAmt").getAsDouble()
                +obj.get("tax_summ_18_discAmt").getAsDouble()
                +obj.get("tax_summ_28_discAmt").getAsDouble();

        double sgstTot = obj.get("tax_summ_5_sgstAmt").getAsDouble()
                +obj.get("tax_summ_12_sgstAmt").getAsDouble()
                +obj.get("tax_summ_18_sgstAmt").getAsDouble()
                +obj.get("tax_summ_28_sgstAmt").getAsDouble();

        double cgstTot = obj.get("tax_summ_5_cgstAmt").getAsDouble()
                +obj.get("tax_summ_12_cgstAmt").getAsDouble()
                +obj.get("tax_summ_18_cgstAmt").getAsDouble()
                +obj.get("tax_summ_28_cgstAmt").getAsDouble();

        double igstTot = obj.get("tax_summ_5_igstAmt").getAsDouble()
                +obj.get("tax_summ_12_igstAmt").getAsDouble()
                +obj.get("tax_summ_18_igstAmt").getAsDouble()
                +obj.get("tax_summ_28_igstAmt").getAsDouble();

        table.addCell(new Phrase("Total Items: "+jsonArray.size(),normal));

        PdfPCell gst12 = new PdfPCell();
        gst12.setBackgroundColor(BaseColor.LIGHT_GRAY);
        gst12.addElement(new Phrase("GST 12.00%: ",normal));
        gst12.setBorder(Rectangle.NO_BORDER);
        gst12.setHorizontalAlignment(Element.ALIGN_CENTER);
        gst12.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(gst12);
        table.addCell(new Phrase(obj.get("tax_summ_12_total").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_12_schAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_12_discAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_12_sgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_12_cgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_12_igstAmt").getAsString(),normal));

        double tot12 = obj.get("tax_summ_12_sgstAmt").getAsDouble()+
                obj.get("tax_summ_12_cgstAmt").getAsDouble()+
                obj.get("tax_summ_12_igstAmt").getAsDouble();

        table.addCell(new Phrase(String.format("%.2f",tot12),normal));
        table.addCell(new Phrase("Total Qty: "+invQty,normal));

        PdfPCell gst18 = new PdfPCell();
        gst18.setBackgroundColor(BaseColor.LIGHT_GRAY);
        gst18.addElement(new Phrase("GST 18.00%: ",normal));
        gst18.setBorder(Rectangle.NO_BORDER);
        gst18.setHorizontalAlignment(Element.ALIGN_CENTER);
        gst18.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(gst18);
        table.addCell(new Phrase(obj.get("tax_summ_18_total").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_18_schAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_18_discAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_18_sgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_18_cgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_18_igstAmt").getAsString(),normal));
        double tot18 = obj.get("tax_summ_18_sgstAmt").getAsDouble()+
                obj.get("tax_summ_18_cgstAmt").getAsDouble()+
                obj.get("tax_summ_18_igstAmt").getAsDouble();

        table.addCell(new Phrase(String.format("%.2f",tot18),normal));
        table.addCell("");

        PdfPCell gst28 = new PdfPCell();
        gst28.setBackgroundColor(BaseColor.LIGHT_GRAY);
        gst28.addElement(new Phrase("GST 28.00%: ",normal));
        gst28.setBorder(Rectangle.NO_BORDER);
        gst28.setHorizontalAlignment(Element.ALIGN_CENTER);
        gst28.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(gst28);
        table.addCell(new Phrase(obj.get("tax_summ_28_total").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_28_schAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_28_discAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_28_sgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_28_cgstAmt").getAsString(),normal));
        table.addCell(new Phrase(obj.get("tax_summ_28_igstAmt").getAsString(),normal));
        double tot28 = obj.get("tax_summ_28_sgstAmt").getAsDouble()+
                obj.get("tax_summ_28_cgstAmt").getAsDouble()+
                obj.get("tax_summ_28_igstAmt").getAsDouble();

        table.addCell(new Phrase(String.format("%.2f",tot28),normal));
        table.addCell("");

        PdfPCell gstTot = new PdfPCell();
        gstTot.setBackgroundColor(BaseColor.LIGHT_GRAY);
        gstTot.addElement(new Phrase("TOTAL:",head));
        gstTot.setBorder(Rectangle.NO_BORDER);
        gstTot.setHorizontalAlignment(Element.ALIGN_CENTER);
        gstTot.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(gstTot);
        table.addCell(new Phrase( String.format("%.2f", itemTot),normal));
        table.addCell(new Phrase( String.format("%.2f", schTot),normal));
        table.addCell(new Phrase( String.format("%.2f", disTot),normal));
        table.addCell(new Phrase( String.format("%.2f", sgstTot),normal));
        table.addCell(new Phrase( String.format("%.2f", cgstTot),normal));
        table.addCell(new Phrase( String.format("%.2f", igstTot),normal));
        table.addCell(new Phrase( String.format("%.2f",tot5 + tot12 + tot18 + tot28),normal));
        table.addCell(new Phrase(""));
    }
    private void totTable(PdfPTable table, JsonArray jsonArray){

        JsonObject obj = jsonArray.get(0).getAsJsonObject();

        table.setWidthPercentage(100);
         table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_RIGHT);
       // table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);

        double itemTot = obj.get("tax_summ_5_total").getAsDouble()
                +obj.get("tax_summ_12_total").getAsDouble()
                +obj.get("tax_summ_18_total").getAsDouble()
                +obj.get("tax_summ_28_total").getAsDouble();

        double disTot = obj.get("tax_summ_5_discAmt").getAsDouble()
                +obj.get("tax_summ_12_discAmt").getAsDouble()
                +obj.get("tax_summ_18_discAmt").getAsDouble()
                +obj.get("tax_summ_28_discAmt").getAsDouble();

        double sgstTot = obj.get("tax_summ_5_sgstAmt").getAsDouble()
                +obj.get("tax_summ_12_sgstAmt").getAsDouble()
                +obj.get("tax_summ_18_sgstAmt").getAsDouble()
                +obj.get("tax_summ_28_sgstAmt").getAsDouble();

        double cgstTot = obj.get("tax_summ_5_cgstAmt").getAsDouble()
                +obj.get("tax_summ_12_cgstAmt").getAsDouble()
                +obj.get("tax_summ_18_cgstAmt").getAsDouble()
                +obj.get("tax_summ_28_cgstAmt").getAsDouble();

        double igstTot = obj.get("tax_summ_5_total").getAsDouble()
                +obj.get("tax_summ_12_total").getAsDouble()
                +obj.get("tax_summ_18_total").getAsDouble()
                +obj.get("tax_summ_28_total").getAsDouble();


        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 8,
                Font.BOLD);

        Font head = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.BOLD);
        table.addCell(new Phrase(" ",normal));
        table.addCell(new Phrase(" ",normal));
        table.addCell(new Phrase("TOTAL",normal));
        table.addCell(new Phrase(String.format("%.2f", itemTot),normal));
        table.addCell(new Phrase("DIST AMT: ",normal));
        table.addCell(new Phrase(String.format("%.2f", disTot),normal));
        table.addCell(new Phrase("SGST PAYABLE: ",normal));
        table.addCell(new Phrase(String.format("%.2f", sgstTot),normal));
        table.addCell(new Phrase("CGST PAYABLE: ", normal));
        table.addCell(new Phrase(String.format("%.2f", cgstTot), normal));
        table.addCell(new Phrase("IGST PAYABLE: ",normal));
        table.addCell(new Phrase(String.format("%.2f", igstTot),normal));
        table.addCell(new Phrase("CR/DR NOTE: ",normal));
        table.addCell(new Phrase("0.00",normal));
        table.addCell(new Phrase(" ",normal));
        table.addCell(new Phrase(" ",normal));
        table.addCell(new Phrase("GRAND TOTAL",head));
       // table.addCell(new Phrase("",normal));
       // table.addCell(new Phrase("",normal));
        table.addCell(new Phrase(obj.get("InvAmt").getAsString(),normal));
        //table.addCell(new Phrase("",normal));
        //table.addCell(new Phrase("",normal));
        table.addCell(new Phrase(" ",normal));
        table.addCell(new Phrase(" ",normal));
    }
    private void sign(PdfPTable table, JsonArray jsonArray){

        table.getDefaultCell().setBorder(Rectangle.BOX);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_LEFT);
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 6,
                Font.NORMAL);

        Font head = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.BOLD);

        JsonObject obj = jsonArray.get(0).getAsJsonObject();
        long invAmt = new Double(obj.get("InvAmt").getAsString()).longValue();
        table.addCell(new Phrase("Rs."+convert(invAmt).toUpperCase(),normal));
        table.addCell("");
        table.addCell(new Phrase("MSG: GST OTHERS 100% SGST",normal));
        table.addCell("");
        table.addCell(new Phrase("INVOICE COPY",head));
        table.addCell(new Phrase("For "+obj.get("Seller").getAsString(),head));
        table.addCell(new Phrase("Invoice copy downloaded from liveorder.in",normal));
        table.addCell("");
        table.addCell(new Phrase("Use Liveorder for Right Product At Right Time",normal));
        table.addCell(new Phrase("Authorised Signatory", head));

    }
    private static final String EMPTY = "";

    private static final String[] X =
            {
                    EMPTY, "One ", "Two ", "Three ", "Four ", "Five ", "Six ",
                    "Seven ", "Eight ", "Nine ", "Ten ", "Eleven ","Twelve ",
                    "Thirteen ", "Fourteen ", "Fifteen ", "Sixteen ",
                    "Seventeen ", "Eighteen ", "Nineteen "
            };
    private static final String[] Y =
            {
                    EMPTY, EMPTY, "Twenty ", "Thirty ", "Forty ", "Fifty ",
                    "Sixty ", "Seventy ", "Eighty ", "Ninety "
            };
    private static String convertToDigit(long n, String suffix)
    {
        if (n == 0) {
            return EMPTY;
        }
        if (n > 19) {
            return Y[(int) (n / 10)] + X[(int) (n % 10)] + suffix;
        }
        else {
            return X[(int) n] + suffix;
        }
    }
    public static String convert(long n)
    {
        StringBuilder res = new StringBuilder();
        res.append(convertToDigit((n / 1000000000) % 100, "Billion, "));
        res.append(convertToDigit((n / 10000000) % 100, "Crore, "));
        res.append(convertToDigit(((n / 100000) % 100), "Lakh, "));
        res.append(convertToDigit(((n / 1000) % 100), "Thousand, "));
        res.append(convertToDigit(((n / 100) % 10), "Hundred "));

        if ((n > 100) && (n % 100 != 0)) {
            res.append("and ");
        }
        res.append(convertToDigit((n % 100), ""));

        return res.toString().trim()
                .replace(", and", " and")
                .replaceAll("^(.*),$", "$1");        // remove trailing comma
    }

    private void expiryHeader(PdfPTable table){
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 9,
                Font.BOLD);

        Stream.of("SI", "ITEM NAME ", "Mfac", "HSN","PACK","QTY","BATCH NO", "EXP DATE", "MRP",
                        "MRP VALUE")
                .forEach(columnTitle -> {
                    Font normal = font;
                    PdfPCell header = new PdfPCell();
                    //table.getDefaultCell().setUseBorderPadding(true);
                    table.getDefaultCell().setPaddingBottom(5);
                    table.getDefaultCell().setPaddingLeft(5);
                    table.getDefaultCell().setPaddingRight(5);
                    table.getDefaultCell().setPaddingLeft(5);
                    table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle,normal));
                    table.addCell(header);
                });
    }

    private void getEspiryEmpty(PdfPTable table){

        for (int i=0; i<170; i++){
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.BOX);
            //table.getDefaultCell().setUseBorderPadding(true);
            table.getDefaultCell().setPaddingBottom(10);
            table.getDefaultCell().setPaddingLeft(10);
            table.getDefaultCell().setPaddingRight(10);
            table.getDefaultCell().setPaddingLeft(10);
            table.getDefaultCell().setBorderWidth(1);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(new Phrase(" "));
        }
    }

    private void formBottom(PdfPTable bottom, Font font) throws DocumentException {

       /* Font fontUnder = new Font(Font.FontFamily.TIMES_ROMAN, 10,
                Font.BOLD|Font.UNDERLINE);*/

        PdfPTable totCell = new PdfPTable(1);
        totCell.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        totCell.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        totCell.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        totCell.addCell(new Phrase("                                  TOTAL:        ",font));
        bottom.addCell(totCell);

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{40, 60});

        Font dif = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                Font.BOLD);

        PdfPCell sig = new PdfPCell();
        sig.setHorizontalAlignment(Element.ALIGN_CENTER);
        sig.setVerticalAlignment(Element.ALIGN_MIDDLE);
        sig.setBorder(Rectangle.BOTTOM);
        sig.addElement(new Phrase("CUSTOMER SEAL & SIGNATURE", font));
        PdfPTable emp = new PdfPTable(1);
        emp.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        emp.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        emp.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        emp.addCell(sig);
        emp.addCell("  ");
        emp.addCell("  ");
        emp.addCell("  ");
        emp.addCell("  ");
        table.addCell(emp);

        PdfPTable office = new PdfPTable(1);
        office.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);
        office.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        office.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        office.addCell(new Phrase("            FOR OFFICE USE", dif));
        office.addCell(new Phrase("CREDIT NOTE NO. :              CR.NOTE DT. :", font));
        office.addCell(new Phrase("PREPARED BY :                       CHECKED BY :",font));
        PdfPCell app = new PdfPCell();
        app.setHorizontalAlignment(Element.ALIGN_RIGHT);
        app.setVerticalAlignment(Element.ALIGN_MIDDLE);
        app.setBorder(Rectangle.BOTTOM);
        app.addElement(new Phrase("APPROVED BY :", font));
        office.addCell(app);
        office.addCell(new Phrase("                             SETTLEMENT DETAILS ",dif));
        office.addCell(new Phrase("INVOICE NO. :                        INVOICE DT. :", font));
        table.addCell(office);
        bottom.addCell(table);
    }
}
