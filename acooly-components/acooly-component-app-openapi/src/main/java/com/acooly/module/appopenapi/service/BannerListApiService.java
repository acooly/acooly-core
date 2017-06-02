package com.acooly.module.appopenapi.service;

import com.acooly.core.utils.Collections3;
import com.acooly.module.app.domain.AppBanner;
import com.acooly.module.app.service.AppBannerService;
import com.acooly.module.appopenapi.dto.MediaInfo;
import com.acooly.module.appopenapi.enums.ApiOwners;
import com.acooly.module.appopenapi.message.BannerListRequest;
import com.acooly.module.appopenapi.message.BannerListResponse;
import com.acooly.module.ofile.OFileProperties;
import com.google.common.collect.Maps;
import com.yiji.framework.openapi.core.meta.OpenApiService;
import com.yiji.framework.openapi.core.service.base.BaseApiService;
import com.yiji.framework.openapi.core.service.enums.ResponseType;
import com.yiji.framework.openapi.core.util.ApiUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 首页banner API
 *
 * @author zhangpu
 */
@OpenApiService(
  name = "bannerList",
  desc = "横幅广告",
  responseType = ResponseType.SYN,
  owner = ApiOwners.COMMON
)
public class BannerListApiService extends BaseApiService<BannerListRequest, BannerListResponse> {

  @Autowired private AppBannerService appBannerService;
  @Autowired private OFileProperties oFileProperties;

  @Override
  protected void doService(BannerListRequest request, BannerListResponse response) {
    Map<String, Boolean> sortMap = Maps.newHashMap();
    sortMap.put("sortTime", false);
    Map<String, Object> map = Maps.newHashMap();
    List<AppBanner> appBanners = appBannerService.query(map, sortMap);
    if (Collections3.isEmpty(appBanners)) {
      return;
    }
    for (AppBanner appBanner : appBanners) {
      response.append(convert(appBanner));
    }
  }

  private MediaInfo convert(AppBanner appBanner) {
    MediaInfo dto = new MediaInfo();
    dto.setComments(appBanner.getComments());
    dto.setImage(getFullUrl(appBanner.getMediaUrl()));
    dto.setThumbnail(dto.getImage());
    dto.setLink(getFullUrl(appBanner.getLink()));
    dto.setTitle(appBanner.getTitle());
    return dto;
  }

  private String getFullUrl(String url) {
    if (ApiUtils.isHttpUrl(url)) {
      return url;
    }
    return oFileProperties.getServerRoot() + '/' + url;
  }
}
