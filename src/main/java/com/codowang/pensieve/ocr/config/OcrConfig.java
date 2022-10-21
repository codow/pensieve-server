package com.codowang.pensieve.ocr.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system.ocr")
public class OcrConfig {

    private String storagePath = "/ocr";

    private int minWidth = 15;

    private int maxWidth = 4096;

    private int minHeight = 15;

    private int maxHeight = 4096;

    private int maxSize = 4096 * 4096;

    // ------ 百度OCR 需要的配置 ------
    private String baiduAppId = null;
    private String baiduApiKey = null;
    private String baiduSecretKey = null;
}
