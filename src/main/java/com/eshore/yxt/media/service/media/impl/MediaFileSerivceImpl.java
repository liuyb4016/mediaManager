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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
                    predicateList.add(cb.equal(root.get("title"), mediaFileReq.getTitle()));
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
            BufferedReader br = new BufferedReader(new InputStreamReader(impFile.getInputStream(), "GBK"));

            String line = null;
            List<String> whitelistlist = new ArrayList<String>();
            boolean flg = false;
            int i = 0;

            DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
            String batchno = formater.format(new Date()) + Util.random6();
            logger.info("WhitelistSerivceImpl whitelistUpload 导入批次号是：batchno=" + batchno);

            while (null != (line = br.readLine())) {
                i++;
                if (StringUtils.isNotBlank(line)) {
                    line = line.trim();
                    if (!line.matches(TELPHONE_REGEX)) {
                        result.setSuccess("0");
                        result.setMsg("导入失败！导入的txt中，第" + i + "行手机号码不符合格式要求，终止导入！请修改后再重新导入！");
                        logger.info("WhitelistSerivceImpl whitelistUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg() + ";failure line:" + line);
                        break;
                    }
                    whitelistlist.add(line);
                }
            }

            br.close();
            if (!flg) {
                if (whitelistlist.size() > 0) {
                    for (String telphone : whitelistlist) {
                        Whitelist insertW = new Whitelist();
                        insertW.setBatchNo(batchno);
                        insertW.setTelphone(telphone);
                        insertW.setCreateTime(new Date());
                        insertW.setState(Constants.WHITE_LIST_STATE_1);
                        insertW.setRemark(remark);
                        insertW.setIsExchangeState(Constants.IS_EXCHANGE_STATE_1);
                        insert(insertW);
                        total++;
                    }
                }
                //更新
                updateWhitelistInvalid(batchno);
                result.setSuccess("1");
                result.setMsg("导入成功！共导入" + total + "条白名单数据；批次号：" + batchno);
                logger.info("WhitelistSerivceImpl whitelistUpload end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg());
                return result;
            }

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

	/*public boolean isExistWhitelist(String cardno) {
		boolean result = false;
		List<Whitelist> whitelists = mediaFileRepository.findWhitelistByCardno(cardno);
		if(null != whitelists && whitelists.size() >0){
			result = true;
		}
		return result;
	}*/


    public Result saveWhitelist(Whitelist whitelist) {

        Result result = new Result();
        if (StringUtils.isNotBlank(whitelist.getTelphone())) {

            String[] cards = whitelist.getTelphone().split(",");

            List<String> whitelists = new ArrayList<String>();
            int i = 1;
            //去重
            for (String c : cards) {
                if (StringUtils.isNotBlank(c)) {
                    if (!c.matches(TELPHONE_REGEX)) {
                        result.setSuccess("0");
                        result.setMsg("操作失败！第" + i + "个手机号码不符合格式要求！请修改后再重新提交！");
                        logger.info("WhitelistSerivceImpl saveWhitelist end isSuccess:" + result.getSuccess() + ";msg:" + result.getMsg() + ";failure line:" + i);
                        break;
                    }
                    whitelists.add(c);
                }
                i++;
            }

            DateFormat formater = new SimpleDateFormat("yyyyMMddHHmmss");
            String batchno = formater.format(new Date()) + Util.random6();
            logger.info("WhitelistSerivceImpl saveWhitelist 少量新增的批次号是：batchno=" + batchno);

            int total = 0;
            //插入
            for (String telphone : whitelists) {
                Whitelist insertW = new Whitelist();
                insertW.setBatchNo(batchno);
                insertW.setTelphone(telphone);
                insertW.setCreateTime(new Date());
                insertW.setState(Constants.WHITE_LIST_STATE_1);
                insertW.setRemark(whitelist.getRemark());
                insertW.setIsExchangeState(Constants.IS_EXCHANGE_STATE_1);
                insert(insertW);
                total++;
            }
            //更新
            updateWhitelistInvalid(batchno);
            result.setSuccess("1");
            result.setMsg("插入成功！共插入" + total + "条数据！批次号：" + batchno);

        } else {
            result.setSuccess("0");
            result.setMsg("白名单号码为空！");
        }


        return result;
    }

    /**
     * @param id
     * @return
     * @throws
     * @Description: 根据ID删除白名单信息
     * @author lbn
     * @date 2016年3月22日
     */
    public boolean deleteById(long id) {
        mediaFileRepository.updateWhiltelistById(id);
        return true;
    }

    public void updateWhitelistInvalid(String batchno) {
        logger.info("WhitelistSerivceImpl updateWhitelistInvalid start batchno=" + batchno);
        List<SysConfig> sysconfigs = sysconfigRepository.querySysconfigByTypeAndKey(Constants.CCB_WX_MANAGER, Constants.CCB_WX_ACTIVITY);
        int num = 0;
        if (null != sysconfigs && sysconfigs.size() > 0) {
            SysConfig sysconfig = sysconfigs.get(0);
            String activityId = sysconfig.getValue();
            logger.info("WhitelistSerivceImpl updateWhitelistInvalid 活动ID  activityId=" + activityId);
            if (StringUtils.isNotBlank(activityId)) {
                List<Activity> activitys = activityRepository.queryActivityById(Long.parseLong(activityId));
                if (null != activitys && activitys.size() > 0) {
                    Activity activity = activitys.get(0);
                    if (null != activity) {
                        num = activity.getUserOrderNum();
                        logger.info("WhitelistSerivceImpl updateWhitelistInvalid activity.getUserOrderNum()=" + num);

                    }
                }
            }
        }
        logger.info("WhitelistSerivceImpl updateWhitelistInvalid 活动数量  num=" + num);

        List<Whitelist> Whitelists = mediaFileRepository.qryWhiltelistForInvalid(batchno, num);
        int total = 0;
        if (null != Whitelists && Whitelists.size() > 0) {
            for (Whitelist whitelist : Whitelists) {
                if (null != whitelist && null != whitelist.getId()) {
                    int t = mediaFileRepository.updateWhiltelistInvalidById(whitelist.getId());
                    total = total + t;
                }
            }

        }

        logger.info("WhitelistSerivceImpl updateWhitelistInvalid end 更新无效数据的行数是：" + total);
    }

}
