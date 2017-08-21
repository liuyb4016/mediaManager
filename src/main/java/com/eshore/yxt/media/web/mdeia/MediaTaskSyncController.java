package com.eshore.yxt.media.web.mdeia;

import com.alibaba.fastjson.JSONArray;
import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.HttpUtils;
import com.eshore.yxt.media.core.util.cache.MemcacheCaller;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskLogService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import com.eshore.yxt.media.web.mdeia.req.TaskMessageReq;
import com.eshore.yxt.media.web.mdeia.resp.TaskMessageResq;
import com.eshore.yxt.media.web.system.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;

@Controller
@RequestMapping(value="/mediaTask")
public class MediaTaskSyncController extends BaseController {

    public static final Logger logger = LoggerFactory.getLogger(MediaTaskSyncController.class);
    @Autowired
    private TaskMessageService taskMessageService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private MediaFileService mediaFileService;

	@RequestMapping(value = "/createtask",method = RequestMethod.POST)
	@ResponseBody
	public TaskMessageResq createtask(@RequestParam(value="type",required = true)  String type,
                                      @RequestParam(value="fileId",required = true)  String fileId,
                                      @RequestParam(value="videoName",required = true)  String videoName,
                                      @RequestParam(value="videoSize",required = true)  String videoSize,
                                      @RequestParam(value="videoMd5",required = true)  String videoMd5,
                                      @RequestParam(value="videoUrl",required = true)  String videoUrl,
                                      @RequestParam(value="callbackUrl",required = true)  String callbackUrl
                                      ) {
        /**
         * private String type;//数据id  1 院线通片花
         private String fileId;//文件ID
         private String videoName;//视频名称
         private Long videoSize;//视频大小
         private String videoMd5;//视频md5
         private String videoUrl;//视频下载路径
         private String callbackUrl;//处理结果回调  提供一个接收结果的接口
         */
        TaskMessageResq taskMessageResq = new TaskMessageResq();
        taskMessageResq.setType(type);
        taskMessageResq.setFileId(fileId);
        taskMessageResq.setStatus("1");
        taskMessageResq.setErrorMsg("任务创建成功，转码工作由定时任务完成。");
        Boolean isIn =  MemcacheCaller.INSTANCE.add(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId, 2, "2");

        try{
            if(isIn){
                TaskMessage taskMessage = taskMessageService.getTaskMessageByFileId(Integer.valueOf(type),fileId);
                if(taskMessage!=null){
                    taskMessageResq.setStatus("-10001");
                    taskMessageResq.setTaskId(taskMessage.getTaskId());
                    taskMessageResq.setErrorMsg("创建任务的文件已存在,无法再创建新的任务了");
                    MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
                    return  taskMessageResq;
                }

                taskMessage = new TaskMessage();
                taskMessage.setType(Integer.valueOf(type));
                taskMessage.setTaskId(DateFormatUtils.format(new Date(),"yyyyMMddHH")+"_"+ UUID.randomUUID().toString());
                taskMessage.setCallbackUrl(callbackUrl);
                taskMessage.setStatus(Constants.TaskMessageStatus.NO_DULE);
                taskMessage.setCreateTime(new Date());
                taskMessage.setUpdateTime(new Date());
                taskMessage.setFileId(fileId);
                taskMessage.setVideoMd5(videoMd5);
                taskMessage.setVideoName(videoName);
                taskMessage.setVideoSize(Long.valueOf(videoSize));
                taskMessage.setVideoUrl(videoUrl);
                taskMessageService.addOrUpdate(taskMessage);
                taskLogService.addLog(taskMessage.getTaskId(),1,"成功创建任务");
                taskMessageResq.setTaskId(taskMessage.getTaskId());
                MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
            }else{
                taskMessageResq.setStatus("-10002");
                taskMessageResq.setErrorMsg("操作重复，已发送创建任务的操作");
                MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
                return  taskMessageResq;
            }
        }catch(Exception e){
            e.printStackTrace();
            MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
            taskMessageResq.setStatus("-10003");
            taskMessageResq.setErrorMsg("系统错误");
        }finally {
            MemcacheCaller.INSTANCE.delete(Constants.CREATE_TASK_KEY+"_"+type+"_"+fileId);
        }
        return taskMessageResq;
	}

    @RequestMapping(value = "/getTaskResult",method = RequestMethod.POST)
    @ResponseBody
    public List<TaskMessageResq> getTaskResult(String taskId) {
	    if(StringUtils.isBlank(taskId)){
            return new ArrayList<TaskMessageResq>();
        }

        TaskMessage taskMessage = taskMessageService.getTaskMessageByTaskId(taskId);
        if(taskMessage==null||taskMessage.getStatus().intValue()!= Constants.TaskMessageStatus.DULED.intValue()){
            return new ArrayList<TaskMessageResq>();
        }

        ///转码完成。新增视频文件数据
        MediaFile mediaFile1 = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_1");
        MediaFile mediaFile2 = mediaFileService.getMediaFileBTaskId(taskMessage.getTaskId()+"_2");
        //类型  1 标清  2 高清
        List<TaskMessageResq> listNew = new ArrayList<TaskMessageResq>();
        if(mediaFile1!=null) listNew.add(new TaskMessageResq(taskMessage.getTaskId(),mediaFile1.getFileId(),mediaFile1.getDefType()+"",taskMessageService.getMediaUrl(taskMessage,mediaFile1)));
        if(mediaFile2!=null) listNew.add(new TaskMessageResq(taskMessage.getTaskId(),mediaFile2.getFileId(),mediaFile2.getDefType()+"",taskMessageService.getMediaUrl(taskMessage,mediaFile2)));
        return listNew;
    }

    @RequestMapping(value = "/result",method = RequestMethod.POST)
    @ResponseBody
    public String result(String result) {
        logger.info("result---->"+result);
	    return "ok";
    }

}
