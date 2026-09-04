package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.CommunicationErrorException;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.bos.MostViewedPrdsBO;
import com.c2.lc.ms.master.services.interfaces.MoleculeService;
import com.c2.lc.ms.master.services.interfaces.SearchService;
import com.c2.lc.ms.master.transactions.base.MasterBaseTransactionImpl;
import com.c2.lc.ms.master.transactions.interfaces.SearchTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchTransactionImpl extends MasterBaseTransactionImpl implements SearchTransaction {

    @Autowired private SearchService searchService;
    @Autowired private MoleculeService moleculeService;

    @Override
    public JsonArray getProductDetails(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getProductDetails(headerBO, searchBO);
    }

    @Override
    public JsonArray getProductsOnMolecule(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getProductsOnMolecule(headerBO, searchBO);
    }

    @Override
    public JsonArray getSellerDetails(SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getSellerDetails(searchBO);
    }

    @Override
    public JsonArray getItemListByCategory(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getItemListByCategory(lcHeaderBO, searchBO);
    }

    @Override
    public long countProductsOnCategory(String searchString) {
        return searchService.countProductsOnCategory(searchString);
    }

    @Override
    public JsonArray getManufactureDetails(SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getManufactureDetails(searchBO);
    }

    @Override
    public long countManufacture(String searchString) {
        return searchService.countManufacture(searchString);
    }

    @Override
    public long countSeller(String searchString) {
        return searchService.countSeller(searchString);
    }

    @Override
    public long countMolecules(SearchBO searchBO) {
        return moleculeService.count(searchBO);
    }

    @Override
    public long countProduct(SearchBO searchBO) {
        return searchService.countProduct(searchBO);
    }

    public JsonArray getItemDetails(String type, String code) {
        return searchService.getItemDetails(type, code);
    }

    @Override
    public JsonArray getProductOnManufacture(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getProductOnManufacture(lcHeaderBO,searchBO);
    }

    @Override
    public long countProductsOnManufacture(SearchBO searchBO) {
        return searchService.countProductsOnManufacture(searchBO);
    }

    @Override
    public JsonArray getProductOnSeller(LcHeaderBO lcHeaderBO, SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getProductOnSeller(lcHeaderBO,searchBO);
    }

    @Override
    public long countProductsOnSeller(String searchString) {
        return searchService.countProductsOnSeller(searchString);
    }

    @Override
    public JsonArray getProductOnMolecule(SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getProductOnMolecule(searchBO);
    }

    @Override
    public long countProductsOnMolecule(SearchBO searchBO) {
        return searchService.countProductsOnMolecule(searchBO);
    }

    @Override
    public long getSellerOnProductCount(LcHeaderBO lcHeaderBo, SearchBO searchBO, String sellerCode) throws RecordNotFoundException,CommunicationErrorException, InvalidRequestException {
        return searchService.getSellerOnProductCount(lcHeaderBo, searchBO,sellerCode);
    }

    @Override
    public JsonArray getSellerOnProduct(LcHeaderBO lcHeaderBO, SearchBO searchBO, String sellerCode) throws RecordNotFoundException, InvalidRequestException, CommunicationErrorException {
        return searchService.getSellerOnProduct(lcHeaderBO, searchBO, sellerCode);
    }

    @Override
    public JsonArray getMolecules(LcHeaderBO header, SearchBO searchBO) throws RecordNotFoundException {
        return moleculeService.list(searchBO);
    }

    @Override
    public JsonArray getProducts(Long userId, SearchBO searchBO, String c2Code) throws RecordNotFoundException {
        return searchService.getTSPrd(userId, searchBO, c2Code);
    }

    @Override
    public JsonArray getElProductDetails(LcHeaderBO headerBO, SearchBO searchBO) throws RecordNotFoundException {
        return searchService.getElProductDetails(headerBO, searchBO);
    }

    @Override
    public long elProductCount( SearchBO searchBO) throws RecordNotFoundException {
        return searchService.elProductCount(searchBO);
    }

    @Override
    public void syncLcItemToElItem(PageBO pageBO) {
        searchService.syncLcItemToElItem(pageBO);
    }

    @Override
    public long mostViewedPrdsCount(MostViewedPrdsBO viewedPrdBO, LcHeaderBO header) throws InputPayloadException {
        return searchService.mostViewedPrdsCount(viewedPrdBO,header);
    }

    @Override
    public String updateSalesCount(JsonObject inputJson) throws RecordNotFoundException {
        return searchService.updateSalesCount(inputJson);
    }

    @Override
    public JsonArray mostViewedPrds(MostViewedPrdsBO viewedPrdBO, LcHeaderBO header) throws InputPayloadException, RecordNotFoundException {
        return searchService.mostViewedPrds(viewedPrdBO,header);
    }

    @Override
    public JsonArray checkProductsStock(JsonArray j_item_codes, String c2Code, String brCode) throws RecordNotFoundException {
        return searchService.checkProductsStock(j_item_codes,c2Code,brCode);
    }
}
