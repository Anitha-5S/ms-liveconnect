package com.c2.lc.ms.customer.entities.seller.pk;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PincodeMstEntityPK implements Serializable{
        private String cC2Code;
        private String cCode;

        @Column(name = "c_c2code", nullable = false, length = 45)
        @Id
        public String getcC2Code() {
            return cC2Code;
        }

        public void setcC2Code(String cC2Code) {
            this.cC2Code = cC2Code;
        }

        @Column(name = "c_code", nullable = false, length = 6)
        @Id
        public String getcCode() {
            return cCode;
        }

        public void setcCode(String cCode) {
            this.cCode = cCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PincodeMstEntityPK that = (PincodeMstEntityPK) o;
            return Objects.equals(cC2Code, that.cC2Code) &&
                    Objects.equals(cCode, that.cCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cC2Code, cCode);
        }
    }
