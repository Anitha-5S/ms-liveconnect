package com.c2.lc.ms.master.services.base;

import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.properties.AppMessages;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.services.BaseDBServiceImpl;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.master.services.interfaces.base.MasterBaseService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class MasterBaseServiceImpl extends BaseDBServiceImpl implements MasterBaseService {

    private static Map<String, EntityManager> connections = null;
    private static Properties properties = null;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    protected int numRows;

    protected Query getQuery(String c2Code, String sql) {
        return this.getQuery(sql);
    }

    protected JsonObject eliminateNullFromJson(String data) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(gson.fromJson(data, JsonObject.class)), JsonObject.class);
    }

    protected JsonArray eliminateNullFromJsonArray(String data) {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(gson.fromJson(data, JsonArray.class)), JsonArray.class);
    }

    protected double decimalFormatter(double number) {
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        return Double.parseDouble(numberFormat.format(number));
    }


    protected String getDeleteQuery(String tableName, JsonObject data) {
        return " DELETE FROM " + tableName + " WHERE " + getDeleteWhereClause(tableName, data) + ";";
    }

    protected String getDeleteWhereClause(String tableName, JsonObject data) {
        StringBuilder condition = new StringBuilder();
        for (String key : data.keySet()) {
            condition.append(tableName + "." + key + " = " + ":" + key + " and ");
        }
        return condition.substring(0, condition.length() - 4);
    }

    protected String addMonthsToDate(String date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.valueOf(date));
        cal.add(Calendar.MONTH, months);
        return dateFormat.format(cal.getTime());
    }

    protected Query getQueryWithPk(String c2Code, String sql, JsonObject data) {
        Query query = getQuery(c2Code, sql);
        query.setParameter("c_br_code", data.get("c_br_code").getAsString());
        query.setParameter("c_year", data.get("c_year").getAsString());
        query.setParameter("c_prefix", data.get("c_prefix").getAsString());
        query.setParameter("n_srno", data.get("n_srno").getAsLong());
        return query;
    }

    protected void tableInsert(String c2Code, String tableName, JsonObject insertQueryJson){
        numRows = executeSQL(c2Code, getInsertQuery(tableName, insertQueryJson), insertQueryJson);
        log.debug("INSERTED INTO {} : {} ", tableName, numRows);
    }

    protected void tableInsertUpdate(String c2Code, String tableName, JsonObject insertJson){
        numRows = executeSQL(c2Code,this.getInsertUpdateQuery(tableName,insertJson),insertJson);
        log.debug("INSERT OR ON EXISTING VALUES UPDATE {} : {}", tableName, numRows);
    }

    protected void tableInsertSkip(String c2Code, String tableName, JsonObject insertJson){
        numRows = executeSQL(c2Code,this.getInsertSkipQuery(tableName,insertJson),insertJson);
        log.debug("INSERT OR ON EXISTING VALUES SKIP {} : {}", tableName, numRows);
    }

    protected int getCount(String c2Code, String tableName, JsonObject data, JsonObject key) {
        String sql = " SELECT COUNT(1) " +
                " FROM " + tableName +
                " WHERE " + getParamString(key);
        Query query = getQueryWithParams(c2Code, sql, key);
        return (int) this.getSingleResult(query);
    }

    protected Long getMasterCount(String columnName, String tableName, JsonObject key) {
        String sql = " SELECT COUNT("+columnName+") " +
                " FROM " + tableName +
                " WHERE " + getParamString(key) +
                " and uim.c_code NOT IN (select c_item_code from  lc_blocked_items) " ;
        Query query = getQueryWithParams("c2Code", sql, key);
        BigInteger bigInteger = (BigInteger) this.getSingleResult(query);
        return  bigInteger.longValue();
    }

    protected int executeDeleteQuery(String c2Code, String tableName, JsonObject key) {
        String sql = " DELETE FROM " + tableName + " WHERE " + getParamString(key);
        Query query = getQueryWithParams(c2Code, sql, key);
        numRows = executeUpdate(query);
        log.debug("DELETED {} : {}", tableName, numRows);
        return numRows;
    }
    //////////////// ONLY ABOVE SHOULD BE USED IN SERVICE LAYER //////////

    protected int executeUpdate(Query query) {
        numRows = query.executeUpdate();
        return numRows;
    }

    protected int executeSQL(String c2Code, String sql, JsonObject data) {
        return setParametersInQueryAndExecute(c2Code, sql, data);
    }

    private int setParametersInQueryAndExecute(String c2Code, String sql, JsonObject data) {
        Query query = this.getQuery(c2Code, sql);
        if (data != null && data.size() > 0) {
            for (String key : data.keySet()) {
                query.setParameter(key, (data.get(key) == null ? null : data.get(key).getAsString()));
            }
        }
        return executeUpdate(query);
    }

    private Query getQueryWithParams(String c2Code, String sql, JsonObject data) {
        Query query = getQuery(c2Code, sql);
        data.entrySet().parallelStream().forEach(entry -> {
            query.setParameter(entry.getKey(), entry.getValue().getAsString());
        });
        return query;
    }

    private String getParamString(JsonObject data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            sb.append(key).append(" = :").append(key).append(" AND ");
        }
        String s = sb.toString();
        return s.substring(0, s.length() - 4);
    }

    private String getInsertFields(JsonObject data, String delimiter) {
        StringBuilder sqlString = new StringBuilder();
        for (String key : data.keySet()) {
            sqlString.append(delimiter).append(key).append(", ");
        }
        return sqlString.substring(0, sqlString.length() - 2);
    }


    protected String getInsertQuery(String tableName, JsonObject data) {
        return "INSERT INTO " + tableName + " ( " + getInsertFields(data, "") + " ) VALUES ( " + getInsertFields(data, ":") + " );";
    }

    protected String getInsertUpdateQuery(String tableName, JsonObject data) {
        return "INSERT INTO " + tableName + " ( " + getInsertFields(data, "") + " ) ON EXISTING UPDATE DEFAULTS OFF VALUES ( " + getInsertFields(data, ":") + " );";
    }

    protected String getInsertSkipQuery(String tableName, JsonObject data) {
        return "INSERT INTO " + tableName + " ( " + getInsertFields(data, "") + " ) ON EXISTING SKIP VALUES ( " + getInsertFields(data, ":") + " );";
    }

    protected JsonObject getPrimaryKey(JsonObject data) {
        JsonObject key = new JsonObject();
        key.addProperty("c_br_code", data.get("c_br_code").getAsString());
        key.addProperty("c_year", data.get("c_year").getAsString());
        key.addProperty("c_prefix", data.get("c_prefix").getAsString());
        key.addProperty("n_srno", data.get("n_srno").getAsLong());
        return key;
    }

    protected List<Object[]> getSelectValues(String c2Code, JsonObject data, String tableName, List<String> columns, int page, int size) throws InputPayloadException {
        String where = Constants.EMPTY_STRING;
        String groupBy = Constants.EMPTY_STRING;
        String orderBy = Constants.EMPTY_STRING;
        String sql;
        JsonObject whereClauseObj = null;
        JsonObject whereParamsObj = null;

        if(data.has("where")){
            whereClauseObj = data.get("where").getAsJsonObject();
            if(whereClauseObj.has("clause") && whereClauseObj.has("params")) {
                if (helper.isEmpty(whereClauseObj.get("clause"))) {
                    throw new InputPayloadException("clause", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
                }
                where = " WHERE " + whereClauseObj.get("clause").getAsString();

                if (helper.isEmpty(whereClauseObj.get("params"))) {
                    throw new InputPayloadException("params", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
                }
                whereParamsObj = whereClauseObj.get("params").getAsJsonObject();
            } else {
                if (helper.isEmpty(data.get("where"))) {
                    throw new InputPayloadException("where", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
                }
            }
        }

        if(data.has("group_by")){
            if (helper.isEmpty(data.get("group_by"))) {
                throw new InputPayloadException("group_by", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
            }
            groupBy = " GROUP BY " + data.get("group_by").getAsString();
        }

        if(data.has("order_by")){
            if (helper.isEmpty(data.get("order_by"))) {
                throw new InputPayloadException("order_by", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
            }
            orderBy = " ORDER BY " + data.get("order_by").getAsString();
        }

        sql = "SELECT " + String.join(",", columns) + " FROM " + tableName + where + groupBy + orderBy;

        Query query = getQuery(c2Code, sql);
        if (!helper.isEmpty(where)) {
            this.setQueryParameters(query, whereParamsObj);
        }
        return getResultList(query, page, size);
    }

    protected Date getMaxDate(String c2Code, JsonObject data, String tableName, String column) throws InputPayloadException {
        String where = Constants.EMPTY_STRING;
        String sql;
        JsonObject whereClauseObj;
        JsonObject whereParamsObj = new JsonObject();

        if(data.has("where")){
            whereClauseObj = data.get("where").getAsJsonObject();
            if(whereClauseObj.has("clause") && whereClauseObj.has("params")) {
                if (helper.isEmpty(whereClauseObj.get("clause"))) {
                    throw new InputPayloadException("clause", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
                }
                where = " WHERE " + whereClauseObj.get("clause").getAsString();

                if (helper.isEmpty(whereClauseObj.get("params"))) {
                    throw new InputPayloadException("params", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
                }
                whereParamsObj = whereClauseObj.get("params").getAsJsonObject();
            } else {
                if (helper.isEmpty(data.get("where"))) {
                    throw new InputPayloadException("where", AppMessages.getPropertyValue(Messages.INPUT_PAYLOAD_CANNOT_BE_NULL));
                }
            }
        }

        sql = "SELECT MAX(" + column + ") FROM " + tableName + where;

        Query query = getQuery(c2Code, sql);
        if (!helper.isEmpty(where)) {
            this.setQueryParameters(query, whereParamsObj);
        }
        String dt = this.getSingleResultNull(query);
        return dt == null ? null : Date.valueOf(dt) ;
    }

    protected List<String> getColumnNames(String c2Code, String tableName) {
        List<String> ret = new ArrayList<>();
        Query query = getQuery(c2Code, "SELECT column_name FROM syscolumn JOIN systable WHERE table_name = :tableName");
        query.setParameter("tableName", tableName);
        List<Object[]> resultList = getResultList(query);
        for (Object obj : resultList) {
            ret.add(obj.toString());
        }
        return ret;
    }

    protected JsonArray writeResultSetToJson(List<String> columns, List<Object[]> values) {
        JsonArray result = new JsonArray();
        for (Object [] row : values) {
            JsonObject set = getRow(columns, row);
            result.add(set);
        }
        return result;
    }

    private JsonObject getRow(List<String> columns, Object[] row) {
        JsonObject set = new JsonObject();
        int i = 0;
        for (String col : columns) {
            Object o = row[i++];
            if (o != null) {
                String value = o.toString();
                if (col.startsWith("n_")) {
                    if (value.contains(".")) {
                        set.addProperty(col, Double.parseDouble(value));
                    } else {
                        set.addProperty(col, Integer.parseInt(value));
                    }
                } else {
                    set.addProperty(col, value);
                }
            }
        }
        return set;
    }

    protected void checkAndAdd(JsonObject srcJson, String field, JsonObject destJson) {
        if (srcJson.has(field)) {
            destJson.add(field, srcJson.get(field));
        }
    }

    protected void checkAndAdd(JsonObject srcJson, String srcField, JsonObject destJson, String destField) {
        if (srcJson.has(srcField)) {
            destJson.add(destField, srcJson.get(srcField));
        }
    }

    protected void ignoreEmptyDate(JsonObject srcJson, String srcField, JsonObject destJson, String destField) {
        if (srcJson.has(srcField)) {
            String srcValue = srcJson.get(srcField).getAsString().trim();
            if (srcValue.length() > 0) {
                destJson.add(destField, srcJson.get(srcField));
            }
        }
    }

    protected String addDaysToDate(String date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(Date.valueOf(date));
        c.add(Calendar.DATE, days);
        return dateFormat.format(c.getTime());
    }

    protected String getDate(JsonElement obj) {
        String ret = null;
        if (obj != null) {
            ret = obj.getAsString();
        }
        return ret;
    }

    protected boolean isNotEqualToZero(Double d) {
        return d != 0.0;
    }

    protected boolean isNotEqualToZero(int i) {
        return i != 0;
    }

    protected boolean isNotEqualToZero(String s) {
        return !s.equals("0");
    }

    protected String getStringValue(String str) {
        return "'" + str + "'";
    }

    protected String getCurrentTime(){
        LocalTime time = LocalTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
        return dtf.format(time);
    }

    public HostnameVerifier skipSslVerification() throws Exception {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return allHostsValid;
    }

    public JsonObject lcApiCall(String uri, String apiVer, String contentType, String payload) throws Exception {
        URL url = new URL(uri);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", contentType);
        con.setRequestProperty("api_ver", apiVer);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(payload.getBytes("UTF-8"));
        os.close();
        boolean isError = (con.getResponseCode() >= 400);
        BufferedReader bufferedReader = isError ? new BufferedReader(new InputStreamReader(con.getErrorStream()))
                : new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine, output = "";
        while ((inputLine = bufferedReader.readLine()) != null) {
            output = output + inputLine;
        }
        bufferedReader.close();
        JsonObject res = (JsonObject) this.helper.fromJson(output, JsonObject.class);
        return res;
    }

    protected String getSubAdmin(JsonObject data){
        String ret ="000";
        String subAdmin = helper.getString(data.get("c_admin_br_code"));
        if(!helper.isEmpty(subAdmin)){
            ret=subAdmin;
        }
        return ret;
    }

}
