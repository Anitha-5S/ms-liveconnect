package com.c2.lc.ms.customer.controllers;

import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.*;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.customer.CombineCronTimeLogEntity;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.ScheduleDemoEntity;
import com.c2.lc.ms.customer.transactions.interfaces.FirmBranchTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.FirmTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.LcUserTransaction;
import com.c2.lc.ms.customer.transactions.interfaces.TouchStoreTransaction;
import com.c2.lc.ms.customer.utils.Utils;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/firm"})
@Slf4j
public class FirmController extends LoBaseController {

    @Autowired private FirmTransaction firmTransaction;
    @Autowired private LcUserTransaction lcUserTransaction;
    @Autowired private FirmBranchTransaction firmBranchTransaction;
    @Autowired private TouchStoreTransaction touchStoreTransaction;

    /**
     * API Id : 3.4.1
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
    @GetMapping(value = "/profile")
    public ResponseEntity<?> getProfile(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/firm/profile");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);
            log.debug("Headers : " + headers);

            FirmEntity firmEntity = firmTransaction.getFirmById(headerBO.getFirmId());
            String tsStatus = touchStoreTransaction.storeRegStatus(firmEntity.getC2Code());
            JsonObject result = Utils.getFirmEntityJsonObject(firmEntity);
            result.addProperty("c_c2code", firmEntity.getC2Code() == null ? "" : firmEntity.getC2Code());
            result.addProperty("c_ts_register_status", tsStatus);
            firmTransaction.getUpdatedStatus(firmEntity, result);

            this.setDataJsonObjectPayload(apiResponse, result);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/demo", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> scheduleDemo(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/firm/demo/ " + payload);

        try {
            ScheduleDemoEntity scheduleDemo = helper.fromJson(payload, ScheduleDemoEntity.class);
            this.validateInputPayload(scheduleDemo);

            if (!"PS".equals(scheduleDemo.getCProduct()) && !"EG".equals(scheduleDemo.getCProduct()) && !"PA".equals(scheduleDemo.getCProduct())) {
                throw new InvalidRequestException("", "Invalid product code!");
            }

            boolean check = firmTransaction.isScheduleExist(scheduleDemo.getCMobileNo(), scheduleDemo.getCProduct());
            if (check) {
                throw new DuplicateRecordException("Demo request has been already sent for this product!");
            } else {
                firmTransaction.saveScheduleDemo(scheduleDemo);
                this.addMessage(apiResponse, "Demo request has been sent successfully!");
            }

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.1.5
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By :
     */
/*
    @PostMapping(value = "/contact", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> updateContact(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/contact");
        try {
            LcHeaderBO headerBO = this.getLcHeader(headers);

            JsonObject json = helper.getJsonObject(payload);
            firmTransaction.updateContact(headerBO, json);

            this.addMessage(apiResponse, "Contact details are updated!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
*/

/*
    @GetMapping(value = "/contact", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> contactDetail(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("GET:${api.base.path}/firm/contact");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            ContactDetailEntity contactDetailEntity = firmTransaction.getFirmContact(header.getFirmId());

            this.setDataJsonObjectPayload(apiResponse, helper.getJsonObject(contactDetailEntity));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
*/

