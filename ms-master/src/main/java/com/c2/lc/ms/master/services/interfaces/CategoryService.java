package com.c2.lc.ms.master.services.interfaces;

import com.c2.lc.lib.bo.PageBO;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.master.models.Category;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategory() throws RecordNotFoundException;

    long categoryCount();

    void saveCategory(Category category);

    List<JsonObject> geTrendingCategories(PageBO pageBo, String c2Code);

    int getTrendingCount(String c2Code);

    JsonArray getCategoryList(String c2Code, int page, int limit, String searchTerm) throws RecordNotFoundException;

    long getCategoryCountByC2Code(String c2Code, int page, int limit, String searchTerm);
}
