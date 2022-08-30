package com.codowang.pensieve.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Elastic 系列之Map工具类，不允许继承。
 *
 * @author wangyb
 */
public class MapUtils {

	private MapUtils () {}

	/**
	 * 复制Map中指定的数据项
	 * @param map 需要修改的数据
	 * @param keys 需要复制的属性
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> copy(Map<String, Object> map, String... keys) {
		return copy(map, false, keys);
	}

	/**
	 * 复制Map中指定的数据项
	 * @param map 需要修改的数据
	 * @param allowEmpty 是否允许空值
	 * @param keys 需要复制的属性
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> copy(Map<String, Object> map, boolean allowEmpty, String... keys) {
		Map<String, Object> target = new HashMap<>(16);
		boolean flag;
		for (String key : keys) {
			flag = allowEmpty && map.containsKey(key) || map.get(key) != null;
			if (flag) {
				target.put(key, map.get(key));
			}
		}
		return target;
	}

	/**
	 * 移除Map中数据项，返回一个新的Map
	 * @param map 需要修改的数据
	 * @param keys 需要复制的属性
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> remove(Map<String, Object> map, String... keys) {
		for (String key : keys) {
			map.remove(key);
		}
		return map;
	}

	/**
	 * map为空时，自动创建新的map
	 * @param map 需要避免为空的map
	 * @return hashMap
	 */
	public static <T, E> Map<T, E> safeMap(Map<T, E> map) {
		if (map instanceof LinkedHashMap) {
			return safeMap((LinkedHashMap<T, E>) map);
		}
		if (map instanceof HashMap) {
			return safeMap((HashMap<T, E>) map);
		}
		if (map != null) {
			return map;
		}
		return new HashMap<>();
	}

	/**
	 * map为空时，自动创建新的map
	 * @param map 需要避免为空的map
	 * @return hashMap
	 */
	public static <T, E> Map<T, E> safeMap(HashMap<T, E> map) {
		if (map != null) {
			return map;
		}
		return new HashMap<>();
	}

