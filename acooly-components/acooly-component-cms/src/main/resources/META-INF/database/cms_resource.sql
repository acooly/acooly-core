INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('20151025', NULL, '内容管理', 'MENU', '0', '2015-10-23 18:32:05', '', '1', 'icons-resource-caozuorizhichaxun', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510251', '20151025', '类型管理', 'URL', '0', '2015-10-25 01:14:31', '/manage/module/feature/cms/contentType/index.html', '1', 'icons-resource-iconfontcolor28', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510252', '20151025', '服务协议', 'URL', '0', '2015-10-25 05:14:39', '/manage/module/feature/cms/content/index.html?code=agreement', '1', 'icons-resource-hezuo', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510253', '20151025', '关于我们', 'URL', '0', '2015-10-25 07:14:41', '/manage/module/feature/cms/content/index.html?code=aboutus', '1', 'icons-resource-guanyuwomen1', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510254', '20151025', '联系我们', 'URL', '0', '2015-10-25 06:14:41', '/manage/module/feature/cms/content/index.html?code=contactus', '1', 'icons-resource-6', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510255', '20151025', '常见问题', 'URL', '0', '2015-10-25 04:14:41', '/manage/module/feature/cms/content/index.html?code=faq', '1', 'icons-resource-changjianwenti', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510256', '20151025', '新闻动态', 'URL', '0', '2015-10-25 03:14:40', '/manage/module/feature/cms/content/index.html?code=news', '1', 'icons-resource-anli1', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201510257', '20151025', '最新公告', 'URL', '0', '2015-10-25 02:14:40', '/manage/module/feature/cms/content/index.html?code=notice', '1', 'icons-resource-tiyanbiao', NULL);

INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '20151025');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510251');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510252');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510253');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510254');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510255');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510256');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201510257');
