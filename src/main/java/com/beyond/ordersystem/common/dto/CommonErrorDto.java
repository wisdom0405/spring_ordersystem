package com.beyond.ordersystem.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonErrorDto {
    private int status_code;
    private String error_message;

    public CommonErrorDto(HttpStatus httpStatus, String message){
        this.status_code = httpStatus.value();
        this.error_message = message;
    }
}
