package com.codowang.pensieve.ocr.enums;

import lombok.Getter;

/**
 * 百度OCR的分类
 *
 * @author wangyb
 */
@Getter
public enum BaiduOcrType {
    ACCURATE_BASIC("ACCURATE_BASIC", "通用文字识别（高精度版）"),
    ACCURATE("ACCURATE", "通用文字识别（高精度含位置版）"),
    GENERAL_BASIC("GENERAL_BASIC", "通用文字识别（标准版）"),
    GENERAL("GENERAL", "通用文字识别（标准含位置版）"),
    DOC_ANALYSIS_OFFICE("DOC_ANALYSIS_OFFICE", "办公文档识别"),
    WEB_IMAGE("WEB_IMAGE", "网络图片文字识别"),
    WEB_IMAGE_LOC("WEB_IMAGE_LOC", "网络图片文字识别（含位置版）"),
    HANDWRITING("HANDWRITING", "手写文字识别"),
    TABLE("TABLE", "表格文字识别V2"),
    FORM_ASYNC("FORM_ASYNC", "表格文字识别(异步接口)"),
    FORM("FORM", "表格文字识别(同步接口)"),
    SEAL("SEAL", "印章识别"),
    NUMBERS("NUMBERS", "数字识别"),
    QRCODE("QRCODE", "二维码识别"),
    CUSTOM("CUSTOM", "自定义模板")
    ;

    private final String value;
    private final String label;

    BaiduOcrType(String value, String label) {
         this.value = value;
         this.label = label;
    }
}
