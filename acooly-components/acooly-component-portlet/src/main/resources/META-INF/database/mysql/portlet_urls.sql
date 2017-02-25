INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('1702261', NULL, '客户反馈', 'MENU', '0', '2015-09-28 18:59:46', '', '1', 'icons-resource-app', NULL);
INSERT INTO `sys_resource` (`ID`, `PARENTID`, `NAME`, `TYPE`, `SHOW_STATE`, `ORDER_TIME`, `VALUE`, `SHOW_MODE`, `ICON`, `DESCN`) VALUES ('17022611', '1702261', '客户反馈', 'URL', '0', '2015-10-01 03:10:00', '/manage/module/portlet/feedback/index.html', '1', 'icons-resource-fangdaozhuomian', NULL);


INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '1702261');
INSERT INTO `sys_role_resc` (`ROLE_ID`, `RESC_ID`) VALUES ('1', '17022611');
