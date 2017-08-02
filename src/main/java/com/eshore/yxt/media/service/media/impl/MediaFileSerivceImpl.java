package com.eshore.yxt.media.service.media.impl;

import com.eshore.yxt.media.core.constants.Constants;
import com.eshore.yxt.media.core.util.ExportUtil;
import com.eshore.yxt.media.core.util.Util;
import com.eshore.yxt.media.core.util.media.FileDigest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.criteria.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MediaFileSerivceImpl implements MediaFileService {

    public static final Logger logger = LoggerFactory.getLogger(MediaFileSerivceImpl.class);

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("root.file.path")
    private String mediaFileRootPath;

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

    @Override
    public Long countMediaFileByTitle(MediaFileReq mediaFileReq) {
        logger.info("MediaFileSerivceImpl findMediaFileByTitle start");
        if (null != mediaFileReq) {
            logger.info("WhitelistSerivceImpl findMediaFileByTitle MediaFileReq " +
                    "title:" + mediaFileReq.getTitle() + "----> sourceId:" + mediaFileReq.getSourceId());
        }
        Long count = mediaFileRepository.count(queryByMutiCondition(mediaFileReq));
        logger.info("MediaFileSerivceImpl findMediaFileByTitle end");
        return count;
    }

    @Override
    public List<MediaFile> findMediaFileByidsourceId(Long id) {
        logger.info("MediaFileSerivceImpl findMediaFileByidsourceId start");
        MediaFileReq mediaFileReq = new MediaFileReq();
        mediaFileReq.setId(id);
        mediaFileReq.setSourceId(id);

        if (null != mediaFileReq) {
            logger.info("WhitelistSerivceImpl findMediaFileByTitle MediaFileReq " +
                    "id:" + mediaFileReq.getId() + "----> sourceId:" + mediaFileReq.getSourceId());
        }
        List<MediaFile> list = mediaFileRepository.findAll(queryByMutiCondition(mediaFileReq));
        logger.info("MediaFileSerivceImpl findMediaFileByidsourceId end");
        return list;
    }

    /**
     * @param mediaFileReq 查询条件
     * @return
     * @throws
     * @Description: 多条件查询
     * @author lbn
     * @date 2016年3月22日
     */
    private Specification<MediaFile> queryByMutiCondition(final MediaFileReq mediaFileReq) {
        return new Specification<MediaFile>() {
            public Predicate toPredicate(Root<MediaFile> root,
                                         CriteriaQuery<?> query, CriteriaBuilder cb) {
                logger.info("MediaFileSerivceImpl queryByMutiCondition start,param:[id:" + mediaFileReq.getId() + ";sourceId:" + mediaFileReq.getSourceId() + ";title:" + mediaFileReq.getTitle());
                //存放多个查询条件
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if(mediaFileReq.getId()!=null&&mediaFileReq.getSourceId()!=null){
                    predicateList.add(cb.equal(root.get("id").as(Long.class), mediaFileReq.getId()));
                    predicateList.add(cb.or(cb.equal(root.get("sourceId").as(Long.class), mediaFileReq.getSourceId())));
                }else{
                    if(mediaFileReq.getNotId()!=null){
                        if (StringUtils.isNotBlank(mediaFileReq.getTitle())) {
                            predicateList.add(cb.like(root.get("title").as(String.class), mediaFileReq.getTitle()));
                        }
                        if (mediaFileReq.getNotId() != null) {
                            predicateList.add(cb.notEqual(root.get("id").as(Long.class), mediaFileReq.getNotId()));
                        }
                    }else{
                        if (StringUtils.isNotBlank(mediaFileReq.getTitle())) {
                            predicateList.add(cb.like(root.get("title").as(String.class), "%"+mediaFileReq.getTitle()+"%"));
                        }
                    }

                    if (mediaFileReq.getType() != null) {
                        predicateList.add(cb.equal(root.get("type").as(Integer.class), mediaFileReq.getType()));
                    }
                    if (mediaFileReq.getSourceId() != null) {
                        predicateList.add(cb.equal(root.get("sourceId").as(Long.class), mediaFileReq.getSourceId()));
                    }
                }
                Predicate[] p = new Predicate[predicateList.size()];
                logger.info("MediaFileSerivceImpl queryByMutiCondition end");
                return cb.and(predicateList.toArray(p));
            }
        };
    }

    @Override
    public Result mediaFileUpload(CommonsMultipartFile mp4File,CommonsMultipartFile imgFile, String title,String mediaDesc,Long id) {
        logger.info("MediaFileSerivceImpl mediaFileUpload start:");
        //CommonsMultipartFile
        Result result = new Result();
        MediaFile mediaFile = new MediaFile();
        try {
            if(id>0){
                //更新
                MediaFileReq mediaFileReq = new MediaFileReq();
                mediaFileReq.setId(id);
                mediaFileReq.setTitle(title);
                Long count = this.countMediaFileByTitle(mediaFileReq);
                if(count>0){
                    result.setSuccess("-1");
                    result.setMsg("修改的名称已存在，请修改后再提交");
                    logger.info("MediaFileSerivceImpl mediaFileUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
                    return result;
                }

                mediaFile = mediaFileRepository.findOne(id);
                mediaFile.setTitle(title);
                mediaFile.setMediaDesc(mediaDesc);
                mediaFile.setUpdateTime(new Date());
                mediaFileRepository.save(mediaFile);

                result.setSuccess("1");
                result.setMsg("更新文件信息成功");
                logger.info("MediaFileSerivceImpl mediaFileUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
                return result;
            }else{
                //新增
                mediaFile = new MediaFile();
                mediaFile.setTitle(title);
                mediaFile.setMediaDesc(mediaDesc);
                mediaFile.setType(0);
                String mp4FilePath = this.uploadFile(mp4File);
                if(StringUtils.isNotBlank(mp4FilePath)){
                    File file = new File(mp4FilePath);
                    if(!file.exists()){
                        result.setSuccess("-2");
                        result.setMsg("视频文件上传失败，请重新上传");
                        logger.info("MediaFileSerivceImpl mediaFileUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
                        return result;
                    }
                    mediaFile.setFileName(mp4FilePath);
                    mediaFile.setSize(file.length());
                    mediaFile.setMd5(FileDigest.getFileMD5(file));
                }else{
                    result.setSuccess("-3");
                    result.setMsg("视频文件上传失败，请重新上传");
                    logger.info("MediaFileSerivceImpl mediaFileUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
                    return result;
                }
                String imgFilePath = this.uploadFile(imgFile);
                //mediaFile.setShowPic(imgFilePath);
                mediaFile.setCreateTime(new Date());
                mediaFile.setUpdateTime(new Date());
                mediaFile.setSourceId(0L);
                mediaFileRepository.save(mediaFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("WhitelistSerivceImpl whitelistUpload exception:" + e.toString());
        } finally {
            if (null == result) {
                result = new Result();
                result.setSuccess("-4");
                result.setMsg("视频文件上传失败，请重新上传");
            }
            logger.info("WhitelistSerivceImpl whitelistUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
            return result;
        }
    }

    @Override
    public boolean deleteById(long id) {
        List<MediaFile> list = this.findMediaFileByidsourceId(id);
        if(list!=null){
            for(MediaFile mediaFile:list){
                Long sourceId = mediaFile.getId();
                //删除源文件、处理后的标清、高清视频资源
                new File(mediaFile.getFileName()).deleteOnExit();
                //删除图片
                /*if("0".equals(mediaFile.getShowPic())){
                    new File(mediaFile.getShowPic()).deleteOnExit();
                }*/
                //删除数据库信息
                mediaFileRepository.delete(mediaFile);
            }
            return true;
        }
        return false;
    }

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    private String uploadFile(CommonsMultipartFile multipartFile){

        String originalFileName =  multipartFile.getOriginalFilename();

        if(StringUtils.isEmpty(originalFileName)) {//上传文件为空
            return null;
        }

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd");
        /** 构建文件保存的目录* */
        String logoPathDir = mediaFileRootPath+File.separator+ dateformat.format(new Date());

        /** 根据真实路径创建目录* */
        File logoSaveFile = new File(logoPathDir);
        if (!logoSaveFile.exists())
            logoSaveFile.mkdirs();

        /** 获取文件的后缀* */
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        /** 使用UUID生成文件名称* */
        String logImageName = UUID.randomUUID().toString() + suffix;// 构建文件名称
        /** 拼成完整的文件保存路径加文件**/
        String fileName = logoPathDir + File.separator + logImageName;
        File file = new File(fileName);
        try {
            multipartFile.transferTo(file);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /** 打印出上传到服务器的文件的绝对路径* */
        logger.debug("****************" + fileName + "upload end**************");
        return fileName;
    }

}
