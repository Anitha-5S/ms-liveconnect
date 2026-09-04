package com.c2.lc.ms.customer.bos.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ToListModelBO {

    @JsonProperty("c_to")
    private List<String> to;

    @JsonProperty("c_to_cc")
    private List<String> toCc;

    @JsonProperty("c_to_bcc")
    private List<String> toBcc;

}
