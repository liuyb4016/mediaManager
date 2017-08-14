package com.eshore.yxt.media.core.constants;

public class Constants {
    public static final String CURRENT_USER = "user";
    public static final String STOCK_AlTER = "stockalter";

    public static final String CREATE_TASK_KEY = "CREATE_TASK_KEY";

    public static final String DOWNLOADING_TASK_KEY = "DOWNLOADING_TASK_KEY";

    /**
     * @DESCRIPTION:
     *      媒体文件类型
     * @PACKAGE: com.eshore.yxt.media.core.constants
     * @CLASSNAME: Constants
     * ---------------------------------------
     * @AUTHOR: liuyb
     * @CREATEDATE: 2017/7/27
     * @CREATETIME: 12:01
     */
    public static class MediaType{
        public static final Integer TYPE_SOURCE = 0;//源文件
        public static final Integer TYPE_SD = 1;//标清
        public static final Integer TYPE_HD = 2;//高清

    }
    /**
     * @DESCRIPTION:
     *      处理状态  0未处理 1 正在下载  2 已下载   3 正在转码处理 4 转码已处理 5 正在转码处理 6 转码已处理
     *      7 正在回调通知给第三方 8 已完成处理 -1 下载处理出错 -2 转码处理出错
     * @PACKAGE: com.eshore.yxt.media.core.constants
     * @CLASSNAME: TaskMessageStatus
     * ---------------------------------------
     * @AUTHOR: liuyb
     * @CREATEDATE: 2017/8/3
     * @CREATETIME: 10:03
     */
    public static class TaskMessageStatus{
        public static final Integer NO_DULE = 0;//未处理

        public static final Integer DOWNLOADING = 1;//正在下载
        public static final Integer DOWNLOADED = 2;//已下载

        public static final Integer FFMPEG270ING = 3;//正在转码处理
        public static final Integer FFMPEG270ED = 4;//转码已处理

        public static final Integer FFMPEG720ING = 5;//正在转码处理
        public static final Integer FFMPEG720ED = 6;//转码已处理

        public static final Integer CALLBACKING = 7;//正在回调通知给第三方
        public static final Integer DULED = 8;//已完成处理

        public static final Integer DOWNLOAD_ERROR = -1;//下载处理出错
        public static final Integer FFMPEG270_ERROR = -2;//转码处理出错
        public static final Integer FFMPEG720_ERROR = -3;//转码处理出错
        public static final Integer CALLBACK_ERROR = -4;//回调url
    }

}
