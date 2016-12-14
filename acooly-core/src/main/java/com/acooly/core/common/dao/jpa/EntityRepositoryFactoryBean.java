package com.acooly.core.common.dao.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

/**
 * 自定义 实体仓库工厂实现
 * 
 * @author zhangpu
 * @date 2012年6月30日
 * @param <R>
 * @param <T>
 * @param <I>
 */
public class EntityRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends
		JpaRepositoryFactoryBean<R, T, I> {

	@SuppressWarnings("rawtypes")
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new EntityRepositoryFactory(entityManager);
	}

	private static class EntityRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
		private EntityManager entityManager;

		public EntityRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@SuppressWarnings("unchecked")
		protected Object getTargetRepository(RepositoryMetadata metadata) {
			return new AbstractEntityJpaDao<T, I>((Class<T>) metadata.getDomainType(), entityManager);
		}

		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return AbstractEntityJpaDao.class;
		}
	}

}
