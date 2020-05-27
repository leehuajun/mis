package com.sunjet.mis.helper;


import com.sunjet.mis.module.system.entity.UserEntity;
import com.sunjet.mis.utils.common.RandomStringGeneratorAdvanced;
import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lhj
 * @create 2015-12-21 上午9:45
 */
public class CommonHelper {

    public static Integer GRID_LINE_HEIGHT = 33;

    public static Integer LEN_SHOW = 5;
    public static Boolean isOk = false;
    //浏览器分辨率高度
    public static Integer windowHeight = 0;
    //屏幕分辨率高度
    public static Integer screenHeight = 0;

    public static Integer windowWidth = 0;
    public static Integer baseGridHeight = 0;

    public static Integer FILTER_VEHICLE_LEN = 4;
    public static String FILTER_VEHICLE_ERROR = "请至少输入VIN后面" + FILTER_VEHICLE_LEN + "位!";

    public static Integer FILTER_PARTS_LEN = 2;
    public static String FILTER_PARTS_ERROR = "请检索关键字至少输入" + FILTER_PARTS_LEN + "位!";

    /**
     * 作废的提示信息
     */
    public static String DESERT_TASK_MESSAGE = "是否作废此单据？\n如果作废相关单据窗口将会被关闭";

    public static final String UPLOAD_DIR_ASM = "/uploads/asm/";
    public static final String UPLOAD_DIR_AGENCY = "/uploads/agency/";
    public static final String UPLOAD_DIR_DEALER = "/uploads/dealer/";
    public static final String UPLOAD_DIR_FLOW = "/uploads/flow/";
    public static final String UPLOAD_DIR_NOTICE = "/uploads/notice/";
    public static final String UPLOAD_DIR_OTHER = "/uploads/other/";
    public static final String UPLOAD_DIR_TEMP = "/uploads/temp/";

    public static Map<String, Object> mapDocumentNo = new HashMap<>();

    static {
        mapDocumentNo.put("AgencyAdmitRequestEntity", "HZSA");    // 合作商准入申请
        mapDocumentNo.put("AgencyAlterRequestEntity", "HZSB");    // 合作商变更申请
        mapDocumentNo.put("AgencyQuitRequestEntity", "HZSQ");     // 合作商退出申请
        mapDocumentNo.put("DealerAdmitRequestEntity", "FWZA");    // 服务站准入申请
        mapDocumentNo.put("DealerAlterRequestEntity", "FWZB");    // 服务站变更申请
        mapDocumentNo.put("DealerLevelChangeRequestEntity", "FWZD"); // 服务站等级变更申请
        mapDocumentNo.put("DealerQuitRequestEntity", "FWZT");     // 服务站退出申请
        mapDocumentNo.put("ActivityDistributionEntity", "ADFB");  // 活动发布单
        mapDocumentNo.put("ActivityMaintenanceEntity", "AMFW");   // 活动服务单
        mapDocumentNo.put("ActivityNoticeEntity", "HDTZ");        // 活动通知单
        mapDocumentNo.put("AgencySettlementEntity", "AEJS");      // 合作商费用结算单
        mapDocumentNo.put("DealerSettlementEntity", "DEJS");      // 服务站费用结算单
        mapDocumentNo.put("FreightSettlementEntity", "FEJS");      // 运费结算单
        mapDocumentNo.put("ExpenseReportEntity", "FYSB");         // 费用速报
        mapDocumentNo.put("FirstMaintenanceEntity", "FMFW");      // 首保服务单
        mapDocumentNo.put("QualityReportEntity", "ZLSB");         // 质量速报
        mapDocumentNo.put("RecycleEntity", "GZFH");               // 故障件返回单
        mapDocumentNo.put("RecycleNoticeEntity", "FHTZ");         // 故障件返回通知单
        mapDocumentNo.put("ServiceProxyEntity", "FWWT");          // 服务委托单
        mapDocumentNo.put("SupplyEntity", "PJGH");                // 供货单
        mapDocumentNo.put("SupplyNoticeEntity", "GHTZ");          // 供货通知单
        mapDocumentNo.put("WarrantyMaintenanceEntity", "WMFW");   // 三包服务单
        mapDocumentNo.put("LeaveBill", "LEAV");   // 请假申请
    }

