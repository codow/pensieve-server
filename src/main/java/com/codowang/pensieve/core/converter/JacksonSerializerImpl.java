package com.codowang.pensieve.core.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.codowang.pensieve.core.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 自定义各种类型数据的jackson序列化方法
 *
 * @author wangyb
 * @since 2022-07-04
 */
public class JacksonSerializerImpl {

    /**
     * Long -> String
     * 前端处理Long会精度丢失
     */
    public static class LongSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(value == null ? null : value.toString());
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(DateUtils.getString(value));
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(DateUtils.getString(value));
        }
    }

    /**
     * 格式化日期字符串
     */
    public static class DateSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(DateUtils.getString(value, DateUtils.DATE_FORMAT));
        }
    }

    /**
     * 格式化日期时间字符串
     */
    public static class DatetimeSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeObject(DateUtils.getString(value, DateUtils.DATETIME_FORMAT));
        }
    }
}
