package com.sustech.ooad.entity.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ForgetPasswordResponse {
    String code;
    String message;
}
