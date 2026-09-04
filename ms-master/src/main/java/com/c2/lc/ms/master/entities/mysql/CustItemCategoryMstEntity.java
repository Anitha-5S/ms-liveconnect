package com.c2.lc.ms.master.entities.mysql;


import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "cust_item_category_mst")
@IdClass(CustItemCategorySubMstEntityPK.class)
public class CustItemCategoryMstEntity {


    private String c2Code;
    private String cCode;
    private String name;
    private String shName;
    private BigDecimal rst;
    private LocalDate lDate;
    private LocalDate aDate;
    private String createUser;
    private BigDecimal audited;
    private BigDecimal nPredefined;
    private BigDecimal discount;
    private BigDecimal points;
    private LocalDateTime lTime;
    private String itemCategoryHeadCode;
    private BigDecimal agePer;
    private String modiUser;
    private String imageUrl;
    private BigInteger salableOnline;
    private BigInteger displayOnline;
    private BigDecimal active;

    @Id
    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
    }

    @Id
    @Column(name = "c_code", nullable = false, length = 20)
    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    @Column(name = "c_name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "c_sh_name", nullable = false, length = 10)
    public String getShName() {
        return shName;
    }

    public void setShName(String shName) {
        this.shName = shName;
    }

    @Column(name = "n_rst", nullable = true)
    public BigDecimal getRst() {
        return rst;
    }

    public void setRst(BigDecimal rst) {
        this.rst = rst;
    }

    @Column(name = "d_ldate", nullable = true)
    public LocalDate getlDate() {
        return lDate;
    }

    public void setlDate(LocalDate lDate) {
        this.lDate = lDate;
    }

    @Column(name = "d_adate", nullable = true)
    public LocalDate getaDate() {
        return aDate;
    }

    public void setaDate(LocalDate aDate) {
        this.aDate = aDate;
    }

    @Column(name = "c_createuser", nullable = false, length = 10)
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Column(name = "n_audited", nullable = false)
    public BigDecimal getAudited() {
        return audited;
    }

    public void setAudited(BigDecimal audited) {
        this.audited = audited;
    }

    @Column(name = "n_predefined", nullable = false)
    public BigDecimal getnPredefined() {
        return nPredefined;
    }

    public void setnPredefined(BigDecimal nPredefined) {
        this.nPredefined = nPredefined;
    }

    @Column(name = "n_discount", nullable = false)
    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Column(name = "n_points", nullable = false)
    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Column(name = "t_ltime", nullable = true)
    public LocalDateTime getlTime() {
        return lTime;
    }

    public void setlTime(LocalDateTime lTime) {
        this.lTime = lTime;
    }

    @Column(name = "c_item_category_head_code", nullable = true, length = 6)
    public String getItemCategoryHeadCode() {
        return itemCategoryHeadCode;
    }

    public void setItemCategoryHeadCode(String itemCategoryHeadCode) {
        this.itemCategoryHeadCode = itemCategoryHeadCode;
    }

    @Column(name = "n_age_per", nullable = true)
    public BigDecimal getAgePer() {
        return agePer;
    }

    public void setAgePer(BigDecimal agePer) {
        this.agePer = agePer;
    }

    @Column(name = "c_modiuser", nullable = true, length = 10)
    public String getModiUser() {
        return modiUser;
    }

    public void setModiUser(String modiUser) {
        this.modiUser = modiUser;
    }

    @Column(name = "c_image_url", nullable = true, length = 300)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(name = "n_salable_online", nullable = true, length = 10)
    public BigInteger getSalableOnline() {
        return salableOnline;
    }

    public void setSalableOnline(BigInteger salableOnline) {
        this.salableOnline = salableOnline;
    }

    @Column(name = "n_display_online", nullable = true, length = 11)
    public BigInteger getDisplayOnline() {
        return displayOnline;
    }

    public void setDisplayOnline(BigInteger displayOnline) {
        this.displayOnline = displayOnline;
    }

    @Column(name = "n_active", nullable = true)
    public BigDecimal getActive() {
        return active;
    }

    public void setActive(BigDecimal active) {
        this.active = active;
    }




}
