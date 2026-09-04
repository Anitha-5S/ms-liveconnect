package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.bo.PageBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.lib.utils.Constants;
import com.c2.lc.ms.customer.bos.BranchDetailsBO;
import com.c2.lc.ms.customer.bos.BranchListBo;
import com.c2.lc.ms.customer.bos.ListBranchModelBO;
import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.EcoUsersPK;
import com.c2.lc.ms.customer.entities.customer.FirmDefaultEntity;
import com.c2.lc.ms.customer.entities.customer.FirmEntity;
import com.c2.lc.ms.customer.entities.customer.FirmUserEntity;
import com.c2.lc.ms.customer.repos.comm.EcoUsersRepo;
import com.c2.lc.ms.customer.repos.customer.FirmDefaultRepo;
import com.c2.lc.ms.customer.repos.customer.FirmRepo;
import com.c2.lc.ms.customer.repos.customer.FirmUserRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.FirmBranchService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FirmBranchServiceImpl extends LcBaseServiceImpl implements FirmBranchService {

    @Autowired private FirmRepo firmRepo;
    @Autowired private FirmUserRepo firmUserRepo;
    @Autowired private FirmDefaultRepo firmDefaultRepo;
    @Autowired private EcoUsersRepo ecoUsersRepo;

    @Override
    public JsonArray searchBranchList(Long firmId, String searchString) {
        String sql = " SELECT fb.n_branch_id, f.c_name, f.c_city_name, f.c_area_name, f.c_landmark, f.c_pincode, f.c_image_url " +
                " FROM firm_branch fb " +
                " LEFT JOIN firm f ON f.n_firm_id = fb.n_branch_id " +
                " WHERE fb.n_firm_id = :parentId " +
                "       AND ( LOWER(f.c_name) LIKE LOWER(:search) " +
                "       OR LOWER(f.c_area_name) LIKE LOWER(:search) " +
                "       OR LOWER(f.c_landmark) LIKE LOWER(:search) ) ";
        Query query = this.getQuery(sql);
        query.setParameter("parentId", firmId);
        query.setParameter("search", helper.getContainLikeQueryString(searchString));

        List<Object[]> resultList = this.getResultList(query);
        JsonArray jsonArray = new JsonArray();
        for (Object[] objects : resultList) {
            int i = -1;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("n_branch_id", helper.getString(objects[++i]));
            jsonObject.addProperty("c_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_city_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_area_name", helper.getString(objects[++i]));
            jsonObject.addProperty("c_landmark", helper.getString(objects[++i]));
            jsonObject.addProperty("c_pincode", helper.getString(objects[++i]));
            jsonObject.addProperty("c_image_url", helper.getString(objects[++i]));
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    //TODO - discuss with FE to return firm entity as payload
    @Override
    public BranchDetailsBO getBranchDetails(Long branchId) throws RecordNotFoundException {
        FirmEntity firmEntity = firmRepo.getOne(branchId);
        BranchDetailsBO branchDetails = new BranchDetailsBO();
        try {
            if (!firmEntity.getCStatus().equals(Constants.STATUS_INACTIVE)) {
                branchDetails.setBranchCode(branchId.toString());
                branchDetails.setContactName(firmEntity.getContactDetail() == null ? "" : firmEntity.getContactDetail().getCContactName());
                branchDetails.setAddress1(firmEntity.getContactDetail() == null ? "" : firmEntity.getContactDetail().getCAddress1());
                branchDetails.setAddress2(firmEntity.getContactDetail() == null ? "" : firmEntity.getContactDetail().getCAddress2());
                branchDetails.setCityCode(firmEntity.getCCityCode());
                branchDetails.setCityName(firmEntity.getCCityName());
                branchDetails.setAreaCode(firmEntity.getCAreaCode());
                branchDetails.setAreaName(firmEntity.getCAreaName());
                branchDetails.setStateCode(firmEntity.getCStateCode());
                branchDetails.setStateName(firmEntity.getCStateName());
                branchDetails.setEmailId(firmEntity.getCEmail());
                branchDetails.setPinCode(firmEntity.getCPin());
                branchDetails.setMobileNo(firmEntity.getCMobileNo());
                branchDetails.setCLandmark(firmEntity.getContactDetail() == null ? "" : firmEntity.getContactDetail().getCLandmark());
                branchDetails.setAuthLetter(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCAuthorityLetter());
                branchDetails.setAuthLetterImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCAuthorityLetterImg());
                branchDetails.setBankStatement(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCBankStatement());
                branchDetails.setBankStatementImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCBankStatementImg());
                branchDetails.setElectricityBill(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCElectricityBill());
                branchDetails.setElectricityBillImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCElectricityBillImg());
                branchDetails.setItPanNo(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCItPanNo());
                branchDetails.setItPanNoImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCItPanNoImg());
                branchDetails.setPanNo(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCPanNo());
                branchDetails.setPanImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCPanNoImg());
                branchDetails.setPartnershipDeed(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCPartnershipDeed());
                branchDetails.setPartnershipDeedImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCPartnershipDeedImg());
                branchDetails.setRentAgreement(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCRentAgreement());
                branchDetails.setRentAgreementImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCRentAgreementImg());
                branchDetails.setTanNo(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCTanNo());
                branchDetails.setTanNoImg(firmEntity.getDocumentDetail() == null ? "" : firmEntity.getDocumentDetail().getCTanNoImg());
                branchDetails.setDrugLicenseNo1(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo1());
                branchDetails.setDrugLicenseNo1Img(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo1Img());
                branchDetails.setDrugLicenseNo1ExpiryDate(firmEntity.getLegalIdentities() == null ? "" : (firmEntity.getLegalIdentities().getDDrugLicenseNo1ExpiryDate() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo1ExpiryDate().toString()));
                branchDetails.setDrugLicenseNo2(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo2());
                branchDetails.setDrugLicenseNo2img(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo2Img());
                branchDetails.setDrugLicenseNo2ExpiryDate(firmEntity.getLegalIdentities() == null ? "" : (firmEntity.getLegalIdentities().getDDrugLicenseNo2ExpiryDate() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo2ExpiryDate().toString()));
                branchDetails.setDrugLicenseNo3(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo3());
                branchDetails.setDrugLicenseNo3img(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCDrugLicenseNo3Img());
                branchDetails.setDrugLicenseNo3ExpiryDate(firmEntity.getLegalIdentities() == null ? "" : (firmEntity.getLegalIdentities().getDDrugLicenseNo3ExpiryDate() == null ? "" : firmEntity.getLegalIdentities().getDDrugLicenseNo3ExpiryDate().toString()));
                branchDetails.setNarcoticNo(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCNarcoticNo());
                branchDetails.setNarcoticImg(firmEntity.getLegalIdentities() == null ? "" : firmEntity.getLegalIdentities().getCNarcoticNoImg());
                branchDetails.setGstNumber(firmEntity.getCGstNo());
                branchDetails.setGstType(firmEntity.getCGstType());
                branchDetails.setFirmName(firmEntity.getCName());
                branchDetails.setFirmImage(firmEntity.getCImageUrl());
                branchDetails.setCType(firmEntity.getCType());
            } else {
            throw new RecordNotFoundException("Branch not found!");
            }
        } catch(EntityNotFoundException e){
            throw new RecordNotFoundException("Branch not found!");
        }
        return branchDetails;
    }

    // TODO - should return the firm details from the firm_user table
    @Override
    public List<BranchListBo> getListOfBranch(LcHeaderBO headerBO, PageBO pageBO) {
        Pageable pageable = PageRequest.of(pageBO.getPage(), pageBO.getLimit());
        return getBranches(headerBO.getFirmId(), headerBO.getUserId(), pageable);
    }

    @Override
    public EcoUsers ecoUserUpdate(LcHeaderBO headerBO,String newBranch) throws RecordNotFoundException {

        EcoUsersPK pk = new EcoUsersPK(headerBO.getC2Code(), headerBO.getBrCode(), helper.getString(headerBO.getUserId()));
        Optional<EcoUsers> users = ecoUsersRepo.findById(pk);
        if (users.isEmpty() || users.get() == null) {
            log.debug(headerBO.getC2Code() + ":" + headerBO.getUserId() + ":" + headerBO.getBrCode());
            throw new RecordNotFoundException("Eco users not found");
        }
        FirmEntity firmEntity = firmRepo.findById(helper.getLong(newBranch)).get();
        EcoUsers ecoUsers = users.get();
        ecoUsersRepo.delete(ecoUsers);
        ecoUsers.setBrCode(firmEntity.getBrCode());
        ecoUsers.setC2Code(firmEntity.getC2Code());
        ecoUsers.setTerminalId(helper.getString(headerBO.getUserId()));
        ecoUsersRepo.save(ecoUsers);
        return ecoUsers;
    }

    private List<BranchListBo> getBranches(Long parentId, Long userId, Pageable pageable) {
        List<FirmUserEntity> firmUserEntities = firmUserRepo.getById(userId, pageable);
        List<BranchListBo> list = new ArrayList<>();
        if (firmUserEntities != null) {
            for (FirmUserEntity firmBranch : firmUserEntities) {
                BranchListBo listModel = new BranchListBo();
                FirmEntity branch = firmRepo.findById(firmBranch.getId().getNFirmId()).get();
                listModel.setBranchName(branch.getCName());
                listModel.setBranchCode(branch.getBrCode());
                listModel.setFirmId(branch.getNFirmId());
                listModel.setCCityName(branch.getCCityName());
                listModel.setCPincode(branch.getCPin());
                Optional<FirmDefaultEntity> firmDefaultEntity = firmDefaultRepo.findById(userId);
                if (firmDefaultEntity.isPresent() && firmDefaultEntity.get().getFirm().getNFirmId().equals(branch.getNFirmId())) {
                    listModel.setDefaultStatus(Constants.STATUS_YES);
                } else {
                    listModel.setDefaultStatus(Constants.STATUS_NO);
                }
                list.add(listModel);
            }
        }
        return list;
    }

    @Override
    public List<ListBranchModelBO> getBranchListDetails(Long firmId) {
        return getBranchListWithDetails(firmId);
    }

    private List<ListBranchModelBO> getBranchListWithDetails(Long parentId) {
        FirmEntity firmEntity = firmRepo.getOne(parentId);
        List<FirmUserEntity> firmUserEntities = firmEntity.getFirmUserEntities();
        List<ListBranchModelBO> list = new ArrayList<>();
        if (firmUserEntities != null) {
            for (FirmUserEntity firmBranch : firmUserEntities) {
                ListBranchModelBO listModel = new ListBranchModelBO();
                FirmEntity branch = firmBranch.getFirm();
                listModel.setCName(branch.getCName());
                listModel.setCAreaName(branch.getCAreaName());
                listModel.setCLandmark(branch.getContactDetail() == null ?
                        null : branch.getContactDetail().getCLandmark());
                listModel.setNBranchId(branch.getNFirmId());
                listModel.setCCityName(branch.getCCityName());
                listModel.setCImageUrl(branch.getCImageUrl());
                listModel.setCPincode(branch.getCPin());

                list.add(listModel);
            }
        }
        return list;
    }
}
