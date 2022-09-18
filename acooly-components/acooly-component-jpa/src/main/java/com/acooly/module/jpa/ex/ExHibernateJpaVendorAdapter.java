package com.acooly.module.jpa.ex;

import com.acooly.core.common.boot.ApplicationContextHolder;
import com.acooly.core.utils.Strings;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.springframework.jdbc.datasource.init.ScriptUtils.*;

/**
 * 扩展支持启动时执行初始化SQL
 *
 * @author qiubo@yiji.com
 * @author zhangpu
 */
@Slf4j
public class ExHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter {
    @Override
    public void postProcessEntityManagerFactory(EntityManagerFactory emf) {
        super.postProcessEntityManagerFactory(emf);
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Session session = entityManager.unwrap(Session.class);
        Database database = getDatabase();
        session.doWork(
                connection -> {
                    String dbType = Strings.lowerCase(database.name());
                    exeScripts(connection, dbType, Lists.newArrayList(
                            "classpath:META-INF/database/" + dbType + "/" + "ddl.sql",
                            "classpath:META-INF/database/" + dbType + "/" + "ddl_*.sql"));

                    exeScripts(connection, dbType, Lists.newArrayList(
                            "classpath:META-INF/database/" + dbType + "/" + "dml.sql",
                            "classpath:META-INF/database/" + dbType + "/" + "dml_*.sql"));
                });
        entityManager.getTransaction().commit();
        session.close();
    }

    private void exeScripts(Connection connection, String dbType, List<String> scripts) {
        scripts.forEach(
                s -> {
                    Resource[] scriptResources;
                    try {
                        scriptResources = ApplicationContextHolder.get().getResources(s);
                    } catch (IOException e) {
                        return;
                    }
                    Arrays.stream(scriptResources)
                            .filter(Resource::exists)
                            .sorted(Comparator.comparing(Resource::getFilename))
                            .forEach(
                                    resource -> {
                                        log.info("执行初始化数据库脚本文件:META-INF/database/{}/{}", dbType, resource.getFilename());
                                        EncodedResource encodedResource = new EncodedResource(resource, Charsets.UTF_8);
                                        ScriptUtils.executeSqlScript(
                                                connection,
                                                encodedResource,
                                                true,
                                                true,
                                                DEFAULT_COMMENT_PREFIX,
                                                DEFAULT_STATEMENT_SEPARATOR,
                                                DEFAULT_BLOCK_COMMENT_START_DELIMITER,
                                                DEFAULT_BLOCK_COMMENT_END_DELIMITER);
                                    });
                });
    }
}
