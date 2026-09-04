package com.c2.lc.ms.master.transactions.interfaces;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.models.Category;

import java.util.List;
import com.google.gson.JsonArray;

import com.c2.lc.lib.bo.SearchBO;
import com.google.gson.JsonObject;


public interface CategoryTransaction {


    List<Category> getAllCategory() throws RecordNotFoundException;

    long categoryCount();

    void saveCategory(Category category);


    //long countCategoryByC2Code(LcHeaderBO headers, SearchBO searchBO);


    List<JsonObject> geTrendingCategories(PageBO pageBo, String c2Code);

    int getTrendingCount(String c2Code);

    JsonArray categoryList(String c2Code, int page, int limit, String searchTerm) throws RecordNotFoundException;

    long countCategoryByC2Code(LcHeaderBO headerBO, int page, int limit, String searchTerm);
}
