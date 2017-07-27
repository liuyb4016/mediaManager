package com.eshore.yxt.media.service.media;

import com.eshore.yxt.media.model.media.MediaFile;
import com.eshore.yxt.media.web.base.Grid;
import com.eshore.yxt.media.web.base.Pager;
import com.eshore.yxt.media.web.base.Result;
import com.eshore.yxt.media.web.mdeia.req.MediaFileReq;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.ServletOutputStream;
import java.util.List;


public interface MediaFileService {

    /**
     * @DESCRIPTION: 查询流媒体文件列表
     * @FILENAME: com.eshore.yxt.media.service.media.MediaFileService
     * @METHODNAME: findAllByPager
     * @PARAM pager
     * @PARAM mediaFileReq
     * ---------------------------------------
     * @AUTHOR: liuyb
     * @CREAETDATE: 2017/7/27
     * @CREATETIME: 14:21
     */
    public Grid findAllByPager(Pager pager, MediaFileReq mediaFileReq);

    /**
     * @DESCRIPTION: 查询所有 数据
     * @FILENAME: com.eshore.yxt.media.service.media.MediaFileService
     * @METHODNAME: findAll
     * @PARAM ---------------------------------------
     * @AUTHOR: liuyb
     * @CREAETDATE: 2017/7/27
     * @CREATETIME: 14:22
     */
    public List<MediaFile> findAll();

    /**
     * @DESCRIPTION: 上传数据，批量上传，全部覆盖，txt文件格式
     * @FILENAME: com.eshore.yxt.media.service.media.MediaFileService
     * @METHODNAME: mediaFileUpload
     * @PARAM impFile
     * @PARAM remark
     * ---------------------------------------
     * @AUTHOR: liuyb
     * @CREAETDATE: 2017/7/27
     * @CREATETIME: 14:22
     */
    public Result mediaFileUpload(CommonsMultipartFile mp4File,CommonsMultipartFile imgFile, String title,String mediaDesc);

    /**
     * @DESCRIPTION: 根据ID删除信息
     * @FILENAME: com.eshore.yxt.media.service.media.MediaFileService
     * @METHODNAME: deleteById
     * @PARAM id
     * ---------------------------------------
     * @AUTHOR: liuyb
     * @CREAETDATE: 2017/7/27
     * @CREATETIME: 14:23
     */
    public boolean deleteById(long id);
}
