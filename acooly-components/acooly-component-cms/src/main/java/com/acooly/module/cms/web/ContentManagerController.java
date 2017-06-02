
package com.acooly.module.cms.web;

import com.acooly.core.common.exception.BusinessException;
import com.acooly.core.common.web.AbstractJQueryEntityController;
import com.acooly.core.common.web.support.JsonResult;
import com.acooly.core.utils.Servlets;
import com.acooly.module.cms.domain.*;
import com.acooly.module.cms.service.AttachmentService;
import com.acooly.module.cms.service.CmsCodeService;
import com.acooly.module.cms.service.ContentService;
import com.acooly.module.cms.service.ContentTypeService;
import com.acooly.module.ofile.OFileProperties;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping(value = "/manage/module/cms/content")
public class ContentManagerController
    extends AbstractJQueryEntityController<Content, ContentService> {

  public static Map<Integer, String> allStatuss = Maps.newTreeMap();

  static {
    allStatuss.put(Content.STATUS_ENABLED, "正常");
    allStatuss.put(Content.STATUS_DISABLED, "禁用");
  }

  @Autowired private ContentService contentService;
  @Autowired private ContentTypeService contentTypeService;
  @Autowired private AttachmentService attachmentService;
  @Autowired private OFileProperties oFileProperties;
  @Autowired private CmsCodeService cmsCodeService;

  @RequestMapping(value = "removeAttachment")
  @ResponseBody
  public JsonResult removeAttachment(HttpServletRequest request, HttpServletResponse response) {

    JsonResult result = new JsonResult();
    try {
      String filePath = getFileStorageRoot();
      String id = request.getParameter("id");
      if (StringUtils.isNotBlank(id)) {
        Attachment a = attachmentService.get(Long.valueOf(id));
        if (a != null) {
          filePath += StringUtils.substringAfter(a.getFilePath(), "/media");
          File f = new File(filePath);
          if (f.exists()) {
            f.delete();
          }
        }
        attachmentService.remove(a);
      }
      result.setMessage("删除成功");
    } catch (Exception e) {
      e.printStackTrace();
      handleException(result, "删除", e);
    }
    return result;
  }

  @Override
  protected Content onSave(
      HttpServletRequest request,
      HttpServletResponse response,
      Model model,
      Content entity,
      boolean isCreate)
      throws Exception {

    Set<Attachment> items = new HashSet<Attachment>();
    String[] strlist = request.getParameterValues("attachment");
    if (strlist != null && strlist.length != 0) {

      for (int index = 0; index < strlist.length; index++) {
        String str = strlist[index];
        if (StringUtils.isNotBlank(str)) {
          String[] v = str.split("｜");
          Attachment a = new Attachment();
          if (v[0] != null && !"".endsWith(v[0])) {
            a.setId(Long.parseLong(v[0]));
          }
          a.setFilePath(v[1] == null ? "0" : v[1]);
          a.setFileName(v[2] == null ? "0" : v[2]);
          a.setFileSize(Long.parseLong(v[3] == null ? "0" : v[3]));
          a.setContent(entity);
          items.add(a);
        }
      }
    }
    entity.setAttachments(items);
    String storageRoot = getFileStorageRoot();
    Map<String, UploadResult> uploadResults = doUpload(request);
    if (uploadResults != null && uploadResults.size() > 0) {
      UploadResult uploadResult = uploadResults.get("cover_f");
      if (uploadResult != null) {
        if (uploadResult.getSize() > 0) {
          File f = uploadResult.getFile();
          String filePath = f.getPath();
          // String cover_f_format = WebUtils.getCleanParam(request,
          // "cover_f_format");
          // if(StringUtils.isNotBlank(cover_f_format)&&"product".equals(cover_f_format)){
          // Images.resize(filePath, filePath, 100, 170,true);
          // }
          filePath = filePath.substring(storageRoot.length());
          filePath = filePath.replaceAll("\\\\", "/");
          entity.setCover(filePath);
        }
      }
    }
    String code = request.getParameter("code");
    String id = request.getParameter("id");
    ContentType contentType = contentTypeService.getContentType(code);
    if (contentType == null) {
      throw new BusinessException("参数有误，内容类型为null，code=" + code);
    }
    entity.setContentType(contentType);
    ContentBody contentBody = new ContentBody();
    String contents = request.getParameter("contents");
    contentBody.setBody(contents == null ? "" : contents);
    if (!isCreate && id != null) {
      contentBody.setId(Long.valueOf(id));
    }
    entity.setContentBody(contentBody);
    contentBody.setContent(entity);
    return entity;
  }

  /** @return */
  private String getFileStorageRoot() {
    return oFileProperties.getStorageRoot();
  }

  /** @return */
  private Object getFileServerRoot() {

    return oFileProperties.getServerRoot();
  }

  @Override
  protected void referenceData(HttpServletRequest request, Map<String, Object> model) {

    model.put("allStatuss", allStatuss);
    model.put("mediaRoot", getFileServerRoot());

    List<CmsCode> allCode = cmsCodeService.getAll();
    List<String> codes = new ArrayList<>();
    allCode.forEach(
        cmsCode -> {
          if (1 == cmsCode.getStatus()) {
            codes.add(cmsCode.getKeycode());
          }
        });
    model.put("allCodes", codes);
  }

  protected void doRemove(
      HttpServletRequest request, HttpServletResponse response, Model model, Serializable... ids)
      throws Exception {

    if (ids == null || ids.length == 0) {
      throw new RuntimeException("请求参数中没有指定需要删除的实体Id");
    }
    try {
      getEntityService().updateStatusBatch(Content.STATUS_DISABLED, ids);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("操作失败");
    }
  }

  protected Content doSave(
      HttpServletRequest request, HttpServletResponse response, Model model, boolean isCreate)
      throws Exception {

    Content entity = loadEntity(request);
    if (entity == null) {
      entity = new Content();
    }
    doDataBinding(request, entity);
    onSave(request, response, model, entity, isCreate);
    // 这里服务层默认是根据entity的Id是否为空自动判断是SAVE还是UPDATE.
    getEntityService().save(entity);
    entity.setContentBody(new ContentBody()); // IE8 兼容性问题，html 转JSON
    return entity;
  }

  @RequestMapping("importJsonReview")
  @ResponseBody
  public String importJsonReview(
      HttpServletRequest request, HttpServletResponse response, Model model) {

    String filePath = "";

    try {
      String storageRoot = getFileStorageRoot();
      Map<String, UploadResult> uploadResults = doUpload(request);
      if (uploadResults != null && uploadResults.size() > 0) {

        Iterator<Entry<String, UploadResult>> iterator = uploadResults.entrySet().iterator();

        if (iterator.hasNext()) {
          Map.Entry<String, UploadResult> entry = iterator.next();
          UploadResult uploadResult = entry.getValue();
          if (uploadResult != null) {
            if (uploadResult.getSize() > 0) {
              File f = uploadResult.getFile();
              filePath = f.getPath();
              filePath = filePath.substring(storageRoot.length());
            }
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return getFileServerRoot() + filePath;
  }

  @RequestMapping(value = "moveTop")
  @ResponseBody
  public JsonResult moveTop(HttpServletRequest request, HttpServletResponse response) {

    JsonResult result = new JsonResult();
    try {
      String id = request.getParameter(getEntityIdName());
      contentService.moveTop(Long.valueOf(id));
      result.setMessage("置顶成功");
    } catch (Exception e) {
      handleException(result, "置顶", e);
    }
    return result;
  }

  @RequestMapping(value = "moveUp")
  @ResponseBody
  public JsonResult moveUp(HttpServletRequest request, HttpServletResponse response) {

    JsonResult result = new JsonResult();
    try {
      String id = request.getParameter(getEntityIdName());
      contentService.moveUp(Long.valueOf(id));
      result.setMessage("上移成功");
    } catch (Exception e) {
      handleException(result, "上移", e);
    }
    return result;
  }

  protected UploadConfig getUploadConfig() {

    super.uploadConfig.setUseMemery(false);
    super.uploadConfig.setNeedTimePartPath(false);
    super.uploadConfig.setStorageRoot(getFileStorageRoot());
    return this.uploadConfig;
  }

  @Override
  protected Map<String, Object> getSearchParams(HttpServletRequest request) {
    Map<String, Object> params = Servlets.getParametersStartingWith(request, "search_");
    String code = request.getParameter("code"); // 内容类型编号
    if (StringUtils.isNotBlank(code)) {
      params.put("EQ_contentType.code", code);
    }
    params.put("EQ_status", Content.STATUS_ENABLED);
    return params;
  }

  @Override
  protected Map<String, Boolean> getSortMap(HttpServletRequest request) {
    Map<String, Boolean> sortMap = Maps.newLinkedHashMap();
    sortMap.put("pubDate", false);
    return sortMap;
  }
}
