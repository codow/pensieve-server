package com.codowang.pensieve.core.utils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用的字符串工具类
 * @author wangyb
 */
public class StringUtils {
    private StringUtils () {
    }


    private static final String[] EMPTY_STRING_ARRAY = {};

    // 用于存放拼音及对应的汉字列表
    private static Map<String, List<String>> pinyinMap = new HashMap<String, List<String>>();

    /**
     * 比较两个字符串是否相等，如果都是空字符串则也相等
     * @param a 字符串A
     * @param b 字符串B
     * @return 是否相等
     */
    public static boolean equals (Object a, Object b) {
        if (isBlank(a) && isBlank(b)) {
            return true;
        }
        if (isBlank(a) || isBlank(b)) {
            return false;
        }
        return a.equals(b);
    }

    /**
     * <pre>
     * isBlank(null)      = true
     * isBlank("")        = true
     * isBlank(" ")       = true
     * isBlank("bob")     = false
     * isBlank("  bob  ") = false
     * </pre>
     */
    public static boolean isBlank(Object obj) {
        Set<String> test = new HashSet<>();
        test.addAll(Arrays.asList("".split(",|;")));
        test.contains(1);
        if (obj == null) {
            return true;
        } else {
            return org.apache.commons.lang3.StringUtils.isBlank(obj.toString());
        }
    }

    /**
     * <pre>
     * isBlank(null)      = true
     * isBlank("")        = true
     * isBlank(" ")       = true
     * isBlank("bob")     = false
     * isBlank("  bob  ") = false
     * </pre>
     */
    public static boolean isBlank(String str) {
        return org.apache.commons.lang3.StringUtils.isBlank(str);
    }

    /**
     * <pre>
     * isNotBlank(null)      = false
     * isNotBlank("")        = false
     * isNotBlank(" ")       = false
     * isNotBlank("bob")     = true
     * isNotBlank("  bob  ") = true
     * </pre>
     */
    public static boolean isNotBlank(String str) {
        return org.apache.commons.lang3.StringUtils.isNotBlank(str);
    }

    /**
     * <pre>
     * isEmpty(null)      = true
     * isEmpty("")        = true
     * isEmpty(" ")       = false
     * isEmpty("bob")     = false
     * isEmpty("  bob  ") = false
     * </pre>
     */
    public static boolean isEmpty(String str) {
        return org.apache.commons.lang3.StringUtils.isEmpty(str);
    }

    public static String join(List<?> list) {
        return join(list, ",");
    }

    public static String join(List<?> list, String separator) {
        return org.apache.commons.lang3.StringUtils.join(list, separator);
    }

    public static String join(List<?>... lists) {
        List<Object> list = new ArrayList<>();
        for (List<?> tempList : lists) {
            list.addAll(tempList);
        }
        return join(list);
    }

    public static String join(Object[] arr) {
        return join(arr, ",");
    }

    public static String join(Object[] arr, String separator) {
        return org.apache.commons.lang3.StringUtils.join(arr, separator);
    }

    public static String join(Object[]... arrList) {
        List<Object> list = new ArrayList<>();
        for (Object[] arr : arrList) {
            list.addAll(Arrays.asList(arr));
        }
        return join(list);
    }

