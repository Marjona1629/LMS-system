package com.example.lmssystem.trnasfer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseData {
    private String message;
    private Object data;
}
