package com.c2.lc.ms.customer.entities.customer;

import com.c2.lc.lib.db.DateAudit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
@NamedQuery(name = "Feedback.findAll", query = "SELECT f FROM FeedbackEntity f")
public class FeedbackEntity extends DateAudit implements Serializable {

    private static final long serialVersionUID = -5443534162176298849L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_feedback_id", unique = true, nullable = false)
    @SerializedName("n_feedback_id")
    @Expose(serialize = false)
    private Long nFeedbackId;

    @SerializedName("n_user_id")
    @Expose(serialize = false)
    @Column(name = "n_user_id", updatable = false, nullable = false)
    private Long nUserId;

    @SerializedName("n_firm_id")
    @Expose(serialize = false)
    @Column(name = "n_firm_id", updatable = false, nullable = false)
    private Long nFirmId;

    @Column(name = "c_feedback_type", length = 255)
    @SerializedName("c_feedback_type")
    private String cFeedbackType;

    @Column(name = "c_distributor_id", length = 10)
    @SerializedName("c_seller_code")
    private String cDistributorId;

    @Column(name = "n_branch_id")
    @SerializedName("c_branch_code")
    private Long nBranchId;

    @SerializedName("c_message")
    @Column(name = "c_query")
    @Size(max = 1000)
    private String cQuery;

    @Column(name = "c_file_url", length = 1024)
    @SerializedName("ax_upload_file")
    private String cFileUrl;

    public FeedbackEntity(Long userId, LocalDateTime currentTime) {
        super(userId, currentTime);
    }

    public FeedbackEntity() {
    }

    public Long getnFeedbackId() {
        return nFeedbackId;
    }

    public void setnFeedbackId(Long nFeedbackId) {
        this.nFeedbackId = nFeedbackId;
    }

    public Long getnUserId() {
        return nUserId;
    }

    public void setnUserId(Long nUserId) {
        this.nUserId = nUserId;
    }

    public Long getnFirmId() {
        return nFirmId;
    }

    public void setnFirmId(Long nFirmId) {
        this.nFirmId = nFirmId;
    }

    public String getcFeedbackType() {
        return cFeedbackType;
    }

    public void setcFeedbackType(String cFeedbackType) {
        this.cFeedbackType = cFeedbackType;
    }

    public String getnDistributorId() {
        return cDistributorId;
    }

    public void setnDistributorId(String cDistributorId) {
        this.cDistributorId = cDistributorId;
    }

    public Long getnBranchId() {
        return nBranchId;
    }

    public void setnBranchId(Long nBranchId) {
        this.nBranchId = nBranchId;
    }

    public String getcQuery() {
        return cQuery;
    }

    public void setcQuery(String cQuery) {
        this.cQuery = cQuery;
    }

    public String getcFileUrl() {
        return cFileUrl;
    }

    public void setcFileUrl(String cFileUrl) {
        this.cFileUrl = cFileUrl;
    }
}
