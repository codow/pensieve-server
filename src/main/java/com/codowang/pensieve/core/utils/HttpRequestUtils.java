package com.codowang.pensieve.core.utils;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * http请求工具类
 * @author wangyb
 */
public class HttpRequestUtils {

    private HttpRequestUtils(){

    }

    public static boolean isUploadRequest (HttpServletRequest request) {
        return isUploadRequest(new ServletRequestContext(request));
    }

    public static boolean isUploadRequest (ServletRequestContext uploadRequest) {
        return ServletFileUpload.isMultipartContent(uploadRequest);
    }
}
