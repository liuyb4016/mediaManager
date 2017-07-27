package com.eshore.yxt.media.web.mdeia;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.BaseController;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;

@Controller
@RequestMapping(value="/whitelist")
public class MediaFileController extends BaseController {
	@Autowired
	private MediaFileService whitelistService;
	
	/**
	 * @DESCRIPTION:
	 *      管理页面
	 * @FILENAME: com.eshore.yxt.media.web.mdeia.MediaFileController
	 * @METHODNAME: initWhitelist
	 * @PARAM model
	 * ---------------------------------------
	 * @AUTHOR: liuyb
	 * @CREAETDATE: 2017/7/27
	 * @CREATETIME: 15:21
	 */
	@RequestMapping("/listMediaFile")
	public String initWhitelist(Model model) {
		return "media/listMediaFile";
	}
	
	@RequestMapping(value = "/listMediaFileByQuery")
	@ResponseBody
	public Grid listWhitelistByQuery(Pager pager, MediaFileReq req) {
		//查询有效数据
		return whitelistService.findAllByPager(pager, req);
	}
	
	@RequestMapping("/addWhitelist")
	public String addWhitelist(Model model) {
	
		return "whitelist/addWhitelist";
	}
	
	@RequestMapping("/whitelistExport")
	public String whitelistExport(MediaFileReq req, HttpServletResponse response) {
		response.setContentType("application/binary;charset=ISO8859_1");  
        try  
        {  
            ServletOutputStream outputStream = response.getOutputStream();  
            String fileName = new String(("白名单").getBytes(), "ISO8859_1");  
            logger.info("whitelistExport fileName:"+fileName);
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            whitelistService.whitelistExport(req,outputStream);  
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
		return null;
	}
	
	
	
	@RequestMapping("/whitelistUpload")
	@ResponseBody
	public Result whitelistUpload(@RequestParam("impFile") CommonsMultipartFile impFile /*,
			@RequestParam("remark") String remark */) {
		String remark = "";
		Result result = null;
		try{
			result =  whitelistService.whitelistUpload(impFile,remark);  
		}catch(Exception ex){
			logger.info("whitelistUpload 导入出现异常:"+ex.toString());
			if(null == result){
				result = new Result();
				result.setSuccess("0");
				result.setMsg("导入失败，出现异常！");
			}
		}
		return result;
	}
	
	@RequestMapping("/saveWhitelist")
	@ResponseBody
	public Result saveWhitelist(Whitelist whitelist) {
		if(null != whitelist){
			logger.info("saveWhitelist 增量录入白名单参数:手机号："+whitelist.getTelphone()+";备注："+whitelist.getRemark());
		}
		return whitelistService.saveWhitelist(whitelist);
	}
	
	/**
	 * 根据ids批量删除渠道
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/batchDelete")
	@ResponseBody
	public Result batchDelete(String ids) {
		Result result = new Result();
		if(StringUtils.isNotBlank(ids)) {
			String[] idArr = ids.split(",");
			for(String id : idArr) {
				whitelistService.deleteById(Long.parseLong(id));
			}
			result.setSuccess("1");
			result.setMsg("批量删除成功！");
		} else {
			result.setSuccess("0");
			result.setMsg("批量删除失败！");
		}
		return result;
	}
	
	/**
	 * 根据ID删除渠道
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Result delete(long id) {
		boolean b = whitelistService.deleteById(id);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
			result.setMsg("删除白名单成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("删除白名单失败！");
		}
		return result;
	}
	
	/**
	 * 根据ids批量删除渠道
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deletewhitelistByBatchno")
	@ResponseBody
	public Result deletewhitelistByBatchno(String ids) {
		Result result = new Result();
		if(StringUtils.isNotBlank(ids)) {
			String[] idArr = ids.split(",");
			for(String id : idArr) {
				whitelistService.deleteById(Long.parseLong(id));
			}
			result.setSuccess("1");
			result.setMsg("批量删除成功！");
		} else {
			result.setSuccess("0");
			result.setMsg("批量删除失败！");
		}
		return result;
	}
	
}
