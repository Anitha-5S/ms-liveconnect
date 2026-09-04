package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.MostViewedPrdsBO;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface SearchService extends MasterBaseService {

    JsonArray getProductDetails(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException;

    JsonArray getProductsOnMolecule(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException;

    JsonArray getSellerDetails(SearchBO searchBO) throws RecordNotFoundException;

    JsonArray getManufactureDetails(SearchBO searchBO) throws RecordNotFoundException;

    long countManufacture(String searchString);

    long countSeller(String searchString);

    long countMolecule(String searchString);

    long countProduct(SearchBO searchBO);

    JsonArray getItemListByCategory(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    long countProductsOnCategory(String searchString);

    JsonArray getItemDetails(String type, String code);

    JsonArray getProductOnManufacture(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    long countProductsOnManufacture(SearchBO searchBO);

    JsonArray getProductOnSeller(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException;

    long countProductsOnSeller(String searchString);

    JsonArray getProductOnMolecule(SearchBO searchBO) throws RecordNotFoundException;

    long countProductsOnMolecule(SearchBO searchBO);

    JsonArray getSellerOnProduct(LcHeaderBO lcHeaderBO, SearchBO searchBO,String sellerCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException;

    long getSellerOnProductCount(LcHeaderBO lcHeaderBo, SearchBO itemCode, String sellerCode) throws RecordNotFoundException,CommunicationErrorException, InvalidRequestException;

    JsonArray getElProductDetails(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException;

    void syncLcItemToElItem(PageBO pageBO);

    long elProductCount(SearchBO searchBO) throws RecordNotFoundException;

    JsonArray getTSPrd(Long userId, SearchBO searchBO, String c2Code) throws RecordNotFoundException;

    JsonArray checkProductsStock(JsonArray j_item_codes, String c2Code, String brCode) throws RecordNotFoundException;

    JsonArray mostViewedPrds(MostViewedPrdsBO viewedPrdBO, LcHeaderBO header) throws InputPayloadException, RecordNotFoundException;

    String updateSalesCount(JsonObject inputJson) throws RecordNotFoundException;

    long mostViewedPrdsCount(MostViewedPrdsBO viewedPrdBO, LcHeaderBO header) throws InputPayloadException;
}
