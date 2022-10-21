package com.codowang.pensieve.provider.impl;

import com.codowang.pensieve.core.utils.MapUtils;
import com.codowang.pensieve.data.WebSecurityData;
import com.codowang.pensieve.entity.User;
import com.codowang.pensieve.entity.UserAccount;
import com.codowang.pensieve.enums.AccountTypeEnum;
import com.codowang.pensieve.error.SecurityErrorEnum;
import com.codowang.pensieve.mapper.WebSecurityMapper;
import com.codowang.pensieve.provider.IWebSecurityProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSecurityProvider implements IWebSecurityProvider, UserDetailsService {

    private final WebSecurityMapper webSecurityMapper;

    private final PasswordEncoder passwordEncoder;

    public WebSecurityProvider (WebSecurityData webSecurityData, WebSecurityMapper webSecurityMapper) {
        this.webSecurityMapper = webSecurityMapper;

        String encodingId = webSecurityData.getPasswordEncoding();
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("ldap", new LdapShaPasswordEncoder());
        encoders.put("MD4", new Md4PasswordEncoder());
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new StandardPasswordEncoder());
        encoders.put("argon2", new Argon2PasswordEncoder());

        passwordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
    }

    @Override
    public User getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof User)) {
            return null;
        }
        User user = (User) principal;
        // 去掉敏感数据
        user.getLoginAccount().setPassword(null);
        return user;
    }

    @Override
    public User getUserById(Serializable userId) {
        // 查询用户信息
        Map<String, Object> userInfo = this.webSecurityMapper.selectUserById(userId);
        // 查询所属机构
        List<Map<String, Object>> orgList = this.webSecurityMapper.selectUserOrgList(userId);
        // 查询所属角色
        List<Map<String, Object>> roleList = this.webSecurityMapper.selectUserRoleList(userId);
        // 组装用户信息
        userInfo.put("login_account", null);
        userInfo.put("org_list", orgList);
        userInfo.put("role_list", roleList);
        return new User(userInfo);
    }

    @Override
    public String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public PasswordEncoder getPasswordEncoder () {
        return this.passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
        // 查询用户信息
        Map<String, Object> accountInfo = this.webSecurityMapper.selectUserAccount(accountName, AccountTypeEnum.ACCOUNT_PASSWORD.getValue());
        if (MapUtils.isEmpty(accountInfo)) {
            throw new UsernameNotFoundException(SecurityErrorEnum.USER_UNKNOWN.getLabel());
        }
        String userId = MapUtils.getString(accountInfo, "user_id");
        User user = getUserById(userId);
        user.setLoginAccount(new UserAccount(accountInfo));
        return user;
    }
}
