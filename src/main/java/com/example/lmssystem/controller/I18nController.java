package com.example.lmssystem.controller;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.servise.I18nServise;
import com.example.lmssystem.trnasfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class I18nController {

    private final I18nServise i18nServise;

    @PutMapping("/language")
    public ResponseEntity<?> greeting(@RequestParam(name = "lang", required = false) String lang) {
        CustomUserDetails customUserDetails = Utils.sessionUser();
        i18nServise.updateLanguage(customUserDetails.getId(), lang);
        return ResponseEntity.ok(ResponseData.builder()
                .message(HttpStatus.OK.toString())
                .build()
        );
    }
}