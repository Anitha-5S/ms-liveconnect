package com.c2.lc.ms.master.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.ms.master.controllers.base.MasterBaseController;
import com.c2.lc.ms.master.transactions.interfaces.AdminTransaction;
import com.c2.lc.ms.master.transactions.interfaces.NetmedsItemPushTransaction;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/adm"})
public class AdminController extends MasterBaseController {

    @Value("${api.base.path}")
    private String basePath;
    @Autowired
    private AdminTransaction adminTransaction;

    /**
     * API Id : 2.0.1
     * Api For: Add State
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/addState", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> addState(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath+"/adm/addState"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.addState(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to create state";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.0.2
     * Api For: Edit State
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/editState", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> editState(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/editState"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.editState(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to update";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.0.3
     * Api For: View State Details
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/stateDetails", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getStateDetails(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/stateDetails"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getStateDetails(data);
            String msg = result.size() > 0 ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.0.4
     * Api For: State List
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/state", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getState(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/state"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getState(data);
            String msg = result.has("stateList") ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.1.1
     * Api For: Add District
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/addDistrict", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> addDistrict(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/addDistrict"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.addDistrict(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to create district";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.1.2
     * Api For: Edit District
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/editDistrict", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> editDistrict(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/editDistrict"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.editDistrict(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to update";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.1.3
     * Api For: View District Details
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/districtDetails", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getDistrictDetails(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/districtDetails"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getDistrictDetails(data);
            String msg = result.size() > 0 ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.1.4
     * Api For: District List
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/district", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getDistrict(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/district"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getDistrict(data);
            String msg = result.has("districtList") ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.1
     * Api For: Add City
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/addCity", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> addCity(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/addCity"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.addCity(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to create city";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.2
     * Api For: Edit City
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/editCity", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> editCity(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/editCity"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.editCity(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to update";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.3
     * Api For: View City Details
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/cityDetails", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getCityDetails(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/cityDetails"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getCityDetails(data);
            String msg = result.size() > 0 ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.4
     * Api For: City List
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/city", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getCity(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/city"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getCity(data);
            String msg = result.has("cityList") ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.3.1
     * Api For: Add Area
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/addArea", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> addArea(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/addArea"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.addArea(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to create area";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.2
     * Api For: Edit Area
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/editArea", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> editArea(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/editArea"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.editArea(data);
            String msg = result.get("result").getAsInt()!=0 ? "Success" : "Failed to update";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, new JsonObject());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.3
     * Api For: View Area Details
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/areaDetails", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getAreaDetails(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/areaDetails"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getAreaDetails(data);
            String msg = result.size() > 0 ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 2.2.4
     * Api For: Area List
     * Developer : eby.p@c2info.com
     * Reviewed By :
     */
    @PostMapping(path = "/area", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse> getArea(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse(basePath +"/adm/area"+" ->"+headers.toString()+" ->"+payload);
        try {
            JsonObject data = helper.getJsonObject(payload);
            JsonObject result = adminTransaction.getArea(data);
            String msg = result.has("areaList") ? "Success" : "Record Not Found";
            this.addMessage(apiResponse, msg);
            this.setJsonPayload(apiResponse, result);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}
