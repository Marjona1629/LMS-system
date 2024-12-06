package com.example.lmssystem.controller.bitganlar;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.service.I18nService;
import com.example.lmssystem.transfer.ResponseData;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class I18nController {

    private final I18nService i18nService;

    @PutMapping
    public ResponseEntity<?> greeting(@RequestParam(name = "lang", required = false) String lang) {
        CustomUserDetails customUserDetails = Utils.sessionUser();
        i18nService.updateLanguage(customUserDetails.getId(), lang);
        return ResponseEntity.ok(ResponseData.builder()
                        .success(true)
                        .data(null)
                .message(Utils.getMessage("language_updated"))
                .build()
        );
    }
}