package com.c2.lc.lib.bo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LcHeaderBO implements Serializable {

    private Long userId;

    private Long firmId;

    private String c2Code;
    private String brCode;

    private String terminalId;
    private String type;

}
