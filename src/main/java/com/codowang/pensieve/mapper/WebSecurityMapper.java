package com.codowang.pensieve.mapper;

import com.codowang.pensieve.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 查询登录用户的相关信息
 *
 * @author wangybSecurityMapper.xml
 */
@Mapper
public interface WebSecurityMapper {

    /**
     * 获取登录用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    Map<String, Object> selectUserById(Object userId);

    /**
     * 查询账户
     *
     * @param account 账户名称
     * @param type 账户类型
     * @return 账户信息
     */
    Map<String, Object> selectUserAccount(String account, Integer type);

    /**
     * 查询账户
     *
     * @param userId 用户ID
     * @return 用户所属机构列表
     */
    List<Map<String, Object>> selectUserOrgList(Object userId);

    /**
     * 查询账户
     *
     * @param userId 用户ID
     * @return 用户所属角色列表
     */
    List<Map<String, Object>> selectUserRoleList(Object userId);

    /**
     * 根据账户信息更新数据
     *
     * @param account 账户信息
     * @return 更新条数
     */
    int updateUserAccount(@Param("account") UserAccount account);
}