    /**
     * Check whether the given object (possibly a {@code String}) is empty.
     * This is effectly a shortcut for {@code !hasLength(String)}.
     * <p>This method accepts any Object as an argument, comparing it to
     * {@code null} and the empty String. As a consequence, this method
     * will never return {@code true} for a non-null non-String object.
     * <p>The Object signature is useful for general attribute handling code
     * that commonly deals with Strings but generally has to iterate over
     * Objects since attributes may e.g. be primitive value objects as well.
     * <p><b>Note: If the object is typed to {@code String} upfront, prefer
     * {@link #hasLength(String)} or {@link #} instead.</b>
     *
     * @param str the candidate object (possibly a {@code String})
     * @since 3.2.1
     */
    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    /**
     * Check that the given {@code CharSequence} is neither {@code null} nor
     * of length 0.
     * <p>Note: this method returns {@code true} for a {@code CharSequence}
     * that purely consists of whitespace.
     * <p><pre class="code">
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = true
     * StringUtils.hasLength("Hello") = true
     * </pre>
     *
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null} and has length
     */
    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Check that the given {@code String} is neither {@code null} nor of length 0.
     * <p>Note: this method returns {@code true} for a {@code String} that
     * purely consists of whitespace.
     *
     * @param str the {@code String} to check (may be {@code null})
     * @return {@code true} if the {@code String} is not {@code null} and has length
     */
    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    /**
     * Delete any character in a given {@code String}.
     *
     * @param inString      the original {@code String}
     * @param charsToDelete a set of characters to delete.
     *                      E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting {@code String}
     */
    public static String deleteAny(String inString, String charsToDelete) {
        if (!hasLength(inString) || !hasLength(charsToDelete)) {
            return inString;
        }

        StringBuilder sb = new StringBuilder(inString.length());
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, null);
    }

    /**
     * Take a {@code String} that is a delimited list and convert it into
     * a {@code String} array.
     * <p>A single {@code delimiter} may consist of more than one character,
     * but it will still be considered as a single delimiter string, rather
     * than as bunch of potential delimiter characters, in contrast to
     * {@link }.
     *
     * @param str           the input {@code String} (potentially {@code null} or empty)
     * @param delimiter     the delimiter between elements (this is a single delimiter,
     *                      rather than a bunch individual delimiter characters)
     * @param charsToDelete a set of characters to delete; useful for deleting unwanted
     *                      line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a {@code String}
     * @return an array of the tokens in the list
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {

        if (str == null) {
            return EMPTY_STRING_ARRAY;
        }
        if (delimiter == null) {
            return new String[]{str};
        }

        List<String> result = new ArrayList<String>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * Copy the given {@link Collection} into a {@code String} array.
     * <p>The {@code Collection} must contain {@code String} elements only.
     *
     * @param collection the {@code Collection} to copy
     *                   (potentially {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    /**
     * Copy the given {@link Enumeration} into a {@code String} array.
     * <p>The {@code Enumeration} must contain {@code String} elements only.
     *
     * @param enumeration the {@code Enumeration} to copy
     *                    (potentially {@code null} or empty)
     * @return the resulting {@code String} array
     */
    public static String[] toStringArray(Enumeration<String> enumeration) {
        return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
    }

    /**
     * 对象转字符串, 空对象转为空字符串
     *
     * @param obj 需要转换的对象
     * @return 字符串
     */
    public static String getString(Object obj) {
        return getString(obj, "");
    }

    /**
     * 对象转字符串, 空对象转为空字符串
     *
     * @param obj        需要转换的对象
     * @param defaultVal 如果为空则采用默认值
     * @return 字符串
     */
    public static String getString(Object obj, String defaultVal) {
        if (isEmpty(obj)) {
            return defaultVal;
        }
        if (!(obj instanceof String)) {
            obj = obj.toString();
        }
        return (String) obj;
    }

    /**
     * 是否根节点ID
     *
     * @param id id
     * @return true/false
     */
    public static boolean isRootId(Object id) {
        return isRootId(id, "-1");
    }

    /**
     * 是否根节点ID
     *
     * @param id     id
     * @param rootId 默认根节点ID
     * @return true/false
     */
    public static boolean isRootId(Object id, String rootId) {
        return isBlank(id) || rootId.equals(id.toString());
    }

    /**
     * 首字母大写
     * @param str 原字符串
     * @return 首字母大写后的字符串
     */
    public static String upperFirstChart(String str) {
        if (isBlank(str)) {
            return str;
        }
        return (str.charAt(0) + "").toUpperCase() + str.substring(1);
    }

    /**
     * 转驼峰命名
     *
     * @param srcStr 字符串
     * @return 驼峰字符串
     */
    public static String camelCase(String srcStr) {
        if (isEmpty(srcStr)) {
            return srcStr;
        }

        String[] arr = srcStr.split("_|-");

        String result = "";
        String tempStr;
        for (int i = 0, l = arr.length; i < l; i++) {
            tempStr = arr[i].trim();
            if (isNotBlank(result) && isNotBlank(tempStr)) {
                tempStr = upperFirstChart(tempStr);
            }
            result += tempStr;
        }
        return result;
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param srcStr 字符串
     * @return 下划线符串
     */
    public static String snakeCase(String srcStr) {
        if (isEmpty(srcStr)) {
            return srcStr;
        }
        srcStr = camelCase(srcStr);
        String result = "";
        char A = "A".charAt(0);
        char Z = "Z".charAt(0);
        char tempChar;
        String tempStr;
        for (int i = 0, l = srcStr.length(); i < l; i++) {
            tempChar = srcStr.charAt(i);
            if (i != 0 && tempChar >= A && tempChar <= Z) {
                tempStr = "_" + (tempChar + "").toLowerCase();
            } else {
                tempStr = (tempChar + "").toLowerCase();
            }
            result += tempStr;
        }

        return result;
    }

    /**
     * 过滤下级levelCode，自保留返回最大的levelCode
     *
     * @param levelCodeList levelCode列表
     * @return
     */
    public static List<String> filterLowerLevelCode(List<String> levelCodeList) {
        if (CollectionUtils.isEmpty(levelCodeList)) {
            return levelCodeList;
        }
        // 先对levelCode排序
        levelCodeList.sort(Comparator.naturalOrder());
        List<String> result = new ArrayList<>();
        Set<String> set = new HashSet<>();
        levelCodeList.forEach(levelCode -> {
            List<String> levelCodeSplitList = splitLevelCode(levelCode);
            if (!CollectionUtils.containsAny(set, levelCodeSplitList)) {
                // 不存在时
                result.add(levelCode);
                set.add(levelCode);
            }
        });
        return result;
    }

    /**
     * 拆分机构的levelCode，不包含本机构的levelCode
     *
     * @param levelCodeList 层级码列表
     * @return levelCode列表
     */
    public static List<String> splitLevelCode(List<String> levelCodeList) {
        return splitLevelCode(levelCodeList, false);
    }

    /**
     * 拆分机构的levelCode，不包含本机构的levelCode
     *
     * @param levelCodeList 层级码列表
     * @param ignoreSelf    忽略本身的levelCode编号
     * @return levelCode列表
     */
    public static List<String> splitLevelCode(List<String> levelCodeList, boolean ignoreSelf) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(levelCodeList)) {
            return result;
        }
        Set<String> set = new HashSet<>();
        levelCodeList.forEach(levelCode -> set.addAll(splitLevelCode(levelCode, ignoreSelf)));
        result.addAll(set);
        return result;
    }

    /**
     * 拆分机构的levelCode，不包含本机构levelCode
     *
     * @param levelCode 层级码
     * @return 上级层级码列表
     */
    public static List<String> splitLevelCode(String levelCode) {
        return splitLevelCode(levelCode, false);
    }

    public static List<String> splitLevelCode(String levelCode, boolean ignoreSelf) {
        return splitLevelCode(levelCode, 3, ignoreSelf);
    }

    /**
     * 拆分机构的levelCode，不包含本机构levelCode
     *
     * @param levelCode  层级码
     * @param levelCount 层级码每层的字符数
     * @return 上级层级码列表
     */
    public static List<String> splitLevelCode(String levelCode, int levelCount, boolean ignoreSelf) {
        List<String> result = new ArrayList<>();
        if (isBlank(levelCode)) {
            return result;
        }
        // 计算遍历长度，如果不忽略本层级码，则长度+1
        int l = levelCode.length();
        if (!ignoreSelf) {
            l++;
        }
        for (int i = levelCount; i < l; i += levelCount) {
            result.add(levelCode.substring(0, i));
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(filterLowerLevelCode(new ArrayList<String>() {{
            add("001002003");
            add("001002004");
            add("002001");
            add("001");
            add("003002");
            add("003001");
            add("002001003");
        }}));
    }

    /**
     * 从html中提取纯文本
     *
     * @param inputString html内容
     */
    public static String html2Text(String inputString) {
        if (isBlank(inputString)) {
            return "";
        }
        // 含html标签的字符串
        String htmlStr = inputString;
        String textStr = "";
        Pattern pattern;
        Matcher matcher;
        try {
            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regExHtml = "<[^>]+>";
            // 转义字符替换
            String regExEscapeChar = "&[a-zA-Z0-9_-]+;";
            pattern = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            // 过滤script标签
            htmlStr = matcher.replaceAll("");
            pattern = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            // 过滤style标签
            htmlStr = matcher.replaceAll("");
            pattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(htmlStr);
            // 过滤html标签
            htmlStr = matcher.replaceAll("");
            pattern = Pattern.compile(regExEscapeChar, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher((htmlStr));
            // 过滤转义字符
            htmlStr = matcher.replaceAll("");
            textStr = htmlStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //剔除空格行
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        // 返回文本字符串
        return textStr;
    }

    /**
     * <b>Description:初始化 所有的多音字词组</b><br>
     *
     * @param file
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static void initPinyin(File file) throws FileNotFoundException, UnsupportedEncodingException {
        // 读取多音字的全部拼音表;
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String s = null;
        try {
            while ((s = br.readLine()) != null) {
                if (s != null) {
                    String[] arr = s.split("#");
                    String pinyin = arr[0];
                    String chinese = arr[1];
                    if (chinese != null) {
                        String[] strArr = chinese.split(" ");
                        List<String> list = Arrays.asList(strArr);
                        pinyinMap.put(pinyin, list);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 字符串是否表示false，为NULL、“”、“false”，不区分大小写
     * @param str 字符串
     * @return 是否为false
     */
    public static boolean isFalse(String str) {
        if (isBlank(str)) {
            return true;
        }
        return "false".equalsIgnoreCase(str);
    }

    /**
     * 字符串是否表示true，不为NULL、“”、“false”，不区分大小写
     * @param str 字符串
     * @return true/false
     */
    public static boolean isTrue(String str) {
        return !isFalse(str);
    }
}
