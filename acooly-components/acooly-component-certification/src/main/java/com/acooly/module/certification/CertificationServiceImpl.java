/*
 * 修订记录:
 * zhike@yiji.com 2017-04-21 10:14 创建
 *
 */
package com.acooly.module.certification;

import com.acooly.core.utils.enums.ResultStatus;
import com.acooly.module.certification.cert.RealNameAuthentication;
import com.acooly.module.certification.cert.RealNameAuthenticationException;
import com.acooly.module.certification.enums.CertResult;
import com.acooly.module.certification.platform.entity.CertificationRecord;
import com.acooly.module.certification.platform.service.CertificationRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 修订记录：
 *
 * @author zhike@yiji.com
 */
@Service("certificationService")
public class CertificationServiceImpl implements CertificationService {

  @Resource private RealNameAuthentication realNameAuthentication;

  @Resource private CertificationRecordService certificationRecordService;

  @Override
  public CertResult certification(String realName, String idCardNo) {
    CertResult result = new CertResult();
    try {
      CertificationRecord certificationRecord =
          certificationRecordService.findEntityByCarNoAndRealName(idCardNo, realName);
      if (certificationRecord != null && certificationRecord.getStatus() == 1) {
        result.setResultCode(ResultStatus.success.getCode());
        result.setResultMessage("实名认证成功");
        result.setBirthday(certificationRecord.getBirthday());
        result.setAddress(certificationRecord.getAddress());
        result.setSex(certificationRecord.getSex());
        result.setRealName(realName);
        result.setIdCardNo(idCardNo);
      } else {
        result = realNameAuthentication.certification(realName, idCardNo);
        //保存或更新数据
        saveRecord(result, certificationRecord);
      }
    } catch (RealNameAuthenticationException e) {
      result.setResultCode(e.getResultCode());
      result.setResultMessage(e.getResultMessage());
    }
    return result;
  }

  protected void saveRecord(CertResult result, CertificationRecord certificationRecord) {
    if (certificationRecord == null) {
      certificationRecord = new CertificationRecord();
    }
    certificationRecord.setStatus(
        StringUtils.equals(result.getResultCode(), ResultStatus.success.getCode()) ? 1 : 0);
    certificationRecord.setRealName(result.getRealName());
    certificationRecord.setIdCarNo(result.getIdCardNo());
    certificationRecord.setAddress(result.getAddress());
    certificationRecord.setBirthday(result.getBirthday());
    certificationRecord.setSex(result.getSex());
    certificationRecordService.save(certificationRecord);
  }
}
