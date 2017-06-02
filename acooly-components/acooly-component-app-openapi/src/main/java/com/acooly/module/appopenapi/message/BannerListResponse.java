/** create by zhangpu date:2015年5月10日 */
package com.acooly.module.appopenapi.message;

import com.acooly.module.appopenapi.dto.MediaInfo;
import com.google.common.collect.Lists;
import com.yiji.framework.openapi.common.annotation.OpenApiField;
import com.yiji.framework.openapi.common.message.ApiResponse;

import java.util.List;

/**
 * 首页 banner 列表
 *
 * @author zhangpu
 */
public class BannerListResponse extends ApiResponse {

  @OpenApiField(desc = "媒体列表")
  private List<MediaInfo> banners = Lists.newArrayList();

  public void append(MediaInfo dto) {
    banners.add(dto);
  }

  public List<MediaInfo> getBanners() {
    return banners;
  }

  public void setBanners(List<MediaInfo> banners) {
    this.banners = banners;
  }
}
