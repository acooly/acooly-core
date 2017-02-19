INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('20170219', NULL, '文件管理', 'MENU', '0', '2017-02-19 18:32:05', '', '1', 'icons-resource-caozuorizhichaxun', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('201702191', '20170219', '文件管理', 'URL', '0', '2017-02-19 18:32:05', '/manage/module/ofile/onlineFile/index.html', '1', 'icons-resource-iconfontcolor28', NULL);


INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '20170219');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '201702191');