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

import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.system.BaseController;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;

@Controller
@RequestMapping(value="/mediaFile")
public class MediaFileController extends BaseController {
	@Autowired
	private MediaFileService mediaFileService;
	
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
	public String listMediaFile(Model model) {
		return "mediaFile/listMediaFile";
	}
	
	@RequestMapping(value = "/listMediaFileByQuery")
	@ResponseBody
	public Grid listMediaFileByQuery(Pager pager, MediaFileReq req) {
		//查询有效数据
		return mediaFileService.findAllByPager(pager, req);
	}
	
	@RequestMapping("/addMediaFile")
	public String addWhitelist(Model model) {
		return "mediaFile/addMediaFile";
	}
	
	@RequestMapping("/saveMediaFile")
	@ResponseBody
	public Result saveWhitelist(
            @RequestParam("mp4File") CommonsMultipartFile mp4File, @RequestParam("imgFile") CommonsMultipartFile imgFile,
            @RequestParam(value="title",required = true) String title,@RequestParam(value="mediaDesc",required = true) String mediaDesc,
            @RequestParam(value="id",required = true) Long id) {
		return mediaFileService.mediaFileUpload(mp4File,imgFile,title,mediaDesc,id);
	}
	
	/**
	 * 根据ids批量删除
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
				mediaFileService.deleteById(Long.parseLong(id));
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
	 * 根据ID删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Result delete(long id) {
		boolean b = mediaFileService.deleteById(id);
		Result result = new Result();
		if(b) {
			result.setSuccess("1");
			result.setMsg("删除成功！");
		}else {
			result.setSuccess("0");
			result.setMsg("删除失败！");
		}
		return result;
	}


}
