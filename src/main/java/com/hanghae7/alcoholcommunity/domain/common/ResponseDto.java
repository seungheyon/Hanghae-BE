package com.hanghae7.alcoholcommunity.domain.common;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    private int status;
    private String msg;
    private Object data;

    public ResponseDto(int status, String msg){
        this.msg = msg;
        this.status = status;
    }

    public static ResponseDto setSuccess(int statusCode, String message, Object data) {
        return new ResponseDto(statusCode, message, data);
    }
    public static ResponseDto setSuccess(int statusCode, String message) {
        return new ResponseDto(statusCode, message);
    }
}
