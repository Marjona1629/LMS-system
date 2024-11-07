package com.example.lmssystem.utils;

import com.example.lmssystem.config.security.CustomUserDetails;
import com.example.lmssystem.servise.I18nServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Component
public class Utils {
    private static final Random RANDOM = new Random();
    private static I18nServise i18n;
    Utils(I18nServise i18nServise) {
        this.i18n = i18nServise;
    }

    public static String getMessage(String code){
        return i18n.getMessage(code);
    }

    public static String getMessage(String code, Object... args) {
        String message = i18n.getMessage(code);
        return MessageFormat.format(message, args);
    }

    public static CustomUserDetails sessionUser() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public static String makeName(List<String> args) {
        StringBuilder name = new StringBuilder();

        for (String arg : args) {
            if (arg != null && !arg.isEmpty()) {
                name.append(arg).append(" ");
            }
        }

        return name.toString();

    }

    public static String uploadImage(MultipartFile image, String targetSource) {

        if (image != null && !image.isEmpty()) {
            try {
                String format = StringUtils.getFilenameExtension(image.getOriginalFilename());
                String genName = UUID.randomUUID().toString().replace("-", "") + "." + format;
                Path filePath = Paths.get(targetSource, genName);
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, image.getBytes());
                return genName;

            } catch (IOException e) {
                throw new RuntimeException(e);
                          }

        }
        return null;
    }



}
