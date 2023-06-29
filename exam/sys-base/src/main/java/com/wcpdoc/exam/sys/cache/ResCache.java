package com.wcpdoc.exam.sys.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.Cache;

import com.wcpdoc.exam.cache.util.EhCacheUtil;
import com.wcpdoc.exam.core.entity.Menu;
import com.wcpdoc.exam.core.util.SpringUtil;
import com.wcpdoc.exam.sys.entity.Res;
import com.wcpdoc.exam.sys.service.ResService;

/**
 * 资源缓存
 * 
 * v1.0 zhanghc 2017年7月5日下午11:51:07
 */
public class ResCache extends EhCacheUtil {
	private static final Logger log = LoggerFactory.getLogger(ResCache.class);
	private static final String CACHE_NAME = "MY_CACHE";
	private static final String RES_URL_RES_MAP = "RES_URL_RES_MAP";//{url : res}
	private static final String RES_ID_RES_MAP = "RES_ID_RES_MAP";//{id : res}
	private static final String RES_MENU_LIST = "RES_MENU_LIST";//[menu]
	private static final String RES_HOME_MENU_LIST = "RES_HOME_MENU_LIST";//[menu]

	static {
		initCache();
	}

	/**
	 * 初始化缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午5:16:20 
	 * void
	 */
	private static void initCache() {
		log.info("资源缓存开始初始化");
		Cache cache = getCache(CACHE_NAME);
		if (cache == null) {
			String message = "缓存名称：" + CACHE_NAME + "不存在！";
			log.error(message);
			throw new RuntimeException(message);
		}
		cache.put(RES_URL_RES_MAP, new HashMap<String, Res>());
		cache.put(RES_ID_RES_MAP, new HashMap<String, Res>());
		cache.put(RES_MENU_LIST, new ArrayList<Menu>());
		cache.put(RES_HOME_MENU_LIST, new ArrayList<Menu>());

		log.info("资源缓存成功初始化");
	}

	/**
	 * 刷新缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午4:06:30 void
	 */
	public static void flushCache() {
		synchronized (ResCache.class) {
			log.info("开始刷新资源缓存");
			clearCache();
			addCache();
			updateMenu();
			log.info("成功刷新资源缓存");
		}
	}

	/**
	 * 清除缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午4:12:24 void
	 */
	private static void clearCache() {
		getUrlResMap().clear();
		getIdResMap().clear();
		getMenuList().clear();
		getHomeMenuList().clear();
	}

	/**
	 * 添加缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午4:14:01 void
	 */
	private static void addCache() {
		List<Res> resList = getResList();
		for (Res res : resList) {
			addCache(res);
		}
	}

	/**
	 * 添加缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午4:14:01 void
	 */
	private static void addCache(Res res) {
		if (res == null) {
			throw new RuntimeException("无法获取参数：res");
		}

		Res source = res;
		Res target = new Res();
		BeanUtils.copyProperties(source, target);

		{
			Map<String, Res> urlResMap = getUrlResMap();
			if (target.getParentId() != 0) {
				String[] urlArr = target.getUrl().split("\\|");
				for (String url : urlArr) {
					urlResMap.put(url, target);
				}
			}
		}

		{
			Map<Integer, Res> idResMap = getIdResMap();
			idResMap.put(target.getId(), target);
		}
	}

	/**
	 * 更新菜单
	 * 
	 * v1.0 zhanghc 2017年7月7日下午5:29:47 void
	 * 
	 * @return
	 */
	private static void updateMenu() {
		Map<Integer, Res> idResMap = getIdResMap();
		Map<Integer, Menu> idMenuMap = new HashMap<Integer, Menu>();
		Map<Integer, Menu> homeIdMenuMap = new HashMap<Integer, Menu>();
		for (Res res : idResMap.values()) {
			if (res.getType() == null || res.getType() == 1) {
				Menu menu = new Menu();
				BeanUtils.copyProperties(res, menu);
				idMenuMap.put(menu.getId(), menu);
			}
			if (res.getType() == null || res.getType() == 2) {
				Menu menu = new Menu();
				BeanUtils.copyProperties(res, menu);
				homeIdMenuMap.put(menu.getId(), menu);
			}
		}

		List<Menu> menuList = getMenuList();
		for (Menu menu : idMenuMap.values()) {
			if (menu.getParentId() == 0) {
				menuList.add(menu);
			} else {
				Menu parentMenu = idMenuMap.get(menu.getParentId());
				List<Menu> children = parentMenu.getChildren();
				if (children == null) {
					children = new ArrayList<Menu>();
					parentMenu.setChildren(children);
				}
				children.add(menu);
				Collections.sort(children, new Comparator<Menu>() {
					@Override
					public int compare(Menu o1, Menu o2) {
						return o1.getNo() - o2.getNo();
					}
				});
			}
		}

		List<Menu> homeMenuList = getHomeMenuList();
		for (Menu menu : homeIdMenuMap.values()) {
			if (menu.getParentId() == 0) {
				homeMenuList.add(menu);
			} else {
				Menu parentMenu = homeIdMenuMap.get(menu.getParentId());
				List<Menu> children = parentMenu.getChildren();
				if (children == null) {
					children = new ArrayList<Menu>();
					parentMenu.setChildren(children);
				}
				children.add(menu);
				Collections.sort(children, new Comparator<Menu>() {
					@Override
					public int compare(Menu o1, Menu o2) {
						return o1.getNo() - o2.getNo();
					}
				});
			}
		}
	}

	/**
	 * 获取资源列表
	 * 
	 * v1.0 zhanghc 2017年7月7日下午4:19:22
	 * 
	 * @return List<Res>
	 */
	private static List<Res> getResList() {
		return SpringUtil.getBean(ResService.class).getList();
	}

	/**
	 * 获取缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午5:01:02
	 * 
	 * @return Map<String,Res>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Res> getUrlResMap() {
		Cache cache = getCache(CACHE_NAME);
		return cache.get(RES_URL_RES_MAP, Map.class);
	}

	/**
	 * 获取缓存
	 * 
	 * v1.0 zhanghc 2017年7月7日下午5:01:02
	 * 
	 * @return Map<Integer,Res>
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, Res> getIdResMap() {
		Cache cache = getCache(CACHE_NAME);
		return cache.get(RES_ID_RES_MAP, Map.class);
	}

	/**
	 * 获取菜单
	 * 
	 * v1.0 zhanghc 2017年7月7日下午5:01:02
	 * 
	 * @return List<Menu>
	 */
	@SuppressWarnings("unchecked")
	public static List<Menu> getMenuList() {
		Cache cache = getCache(CACHE_NAME);
		return cache.get(RES_MENU_LIST, List.class);
	}

	/**
	 * 获取前台菜单
	 * 
	 * v1.0 zhanghc 2017年8月22日下午10:41:13
	 * 
	 * @return List<Menu>
	 */
	@SuppressWarnings("unchecked")
	public static List<Menu> getHomeMenuList() {
		Cache cache = getCache(CACHE_NAME);
		return cache.get(RES_HOME_MENU_LIST, List.class);
	}
}
