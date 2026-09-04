package com.c2.lc.ms.master.services;

import com.c2.lc.lib.bo.PageBO;

import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.ms.master.models.Category;
import com.c2.lc.ms.master.repos.mongo.CategoryRepository;
import com.c2.lc.ms.master.repos.mysql.CustItemCategoryMstRepository;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.CategoryService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl extends MasterBaseServiceImpl implements CategoryService {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CustItemCategoryMstRepository custItemCategoryMstRepository;

    @Override
    public List<Category> getAllCategory() throws RecordNotFoundException {

       // Pageable pageable = PageRequest.of(0, 10,Sort.by(Sort.Direction.ASC,"c_name"));
        //Page<Category> entities = categoryRepository.findAll(pageable);
        List<Category> entities = categoryRepository.findAll(Sort.by(Sort.Direction.ASC,"c_category_name"));

        if (entities.isEmpty()) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        return entities;
    }

    @Override
    public long categoryCount() {
        return categoryRepository.count();
    }

    @Override
    public void saveCategory(Category category) {
            categoryRepository.save(category);
    }


    @Override
    public JsonArray getCategoryList(String c2Code, int page, int limit, String searchTerm) throws RecordNotFoundException {
        String sql = getCategoryOnC2Code(searchTerm);
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        if(!searchTerm.isBlank()){
            query.setParameter("searchTerm", searchTerm + '%');
        }
        // query.setParameter("itemCode", itemCode);
        List<Object[]> categoryList = this.getResultList(query, page, limit);

        if (categoryList.size() == 0) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        JsonArray jsonArray = new JsonArray();
        for (Object[] objects :categoryList
        ) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("c_category_code",helper.getString(objects[0]));
            jsonObject.addProperty("c_category_name",helper.getString(objects[1]));
            jsonObject.addProperty("c_category_image_url",helper.getString(objects[2]));
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    @Override
    public long getCategoryCountByC2Code(String c2Code, int page, int limit, String searchTerm) {
        BigInteger count = BigInteger.ZERO;
        String sql = "SELECT COUNT(*) FROM (" +getCategoryOnC2Code(searchTerm)+ ") DUMMY";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        if(!searchTerm.isBlank()){
            query.setParameter("searchTerm", searchTerm + '%');
        }
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    private String getCategoryOnC2Code(String searchTerm){
        String sql = "select distinct cicm.c_code, cicm.c_name, cicm.c_image_url from cust_item_category_mst cicm " +
                "where cicm.c_c2code = :c2Code " ;

        if (!searchTerm.isBlank()) {
            sql += "AND cicm.c_name like :searchTerm ";
        }
             sql += "ORDER BY cicm.c_name "  ;
        return sql;
    }

    @Override
    public List<JsonObject> geTrendingCategories(PageBO pageBo, String c2Code) {
        JsonObject json;
        List<JsonObject> list = new ArrayList<>();
        String sql = getTrendingCategory();
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        List<Object[]> resultList = this.getResultList(query, pageBo.getPage(), pageBo.getLimit());

        if(resultList != null) {
            for (Object[] obj : resultList) {
                json = new JsonObject();
                json.addProperty("c_category_code", helper.getString(obj[0]));
                json.addProperty("c_category_name", helper.getString(obj[1]));
                json.addProperty("c_category_image_url", helper.getString(obj[2]));
                list.add(json);
            }
        }
        return list;
    }

    @Override
    public int getTrendingCount(String c2Code) {
        BigInteger count = BigInteger.ZERO;
        String sql = "SELECT COUNT(*) FROM (" +getTrendingCategory()+ ") DUMMY";
        Query query = this.getQuery(sql);
        query.setParameter("c2Code", c2Code);
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }
        return count.intValue();
    }

    public String getTrendingCategory() {
        return "select cicm.c_code, cicm.c_name, cicm.c_image_url from cust_popular_category cpc " +
                "   join cust_item_category_mst cicm on cpc.c_category_code = cicm.c_code and cpc.c_c2code = cicm.c_c2code" +
                "   where cpc.c_c2code = :c2Code";
    }
}
