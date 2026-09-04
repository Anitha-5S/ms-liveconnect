package com.c2.lc.ms.master.entities.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CustActMstEntityPK implements Serializable {

    @Column(name = "c_c2code")
    private String cC2Code;

    @Column(name = "c_code")
    private String cCode;

    @Override
    public String toString() {
        return "cC2Code='" + cC2Code + '\'' +
                ", cCode='" + cCode + '\'';
    }
}
