acooly-core
====

## 1. 持久化

## 2. 控制层

### 文件上传

通过文件上传做了部分优化和调整。

1、文件上传的配置参数(UploadConfig)增加了针对图片自动生成缩略图的特性。

// 图片是否生成缩略图，默认false
thumbnailEnable=false
// 缩略图与原始图文件后新增的后缀，建议不要轻易修改，涉及删除清理。
thumbnailExt=_thum
// 缩率图的大小（长宽取最大的做等比例缩放）
thumbnailSize=200

>该特性默认关闭。

2、文件上传后，在uploadResult中增加返回getRelativeFile方法，可以获取直接存放到数据库中的相对路径.
