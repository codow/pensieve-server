package com.codowang.pensieve.core.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.codowang.pensieve.core.utils.DateUtils;
import com.codowang.pensieve.core.utils.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自定义各种类型数据的jackson反序列化方法
 *
 * @author wangyb
 * @since 2022-07-05
 */
public class JacksonDeserializerImpl {

    /**
     * Long -> String
     * 前端处理Long会精度丢失
     */
    public static class LongDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String value = p.getValueAsString();
            if (StringUtils.isBlank(value)) {
                return null;
            }
            return Long.parseLong(value);
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class DateDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString();
            try {
                return DateUtils.getDate(value, DateUtils.DATE_FORMAT);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class DateTimeDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getValueAsString();
            try {
                return DateUtils.getDate(value, DateUtils.DATETIME_FORMAT);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return DateUtils.getLocalDate(p.getValueAsString());
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return DateUtils.getLocalDateTime(p.getValueAsString());
        }
    }
}
