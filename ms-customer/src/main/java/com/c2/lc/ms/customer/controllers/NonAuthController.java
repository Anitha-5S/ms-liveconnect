package com.c2.lc.ms.customer.controllers;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventProcessorClient;
import com.azure.messaging.eventhubs.EventProcessorClientBuilder;
import com.c2.lc.lib.api.ApiResponse;
import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.controller.LoBaseController;
import com.c2.lc.lib.exceptions.DuplicateRecordException;
import com.c2.lc.lib.exceptions.InputPayloadException;
import com.c2.lc.lib.exceptions.InvalidRequestException;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.configs.SampleCheckpointStore;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import com.c2.lc.ms.customer.entities.comm.LcUserType;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.transactions.interfaces.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = {"${api.base.path}/na"})
public class NonAuthController extends LoBaseController {

    @Autowired private FirmTransaction firmTransaction;
    @Autowired private LcUserTransaction lcUserTransaction;
    @Autowired private TouchStoreTransaction touchStoreTransaction;
    @Autowired private SellerTransaction sellerTransaction;


    @Value("${seller.new.launch.days}")
    private Long sellerNewLaunchDays;

    @Value("${web.base.url}") private String webUrl;

//    @Value("${stock.eventhub.connection.string}")
//    private String connectionString;
//
//    @Value("${stock.eventhub.name}")
//    private String eventhubName;

