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
}
