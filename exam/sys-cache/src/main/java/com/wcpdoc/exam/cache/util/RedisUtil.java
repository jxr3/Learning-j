package com.wcpdoc.exam.cache.util;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 缓存配置
 */
@Component
public class RedisUtil implements ApplicationContextAware {
	private static RedisTemplate<String, Object> redisTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		redisTemplate = (RedisTemplate<String, Object>) applicationContext.getBean("redisTemplate");
	}

	/**
	 * 添加数据
	 */
	public static void set(String key, Object value, long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 添加数据
	 */
	public static void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 获取数据 v1.0 zhanghc 2019年4月12日下午12:29:49
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(String key, Class<T> entity) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	/**
	 * 获取数据
	 */
	public static Object get(String key) {
		return get(key, Object.class);
	}

	/**
	 * 删除数据
	 */
	public static void del(String... key) {
		redisTemplate.delete(Arrays.asList(key));
	}

	/**
	 * 递增
	 */
	public static Long incr(String key, long delta) {
		return redisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * @author Caiy
	 * @param prefix key前缀
	 * @param suffix key后缀
	 */

	public static Set<String> getKeys(String prefix, String suffix) {
		if (null == prefix) {
			prefix = "";
		}
		if (null == suffix) {
			suffix = "";
		}
		return redisTemplate.keys(prefix + "*" + suffix);
	}
}
