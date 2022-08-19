package com.acooly.module.jpa.usertype;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.usertype.UserType;

/**
 * @author qiubo
 */
@Slf4j
public class CustomUserTypesIntegrator implements Integrator {
    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory,
                          SessionFactoryServiceRegistry serviceRegistry) {
        final MetadataImplementor mi;
        if (metadata instanceof MetadataImplementor) {
            mi = (MetadataImplementor) metadata;
        } else {
            throw new IllegalArgumentException(
                    "Metadata was not assignable to MetadataImplementor: " + metadata.getClass());
        }
        registerType(mi, new MoneyUserType());
    }

    @SuppressWarnings("deprecation")
	private void registerType(MetadataImplementor mi, UserType type) {
        log.info("自动注册jpa usertype:{}", type.getClass().getName());
        String className = type.returnedClass().getName();
        mi.getTypeResolver().registerTypeOverride(type, new String[]{className});
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {

    }
}
