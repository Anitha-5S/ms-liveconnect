package com.c2.lc.ms.customer.services;

import com.c2.lc.lib.bo.LcHeaderBO;
import com.c2.lc.lib.exceptions.RecordNotFoundException;
import com.c2.lc.ms.customer.entities.customer.FeedbackEntity;
import com.c2.lc.ms.customer.repos.customer.FeedbackRepo;
import com.c2.lc.ms.customer.services.base.LcBaseServiceImpl;
import com.c2.lc.ms.customer.services.interfaces.FeedbackService;
import com.c2.lc.ms.customer.services.interfaces.SellerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.azure.storage.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class FeedbackServiceImpl extends LcBaseServiceImpl implements FeedbackService {
    @Autowired
    FeedbackRepo feedbackRepo;
    @Autowired
    SellerService sellerService;
    @PersistenceContext(unitName = "mysql")
    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveFeedback(Long userId, Long firmId, FeedbackEntity feedbackEntity) {
        feedbackEntity.setNCreatedBy(userId);
        feedbackEntity.setTCreatedAt(helper.getCurrentTime());
        feedbackEntity.setnUserId(userId);
        feedbackEntity.setnFirmId(firmId);
        feedbackRepo.save(feedbackEntity);
    }

//    @Override
//    public List<FeedbackEntity> getFeedbackByDistributorId(Long distributorId) throws RecordNotFoundException {
//        List<FeedbackEntity> list = feedbackRepo.findByDistributorId(distributorId);
//        if (list.isEmpty()) {
//            throw new RecordNotFoundException(distributorId, "Record not found!");
//        }
//        return list;
//    }

    @Override
    public JsonObject uploadDocument(Long userId, Long firmId, JsonObject data) throws StorageException, IOException, URISyntaxException {
        String docData = helper.getString(data.get("docData"));
        String docName = helper.getString(data.get("docName"));
        JsonObject response = new JsonObject();

        String path = "feedback/" + firmId + "/" + userId + "/" + docName;

        String uri = blobUpload(path, docData);
        response.addProperty("URI", uri);
        return response;
    }

    @Override
    public JsonArray getListDistributor(String mobileNo, LcHeaderBO lcHeaderBO) throws RecordNotFoundException {
        JsonArray distributor = new JsonArray();
        JsonObject response;
        String sellerCodes = sellerService.getBranchSeller(lcHeaderBO);
        if (helper.isEmpty(sellerCodes)) {
            throw new RecordNotFoundException("Firm seller not Found");
        }

        String sql = "SELECT lccm.c_code, lccm.c_name, lccm.c_email FROM lc_c2code_mst lccm " +
                " WHERE lccm.c_code IN (" + sellerCodes + ") ORDER BY lccm.c_name ASC ";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> distributorList = this.getResultList(query);

        if (!distributorList.isEmpty()) {
            String[] mail;
            JsonArray arr;
            for (Object[] obj : distributorList) {
                int i = -1;
                response = new JsonObject();
                arr = new JsonArray();
                response.addProperty("c_distributor_id", helper.getString(obj[++i]));
                response.addProperty("c_distributor_name", helper.getString(obj[++i]));
                mail = helper.getString(obj[++i]).split(";|\\,");
                for (String mailId : mail) {
                    arr.add(mailId);
                }
                response.add("j_email_id", arr);
                distributor.add(response);
            }
        }
        return distributor;
    }
}
