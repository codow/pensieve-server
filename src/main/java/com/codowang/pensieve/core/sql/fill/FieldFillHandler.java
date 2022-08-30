package com.codowang.pensieve.core.sql.fill;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.codowang.pensieve.core.annotation.FieldInsertFill;
import com.codowang.pensieve.core.annotation.FieldUpdateFill;
import com.codowang.pensieve.core.sql.enums.FieldFillStrategy;
import com.codowang.pensieve.core.utils.DateUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 实体类数据填充实现, 会与mybatisplus-plus冲突P
 *
 * @author wangyb
 * @since 2022-07-05
 */
@Component
public class FieldFillHandler implements MetaObjectHandler {

    private static final Logger logger = LoggerFactory.getLogger(FieldFillHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {
        Object classObj = metaObject.getOriginalObject();
        Field[] declaredFields = classObj.getClass().getDeclaredFields();
        if (declaredFields.length == 0) {
            return;
        }
        for (Field field : declaredFields) {
            FieldInsertFill insertFill = field.getAnnotation(FieldInsertFill.class);
            if (insertFill == null) {
                continue;
            }
            // 判断是否符合填充策略
            if (!this.checkFillStrategy(metaObject, field, insertFill.strategy())) {
                continue;
            }
            String fieldName = field.getName();
            Object fieldVal = getFillValue(insertFill.value(), field.getType());
            // 如果存在默认值
            if (fieldVal == null) {
                fieldVal = getFillValue(insertFill.using());
            }
            this.fillStrategy(metaObject, fieldName, fieldVal);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object classObj = metaObject.getOriginalObject();
        Field[] declaredFields = classObj.getClass().getDeclaredFields();
        if (declaredFields.length == 0) {
            return;
        }
        for (Field field : declaredFields) {
            FieldUpdateFill updateFill = field.getAnnotation(FieldUpdateFill.class);
            if (updateFill == null) {
                continue;
            }
            // 判断是否符合填充策略
            if (!this.checkFillStrategy(metaObject, field, updateFill.strategy())) {
                continue;
            }
            String fieldName = field.getName();
            Object fieldVal = getFillValue(updateFill.value(), field.getType());
            // 如果存在默认值
            if (fieldVal == null) {
                fieldVal = getFillValue(updateFill.using());
            }
            // 设置值
            this.fillStrategy(metaObject,fieldName, fieldVal);
        }
    }

    /**
     * 判断是否需要填充
     *
     * @param metaObject 对象
     * @param field 字段
     * @param strategy 填充策略
     * @return true 填充， false 不填充
     */
    private boolean checkFillStrategy(MetaObject metaObject, Field field, FieldFillStrategy strategy) {
        // 总是填充，或者没有设置
        if (strategy == null || FieldFillStrategy.ALWAYS.equals(strategy)) {
            return true;
        }
        // 非空填充
        Object value = metaObject.getValue(field.getName());
        return value == null;
    }

    private Object getFillValue (String value, Class<?> type) {
        if (IFieldFill.NULL.equals(value)) {
            return null;
        }
        if (Integer.class.equals(type)) {
            return Integer.parseInt(value);
        } else if (Long.class.equals(type)) {
            return Long.parseLong(value);
        } else if (BigDecimal.class.equals(type)) {
            return new BigDecimal(value);
        } else if (LocalDate.class.equals(type)) {
            return DateUtils.getLocalDate(type);
        } else if (LocalDateTime.class.equals(type)) {
            return DateUtils.getLocalDateTime(type);
        } else if (Date.class.equals(type)) {
            try {
                return DateUtils.getDate(value);
            } catch (ParseException e) {
                throw new RuntimeException("fill error", e);
            }
        }
        return value;
    }

    private Object getFillValue (Class<? extends IFieldFill<?>> using) {
        if (using == null) {
            return null;
        }
        if (using.equals(FieldFillImpl.None.class)) {
            return null;
        }
        Object fieldVal;
        try {
            fieldVal = using.newInstance().generateValue();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("fill error", e);
        }
        return fieldVal;
    }
}
