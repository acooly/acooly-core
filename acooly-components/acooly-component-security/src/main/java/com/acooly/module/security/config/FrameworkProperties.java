package com.acooly.module.security.config;

import com.acooly.module.security.domain.User;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author qiubo
 */
@ConfigurationProperties("acooly.framework")
@Getter
@Setter
@ToString
public class FrameworkProperties implements Serializable {
	public static final int SHOW_STATE_YES = 0;
	public static final int SHOW_STATE_NO = 1;
	public static final int SHOW_MODE_AJAXLOAD = 1;
	public static final int SHOW_MODE_IFRAME = 2;
	public static final int LOAD_MODE_URL = 1;
	public static final int LOAD_MODE_HTML = 2;
	public static final String RESOURCE_TYPE_URL = "URL";
	public static final String RESOURCE_TYPE_FUNCTION = "FUNCTION";
	public static final String RESOURCE_TYPE_MENU = "MENU";
	
	private String title = "Acooly Boss 4.x";
	
	private String subtitle = "专注于业务开发，规范最佳实践，自动代码生成，提高70%效率！";
	
	private String logo = "/manage/assert/image/logo.png";
	
	private String copyright = "Copyright © 2016 acooly. All rights reserved";
	
	/** 是否开启同名用户登录互斥 开关 [未实现] */
	private boolean conflict = false;
	
	/** 是否开启密码过期处理 开关 */
	private boolean expire = true;
	
	/** 多长时间密码过期 单位天 */
	private int expireDays = 90;
	
	/** 密码错误次数锁定 开关 */
	private boolean loginLock = false;
	/** 密码错误几次后触发锁定 */
	private int loginLockErrorTimes = 5;
	/** 密码锁定时长 秒 */
	private long loginLockSeconds = 43200;
	private List<String> icons = Lists.newArrayList();
	
	/** 密码格式组成规则 */
	//[a-zA-Z]{1}[\\\\w]{7,15}  密码必须以字母开头，由字母、数字、下划线组成，长度8-16字节。
	private String passwordRegex = "[\\\\w]{6,16}";
	/** 密码格式错误提示 */
	private String passwordError = "密码由任意字母、数字、下划线组成，长度6-16字节";
	/** 用户状态 */
	private Map<Integer, String> userStatus = Maps.newLinkedHashMap();
	/** 用户类型 */
	private Map<Integer, String> userTypes = Maps.newLinkedHashMap();
	public Map<Integer, String> showStates = Maps.newLinkedHashMap();
	public Map<Integer, String> showModes = Maps.newLinkedHashMap();
	public Map<Integer, String> loadModes = Maps.newLinkedHashMap();
	public Map<String, String> resourceTypes = Maps.newLinkedHashMap();
	
