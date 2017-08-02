package com.eshore.yxt.media.core.util.cache;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.TimeoutException;

public class MemcacheCaller {

	private static final Logger logger = LoggerFactory.getLogger(MemcacheCaller.class);
	
	@Resource(name="memcachedClientForSystem")
	private MemcachedClient memcachedClientForSystem;
	
	private static final int DEFAULT_EXP = 0;//2个小时
	public static MemcacheCaller INSTANCE=new MemcacheCaller();
	/**
	 * 获得缓存内容	
	 * @param key 缓存key
	 * @return
	 */
	public Object get(String key){
		try {
			return memcachedClientForSystem.get(key);
		} catch (TimeoutException e) {
			logger.error(e.toString());
		} catch (InterruptedException e) {
			logger.error(e.toString());
		} catch (MemcachedException e) {
			logger.error(e.toString());
		}catch (Exception e) {
			logger.error(e.toString());
		}
		return null;
	}
	
	/**
	 * 设置缓存
	 * @param key 缓存key
	 * @param value 缓存value
	 * @return
	 */
	 public boolean set(String key, Object value){
		 try {
			return memcachedClientForSystem.set(key, DEFAULT_EXP, value);
		} catch (TimeoutException e) {
			logger.error(e.toString());
		} catch (InterruptedException e) {
			logger.error(e.toString());
		} catch (MemcachedException e) {
			logger.error(e.toString());
		}catch (Exception e) {
			logger.error(e.toString());
		}
		 return false;
	 }
	 
	 /**
	  * 有有效期的 缓存设置
	  * @param key 缓存key
	  * @param value 缓存value
	  * @param expiry 缓存有效期 单位秒
	  * @return
	  */
	 public boolean setExpiry(String key, Object value,int expiry){
		 try {
			return memcachedClientForSystem.set(key, expiry, value);
		}  catch (TimeoutException e) {
			logger.error(e.toString());
		} catch (InterruptedException e) {
			logger.error(e.toString());
		} catch (MemcachedException e) {
			logger.error(e.toString());
		}catch (Exception e) {
			logger.error(e.toString());
		}
		 return false;
	 }
	
	 /**
	  * 根据key 删除缓存
	  * @param key 缓存key
	  * @return
	  */
	public boolean delete(String key){
		try {
			return memcachedClientForSystem.delete(key);
		}catch (TimeoutException e) {
			logger.error(e.toString());
		} catch (InterruptedException e) {
			logger.error(e.toString());
		} catch (MemcachedException e) {
			logger.error(e.toString());
		}catch (Exception e) {
			logger.error(e.toString());
		}
		 return false;
	}
	
