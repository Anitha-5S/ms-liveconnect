package com.c2.lc.ms.master.entities.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("system_parameter")
public class SystemParameterEntity {

    @NotNull
    private String domain;
    @NotNull
    private String feature;
    @NotNull
    private String parameterName;

    private String parameterValue;

}
