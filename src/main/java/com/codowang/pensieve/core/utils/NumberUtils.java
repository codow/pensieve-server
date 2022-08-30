package com.codowang.pensieve.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Elastic 系列之字符串工具类，不允许继承。
 *
 * @author lvhonglun
 */
public abstract class NumberUtils {

    /**
     * 转换格式
     */
    private static DecimalFormat format = new DecimalFormat("0.00000000000000000000");

    /**
     * 小数位数
     */
    private static int scale = 20;

    public static String getSequence(String systemName, int places) {
        return null;
    }

    public static String getLevelNumber(String systemName, int places) {
        return null;
    }

    /**
     * 判断数字是否为true
     *
     * @param num 数字
     * @return true/false
     */
    public static boolean isTrue(Integer num) {
        if (num == null) {
            return false;
        }
        return num == 1;
    }

    public static boolean isTrue(Long num) {
        if (num == null) {
            return false;
        }
        return num == 1;
    }

    public static boolean isFalse(Integer num) {
        return !isTrue(num);
    }

    /**
     * 判断是否是数字类型
     *
     * @param obj 对象
     * @return true/false
     */
    public static boolean isNumber(Object obj) {
        if (obj instanceof Integer || obj instanceof Short || obj instanceof Long || obj instanceof Float || obj instanceof Double) {
            return true;
        }
        return false;
    }