	/**
	 * 线程安全 缓存插入    
	 * @param key 缓存key
	 * @param expiry 并发等待超时时间
	 * @param value 缓存value
	 * @return
	 */
	public boolean add(String key, int expiry,Object value){
		 try {
			return memcachedClientForSystem.add(key, expiry, value);
		}  catch (TimeoutException e) {
			logger.error(e.toString());
		} catch (InterruptedException e) {
			logger.error(e.toString());
		} catch (MemcachedException e) {
			logger.error(e.toString());
		}catch (Exception e) {
			logger.error(e.toString());
		}
		 return false;
	 }
	
	
	
//	public final static String SHOPPING_CAR = "shoppingcar"; 
//	
//	public final static String SHOPPING_CAR_SEAT = "shoppingcarseat";
//	
//	private static final int BUSSINESS_TYPE_EXP = 7200;//2个小时 单位s
//	private static final int SESSION_EXP = 7200;//2个小时
//	private static final int BALANCE_EXP = 7200;//2个小时
//	private static final int CHARGE_EXP = 7200;//2个小时
//	private static final int PACKAGE_USE_EXP = 7200;//2个小时
//	
//	private static final int BALANCE_GRASP_EXP = 7200;//2个小时    来自抓取
//	private static final int CHARGE_GRASP_EXP = 7200;//2个小时    来自抓取
//	private static final int FLOW_GRASP_EXP = 7200;//2个小时    来自抓取
//	private static final int PACKAGE_USE_GRASP_EXP = 7200;//来自bill抓取套餐 2个小时
//	
//	private static final int RATE_VIEW_EXP = 7200;//2个小时
//	private static final int DICT_CSB_ACCESS_EXP = 7200;//2个小时
//	
//	private static final int CLIENT_TOKEN_EXP = 600;//10分钟
//	private static final int PHONE_SESSIONKEY_EXP = 600 ;//10分钟 
//	
//	private static final int PPC_API_ACCESS_TOKEN_EXP = 1800;//30分钟
//	/**
//	 * 充值卡充值接口的accessToken的缓存key
//	 */
//	private static final String PPC_API_ACCESS_TOKEN_KEY = "ppc_api_access_token";
//	
//	
//	private static final int HLR_DATA_EXP = 60*60*24;//24小时
//	
//	private static final int IMSI_MAP_PHONE_EXP = 60*60*24;//24小时
//	
//	private static final int LOGIN_RESPONSE_EXP = 60*5;//5分钟，必须小于session过期时间，否则有问题
//	
//	
//	@Resource(name="memcachedClientForSystem")
//	private MemcachedClient memcachedClientForSystem;
//	
//	
//	
//	
//	/**
//	 * 设置session缓存
//	 * @param sessionkey
//	 * @param sessionDto
//	 */
//	public void setSession(String sessionkey, Object sessionDto) throws TimeoutException, InterruptedException, MemcachedException {
////		int exp = 600;//10分钟
////		int exp = 1800;//30分钟
//		memcachedClientForSystem.set(sessionkey, SESSION_EXP, sessionDto);
//	}
//	
//	/**
//	 * 获取session信息
//	 * @param sessionkey
//	 * @return
//	 */
//	public SessionVO getSession(String sessionkey) throws TimeoutException, InterruptedException, MemcachedException {
//		SessionVO sessionDto = memcachedClientForSystem.get(sessionkey);
//		return sessionDto;
//	}
//	
//	/**
//	 * 设置token缓存
//	 * @param phone
//	 * @param balance
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setPpcApiAccessToken(Object accessToken) throws TimeoutException, InterruptedException, MemcachedException {		
//		return memcachedClientForSystem.set(PPC_API_ACCESS_TOKEN_KEY, PPC_API_ACCESS_TOKEN_EXP, accessToken);
//	}
//	
//	/**
//	 * 获取token缓存
//	 * @param phone
//	 * @param balance
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public List<SelectedSeat> getShoppingCarSeat() throws TimeoutException, InterruptedException, MemcachedException {
//		return memcachedClientForSystem.get(SHOPPING_CAR_SEAT);
//	}
//	
//	/**
//	 * 设置余额缓存
//	 * @param phone
//	 * @param balance
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setBalance(String phone, Object balance) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_balance";
//		return memcachedClientForSystem.set(key, BALANCE_EXP, balance);
//	}
//	
//	/**
//	 * 获取余额缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getBalance(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_balance";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	/**
//	 * 删除余额缓存
//	 * @param phone 手机号码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean deleteBalance(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_balance";
//		return memcachedClientForSystem.delete(key);
//	}
//	
//	/**
//	 * 设置话费缓存
//	 * @param phone
//	 * @param charge
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setCharge(String phone, Object charge) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_charge";
//		return memcachedClientForSystem.set(key, CHARGE_EXP, charge);
//	}
//	
//	/**
//	 * 获取话费缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getCharge(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_charge";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	/**
//	 * 删除话费缓存
//	 * @param phone 手机号码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean deleteCharge(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_charge";
//		return memcachedClientForSystem.delete(key);
//	}
//	
//	/**
//	 * 设置套餐详情缓存
//	 * @param phone
//	 * @param ppAccuInfos
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setPackageUse(String phone, List<PPAccuInfo> ppAccuInfos) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_packageUse";
//		return memcachedClientForSystem.set(key, PACKAGE_USE_EXP, ppAccuInfos);
//	}
//
//	/**
//	 * 获取套餐详情缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public List<PPAccuInfo> getPackageUse(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_packageUse";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 删除套餐详情缓存
//	 * @param phone 手机号码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean deletePackageUse(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_packageUse";
//		return memcachedClientForSystem.delete(key);
//	}
//	
//	/**
//	 * 设置imsi对应的手机号码缓存
//	 * @param imsi imsi码
//	 * @param phone 手机号码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setPhoneByImsi(String imsi, String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = imsi + "_imsiMapPhone";
//		return memcachedClientForSystem.set(key, IMSI_MAP_PHONE_EXP, phone);
//	}
//	
//	/**
//	 * 获取imsi对应的手机号码缓存
//	 * @param imsi  imsi码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getPhoneByImsi(String imsi) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = imsi + "_imsiMapPhone";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	/**
//	 * 设置号段归属地缓存
//	 * @param mdnSeg 7位数号段
//	 * @param hlrData	H码数据实体
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setHlrData(String mdnSeg, HlrData hlrData) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = mdnSeg + "_hlrData";
//		return memcachedClientForSystem.set(key, HLR_DATA_EXP, hlrData);
//	}
//	
//	/**
//	 * 获取号段归属地缓存
//	 * @param mdnSeg 7位数号段
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public HlrData getHlrData(String mdnSeg) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = mdnSeg + "_hlrData";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 设置LoginResponse缓存
//	 * @param imsi imsi码
//	 * @param loginResponse
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setLoginResponse(String imsi, LoginResponse loginResponse) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = imsi + "_loginResponse";
//		return memcachedClientForSystem.set(key, LOGIN_RESPONSE_EXP, loginResponse);
//	}
//	
//	/**
//	 * 获取LoginResponse缓存
//	 * @param imsi imsi码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public LoginResponse getLoginResponse(String imsi) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = imsi + "_loginResponse";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	/**
//	 * 设置csb业务类型缓存
//	 * @param phone
//	 * @param ppAccuInfos
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setBusinessType(String phone, String bussinessType) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_businessType";
//		return memcachedClientForSystem.set(key, BUSSINESS_TYPE_EXP , bussinessType);
//	}
//
//	/**
//	 * 获取csb业务类型缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getBusinessType(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_businessType";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	public String getExternalApiCallLogCurrentKey() throws TimeoutException, InterruptedException, MemcachedException {
//		String key = "external_api_call_log_current_key";
//		return memcachedClientForLog.get(key);
//	}
//	
//	public boolean appendExternalApiCallLog(String valueSql) throws TimeoutException, InterruptedException, MemcachedException {
////		String key = "external_api_call_log_value_sql";
//		String key = getExternalApiCallLogCurrentKey();
//		return memcachedClientForLog.append(key, valueSql, 3000);
//	}
//	
//	
//	public String getApiCallLogCurrentKey() throws TimeoutException, InterruptedException, MemcachedException {
//		String key = "api_call_log_current_key";
//		return memcachedClientForLog.get(key);
//	}
//	
//	public boolean appendApiCallLog(String valueSql) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = getApiCallLogCurrentKey();
//		return memcachedClientForLog.append(key, valueSql, 3000);
//	}
//	
//	/**
//	 * 设置业务视图GetRateViewResponse缓存
//	 * @param phone 手机号
//	 * @param operaCode 操作码
//	 * @param getRateViewResponse
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setRateViewResponse(String phone,String operaCode, GetRateViewResponse getRateViewResponse) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_" + operaCode + "_getRateViewResponse";
//		return memcachedClientForSystem.set(key, RATE_VIEW_EXP, getRateViewResponse);
//	}
//	
//	/**
//	 * 获取业务视图GetRateViewResponse缓存
//	 * @param phone 手机号
//	 * @param operaCode 操作码
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public GetRateViewResponse getRateViewResponse(String phone,String operaCode) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_" + operaCode + "_getRateViewResponse";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	/**
//	 * 设置调用csb接口的准入区域名缓存
//	 * @param dictType
//	 * @param dictCode
//	 * @param dictName
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setDictName(String dictType,String dictCode,String dictName) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = "DictCode_" + dictType + "_" + dictCode;
//		return memcachedClientForSystem.set(key, DICT_CSB_ACCESS_EXP , dictName);
//	}
//
//	/**
//	 * 获取调用csb接口的准入区域名缓存
//	 * @param dictType
//	 * @param dictCode
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getDictName(String dictType,String dictCode) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = "DictCode_" + dictType + "_" + dictCode;
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 设置Client cookies 缓存
//	 * @param number
//	 * @param cookies
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setClientCookies(String number,Cookie[] cookies) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = number+ "_cookie";
//		return memcachedClientForSystem.set(key, CLIENT_TOKEN_EXP, cookies);
//	}
//	
//	/**
//	 * 获取Client cookies 缓存
//	 * @param number
//	 * @param cookies
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getClientCookies(String number) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = number+ "_cookie";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 通过手机号设置sessionkey
//	 * @param phone
//	 * @param sessionkey
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setSessionkeyByPhone(String phone, Object sessionkey) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_sessionkey";
//		return memcachedClientForSystem.set(key, PHONE_SESSIONKEY_EXP , sessionkey);
//	}
//	
//	/**
//	 * 通过手机号获取sessionkey
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getSessionkeyByPhone(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_sessionkey";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 获取抓取过来的套餐详情缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public List<PackageUseDto> getPackageUseFromGrasp(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_packageUseFromGrasp";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 设置抓取过来的套餐详情缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setPackageUseFromGrasp(String phone, List<PackageUseDto> packageUseDtos) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_packageUseFromGrasp";
//		return memcachedClientForSystem.set(key, PACKAGE_USE_GRASP_EXP , packageUseDtos);
//	}
//	
//	
//	/**
//	 * 设置抓取过来的话费缓存
//	 * @param phone
//	 * @param charge
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setChargeFromGrasp(String phone, Object charge) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_chargeFromGrasp";
//		return memcachedClientForSystem.set(key, CHARGE_GRASP_EXP, charge);
//	}
//	
//	/**
//	 * 获取抓取过来的话费缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getChargeFromGrasp(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_chargeFromGrasp";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 设置抓取过来的余额缓存
//	 * @param phone
//	 * @param charge
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setBalanceFromGrasp(String phone, Object balance) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_balanceFromGrasp";
//		return memcachedClientForSystem.set(key, BALANCE_GRASP_EXP, balance);
//	}
//	
//	/**
//	 * 获取抓取过来的话费缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String getBalanceFromGrasp(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_balanceFromGrasp";
//		return memcachedClientForSystem.get(key);
//	}
//	
//	
//	/**
//	 * 设置抓取过来的流量缓存
//	 * @param phone
//	 * @param charge
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public boolean setFlowFromGrasp(String phone, Object flow) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_flowFromGrasp";
//		return memcachedClientForSystem.set(key, FLOW_GRASP_EXP, flow);
//	}
//	
//	/**
//	 * 获取抓取过来的流量缓存
//	 * @param phone
//	 * @return
//	 * @throws TimeoutException
//	 * @throws InterruptedException
//	 * @throws MemcachedException
//	 */
//	public String[] getFlowFromGrasp(String phone) throws TimeoutException, InterruptedException, MemcachedException {
//		String key = phone + "_flowFromGrasp";
//		return memcachedClientForSystem.get(key);
//	}
//	
	
	
}
