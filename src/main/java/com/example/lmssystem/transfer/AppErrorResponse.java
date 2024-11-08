package com.example.lmssystem.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppErrorResponse {
    private Integer status;
    private String message;
    private String error;
    private String path;

    public AppErrorResponse(Integer status, String message) {
        this.status = status;
        this.error = message;
    }
}