    /**
     * 流程审核完成，是否自动关闭？ true:自动关闭  false:不自动关闭（由事件出发关闭）
     */
    public static Map<String, Boolean> mapAutoClose = new HashMap<>();

    static {
        mapAutoClose.put("AgencyAdmitRequestEntity", true);          // 合作商准入申请
        mapAutoClose.put("AgencyAlterRequestEntity", true);          // 合作商变更申请
        mapAutoClose.put("AgencyQuitRequestEntity", true);       // 合作商退出申请
        mapAutoClose.put("DealerAdmitRequestEntity", true);      // 服务站准入申请
        mapAutoClose.put("DealerAlterRequestEntity", true);      // 服务站变更申请
        mapAutoClose.put("DealerLevelChangeRequestEntity", true); // 服务站等级变更申请
        mapAutoClose.put("DealerQuitRequestEntity", true);       // 服务站退出申请
        mapAutoClose.put("ActivityDistributionEntity", true);    // 活动发布单
        mapAutoClose.put("ActivityMaintenanceEntity", true);     // 活动服务单
        mapAutoClose.put("ActivityNoticeEntity", true);              // 活动通知单
        mapAutoClose.put("AgencySettlementEntity", true);            // 合作商费用结算单
        mapAutoClose.put("DealerSettlementEntity", true);        // 服务站费用结算单
        mapAutoClose.put("FreightSettlementEntity", true);        // 运费结算单
        mapAutoClose.put("ExpenseReportEntity", true);           // 费用速报
        mapAutoClose.put("FirstMaintenanceEntity", true);        // 首保服务单
        mapAutoClose.put("QualityReportEntity", true);           // 质量速报
        mapAutoClose.put("RecycleEntity", true);                 // 故障件返回单
        mapAutoClose.put("RecycleNoticeEntity", true);           // 故障件返回通知单
        mapAutoClose.put("ServiceProxyEntity", true);            // 服务委托单
        mapAutoClose.put("SupplyEntity", true);                  // 供货单
        mapAutoClose.put("SupplyNoticeEntity", true);             // 供货通知单
        mapAutoClose.put("WarrantyMaintenanceEntity", false);     // 三包服务单
        mapAutoClose.put("LeaveBill", true);   // 请假申请
    }


    /**
     * 密码初始化
     *
     * @param userEntity
     * @return
     */
    public static UserEntity genPassword(UserEntity userEntity) {
        String salt = RandomStringGeneratorAdvanced.getRandom(10, RandomStringGeneratorAdvanced.TYPE.LETTER_CAPITAL_NUMBER_SIGN);
        Integer hashInterations = 1;
        Md5Hash md5Hash = new Md5Hash(userEntity.getPassword(), salt, hashInterations);
        userEntity.setPassword(md5Hash.toString());
        userEntity.setSalt(salt);
        return userEntity;
    }


    public static String genMD5(String str) {
        Md5Hash md5Hash = new Md5Hash(str);
        return md5Hash.toString();
    }

    /**
     * 生成固定长度的流水号，不足长度，前面补0
     *
     * @param sn          数字
     * @param fixedLength 长度
     * @return
     */
    public static String genFixedStringBySn(Integer sn, Integer fixedLength) {
        // 0 代表前面补充0
        // 10代表长度为10
        // d 代表参数为正数型
        String format = "%0" + fixedLength + "d";
        String fixedString = String.format(format, sn);
//        logger.info(fixedString);
        return fixedString;
    }


    public static boolean rexCheckPassword(String password) {
        // 6-20 位，字母、数字、字符
        //String reg = "^([A-Z]|[a-z]|[0-9]|[`-=[];,./~!@#$%^*()_+}{:?]){6,20}$";
        String regStr = "^((?=.*[0-9].*)(?=.*[A-Za-z].*))[_0-9A-Za-z@!#%]{6,16}$";
        return password.matches(regStr);
    }

    public static boolean rexCheckEmail(String email) {
        String regStr = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return email.matches(regStr);
    }
}
