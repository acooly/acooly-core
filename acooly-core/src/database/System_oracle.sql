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
'�˵���';

COMMENT ON COLUMN SYS_MENU.NAME IS
'�˵�����';

COMMENT ON COLUMN SYS_MENU.DESCN IS
'����';

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
'ϵͳ��Դ��';

COMMENT ON COLUMN SYS_RESOURCE.ID IS
'����';

COMMENT ON COLUMN SYS_RESOURCE.NAME IS
'��Դ����';

COMMENT ON COLUMN SYS_RESOURCE.MENUID IS
'�˵�����';

COMMENT ON COLUMN SYS_RESOURCE.RESTYPE IS
'��Դ����
FUNC
URL';

COMMENT ON COLUMN SYS_RESOURCE.RES_STRING IS
'��Դֵ';

COMMENT ON COLUMN SYS_RESOURCE.DESCN IS
'����';

INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('����ϵͳ��̨��ҳ', 'URL', '/admin/index.jsp', NULL, '����ϵͳ��̨');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('�ʺŹ���', 'URL', '/admin/acegi/user.mvc?method=index', 1, '../images/icon/16x16/role.gif');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('��־����', 'URL', '/admin/log/log.mvc?method=index', 1, '../images/icon/16x16/user.jpg');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('��ɫ����', 'URL', '/admin/acegi/role.mvc?method=index', 1, '../images/icon/16x16/resource');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('�˵�����', 'URL', '/admin/acegi/menu.mvc?method=index', 1, '../images/icon/16x16/resource');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('��Դ����', 'URL', '/admin/acegi/resource.mvc?method=index', 1, '../images/icon/16x16/user.jpg');
INSERT INTO SYS_RESOURCE (NAME, RESTYPE, RES_STRING, MENUID, DESCN)
VALUES ('ϵͳ����', 'URL', '/admin/platframe/systemParameter.mvc?method=index', 2, NULL);

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
'����Ա��';

COMMENT ON COLUMN SYS_USER.LOGINID IS
'��¼�û���';

COMMENT ON COLUMN SYS_USER.NAME IS
'����Ա����';

COMMENT ON COLUMN SYS_USER.PASSWD IS
'��¼����';

COMMENT ON COLUMN SYS_USER.USERTYPE IS
'�û�����
1:����Ա
2:����Ա
';

COMMENT ON COLUMN SYS_USER.EMAIL IS
'�����ʼ�';

COMMENT ON COLUMN SYS_USER.STATUS IS
'״̬
1:��Ч
0:��Ч';

COMMENT ON COLUMN SYS_USER.DESCN IS
'����';

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

