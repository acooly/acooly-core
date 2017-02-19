
package com.acooly.module.cms.service.impl;

import com.acooly.core.common.dao.support.PageInfo;
import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.service.EntityServiceImpl;
import com.acooly.core.utils.Collections3;
import com.acooly.core.utils.Dates;
import com.acooly.module.cms.dao.ContentDao;
import com.acooly.module.cms.domain.Content;
import com.acooly.module.cms.service.AttachmentService;
import com.acooly.module.cms.service.ContentBodyService;
import com.acooly.module.cms.service.ContentService;
import com.acooly.module.ds.PagedJdbcTemplate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

@Service("contentService")
public class ContentServiceImpl extends EntityServiceImpl<Content, ContentDao>
	implements ContentService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ContentBodyService contentBodyService;
	@Autowired
	private AttachmentService attachmentService;

	@Override
	public Content getLatestByTypeCode(String typeCode) {

		return getLatestByTypeCode(typeCode, null);
	}

	@Override
	public Content getLatestByTypeCode(String typeCode, String carrierCode) {

		PageInfo<Content> pageInfo = new PageInfo<Content>();
		pageInfo.setCountOfCurrentPage(1);
		Map<String, Boolean> sortMap = new HashMap<String, Boolean>();
		sortMap.put("pubDate", false);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("EQ_status", Content.STATUS_ENABLED);
		paramMap.put("EQ_contentType.code", typeCode);
		if (StringUtils.isNotBlank(carrierCode)) {
			paramMap.put("EQ_carrierCode", carrierCode);
		}
		// 是否单独显示业务
		pageInfo = query(pageInfo, paramMap, sortMap);
		if (pageInfo == null || Collections3.isEmpty(pageInfo.getPageResults())) {
			return null;
		}
		return Collections3.getFirst(pageInfo.getPageResults());
	}

	public void updateStatusBatch(Integer status, Serializable... ids) {

		String updateSql =
			"update cms_content set status = " + status + " where 1=1 ";
		Iterator<Serializable> iterator = Lists.newArrayList(ids).iterator();
		String whereStr = "and id in(";
		int i = 0;
		while (iterator.hasNext()) {
			whereStr += iterator.next() + ",";
			i++;
		}
		whereStr = whereStr.substring(0, whereStr.length() - 1);
		whereStr += ")";
		updateSql += whereStr;
		if (i != 0) {
			jdbcTemplate.update(updateSql);
		}

	}

	public PageInfo<Content> search(
            PageInfo<Content> pageInfo, String q, int status) {

		String sql =
			"select c.*,b.body body_ from cms_content c " +
				"left join cms_content_body b on c.id = b.id " +
				"left join cms_content_type t on t.id = c.type " +
				"where c.status=" + status + " and t.code = '1006' " +
				" and (c.keywords like '%" + q + "%' or c.title like '%" + q +
				"%') ";
		return this.queryMySql(pageInfo, sql);
	}

	private PageInfo<Content> queryMySql(PageInfo<Content> pageInfo, String sql) {

		try {
			String sqlFrom = sql.substring(sql.indexOf("from"));
			String sqlCount = "select count(*) " + sqlFrom;
			long totalCount = 0; //pagedJdbcTemplate.queryForLong(sqlCount);
			int startNum =
				pageInfo.getCountOfCurrentPage() *
					(pageInfo.getCurrentPage() - 1);
			int endNum =
				pageInfo.getCountOfCurrentPage() * pageInfo.getCurrentPage();
			if (endNum > totalCount) {
				endNum = (int) totalCount;
			}
			long totalPage =
				(totalCount % pageInfo.getCountOfCurrentPage() == 0L)
					? totalCount / pageInfo.getCountOfCurrentPage()
					: totalCount / pageInfo.getCountOfCurrentPage() + 1L;

			String pageSql =
				sql + "limit " + startNum + "," +
					pageInfo.getCountOfCurrentPage();
			List result =
					jdbcTemplate.query(
					pageSql, BeanPropertyRowMapper.newInstance(Content.class));
			pageInfo.setPageResults(result);
			pageInfo.setTotalCount(totalCount);
			pageInfo.setTotalPage(totalPage);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return pageInfo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delects(Serializable... ids) {

		/** 需要改动 */
		Iterator<Serializable> iterator = Lists.newArrayList(ids).iterator();
		Content content = new Content();
		while (iterator.hasNext()) {
			content = super.get(iterator.next());
			if (content != null) {
				super.remove(content);
			}
		}
		content = null;

	}

	public void moveUp(Long id) {

		try {
			Content content = get(id);
			String code = content.getContentType().getCode();

			Date current = content.getPubDate();
			Content perv = null;

			long count = getEntityDao().getCountByGtPubDate(current);

			if (count > 0) {
				PageInfo<Content> pageInfo =
					new PageInfo<Content>(1, (int) count);
				Map<String, Boolean> sortMap = Maps.newLinkedHashMap();
				sortMap.put("pubDate", false);
				Map<String, Object> searchMap = Maps.newHashMap();
				searchMap.put("EQ_contentType.code", code);
				searchMap.put(
					"GT_pubDate",
					Dates.format(current, Dates.CHINESE_DATETIME_FORMAT_LINE));
				pageInfo = getEntityDao().query(pageInfo, searchMap, sortMap);
				if (pageInfo.getPageResults() != null &&
					pageInfo.getPageResults().size() > 0) {
					perv = pageInfo.getPageResults().get(0);
				}
			}

			if (perv != null) {
				content.setPubDate(perv.getPubDate());
				perv.setPubDate(current);
				save(perv);
			}
			else {
				content.setPubDate(new Date());
			}
			save(content);
		}
		catch (Exception e) {
			throw new BusinessException("上移失败", e);
		}

	}

	public void moveTop(Long id) {

		try {
			Content content = get(id);
			content.setPubDate(new Date());
			save(content);
		}
		catch (Exception e) {
			throw new BusinessException("置顶失败", e);
		}
	}

	@Override
	public Content getByKeycode(String keycode) {

		return getEntityDao().findByKeycode(keycode);
	}

}