package com.c2.lc.ms.master.entities.mysql;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "lc_lo_c2code")
public class LcLoC2CodeEntity {
    private int nId;
    private String cC2Code;
    private Timestamp dAtime;
    private Timestamp dLtime;
    private Byte nInOutFlag;
    private Byte nActiveFlag;
    private Byte nLcOrderDownloadFlag;

    @Basic
    @Column(name = "n_id", nullable = false)
    public int getnId() {
        return nId;
    }

    public void setnId(int nId) {
        this.nId = nId;
    }

    @Id
    @Column(name = "c_c2code", nullable = false, length = 20)
    public String getcC2Code() {
        return cC2Code;
    }

    public void setcC2Code(String cC2Code) {
        this.cC2Code = cC2Code;
    }

    @Basic
    @Column(name = "d_atime", nullable = true)
    public Timestamp getdAtime() {
        return dAtime;
    }

    public void setdAtime(Timestamp dAtime) {
        this.dAtime = dAtime;
    }

    @Basic
    @Column(name = "d_ltime", nullable = true)
    public Timestamp getdLtime() {
        return dLtime;
    }

    public void setdLtime(Timestamp dLtime) {
        this.dLtime = dLtime;
    }

    @Basic
    @Column(name = "n_in_out_flag", nullable = true)
    public Byte getnInOutFlag() {
        return nInOutFlag;
    }

    public void setnInOutFlag(Byte nInOutFlag) {
        this.nInOutFlag = nInOutFlag;
    }

    @Basic
    @Column(name = "n_active_flag", nullable = true)
    public Byte getnActiveFlag() {
        return nActiveFlag;
    }

    public void setnActiveFlag(Byte nActiveFlag) {
        this.nActiveFlag = nActiveFlag;
    }

    @Basic
    @Column(name = "n_lc_order_download_flag", nullable = true)
    public Byte getnLcOrderDownloadFlag() {
        return nLcOrderDownloadFlag;
    }

    public void setnLcOrderDownloadFlag(Byte nLcOrderDownloadFlag) {
        this.nLcOrderDownloadFlag = nLcOrderDownloadFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LcLoC2CodeEntity that = (LcLoC2CodeEntity) o;
        return nId == that.nId &&
                Objects.equals(cC2Code, that.cC2Code) &&
                Objects.equals(dAtime, that.dAtime) &&
                Objects.equals(dLtime, that.dLtime) &&
                Objects.equals(nInOutFlag, that.nInOutFlag) &&
                Objects.equals(nActiveFlag, that.nActiveFlag) &&
                Objects.equals(nLcOrderDownloadFlag, that.nLcOrderDownloadFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nId, cC2Code, dAtime, dLtime, nInOutFlag, nActiveFlag, nLcOrderDownloadFlag);
    }
}
