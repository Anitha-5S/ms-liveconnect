package com.c2.lc.ms.master.transactions;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.transactions.BaseTransactionImpl;
import com.c2.lc.ms.master.models.Category;
import com.c2.lc.ms.master.services.interfaces.CategoryService;
import com.c2.lc.ms.master.transactions.interfaces.CategoryTransaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryTransactionImpl extends BaseTransactionImpl implements CategoryTransaction {

    @Autowired
    private CategoryService categoryService;
    @Override
    public List<Category> getAllCategory() throws RecordNotFoundException {
        return categoryService.getAllCategory();
    }

    @Override
    public long categoryCount() {
        return categoryService.categoryCount();
    }

    @Override
    public void saveCategory(Category category) {
        categoryService.saveCategory(category);
    }

    public List<JsonObject> geTrendingCategories(PageBO pageBo, String c2Code) {
        return categoryService.geTrendingCategories(pageBo, c2Code);
    }

    @Override
    public int getTrendingCount(String c2Code) {
        return categoryService.getTrendingCount(c2Code);

    }

    @Override
    public JsonArray categoryList(String c2Code, int page, int limit, String searchTerm) throws RecordNotFoundException {
        return categoryService.getCategoryList(c2Code, page, limit,searchTerm );
    }

    @Override
    public long countCategoryByC2Code(LcHeaderBO headerBO, int page, int limit, String searchTerm) {
        return categoryService.getCategoryCountByC2Code(headerBO.getC2Code(), page,limit,searchTerm);
    }
}
