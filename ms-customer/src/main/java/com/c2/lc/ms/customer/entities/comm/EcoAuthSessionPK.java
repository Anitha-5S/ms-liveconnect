package com.c2.lc.ms.customer.entities.comm;

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
public class EcoAuthSessionPK implements Serializable {

	private static final long serialVersionUID = -7070537544238971249L;

	@Column(name="c_c2code")
	private String c2Code;

	@Column(name="c_br_code")
	private String brCode;

	@Column(name="c_terminal_id")
	private String terminalId;

	@Column(name="c_type")
	private String type;

	@Column(name="c_device_token")
	private String deviceToken;

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EcoAuthSessionPK)) {
			return false;
		}
		EcoAuthSessionPK castOther = (EcoAuthSessionPK)other;
		return 
			this.c2Code.equals(castOther.c2Code)
			&& this.brCode.equals(castOther.brCode)
			&& this.terminalId.equals(castOther.terminalId)
			&& this.type.equals(castOther.type)
			&& this.deviceToken.equals(castOther.deviceToken);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.c2Code.hashCode();
		hash = hash * prime + this.brCode.hashCode();
		hash = hash * prime + this.terminalId.hashCode();
		hash = hash * prime + this.type.hashCode();
		hash = hash * prime + this.deviceToken.hashCode();
		return hash;
	}
}