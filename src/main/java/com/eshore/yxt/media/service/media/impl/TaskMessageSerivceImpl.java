package com.eshore.yxt.media.service.media.impl;

import com.eshore.yxt.media.core.util.media.FileDigest;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.media.TaskMessage;
import com.eshore.yxt.media.repository.media.MediaFileRepository;
import com.eshore.yxt.media.repository.media.TaskMessageRepository;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.service.media.TaskMessageService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TaskMessageSerivceImpl implements TaskMessageService {

    public static final Logger logger = LoggerFactory.getLogger(TaskMessageSerivceImpl.class);

    @Autowired
    private TaskMessageRepository taskMessageRepository;

    @Value("root.file.path")
    private String mediaFileRootPath;


    @Override
    public TaskMessage getTaskMessageByFileId(String type,String fileId) {
        return taskMessageRepository.getTaskMessageByFileId(type,fileId);
    }

    @Override
    public TaskMessage addOrUpdate(TaskMessage taskMessage) {
        return taskMessageRepository.save(taskMessage);
    }

}
