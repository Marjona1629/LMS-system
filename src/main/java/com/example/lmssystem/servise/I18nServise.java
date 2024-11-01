package com.example.lmssystem.servise;

import com.example.lmssystem.repository.UserRepository;
import com.example.lmssystem.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Locale;

@Service
@RequiredArgsConstructor
public class I18nServise {

    private final MessageSource messageSource;
    private final UserRepository authUserRepository;



    @Transactional
    public void updateLanguage(Long id,String lang){
        authUserRepository.updateLocale(id,lang);
    }


    public String getMessage(String code) {
        return messageSource.getMessage(code,null,new Locale(Utils.sessionUser().getLocale()));
    }
}