    @GetMapping(value = "/detail", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getFirm(@RequestHeader Map<String, String> headers) {
        ApiResponse apiResponse = this.initializeResponse("GET:${api.base.path}/firm/detail");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            FirmEntity firm = firmTransaction.getFirmById(header.getFirmId());

            this.setDataJsonObjectPayload(apiResponse, helper.getJsonObject(firm));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/detail", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> save(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/detail");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            BranchDetailsBO firm = helper.fromJson(payload, BranchDetailsBO.class);
            this.validateInputPayload(firm);

            FirmEntity firmEntity = firmTransaction.save(header, firm);
            LcUser lcUser = firmTransaction.getLcUserStatus(firmEntity.getCMobileNo());

            JsonObject obj = new JsonObject();
            obj.addProperty("c_store_combine_status", firmEntity.getStoreCombineStatus() == null ? Constants.STRING_VALUE_ZERO : firmEntity.getStoreCombineStatus());
            obj.addProperty("c_lc_user_status", lcUser.getLcUserStatus());
            obj.addProperty("c_lc_user_active_status", lcUser.getStatus());
            obj.addProperty("c_update_status", Constants.STRING_VALUE_ONE);
//            firmTransaction.getUpdatedStatus(firmEntity, obj);

            this.setDataJsonObjectPayload(apiResponse, obj);
            this.addMessage(apiResponse, "Save successful!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/check/gst", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> checkGst(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/check/gst");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            DataBO gstBo = helper.fromJSON(payload, DataBO.class);
            helper.validateDataLength(gstBo.getData(), "data", 10, 15);
           // FirmEntity firm = firmTransaction.checkGst(gstBo.getData());
            int count = firmTransaction.checkGst(gstBo.getData(),header.getUserId());
            if(count>0)
                throw new DuplicateRecordException("GST Number Already Exist!");
            else
                this.addMessage(apiResponse, "GST Number Not Exist!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/check/dl", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> checkDrugLicense(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/check/dl");
        try {
            LcHeaderBO header = this.getLcHeader(headers);
            DataBO dlBo = helper.fromJSON(payload, DataBO.class);
            helper.validateNotEmptyData(dlBo.getData(),"data");

            int count = firmTransaction.checkDrugLicense(dlBo.getData());
            if(count>0)
                throw new DuplicateRecordException("Drug License Exist!");
            else
                this.addMessage(apiResponse, "Drug License Not Exist!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/update/store", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateStore(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/store/update/store");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            StoreCombineBO store = helper.fromJson(payload, StoreCombineBO.class);
            this.validateInputPayload(store);

            firmTransaction.updateStore(store, header.getUserId());

            this.addMessage(apiResponse, "Updated Successfully!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/setdefaultbranch", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> setDefaultBranch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/c2/lc/ms/cust/firm/setdefaultbranch");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            JsonObject json = helper.getJsonObject(payload);

            if (json.has("c_br_code")) {
                String c_br_code_val = json.get("c_br_code").getAsString();
                if (c_br_code_val != null && !c_br_code_val.trim().isBlank()) {
                    Long branchCode = getBranchCode(payload);
                    Boolean result = firmBranchTransaction.isBranchExist(header.getUserId(), branchCode);
                    if (result) {
                        String deviceToken = json.get("c_device_token").getAsString();
                        EcoUsers ecoUsers = firmBranchTransaction.ecoUserUpdate(header, json.get("c_br_code").getAsString());
                        firmBranchTransaction.setDefaultBranch(header.getUserId(), branchCode);
                        JsonObject response = firmBranchTransaction.callC2Service(ecoUsers.getC2Code() ,header.getUserId(), ecoUsers.getBrCode(), Constants.ROLE_BUYER, deviceToken);

                        this.setDataJsonObjectPayload(apiResponse, response);
                    }
                    this.addMessage(apiResponse, "Default branch set successfully!");
                } else {
                    this.addMessage(apiResponse, "c_br_code can not be empty");
                }
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/getbranchlist", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getListOfBranch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/branch/getbranchlist");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            PageBO pageBo = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBo);

            List<BranchListBo> branchList = firmBranchTransaction.getListOfBranch(header, pageBo);

            BranchListResponseBO branchListResponseBO = new BranchListResponseBO();
            NextPageBO nextPageBO = new NextPageBO();
            branchListResponseBO.setList(branchList);
            nextPageBO.setPage(pageBo.getPage() + 1);
           // nextPageBO.setTotal(firmBranchTransaction.getCount(header.getUserId()));
            branchListResponseBO.setNextPage(nextPageBO);

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(branchListResponseBO, BranchListResponseBO.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/getbranchdetails", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBranchDetails(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/branch/getbranchdetails");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            Long branchCode = getBranchCode(payload);
            BranchDetailsBO branchDetailsBO = firmBranchTransaction.getBranchDetails(branchCode);

            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(branchDetailsBO, BranchDetailsBO.class));

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/addbranch", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> addBranch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/branch/addbranch");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            BranchDetailsBO branchDetailsBO = helper.fromJson(payload, BranchDetailsBO.class);
            this.validateInputPayload(branchDetailsBO);

            if(helper.isEmpty(branchDetailsBO.getFirmName())){
                throw new InvalidRequestException("c_firm_name", "can't be empty/required");
            }

            firmBranchTransaction.addBranch(header, branchDetailsBO);

            this.addMessage(apiResponse, "Created Successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/removebranch", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteBranch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/firm/removebranch");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            Long branchCode = getBranchCode(payload);
            firmBranchTransaction.deleteBranch(header, branchCode);

            this.addMessage(apiResponse, "Branch removed successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    private Long getBranchCode(String payload) throws InvalidRequestException {

        Long branchCode;
        JsonObject json = helper.getJsonObject(payload);
        if (json.has("c_br_code")) {
            String c_br_code_val = json.get("c_br_code").getAsString();
            try{
                Integer.parseInt(c_br_code_val);
            }catch(NumberFormatException e){
                throw new InvalidRequestException("c_br_code","'c_br_code' is not valid");
            }
            if(c_br_code_val != null && !c_br_code_val.trim().isBlank()){
                branchCode = json.get("c_br_code").getAsLong();
            }else{
                throw new InvalidRequestException("", "'c_br_code' cannot be empty!");
            }

        } else {
            throw new InvalidRequestException("", "'c_br_code' is mandatory");
        }
        return branchCode;
    }

    @PostMapping(value = "/update", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<?> updateBranch(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("/${api.base.path}/firm/update");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            BranchDetailsBO branchDetailsBO = helper.fromJson(payload, BranchDetailsBO.class);
            this.validateInputPayload(branchDetailsBO);

            firmBranchTransaction.updateBranch(header, helper.getLong(branchDetailsBO.getBranchCode()), branchDetailsBO);

            this.addMessage(apiResponse, "Updated Successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
    @PostMapping(value = "/save/uncombined/stores", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveUncombinedStores(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/save/uncombined/stores");
        try {
            LcHeaderBO header = this.getLcHeader(headers);

            StoreCombineRequestBO requestBO = helper.fromJson(payload, StoreCombineRequestBO.class);
            this.validateInputPayload(requestBO);

            firmTransaction.saveUncombinedStores(requestBO, header.getUserId());

            LcUser lcUser = lcUserTransaction.getLcUser(requestBO.getMobileNo());
            FirmEntity firm = firmTransaction.getDefaultFirm(lcUser.getNId());
            FirmEntity firmStatus = firmTransaction.getFirmById(firm.getNFirmId());

            JsonObject obj = new JsonObject();
            obj.addProperty("c_store_combine_status", firmStatus.getStoreCombineStatus() == null ? Constants.STRING_VALUE_ZERO : firmStatus.getStoreCombineStatus());
            obj.addProperty("c_lc_user_status", lcUser.getLcUserStatus());
            obj.addProperty("c_lc_user_active_status", lcUser.getStatus());
            obj.addProperty("c_update_status", Constants.STRING_VALUE_ONE);

            this.setDataJsonObjectPayload(apiResponse, obj);

            this.addMessage(apiResponse, "Stores imported to LO2 successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/combine/store", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> combineStores(@RequestHeader Map<String, String> headers, @RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/combine/store");
        try {
            log.info("Cron Started at "+ helper.getCurrentTime());
            LcHeaderBO header = this.getLcHeader(headers);

            JsonObject request = helper.fromJson(payload, JsonObject.class);

            firmTransaction.combine(request, header);
            log.info("Cron Completed at "+ helper.getCurrentTime());
            this.addMessage(apiResponse, "Stores combined successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }
}

