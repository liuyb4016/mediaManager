package com.eshore.yxt.media.core.util.cache;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum MediaCache {

    TASK_MESSAGE("task_message"),//任务
    MEDIA_FILE("media_file"),//视频
    ;
	
	public static final int DEFAULT_EXP = 7200;//2个小时
	private String key;
	private final static String LINE = "_";
	private final static int EXPIRE_AP_TOKENID_MAPPING_RESPKEY = 60*60;	//一小时
	public static final String EXCEPTION_RESULT = "Exception!!!";
	private final static String MAPSTART = "__MAP__";

	private static Logger logger = LoggerFactory.getLogger(MediaCache.class);
	
	private MediaCache(String key){
		this.key = key;
	}
	
	@Override
	public String toString(){
		return this.key;
	}
	
	
	/**
	 * 刷新全部缓存
	 */
	public static void flushAll(){
		for(MediaCache mc : MediaCache.values()){
			/*//TICKETUSER("ticketuser"),//用户
			//AUTHAP("authap"), //授权
			if(mc.key.equals(TICKETUSER.key)||mc.key.equals(AUTHAP.key)){
				continue;//不做刷新
			}*/
			flushKey(mc);
		}
	}
	
	/**
	 * 刷新指定缓存
	 */
	public static void flushKey(MediaCache mc){
		MemcacheCaller.INSTANCE.delete(MAPSTART+mc.key);
		logger.info("flushKey--->"+MAPSTART+mc.key);
		MemcacheCaller.INSTANCE.delete(mc.key);
		logger.info("flushKey--->"+mc.key);
	}
	
	/**
	 * 删除某个缓存
	 */
	public static void delByKey(String key){
		MemcacheCaller.INSTANCE.delete(key);
	}
	/**
	 * 清理某个枚举具体的某个key的缓存
	 * @param params 参数
	 */
	@SuppressWarnings("unchecked")
	public void delete(Object... params){
		if(params!=null&&params.length>0){
			Map<String,Boolean> cacheflagMap = (Map<String,Boolean>)MemcacheCaller.INSTANCE.get(MAPSTART+this.key);
			if(cacheflagMap!=null){
				String requestKey = this.key;
				for(Object o : params){
					requestKey += LINE;
					requestKey += o;
				}
				if(cacheflagMap.get(requestKey)!=null && true==(Boolean)cacheflagMap.get(requestKey)){
					MemcacheCaller.INSTANCE.delete(requestKey);
					cacheflagMap.remove(requestKey);
					MemcacheCaller.INSTANCE.set(MAPSTART+this.key,cacheflagMap);
				} 
			}
		}else{
			MemcacheCaller.INSTANCE.delete(this.key);
		}
	}
	
	/**
	 * 清除某个枚举的所有缓存
	 */
	@SuppressWarnings("unchecked")
	public void deleteAll(){
		MemcacheCaller.INSTANCE.delete(this.key);//先清除key缓存
		//缓存key的集合，清除各个key对应的缓存
		Map<String,Boolean> cacheflagMap = (Map<String,Boolean>)MemcacheCaller.INSTANCE.get(MAPSTART+this.key);
		if(cacheflagMap!=null){
			MemcacheCaller.INSTANCE.delete(MAPSTART+this.key);//先清除map缓存
			for (Map.Entry<String, Boolean> entry : cacheflagMap.entrySet()) {//清除map中对应的各个缓存key的值
				MemcacheCaller.INSTANCE.delete(entry.getKey());
			}
		}
	}
	
	
	/**
	 * 根据枚举key进行删除缓存
	 * @param enumKey
	 */
	@SuppressWarnings("unchecked")
	public static void deleteAllByKey(String enumKey){
		if(StringUtils.isNotEmpty(enumKey)){
			MemcacheCaller.INSTANCE.delete(enumKey);//先清除key缓存
			String mapCache=MAPSTART+enumKey;
			Map<String,Boolean> cacheflagMap = (Map<String,Boolean>)MemcacheCaller.INSTANCE.get(mapCache);//缓存key的集合，清除各个key对应的缓存
			if(cacheflagMap!=null){
				MemcacheCaller.INSTANCE.delete(mapCache);//先清除map缓存
				for (Map.Entry<String, Boolean> entry : cacheflagMap.entrySet()) {//清除map中对应的各个缓存key的值
					MemcacheCaller.INSTANCE.delete(entry.getKey());
				}
			}
		}
	}
	
	/**
	 * 从缓存取对象
	 * @param params 可以不传，存在参数时使用，如翻页的页码等。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object get(Object... params){
		if(params.length>0){
			Map<String,Boolean> cacheflagMap = (Map<String,Boolean>)MemcacheCaller.INSTANCE.get(MAPSTART+this.key);
			if(cacheflagMap==null){
				return null;
			}
			String requestKey = this.key;
			for(Object o : params){
				requestKey += LINE;
				requestKey += o;
			}
			if(cacheflagMap!=null && cacheflagMap.get(requestKey)!=null && true==(Boolean)cacheflagMap.get(requestKey)){
				return MemcacheCaller.INSTANCE.get(requestKey);
			}else {
				return null;
			}
		}else {
			return MemcacheCaller.INSTANCE.get(this.key);
		}
	}
	
	/**
	 * 将对象放进缓存（有参数时，也可全部刷新清除）
	 * @param value 放进去的值
	 * @param params 可以不传，存在参数时使用，如翻页的页码等。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void set(Object value, Object... params) {
		if(value==null){
			return ;
		}
		if(params.length>0){
			Map<String,Boolean> cacheflagMap = (Map<String,Boolean>)MemcacheCaller.INSTANCE.get(MAPSTART+this.key);
			if(cacheflagMap==null){
				cacheflagMap = new HashMap<String, Boolean>();
			}
			String requestKey = this.key;
			for(Object o : params){
				requestKey += LINE;
				requestKey += o;
			}
			MemcacheCaller.INSTANCE.set(requestKey,value);
			cacheflagMap.put(requestKey,true);
			MemcacheCaller.INSTANCE.set(MAPSTART+this.key,cacheflagMap);
		}else {
			MemcacheCaller.INSTANCE.set(this.key, value);
		}
	}
	
	/**
	 * 
	 * @param value
	 * @param expiry:单位秒，假如为空，则默认一天
	 * @param params
	 */
	@SuppressWarnings("unchecked")
	public void setByExpiry(Object value, Integer expiry,Object... params) {
		if(expiry==null){
			expiry=24*60*60;//一天
		}
		if(value==null){
			return ;
		}
		if(params.length>0){
			Map<String,Boolean> cacheflagMap = (Map<String,Boolean>)MemcacheCaller.INSTANCE.get(MAPSTART+this.key);
			if(cacheflagMap==null){
				cacheflagMap = new HashMap<String, Boolean>();
			}
			String requestKey = this.key;
			for(Object o : params){
				requestKey += LINE;
				requestKey += o;
			}
			MemcacheCaller.INSTANCE.setExpiry(requestKey,value,expiry);
			cacheflagMap.put(requestKey,true);
			MemcacheCaller.INSTANCE.setExpiry(MAPSTART+this.key,cacheflagMap,expiry);
		}else {
			MemcacheCaller.INSTANCE.setExpiry(this.key, value,expiry);
		}
	}

	/**
	 * 将对象放进缓存（无法后台刷新清除,较大量数据使用）
	 * @param value 放进去的值
	 * @param params 可以不传，存在参数时使用，如翻页的页码等。
	 * @return
	 */
	public void setOnly(Object value, Object... params) {
		String requestKey = this.key;
		for(Object o : params){
			requestKey += LINE;
			requestKey += o;
		}
		MemcacheCaller.INSTANCE.set(requestKey,value);
	}
	/**
	 * 将对象放进缓存（无法后台刷新清除,较大量数据使用）
	 * @param value 放进去的值
	 * @param expiry 超时时间，秒
	 * @param params 可以不传，存在参数时使用，如翻页的页码等。
	 * @return
	 */
	public void setOnly(Object value, Integer expiry, Object... params) {
		String requestKey = this.key;
		for(Object o : params){
			requestKey += LINE;
			requestKey += o;
		}
		MemcacheCaller.INSTANCE.setExpiry(requestKey,value,expiry);
	}
	
	/**
	 * 从缓存取对象（无法后台刷新清除,较大量数据使用）
	 * @param params 可以不传，存在参数时使用，如翻页的页码等。
	 * @return
	 */
	public Object getOnly(Object... params){
		String requestKey = this.key;
		for(Object o : params){
			requestKey += LINE;
			requestKey += o;
		}
		return MemcacheCaller.INSTANCE.get(requestKey);
	}
	
	/**
	 * 从缓存删除对象（无法后台刷新清除,较大量数据使用）
	 * @param params 可以不传，存在参数时使用，如翻页的页码等。
	 * @return
	 */
	public void deleteOnly(Object... params){
		String requestKey = this.key;
		for(Object o : params){
			requestKey += LINE;
			requestKey += o;
		}
		MemcacheCaller.INSTANCE.delete(requestKey);
	}

}
