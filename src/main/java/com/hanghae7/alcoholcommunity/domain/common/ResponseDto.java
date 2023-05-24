package com.hanghae7.alcoholcommunity.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> {
    private int status;
    private String msg;
    private T object;

    public ResponseDto(int status, String msg){
        this.msg = msg;
        this.status = status;
    }
}
