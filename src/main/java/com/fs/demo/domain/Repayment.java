package com.fs.demo.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Repayment {
    String sn;
    String day;
    String date;
    String outstanding;
    String principle;
    String interest;
    String total;
}
