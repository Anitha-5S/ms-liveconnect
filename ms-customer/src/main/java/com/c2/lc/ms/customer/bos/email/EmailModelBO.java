package com.c2.lc.ms.customer.bos.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmailModelBO {

    @JsonProperty("c_from")
    private String from;

    @JsonProperty("to")
    private ToListModelBO toList;

    @JsonProperty("c_subject")
    private String subject;

    @JsonProperty("c_content")
    private String content;

}
