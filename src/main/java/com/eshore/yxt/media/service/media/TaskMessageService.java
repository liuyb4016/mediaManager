package com.eshore.yxt.media.service.media;

import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.List;


public interface TaskMessageService {

    TaskMessage getTaskMessageByFileId(String type,String fileId);

    TaskMessage addOrUpdate(TaskMessage taskMessage);

    /**
     * @DESCRIPTION:
     *      TODO
     * @FILENAME: com.eshore.yxt.media.service.media.TaskMessageService
     * @METHODNAME: findListTaskMessageByStatus
     * @PARAM status  处理状态  0未处理 1 正在下载  2 已下载   3 正在转码处理 4 转码已处理
     *                  5 正在回调通知给第三方 6 已完成处理 -1 下载处理出错 -2 转码处理出错
     * ---------------------------------------
     * @AUTHOR: liuyb
     * @CREAETDATE: 2017/8/3
     * @CREATETIME: 9:46
     */
    public List<Long> findIdListTaskMessageByStatus(Integer status);

    TaskMessage getByid(Long id);
}
