/**
 * acooly-core-parent
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author zhangpu
 * @date 2021-03-29 00:33
 */
package com.acooly.core.test.common;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.enums.Gender;
import com.acooly.core.utils.Dates;
import com.acooly.core.utils.Money;
import com.acooly.core.utils.enums.SimpleStatus;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangpu
 * @date 2021-03-29 00:33
 */
@Slf4j
public class PageInfoTest {


    @Test
    public void testToWithBiFunction() {
        PageInfo<AppEntity> pageInfo = buildEntityPageInfo();
        log.info("entity pageInfo: {}", pageInfo);

        PageInfo<AppEntity> entityPageInfo = pageInfo.to(AppEntity.class);


        PageInfo<AppDto> dtoPageInfo = pageInfo.to(AppDto.class, (AppEntity entity, AppDto dto) -> {
            dto.setGenderText(dto.getGender().message());
            dto.setStatusText(dto.getStatus().message());
            return dto;
        });
        log.info("dto pageInfo:{}", dtoPageInfo);
    }

    protected AppEntity buildEntity() {
        AppEntity entity = new AppEntity();
        entity.setUsername("zhangpu");
        entity.setName("张飞");
        entity.setAge(39);
        entity.setBirthday(Dates.parse("1982-09-01"));
        entity.setGender(Gender.male);
        entity.setSalary(Money.amout("10000"));
        entity.setStatus(SimpleStatus.enable);
        return entity;
    }

    protected PageInfo<AppEntity> buildEntityPageInfo() {
        PageInfo<AppEntity> pageInfo = new PageInfo<>();
        List<AppEntity> appEntities = Lists.newArrayList();
        for (int i = 1; i <= 2; i++) {
            AppEntity entity = new AppEntity();
            entity.setUsername("zhangpu" + i);
            entity.setName("张飞" + i);
            entity.setAge(39);
            entity.setBirthday(Dates.parse("1982-09-01"));
            entity.setGender(i % 2 == 0 ? Gender.male : Gender.female);
            entity.setSalary(Money.amout("10000"));
            entity.setStatus(i % 2 == 0 ? SimpleStatus.enable : SimpleStatus.disable);
            appEntities.add(entity);
        }
        pageInfo.setPageResults(appEntities);
        return pageInfo;
    }


}
