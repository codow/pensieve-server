package com.codowang.pensieve.core.utils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author wangyb
 */
public abstract class CollectionUtils {

	/**
	 * 对象是否为数组对象
	 *
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(Object obj) {
		if (null == obj) {
			return false;
		}
		// 反射 获得类型
		return obj.getClass().isArray();
	}

	public static boolean isEmptyArray(Object obj) {
		if (!isArray(obj)) {
			return true;
		}
		return Arrays.asList(obj).isEmpty();
	}

	/**
	 * Return {@code true} if the supplied Collection is {@code null} or empty.
	 * Otherwise, return {@code false}.
	 * @param collection the Collection to check
	 * @return whether the given Collection is empty
	 */
	public static boolean isEmpty( Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}

	public static boolean isNotEmpty( Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * 重载空列表校验，增加对空对象的支持
	 * @param collection 列表
	 * @return 是否空列表
	 */
	public static boolean isEmpty(Object collection) {
		if (collection == null) {
			return true;
		}
		if (collection instanceof Collection<?>) {
			return isEmpty((Collection<?>) collection);
		} else if (collection instanceof Map<?, ?>) {
			return isEmpty((Map<?, ?>) collection);
		}
		return isEmptyArray(collection);
	}

	/**
	 * Return {@code true} if the supplied Map is {@code null} or empty.
	 * Otherwise, return {@code false}.
	 * @param map the Map to check
	 * @return whether the given Map is empty
	 */
	public static boolean isEmpty( Map<?, ?> map) {
		return (map == null || map.isEmpty());
	}

	/**
	 * Convert the supplied array into a List. A primitive array gets converted
	 * into a List of the appropriate wrapper type.
	 * <p><b>NOTE:</b> Generally prefer the standard {@link Arrays#asList} method.
	 * This {@code arrayToList} method is just meant to deal with an incoming Object
	 * value that might be an {@code Object[]} or a primitive array at runtime.
	 * <p>A {@code null} source value will be converted to an empty List.
	 * @param source the (potentially primitive) array
	 * @return the converted List result
	 * @see ObjectUtils#toObjectArray(Object)
	 * @see Arrays#asList(Object[])
	 */
	@SuppressWarnings("rawtypes")
	public static List arrayToList( Object source) {
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	/**
	 * Merge the given array into the given Collection.
	 * @param array the array to merge (may be {@code null})
	 * @param collection the target Collection to merge the array into
	 */
	@SuppressWarnings("unchecked")
	public static <E> void mergeArrayIntoCollection( Object array, Collection<E> collection) {
		Object[] arr = ObjectUtils.toObjectArray(array);
		for (Object elem : arr) {
			collection.add((E) elem);
		}
	}

	/**
	 * Merge the given Properties instance into the given Map,
	 * copying all properties (key-value pairs) over.
	 * <p>Uses {@code Properties.propertyNames()} to even catch
	 * default properties linked into the original Properties instance.
	 * @param props the Properties instance to merge (may be {@code null})
	 * @param map the target Map to merge the properties into
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> void mergePropertiesIntoMap( Properties props, Map<K, V> map) {
		if (props != null) {
			for (Enumeration<?> en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.get(key);
				if (value == null) {
					// Allow for defaults fallback or potentially overridden accessor...
					value = props.getProperty(key);
				}
				map.put((K) key, (V) value);
			}
		}
	}


	/**
	 * Check whether the given Iterator contains the given element.
	 * @param iterator the Iterator to check
	 * @param element the element to look for
	 * @return {@code true} if found, {@code false} otherwise
	 */
	public static boolean contains( Iterator<?> iterator, Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Enumeration contains the given element.
	 * @param enumeration the Enumeration to check
	 * @param element the element to look for
	 * @return {@code true} if found, {@code false} otherwise
	 */
	public static boolean contains( Enumeration<?> enumeration, Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether the given Collection contains the given element instance.
	 * <p>Enforces the given instance to be present, rather than returning
	 * {@code true} for an equal element as well.
	 * @param collection the Collection to check
	 * @param element the element to look for
	 * @return {@code true} if found, {@code false} otherwise
	 */
	public static boolean containsInstance( Collection<?> collection, Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Return {@code true} if any element in '{@code candidates}' is
	 * contained in '{@code source}'; otherwise returns {@code false}.
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return whether any of the candidates has been found
	 */
	public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return false;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the first element in '{@code candidates}' that is contained in
	 * '{@code source}'. If no element in '{@code candidates}' is present in
	 * '{@code source}' returns {@code null}. Iteration order is
	 * {@link Collection} implementation specific.
	 * @param source the source Collection
	 * @param candidates the candidates to search for
	 * @return the first present object, or {@code null} if not found
	 */
	@SuppressWarnings("unchecked")

	public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
		if (isEmpty(source) || isEmpty(candidates)) {
			return null;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return (E) candidate;
			}
		}
		return null;
	}

	/**
	 * 支持自定义判断逻辑的查找方法
	 * @param source 原列表
	 * @param action 匹配方法
	 * @param <E> 列表值泛型
	 * @return 查找结果
	 */
	public static <E> E findFirstMatch(Collection<E> source, Function<E, Boolean> action) {
		if (isEmpty(source)) {
			return null;
		}
		for (E item : source) {
			if (action.apply(item)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Find a single value of the given type in the given Collection.
	 * @param collection the Collection to search
	 * @param type the type to look for
	 * @return a value of the given type found if there is a clear match,
	 * or {@code null} if none or more than one such value found
	 */
	@SuppressWarnings("unchecked")

	public static <T> T findValueOfType(Collection<?> collection,  Class<T> type) {
		if (isEmpty(collection)) {
			return null;
		}
		T value = null;
		for (Object element : collection) {
			if (type == null || type.isInstance(element)) {
				if (value != null) {
					// More than one value found... no clear single value.
					return null;
				}
				value = (T) element;
			}
		}
		return value;
	}

	/**
	 * Find a single value of one of the given types in the given Collection:
	 * searching the Collection for a value of the first type, then
	 * searching for a value of the second type, etc.
	 * @param collection the collection to search
	 * @param types the types to look for, in prioritized order
	 * @return a value of one of the given types found if there is a clear match,
	 * or {@code null} if none or more than one such value found
	 */

	public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
		if (isEmpty(collection) || ObjectUtils.isEmpty(types)) {
			return null;
		}
		for (Class<?> type : types) {
			Object value = findValueOfType(collection, type);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	/**
	 * Determine whether the given Collection only contains a single unique object.
	 * @param collection the Collection to check
	 * @return {@code true} if the collection contains a single reference or
	 * multiple references to the same instance, {@code false} otherwise
	 */
	public static boolean hasUniqueObject(Collection<?> collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Object elem : collection) {
			if (!hasCandidate) {
				hasCandidate = true;
				candidate = elem;
			}
			else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Find the common element type of the given Collection, if any.
	 * @param collection the Collection to check
	 * @return the common element type, or {@code null} if no clear
	 * common type has been found (or the collection was empty)
	 */

	public static Class<?> findCommonElementType(Collection<?> collection) {
		if (isEmpty(collection)) {
			return null;
		}
		Class<?> candidate = null;
		for (Object val : collection) {
			if (val != null) {
				if (candidate == null) {
					candidate = val.getClass();
				}
				else if (candidate != val.getClass()) {
					return null;
				}
			}
		}
		return candidate;
	}

	/**
	 * Retrieve the last element of the given Set, using {@link SortedSet#last()}
	 * or otherwise iterating over all elements (assuming a linked set).
	 * @param set the Set to check (may be {@code null} or empty)
	 * @return the last element, or {@code null} if none
	 * @since 5.0.3
	 * @see SortedSet
	 * @see LinkedHashMap#keySet()
	 * @see LinkedHashSet
	 */

	public static <T> T lastElement( Set<T> set) {
		if (isEmpty(set)) {
			return null;
		}
		if (set instanceof SortedSet) {
			return ((SortedSet<T>) set).last();
		}

		// Full iteration necessary...
		Iterator<T> it = set.iterator();
		T last = null;
		while (it.hasNext()) {
			last = it.next();
		}
		return last;
	}

	/**
	 * Retrieve the last element of the given List, accessing the highest index.
	 * @param list the List to check (may be {@code null} or empty)
	 * @return the last element, or {@code null} if none
	 * @since 5.0.3
	 */

	public static <T> T lastElement( List<T> list) {
		if (isEmpty(list)) {
			return null;
		}
		return list.get(list.size() - 1);
	}

	/**
	 * Marshal the elements from the given enumeration into an array of the given type.
	 * Enumeration elements must be assignable to the type of the given array. The array
	 * returned will be a different instance than the array given.
	 */
	public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
		ArrayList<A> elements = new ArrayList<A>();
		while (enumeration.hasMoreElements()) {
			elements.add(enumeration.nextElement());
		}
		return elements.toArray(array);
	}

	/**
	 * 转树型数据
	 * 采用默认选项，对列表进行转换
	 * @param nodeList 需要转换的列表
	 * @return 树型的列表
	 */
	public static List<Map<String, Object>> toTree(List<Map<String, Object>> nodeList) {
		return toTree(nodeList, null, null);
	}

	/**
	 * 列表转树型数据
	 * @param nodeList 需要转换的列表
	 * @param parentId 父ID
	 * @param options 选项，设置字段idField，parentField，childrenField，rootId
	 * @return 树型的列表
	 */
//	public static List<Map<String, Object>> toTree(List<Map<String, Object>> nodeList, Object parentId, Map<String, Object> options) {
//		if(nodeList == null) {
//			return new ArrayList<>();
//		}
//
//		if (options == null) {
//			options = new HashMap<String, Object>(2) {{
//				put("idField", "id");
//				put("parentField", "parent_id");
//				put("childrenField", "children");
//				put("rootId", "-1");
//			}};
//		}
//		String idField = MapUtils.getString(options, "idField");
//		String parentField = MapUtils.getString(options, "parentField");
//		String childrenField = MapUtils.getString(options, "childrenField");
//		String rootId = MapUtils.getString(options, "rootId");
//
//		boolean isRoot = StringUtils.isRootId(parentId, rootId);
//
//		// 保存筛选后的节点数据
//		List<Map<String, Object>> currLevelNodeList = new ArrayList<>();
//		// 遍历当前节点的所有数据
//		List<Map<String, Object>> nextNodeList = new ArrayList<>(nodeList);
//		Iterator<Map<String, Object>> nextNodeListIterator = nextNodeList.iterator();
//		Map<String, Object> node;
//
//		//查询节点下的路由对象
//		boolean isRootNode, isNextNode;
//		while(nextNodeListIterator.hasNext()){
//			node = nextNodeListIterator.next();
//			Object nodeParentId = node.get(parentField);
//			isRootNode = isRoot && StringUtils.isRootId(nodeParentId, rootId);
//			isNextNode = !isRoot && parentId.equals(nodeParentId);
//			// 如果是获取根节点，则判断,当前节点是否根节点，不是根节点时，判断当前父节点是指定的节点
//			if(isRootNode || isNextNode){
//				currLevelNodeList.add(node);
//				// 从循环列表中移出
//				nextNodeListIterator.remove();
//			}
//		}
//
//		//遍历当前节点，查询下级当前节点的下级节点对象
//		for(Map<String, Object> currentNode : currLevelNodeList){
//			List<Map<String, Object>> children = toTree(nextNodeList, currentNode.get(idField), options);
//			if (children != null && !children.isEmpty()) {
//				currentNode.put(childrenField, children);
//			}
//		}
//
//		return currLevelNodeList;
//	}

	public static List<Map<String, Object>> toTree(List<Map<String, Object>> nodeList, Object parentId, Map<String, Object> options) {
		// 调整树转换的逻辑, 避免数据丢失的情况
		List<Map<String, Object>> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(nodeList)) {
			return result;
		}
		if (options == null) {
			options = new HashMap<String, Object>(2) {{
				put("idField", "id");
				put("parentField", "parent_id");
				put("childrenField", "children");
				put("rootId", "-1");
			}};
		}
		String idField = MapUtils.getString(options, "idField");
		String parentField = MapUtils.getString(options, "parentField");
		String childrenField = MapUtils.getString(options, "childrenField");
		String rootId = MapUtils.getString(options, "rootId");
		boolean isRoot = StringUtils.isRootId(parentId, rootId);

		// 缓存有哪些数据
		Map<String, Map<String, Object>> itemMap = new HashMap<>(6);

		nodeList.forEach(item -> {
			if (isRoot || parentId.equals(MapUtils.getString(item, parentField))) {
				itemMap.put(MapUtils.getString(item, idField), item);
			}
		});
		Map<String, List<Map<String, Object>>> childrenMap = new HashMap<>(6);
		// 将每个数据分配到对应的集合中
		nodeList.forEach(item -> {
			// 查询当前属于那个列表
			String itemParentId = MapUtils.getString(item, parentField);
			if (!itemMap.containsKey(itemParentId)) {
				result.add(item);
				return;
			}
			List<Map<String, Object>> children = childrenMap.computeIfAbsent(itemParentId, key -> new ArrayList<>());
			children.add(item);
		});
		// 将下级节点数组分配到对应的数据下
		for (String itemParentId : childrenMap.keySet()) {
			itemMap.get(itemParentId).put(childrenField, childrenMap.get(itemParentId));
		}
		return result;
	}

	/**
	 * 安全地把对象转MapList
	 * @param obj 需要转换的对象
	 * @param keyType key的类型
	 * @param valueType value的类型
	 * @param <K> key的泛型
	 * @param <V> value的泛型
	 * @return 返回list
	 */
	public static <K, V> List<Map<K, V>> castMapList(Object obj, Class<K> keyType, Class<V> valueType) {
		if (obj instanceof List<?>) {
			List<Map<K, V>> result = new ArrayList<>();

			for (Object element : (List<?>) obj) {
				if (element instanceof Map<?, ?>) {
					Map<K, V> map = MapUtils.castMap(element, keyType, valueType);

					if (map != null) {
						result.add(map);
					}
				}
			}

			return result;
		}
		return null;
	}

	/**
	 * 安全地把对象转ObjectList
	 * @param obj 需要转换的对象
	 * @return 返回list
	 */
	public static List<Object> castObjectList(Object obj) {
		return castObjectList(obj, Object.class);
	}

	/**
	 * 安全地把对象转ObjectList
	 * @param obj 需要转换的对象
	 * @param valueType value的类型
	 * @param <V> value的泛型
	 * @return 返回list
	 */
	public static <V> List<V> castObjectList(Object obj, Class<V> valueType) {
		if (obj instanceof List<?>) {
			List<V> result = new ArrayList<>();

			for (Object element : (List<?>) obj) {
				if (valueType.isInstance(element)) {
					V castElement = valueType.cast(element);
					result.add(castElement);
				}
			}

			return result;
		}
		return null;
	}

	/**
	 * 封装一个filter方法，对列表进行过滤
	 * @param list 列表
	 * @param action 过滤方法
	 * @return 过滤后的列表
	 */
	public static <T> List<T> filter(List<T> list, Function<T, Boolean> action) {
		List<T> result = new ArrayList<>();
		if (isEmpty(list)) {
			return result;
		}
		list.forEach((T item) -> {
			if (action.apply(item)) {
				result.add(item);
			}
		});
		return result;
	}

	/**
	 * 提供列表遍历时实现index注入
	 */
	public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
		class Obj {
			int i;
		}
		Obj obj = new Obj();
		return t -> {
			int index = obj.i++;
			consumer.accept(t, index);
		};
	}

	/**
	 * 获取map列表中，指定值的列表
	 * @param dataList 数据列表
	 * @param key 数据字段
	 * @return 列表
	 */
	public static List<Object> getValueList(List<Map<String, Object>> dataList, String key) {
		List<Object> result = new ArrayList<>();
		if (isEmpty(dataList)) {
			return result;
		}
		dataList.forEach(item -> {
			Object tempValue = item.get(key);
			// 特殊处理字符串，空字符传应该是无效值
			if (tempValue instanceof String ) {
				if (StringUtils.isNotBlank((String)tempValue)) {
					result.add(item.get(key));
				}
			} else if (tempValue != null) {
				result.add(item.get(key));
			}
		});
		return result;
	}

	/**
	 * 删除列表项中的字段
	 * @param dataList 列表项
	 * @param keys 字段名
	 */
	public static void removeValue(List<Map<String, Object>> dataList, String... keys) {
		if (isEmpty(dataList) || isEmpty(keys)) {
			return;
		}
		for (Map<String, Object> dataMap : dataList) {
			if (dataMap == null) {
				continue;
			}
			for (String key : keys) {
				dataMap.remove(key);
			}
		}
	}

	/**
	 * 格式化列表项中bigDecimal字段
	 * @param dataList 数据列表
	 * @param formatStr 格式化字符串
	 * @param keys 字段
	 */
	public static void formatBigDecimalValue (List<Map<String, Object>> dataList, String formatStr, String... keys) {
		if (isEmpty(dataList)) {
			return;
		}
		for (Map<String, Object> dataMap : dataList) {
			MapUtils.formatBigDecimalValue(dataMap, formatStr, keys);
		}
	}

	/**
	 * 给List<Map>中的对象
	 * @param dataList 数据列表
	 * @param key 字段
 	 * @param value 值
	 */
	public static void setValue (List<Map<String, Object>> dataList, String key, Object value) {
		if (isEmpty(dataList) || StringUtils.isBlank(key)) {
			return;
		}
		for (Map<String, Object> dataMap : dataList) {
			if (dataMap == null) {
				continue;
			}
			dataMap.put(key, value);
		}
	}

	/**
	 * 对列表数据进行排重
	 * @param srcList 原始列表
	 * @param <E> 列表泛型
	 */
	public static <E> void distinct (List<E> srcList) {
		if (isEmpty(srcList)) {
			return;
		}
		Map<Object, Boolean> cache = new HashMap<>(6);
		List<E> result = new ArrayList<>();
		srcList.forEach(item -> {
			Boolean exists = cache.get(item);
			if (item != null && exists == null || !exists) {
				result.add(item);
				cache.put(item, true);
			}
		});
		srcList.clear();
		srcList.addAll(result);
	}

	@SuppressWarnings("unchecked")
	public static <E, F, V> Map<V, Map<E, F>> toCacheMap(List<Map<E, F>> list, E key, Class<V> valueType) {
		Map<V, Map<E, F>> cache = new HashMap<>();
		if (isEmpty(list)) {
			return cache;
		}
		for (Map<E, F> temp : list) {
			cache.put((V)temp.get(key), temp);
		}
		return cache;
	}

	/**
	 * 继承列表中map的数据
	 * @param targetList 复制到的列表
	 * @param srcList 原列表
	 * @param key 主键字段
	 */
	public static void extendMap(List<Map<String, Object>> targetList, List<Map<String, Object>> srcList, String key) {
		if (isEmpty(srcList)) {
			return;
		}
		// 继承所有字段
		extendMap(targetList, srcList, key, srcList.get(0).keySet().toArray(new String[]{}));
	}

	/**
	 * 继承列表中map的数据
	 * @param targetList 复制到的列表
	 * @param srcList 原列表
	 * @param key 主键字段
	 * @param fields 复制的字段
	 */
	public static void extendMap(List<Map<String, Object>> targetList, List<Map<String, Object>> srcList, String key, String... fields) {
		if (isEmpty(targetList) || isEmpty(srcList) || isEmpty(fields)) {
			return;
		}
		Map<String, Map<String, Object>> cache = toCacheMap(targetList, key, String.class);
		Map<String, Object> target;
		for (Map<String, Object> src : srcList) {
			target = cache.get(MapUtils.getString(src, key));
			if (target == null) {
				continue;
			}
			for (String field: fields) {
				target.put(field, src.get(field));
			}
		}
	}
}
