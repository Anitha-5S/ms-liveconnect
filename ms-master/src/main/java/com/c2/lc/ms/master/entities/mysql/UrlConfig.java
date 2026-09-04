package com.c2.lc.ms.master.entities.mysql;

import com.c2.lc.lib.db.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "url_config")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UrlConfig extends DateAudit {

    @EmbeddedId
    private UrlConfigPK urlConfigPK;

    @Column(name = "c_url", nullable = false, length = 1024)
    private String c_url;

}