	/**
	 * map为空时，自动创建新的map
	 * @param map 需要避免为空的map
	 * @return map LinkedHashMap
	 */
	public static <T, E> Map<T, E> safeMap(LinkedHashMap<T, E> map) {
		if (map != null) {
			return map;
		}
		return new LinkedHashMap<>();
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(Map<String, Object> map, String key) throws ClassCastException{
		return (Map<String, Object>) map.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getList(Map<String, Object> map, String key) throws ClassCastException{
		return getList(map, false, key);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getList(Map<String, Object> map, boolean allowEmpty, String key) throws ClassCastException{
		Object value = map.get(key);
		if (value instanceof List) {
			return (List<Map<String, Object>>) value;
		}
		return new ArrayList<>();
	}
	
	public static Date getDate(Map<String, Object> map, String key) {
		try {
			return DateUtils.getDate(map.get(key).toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date getDate(Map<String, Object> map, String key, String datePattern) {
		try {
			return DateUtils.getDate(map.get(key).toString(), datePattern);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getString(Map<String, Object> map, String key){
		 return getString(map, false, key);
	}

	public static String getString(Map<String, Object> map, boolean allowNull, String key){
		String defaultValue = allowNull ? null : "";
		if (map == null) {
			return defaultValue;
		}
		return map.get(key) == null ? defaultValue : map.get(key).toString();
	}

	public static String getString(Map<String, Object> map, String... keys){
		return getString(map, false, keys);
	}

	public static String getString(Map<String, Object> map, boolean allowNull, String... keys){
		String defaultValue = allowNull ? null : "";
		if (keys == null || keys.length == 0) {
			return defaultValue;
		}
		String result = defaultValue;
		for (String key : keys) {
			result = getString(map, key);
			if (StringUtils.isNotBlank(result)) {
				break;
			}
		}
		return result;
	}

	public static Object getObject(Map<String, Object> map, String... keys){
		if (keys == null || keys.length == 0) {
			return null;
		}
		Object result = null;
		for (String key : keys) {
			result = map.get(key);
			if (result != null) {
				break;
			}
		}
		return result;
	}

	public static Integer getInteger(Map<String, Object> map, String key) {
		return getInteger(map, key, null);
	}

	public static Integer getInteger(Map<String, Object> map, String key, Integer defaultVal) {
		if (map == null || !map.containsKey(key)) {
			return defaultVal;
		}
		Object value = map.get(key);
		if (value == null) {
			return defaultVal;
		}
		if (value instanceof Integer) {
			return (Integer) value;
		} else {
			return Integer.parseInt(value.toString());
		}
	}

	public static Long getLong(Map<String, Object> map, String key) {
		return getLong(map, key, null);
	}

	public static Long getLong(Map<String, Object> map, String key, Long defaultVal) {
		if (map == null || !map.containsKey(key)) {
			return defaultVal;
		}
		Object value = map.get(key);
		if (value == null) {
			return defaultVal;
		}
		if (value instanceof Long) {
			return (Long) value;
		} else {
			return Long.parseLong(value.toString());
		}
	}

	public static BigDecimal getBigDecimal(Map<String, Object> map, String key) {
		if (map == null || !map.containsKey(key)) {
			return null;
		}
		Object value = map.get(key);
		if (value == null) {
			return null;
		}
		return new BigDecimal(value.toString());
	}

	public static BigDecimal getBigDecimal(Map<String, Object> map, String key, Object defaultVal) {
		if (defaultVal == null) {
			defaultVal = "0";
		}
		if (map == null || !map.containsKey(key)) {
			return new BigDecimal(defaultVal.toString());
		}
		Object value = map.get(key);
		if (value == null) {
			return new BigDecimal(defaultVal.toString());
		}
		return new BigDecimal(value.toString());
	}

	/**
	 * 对bigDecimal数据进行格式化
	 * @param map 数据兑现
	 * @param formatStr 格式化字符串
	 * @param keys 字段
	 */
	public static void formatBigDecimalValue (Map<String, Object> map, String formatStr, String... keys) {
		if (isEmpty(map) || CollectionUtils.isEmpty(keys) || StringUtils.isBlank(formatStr)) {
			return;
		}
		BigDecimal temp;
		DecimalFormat decimalFormat = new DecimalFormat(formatStr);
		String formatResult;
		for (String key : keys) {
			temp = getBigDecimal(map, key);
			formatResult = null;
			if (temp != null) {
				formatResult = decimalFormat.format(temp);
			}
			map.put(key, formatResult);
		}
	}

	public static boolean isEmpty(Map map) {
		return map == null || map.isEmpty();
	}

	public static boolean isNotEmpty(Map map) {
		return map != null && !map.isEmpty();
	}

	/**
	 * 执行安全的数据转换
	 * @param obj 需要转换的对象
	 * @param keyType key类型
	 * @param valueType value类型
	 * @return 转换完成后的Map
	 */
	public static <K, V> Map<K, V> castMap(Object obj, Class<K> keyType, Class<V> valueType) {
		if (obj instanceof Map<?, ?>) {
			Map<K, V> map = new HashMap<>(16);

			for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
				map.put(keyType.cast(entry.getKey()), valueType.cast(entry.getValue()));
			}

			return map;
		}
		return null;
	}

	/**
	 * 获取一组值列表，值类型必须一致
	 * @param map 键值对
	 * @param keys 关键值列表
	 * @return 任务列表
	 */
	public static <E> List<E> getValueList (Map<String, E> map, String... keys) {
		List<E> result = new ArrayList<>();
		if (isEmpty(map)) {
			return result;
		}
		E temp;
		for (String key : keys) {
			temp = map.get(key);
			if (temp != null) {
				result.add(temp);
			}
		}
		return result;
	}

	/**
	 * 从缓存中获取所有指定数据的列表
	 * @param map 键值对
	 * @param keys 关键值列表
	 * @return 任务列表
	 */
	public static <E> List<E> getValueList(Map<String, E> map, Collection<String> keys) {
		if (CollectionUtils.isEmpty(keys)) {
			return new ArrayList<>();
		}
		return getValueList(map, keys.toArray(new String[0]));
	}

	/**
	 * 判断是什么类型的map
	 * @param obj
	 * @param keyType
	 * @param valueType
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> boolean  instanceOfMap(Object obj, Class<K> keyType, Class<V> valueType) {
		if (!(obj instanceof Map)) {
			return false;
		}
		return ((Map<?, ?>)obj).keySet().stream().allMatch(key -> key == null || ObjectUtils.isSuperClass(key.getClass(), keyType))
				&& ((Map<?, ?>)obj).values().stream().allMatch(value -> value == null || ObjectUtils.isSuperClass(value.getClass(), valueType));
	}

	private static Object camelValueCase(Object value) {
		if (instanceOfMap(value, String.class, Object.class)) {
			value = camelKeyCase((Map<String, Object>)value);
		} else if (value instanceof Collection) {
			value = ((Collection)value).stream().map(item -> camelValueCase(item)).collect(Collectors.toList());
		}
		return value;
	}

	public static Map<String, Object> camelKeyCase (Map<String, Object> map) {
		if (isEmpty(map)) {
			return map;
		}
		Map<String, Object> result = null;
		try {
			result = (Map<String, Object>)map.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Object value;
		for (String key : map.keySet()) {
			value = map.get(key);
			value = camelValueCase(value);
			result.put(StringUtils.camelCase(key), value);
		}
		return result;
	}

	public static <E> Map<String, E> snakeKeyCase (Map<String, E> map) {
		if (isEmpty(map)) {
			return map;
		}
		Map<String, E> result = new HashMap<>();
		for (String key : map.keySet()) {
			result.put(StringUtils.snakeCase(key), map.get(key));
		}
		return result;
	}
}
