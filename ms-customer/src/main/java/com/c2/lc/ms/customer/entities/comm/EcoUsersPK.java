package com.c2.lc.ms.customer.entities.comm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EcoUsersPK implements Serializable {

    @NotEmpty(message = "'c_c2code' can not be empty!")
    @Column(name="c_c2code")
    private String c2Code;

    @NotEmpty(message = "'c_br_code' can not be empty!")
    @Column(name="c_br_code")
    private String brCode;

    @NotEmpty(message = "'c_terminal_id' can not be empty!")
    @Column(name="c_terminal_id")
    private String terminalId;

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EcoUsersPK)) {
            return false;
        }
        EcoUsersPK castOther = (EcoUsersPK)other;
        return
                this.c2Code.equals(castOther.c2Code)
                        && this.brCode.equals(castOther.brCode)
                        && this.terminalId.equals(castOther.terminalId);
    }

    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.c2Code.hashCode();
        hash = hash * prime + this.brCode.hashCode();
        hash = hash * prime + this.terminalId.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return c2Code + ":" + brCode + ":" + terminalId;
    }
}