	public FrameworkProperties() {
		userStatus.put(User.STATUS_ENABLE, "正常");
		userStatus.put(User.STATUS_LOCK, "冻结");
		userStatus.put(User.STATUS_EXPIRES, "密码过期");
		userStatus.put(User.STATUS_DISABLE, "禁用");
		userTypes.put(1, "管理员");
		userTypes.put(2, "操作员");
		showStates.put(SHOW_STATE_YES, "显示");
		showStates.put(SHOW_STATE_NO, "隐藏");
		showModes.put(SHOW_MODE_AJAXLOAD, "AJAX加载");
		showModes.put(SHOW_MODE_IFRAME, "IFrame加载");
		loadModes.put(LOAD_MODE_URL, "URL");
		loadModes.put(LOAD_MODE_HTML, "文本");
		resourceTypes.put(RESOURCE_TYPE_URL, "URL");
		resourceTypes.put(RESOURCE_TYPE_FUNCTION, "FUNCTION");
		resourceTypes.put(RESOURCE_TYPE_MENU, "MENU");
		String iconsStr = "icons-resource-android,icons-resource-androidcyc,icons-resource-app,icons-resource-app1," +
				"icons-resource-apple,icons-resource-ipad,icons-resource-iphone,icons-resource-mac,icons-resource-6," +
				"icons-resource-access-user,icons-resource-admin,icons-resource-anli,icons-resource-anli1,icons-resource-benqihuankuan" +
				",icons-resource-bianmin,icons-resource-biao,icons-resource-caozuorizhichaxun,icons-resource-changjianwenti,icons-resource-chongzhi" +
				",icons-resource-chongzhi1,icons-resource-choujiang,icons-resource-choujiang1,icons-resource-choujiang2,icons-resource-dailishang" +
				",icons-resource-dengebenxihuankuanbiao,icons-resource-dingdan,icons-resource-dingdan1,icons-resource-diyabiao,icons-resource-fabujiekuan" +
				",icons-resource-fangdaozhuomian,icons-resource-guanggao,icons-resource-guanyuwomen,icons-resource-guanyuwomen1,icons-resource-hezuo," +
				"icons-resource-huankuanguanli,icons-resource-huankuankuan,icons-resource-hudongchoujiang,icons-resource-huoqilicai2,icons-resource-icon," +
				"icons-resource-icon12fuben,icons-resource-iconfontcolor28,icons-resource-iconfontliushuizijin,icons-resource-jiekuan1,icons-resource-jiekuanbiaoxinxi," +
				"icons-resource-jiekuanhetongguanli,icons-resource-jiekuanxiangqing,icons-resource-jinbizhuanchu,icons-resource-jinbizhuanru,icons-resource-jingjirenfuzhi," +
				"icons-resource-jingxiaoshang,icons-resource-kehuguanli,icons-resource-lianxiwomen,icons-resource-meiyuehuankuan,icons-resource-meiyuehuankuan1," +
				"icons-resource-shezhi,icons-resource-shezhi1,icons-resource-shimingrenzheng,icons-resource-shimingrenzheng1,icons-resource-shimingrenzheng3," +
				"icons-resource-shoukuan,icons-resource-shouye,icons-resource-shouyeshouye,icons-resource-shouyi,icons-resource-shouzhimingxi,icons-resource-shujutongji," +
				"icons-resource-smses-send,icons-resource-tixian,icons-resource-tiyanbiao,icons-resource-tongji,icons-resource-tongji1,icons-resource-tongjibaobiao," +
				"icons-resource-tongjixinxi,icons-resource-wangzhan,icons-resource-wangzhan1,icons-resource-wodejiekuan,icons-resource-zhanghuyue," +
				"icons-resource-zhongjiangjilu,icons-resource-zhuanzhang,icons-resource-zhuanzhang1,icons-resource-zhuanzhang2,icons-resource-zidonghuankuan," +
				"icons-resource-basket,icons-resource-bell,icons-resource-book,icons-resource-book-open,icons-resource-box,icons-resource-bricks,icons-resource-building," +
				"icons-resource-cake,icons-resource-camera,icons-resource-cart,icons-resource-chart-bar,icons-resource-chart-curve,icons-resource-chart-organisation," +
				"icons-resource-chart-pie,icons-resource-clock,icons-resource-cog,icons-resource-coins,icons-resource-color-swatch,icons-resource-compress," +
				"icons-resource-computer,icons-resource-computer-key,icons-resource-connect,icons-resource-control-play-blue,icons-resource-control-stop-blue," +
				"icons-resource-cup,icons-resource-database-key,icons-resource-disconnect,icons-resource-disk,icons-resource-drink,icons-resource-drive," +
				"icons-resource-drive-web,icons-resource-eye,icons-resource-feed,icons-resource-folder,icons-resource-folder-add,icons-resource-folder-explore," +
				"icons-resource-folder-magnify,icons-resource-font,icons-resource-group,icons-resource-heart,icons-resource-help,icons-resource-hourglass," +
				"icons-resource-house,icons-resource-ipod,icons-resource-lightbulb,icons-resource-lock,icons-resource-lorry,icons-resource-money," +
				"icons-resource-money-yen,icons-resource-monitor,icons-resource-music,icons-resource-note,icons-resource-palette,icons-resource-shape-align-bottom," +
				"icons-resource-shape-rotate-anticlockwise,icons-resource-shape-square,icons-resource-shield,icons-resource-sitemap-color,icons-resource-text-dropcaps," +
				"icons-resource-time,icons-resource-transmit,icons-resource-wrench-orange,icons-resource-zoom,icons-resource-huoqilicai";
		icons.addAll(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(iconsStr));
	}
}
