package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.bo.SearchBO;
import com.c2.lc.lib.exceptions.*;
import com.c2.lc.lib.properties.Messages;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.*;
import com.c2.lc.ms.customer.entities.customer.*;
import com.c2.lc.ms.customer.entities.customer.pk.FirmUserPKEntity;
import com.c2.lc.ms.customer.entities.seller.LcSellerBuyerPriorityEntity;
import com.c2.lc.ms.customer.entities.seller.LoCombinedFirmEntity;
import com.c2.lc.ms.customer.repos.customer.*;
import com.c2.lc.ms.customer.repos.seller.LcUserSellerPriority;
import com.c2.lc.ms.customer.repos.seller.LoCombinedFirmRepository;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.FirmBranchService;
import com.c2.lc.ms.customer.services.interfaces.FirmService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URISyntaxException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FirmServiceImpl extends LcBaseServiceImpl implements FirmService {

    @Autowired
    private FirmRepo firmRepo;
    @Autowired
    private FirmBranchService firmBranchService;
    @Autowired
    private FirmUserRepo firmUserRepo;
    @Autowired
    private CloudBlobContainer cloudBlobContainer;
    @Autowired
    private ScheduleDemoRepo scheduleDemoRepo;
    @Autowired
    private ContactDetailRepo contactDetailRepo;
    @Autowired
    private OtherDocumentsRepo otherDocumentsRepo;
    @Autowired
    private LegalIdentitiesRepo legalIdentitiesRepo;
    @Autowired
    private UserDetailRepo userDetailRepo;
    @Autowired
    private FirmDefaultRepo firmDefaultRepo;
    @Autowired
    private LoCombinedFirmRepository loCombinedFirmRepository;
    @Autowired
    private FirmSellersRepo firmSellersRepo;
    @Autowired
    private TouchStoreRepo touchStoreRepo;
    @Autowired
    private TSPinCodeReqRepo pinCodeReqRepo;
    @Autowired
    private UserOwnerRepo userOwnerRepo;
    @Autowired
    private CombineFailedMobNoRepo failedMobNoRepo;
    @Autowired
    private CombineCronTimeLogRepo logRepo;

    @Autowired
    private LcUserSellerPriority userSellerPriority;

    @PersistenceContext(unitName = "mysql")
    @Autowired
    private EntityManager entityManager;

    @Value("${spring.email.from}")
    String fromMail;
    @Value("${spring.email.to}")
    String toMail;
    @Value("${spring.email.url}")
    String url;

    @Override
    public FirmEntity getFirmById(Long firmId) throws RecordNotFoundException {
        return firmRepo.findById(firmId)
                .orElseThrow(() -> new RecordNotFoundException(firmId, "Record not found!"));
    }

    @Override
    public ContactDetailEntity getFirmContact(Long firmId) throws RecordNotFoundException {
        return getFirmById(firmId).getContactDetail();
    }

    @Override
    public boolean doesExistMobileNo(String mobileNo) {
        List<FirmEntity> list = firmRepo.getByMobileNo(mobileNo);
        return !helper.isEmpty(mobileNo) && list != null && list.size() > 0;

    }

    @Override
    public FirmEntity getRegByMobileNoAndType(String mobileNo, String type) {
        return firmRepo.getByMobileWithType(mobileNo, type);
    }

    @Override
    public boolean doesMobileNumberExist(String mobileNo, String type) {
        return firmRepo.getByMobileWithType(mobileNo, type) != null;
    }

    @Override
    public boolean isScheduleExist(String mobileNo, String product) {
        return scheduleDemoRepo.checkScheduleExist(mobileNo, product) != null;
    }

    @Override
    public void saveScheduleDemo(ScheduleDemoEntity scheduleDemo) {
        scheduleDemoRepo.save(scheduleDemo);
    }

 /*   private String bodyForEmail(DemoRequestEntity demoRequestEntity) {
        String cName = demoRequestEntity.getCName();
        String cEmail = demoRequestEntity.getCEmail();
        String cMobileNo = demoRequestEntity.getCMobileNo();
        String cCompany = demoRequestEntity.getCCompany();
        String cCityName = demoRequestEntity.getCCityName();
        String cProductName = demoRequestEntity.getCProductName();
        return "<p>Hi,</p> <p>We have received a " +cProductName+ " demo request from "+ cName +". Please refer to the following details.</p> <p>&nbsp;</p> <p><b>Name : "+ cName +"</b></p> <p><b>Email Address : "+ cEmail +"</b></p> <p><b>Phone Number : "+ cMobileNo +"</b></p> <p><b>Company : "+ cCompany +"</b></p> <p><b>City : "+ cCityName +"</b></p> <p><b>Product : " +cProductName+ "</b></p> <p>&nbsp;</p> <p>Thanks And Regards,</p> <p>LiveOrder</p>";
    }*/

    @Override
    public boolean doesExistEmail(String email) {
        return !helper.isEmpty(email) && firmRepo.getByEmail(email) != null;
    }

    @Override
    public FirmEntity saveFirm(Long userId, FirmEntity firmEntity) throws RecordNotFoundException {
        List<Object[]> location;
        String sql = "SELECT pm.c_state, pm.c_state_code, ugcm.c_code AS city_code, ugcm.c_name AS city_name, ugam.c_code AS area_code, ugam.c_name AS area_name " +
                "   FROM pincode_mst pm " +
                "   JOIN u_geo_district_mst ugdm ON ugdm.c_name = pm.c_district " +
                "   JOIN u_geo_city_mst ugcm ON ugdm.c_code = ugcm.c_geo_district_code AND ugcm.c_name = pm.c_district" +
                "   LEFT JOIN u_geo_area_mst ugam ON ugam.c_geo_city_code = ugcm.c_code " +
                "   WHERE pm.c_code = :pinCode";

        Query locQuery = entityManager.createNativeQuery(sql);
        locQuery.setParameter("pinCode", firmEntity.getCPin());
        location = this.getResultList(locQuery);

        if (!location.isEmpty()) {
            for (Object[] objects : location) {
                firmEntity.setCStateName(helper.getString(objects[0]).equals("") ? null : helper.getString(objects[0]));
                firmEntity.setCStateCode(helper.getString(objects[1]).equals("") ? null : helper.getString(objects[1]));
                firmEntity.setCCityCode(helper.getString(objects[2]).equals("") ? null : helper.getString(objects[2]));
                firmEntity.setCCityName(helper.getString(objects[3]).equals("") ? null : helper.getString(objects[3]));
                firmEntity.setCAreaCode(helper.getString(objects[4]).equals("") ? null : helper.getString(objects[4]));
                firmEntity.setCAreaName(helper.getString(objects[5]).equals("") ? null : helper.getString(objects[5]));
            }
        }

        firmEntity.setNLastUpdatedBy(userId);
        firmEntity.setTLastUpdatedAt(helper.getCurrentTime());
        return firmRepo.save(firmEntity);
    }

    @Override
    public ContactDetailEntity saveContact(Long userId, ContactDetailEntity contactDetailEntity) {
        contactDetailEntity.setNLastUpdatedBy(userId);
        contactDetailEntity.setTLastUpdatedAt(helper.getCurrentTime());
        return contactDetailRepo.save(contactDetailEntity);
    }

    @Override
    public void makeFirmInactive(Long firmId) throws RecordNotFoundException {
        FirmEntity firmEntity = firmRepo.findById(firmId)
                .orElseThrow(() -> new RecordNotFoundException(firmId, "Record not found!"));

        //firmEntity.setCMobileNo(firmEntity.getNFirmId().toString());
        //firmEntity.getContactDetail().setCEmailId(firmEntity.getContactDetail().getCEmailId());
        firmEntity.setCStatus(Constants.STATUS_INACTIVE);
        firmRepo.save(firmEntity);
    }

    @Override
    public String uploadDocument(Long userId, Long firmId, JsonObject data, String imageType) throws URISyntaxException, StorageException, IOException, RecordNotFoundException {
        String imageData = helper.getString(data.get("docData"));
        String[] fileName = helper.getString(data.get("docName")).split("\\.");
        String fileType = fileName[fileName.length - 1];
        String path = "firm/" + firmId + "/" + imageType + "/" + imageType + "-" + firmId + "-" + helper.getCurrentTime().toString() + "." + fileType;

        return blobUpload(path, imageData);
    }

    @Override
    public boolean isBuyer(Long firmId) throws RecordNotFoundException {
        FirmEntity firmEntity = getFirmById(firmId);
        return firmEntity.getCType().equals(Messages.BUYER_TYPE);
    }

    @Override
    public boolean isSeller(Long firmId) throws RecordNotFoundException {
        FirmEntity firmEntity = getFirmById(firmId);
        return firmEntity.getCType().equals(Messages.SELLER_TYPE);
    }

    @Override
    public List<FirmEntity> getListSeller() {
        return firmRepo.findBySeller("Y");
    }

    @Override
    public List<FirmEntity> getListBuyer() {
        return firmRepo.findByBuyer("Y");
    }

    @Override
    public boolean deleteDocument(Long firmId, String path) throws StorageException, URISyntaxException, RecordNotFoundException {
        path = path.substring(path.indexOf("firm/"));
        return blobDelete(path);
    }

    @Override
    public List<Object> listDocument() {
        return null;
    }

    //TODO delete is not used
    @Override
    public void updateContact(Long userId, ContactDetailEntity contactDetail, JsonObject json) throws RecordNotFoundException, DuplicateRecordException, InputPayloadException {
        ContactDetailEntity saveContact = getFirmContact(contactDetail.getNContactId());

        if (json.has("c_mobile_no") && !contactDetail.getCMobileNo().equals(json.get("c_mobile_no").getAsString())) {
            throw new InputPayloadException(json.get("c_mobile_no").getAsString(), "Mobile No. cannot be changed!");
        }

        if (json.has("c_email")) {
            saveContact.setCEmailId(json.get("c_email").getAsString());
        }
        if (json.has("c_alternate_email_id")) {
            saveContact.setCAlternateEmailId(json.get("c_alternate_email_id").getAsString());
        }
        if (json.has("c_address1")) {
            saveContact.setCAddress1(json.get("c_address1").getAsString());
        }
        if (json.has("c_address2")) {
            saveContact.setCAddress2(json.get("c_address2").getAsString());
        }
        if (json.has("c_mobile_no")) {
            saveContact.setCMobileNo(json.get("c_mobile_no").getAsString());
        }
        if (json.has("c_alternate_mobile_no")) {
            saveContact.setCAlternateMobileNo(json.get("c_alternate_mobile_no").getAsString());
        }
        if (json.has("c_phone_no")) {
            saveContact.setCPhoneNo(json.get("c_phone_no").getAsString());
        }
        if (json.has("c_alternate_phone_no")) {
            saveContact.setCAlternatePhoneNo(json.get("c_alternate_phone_no").getAsString());
        }
        if (json.has("c_contact_name")) {
            saveContact.setCContactName(json.get("c_contact_name").getAsString());
        }
        if (json.has("c_pin")) {
            saveContact.setCPin(json.get("c_pin").getAsString());
        }
        if (json.has("c_note")) {
            saveContact.setCNote(json.get("c_note").getAsString());
        }
        if (json.has("c_country_name")) {
            saveContact.setCCountryName(json.get("c_country_name").getAsString());
        }
        if (json.has("c_country_code")) {
            saveContact.setCCountryCode(json.get("c_country_code").getAsString());
        }
        if (json.has("c_state_name")) {
            saveContact.setCStateName(json.get("c_state_name").getAsString());
        }
        if (json.has("c_state_code")) {
            saveContact.setCStateCode(json.get("c_state_code").getAsString());
        }
        if (json.has("c_city_name")) {
            saveContact.setCCityName(json.get("c_city_name").getAsString());
        }
        if (json.has("c_city_code")) {
            saveContact.setCCityCode(json.get("c_city_code").getAsString());
        }
        if (json.has("c_area_name")) {
            saveContact.setCAreaName(json.get("c_area_name").getAsString());
        }
        if (json.has("c_area_code")) {
            saveContact.setCAreaCode(json.get("c_area_code").getAsString());
        }
        if (json.has("c_landmark")) {
            saveContact.setCLandmark(json.get("c_landmark").getAsString());
        }
        if (json.has("c_image_url")) {
            saveContact.setCImageUrl(json.get("c_image_url").getAsString());
        }

        saveContact(userId, saveContact);
    }

    @Override
    public void updateFirmEntity(Long userId, FirmEntity current, BranchDetailsBO firm) throws InvalidRequestException {
        current.setCCityCode(firm.getCityCode());
        current.setCCityName(firm.getCityName());
        current.setCCityCode(firm.getCityCode());
        current.setCAreaCode(firm.getAreaCode());
        current.setCAreaName(firm.getAreaName());
        current.setCStateCode(firm.getStateCode());
        current.setCStateName(firm.getStateName());
        current.setCEmail(firm.getEmailId());
        current.setCPin(firm.getPinCode());
        current.setCGstNo(firm.getGstNumber());
        current.setCGstType(firm.getGstType());
        current.setCMobileNo(firm.getMobileNo());
        if (firm.getFirmName() != null && !firm.getFirmName().equals("")) {
            current.setCName(firm.getFirmName());
        }
        current.setCImageUrl(firm.getFirmImage());
        current.setContactDetail(getContactDetails(userId, firm, current.getContactDetail() == null ? null : current.getContactDetail().getNContactId()));
        current.setLegalIdentities(getLegalIdentities(userId, firm, current.getLegalIdentities() == null ? null : current.getLegalIdentities().getNLegalId()));
        current.setDocumentDetail(getDocumentDetails(userId, firm, current.getDocumentDetail() == null ? null : current.getDocumentDetail().getNDocumentsId()));
        current.setIdTime(userId, helper.getCurrentTime());
        firmRepo.save(current);
    }

    @Override
    public FirmEntity saveFirmBranch(LcHeaderBO header, BranchDetailsBO branch) throws DuplicateRecordException, InvalidRequestException {
        FirmEntity firmEntity = new FirmEntity();
        firmEntity = setFirmDetails(header, branch, firmEntity);
        firmRepo.save(firmEntity);
//        firmEntity.setBrCode(helper.getString(firmEntity.getNFirmId()));
        firmEntity.setBrCode("000");
        return firmRepo.save(firmEntity);
    }

    @Override
    public void delete(String mobile) throws RecordNotFoundException {

        List<FirmEntity> entities = firmRepo.getByMobileNo(mobile);

        if (entities != null && entities.size() > 0) {
            for (FirmEntity entity : entities) {
                List<FirmSellersEntity> firmSellersEntities = firmSellersRepo.getByFirmId(entity.getNFirmId());
                List<LcSellerBuyerPriorityEntity> priorityEntities = userSellerPriority.getByFirmId(entity.getNFirmId());
                if (entity.getFirmUserEntities() != null && entity.getFirmUserEntities().size() > 0)
                    firmUserRepo.deleteAll(entity.getFirmUserEntities());
                //userDetailRepo.deleteById(entity.getNCreatedBy());
                if (entity.getDefaultEntities() != null) {
                    for (int i = 0; i < entity.getDefaultEntities().size(); i++) {
                        firmDefaultRepo.delete(entity.getDefaultEntities().get(i));
                    }
                }
                if (firmSellersEntities.size() >0){
                 firmSellersRepo.deleteAll(firmSellersEntities);
                }
                if (entity.getC2Code() != null && !helper.isEmpty(entity.getC2Code())) {
                    List<TSStoreRegisterEntity> list = touchStoreRepo.getByC2Code(entity.getC2Code());
                    if (list.size() > 0) {
                        touchStoreRepo.deleteAll(list);
                    }
                }
                if (priorityEntities.size() > 0){
                    userSellerPriority.deleteAll(priorityEntities);
                }
            }
        }
        firmRepo.deleteAll(entities);

    }

    @Override
    public int checkGst(String gstNumber, Long userId) throws RecordNotFoundException {
             int count = firmRepo.getByGst(gstNumber,userId);
             return count;
    }

    @Override
    public int checkDrugLicense(String dlNo) {
        int count = legalIdentitiesRepo.isDrugLicenseExists(dlNo);
        return count;
    }

    @Override
    public JsonObject combineList(String mobileNo) throws DataFormatException {
        int i, j;
        StoreCombineBO storeCombineBO;
        Lc1FirmBO firmEntity;
        JsonObject obj = new JsonObject();
        List<Object[]> listOfFirms;
        List<Lc1FirmBO> firmEntityList = new ArrayList<>();

        String sql = getLC1FirmQuery();
        Query allFirms = entityManager.createNativeQuery(sql);
        allFirms.setParameter("mobileNo", mobileNo);
        listOfFirms = this.getResultList(allFirms);

        Map<String, List<Lc1FirmBO>> licenseMap = new HashMap<>();
        if (!listOfFirms.isEmpty()) {
            for (Object[] firm : listOfFirms) {
                j = -1;
                Lc1FirmBO lc1Firm = new Lc1FirmBO();
                lc1Firm.setGstNumber(helper.getString(firm[++j]));
                lc1Firm.setStateName(helper.getString(firm[++j]));
                lc1Firm.setStateCode(helper.getString(firm[++j]));
                lc1Firm.setCityName(helper.getString(firm[++j]));
                lc1Firm.setCityCode(helper.getString(firm[++j]));
                lc1Firm.setAreaCode(helper.getString(firm[++j]));
                lc1Firm.setAreaName(helper.getString(firm[++j]));
                lc1Firm.setFirmName(helper.getString(firm[++j]));
                lc1Firm.setPinCode(helper.getString(firm[++j]));

                lc1Firm.setDrugLicenseNo1(helper.getString(firm[++j]));
                lc1Firm.setDrugLicenseNo2(helper.getString(firm[++j]));
                lc1Firm.setDrugLicenseNo3(helper.getString(firm[++j]));
                lc1Firm.setMobileNo(helper.getString(firm[++j]));
                lc1Firm.setAddress1(helper.getString(firm[++j]));
                lc1Firm.setAddress2(helper.getString(firm[++j]));
                lc1Firm.setAddress3(helper.getString(firm[++j]));
                lc1Firm.setBranchCode(helper.getString(firm[++j]));
                lc1Firm.setSellerCode(helper.getString(firm[++j]));
                lc1Firm.setSellerName(helper.getString(firm[++j]));
                firmEntityList.add(lc1Firm);
            }
        }

        for (i = 0; i < firmEntityList.size(); i++) {
            List<Lc1FirmBO> firmList;
            String l1 = firmEntityList.get(i).getDrugLicenseNo1() == null ? "" : firmEntityList.get(i).getDrugLicenseNo1();
            String l2 = firmEntityList.get(i).getDrugLicenseNo2() == null ? "" : firmEntityList.get(i).getDrugLicenseNo2();
            String l3 = firmEntityList.get(i).getDrugLicenseNo3() == null ? "" : firmEntityList.get(i).getDrugLicenseNo3();

            String licenseNo1 = l1.equals("") ? "#" : (l1.length() > 4 ? l1.substring(l1.length() - 4) : l1);
            String licenseNo2 = l2.equals("") ? "#" : (l2.length() > 4 ? l2.substring(l2.length() - 4) : l2);
            String licenseNo3 = l3.equals("") ? "#" : (l3.length() > 4 ? l3.substring(l3.length() - 4) : l3);
            if (licenseMap.containsKey(licenseNo1)) {
                firmList = licenseMap.get(licenseNo1);
                firmList.add(firmEntityList.get(i));
                licenseMap.put(licenseNo1, firmList);
            } else if (licenseMap.containsKey(licenseNo2)) {
                firmList = licenseMap.get(licenseNo2);
                firmList.add(firmEntityList.get(i));
                licenseMap.put(licenseNo2, firmList);
            } else if (licenseMap.containsKey(licenseNo3)) {
                firmList = licenseMap.get(licenseNo3);
                firmList.add(firmEntityList.get(i));
                licenseMap.put(licenseNo3, firmList);
            } else {
                firmList = new ArrayList<>();
                if (!licenseNo1.equals("")) {
                    firmList.add(firmEntityList.get(i));
                    licenseMap.put(licenseNo1, firmList);
                } else if (!licenseNo2.equals("")) {
                    firmList.add(firmEntityList.get(i));
                    licenseMap.put(licenseNo2, firmList);
                } else if (!licenseNo3.equals("")) {
                    firmList.add(firmEntityList.get(i));
                    licenseMap.put(licenseNo3, firmList);
                }
            }
        }

        JsonArray finalArray = new JsonArray();
        for (Map.Entry<String, List<Lc1FirmBO>> license : licenseMap.entrySet()) {
            JsonArray array = new JsonArray();
            List<Lc1FirmBO> list = license.getValue();
            for (i = 0; i < list.size(); i++) {
                firmEntity = list.get(i);
                storeCombineBO = new StoreCombineBO();

                storeCombineBO.setAreaCode(firmEntity.getAreaCode());
                storeCombineBO.setAreaName(firmEntity.getAreaName());
                storeCombineBO.setCityCode(firmEntity.getCityCode());
                storeCombineBO.setCityName(firmEntity.getCityName());
                storeCombineBO.setStateCode(firmEntity.getStateCode());
                storeCombineBO.setStateName(firmEntity.getStateName());
                storeCombineBO.setAddress1(firmEntity.getAddress1());
                storeCombineBO.setAddress2(firmEntity.getAddress2() + firmEntity.getAddress3());
                storeCombineBO.setBrCode(firmEntity.getBranchCode());
                storeCombineBO.setFirmName(firmEntity.getFirmName());
                if (firmEntity.getDrugLicenseNo1() != null && !firmEntity.getDrugLicenseNo1().equals("") && firmEntity.getDrugLicenseNo1().contains(license.getKey())) {
                    storeCombineBO.setDrugLicenseNo1(firmEntity.getDrugLicenseNo1());
                } else if (firmEntity.getDrugLicenseNo2() != null && !firmEntity.getDrugLicenseNo2().equals("") && firmEntity.getDrugLicenseNo2().contains(license.getKey())) {
                    storeCombineBO.setDrugLicenseNo1(firmEntity.getDrugLicenseNo2());
                } else if (firmEntity.getDrugLicenseNo3() != null && !firmEntity.getDrugLicenseNo3().equals("") && firmEntity.getDrugLicenseNo3().contains(license.getKey())) {
                    storeCombineBO.setDrugLicenseNo1(firmEntity.getDrugLicenseNo3());
                } else {
                    storeCombineBO.setDrugLicenseNo1(license.getKey());
                }
                storeCombineBO.setGstNumber(firmEntity.getGstNumber());
                storeCombineBO.setPinCode(firmEntity.getPinCode());
                storeCombineBO.setSellerCode(firmEntity.getSellerCode());
                storeCombineBO.setSellerName(firmEntity.getSellerName());
                array.add(helper.toJsonObjectTree(storeCombineBO, StoreCombineBO.class));
            }
            finalArray.add(array);
        }
        obj.add("j_drug_license_no", finalArray);
        return obj;
    }

    private ContactDetailEntity getLC1Contact(String address1, String address2, String address3, String mobile, String email) {
        ContactDetailEntity contactDetailEntity = new ContactDetailEntity();
        contactDetailEntity.setCMobileNo(mobile);
        contactDetailEntity.setCAddress1(address1);
        contactDetailEntity.setCAddress2(address2 + "," + address3);
        contactDetailEntity.setCEmailId(email);
        return contactDetailEntity;
    }

    private LegalIdentitiesEntity getLC1Legal(String license1, String license2, String license3, String expiry) {
        LegalIdentitiesEntity legalIdentitiesEntity = new LegalIdentitiesEntity();
        if (expiry != null && !expiry.equals("")) {
            legalIdentitiesEntity.setDDrugLicenseNo1ExpiryDate(helper.getLocalDate(expiry));
            legalIdentitiesEntity.setDDrugLicenseNo2ExpiryDate(helper.getLocalDate(expiry));
        }

        legalIdentitiesEntity.setCDrugLicenseNo1(license1 == null ? "" : license1);
        legalIdentitiesEntity.setCDrugLicenseNo2(license2 == null ? "" : license2);
        legalIdentitiesEntity.setCDrugLicenseNo3(license3 == null ? "" : license3);
        return legalIdentitiesEntity;
    }

    private String getLC1FirmQuery() {
        return "SELECT cad.c_gst_no, pm.c_state, pm.c_state_code, act.c_city, ugcm.c_code, act.c_area_code, ugam.c_name AS area_name," +
                "   act.c_name, act.c_pin, act.c_drug_licence_no_1, act.c_drug_licence_no_2, act.c_drug_licence_no_3, " +
                "   act.c_mobile AS mobile,act.c_add_1 AS add1,act.c_add_2 add2,act.c_add_3 AS add38, act.c_code as c_firm_id, lcmst.c_code as sellerCode, lcmst.c_name as sellerName " +
                "   FROM cust_act_mst act LEFT JOIN lc_c2code_mst lcmst ON lcmst.c_code=act.c_c2code" +
                "   LEFT JOIN pincode_mst pm ON pm.c_code = act.c_pin " +
                "   LEFT JOIN lo_combined_firm_temp lcft ON lcft.c_code = act.c_code  " +
                "       AND (lcft.c_drug_license_no1 = act.c_drug_licence_no_1 OR lcft.c_drug_license_no2 = act.c_drug_licence_no_2  OR lcft.c_drug_license_no3 = act.c_drug_licence_no_3)" +
                "   LEFT JOIN u_geo_city_mst ugcm ON ugcm.c_name = act.c_city" +
                "   LEFT JOIN cust_area_mst ugam ON ugam.c_c2code = act.c_c2code AND ugam.c_code = act.c_area_code " +
                "   LEFT JOIN cust_act_det cad ON cad.c_c2code = act.c_c2code AND act.c_code = cad.c_cust_code " +
                "   WHERE lcft.c_code IS NULL " +
                "       AND (lcft.c_drug_license_no1 IS NULL OR lcft.c_drug_license_no2 IS NULL OR lcft.c_drug_license_no3 IS NULL) " +
                "   AND act.n_customer = 1 AND (act.c_phone_1 LIKE :mobileNo OR act.c_phone_2 LIKE :mobileNo OR act.c_mobile LIKE :mobileNo)";
    }

    @Override
    public FirmEntity combineStores(StoreCombineRequestBO requestBO, Long userId) throws DataFormatException {
        String sql = getLC1FirmByIdList();
        int j;

        saveLoCombinedFirmTemp(requestBO);
        Query firmDetail = entityManager.createNativeQuery(sql);

        for (int i = 0; i < requestBO.getDrugList().size(); i++) {
            if (requestBO.getDrugList().get(i).equals("#")) {
                requestBO.getDrugList().set(i, "");
            }
        }
        firmDetail.setParameter("mobileNo", helper.getContainLikeQueryString(requestBO.getMobileNo()));
        firmDetail.setParameter("storeList", requestBO.getStoreList());
        firmDetail.setParameter("drugList", requestBO.getDrugList());
        List<Object[]> firmList = this.getResultList(firmDetail);

        FirmEntity lc1Firm = new FirmEntity();
        if (!firmList.isEmpty()) {
            for (Object[] firm : firmList) {
                lc1Firm.setCGstNo(helper.getString(firm[0]));
                lc1Firm.setCStateName(helper.getString(firm[1]));
                lc1Firm.setCStateCode(helper.getString(firm[2]));
                lc1Firm.setCCityName(helper.getString(firm[3]));
                lc1Firm.setCCityCode(helper.getString(firm[4]));
                lc1Firm.setCAreaCode(helper.getString(firm[5]));
                lc1Firm.setCAreaName(helper.getString(firm[6]));
                lc1Firm.setCName(helper.getString(firm[7]));
                lc1Firm.setCPin(helper.getString(firm[8]));

                String licenseNo1 = helper.getString(firm[9]);
                String licenseNo2 = helper.getString(firm[10]);
                String licenseNo3 = helper.getString(firm[11]);
                String mobile = helper.getString(firm[12]);
                String address1 = helper.getString(firm[13]);
                String address2 = helper.getString(firm[14]);
                String address3 = helper.getString(firm[15]);
                lc1Firm.setBrCode(helper.getString(firm[16]));
                lc1Firm.setC2Code(helper.getString(firm[17]));
                String email = helper.getString(firm[18]);
//                lc1Firm.setLegalIdentities(getLC1Legal(licenseNo1, licenseNo2, licenseNo3, expiry));
                lc1Firm.setCMobileNo(mobile);
                lc1Firm.setCEmail(email);
                lc1Firm.setContactDetail(getLC1Contact(address1, address2, address3, mobile, email));
                lc1Firm.setCType(Constants.ROLE_BUYER);
                lc1Firm.setCStatus(Constants.STATUS_ACTIVE);
                break;
            }
        }
        createCombinedStore(userId, lc1Firm);
        if (!firmList.isEmpty()) {
            for (Object[] firm : firmList) {
                saveFirmSellers(userId, lc1Firm, helper.getString(firm[16]), helper.getString(firm[17]));
            }
        }
        createFirmUser(userId, lc1Firm);
        return lc1Firm;
    }

    private void saveFirmSellers(long userId, FirmEntity lc1Firm, String brCode, String sellerCode) {
        FirmSellersEntity sellersEntity = new FirmSellersEntity(userId, helper.getCurrentTime());
        sellersEntity.setCSellerCode(sellerCode);
        sellersEntity.setNFirmId(lc1Firm.getNFirmId());
        sellersEntity.setCBuyerCode(brCode);
        firmSellersRepo.save(sellersEntity);
    }

    public void saveLoCombinedFirmTemp(StoreCombineRequestBO requestBO) {
        int j;
        String sql = getLC1FirmByIdList();
        Query firmDetail = entityManager.createNativeQuery(sql);

        for (int i = 0; i < requestBO.getDrugList().size(); i++) {
            if (requestBO.getDrugList().get(i).equals("#")) {
                requestBO.getDrugList().set(i, "");
            }
        }
        firmDetail.setParameter("mobileNo", helper.getContainLikeQueryString(requestBO.getMobileNo()));
        firmDetail.setParameter("storeList", requestBO.getStoreList());
        firmDetail.setParameter("drugList", requestBO.getDrugList());
        List<Object[]> firmList = this.getResultList(firmDetail);


        if (!firmList.isEmpty()) {
            for (Object[] firm : firmList) {
                LoCombinedFirmEntity loCombinedFirmEntity = new LoCombinedFirmEntity();
                loCombinedFirmEntity.setcGstNo(helper.getString(firm[0]));
                loCombinedFirmEntity.setcStateName(helper.getString(firm[1]));
                loCombinedFirmEntity.setcStateCode(helper.getString(firm[2]));
                loCombinedFirmEntity.setcCityName(helper.getString(firm[3]));
                loCombinedFirmEntity.setcCityCode(helper.getString(firm[4]));
                loCombinedFirmEntity.setcAreaCode(helper.getString(firm[5]));
                loCombinedFirmEntity.setcAreaName(helper.getString(firm[6]));
                loCombinedFirmEntity.setcName(helper.getString(firm[7]));
                loCombinedFirmEntity.setcPinCode(helper.getString(firm[8]));
                loCombinedFirmEntity.setcDrugLicenseNo1(helper.getString(firm[9]));
                loCombinedFirmEntity.setcDrugLicenseNo2(helper.getString(firm[10]));
                loCombinedFirmEntity.setcDrugLicenseNo3(helper.getString(firm[11]));
                loCombinedFirmEntity.setcMobile(helper.getString(firm[12]));
                loCombinedFirmEntity.setcAddress1(helper.getString(firm[13]));
                loCombinedFirmEntity.setcAddress2(helper.getString(firm[14]) + helper.getString(firm[15]));
                loCombinedFirmEntity.setcCode(helper.getString(firm[16]));
                loCombinedFirmEntity.setc2Code(helper.getString(firm[17]));
                loCombinedFirmRepository.save(loCombinedFirmEntity);
            }
        }
    }

    @Override
    public void deleteLc1(String mobileNo) throws RecordNotFoundException {
        loCombinedFirmRepository.deleteByMobile(mobileNo);
//        if (loList.size() > 0) {
//            for (LoCombinedFirmEntity loCombinedFirmEntity : loList) {
//                loCombinedFirmRepository.deleteById(loCombinedFirmEntity.getc2Code());
//            }
//        } else {
//            throw new RecordNotFoundException(mobileNo + "Record Not Found!");
//        }
    }

    @Override
    public FirmEntity saveUncombinedStores(StoreCombineRequestBO requestBO, Long userId) throws DataFormatException {
        int j;
        String sql = getLC1FirmByIdList();
        FirmEntity lc1Firm = null;
        Query firmDetail = entityManager.createNativeQuery(sql);

        firmDetail.setParameter("mobileNo", helper.getContainLikeQueryString(requestBO.getMobileNo()));
        firmDetail.setParameter("storeList", requestBO.getStoreList());
        firmDetail.setParameter("drugList", requestBO.getDrugList());
        List<Object[]> firmList = this.getResultList(firmDetail);

        if (!firmList.isEmpty()) {
            for (Object[] firm : firmList) {
                lc1Firm = new FirmEntity();
                lc1Firm.setCGstNo(helper.getString(firm[0]));
                lc1Firm.setCStateName(helper.getString(firm[1]));
                lc1Firm.setCStateCode(helper.getString(firm[2]));
                lc1Firm.setCCityName(helper.getString(firm[3]));
                lc1Firm.setCCityCode(helper.getString(firm[4]));
                lc1Firm.setCAreaCode(helper.getString(firm[5]));
                lc1Firm.setCAreaName(helper.getString(firm[6]));
                lc1Firm.setCName(helper.getString(firm[7]));
                lc1Firm.setCPin(helper.getString(firm[8]));

                String licenseNo1 = helper.getString(firm[9]);
                String licenseNo2 = helper.getString(firm[10]);
                String licenseNo3 = helper.getString(firm[11]);
                String mobile = helper.getString(firm[12]);
                String address1 = helper.getString(firm[13]);
                String address2 = helper.getString(firm[14]);
                String address3 = helper.getString(firm[15]);
                lc1Firm.setBrCode(helper.getString(firm[16]));
                lc1Firm.setC2Code(helper.getString(firm[17]));
                String email = helper.getString(firm[18]);
//                lc1Firm.setLegalIdentities(getLC1Legal(licenseNo1, licenseNo2, licenseNo3, expiry));
                lc1Firm.setCMobileNo(mobile);
                lc1Firm.setContactDetail(getLC1Contact(address1, address2, address3, mobile, email));
                lc1Firm.setCType(Constants.ROLE_BUYER);
                lc1Firm.setCStatus(Constants.STATUS_ACTIVE);
                createCombinedStore(userId, lc1Firm);
                saveFirmSellers(userId, lc1Firm, helper.getString(firm[16]), helper.getString(firm[17]));
                createFirmUser(userId, lc1Firm);
            }
        }
        return lc1Firm;
    }

    private String getLC1FirmById() {
        return "SELECT cad.c_gst_no, pm.c_state, pm.c_state_code, act.c_city, ugcm.c_code, act.c_area_code, ugam.c_name AS area_name, " +
                "   act.c_name, act.c_pin, act.c_drug_licence_no_1, act.c_drug_licence_no_2, act.c_drug_licence_no_3,  " +
                "   act.c_mobile AS mobile,act.c_add_1 AS add1,act.c_add_2 add2,act.c_add_3 AS add38, act.c_code as c_firm_id  " +
                "   FROM cust_act_mst act LEFT JOIN lc_c2code_mst lcmst ON lcmst.c_code=act.c_c2code " +
                "   LEFT JOIN pincode_mst pm ON pm.c_code = act.c_pin  " +
                "   LEFT JOIN u_geo_city_mst ugcm ON ugcm.c_name = act.c_city " +
                "   LEFT JOIN cust_area_mst ugam ON ugam.c_c2code = act.c_c2code and ugam.c_code = act.c_area_code  " +
                "   LEFT JOIN cust_act_det cad ON cad.c_c2code = act.c_c2code and act.c_code = cad.c_cust_code  " +
                "   WHERE act.n_customer = 1 AND act.c_code = :brCode AND (act.c_phone_1 LIKE :mobileNo OR act.c_phone_2 LIKE :mobileNo OR act.c_mobile LIKE :mobileNo)" +
                "   AND (act.c_drug_licence_no_1 = :drugList OR act.c_drug_licence_no_2 = :drugList OR act.c_drug_licence_no_3 = :drugList)";
    }

    private String getLC1FirmByIdList() {
        return "SELECT cad.c_gst_no, pm.c_state, pm.c_state_code, act.c_city, ugcm.c_code, act.c_area_code, ugam.c_name AS area_name, " +
                "   act.c_name, act.c_pin, act.c_drug_licence_no_1, act.c_drug_licence_no_2, act.c_drug_licence_no_3,  " +
                "   act.c_mobile AS mobile,act.c_add_1 AS add1,act.c_add_2 add2,act.c_add_3 AS add38, act.c_code as c_firm_id, act.c_c2code," +
                "   act.c_email_id  " +
                "   FROM cust_act_mst act LEFT JOIN lc_c2code_mst lcmst ON lcmst.c_code=act.c_c2code " +
                "   LEFT JOIN pincode_mst pm ON pm.c_code = act.c_pin  " +
                "   LEFT JOIN u_geo_city_mst ugcm ON ugcm.c_name = act.c_city " +
                "   LEFT JOIN cust_area_mst ugam ON ugam.c_c2code = act.c_c2code and ugam.c_code = act.c_area_code  " +
                "   LEFT JOIN cust_act_det cad ON cad.c_c2code = act.c_c2code and act.c_code = cad.c_cust_code  " +
                "   WHERE act.n_customer = 1 AND act.c_code IN :storeList AND (act.c_phone_1 LIKE :mobileNo OR act.c_phone_2 LIKE :mobileNo OR act.c_mobile LIKE :mobileNo)" +
                "   AND (act.c_drug_licence_no_1 IN :drugList OR act.c_drug_licence_no_2 IN :drugList OR act.c_drug_licence_no_3 IN :drugList)";
    }

    private void createFirmUser(Long userId, FirmEntity firmEntity) {
        FirmUserEntity firmUserEntity = new FirmUserEntity(userId, helper.getCurrentTime());
        FirmUserPKEntity pk = new FirmUserPKEntity(firmEntity.getNFirmId(), userId);
        firmUserEntity.setId(pk);
        firmUserEntity.setCStatus(Constants.STATUS_ACTIVE);
        firmUserRepo.save(firmUserEntity);
    }

    private FirmEntity createCombinedStore(Long userId, FirmEntity store) {
        store.setStoreCombineStatus(Constants.STRING_VALUE_ONE);
        store.setCStatus(Constants.STATUS_ACTIVE);
        store.setIdTime(userId, helper.getCurrentTime());
        return firmRepo.save(store);
    }

    @Override
    public void updateStore(StoreCombineBO store, Long userId) throws RecordNotFoundException {
        FirmEntity firmEntity = firmRepo.findById(helper.getLong(store.getBrCode())).orElse(null);
        if (firmEntity == null) {
            throw new RecordNotFoundException("No branch found");
        }
        firmEntity.setCCityName(store.getCityName());
        firmEntity.setCCityCode(store.getCityCode());
        firmEntity.setCAreaCode(store.getAreaCode());
        firmEntity.setCAreaName(store.getAreaName());
        firmEntity.setCStateCode(store.getStateCode());
        firmEntity.setCStateName(store.getStateName());
        firmEntity.setCPin(store.getPinCode());
        firmEntity.setCGstNo(store.getGstNumber());
        firmEntity.setCName(store.getFirmName());
        firmEntity.setCEmail(store.getEmailId());
        firmEntity.setContactDetail(getStoreContact(userId, store, firmEntity.getContactDetail() == null ? null : firmEntity.getContactDetail().getNContactId()));
        firmEntity.setLegalIdentities(getStoreLegalIdentities(userId, store, firmEntity.getLegalIdentities() == null ? null : firmEntity.getLegalIdentities().getNLegalId()));
        firmEntity.setIdTime(userId, helper.getCurrentTime());
        firmRepo.save(firmEntity);
    }

    @Override
    public StoreCombineBO getStoreDetail(FirmEntity store) {
        StoreCombineBO storeResult = new StoreCombineBO();
        storeResult.setFirmName(store.getCName());
        storeResult.setCLandmark(store.getContactDetail().getCLandmark());
        storeResult.setPinCode(store.getCPin());
        storeResult.setGstNumber(store.getCGstNo());
        storeResult.setBrCode(store.getNFirmId().toString());
        storeResult.setAddress2(store.getContactDetail().getCAddress2());
        storeResult.setAddress1(store.getContactDetail().getCAddress1());
        storeResult.setDrugLicenseNo1(store.getLegalIdentities().getCDrugLicenseNo1());
        storeResult.setCityName(store.getCCityName());
        storeResult.setCityCode(store.getCCityCode());
        storeResult.setAreaCode(store.getCAreaCode());
        storeResult.setAreaName(store.getCAreaName());
        storeResult.setStateCode(store.getCStateCode());
        storeResult.setStateName(store.getCStateName());
        if (store.getCEmail() != null) {
            String[] str = store.getCEmail().split(",");
            storeResult.setEmailId(str[0]);
        } else {
            storeResult.setEmailId(store.getCEmail());
        }
        return storeResult;
    }

    @Override
    public String getC2Code(String cMobileNo) {
        String c2Code = null;
        String sql = "SELECT c_csquare_code FROM u_act_mst uam WHERE c_mobile = :mobile AND c_csquare_code <> '' ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("mobile", cMobileNo);
        Object obj = this.getSingleResult(query);

        if (!obj.equals(0)) {
            c2Code = obj.toString();
        }
        return c2Code;
    }

    @Override
    public AddressModelBO getAddressService(String pincode) throws RecordNotFoundException {
        List<Object[]> location;
        String sql = "select pm.c_name as c_area_name,pm.c_state as c_state_name,pm.c_state_code as c_state_code,pm.c_district as c_city_name," +
                " ugsm.c_sh_name " +
                " FROM pincode_mst pm   " +
                " LEFT JOIN u_geo_state_mst ugsm ON ugsm.c_name = pm.c_state " +
                " where pm.c_c2code='C2INFO' and pm.c_code=:pinCode";

        Query locQuery = entityManager.createNativeQuery(sql);
        locQuery.setParameter("pinCode", pincode);
        location = this.getResultList(locQuery);

        if (location.isEmpty()) {
            throw new RecordNotFoundException(Messages.RECORD_NOT_FOUND);
        }
        AddressModelBO addressModelBO = new AddressModelBO();
        for (Object[] objects : location) {
            String area = helper.getString(objects[0]).equals("") ? null : helper.getString(objects[0]);
            assert area != null;
            String[] areaArr = area.split(",");
            JsonArray array = new JsonArray();
            for (String a : areaArr) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("c_area_name", a);
                jsonObject.addProperty("c_area_code","");
                array.add(jsonObject);
            }
            addressModelBO.setAreaList(array);
            addressModelBO.setCityCode("");
            addressModelBO.setStateName(helper.getString(objects[1]).equals("") ? null : helper.getString(objects[1]));
            addressModelBO.setStateCode(helper.getString(objects[2]).equals("") ? null : helper.getString(objects[2]));
            addressModelBO.setCityName(helper.getString(objects[3]).equals("") ? null : helper.getString(objects[3]));
            addressModelBO.setShStateName(helper.getString(objects[4]));

        }
        return addressModelBO;
    }

    private LegalIdentitiesEntity getStoreLegalIdentities(Long userId, StoreCombineBO store, Long nLegalId) {
        LegalIdentitiesEntity legal;
        if (nLegalId != null) {
            legal = legalIdentitiesRepo.getOne(nLegalId);
        } else {
            legal = new LegalIdentitiesEntity();
        }
        legal.setCDrugLicenseNo1(store.getDrugLicenseNo1());
        legal.setIdTime(userId, helper.getCurrentTime());
        return legal;
    }

    private ContactDetailEntity getStoreContact(Long userId, StoreCombineBO store, Long nContactId) {
        ContactDetailEntity contact;
        if (nContactId != null) {
            contact = contactDetailRepo.getOne(nContactId);
        } else {
            contact = new ContactDetailEntity();
        }
        contact.setCCityName(store.getCityName());
        contact.setCCityCode(store.getCityCode());
        contact.setCAreaCode(store.getAreaCode());
        contact.setCAreaName(store.getAreaName());
        contact.setCStateCode(store.getStateCode());
        contact.setCStateName(store.getStateName());
        contact.setCAddress1(store.getAddress1());
        contact.setCAddress2(store.getAddress2());
        contact.setCPin(store.getPinCode());
        contact.setCEmailId(store.getEmailId());
        contact.setCLandmark(store.getCLandmark());
        contact.setIdTime(userId, helper.getCurrentTime());
        return contact;
    }

    @Override
    public void updateBranch(LcHeaderBO header, Long branchId, BranchDetailsBO branch) throws RecordNotFoundException, InvalidRequestException {
        FirmEntity firmEntity = getFirmById(branchId);
        if (firmEntity != null && !firmEntity.getCStatus().equals(Constants.STATUS_INACTIVE)) {
            firmRepo.save(setFirmDetails(header, branch, firmEntity));
            firmEntity.setBrCode("000");
            firmRepo.save(firmEntity);
        } else {
            throw new RecordNotFoundException("Branch not found!");
        }
    }


    private FirmEntity setFirmDetails(LcHeaderBO header, BranchDetailsBO branch, FirmEntity firmEntity) throws InvalidRequestException {
        firmEntity.setCCityName(branch.getCityName());
        firmEntity.setCCityCode(branch.getCityCode());
        firmEntity.setCAreaCode(branch.getAreaCode());
        firmEntity.setCAreaName(branch.getAreaName());
        firmEntity.setCStateCode(branch.getStateCode());
        firmEntity.setCStateName(branch.getStateName());
        firmEntity.setCEmail(branch.getEmailId());
        firmEntity.setCPin(branch.getPinCode());
        firmEntity.setCGstNo(branch.getGstNumber());
        firmEntity.setCGstType(branch.getGstType());
        firmEntity.setCMobileNo(branch.getMobileNo());
        firmEntity.setCName(branch.getFirmName());
        firmEntity.setCImageUrl(branch.getFirmImage());
        if (branch.getCType() != null) {
            if (Objects.equals(branch.getCType(), Constants.ROLE_SELLER) || Objects.equals(branch.getCType(), Constants.ROLE_BUYER)) {
                firmEntity.setCType(branch.getCType());
            } else {
                throw new IllegalArgumentException("Type must be S -> seller or B -> buyer");
            }
        }
        firmEntity.setContactDetail(getContactDetails(header.getUserId(), branch, firmEntity.getContactDetail() == null ? null : firmEntity.getContactDetail().getNContactId()));
        firmEntity.setLegalIdentities(getLegalIdentities(header.getUserId(), branch, firmEntity.getLegalIdentities() == null ? null : firmEntity.getLegalIdentities().getNLegalId()));
        firmEntity.setDocumentDetail(getDocumentDetails(header.getUserId(), branch, firmEntity.getDocumentDetail() == null ? null : firmEntity.getDocumentDetail().getNDocumentsId()));
        firmEntity.setIdTime(header.getUserId(), helper.getCurrentTime());
        firmEntity.setCStatus(Constants.STATUS_ACTIVE);
        firmEntity.setC2Code(firmEntity.getC2Code() == null ? "LO" : firmEntity.getC2Code());
        //firmEntity.setBrCode(firmEntity.getBrCode() == null ? helper.getString(header.getFirmId()) : firmEntity.getBrCode());
        return firmEntity;
    }

    private DocumentEntity getDocumentDetails(Long userId, BranchDetailsBO branch, Long nDocumentsId) {
        DocumentEntity document;
        if (nDocumentsId != null) {
            document = otherDocumentsRepo.getOne(nDocumentsId);
        } else {
            document = new DocumentEntity();
        }
        document.setCAuthorityLetter(branch.getAuthLetter());
        document.setCAuthorityLetterImg(branch.getAuthLetterImg());
        document.setCBankStatement(branch.getBankStatement());
        document.setCBankStatementImg(branch.getBankStatementImg());
        document.setCElectricityBill(branch.getElectricityBill());
        document.setCElectricityBillImg(branch.getElectricityBillImg());
        document.setCItPanNo(branch.getItPanNo());
        document.setCItPanNoImg(branch.getItPanNoImg());
        document.setCPanNo(branch.getPanNo());
        document.setCPanNoImg(branch.getPanImg());
        document.setCPartnershipDeed(branch.getPartnershipDeed());
        document.setCPartnershipDeedImg(branch.getPartnershipDeedImg());
        document.setCRentAgreement(branch.getRentAgreement());
        document.setCRentAgreementImg(branch.getRentAgreementImg());
        document.setCTanNo(branch.getTanNo());
        document.setCTanNoImg(branch.getTanNoImg());
        document.setIdTime(userId, helper.getCurrentTime());
        return document;
    }

    private LegalIdentitiesEntity getLegalIdentities(Long userId, BranchDetailsBO firm, Long nLegalId) throws InvalidRequestException {
        LegalIdentitiesEntity legal;
        LocalDate expiryDate;

        if (nLegalId != null) {
            legal = legalIdentitiesRepo.getOne(nLegalId);
        } else {
            legal = new LegalIdentitiesEntity();
        }

        legal.setCDrugLicenseNo1(firm.getDrugLicenseNo1());
        legal.setCDrugLicenseNo1Img(firm.getDrugLicenseNo1Img());

        if (firm.getDrugLicenseNo1ExpiryDate() != null && !firm.getDrugLicenseNo1ExpiryDate().equals("")) {
            expiryDate = helper.getLocalDate(firm.getDrugLicenseNo1ExpiryDate());
            if (expiryDate.isAfter(helper.getCurrentDate())) {
                legal.setDDrugLicenseNo1ExpiryDate(expiryDate);
            } else {
                throw new InvalidRequestException("'Drug_license_no1_expiry_date'","Expiry date should be Future Date!");
            }
        }

        legal.setCDrugLicenseNo2(firm.getDrugLicenseNo2());
        legal.setCDrugLicenseNo2Img(firm.getDrugLicenseNo2img());

        if (firm.getDrugLicenseNo2ExpiryDate() != null && !firm.getDrugLicenseNo2ExpiryDate().equals("")) {
            expiryDate = helper.getLocalDate(firm.getDrugLicenseNo2ExpiryDate());
            if (expiryDate.isAfter(helper.getCurrentDate())) {
                legal.setDDrugLicenseNo2ExpiryDate(expiryDate);
            } else {
                throw new InvalidRequestException("'Drug_license_no2_expiry_date'","Expiry date should be Future Date!");
            }
        }

        legal.setCDrugLicenseNo3(firm.getDrugLicenseNo3());
        legal.setCDrugLicenseNo3Img(firm.getDrugLicenseNo3img());

        if (firm.getDrugLicenseNo3ExpiryDate() != null && !firm.getDrugLicenseNo3ExpiryDate().equals("")) {
            expiryDate = helper.getLocalDate(firm.getDrugLicenseNo3ExpiryDate());
            if (expiryDate.isAfter(helper.getCurrentDate())) {
                legal.setDDrugLicenseNo3ExpiryDate(expiryDate);
            } else {
                throw new InvalidRequestException("'Drug_license_no3_expiry_date'","Expiry date should be Future Date!");
            }
        }

        legal.setCNarcoticNo(firm.getNarcoticNo());
        legal.setCNarcoticNoImg(firm.getNarcoticImg());
        legal.setIdTime(userId, helper.getCurrentTime());
        return legal;
    }

    private ContactDetailEntity getContactDetails(Long userId, BranchDetailsBO firm, Long nContactId) {
        ContactDetailEntity contact;
        if (nContactId != null) {
            contact = contactDetailRepo.getOne(nContactId);
        } else {
            contact = new ContactDetailEntity();
        }
        contact.setCContactName(firm.getContactName());
        contact.setCAddress1(firm.getAddress1());
        contact.setCAddress2(firm.getAddress2());
        contact.setCCityCode(firm.getCityCode());
        contact.setCCityName(firm.getCityName());
        contact.setCAreaCode(firm.getAreaCode());
        contact.setCAreaName(firm.getAreaName());
        contact.setCStateCode(firm.getStateCode());
        contact.setCStateName(firm.getStateName());
        contact.setCEmailId(firm.getEmailId());
        contact.setCPin(firm.getPinCode());
        contact.setCMobileNo(firm.getMobileNo());
        contact.setCLandmark(firm.getCLandmark());
        contact.setIdTime(userId, helper.getCurrentTime());
        return contact;
    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/"));
    }

    @Override
    public JsonArray searchLc1(String columnName, String searchKey, int page, int limit) {
        List<Object[]> cNameList;
        JsonObject res;
        JsonArray array = new JsonArray();

        String sql = "SELECT DISTINCT c_name, c_code FROM " + columnName + " where c_name like '" + searchKey + "%'";
        Query lcQuery = entityManager.createNativeQuery(sql);
        cNameList = this.getResultList(lcQuery, page, limit);

        if (!cNameList.isEmpty()) {
            for (Object[] objects : cNameList) {
                res = new JsonObject();
                res.addProperty("c_name", helper.getString(objects[0]));
                res.addProperty("c_code", helper.getString(objects[1]));
                array.add(res);
            }
        }
        return array;
    }

    @Override
    public int getLc1SearchCount(String colName, String searchKey) {
        List<Object[]> cNameList;
        int count = 0;

        String sql = "SELECT c_name FROM " + colName + " where c_name like '%" + searchKey + "%'";
        Query lcQuery = entityManager.createNativeQuery(sql);
        cNameList = this.getResultList(lcQuery);

        if (!cNameList.isEmpty()) {
            count = cNameList.size();
        }
        return count;
    }

    @Override
    public int getLc1StoreCount(String cMobileNo) {
        BigInteger count = BigInteger.ZERO;

        String sql = "SELECT COUNT(*) FROM ( " + getLC1FirmQuery() + " ) DUMMY";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("mobileNo", cMobileNo);
        Object result = this.getSingleResult(query);

        if (result != null) {
            count = (BigInteger) result;
        }

        return count.intValue();
    }

    @Override
    public List<FirmEntity> saveStore(String cMobileNo, Long nUserId, List<String> c2CodeAndCustCodeList, List<String> remainingComboList) {
        String sql = getUActQuery();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("list", c2CodeAndCustCodeList);
        List<Object[]> list = this.getResultList(query);

        List<String> firmNames = new ArrayList<>();
        List<FirmEntity> firmList = new ArrayList<>();
        setAndSaveFirm(cMobileNo, nUserId, list, firmList, firmNames);

        if (remainingComboList.size() > 0) {
            List<Object[]> resultList = getCustActMstQuery(remainingComboList);
            setAndSaveFirm(cMobileNo, nUserId, resultList, firmList, firmNames);
        }
        return firmList;
    }

    private List<Object[]> getCustActMstQuery(List<String> remainingComboList) {
        String sql = " SELECT cad.c_gst_no, pm.c_state, pm.c_state_code, cam.c_city, ugcm.c_code, cam.c_area_code, " +
                "   CONCAT(cam.c_name,'-',COALESCE(lccm.c_name,'')), cam.c_pin, cam.c_drug_licence_no_1, cam.c_drug_licence_no_2, cam.c_drug_licence_no_3, " +
                "   cam.c_add_1 AS add1,cam.c_add_2 AS add2,cam.c_add_3 AS add3, cam.c_email_id, '' as c_ucode,  cam.c_contact_person, '' as c_csquare_code, cam.d_dl_date " +
                "   FROM cust_act_mst cam " +
                "   JOIN lc_c2code_mst lccm ON cam.c_c2code = lccm.c_code " +
                "   LEFT JOIN pincode_mst pm ON pm.c_code = cam.c_pin " +
                "   LEFT JOIN u_geo_city_mst ugcm ON ugcm.c_name = cam.c_city " +
                "   LEFT JOIN cust_area_mst ugam ON ugam.c_c2code = cam.c_c2code AND ugam.c_code = cam.c_area_code " +
                "   LEFT JOIN cust_act_det cad ON cad.c_c2code = cam.c_c2code AND cam.c_code = cad.c_cust_code " +
                "   WHERE CONCAT(cam.c_c2code,cam.c_code) IN :list AND lccm.n_order_flag = 1 AND lccm.n_non_visible_flag = 0 AND cam.n_lock = 0  ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("list", remainingComboList);
        return this.getResultList(query);
    }

    private void setAndSaveFirm(String cMobileNo, Long nUserId, List<Object[]> list, List<FirmEntity> firmList, List<String> firmNames) {
        FirmEntity lc1Firm;
        if (!list.isEmpty()) {
            for (Object[] firm : list) {
                String uCode = helper.getString(firm[15]);
                String c2Code = helper.getString(firm[17]) == null ? "LO" : (helper.getString(firm[17]).equals("") ? "LO" : helper.getString(firm[17]));
                List<FirmEntity> firmEntity = new ArrayList<>();
                if (!helper.isEmpty(uCode)) {
                    firmEntity = firmRepo.getByC2CodeAndUcode(c2Code, uCode);
                }
                if (firmEntity == null || firmEntity.size() == 0) {
                    lc1Firm = new FirmEntity();
                    lc1Firm.setCGstNo(helper.getString(firm[0]));
                    lc1Firm.setCStateName(helper.getString(firm[1]));
                    lc1Firm.setCStateCode(helper.getString(firm[2]));
                    lc1Firm.setCCityName(helper.getString(firm[3]));
                    lc1Firm.setCCityCode(helper.getString(firm[4]));
                    lc1Firm.setCAreaCode(helper.getString(firm[5]));
                    lc1Firm.setCName(helper.getString(firm[6]));
                    lc1Firm.setCPin(helper.getString(firm[7]));

                    String licenseNo1 = helper.getString(firm[8]);
                    String licenseNo2 = helper.getString(firm[9]);
                    String licenseNo3 = helper.getString(firm[10]);
                    String address1 = helper.getString(firm[11]);
                    String address2 = helper.getString(firm[12]);
                    String address3 = helper.getString(firm[13]);
                    String email = helper.getString(firm[14]);
                    String contactPerson = helper.getString(firm[16]);
                    String expiry = helper.getString(firm[18]);
                    lc1Firm.setCUcode(helper.getString(firm[15]));
                    lc1Firm.setBrCode("000");
                    lc1Firm.setLegalIdentities(getLC1Legal(licenseNo1, licenseNo2, licenseNo3, expiry));
                    lc1Firm.setCMobileNo(cMobileNo);
                    lc1Firm.setCEmail(email);
                    lc1Firm.setC2Code(c2Code);
                    ContactDetailEntity contactDetail = getLC1Contact(address1, address2, address3, cMobileNo, email);
                    contactDetail.setCContactName(contactPerson);
                    lc1Firm.setContactDetail(contactDetail);
                    lc1Firm.setCType(Constants.ROLE_BUYER);
                    lc1Firm.setCStatus(Constants.STATUS_ACTIVE);
                    lc1Firm = createCombinedStore(nUserId, lc1Firm);
                    createFirmUser(nUserId, lc1Firm);
                    firmList.add(lc1Firm);
                } else {
                    firmList.addAll(firmEntity);
                }
            }
        }
    }

    @Override
    public void saveC2CodeAndBrCode(List<FirmEntity> firmList, Long nUserId, List<String> c2CodeAndCustCodeList, List<String> remainingComboList) {
        String c2Code = "";
        String brCode = "";
        List<Object[]> list;
        String sql;

        int count = 0;
        for (FirmEntity firm : firmList) {
            if (remainingComboList.size() > 0 && (firm.getCUcode().equals("") || firm.getCUcode() == null)) {
                sql = " SELECT cam.c_code, cam.c_c2code " +
                        " FROM cust_act_mst cam " +
                        " WHERE CONCAT(cam.c_c2code,cam.c_code) = :sellerBuyerCode ";
                Query query = entityManager.createNativeQuery(sql);
                query.setParameter("sellerBuyerCode", remainingComboList.get(count));
                list = this.getResultList(query);
                count++;
            } else {
                sql = "SELECT u_stockiest_cust_code, c_stockiest_code " +
                        "   FROM u_stockiest_customer_map uscm " +
                        "   WHERE uscm.c_ucode = :uCode AND " +
                        "   CONCAT(uscm.c_stockiest_code,uscm.u_stockiest_cust_code) IN :list ";
                Query query = entityManager.createNativeQuery(sql);
                query.setParameter("uCode", firm.getCUcode());
                query.setParameter("list", c2CodeAndCustCodeList);
                list = this.getResultList(query);
            }

            if (!list.isEmpty()) {
                for (Object[] obj : list) {
                    c2Code = helper.getString(obj[1]);
                    brCode = helper.getString(obj[0]);
                    saveSelerPriority(nUserId, firm.getNFirmId(), brCode, c2Code);
                    saveFirmSellers(nUserId, firm, brCode, c2Code);
                }
            }
//            firm.setBrCode(helper.getLongStringValue(firm.getNFirmId()));
            if (firm.getC2Code().equals("LO")) {
                firm.setC2Code("L" + firm.getNFirmId());
            }
            firmRepo.save(firm);
        }
    }

    private void saveSelerPriority(Long nUserId, Long nFirmId, String brCode, String c2Code) {
        LcSellerBuyerPriorityEntity sellerPriorityEntity = new LcSellerBuyerPriorityEntity();
        sellerPriorityEntity.setCSellerCode(c2Code);
        sellerPriorityEntity.setCBuyerCode(brCode);
        sellerPriorityEntity.setNFirmId(nFirmId);
        sellerPriorityEntity.setDTime(helper.getCurrentTime());
        userSellerPriority.save(sellerPriorityEntity);
    }

    private String getUActQuery() {
        return "SELECT DISTINCT uam.c_gst_no, pm.c_state, pm.c_state_code, uam.c_city, ugcm.c_code, uam.c_geo_area_code, " +
                " uam.c_name, uam.c_pin, uam.c_dl_no_1, uam.c_dl_no_2, uam.c_dl_no_3, " +
                " uam.c_add_1, uam.c_add_2, uam.c_add_3, uam.c_email, uam.c_ucode, uam.c_main_contact_person, " +
                " uam.c_csquare_code, uam.d_dl_date " +
                " FROM u_stockiest_customer_map uscm " +
                " JOIN u_act_mst uam ON uscm.c_ucode = uam.c_ucode " +
                " LEFT JOIN pincode_mst pm on pm.c_code = uam.c_pin " +
                " LEFT JOIN u_geo_city_mst ugcm on ugcm.c_name = uam.c_city " +
                " WHERE CONCAT(uscm.c_stockiest_code,uscm.u_stockiest_cust_code) IN :list " +
                " GROUP BY uam.c_ucode ";
    }

    @Override
    public List<JsonObject> fetchBranchList(String c2Code, SearchBO searchBO) throws RecordNotFoundException {
        Pageable pageable = PageRequest.of(searchBO.getPage(), searchBO.getLimit(), Sort.by("tCreatedAt").descending());
        List<JsonObject> list = new ArrayList<>();
        List<Long> firmIdList = new ArrayList<>();
        JsonObject obj;
        List<FirmEntity> firmList = firmRepo.getByC2Code(c2Code, pageable);

        if (firmList.size() > 0) {
            if (searchBO.getSearchTerm() != null) {
                for (FirmEntity firm : firmList) {
                    firmIdList.add(firm.getNFirmId());
                }

                List<FirmEntity> searchList = firmRepo.getByNameAndId(firmIdList, searchBO.getSearchTerm());
                if (searchList.size() > 0) {
                    for (FirmEntity entity : firmList) {
                        obj = new JsonObject();
                        setBranchListRes(list, obj, entity);
                    }
                } else {
                    throw new RecordNotFoundException("Record Not Found!");
                }
            } else {
                for (FirmEntity entity : firmList) {
                    obj = new JsonObject();
                    setBranchListRes(list, obj, entity);
                }
            }
        } else {
            throw new RecordNotFoundException("Record Not Found!");
        }
        return list;
    }

    private void setBranchListRes(List<JsonObject> list, JsonObject obj, FirmEntity entity) {
        obj.addProperty("n_firm_id", entity.getNFirmId());
        obj.addProperty("c_br_code", entity.getNFirmId());
        obj.addProperty("c_branch_name", entity.getCName());
        obj.addProperty("c_area_name", entity.getCAreaName());
        obj.addProperty("c_state_name", entity.getCStateName());
        obj.addProperty("c_city_name", entity.getCCityName());
        list.add(obj);
    }

    @Override
    public List<String> getC2CodeCombination(String cMobileNo, int page, int size) throws RecordNotFoundException {
        List<String> list = new ArrayList<>();
        lcC2RegistrationCombination(cMobileNo, list, page, size);
        custActMstCombination(cMobileNo, list, page, size);
        return list;
    }

    @Override
    public List<String> getRemainingCombination(List<String> c2CodeAndCustCodeList, int page, int size) throws RecordNotFoundException {
        List<String> mappedComboList = new ArrayList<>();
        List<String> allMappedComboList = new ArrayList<>();
        String sql = "SELECT CONCAT(uscm.c_stockiest_code,uscm.u_stockiest_cust_code) " +
                " FROM u_stockiest_customer_map uscm " +
                " WHERE CONCAT(uscm.c_stockiest_code,uscm.u_stockiest_cust_code) IN :list " +
                " ORDER BY uscm.d_ldate DESC ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("list", c2CodeAndCustCodeList);
        List<Object[]> resultList = this.getResultList(query);

        if(resultList.size() > 0) {
            allMappedComboList = new ArrayList<>(c2CodeAndCustCodeList);
            for (int i = 0; i < resultList.size(); i++) {
                mappedComboList.add(helper.getString(resultList.get(i)));
            }
            allMappedComboList.removeAll(mappedComboList);
        } else {
            String custSql = " SELECT CONCAT(cam.c_c2code,cam.c_code) " +
                    " FROM cust_act_mst cam " +
                    " WHERE CONCAT(cam.c_c2code,cam.c_code) IN :list " +
                    " ORDER BY cam.d_ldate DESC ";
            query = entityManager.createNativeQuery(custSql);
            query.setParameter("list", c2CodeAndCustCodeList);
            List<Object[]> list = this.getResultList(query);

            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++)
                allMappedComboList.add(helper.getString(list.get(i)));
            }
        }
        return allMappedComboList;
    }

    @Override
    public JsonObject getMobileNumber(String c_mid) throws RecordNotFoundException {
        String sql = " SELECT lmoccm.c_c2code,lmmo.c_store_mobile FROM lc_mid_merchant_onbording lmmo " +
                " JOIN lc_merchant_onbording_c2code_mapping lmoccm ON lmmo.c_MID = lmoccm.c_MID " +
                " WHERE lmmo.c_MID ='" + c_mid + "' " +
                " ORDER BY lmoccm.c_c2code ASC";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> resultList = this.getResultList(query);
        if (resultList == null) {
            throw new RecordNotFoundException("Record Not Found");
        }
        JsonObject jsonObject = new JsonObject();
        for (Object[] objects : resultList) {
            int i = -1;
            jsonObject.addProperty("c_c2code", helper.getString(objects[0]));
            jsonObject.addProperty("c_store_mobile", helper.getString(objects[1]));
        }
        return jsonObject;
    }

    private void custActMstCombination(String cMobileNo, List<String> list, int page, int size) {
        String sql = " SELECT DISTINCT CONCAT(cam.c_c2code,cam.c_code) FROM cust_act_mst cam " +
                " LEFT JOIN act_contact_det det ON cam.c_c2code = det.c_c2code AND cam.c_code = det.c_code " +
                " LEFT JOIN lc_c2code_mst lccm ON cam.c_c2code = lccm.c_code " +
                " WHERE (cam.c_mobile LIKE :mobile OR cam.c_phone_1 LIKE :mobile OR cam.c_phone_2 LIKE :mobile) AND cam.n_customer = 1" +
                " AND cam.c_c2code <> '03C000' AND lccm.n_order_flag = 1 AND lccm.n_non_visible_flag = 0" +
                " AND cam.c_c2code <> '' AND cam.c_code <> '' AND cam.n_lock = 0 " +
                " ORDER BY cam.d_ldate DESC ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("mobile", helper.getContainLikeQueryString(cMobileNo));
        List<Object[]> resultList = this.getResultList(query, page, size);

        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                list.add(helper.getString(resultList.get(i)));
            }
        }
    }

    private void lcC2RegistrationCombination(String cMobileNo, List<String> list, int page, int size) {
        String sql1 = "SELECT DISTINCT CONCAT(det.c_supp_code,det.c_chem_code) " +
                " FROM lc_c2_registration_mst mst " +
                " JOIN lc_c2_registration_det det ON mst.n_id = det.n_mst_id " +
                " LEFT JOIN lc_c2code_mst lccm ON det.c_supp_code = lccm.c_code " +
                " WHERE mst.n_mobile_no = :mobile AND lccm.n_order_flag = 1 AND lccm.n_non_visible_flag = 0 " +
                " AND det.c_supp_code <> '' AND det.c_chem_code <> '' " +
                " ORDER BY det.t_ltime DESC ";
        Query c2RegistrationQuery = entityManager.createNativeQuery(sql1);
        c2RegistrationQuery.setParameter("mobile", cMobileNo);
        List<Object[]> resultList = this.getResultList(c2RegistrationQuery, page, size);

        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                list.add(helper.getString(resultList.get(i)));
            }
        }
    }

    @Override
    public Set<String> getMobileNumbersList(JsonObject request, int page, int size) {
        Set<String> mobileNoSet = new HashSet<>();
        getMobileNoFromC2RegistrationTables(request, mobileNoSet, page, size);
        getMobileNoFromCustActMst(request, mobileNoSet, page, size);
        return mobileNoSet;
    }

    @Override
    public List<FirmEntity> getByMobileNo(String cMobileNo) {
        return firmRepo.getByMobileNo(cMobileNo);
    }

    @Override
    public List<String> getFirmSellersCombo(List<FirmEntity> firmEntities) {
        List<String> list = new ArrayList<>();
        for (FirmEntity entity : firmEntities) {
            List<FirmSellersEntity> firmSellersList = firmSellersRepo.getByFirmId(entity.getNFirmId());
            if (firmSellersList.size() > 0) {
                for (FirmSellersEntity firmSellers : firmSellersList) {
                    list.add(firmSellers.getCSellerCode() + firmSellers.getCBuyerCode());
                }
            }
        }
        return list;
    }

    @Override
    public List<FirmEntity> saveNewLC1Store(List<String> firmNames, String cMobileNo, Long nUserId, List<String> c2CodeAndCustCodeList, List<String> remainingComboList) {
        String sql = getUActQuery();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("list", c2CodeAndCustCodeList);
        List<Object[]> list = this.getResultList(query);

        List<FirmEntity> firmList = new ArrayList<>();
        setAndSaveFirm(cMobileNo, nUserId, list, firmList, firmNames);

        if (remainingComboList.size() > 0) {
            List<Object[]> resultList = getCustActMstQuery(remainingComboList);
            setAndSaveFirm(cMobileNo, nUserId, resultList, firmList, firmNames);
        }
        return firmList;
    }

    private void getMobileNoFromC2RegistrationTables(JsonObject request, Set<String> mobileNoSet, int page, int size) {
        String sql = "SELECT distinct mst.n_mobile_no " +
                "   FROM lc_c2_registration_mst mst " +
                "   JOIN lc_c2_registration_det det ON mst.n_id = det.n_mst_id " +
                "   LEFT JOIN lc_c2code_mst lccm ON det.c_supp_code = lccm.c_code " +
                "   WHERE det.t_ltime >= :fromDate AND det.t_ltime <= :toDate AND lccm.n_order_flag = 1 AND lccm.n_non_visible_flag = 0 " +
                "    AND length(mst.n_mobile_no) = 10 " +
                "    AND det.c_supp_code <> '' AND det.c_chem_code <> '' " +
                "    AND CONCAT(det.c_supp_code,det.c_chem_code) " +
                "       NOT IN (SELECT CONCAT(cdm.c_c2code,cdm.c_code) " +
                "       FROM cust_deleted_master cdm " +
                "       WHERE cdm.c_c2code = det.c_supp_code AND cdm.c_code = det.c_chem_code AND cdm.n_mst_type = 1) " +
                "   ORDER BY det.t_ltime DESC ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("fromDate", request.get("d_from_date").getAsString());
        query.setParameter("toDate", request.get("d_to_date").getAsString());
        List<Object[]> resultList = this.getResultList(query, page, size);

        if (resultList.size() > 0) {
            for (int i = 0; i < resultList.size(); i++) {
                mobileNoSet.add(helper.getString(resultList.get(i)));
            }
        }
    }

    private void getMobileNoFromCustActMst(JsonObject request, Set<String> mobileNoSet, int page, int size) {
        String sql = " SELECT DISTINCT cam.c_mobile, cam.c_phone_1, cam.c_phone_2 " +
                "   FROM cust_act_mst cam " +
                "   LEFT JOIN act_contact_det det ON cam.c_c2code = det.c_c2code AND cam.c_code = det.c_code " +
                "   LEFT JOIN lc_c2code_mst lccm ON cam.c_c2code = lccm.c_code " +
                "   WHERE cam.d_ldate >= :fromDate AND cam.d_ldate <= :toDate " +
                "   AND cam.n_customer = 1 AND cam.c_c2code <> '03C000' AND lccm.n_order_flag = 1 AND lccm.n_non_visible_flag = 0 " +
                "   AND (length(cam.c_mobile) = 10 OR length(cam.c_phone_1) = 10 OR length(cam.c_phone_2) = 10) " +
                "   AND cam.c_c2code <> '' AND cam.c_code <> '' " +
                "   AND CONCAT(cam.c_c2code,cam.c_code) " +
                "       NOT IN (SELECT CONCAT(cdm.c_c2code,cdm.c_code) " +
                "       FROM cust_deleted_master cdm " +
                "       WHERE cam.c_c2code = cdm.c_c2code AND cam.c_code = cdm.c_code AND cdm.n_mst_type = 1)   " +
                "   ORDER BY cam.d_ldate DESC ";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("fromDate", request.get("d_from_date").getAsString());
        query.setParameter("toDate", request.get("d_to_date").getAsString());
        List<Object[]> resultList = this.getResultList(query, page, size);

        if (resultList.size() > 0) {
            for (Object[] obj : resultList) {
                String cMobile = helper.getString(obj[0]);
                String phone1 = helper.getString(obj[1]);
                String phone2 = helper.getString(obj[2]);

                Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Matcher mob = p.matcher(cMobile);
                Matcher ph1 = p.matcher(phone1);
                Matcher ph2 = p.matcher(phone2);

                if (cMobile.length() == 10 && StringUtils.isNumeric(cMobile) && !mob.find()) {
                    mobileNoSet.add(cMobile);
                }
                if (phone1.length() == 10 && StringUtils.isNumeric(phone1) && !ph1.find()) {
                    mobileNoSet.add(phone1);
                }
                if (phone2.length() == 10 && StringUtils.isNumeric(phone2) && !ph2.find()) {
                    mobileNoSet.add(phone2);
                }
            }
        }
    }

    @Override
    public List<JsonObject> fetchBranchListByPinCode(String c2Code, String pinCode, PageBO pageBO) {
        Pageable pageable = PageRequest.of(pageBO.getPage(), pageBO.getLimit(), Sort.by("tCreatedAt").descending());
        List<JsonObject> list = new ArrayList<>();
        JsonObject obj;
        List<FirmEntity> firmList = firmRepo.getByC2Code(c2Code, pageable);

        if (firmList.size() > 0) {
            firmList = firmList.stream().filter(x -> x.getCPin().equals(pinCode)).collect(Collectors.toList());
            for (FirmEntity entity : firmList) {
                obj = new JsonObject();
                setBranchListRes(list, obj, entity);
            }
        }
        return list;
    }

    @Override
    public List<String> getAllUserMobile(Long parentUserId) {
        List<String> list = new ArrayList<>();
        List<UserOwnerEntity> userIdList = userOwnerRepo.getAllChildUserId(parentUserId);
        List<Long> childUserIdList = new ArrayList<>();
        for (UserOwnerEntity entity : userIdList) {
            childUserIdList.add(entity.getChildUser());
        }
        if (childUserIdList.size() > 0) {
            List<Long> contactIdList = userDetailRepo.getContactIdList(childUserIdList);
            list = contactDetailRepo.getAllMobileNumber(contactIdList);
        }
        return list;
    }

    @Override
    public int updateFirmC2code() {
        String sql = "SELECT n_firm_id , c_ucode from firm  " +
                "WHERE c_c2code = 'LO' and (c_ucode!= '' and c_ucode is not null) " ;
        Query query = this.getQuery(sql);
        List<Object[]> resultList = this.getResultList(query);
        int count = 0;
        if(!resultList.isEmpty()) {
            List<String> firm_ucode = new ArrayList<String>();
            for (Object[] object : resultList) {
                firm_ucode.add(helper.getString(object[1]));
            }

            String sql1 = " select c_ucode, c_csquare_code from u_act_mst uam  " +
                    " where ( c_csquare_code!= ''  and c_csquare_code is not null) and c_ucode in :firm_ucode ";
            Query query1 = entityManager.createNativeQuery(sql1);
            query1.setParameter("firm_ucode", firm_ucode);

            List<Object[]> ucodeFromUact = this.getResultList(query1);
            if (!ucodeFromUact.isEmpty()) {
                for (Object[] object : ucodeFromUact) {
                     count = firmRepo.updateC2codeByucode(helper.getString(object[0]),helper.getString(object[1]),helper.getCurrentTime());
                }
            }

        }
        return count;
    }

    @Override
    public void savePinCodeReq(UserDetailEntity userDetailEntity, TSRegisterBO registerBO) {
        List<TSPinCodeReqEntity> entityList = pinCodeReqRepo.getByPin(registerBO.getC2Code(), registerBO.getPinCode());
        TSPinCodeReqEntity entity = new TSPinCodeReqEntity();
        entity.setCPin(registerBO.getPinCode());
        entity.setC2Code(registerBO.getC2Code());
        entity.setUserDetailEntity(userDetailEntity);
        entity.setTCreatedAt(helper.getCurrentTime());
        if (entityList.size() > 0) {
            entity.setServiceActiveStatus(entityList.get(0).getServiceActiveStatus());
        } else {
            entity.setServiceActiveStatus("N");
        }
        pinCodeReqRepo.save(entity);
    }

    @Override
    public void saveFailedMobiles(String cMobileNo, String stackTrace) {
        CombineFailedMobileNoEntity entity = new CombineFailedMobileNoEntity();
        entity.setMobileNo(cMobileNo);
        entity.setExceptionMsg(stackTrace);
        entity.setTCreatedAt(helper.getCurrentTime());
        failedMobNoRepo.save(entity);
    }

    @Override
    public CombineCronTimeLogEntity saveLog(CombineCronTimeLogEntity logEntity) {
        return logRepo.save(logEntity);
    }

    @Override
    public List<FirmEntity> getStoresFromUactMstAndSave(String cMobileNo, Long nUserId) {
        String sql = getUActByMobileNoQuery();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("cMobile", cMobileNo);
        List<Object[]> list = this.getResultList(query);

        List<String> firmNames = new ArrayList<>();
        List<FirmEntity> firmList = new ArrayList<>();
        setAndSaveFirm(cMobileNo, nUserId, list, firmList, firmNames);

        return firmList;
    }

    private String getUActByMobileNoQuery() {
        return "SELECT DISTINCT uam.c_gst_no, pm.c_state, pm.c_state_code, uam.c_city, ugcm.c_code, uam.c_geo_area_code, " +
                " uam.c_name, uam.c_pin, uam.c_dl_no_1, uam.c_dl_no_2, uam.c_dl_no_3, " +
                " uam.c_add_1, uam.c_add_2, uam.c_add_3, uam.c_email, uam.c_ucode, uam.c_main_contact_person, " +
                " uam.c_csquare_code, uam.d_dl_date  " +
                " FROM u_stockiest_customer_map uscm " +
                " JOIN u_act_mst uam ON uscm.c_ucode = uam.c_ucode " +
                " LEFT JOIN pincode_mst pm on pm.c_code = uam.c_pin " +
                " LEFT JOIN u_geo_city_mst ugcm on ugcm.c_name = uam.c_city " +
                " WHERE uam.c_mobile = :cMobile " +
                " GROUP BY uam.c_ucode ";
    }

    @Override
    public void save(FirmEntity firm) {
        firmRepo.save(firm);
    }

    @Override
    public int getUactMstCount(String cMobileNo) {
        String sql = getUActByMobileNoQuery();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("cMobile", cMobileNo);
        List<Object[]> list = this.getResultList(query);
        return list.size();
    }
}
