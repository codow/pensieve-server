package com.codowang.pensieve.core.sql.provider;

import com.codowang.pensieve.core.annotation.SelectParamItem;

/**
 * 提供常用sql
 *
 * @author wangyb
 * @since 2022-07-07
 */
public class SqlProviderHelper {
    private SqlProviderHelper() {

    }

    /**
     * 空，表示没有定义sqlProvider
     */
    public static class None implements ISqlProvider {
        @Override
        public String getSql(String fieldName, SelectParamItem... params) {
            return null;
        }
    }

    /**
     * 提供user_id转user_name的格式化
     * 例如获取create_user_id对应的用户名
     */
    public static class UserNameFormatSqlProvider implements ISqlProvider {
        @Override
        public String getSql(String fieldName, SelectParamItem... params) {
            return fieldName;
        }
    }

    /**
     * 提供org_id转org_name的格式化
     */
    public static class OrgNameFormatSqlProvider implements ISqlProvider {
        @Override
        public String getSql(String fieldName, SelectParamItem... params) {
            return fieldName;
        }
    }

    /**
     * 提供role_id转role_name的格式化
     */
    public static class RoleNameFormatSqlProvider implements ISqlProvider {
        @Override
        public String getSql(String fieldName, SelectParamItem... params) {
            return fieldName;
        }
    }

    /**
     * 提供code_list中value转label的格式化
     */
    public static class CodeListNameFormatSqlProvider implements ISqlProvider {
        @Override
        public String getSql(String fieldName, SelectParamItem... params) {
            if (params.length == 0) {
                throw new RuntimeException("代码表格式化错误，必须指定参数0作为代码类型");
            }
            return fieldName;
        }
    }
}
