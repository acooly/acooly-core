package com.acooly.module.jpa;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.springframework.jdbc.datasource.init.ScriptUtils.*;

/**
 * @author qiubo@yiji.com
 */
@Slf4j
public class ExHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter {
	@Override
	public void postProcessEntityManagerFactory(EntityManagerFactory emf) {
		super.postProcessEntityManagerFactory(emf);
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		Session session = entityManager.unwrap(Session.class);
		session.doWork(connection -> {
			String jdbcUrl = connection.getMetaData().getURL();
			String dbType;
			if (StringUtils.contains(jdbcUrl, ":mysql:")) {
				dbType = "mysql";
			} else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
				dbType = "oracle";
			} else {
				throw new UnsupportedOperationException("不支持此数据库");
			}
			String ddl = "classpath:META-INF/database/" + dbType + "/" + "ddl.sql";
            String dml = "classpath:META-INF/database/" + dbType + "/" + "dml.sql";
            List<String> sqls = Lists.newArrayList(ddl, dml);
			sqls.forEach(s -> {
				Resource scriptResource = ApplicationContextHolder.get().getResource(s);
				if (scriptResource.exists()) {
					log.info("执行自定义数据库脚本文件:{}", s);
					EncodedResource encodedResource = new EncodedResource(scriptResource, Charsets.UTF_8);
					ScriptUtils.executeSqlScript(connection, encodedResource, true, true, DEFAULT_COMMENT_PREFIX,
						DEFAULT_STATEMENT_SEPARATOR, DEFAULT_BLOCK_COMMENT_START_DELIMITER,
						DEFAULT_BLOCK_COMMENT_END_DELIMITER);
				}
			});
		});
		entityManager.getTransaction().commit();
		session.close();
	}
}