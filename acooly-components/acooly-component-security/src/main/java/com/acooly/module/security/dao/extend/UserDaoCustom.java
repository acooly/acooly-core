package com.acooly.module.security.dao.extend;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.module.security.dto.UserDto;

import java.util.Map;

public interface UserDaoCustom {

    PageInfo<UserDto> queryDto(PageInfo<UserDto> pageInfo, Map<String, Object> map, Map<String, Boolean> orderMap);

}
