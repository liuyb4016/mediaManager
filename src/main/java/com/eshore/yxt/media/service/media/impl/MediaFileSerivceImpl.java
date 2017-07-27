package com.eshore.yxt.media.service.media.impl;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.ExportUtil;
import com.eshore.yxt.media.core.util.Util;
import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.model.system.SysConfig;
import com.eshore.yxt.media.repository.media.MediaFileRepository;
import com.eshore.yxt.media.service.media.MediaFileService;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;
import org.activiti.bpmn.model.Activity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.criteria.*;
import javax.servlet.ServletOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MediaFileSerivceImpl implements MediaFileService {

    public static final Logger logger = LoggerFactory.getLogger(MediaFileSerivceImpl.class);

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Override
    public Grid findAllByPager(Pager pager, MediaFileReq mediaFileReq) {
        logger.info("MediaFileSerivceImpl findAllByPager start");
        Pageable pageable = new PageRequest(pager.getPage() - 1, pager.getRows());
        if (null != mediaFileReq) {
            logger.info("WhitelistSerivceImpl findAllByPager MediaFileReq " +
                    "title:" + mediaFileReq.getTitle() + "----> sourceId:" + mediaFileReq.getSourceId());
        }
        Page page = mediaFileRepository.findAll(queryByMutiCondition(mediaFileReq), pageable);
        List<MediaFile> mediaFileList = page.getContent();
        Grid grid = new Grid();
        grid.setTotal(page.getTotalElements());
        grid.setRows(mediaFileList);
        logger.info("MediaFileSerivceImpl findAllByPager end");
        return grid;
    }

    /**
     * @param mediaFileReq 查询条件
     * @return
     * @throws
     * @Description: 多条件查询
     * @author lbn
     * @date 2016年3月22日
     */
    public Specification<MediaFile> queryByMutiCondition(final MediaFileReq mediaFileReq) {
        return new Specification<MediaFile>() {
            public Predicate toPredicate(Root<MediaFile> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                logger.info("MediaFileSerivceImpl queryByMutiCondition start,param:[id:" + mediaFileReq.getId() + ";sourceId:" + mediaFileReq.getSourceId() + ";title:" + mediaFileReq.getTitle());
                //存放多个查询条件
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(mediaFileReq.getTitle())) {
                    predicateList.add(cb.like((Path)root.get("title"), "%"+mediaFileReq.getTitle()+"%"));
                }

                if (mediaFileReq.getType() != null) {
                    predicateList.add(cb.equal(root.get("type"), mediaFileReq.getTitle()));
                }

                if (mediaFileReq.getSourceId() != null) {
                    predicateList.add(cb.equal(root.get("sourceId"), mediaFileReq.getSourceId()));
                }

                Predicate[] p = new Predicate[predicateList.size()];
                logger.info("MediaFileSerivceImpl queryByMutiCondition end");
                return cb.and(predicateList.toArray(p));
            }
        };
    }


    @Override
    public List<MediaFile> findAll() {
        return (List<MediaFile>) mediaFileRepository.findAll();
    }

    public MediaFile insert(MediaFile mediaFile) {
        return mediaFileRepository.save(mediaFile);
    }


    @SuppressWarnings("finally")
    @Override
    public Result mediaFileUpload(CommonsMultipartFile mp4File,CommonsMultipartFile imgFile, String title,String mediaDesc) {
        logger.info("MediaFileSerivceImpl mediaFileUpload start:");
        Result result = new Result();
        int total = 0;
        try {
                result.setSuccess("1");
                result.setMsg("导入成功！共导入" + total + "条白名单数据；批次号：");
                logger.info("WhitelistSerivceImpl whitelistUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
                return result;

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("WhitelistSerivceImpl whitelistUpload exception:" + e.toString());
        } finally {
            if (null == result) {
                result = new Result();
                result.setSuccess("0");
                result.setMsg("导入失败，出现异常！");
            }
            logger.info("WhitelistSerivceImpl whitelistUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
            return result;
        }
    }

    public boolean deleteById(long id) {
        mediaFileRepository.delete(id);
        return true;
    }

}
