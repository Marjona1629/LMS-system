package com.example.lmssystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class YmlData {
    @Value("${application.image-source:null}")
    private String imageSource;

    @Value("${application.domen:null}")
    private String domen;

    @Value("${application.max-image-size:2000000}")
    private Integer maxImageSize;
}