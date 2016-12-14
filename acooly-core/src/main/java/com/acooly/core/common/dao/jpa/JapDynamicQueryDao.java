package com.acooly.core.common.dao.jpa;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.Assert;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.utils.GenericsUtils;

/**
 * Jap动态查询抽象实现
 * 
 * @author zhangpu
 * @param <T>
 */
public abstract class JapDynamicQueryDao<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	/** 所管理的Entity类型. */
	protected Class<T> entityClass;

	/**
	 * SQL方式通用分页查询，支持HQL
	 * 
	 * @param pageInfo
	 * @param ql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected PageInfo<T> pagedQueryByJpql(PageInfo<T> pageInfo, String ql, Object... args) {
		long totalCount = queryCount(false, ql, args);
		pageInfo.setTotalCount(totalCount);

		Query query = entityManager.createQuery(ql);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		query.setMaxResults(pageInfo.getCountOfCurrentPage());
		query.setFirstResult(pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1));
		pageInfo.setPageResults(query.getResultList());
		return pageInfo;
	}

	/**
	 * 原生SQL分页查询，默认使用子类泛型指定的类型作为返回对象类型
	 * 
	 * @param pageInfo
	 * @param sql
	 * @param args
	 * @return
	 */
	protected PageInfo<T> pagedQueryByNativeSql(PageInfo<T> pageInfo, String sql, Object... args) {
		return queryByNativeSql(getEntityClass(), pageInfo, sql, args);
	}

	/**
	 * 原生SQL分页查询，注意：只支持简单单层DTO模式的自动类型映射
	 * 
	 * @param resultClass
	 * @param pageInfo
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected PageInfo<T> queryByNativeSql(Class<T> resultClass, PageInfo<T> pageInfo, String sql, Object... args) {
		long totalCount = queryNativeCount(sql, args);
		pageInfo.setTotalCount(totalCount);
		Query query = entityManager.createNativeQuery(sql, resultClass);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		query.setMaxResults(pageInfo.getCountOfCurrentPage());
		query.setFirstResult(pageInfo.getCountOfCurrentPage() * (pageInfo.getCurrentPage() - 1));
		pageInfo.setPageResults(query.getResultList());
		return pageInfo;
	}
	
	protected long queryNativeCount(String sql,Object... args){
		String countQueryString = "select count(*) from (" + sql + ") as xtb";
		Query query = entityManager.createNativeQuery(countQueryString, Long.class);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return (Long) query.getSingleResult();
	}

	/**
	 * JPQL查询
	 * 
	 * @param jpql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> queryByJpql(String jpql, Object... args) {
		Query query = entityManager.createQuery(jpql);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query.getResultList();
	}

	/**
	 * 原生SQL查询
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected List<T> queryByNativeSql(Class<T> resultClass, String sql, Object... args) {
		Query query = entityManager.createNativeQuery(sql, resultClass);
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query.getResultList();
	}

	/**
	 * 取得entityClass的函数
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		entityClass = GenericsUtils.getSuperClassGenricType(getClass());
		return entityClass;
	}

	/**
	 * 通过原始QL转换后查询Count
	 * 
	 * @param nativeSql
	 * @param ql
	 * @param args
	 * @return
	 */
	private long queryCount(boolean nativeSql, String ql, Object... args) {
		String countQueryString = "select count (*) " + removeSelect(removeOrders(ql));
		Query query = null;
		if (nativeSql) {
			query = entityManager.createNativeQuery(countQueryString, Long.class);
		} else {
			query = entityManager.createQuery(countQueryString);
		}
		if (args != null && args.length != 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return (Long) query.getSingleResult();
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 */
	private static String removeSelect(String hql) {
		Assert.hasText(hql);
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除orderby 子句
	 */
	private static String removeOrders(String hql) {
		Assert.hasText(hql);
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
