package com.c2.lc.ms.master.services;

import com.c2.lc.lib.exceptions.SimpleException;
import com.c2.lc.ms.master.services.base.MasterBaseServiceImpl;
import com.c2.lc.ms.master.services.interfaces.AdminService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;


@Slf4j
@Service
@Transactional
public class AdminServiceImpl extends MasterBaseServiceImpl implements AdminService {

    @Override
    public JsonObject getState(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        JsonArray elements = new JsonArray();
        String searchValue = helper.getString(data.get("c_search_value")) != "" ? helper.getString(data.get("c_search_value"))+"%" : "";
        String sortOrder = helper.getString(data.get("c_sort")) != "" ? helper.getString(data.get("c_sort")) : "d_adate";
        String orderBy = helper.getString(data.get("c_order_by")) !="" ? helper.getString(data.get("c_order_by")) : "ASC";
        int nOffset = helper.getInt(data.get("n_offset"));
        int nLimit = helper.getInt(data.get("n_limit"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_zone_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_state_mst ";
        if(!searchValue.equals("")){
            if(searchValue.length() >= 4) {
                sql += " WHERE c_name LIKE :searchValue ";
            }else{
                sql += " WHERE c_sh_name LIKE :searchValue";
            }
        }
        sql += " order by " + sortOrder + " " + orderBy;
        Query query = this.getQuery(sql);
        query.setParameter("searchValue", searchValue);
        query.setFirstResult(nOffset);
        query.setMaxResults(nLimit);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            JsonObject object = new JsonObject();
            int i = -1;
            object.addProperty("c_code", helper.getString(obj[++i]));
            object.addProperty("c_name", helper.getString(obj[++i]));
            object.addProperty("c_sh_name", helper.getString(obj[++i]));
            object.addProperty("c_geo_zone_code", helper.getString(obj[++i]));
            object.addProperty("c_geo_lat", helper.getString(obj[++i]));
            object.addProperty("c_geo_lon", helper.getString(obj[++i]));
            object.addProperty("n_audited", helper.getString(obj[++i]));
            object.addProperty("n_predefined", helper.getString(obj[++i]));
            object.addProperty("c_createuser", helper.getString(obj[++i]));
            object.addProperty("d_adate", helper.getString(obj[++i]));
            object.addProperty("d_ldate", helper.getString(obj[++i]));
            object.addProperty("t_ltime", helper.getString(obj[++i]));
            object.addProperty("c_modiuser", helper.getString(obj[++i]));
            elements.add(object);
        }
        if(elements.size() > 0){
            responseJson.add("stateList", elements);
        }
        return responseJson;
    }

    @Override
    public JsonObject getStateDetails(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        String stateCode = helper.getString(data.get("c_state_code"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_zone_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_state_mst WHERE c_code = :stateCode ";
        Query query = this.getQuery(sql);
        query.setParameter("stateCode", stateCode);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            int i = -1;
            responseJson.addProperty("c_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_sh_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_zone_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lat", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lon", helper.getString(obj[++i]));
            responseJson.addProperty("n_audited", helper.getString(obj[++i]));
            responseJson.addProperty("n_predefined", helper.getString(obj[++i]));
            responseJson.addProperty("c_createuser", helper.getString(obj[++i]));
            responseJson.addProperty("d_adate", helper.getString(obj[++i]));
            responseJson.addProperty("d_ldate", helper.getString(obj[++i]));
            responseJson.addProperty("t_ltime", helper.getString(obj[++i]));
            responseJson.addProperty("c_modiuser", helper.getString(obj[++i]));
        }
        return responseJson;
    }

    @Override
    public JsonObject addState(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String stateCode = helper.getString(data.get("c_code").getAsString());
        String stateName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String zoneCode = "-";
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if (stateName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (zoneCode.equals("")){
            field = "c_geo_zone_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getStateDataCount(stateCode);
        int existsTemp = getTempStateDataCount(stateCode,stateName,shortName,zoneCode);
        int result = 0;
        if(exists == 0 && existsTemp == 0) {
            String sql = "INSERT INTO u_geo_state_mst_temp (c_code,c_name,c_sh_name,c_geo_zone_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:stateCode,:stateName,:shortName,:zoneCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
            Query query = this.getQuery(sql);
            query.setParameter("stateCode", stateCode);
            query.setParameter("stateName", stateName);
            query.setParameter("shortName", shortName);
            query.setParameter("zoneCode", zoneCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        }else{
            msg = "State code already exists";
            throw new SimpleException(msg);
        }
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getTempStateDataCount(String stateCode, String stateName, String shortName, String zoneCode) {
        String sql = "SELECT count(c_sh_name) FROM u_geo_state_mst_temp where c_code = :stateCode AND c_name = :stateName AND c_sh_name = :shortName " +
                " AND c_geo_zone_code = :zoneCode AND n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("stateCode", stateCode);
        query.setParameter("stateName", stateName);
        query.setParameter("shortName", shortName);
        query.setParameter("zoneCode", zoneCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject editState(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String stateCode = helper.getString(data.get("c_code").getAsString());
        String stateName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String zoneCode = helper.getString(data.get("c_geo_zone_code").getAsString()) != "" ? helper.getString(data.get("c_geo_zone_code").getAsString()) : "-";
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if(stateCode.equals("")){
            field = "c_code";
        }else if (stateName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (zoneCode.equals("")){
            field = "c_geo_zone_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getStateTempDataCount(stateCode);
        int result = 0;
        String sql = "";
        if(exists == 0) {
            sql = "INSERT INTO u_geo_state_mst_temp (c_code,c_name,c_sh_name,c_geo_zone_code,c_geo_lat, " +
                    " c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    " VALUES (:stateCode,:stateName,:shortName,:zoneCode,:latitude,:longitude,:userName, " +
                    " now(),now(),now(),:userName)";
        }else{
            sql = "UPDATE u_geo_state_mst_temp SET c_name = :stateName, c_sh_name = :shortName, " +
                    " c_geo_zone_code = :zoneCode,c_geo_lat = :latitude,c_geo_lon = :longitude, " +
                    " d_ldate =  now(),t_ltime = now(),c_modiuser = :userName WHERE c_code = :stateCode and n_approval_flag = 0";
        }
            Query query = this.getQuery(sql);
            query.setParameter("stateCode", stateCode);
            query.setParameter("stateName", stateName);
            query.setParameter("shortName", shortName);
            query.setParameter("zoneCode", zoneCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getStateTempDataCount(String stateCode) {
        String sql = "SELECT count(c_code) as count from u_geo_state_mst_temp where c_code = :stateCode and n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("stateCode", stateCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject addDistrict(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String districtName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String stateCode = helper.getString(data.get("c_geo_state_code").getAsString());
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if (districtName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (stateCode.equals("")){
            field = "c_geo_state_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getDistrictDataCount(districtName,stateCode);
        int existsTemp = getTempDistrictDataCount(districtName,shortName,stateCode);
        int result = 0;
        if(exists == 0 && existsTemp == 0) {
            String sql = "INSERT INTO u_geo_district_mst_temp (c_name,c_sh_name,c_geo_state_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:districtName,:shortName,:stateCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
            Query query = this.getQuery(sql);
            query.setParameter("districtName", districtName);
            query.setParameter("shortName", shortName);
            query.setParameter("stateCode", stateCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        }else{
            msg = "District name already exists";
            throw new SimpleException(msg);
        }
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getTempDistrictDataCount(String districtName, String shortName, String stateCode) {
        String sql = "SELECT count(c_sh_name) FROM u_geo_district_mst_temp where c_name = :districtName AND c_sh_name = :shortName " +
                " AND c_geo_state_code = :stateCode  AND n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("stateCode", stateCode);
        query.setParameter("districtName", districtName);
        query.setParameter("shortName", shortName);
        query.setParameter("stateCode", stateCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject editDistrict(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String districtCode = helper.getString(data.get("c_code").getAsString());
        String districtName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String stateCode = helper.getString(data.get("c_geo_state_code").getAsString());
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if(districtCode.equals("")){
            field = "c_code";
        }else if (districtName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (stateCode.equals("")){
            field = "c_geo_state_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getDistrictTempDataCount(districtCode);
        int result = 0;
        String sql = "";
        if(exists == 0) {
            sql = "INSERT INTO u_geo_district_mst_temp (c_code,c_name,c_sh_name,c_geo_state_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:districtCode,:districtName,:shortName,:stateCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
        } else{
            sql = "UPDATE u_geo_district_mst_temp SET c_name = :districtName, c_sh_name = :shortName, " +
                    " c_geo_state_code = :stateCode,c_geo_lat = :latitude,c_geo_lon = :longitude, " +
                    " d_ldate =  now(),t_ltime = now(),c_modiuser = :userName WHERE c_code = :districtCode and n_approval_flag = 0";
        }
            Query query = this.getQuery(sql);
            query.setParameter("districtCode", districtCode);
            query.setParameter("districtName", districtName);
            query.setParameter("shortName", shortName);
            query.setParameter("stateCode", stateCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getDistrictTempDataCount(String districtCode) {
        String sql = "SELECT count(c_code) as count from u_geo_district_mst_temp where c_code = :districtCode and n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("districtCode", districtCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject getDistrictDetails(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        String districtCode = helper.getString(data.get("c_district_code"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_state_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_district_mst WHERE c_code = :districtCode ";
        Query query = this.getQuery(sql);
        query.setParameter("districtCode", districtCode);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            int i = -1;
            responseJson.addProperty("c_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_sh_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_state_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lat", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lon", helper.getString(obj[++i]));
            responseJson.addProperty("n_audited", helper.getString(obj[++i]));
            responseJson.addProperty("n_predefined", helper.getString(obj[++i]));
            responseJson.addProperty("c_createuser", helper.getString(obj[++i]));
            responseJson.addProperty("d_adate", helper.getString(obj[++i]));
            responseJson.addProperty("d_ldate", helper.getString(obj[++i]));
            responseJson.addProperty("t_ltime", helper.getString(obj[++i]));
            responseJson.addProperty("c_modiuser", helper.getString(obj[++i]));
        }
        return responseJson;
    }

    @Override
    public JsonObject getDistrict(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        JsonArray elements = new JsonArray();
        String stateCode = helper.getString(data.get("c_state_code"));
        String searchValue = helper.getString(data.get("c_search_value")) != "" ? helper.getString(data.get("c_search_value"))+"%" : "";
        String sortOrder = helper.getString(data.get("c_sort")) != "" ? helper.getString(data.get("c_sort")) : "d_adate";
        String orderBy = helper.getString(data.get("c_order_by")) !="" ? helper.getString(data.get("c_order_by")) : "ASC";
        int nOffset = helper.getInt(data.get("n_offset"));
        int nLimit = helper.getInt(data.get("n_limit"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_state_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_district_mst where c_geo_state_code = :stateCode";
        if(!searchValue.equals("")){
            if(searchValue.length() >= 5) {
                sql += " and c_name LIKE :searchValue ";
            }else{
                sql += " and c_sh_name LIKE :searchValue";
            }
        }
        sql += " order by " + sortOrder + " " + orderBy;
        Query query = this.getQuery(sql);
        query.setParameter("searchValue", searchValue);
        query.setParameter("stateCode", stateCode);
        query.setFirstResult(nOffset);
        query.setMaxResults(nLimit);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            JsonObject object = new JsonObject();
            int i = -1;
            object.addProperty("c_code", helper.getString(obj[++i]));
            object.addProperty("c_name", helper.getString(obj[++i]));
            object.addProperty("c_sh_name", helper.getString(obj[++i]));
            object.addProperty("c_geo_state_code", helper.getString(obj[++i]));
            object.addProperty("c_geo_lat", helper.getString(obj[++i]));
            object.addProperty("c_geo_lon", helper.getString(obj[++i]));
            object.addProperty("n_audited", helper.getString(obj[++i]));
            object.addProperty("n_predefined", helper.getString(obj[++i]));
            object.addProperty("c_createuser", helper.getString(obj[++i]));
            object.addProperty("d_adate", helper.getString(obj[++i]));
            object.addProperty("d_ldate", helper.getString(obj[++i]));
            object.addProperty("t_ltime", helper.getString(obj[++i]));
            object.addProperty("c_modiuser", helper.getString(obj[++i]));
            elements.add(object);
        }
        if(elements.size() > 0){
            responseJson.add("districtList", elements);
        }
        return responseJson;
    }

    @Override
    public JsonObject addCity(JsonObject data) throws SimpleException{
        JsonObject responseJson = new JsonObject();
        String cityName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String districtCode = helper.getString(data.get("c_geo_district_code").getAsString());
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if (cityName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (districtCode.equals("")){
            field = "c_geo_district_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getCityDataCount(cityName,districtCode);
        int existsTemp = getTempCityDataCount(cityName,shortName,districtCode);
        int result = 0;
        if(exists == 0 && existsTemp == 0) {
            String sql = "INSERT INTO u_geo_city_mst_temp (c_name,c_sh_name,c_geo_district_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:cityName,:shortName,:districtCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
            Query query = this.getQuery(sql);
            query.setParameter("cityName", cityName);
            query.setParameter("shortName", shortName);
            query.setParameter("districtCode", districtCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        }else{
            msg = "City name already exists";
            throw new SimpleException(msg);
        }
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getTempCityDataCount(String citytName, String shortName, String districtCode) {
        String sql = "SELECT count(c_sh_name) FROM u_geo_city_mst_temp where c_name = :cityName AND c_sh_name = :shortName " +
                " AND c_geo_district_code = :districtCode AND n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("cityName", citytName);
        query.setParameter("shortName", shortName);
        query.setParameter("districtCode", districtCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject editCity(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String cityCode = helper.getString(data.get("c_code").getAsString());
        String cityName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String districtCode = helper.getString(data.get("c_geo_district_code").getAsString());
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if(cityCode.equals("")){
            field = "c_code";
        }else if (cityName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (districtCode.equals("")){
            field = "c_geo_city_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getCityTempDataCount(cityCode);
        int result = 0;
        String sql = "";
        if(exists > 0) {
            sql = "UPDATE u_geo_city_mst_temp SET c_name = :cityName, c_sh_name = :shortName, " +
                    " c_geo_district_code = :districtCode,c_geo_lat = :latitude,c_geo_lon = :longitude, " +
                    " d_ldate =  now(),t_ltime = now(),c_modiuser = :userName WHERE c_code = :cityCode and n_approval_flag = 0";
        }else {
            sql = "INSERT INTO u_geo_city_mst_temp (c_code,c_name,c_sh_name,c_geo_district_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:cityCode,:cityName,:shortName,:districtCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
        }
            Query query = this.getQuery(sql);
            query.setParameter("cityCode", cityCode);
            query.setParameter("cityName", cityName);
            query.setParameter("shortName", shortName);
            query.setParameter("districtCode", districtCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getCityTempDataCount(String cityCode) {
        String sql = "SELECT count(c_code) as count from u_geo_city_mst_temp where c_code = :cityCode and n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("cityCode", cityCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject getCityDetails(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        String cityCode = helper.getString(data.get("c_city_code"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_district_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_city_mst WHERE c_code = :cityCode ";
        Query query = this.getQuery(sql);
        query.setParameter("cityCode", cityCode);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            int i = -1;
            responseJson.addProperty("c_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_sh_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_district_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lat", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lon", helper.getString(obj[++i]));
            responseJson.addProperty("n_audited", helper.getString(obj[++i]));
            responseJson.addProperty("n_predefined", helper.getString(obj[++i]));
            responseJson.addProperty("c_createuser", helper.getString(obj[++i]));
            responseJson.addProperty("d_adate", helper.getString(obj[++i]));
            responseJson.addProperty("d_ldate", helper.getString(obj[++i]));
            responseJson.addProperty("t_ltime", helper.getString(obj[++i]));
            responseJson.addProperty("c_modiuser", helper.getString(obj[++i]));
        }
        return responseJson;
    }

    @Override
    public JsonObject getCity(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        JsonArray elements = new JsonArray();
        String districtCode = helper.getString(data.get("c_district_code"));
        String searchValue = helper.getString(data.get("c_search_value")) != "" ? helper.getString(data.get("c_search_value"))+"%" : "";
        String sortOrder = helper.getString(data.get("c_sort")) != "" ? helper.getString(data.get("c_sort")) : "d_adate";
        String orderBy = helper.getString(data.get("c_order_by")) !="" ? helper.getString(data.get("c_order_by")) : "ASC";
        int nOffset = helper.getInt(data.get("n_offset"));
        int nLimit = helper.getInt(data.get("n_limit"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_district_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_city_mst where c_geo_district_code = :districtCode";
        if(!searchValue.equals("")){
            if(searchValue.length() >= 4) {
                sql += " and c_name LIKE :searchValue ";
            }else{
                sql += " and c_sh_name LIKE :searchValue";
            }
        }
        sql += " order by " + sortOrder + " " + orderBy;
        Query query = this.getQuery(sql);
        query.setParameter("searchValue", searchValue);
        query.setParameter("districtCode", districtCode);
        query.setFirstResult(nOffset);
        query.setMaxResults(nLimit);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            JsonObject object = new JsonObject();
            int i = -1;
            object.addProperty("c_code", helper.getString(obj[++i]));
            object.addProperty("c_name", helper.getString(obj[++i]));
            object.addProperty("c_sh_name", helper.getString(obj[++i]));
            object.addProperty("c_geo_district_code", helper.getString(obj[++i]));
            object.addProperty("c_geo_lat", helper.getString(obj[++i]));
            object.addProperty("c_geo_lon", helper.getString(obj[++i]));
            object.addProperty("n_audited", helper.getString(obj[++i]));
            object.addProperty("n_predefined", helper.getString(obj[++i]));
            object.addProperty("c_createuser", helper.getString(obj[++i]));
            object.addProperty("d_adate", helper.getString(obj[++i]));
            object.addProperty("d_ldate", helper.getString(obj[++i]));
            object.addProperty("t_ltime", helper.getString(obj[++i]));
            object.addProperty("c_modiuser", helper.getString(obj[++i]));
            elements.add(object);
        }
        if(elements.size() > 0){
            responseJson.add("cityList", elements);
        }
        return responseJson;
    }

    @Override
    public JsonObject addArea(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String areaName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String cityCode = helper.getString(data.get("c_geo_city_code").getAsString());
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if (areaName.equals("")){
            field = "c_name";
        }else if(shortName.equals("")){
            field = "c_sh_name";
        }else if (cityCode.equals("")){
            field = "c_geo_city_code";
        }else if (userName.equals("")){
            field = "c_createuser";
        }
        if(!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getAreaDataCount(areaName,cityCode);
        int existsTemp = getTempAreaDataCount(areaName,shortName,cityCode);
        int result = 0;
        if(exists == 0 && existsTemp == 0) {
            String sql = "INSERT INTO u_geo_area_mst_temp (c_name,c_sh_name,c_geo_city_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:areaName,:shortName,:cityCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
            Query query = this.getQuery(sql);
            query.setParameter("areaName", areaName);
            query.setParameter("shortName", shortName);
            query.setParameter("cityCode", cityCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        }else{
            msg = "Area name already exists";
            throw new SimpleException(msg);
        }
        responseJson.addProperty("result", result);
        return responseJson;
    }

    private int getTempAreaDataCount(String areaName, String shortName, String cityCode) {
        String sql = "SELECT count(c_sh_name) FROM u_geo_area_mst_temp where c_name = :areaName AND c_sh_name = :shortName " +
                " AND c_geo_city_code = :cityCode AND n_approval_flag = 0";
        Query query = this.getQuery(sql);
        query.setParameter("areaName", areaName);
        query.setParameter("shortName", shortName);
        query.setParameter("cityCode", cityCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    @Override
    public JsonObject editArea(JsonObject data) throws SimpleException {
        JsonObject responseJson = new JsonObject();
        String areaCode = helper.getString(data.get("c_code").getAsString());
        String areaName = helper.getString(data.get("c_name").getAsString());
        String shortName = helper.getString(data.get("c_sh_name").getAsString());
        String cityCode = helper.getString(data.get("c_geo_city_code").getAsString());
        String latitude = helper.getString(data.get("c_geo_lat").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lat").getAsString());
        String longitude = helper.getString(data.get("c_geo_lon").getAsString()).equals("") ? "-" : helper.getString(data.get("c_geo_lon").getAsString());
        String userName = helper.getString(data.get("c_createuser").getAsString());
        String msg = "";
        String field = "";
        if (areaCode.equals("")) {
            field = "c_code";
        } else if (areaName.equals("")) {
            field = "c_name";
        } else if (shortName.equals("")) {
            field = "c_sh_name";
        } else if (cityCode.equals("")) {
            field = "c_geo_city_code";
        } else if (userName.equals("")) {
            field = "c_createuser";
        }
        if (!field.equals("")) {
            msg = "Required Parameter! Parameter <" + field + "> cannot be null";
            throw new SimpleException(msg);
        }
        int exists = getAreaDataCountTemp(areaCode);
        int result = 0;
        String sql = "";
        if (exists > 0) {
             sql = "UPDATE u_geo_area_mst_temp SET c_name = :areaName, c_sh_name = :shortName, " +
                    " c_geo_city_code = :cityCode,c_geo_lat = :latitude,c_geo_lon = :longitude, " +
                    " d_ldate =  now(),t_ltime = now(),c_modiuser = :userName WHERE c_code = :areaCode and n_approval_flag = 0";
        } else{
            sql = "INSERT INTO u_geo_area_mst_temp (c_code,c_name,c_sh_name,c_geo_city_code,c_geo_lat, " +
                    "c_geo_lon,c_createuser,d_adate,d_ldate,t_ltime,c_modiuser) " +
                    "VALUES (:areaCode,:areaName,:shortName,:cityCode,:latitude,:longitude,:userName, " +
                    "now(),now(),now(),:userName)";
        }
            Query query = this.getQuery(sql);
            query.setParameter("areaCode", areaCode);
            query.setParameter("areaName", areaName);
            query.setParameter("shortName", shortName);
            query.setParameter("cityCode", cityCode);
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            query.setParameter("userName", userName);
            result = query.executeUpdate();
        responseJson.addProperty("result", result);
        return responseJson;
    }

    @Override
    public JsonObject getAreaDetails(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        String areaCode = helper.getString(data.get("c_area_code"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_city_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_area_mst WHERE c_code = :areaCode ";
        Query query = this.getQuery(sql);
        query.setParameter("areaCode", areaCode);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            int i = -1;
            responseJson.addProperty("c_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_sh_name", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_city_code", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lat", helper.getString(obj[++i]));
            responseJson.addProperty("c_geo_lon", helper.getString(obj[++i]));
            responseJson.addProperty("n_audited", helper.getString(obj[++i]));
            responseJson.addProperty("n_predefined", helper.getString(obj[++i]));
            responseJson.addProperty("c_createuser", helper.getString(obj[++i]));
            responseJson.addProperty("d_adate", helper.getString(obj[++i]));
            responseJson.addProperty("d_ldate", helper.getString(obj[++i]));
            responseJson.addProperty("t_ltime", helper.getString(obj[++i]));
            responseJson.addProperty("c_modiuser", helper.getString(obj[++i]));
        }
        return responseJson;
    }

    @Override
    public JsonObject getArea(JsonObject data) {
        JsonObject responseJson = new JsonObject();
        JsonArray elements = new JsonArray();
        String cityCode = helper.getString(data.get("c_city_code"));
        String searchValue = helper.getString(data.get("c_search_value")) != "" ? helper.getString(data.get("c_search_value"))+"%" : "";
        String sortOrder = helper.getString(data.get("c_sort")) != "" ? helper.getString(data.get("c_sort")) : "d_adate";
        String orderBy = helper.getString(data.get("c_order_by")) !="" ? helper.getString(data.get("c_order_by")) : "ASC";
        int nOffset = helper.getInt(data.get("n_offset"));
        int nLimit = helper.getInt(data.get("n_limit"));
        String sql = "SELECT c_code, c_name, c_sh_name, c_geo_city_code, c_geo_lat, c_geo_lon, " +
                " n_audited, n_predefined, c_createuser, d_adate, d_ldate, t_ltime, c_modiuser " +
                " FROM u_geo_area_mst where c_geo_city_code = :cityCode";
        if(!searchValue.equals("")){
            if(searchValue.length() >= 4) {
                sql += " and c_name LIKE :searchValue ";
            }else{
                sql += " and c_sh_name LIKE :searchValue";
            }
        }
        sql += " order by " + sortOrder + " " + orderBy;
        Query query = this.getQuery(sql);
        query.setParameter("searchValue", searchValue);
        query.setParameter("cityCode", cityCode);
        query.setFirstResult(nOffset);
        query.setMaxResults(nLimit);
        List<Object[]> resultList = query.getResultList();

        for (Object[] obj : resultList) {
            JsonObject object = new JsonObject();
            int i = -1;
            object.addProperty("c_code", helper.getString(obj[++i]));
            object.addProperty("c_name", helper.getString(obj[++i]));
            object.addProperty("c_sh_name", helper.getString(obj[++i]));
            object.addProperty("c_geo_city_code", helper.getString(obj[++i]));
            object.addProperty("c_geo_lat", helper.getString(obj[++i]));
            object.addProperty("c_geo_lon", helper.getString(obj[++i]));
            object.addProperty("n_audited", helper.getString(obj[++i]));
            object.addProperty("n_predefined", helper.getString(obj[++i]));
            object.addProperty("c_createuser", helper.getString(obj[++i]));
            object.addProperty("d_adate", helper.getString(obj[++i]));
            object.addProperty("d_ldate", helper.getString(obj[++i]));
            object.addProperty("t_ltime", helper.getString(obj[++i]));
            object.addProperty("c_modiuser", helper.getString(obj[++i]));
            elements.add(object);
        }
        if(elements.size() > 0){
            responseJson.add("areaList", elements);
        }
        return responseJson;
    }

    private int getAreaDataCountTemp(String areaCode) {
        String sql = "SELECT count(c_code) as count from u_geo_area_mst_temp where c_code = :areaCode and n_approval_flag = '0'";
        Query query = this.getQuery(sql);
        query.setParameter("areaCode", areaCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    private int getAreaDataCount(String areaName,String cityCode) {
        String sql = "SELECT count(c_code) as count from u_geo_area_mst where c_name = :areaName and c_geo_city_code = :cityCode";
        Query query = this.getQuery(sql);
        query.setParameter("areaName", areaName);
        query.setParameter("cityCode", cityCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    private int getDistrictDataCount(String districtName, String stateCode) {
        String sql = "SELECT count(c_code) as count from u_geo_district_mst where c_name = :districtName and c_geo_state_code = :stateCode";
        Query query = this.getQuery(sql);
        query.setParameter("districtName", districtName);
        query.setParameter("stateCode", stateCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    private int getStateDataCount(String stateCode) {
        String sql = "SELECT count(c_code) as count from u_geo_state_mst where c_code = :stateCode";
        Query query = this.getQuery(sql);
        query.setParameter("stateCode", stateCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }

    private int getCityDataCount(String cityName,String districtCode) {
        String sql = "SELECT count(c_code) as count from u_geo_city_mst where c_name = :cityName and c_geo_district_code = :districtCode";
        Query query = this.getQuery(sql);
        query.setParameter("cityName", cityName);
        query.setParameter("districtCode", districtCode);
        BigInteger count = (BigInteger) this.getSingleResult(query);
        return helper.getInt(count.intValue());
    }
}
