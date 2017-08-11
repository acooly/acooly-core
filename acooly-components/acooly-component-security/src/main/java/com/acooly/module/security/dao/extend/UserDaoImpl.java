package com.acooly.module.security.dao.extend;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.Strings;
import com.acooly.module.ds.AbstractJdbcTemplateDao;
import com.acooly.module.security.dto.UserDto;

import java.util.Map;

/**
 * JPA方案中，对特殊需求的DAO的扩展实现DEMO
 * <p>
 * <p>要求： <br>
 * 1.另外定义接口UserDaoCustom <br>
 * 2.实现新定义的扩展接口，但名次必须命名为JAP接口名+Impl后缀
 *
 * @author zhangpu
 */
public class UserDaoImpl extends AbstractJdbcTemplateDao implements UserDaoCustom {
    @Override
    public PageInfo<UserDto> queryDto(PageInfo<UserDto> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap) {
        String sql = "select t1.id,t1.USERNAME as userName,t1.REAL_NAME as realName,t1.USER_TYPE as userType,\n" +
                "t1.EMAIL as email,t1.MOBILE_NO as mobileNo,t1.CREATE_TIME as createTime,t1.LAST_MODIFY_TIME as lastModifyTime,\n" +
                "t1.UNLOCK_TIME as unlockTime,t3.id as roleId,t3.NAME as roleName,t3.DESCN as roleDescn,\n" +
                "t1.STATUS as status,t1.ORG_ID as orgId,t1.ORG_NAME as orgName\n" +
                "from sys_user t1, sys_user_role t2,sys_role t3 where t1.id = t2.user_id and t2.role_id = t3.id ";

        String username = (String) map.get("LIKE_username");
        if (Strings.isNotBlank(username)) {
            sql += " and t1.USERNAME like '%" + username + "%'";
        }

        String realName = (String) map.get("LIKE_realName");
        if (Strings.isNotBlank(realName)) {
            sql += " and t1.REAL_NAME like '%" + realName + "%'";
        }

        String status = (String) map.get("EQ_status");
        if (Strings.isNotBlank(status)) {
            sql += " and t1.STATUS = " + status;
        }
        String userType = (String) map.get("EQ_userType");
        if (Strings.isNotBlank(userType)) {
            sql += " and t1.USER_TYPE = " + userType;
        }

        String roleId = (String) map.get("EQ_role");
        if (Strings.isNotBlank(roleId)) {
            sql += " and t3.id = " + roleId;
        }

        String orgId = (String) map.get("EQ_orgId");
        if (Strings.isNotBlank(orgId)) {
            sql += " and t1.orgId = " + orgId;
        }

        sql += " order by t1.id desc";

        return super.query(pageInfo, sql, UserDto.class);
    }


}
