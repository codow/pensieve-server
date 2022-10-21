package com.codowang.pensieve.service.impl;

import com.codowang.pensieve.core.utils.DateUtils;
import com.codowang.pensieve.core.utils.HttpResponseUtils;
import com.codowang.pensieve.data.WebSecurityData;
import com.codowang.pensieve.entity.User;
import com.codowang.pensieve.entity.UserAccount;
import com.codowang.pensieve.error.CommonException;
import com.codowang.pensieve.error.SecurityErrorEnum;
import com.codowang.pensieve.error.WebAuthenticationException;
import com.codowang.pensieve.mapper.WebSecurityMapper;
import com.codowang.pensieve.service.IWebSecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WebSecurityService implements IWebSecurityService, AuthenticationSuccessHandler,
        AuthenticationFailureHandler, LogoutSuccessHandler {

    private final WebSecurityData webSecurityData;

    private final WebSecurityMapper webSecurityMapper;

    public WebSecurityService (WebSecurityData webSecurityData, WebSecurityMapper webSecurityMapper) {
        this.webSecurityData = webSecurityData;
        this.webSecurityMapper = webSecurityMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        UserAccount userAccount = ((User)authentication.getPrincipal()).getLoginAccount();
        userAccount.setErrorNum(0);
        userAccount.setErrorTime(null);
        userAccount.setLastLoginTime(new Date());
        // 更新错误时间和次数，最后登录时间
        this.webSecurityMapper.updateUserAccount(userAccount);
        // 组装返回信息
        HttpResponseUtils.writeSuccess(httpServletResponse, httpServletRequest.getSession().getId());
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        e.printStackTrace();
        // 使用内部自己的异常信息
        WebAuthenticationException elasticAuthException = (WebAuthenticationException) e;
        CommonException commonException;
        Map<String, Object> errorInfos = new LinkedHashMap<>();
        // 判断具体错误类型，根据错误类型，返回数据
        UserAccount account = elasticAuthException.getAccount();
        SecurityErrorEnum errorEnum = elasticAuthException.getErrorEnum();
        // 错误次数判定
        if (SecurityErrorEnum.INTERVAL_TIME_ERROR.equals(errorEnum)) {
            // 当前错误次数
            commonException = elasticAuthException.toCommonException(
                    "操作太频繁了，请在"
                            + DateUtils.getString(account.getErrorIntervalTime(), DateUtils.DATETIME_FORMAT)
                            + "后尝试登录，再错误" + (this.webSecurityData.getLoginErrorLockNum() - account.getErrorNum()) + "次账户将被锁定。"
            );
        } else if (SecurityErrorEnum.PASSWORD_ERROR.equals(errorEnum)) {
            // 次数加1，并更新account状态
            account.setErrorNum(account.getErrorNum() + 1);
            if (account.getErrorNum() >= this.webSecurityData.getLoginErrorLockNum()) {
                account.setIsLocked(1);
                // 提示账户被锁定
                commonException = elasticAuthException.toCommonException("密码错误，账号已被锁定，请联系管理员解锁。");
            } else if (account.getErrorNum() >= this.webSecurityData.getLoginErrorIntervalNum()) {
                // 提示账户将被锁定
                commonException = elasticAuthException.toCommonException(
                        "密码错误，再错误" + (this.webSecurityData.getLoginErrorLockNum() - account.getErrorNum()) + "次账户将被锁定。"
                );
            } else {
                commonException = elasticAuthException.toCommonException();
            }
            account.setErrorTime(new Date());
            this.webSecurityMapper.updateUserAccount(account);
        } else if (SecurityErrorEnum.CAPTCHA_ERROR.equals(errorEnum)) {
            commonException = elasticAuthException.toCommonException();
        } else {
            commonException = elasticAuthException.toCommonException();
        }
        // 设置是否需要验证码
        if (account != null && account.getErrorNum() > this.webSecurityData.getLoginErrorNum()) {
            errorInfos.put("captcha", true);
        }
        // 登录失败处理
        HttpResponseUtils.writeError(httpServletResponse, commonException, errorInfos);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpResponseUtils.writeSuccess(response, "注销成功");
    }
}