    /**
     * API Id : 3.1.1
     * Developer : deepanraj.elumalai@c2info.com
     * Reviewed By : selva.sk@c2info.com 2021-07-19 18 10
     */
    @PostMapping(value = "/check", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> doesMobileNumberExist(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/check/ " + payload);

        try {
            MobileCheckBO mobileCheckBO = helper.fromJson(payload, MobileCheckBO.class);
            this.validateInputPayload(mobileCheckBO);

            boolean check = false;
            JsonObject obj = new JsonObject();
            JsonArray array = new JsonArray();

            if (helper.isEmpty(mobileCheckBO.getCType())) {
                List<LcUserType> lcUserTypes = firmTransaction.doesMobileNumberExist(mobileCheckBO.getCMobileNo());
                if (lcUserTypes != null && lcUserTypes.size() > 0) {
                    check = true;

                    JsonArray list = (JsonArray) helper.getGson().toJsonTree(lcUserTypes,
                            new TypeToken<List<LcUserType>>() {
                            }.getType());
                    this.setDataJsonArrayPayload(apiResponse, list);
                }
            } else {
                check = firmTransaction.doesMobileNumberWithTypeExist(mobileCheckBO.getCMobileNo(), mobileCheckBO.getCType());
            }

            if (check) {
               // firmTransaction.importNewlyAddedStore(mobileCheckBO.getCMobileNo());
                throw new DuplicateRecordException("", "Already registered!"); //enter pwd
            } else if (firmTransaction.checkUserExistInLC(mobileCheckBO.getCMobileNo())) {
               // firmTransaction.saveStore(mobileCheckBO.getCMobileNo());
                obj.addProperty("c_mobile_no", mobileCheckBO.getCMobileNo());
                obj.addProperty("c_type", mobileCheckBO.getCType());
                obj.addProperty("user_exist_in_lc1", Constants.STRING_VALUE_ONE);
                array.add(obj);
                this.setDataJsonArrayPayload(apiResponse, array);
                throw new DuplicateRecordException("", "User does exist in LC!"); //call combine
            } else {
                obj.addProperty("c_mobile_no", "");
                obj.addProperty("c_type", "");
                obj.addProperty("user_exist_in_lc1", Constants.STRING_VALUE_ZERO);
                array.add(obj);
                this.setDataJsonArrayPayload(apiResponse, array);
                throw new RecordNotFoundException("", "User does not exist in LO!"); //register
            }

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.1.4
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By : selva.sk@c2info.com 2021-08-24 14:10
     */
    @PostMapping(value = "/register", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> registerProfile(@RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/register");
        try {
            JsonObject json = helper.getJsonObject(payload);

            LcUser lcUser = helper.fromJson(json, LcUser.class);
            lcUser.setStatus(Constants.STATUS_ACTIVE);
            this.validateInputPayload(lcUser);
            if(helper.isEmpty(lcUser.getPassword())){
                throw new InvalidRequestException("c_pwd","'c_pwd' can not be empty");
            }

            FirmEntity firm = helper.fromJson(json, FirmEntity.class);
            this.validateInputPayload(firm);
            if (firm.getCType().length() > 1) {
                throw new InvalidRequestException("c_type", "value is not valid");
            }
            if (firm.getCPin().length() != 6)
                throw new InvalidRequestException("c_pinCode", "Should be in length of 6 digits");

            String deviceToken = "E";
            JsonObject ret = firmTransaction.registerFirm(lcUser, firm, null, json.get("c_pwd").getAsString(), deviceToken);

            this.setDataJsonObjectPayload(apiResponse, ret);

            this.addMessage(apiResponse, "Registered Successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * API Id : 3.1.6
     * Developer : srutiarya.panda@c2info.com
     * Reviewed By : selva.sk@c2info.com 2021-08-24 16:00
     */
    @PostMapping(value = "/login", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> login(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/login");
        try {
            LoginRequest loginRequest = helper.fromJson(payload, LoginRequest.class);
            this.validateInputPayload(loginRequest);

            LcUser lcUser = lcUserTransaction.getLcUser(loginRequest.getMobileNumber());
            FirmEntity firm = firmTransaction.getDefaultFirm(lcUser.getNId());
            FirmEntity firmStatus = firmTransaction.getFirmById(firm.getNFirmId());

            String c2Code = firm.getC2Code() == null ? "L" + firm.getNFirmId() : (firm.getC2Code().equals("") ? "L" + firm.getNFirmId() : firm.getC2Code());
            String brCode = "000";
            String terminalId = helper.getString(lcUser.getNId());
            String type = lcUser.getType();
            String deviceToken = loginRequest.getDeviceToken();
            int loginCount = lcUserTransaction.getLoginCount(c2Code, brCode, terminalId, type);
            log.info("Current Login Session count for MobileNumber : " + loginRequest.getMobileNumber() + " and Count : " + loginCount);
//            if (loginCount >= 3) {
//                throw new InvalidRequestException("Reached Maximum Login at a time!."," Maximum count is : 3");
//            }

            JsonObject keyValue = firmTransaction.login(lcUser.getNId(), firm, loginRequest.getPassword(), lcUser.getLcUserStatus(), firmStatus, lcUser.getStatus(), deviceToken);

            this.setDataJsonObjectPayload(apiResponse, keyValue);
            this.addMessage(apiResponse, "Logged Successfully!");
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    // Not used in application.. for testing purpose only
    @PostMapping(value = "/delete", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteUser(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/user/delete");
        try {
//            if (!"https://prod-lc-web.livc.in".equals(webUrl)) {
                JsonObject json = helper.getJsonObject(payload);

                firmTransaction.delete(json.get("c_mobile").getAsString());
                //lcUserTransaction.delete(json.get("c_mobile").getAsString());

                this.addMessage(apiResponse, "User Deleted Successfully!");
//            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    //TODO move to authentication controller
    @PostMapping(value = "/branch/address", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> orderDeliveryAddress(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/branch/address");
        try {
            JsonObject json = helper.fromJson(payload, JsonObject.class);

            long deliveryId = json.get("c_delivery_branch_id").getAsLong();
            long branchId = json.get("c_order_branch_id").getAsLong();
            List<ContactDetailEntity> list = firmTransaction.getAddress(deliveryId, branchId);

            JsonArray ret = helper.toJsonArrayTree(list, new TypeToken<List<ContactDetailEntity>>() {
            }.getType());
            this.setDataJsonArrayPayload(apiResponse, ret);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/address", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> address(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/address");
        try {
            JsonObject json = helper.fromJson(payload, JsonObject.class);
            String pincode = "";
            if(json.has("c_pincode")) {
                pincode = json.get("c_pincode").getAsString();
                String regex
                        = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(pincode);
                if (!m.matches()){
                    throw new InvalidRequestException("'c_pincode' Invalid format", Messages.INVALID_REQUEST);
                }
                if (helper.isEmpty(pincode)) {
                    throw new InvalidRequestException("'c_pincode' can't be empty", Messages.INVALID_REQUEST);
                }
            }
            else
                throw new InvalidRequestException("'c_pincode' can't be empty", Messages.INVALID_REQUEST);
            AddressModelBO addressModelBO = firmTransaction.getAddressService(pincode);

            this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(addressModelBO, AddressModelBO.class));
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/demo", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> scheduleDemo(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/demo/ " + payload);

        try {
            ScheduleDemoEntity scheduleDemo = helper.fromJson(payload, ScheduleDemoEntity.class);
            if(helper.isEmpty(scheduleDemo.getCMobileNo())){
                throw new InvalidRequestException("c_mobile_no","'c_mobile_no' can't be empty");
            }
            this.validateInputPayload(scheduleDemo);

            if ( !"PS".equals(scheduleDemo.getCProduct()) && !"EG".equals(scheduleDemo.getCProduct())
                    && !"PA".equals(scheduleDemo.getCProduct()) && !"LO".equals(scheduleDemo.getCProduct())) {
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

    @PostMapping(value = "/store/combine/list", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getStoreCombine(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/store/combine/list");
        try {

            JsonObject json = helper.getJsonObject(payload);
            JsonObject store = firmTransaction.combineList(json.get("c_mobile_no").getAsString());

            this.setDataJsonObjectPayload(apiResponse, store);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/combine/store", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> combineStores(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/na/store/combine/store");
        try {

            StoreCombineRequestBO requestBO = helper.fromJson(payload, StoreCombineRequestBO.class);
            this.validateInputPayload(requestBO);

            CombineFirmAndRegisterBO results = firmTransaction.combineStores(requestBO);

            StoreCombineBO storeResult = firmTransaction.getStoreDetail(results.getFirmEntity());
            CombinedStoreResultBO combinedStoreResultBO = new CombinedStoreResultBO();
            combinedStoreResultBO.setStoreCombineBO(storeResult);
            combinedStoreResultBO.setRegObj(results.getRegObj());

            this.setJsonPayload(apiResponse, helper.toJsonObjectTree(combinedStoreResultBO, CombinedStoreResultBO.class));
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/save/uncombined/stores", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> saveUncombinedStores(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/na/save/uncombined/stores");
        try {
            StoreCombineRequestBO requestBO = helper.fromJson(payload, StoreCombineRequestBO.class);
            this.validateInputPayload(requestBO);

            CombineFirmAndRegisterBO result = firmTransaction.saveUncombinedStoresNA(requestBO);

            LcUser lcUser = lcUserTransaction.getLcUser(requestBO.getMobileNo());
            FirmEntity firm = firmTransaction.getDefaultFirm(lcUser.getNId());
            FirmEntity firmStatus = firmTransaction.getFirmById(firm.getNFirmId());

            JsonObject obj = new JsonObject();
            obj.addProperty("c_store_combine_status", firmStatus.getStoreCombineStatus() == null ? Constants.STRING_VALUE_ZERO : firmStatus.getStoreCombineStatus());
            obj.addProperty("c_lc_user_status", lcUser.getLcUserStatus());
            obj.addProperty("c_lc_user_active_status", lcUser.getStatus());
            obj.addProperty("c_update_status", Constants.STRING_VALUE_ONE);
            obj.add("j_register", result.getRegObj());

            this.setDataJsonObjectPayload(apiResponse, obj);

            this.addMessage(apiResponse, "Stores imported to LO2 successfully!");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    // Not used in application.. for testing purpose only
    @PostMapping(value = "/delete/lc1", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> deleteLc1Temp(@RequestHeader Map<String, String> headers, @RequestBody Object payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/user/delete/lc1");
        try {
            if (!"https://prod-lc-web.livc.in".equals(webUrl)) {
                JsonObject json = helper.getJsonObject(payload);

                firmTransaction.deleteLc1(json.get("c_mobile").getAsString());

                this.addMessage(apiResponse, "LC1 Data cleared Successfully!");
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/search/lc1", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> searchLc1(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/na/search/lc1");
        try {
            JsonObject request = helper.getJsonObject(payload);

            if (request.get("c_column_name") != null) {
                JsonObject result = new JsonObject();
                String colName = request.get("c_column_name").getAsString();
                String searchKey = request.get("c_search_key").getAsString();
                int page = request.get("n_page").getAsInt();
                int limit = request.get("n_limit").getAsInt();
                JsonArray res = firmTransaction.searchLc1(colName, searchKey, page, limit);

                result.add("j_item_list", res);
                result.addProperty("n_next_offset", page+1);
                result.addProperty("n_total", firmTransaction.getLc1SearchCount(colName, searchKey));

                this.setJsonPayload(apiResponse, result);
            } else {
                throw new InvalidRequestException("","c_column_name is null!");
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     *   TOUCH STORE : Mobile Number Check API
     */
    @PostMapping(value = "/b2c/check", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> doesMobileNumberExistInTS(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/ts/check/ " + payload);

        try {
            JsonObject mobileCheck = helper.fromJson(payload, JsonObject.class);
            if(mobileCheck.has("c_c2code")){
            if(mobileCheck.get("c_c2code").getAsString().isBlank()){
                throw new InvalidRequestException("c_c2code", "c_c2code can't be blank");
            }
            }else{
                throw new InvalidRequestException("c_c2code", "c_c2code is required");
            }

            if (mobileCheck.has("c_mobile_no")) {
                int c_mobile_length = mobileCheck.get("c_mobile_no").getAsString().length();
                if(c_mobile_length == 0){
                    throw new InvalidRequestException("c_mobile_no", "c_mobile_no can't be blank");
                }
                else if (c_mobile_length < 10) {
                    throw new InvalidRequestException(mobileCheck.get("c_mobile_no").getAsString(), "c_mobile_no should be 10 digits");
                } else {
                    boolean result = lcUserTransaction.checkTSUserExist(mobileCheck.get("c_mobile_no").getAsString(), mobileCheck.get("c_c2code").getAsString(), Constants.ROLE_CUSTOMER);
                    if (result) {
                        this.addMessage(apiResponse, "Already Registered!");
                    } else {
                        throw new RecordNotFoundException("Record not found!");
                    }
                }
            }else{
                throw new InvalidRequestException("c_mobile_no", "c_mobile_no is required");
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     *   TOUCH STORE : REGISTER API
     */
    @PostMapping(value = "/b2c/register", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> registerForTS(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/signup");

        try {
            TSRegisterBO registerBO = helper.fromJson(payload, TSRegisterBO.class);
            this.validateInputPayload(registerBO);

            if (registerBO.getBrCode() != null && helper.isEmpty(registerBO.getBrCode())) {
                throw new InputPayloadException("'c_br_code' should not be empty!");
            }

            JsonObject response = firmTransaction.tsRegister(registerBO);

            this.setDataJsonObjectPayload(apiResponse, response);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/combine", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> combine(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/combine");

        try {
            log.info("LC user data import started at "+ helper.getCurrentTime());
            JsonObject json = helper.getJsonObject(payload);
            String message = firmTransaction.saveStore(json.get("c_mobile_no").getAsString());

            log.info("LC user data import Completed at "+ helper.getCurrentTime());
            this.addMessage(apiResponse, message);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/b2c/get/setting", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSettingDetail(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/b2c/get/setting");
        try {
            JsonObject json = helper.getJsonObject(payload);

            if (json.has("c_application_id") && !helper.isEmpty(json.get("c_application_id").getAsString())) {
                String applicationId = json.get("c_application_id").getAsString();
                TSSettingDetailEntity result = touchStoreTransaction.getSettingDetailForCustomer(applicationId);
                this.setDataJsonObjectPayload(apiResponse, helper.toJsonObjectTree(result, TSSettingDetailEntity.class));
            } else {
                this.addMessage(apiResponse, "'c_application_id' should not be empty!");
            }
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     *   ITEM MAPPING LOGIN - mobile number
     */

    @PostMapping(value = "/mobile", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getMobileNo(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/store/combine/list");
        try {
            JsonObject json = helper.getJsonObject(payload);
            this.validateInputPayload(json);
            JsonObject data = firmTransaction.getMobileNumber(json.get("c_mid").getAsString());
            this.setDataJsonObjectPayload(apiResponse, data);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @Scheduled(cron = "0 0 2 * * ?")
    @GetMapping(value = "/sellerNew", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getSellerNewLaunch() {
        ApiResponse apiResponse = this.initializeResponse("/na/sellerNew");

        try {
            //   LcHeaderBO headerBO = this.getLcHeader(headers);
           // System.out.println("Scheduler running time "+ LocalDateTime.now() );

            log.debug("New Launch Notification cron Started at "+ LocalDateTime.now());
            sellerTransaction.getSellerNewLaunched(sellerNewLaunchDays);
            log.debug("New Launch Notification cron Completed at "+ LocalDateTime.now());
            this.addMessage(apiResponse, "Success");

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/stockNotification", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8, consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> stockNotification(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/na/stockNotification");
        try {
            JsonArray json = helper.fromJson(payload, JsonArray.class);
            sellerTransaction.stockNotification(json);
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TS API ID : 1.14.1
     * Developer : sathya.narayan@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/branch/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBranchList(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/branch/list");
        try {
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);
            List<JsonObject> list = firmTransaction.fetchBranchList(jsonObject.get("c_c2code").getAsString(), searchBO);

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /**
     * TS API ID : 1.14.2
     * Developer : sathya.narayan@c2info.com
     * Reviewed By :
     */
    @PostMapping(value = "/pincode/branch/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> getBranchByPinCode(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/pincode/branch/list");
        try {
            PageBO pageBO = helper.fromJson(payload, PageBO.class);
            this.validateInputPayload(pageBO);

            JsonObject jsonObject = helper.fromJson(payload, JsonObject.class);

            List<JsonObject> list = firmTransaction.fetchBranchListByPinCode(jsonObject.get("c_c2code").getAsString(), jsonObject.get("c_pincode"), pageBO);

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", pageBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @PostMapping(value = "/service/pincode/list", consumes = Constants.APPLICATION_JSON_CHARSET_UTF_8, produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> servicePinCodeList(@RequestBody String payload) {
        ApiResponse apiResponse = this.initializeResponse("${api.base.path}/service/pincode/list");
        try {
            JsonObject obj = helper.fromJson(payload, JsonObject.class);
            SearchBO searchBO = helper.fromJson(payload, SearchBO.class);
            this.validateInputPayload(searchBO);

            List<JsonObject> list = touchStoreTransaction.getServicePinCodeList(obj.get("c_c2code").getAsString(), searchBO);

            JsonObject res = new JsonObject();
            JsonArray result = (JsonArray) helper.getGson().toJsonTree(list,
                    new TypeToken<List<JsonObject>>() {
                    }.getType());
            res.add("j_list", result);
            res.addProperty("n_next_page", searchBO.getPage() + 1);
            this.setDataJsonObjectPayload(apiResponse, res);

        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @Scheduled(cron = "0 0 * * * *")
    @GetMapping(value = "/combine/store/cron", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> combineStoresCron() {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/combine/store");
        CombineCronTimeLogEntity logEntity = new CombineCronTimeLogEntity();
        try {

            log.info("Cron Started at "+ helper.getCurrentTime());
            logEntity.setStartedAt(helper.getCurrentTime());
            logEntity = firmTransaction.saveLog(logEntity);

            JsonObject request = new JsonObject();
            request.addProperty("d_from_date",helper.getCurrentDateString());
            request.addProperty("d_to_date", helper.getCurrentDateString());
            firmTransaction.combine(request);

            log.info("Cron Completed at "+ helper.getCurrentTime());
            logEntity.setEndedAt(helper.getCurrentTime());
            logEntity.setCompletedStatus("Success");
            firmTransaction.saveLog(logEntity);
            this.addMessage(apiResponse, "Stores combined successfully!");

        } catch (Exception e) {
            logEntity.setEndedAt(helper.getCurrentTime());
            logEntity.setCompletedStatus("Failed"+helper.getString(e.getStackTrace()));
            firmTransaction.saveLog(logEntity);
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    @Scheduled(cron = "0 0 * * * *")
    @GetMapping(value = "/update/c2code", produces = Constants.APPLICATION_JSON_CHARSET_UTF_8)
    public ResponseEntity<ApiResponse> updateC2codeInFirm() {
        ApiResponse apiResponse = this.initializeResponse("POST:${api.base.path}/firm/update/c2code");
        try {
            log.info("Update C2Code Cron Started at "+ helper.getCurrentTime());
            int firmUpdatedCount = firmTransaction.updateFirmC2code();

            this.addMessage(apiResponse, firmUpdatedCount + " records updated ");
            log.info(firmUpdatedCount + " records updated ");
            log.info("Update C2Code Cron Ended at "+ helper.getCurrentTime());
        } catch (Exception e) {
            this.handleAppExceptions(e, apiResponse);
        }
        return this.getResponseEntity(apiResponse);
    }

    /*@PostConstruct
    public void eventHubListener()  throws IOException {
        EventProcessorClient eventProcessorClient = new EventProcessorClientBuilder()
                .consumerGroup(EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME)
                .connectionString(connectionString, eventhubName)
                .checkpointStore(new SampleCheckpointStore())
                .processEvent(eventContext -> {
                    try {
                        String consumedData = eventContext.getEventData().getBodyAsString();
                        System.out.println("----------------"+consumedData);
                        JsonArray jsonArray = helper.fromJson(consumedData, JsonArray.class);
                        sellerTransaction.stockNotification(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .processError(errorContext -> {

                })
                .buildEventProcessorClient();
        eventProcessorClient.start();
    }*/
}

