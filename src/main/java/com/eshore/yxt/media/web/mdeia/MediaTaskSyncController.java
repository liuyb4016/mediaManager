package com.eshore.yxt.media.web.mdeia;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.cache.MediaCache;
import com.eshore.yxt.media.core.util.cache.MemcacheCaller;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;
import com.eshore.yxt.media.web.mdeia.req.TaskMessageReq;
import com.eshore.yxt.media.web.mdeia.resp.TaskMessageResq;
import com.eshore.yxt.media.web.system.BaseController;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
@RequestMapping(value="/mediaTask")
public class MediaTaskSyncController extends BaseController {


	@Autowired
	private MediaFileService mediaFileService;
    @Autowired
    private TaskMessageService taskMessageService;

	@RequestMapping("/createtask")
	@ResponseBody
	public TaskMessageResq createtask(@RequestParam(value="task",required = true) TaskMessageReq task) {

        TaskMessageResq taskMessageResq = new TaskMessageResq();
        String type = task.getType();
        String fileId = task.getFileId();

        taskMessageResq.setType(type);
        taskMessageResq.setFileId(fileId);

        if(StringUtils.isBlank(type)||StringUtils.isBlank(fileId)||StringUtils.isBlank(task.getVideoMd5())||StringUtils.isBlank(task.getVideoName())
                ||StringUtils.isBlank(task.getVideoUrl())||task.getVideoSize()==null||task.getVideoSize()==0L||StringUtils.isBlank(task.getCallbackUrl())){
            taskMessageResq.setStatus("-10001");
            taskMessageResq.setErrorMsg("任务的参数错误");
            return taskMessageResq;
        }

        Boolean isIn =  MemcacheCaller.INSTANCE.add(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId, 2, "2");
        try{
            if(isIn){
                TaskMessage taskMessage = taskMessageService.getTaskMessageByFileId(type,fileId);
                if(taskMessage!=null){
                    taskMessageResq.setStatus("-10002");
                    taskMessageResq.setErrorMsg("创建任务的文件已存在,无法再创建新的任务了");
                    MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
                    return  taskMessageResq;
                }

                taskMessage = new TaskMessage();
                taskMessage.setTaskId(DateFormatUtils.format(new Date(),"yyyyMMddHH")+"_"+ UUID.randomUUID().toString());
                taskMessage.setFileId(task.getFileId());
                taskMessage.setCallbackUrl(task.getCallbackUrl());
                taskMessage.setStatus(Constants.TaskMessageStatus.NO_DULE);
                taskMessage.setCreateTime(new Date());
                taskMessage.setUpdateTime(new Date());
                taskMessage.setFileId(fileId);
                taskMessage.setVideoId(task.getVideoId());
                taskMessage.setVideoMd5(task.getVideoMd5());
                taskMessage.setVideoName(task.getVideoName());
                taskMessage.setVideoSize(task.getVideoSize());
                taskMessage.setUrlType(task.getUrlType());
                taskMessage.setVideoUrls(task.getVideoUrl());
                taskMessageService.addOrUpdate(taskMessage);
                MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
            }else{
                taskMessageResq.setStatus("-10003");
                taskMessageResq.setErrorMsg("操作重复，已发送创建任务的操作");
                MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
                return  taskMessageResq;
            }
        }catch(Exception e){
            e.printStackTrace();
            MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
            taskMessageResq.setStatus("-10004");
            taskMessageResq.setErrorMsg("系统错误");
        }finally {
            MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
        }
        return taskMessageResq;
	}

}
