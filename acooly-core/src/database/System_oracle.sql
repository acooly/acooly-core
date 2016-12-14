/*==============================================================*/
/* DBMS name:      ORACLE Version 10gR2                         */
/* Created on:     2012/11/22 22:20:15                          */
/*==============================================================*/


ALTER TABLE SYS_RESOURCE
   DROP CONSTRAINT FK_SYS_RESOURCE_SYS_MENU;

ALTER TABLE SYS_ROLE_RESC
   DROP CONSTRAINT FK_SYS_ROLE_RESC_SYS_RESOURCE;

ALTER TABLE SYS_ROLE_RESC
   DROP CONSTRAINT FK_SYS_ROLE_RESC_SYS_ROLE;

ALTER TABLE SYS_USER_ROLE
   DROP CONSTRAINT FK_SYS_USER_ROLE_SYS_ROLE;

ALTER TABLE SYS_USER_ROLE
   DROP CONSTRAINT FK_SYS_USER_ROLE_SYS_USER;

DROP TABLE SYS_MENU CASCADE CONSTRAINTS;

DROP TABLE SYS_RESOURCE CASCADE CONSTRAINTS;

DROP TABLE SYS_ROLE CASCADE CONSTRAINTS;

DROP TABLE SYS_ROLE_RESC CASCADE CONSTRAINTS;

DROP TABLE SYS_USER CASCADE CONSTRAINTS;

DROP INDEX "_WA_SYS_USER_ID_1ED998B2";

DROP TABLE SYS_USER_ROLE CASCADE CONSTRAINTS;

DROP SEQUENCE SEQ_SYS_MENU;

DROP SEQUENCE SEQ_SYS_RESOURCE;

DROP SEQUENCE SEQ_SYS_ROLE;

DROP SEQUENCE SEQ_SYS_USER;

CREATE SEQUENCE SEQ_SYS_MENU
INCREMENT BY 1
START WITH 1
 MAXVALUE 9999999999
 MINVALUE 1
CYCLE
 CACHE 20;

CREATE SEQUENCE SEQ_SYS_RESOURCE
INCREMENT BY 1
START WITH 1
 MAXVALUE 9999999999
 MINVALUE 1
CYCLE
 CACHE 20;

CREATE SEQUENCE SEQ_SYS_ROLE
INCREMENT BY 1
START WITH 1
 MAXVALUE 9999999999
 MINVALUE 1
CYCLE
 CACHE 20;

CREATE SEQUENCE SEQ_SYS_USER
INCREMENT BY 1
START WITH 1
 MAXVALUE 9999999999
 MINVALUE 1
CYCLE
 CACHE 20;

/*==============================================================*/
/* Table: SYS_MENU                                              */
/*==============================================================*/
CREATE TABLE SYS_MENU  (
   ID                   NUMBER(10)                      NOT NULL,
   NAME                 VARCHAR2(64)                    NOT NULL,
   DESCN                VARCHAR2(256),
   CONSTRAINT PK_SYS_MENU PRIMARY KEY (ID)

);

COMMENT ON TABLE SYS_MENU IS
'菜单表';

COMMENT ON COLUMN SYS_MENU.NAME IS
'菜单名称';

COMMENT ON COLUMN SYS_MENU.DESCN IS
'描述';

/*==============================================================*/
/* Table: SYS_RESOURCE                                          */
/*==============================================================*/
CREATE TABLE SYS_RESOURCE  (
   ID                   NUMBER(10)                      NOT NULL,
   NAME                 VARCHAR2(64),
   MENUID               NUMBER(10),
   RESTYPE              VARCHAR2(64),
   RES_STRING           VARCHAR2(128)                   NOT NULL,
   DESCN                VARCHAR2(256),
   CONSTRAINT PK_SYS_RESOURCE PRIMARY KEY (ID)
);

COMMENT ON TABLE SYS_RESOURCE IS
'系统资源表';

COMMENT ON COLUMN SYS_RESOURCE.ID IS
'主键';

COMMENT ON COLUMN SYS_RESOURCE.NAME IS
'资源名称';

COMMENT ON COLUMN SYS_RESOURCE.MENUID IS
'菜单主键';

COMMENT ON COLUMN SYS_RESOURCE.RESTYPE IS
'资源类型
FUNC
URL';

COMMENT ON COLUMN SYS_RESOURCE.RES_STRING IS
'资源值';

COMMENT ON COLUMN SYS_RESOURCE.DESCN IS
'描述';

INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('管理系统后台首页', 'URL', '/admin/index.jsp', NULL, '管理系统后台');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('帐号管理', 'URL', '/admin/acegi/user.mvc?method=index', 1, '../images/icon/16x16/role.gif');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('日志管理', 'URL', '/admin/log/log.mvc?method=index', 1, '../images/icon/16x16/user.jpg');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('角色管理', 'URL', '/admin/acegi/role.mvc?method=index', 1, '../images/icon/16x16/resource');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('菜单管理', 'URL', '/admin/acegi/menu.mvc?method=index', 1, '../images/icon/16x16/resource');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('资源管理', 'URL', '/admin/acegi/resource.mvc?method=index', 1, '../images/icon/16x16/user.jpg');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('系统参数', 'URL', '/admin/platframe/systemParameter.mvc?method=index', 2, NULL);

/*==============================================================*/
/* Table: SYS_ROLE                                              */
/*==============================================================*/
CREATE TABLE SYS_ROLE  (
   ID                   NUMBER(10)                      NOT NULL,
   NAME                 VARCHAR(64)                     NOT NULL,
   DESCN                VARCHAR(256),
   CONSTRAINT PK_SYS_ROLE PRIMARY KEY (ID)
);

/*==============================================================*/
/* Table: SYS_ROLE_RESC                                         */
/*==============================================================*/
CREATE TABLE SYS_ROLE_RESC  (
   ROLE_ID              NUMBER(10)                      NOT NULL,
   RESC_ID              NUMBER(10)                      NOT NULL,
   CONSTRAINT PK_SYS_ROLE_RESC PRIMARY KEY (ROLE_ID, RESC_ID)
);

INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 1);
INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 2);
INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 3);
INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 4);
INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 5);
INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 6);
INSERT INTO SYS_ROLE_RESC (ROLE_ID, RESC_ID) VALUES (1, 7);

/*==============================================================*/
/* Table: SYS_USER                                              */
/*==============================================================*/
CREATE TABLE SYS_USER  (
   ID                   NUMBER(10)                      NOT NULL,
   LOGINID              VARCHAR2(16)                    NOT NULL,
   NAME                 VARCHAR2(32),
   PASSWD               VARCHAR2(128)                   NOT NULL,
   USERTYPE             VARCHAR2(1)                     NOT NULL,
   EMAIL                VARCHAR2(64),
   STATUS               VARCHAR2(1)                     NOT NULL,
   DESCN                VARCHAR2(256),
   CONSTRAINT PK_SYS_USER PRIMARY KEY (ID)
);

COMMENT ON TABLE SYS_USER IS
'操作员表';

COMMENT ON COLUMN SYS_USER.LOGINID IS
'登录用户名';

COMMENT ON COLUMN SYS_USER.NAME IS
'操作员姓名';

COMMENT ON COLUMN SYS_USER.PASSWD IS
'登录密码';

COMMENT ON COLUMN SYS_USER.USERTYPE IS
'用户类型
1:管理员
2:操作员
';

COMMENT ON COLUMN SYS_USER.EMAIL IS
'电子邮件';

COMMENT ON COLUMN SYS_USER.STATUS IS
'状态
1:有效
0:无效';

COMMENT ON COLUMN SYS_USER.DESCN IS
'描述';

/*==============================================================*/
/* Table: SYS_USER_ROLE                                         */
/*==============================================================*/
CREATE TABLE SYS_USER_ROLE  (
   ROLE_ID              NUMBER(10)                      NOT NULL,
   USER_ID              NUMBER(10)                      NOT NULL,
   CONSTRAINT PK_SYS_USER_ROLE PRIMARY KEY (ROLE_ID, USER_ID)
);

/*==============================================================*/
/* Index: "_WA_SYS_USER_ID_1ED998B2"                            */
/*==============================================================*/
CREATE INDEX "_WA_SYS_USER_ID_1ED998B2" ON SYS_USER_ROLE (
   USER_ID ASC
)
ON PRIMARY;

ALTER TABLE SYS_RESOURCE
   ADD CONSTRAINT FK_SYS_RESOURCE_SYS_MENU FOREIGN KEY (MENUID)
      REFERENCES SYS_MENU (ID);

ALTER TABLE SYS_ROLE_RESC
   ADD CONSTRAINT FK_SYS_ROLE_RESC_SYS_RESOURCE FOREIGN KEY (RESC_ID)
      REFERENCES SYS_RESOURCE (ID);

ALTER TABLE SYS_ROLE_RESC
   ADD CONSTRAINT FK_SYS_ROLE_RESC_SYS_ROLE FOREIGN KEY (ROLE_ID)
      REFERENCES SYS_ROLE (ID);

ALTER TABLE SYS_USER_ROLE
   ADD CONSTRAINT FK_SYS_USER_ROLE_SYS_ROLE FOREIGN KEY (ROLE_ID)
      REFERENCES SYS_ROLE (ID);

ALTER TABLE SYS_USER_ROLE
   ADD CONSTRAINT FK_SYS_USER_ROLE_SYS_USER FOREIGN KEY (USER_ID)
      REFERENCES SYS_USER (ID);

