package com.codowang.pensieve.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供一些基本的数据返回处理方法
 * @author wangyb
 */
public class HttpResponseUtils {
    /**
     * 禁止实例化
     */
    private HttpResponseUtils() {}

    private static void write(HttpServletResponse response, String result) throws IOException{
        write(response, "application/json;charset=utf-8", result);
    }

    private static void write(HttpServletResponse response, String contentType, String result) throws IOException{
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        // 构建JSON对象
        out.println(result);
        out.flush();
        out.close();
    }

    public static void writeSuccess(HttpServletResponse response, Object obj) throws IOException {
        write(response, new ObjectMapper().writeValueAsString(obj));
    }

    public static void writeError(HttpServletResponse response, Exception e) throws IOException {
        writeError(response, e, new HashMap<>());
    }

    public static void writeError(HttpServletResponse response, Exception e, Map<String, Object> resultMap) throws IOException {
    }
}
