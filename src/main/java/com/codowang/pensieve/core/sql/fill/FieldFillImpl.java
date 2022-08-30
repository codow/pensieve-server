package com.codowang.pensieve.core.sql.fill;

import com.codowang.pensieve.core.utils.ContextUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 整合需要
 * @author wangyb
 * @since 2022-07-05
 */
public class FieldFillImpl {
    /**
     * 唯一的整数雪花ID生成器
     */
    private static final SnowflakeIdWorker interIdWorker = new SnowflakeIdWorker();

    /**
     * 返回整形的雪花ID
     * 不支持分布式环境
     */
    public static class None implements IFieldFill<Object> {
        @Override
        public Object generateValue() {
            return null;
        }
    }

    /**
     * 返回整形的雪花ID
     * 不支持分布式环境
     */
    public static class IntegerSnowflakeIdFill implements IFieldFill<Integer> {
        @Override
        public Integer generateValue() {
            return interIdWorker.nextId();
        }
    }

    /**
     * 生成isDeleted的默认值
     */
    public static class IsDeletedFill implements IFieldFill<Integer> {
        @Override
        public Integer generateValue() {
            return 0;
        }
    }

    /**
     * 返回当前登录人ID
     */
    public static class LoginUserIdFill implements IFieldFill<String> {
        @Override
        public String generateValue() {
            return ContextUtils.getLoginUserId();
        }
    }

    /**
     * 返回当前登录人ID
     */
    public static class LoginUserIdIntegerFill implements IFieldFill<Integer> {
        @Override
        public Integer generateValue() {
            String userId = ContextUtils.getLoginUserId();
            if (userId == null) {
                return -1;
            }
            return Integer.parseInt(userId);
        }
    }

    /**
     * 返回当前登录人ID
     */
    public static class LoginUserIdLongFill implements IFieldFill<Long> {
        @Override
        public Long generateValue() {
            String userId = ContextUtils.getLoginUserId();
            if (userId == null) {
                return -1L;
            }
            return Long.parseLong(userId);
        }
    }

    /**
     * 返回当前日期对象
     */
    public static class DateNowFill implements IFieldFill<Date> {
        @Override
        public Date generateValue() {
            return new Date();
        }
    }

    /**
     * 返回当前日期对象
     */
    public static class LocalDateNowFill implements IFieldFill<LocalDate> {
        @Override
        public LocalDate generateValue() {
            return LocalDate.now();
        }
    }

    /**
     * 返回当前日期对象
     */
    public static class LocalDateTimeNowFill implements IFieldFill<LocalDateTime> {
        @Override
        public LocalDateTime generateValue() {
            return LocalDateTime.now();
        }
    }
}
