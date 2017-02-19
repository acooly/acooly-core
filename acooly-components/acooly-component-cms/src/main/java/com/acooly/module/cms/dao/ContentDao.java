
package com.acooly.module.cms.dao;

import com.acooly.module.cms.domain.Content;
import com.acooly.module.jpa.EntityJpaDao;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * 内容主表 JPA Dao Date: 2013-07-12 15:06:46
 *
 * @author Acooly Code Generator
 */
public interface ContentDao extends EntityJpaDao<Content, Long> {

    @Query("select count(*) from Content where pubDate > ?1")
    long getCountByGtPubDate(Date pubDate);

    Content findByKeycode(String keycode);
}
