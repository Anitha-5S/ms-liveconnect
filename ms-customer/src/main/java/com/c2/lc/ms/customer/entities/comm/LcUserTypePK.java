package com.c2.lc.ms.customer.entities.comm;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LcUserTypePK  implements Serializable {

    @SerializedName("c_mobile_no")
    @Column(name="c_mobile_no")
    private String mobileNo;

    @SerializedName("c_type")
    @Column(name="c_type")
    private String type;

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LcUserTypePK)) {
            return false;
        }
        LcUserTypePK castOther = (LcUserTypePK)other;
        return
                this.mobileNo.equals(castOther.mobileNo)
                        && this.type.equals(castOther.type);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.mobileNo.hashCode();
        hash = hash * prime + this.type.hashCode();
        return hash;
    }

}
