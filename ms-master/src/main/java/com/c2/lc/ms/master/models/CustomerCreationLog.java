package com.c2.lc.ms.master.models;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreationLog {
    private String status;
    private LocalDate date;
    private Object response;
    private String csqCustomerCode;
    private LocalDateTime requestTimeStamp;
    private LocalDateTime responseTimeStamp;
}
