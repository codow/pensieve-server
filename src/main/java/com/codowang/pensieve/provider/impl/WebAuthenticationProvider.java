package com.codowang.pensieve.provider.impl;

import com.codowang.pensieve.core.utils.DateUtils;
import com.codowang.pensieve.entity.User;
import com.codowang.pensieve.entity.UserAccount;
import com.codowang.pensieve.entity.WebUsernamePasswordAuthenticationToken;
import com.codowang.pensieve.error.SecurityErrorEnum;
import com.codowang.pensieve.error.WebAuthenticationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Calendar;
import java.util.Date;

/**
 * 继承spring security的 DaoAuthenticationProvider
 * 提供对验证码、登录次数、登录间隔控制的支持
 *
 * @author wangyb
 * @since 2022-07-14
 */
@Getter
@Setter
public class WebAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * 允许登录错误次数
     */
    private int loginErrorNum = 2;
    /**
     * 错误后增加登录间隔
     */
    private int loginErrorIntervalNum = 5;
    /**
     * 错误后自动锁定的次数
     */
    private int loginErrorLockNum = 8;
    /**
     * 登录错误间隔时间（秒）
     */
    private int loginErrorIntervalTime = 120;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                () -> messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();

        boolean cacheWasUsed = true;
        UserDetails userDetails = this.getUserCache().getUserFromCache(username);

        if (userDetails == null) {
            cacheWasUsed = false;

            try {
                userDetails = retrieveUser(username,
                        (UsernamePasswordAuthenticationToken) authentication);
            }
            catch (UsernameNotFoundException notFound) {
                logger.debug("User '" + username + "' not found");

                if (hideUserNotFoundExceptions) {
                    throw new WebAuthenticationException(SecurityErrorEnum.USERNAME_PASSWORD_ERROR);
                }
                else {
                    throw new WebAuthenticationException(SecurityErrorEnum.USER_UNKNOWN, null, notFound);
                }
            }

            Assert.notNull(userDetails,
                    "retrieveUser returned null - a violation of the interface contract");
        }

        User user = (User) userDetails;
        // 检查错误次数等信息
        checkErrorNum(user, (WebUsernamePasswordAuthenticationToken) authentication);

        // 检查登录状态
        try {
            cacheWasUsed = checkAuth(userDetails, username, authentication, cacheWasUsed);
        } catch (LockedException e) {
            throw new WebAuthenticationException(SecurityErrorEnum.ACCOUNT_LOCKED, user.getLoginAccount(), e);
        } catch (DisabledException e) {
            throw new WebAuthenticationException(SecurityErrorEnum.USER_UNKNOWN, user.getLoginAccount(), e);
        } catch (AccountExpiredException e) {
            throw new WebAuthenticationException(SecurityErrorEnum.ACCOUNT_EXPIRED, user.getLoginAccount(), e);
        } catch (CredentialsExpiredException e) {
            throw new WebAuthenticationException(SecurityErrorEnum.PASSWORD_EXPIRED, user.getLoginAccount(), e);
        } catch (BadCredentialsException e) {
            throw new WebAuthenticationException(SecurityErrorEnum.PASSWORD_ERROR, user.getLoginAccount(), e);
        }

        if (!cacheWasUsed) {
            this.getUserCache().putUserInCache(userDetails);
        }

        Object principalToReturn = userDetails;

        if (this.isForcePrincipalAsString()) {
            principalToReturn = userDetails.getUsername();
        }

        return createSuccessAuthentication(principalToReturn, authentication, userDetails);
    }

    private void checkErrorNum (User user, WebUsernamePasswordAuthenticationToken authToken) throws AuthenticationException {
        // 对登录的账户进行校验
        UserAccount account = user.getLoginAccount();
        // 判断账户是否已经锁定
        if (!user.isAccountNonLocked()) {
            throw new WebAuthenticationException(SecurityErrorEnum.ACCOUNT_LOCKED, user.getLoginAccount());
        }
        // 如果错误登录次数达到限制次数AND登录错误间隔时间在限制时间内，判断验证码
        Integer errorNum = account.getErrorNum();
        Date lastErrorTime = account.getErrorTime();
        long timeDifference = 0;
        if (lastErrorTime != null) {
            timeDifference = DateUtils.diffSecond(account.getErrorTime(), new Date());
        }
        // 超过错误次数后, 还未到登录间隔时
        if (errorNum > loginErrorIntervalNum
                && timeDifference <= (long) loginErrorIntervalTime * (errorNum - loginErrorIntervalNum)
        ) {
            // 加上错误间隔
            account.setErrorIntervalTime(
                    DateUtils.add(
                            lastErrorTime,
                            loginErrorIntervalTime * (errorNum - loginErrorIntervalNum),
                            Calendar.SECOND
                    )
            );
            // 计算登录错误间隔时间
            throw new WebAuthenticationException(SecurityErrorEnum.INTERVAL_TIME_ERROR, account);
        }
        // 超过错误次数，必须要验证码
        if (errorNum > loginErrorNum
                && !authToken.isCaptchaCorrect()) {
            throw new WebAuthenticationException(SecurityErrorEnum.CAPTCHA_ERROR, account);
        }
    }

    /**
     * 检查是否登录成功
     * @param user 用户信息
     * @param username 账户名
     * @param authentication 认证句柄
     * @param cacheWasUsed 是否使用缓存
     * @return 是否使用缓存
     */
    private boolean checkAuth (UserDetails user, String username, Authentication authentication, boolean cacheWasUsed) {
        try {
            getPreAuthenticationChecks().check(user);
            additionalAuthenticationChecks(user,
                    (UsernamePasswordAuthenticationToken) authentication);
        }
        catch (AuthenticationException exception) {
            if (cacheWasUsed) {
                // There was a problem, so try again after checking
                // we're using latest data (i.e. not from the cache)
                cacheWasUsed = false;
                user = retrieveUser(username,
                        (UsernamePasswordAuthenticationToken) authentication);
                getPreAuthenticationChecks().check(user);
                additionalAuthenticationChecks(user,
                        (UsernamePasswordAuthenticationToken) authentication);
            }
            else {
                throw exception;
            }
        }

        getPreAuthenticationChecks().check(user);

        return cacheWasUsed;
    }
}