    public static Integer toInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (StringUtils.isBlank(obj.toString())) {
            return null;
        }
        return Integer.valueOf(obj.toString());
    }

    public static String formatAmount(Integer amount) {
        return formatAmount(Double.valueOf(amount));
    }

    public static String formatAmount(Long amount) {
        return formatAmount(Double.valueOf(amount));
    }

    public static String formatAmount(Float amount) {
        return formatAmount(Double.valueOf(amount));
    }

    public static String formatAmount(Double amount) {
        return formatAmount(amount, 2);
    }

    /**
     * 格式化金额
     *
     * @param amount 金额
     * @param scale  保留的小数位
     * @return 金额字符串
     */
    public static String formatAmount(Double amount, int scale) {
        if (amount == null) {
            return null;
        }
        BigDecimal bg = BigDecimal.valueOf(amount);
        bg = bg.setScale(scale, BigDecimal.ROUND_HALF_UP);
        // 获取金额字符串
        String amountStr = bg.toString();
        // 设置金额分隔符
        return amountStr.replaceAll("(\\d{1,3})(?=(\\d{3})+(?:$|\\.))", "$1,");
    }

    /**
     * 数字转为bigDecimal
     *
     * @param number 数字对象
     * @return bigDecimal对象
     */
    public static BigDecimal toBigDecimal(Object number) {
        if (number == null) {
            return new BigDecimal(0);
        }
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        if (number instanceof Integer) {
            return BigDecimal.valueOf((Integer) number);
        }
        if (number instanceof Long) {
            return BigDecimal.valueOf((Long) number);
        }
        if (number instanceof Float) {
            return BigDecimal.valueOf((Float) number);
        }
        if (number instanceof Double) {
            return BigDecimal.valueOf((Double) number);
        }
        if (number instanceof Short) {
            return BigDecimal.valueOf((Short) number);
        }
        return new BigDecimal(0);
    }

    /**
     * 格式化金额
     *
     * @param value
     * @return
     */
    public static String formatMoney(Object value) {
        String nullValue = "null";

        if (value == null || "".equals(value.toString()) || nullValue.equalsIgnoreCase(value.toString())) {
            value = "0.00";
        }
        return format.format(new BigDecimal(value.toString()));
    }

    /**
     * 格式化金额
     *
     * @param value
     * @return
     */
    public static String formatMoney(Object value, DecimalFormat dFormat) {
        if (ObjectUtils.isEmpty(dFormat)) {
            dFormat = format;
        }

        String nullValue = "null";

        if (value == null || "".equals(value.toString()) || nullValue.equalsIgnoreCase(value.toString())) {
            value = "0.00";
        }
        return dFormat.format(new BigDecimal(value.toString()));
    }

    /**
     * 金额相加
     *
     * @param valueStr 基础值
     * @param addStr   被加数
     * @return
     */
    public static String moneyAdd(Object valueStr, Object addStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal augend = new BigDecimal(addStr == null ? "0" : addStr.toString());
        return format.format(value.add(augend));
    }

    /**
     * 金额相加
     *
     * @param value  基础值
     * @param augend 被加数
     * @return
     */
    public static BigDecimal moneyAdd(BigDecimal value, BigDecimal augend) {
        return value.add(augend);
    }

    /**
     * 金额相减
     *
     * @param valueStr 基础值
     * @param minusStr 减数
     * @return
     */
    public static String moneySub(Object valueStr, Object minusStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal subtrahend = new BigDecimal(minusStr == null ? "0" : minusStr.toString());
        return format.format(value.subtract(subtrahend));
    }

    /**
     * 金额相减
     *
     * @param valueStr 基础值
     * @param minusStr 减数
     * @return
     */
    public static String moneySub(Object valueStr, Object minusStr, DecimalFormat df) {
        if (ObjectUtils.isEmpty(df)) {
            df = format;
        }

        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal subtrahend = new BigDecimal(minusStr == null ? "0" : minusStr.toString());
        return df.format(value.subtract(subtrahend));
    }

    /**
     * 金额相减
     *
     * @param value      基础值
     * @param subtrahend 减数
     * @return
     */
    public static BigDecimal moneySub(BigDecimal value, BigDecimal subtrahend) {
        return value.subtract(subtrahend);
    }


    /**
     * 金额相乘
     *
     * @param valueStr 基础值
     * @param mulStr   被乘数
     * @return
     */
    public static String moneyMul(Object valueStr, Object mulStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal mulValue = new BigDecimal(mulStr == null ? "0" : mulStr.toString());
        return format.format(value.multiply(mulValue));
    }

    /**
     * 金额相乘
     *
     * @param value    基础值
     * @param mulValue 被乘数
     * @return
     */
    public static BigDecimal moneyMul(BigDecimal value, BigDecimal mulValue) {
        return value.multiply(mulValue);
    }

    /**
     * 金额相除 <br/>
     * 精确小位小数
     *
     * @param valueStr  基础值
     * @param divideStr 被乘数
     * @return
     */
    public static String moneyDiv(Object valueStr, Object divideStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal divideValue = new BigDecimal(divideStr == null ? "0" : divideStr.toString());
        return format.format(value.divide(divideValue, scale, BigDecimal.ROUND_HALF_UP));
    }

    /**
     * 金额相除 <br/>
     * 精确小位小数
     *
     * @param value       基础值
     * @param divideValue 被乘数
     * @return
     */
    public static BigDecimal moneyDiv(BigDecimal value, BigDecimal divideValue) {
        return value.divide(divideValue, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 与指定值比较大小(不代等于判断)
     * <br/>如果valueStr大于compValueStr,则返回true,否则返回false
     * true 代表可用余额不足
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyCompNoEqual(Object valueStr, Object compValueStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal compValue = new BigDecimal(compValueStr == null ? "0" : compValueStr.toString());
        //0:等于    >0:大于    <0:小于
        int result = value.compareTo(compValue);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 与指定值比较大小(不代等于判断)
     * <br/>如果valueStr大于compValueStr,则返回true,否则返回false
     * true 代表可用余额不足
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyCompNoEqual(BigDecimal valueStr, BigDecimal compValueStr) {
        //0:等于    >0:大于    <0:小于
        int result = valueStr.compareTo(compValueStr);
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 与指定值比较是否相等
     * <br/>如果valueStr等于compValueStr,则返回true,否则返回false
     * true 代表相等
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyIsEqual(Object valueStr, Object compValueStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal compValue = new BigDecimal(compValueStr == null ? "0" : compValueStr.toString());
        //0:等于    >0:大于    <0:小于
        int result = value.compareTo(compValue);
        if (result == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 与指定值比较是否相等
     * <br/>如果valueStr等于compValueStr,则返回true,否则返回false
     * true 代表相等
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyIsEqual(BigDecimal valueStr, BigDecimal compValueStr) {
        //0:等于    >0:大于    <0:小于
        int result = valueStr.compareTo(compValueStr);
        if (result == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 值比较大小
     * <br/>如果valueStr大于等于compValueStr,则返回true,否则返回false
     * true 代表可用余额不足
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyCompEqual(Object valueStr, Object compValueStr) {
        BigDecimal value = new BigDecimal(valueStr == null ? "0" : valueStr.toString());
        BigDecimal compValue = new BigDecimal(compValueStr == null ? "0" : compValueStr.toString());
        //0:等于    >0:大于    <0:小于
        int result = value.compareTo(compValue);
        if (result >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 值比较大小
     * <br/>如果valueStr大于等于compValueStr,则返回true,否则返回false
     * true 代表可用余额不足
     *
     * @param valueStr     (需要消费金额)
     * @param compValueStr (可使用金额)
     * @return
     */
    public static boolean moneyCompEqual(BigDecimal valueStr, BigDecimal compValueStr) {
        //0:等于    >0:大于    <0:小于
        int result = valueStr.compareTo(compValueStr);
        if (result >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 金额乘以，省去小数点
     *
     * @param valueStr 基础值
     * @return
     */
    public static String moneyMulOfNotPoint(Object valueStr, Object divideStr) {
        if (valueStr == null) {
            valueStr = "0";
        }
        BigDecimal value = new BigDecimal(valueStr.toString());
        BigDecimal mulValue = new BigDecimal(divideStr == null ? "0" : divideStr.toString());
        valueStr = format.format(value.multiply(mulValue));
        return format.format(value.multiply(mulValue)).substring(0, valueStr.toString().length() - 3);
    }

    /**
     * 给金额加逗号切割
     *
     * @param str
     * @return
     */
    public static String addComma(Object str) {
        if (str == null) {
            str = "0";
        }
        String nStr = str.toString();

        try {
            String banNum = "";
            String period = ".";
            String comma = ",";
            int splitNum = 2;
            if (nStr.contains(period)) {
                String[] arr = nStr.split("\\.");
                if (arr.length == splitNum) {
                    str = arr[0];
                    banNum = "." + arr[1];
                }
            }
            // 将传进数字反转
            String reverseStr = new StringBuilder(nStr).reverse().toString();
            String strTemp = "";
            for (int i = 0; i < reverseStr.length(); i++) {
                if (i * 3 + 3 > reverseStr.length()) {
                    strTemp += reverseStr.substring(i * 3, reverseStr.length());
                    break;
                }
                strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
            }
            // 将[789,456,] 中最后一个[,]去除
            if (strTemp.endsWith(comma)) {
                strTemp = strTemp.substring(0, strTemp.length() - 1);
            }
            // 将数字重新反转
            String resultStr = new StringBuilder(strTemp).reverse().toString();
            resultStr += banNum;
            return resultStr;
        } catch (Exception e) {
            if (str == null) {
                return null;
            } else {
                return str.toString();
            }
        }

    }

    /**
     * 获取数字的有效位数(如：1.2020为3位小数；1.102为3位小数)
     *
     * @param value
     * @return
     */
    public static int getNumberOfDecimalPlace(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return 0;
        }
        final BigDecimal bigDecimal = new BigDecimal("" + value);
        final String s = bigDecimal.toPlainString();
        final int index = s.indexOf('.');
        if (index < 0) {
            return 0;
        }
        return s.length() - 1 - index;
    }

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟"};
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoneyObj
     * @return 转换后的大写人民币字符串
     */
    public static String number2CapitalRMB(Object numberOfMoneyObj) {
        BigDecimal numberOfMoney = new BigDecimal(numberOfMoneyObj.toString());
        return number2CapitalRMB(numberOfMoney);
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney 输入的金额
     * @return 转换后的大写人民币字符串
     */
    public static String number2CapitalRMB(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        // 这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }
}
