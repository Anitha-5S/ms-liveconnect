package com.c2.lc.ms.master.repos.mysql;

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
public class CustActMstRequestPK implements Serializable {

    @Column(name = "c_c2code")
    private String cc2Code;

    @Column(name = "c_code")
    private String ccode;

    @Override
    public String toString() {
        return "cC2Code='" + cc2Code + '\'' +
                ", cCode='" + ccode + '\'';
    }

}
