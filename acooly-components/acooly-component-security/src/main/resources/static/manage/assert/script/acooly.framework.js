/**
 * acooly framework basic
 * 
 * @author zhangpu
 * @date 2012-4-15
 * @version: 1.0.0
 * @required JQuery EasyUI
 */
(function($) {
	$.extend({
		acooly : {
			selectedMenu : null,
			westTree : null,
			divdialog : null
		}
	});
	$.extend($.acooly, {
		framework : {
			// 各个表单的查询条件,用于导出
			afterQueryParams : {},
			/**
			 * 添加
			 */
			create : function(opts) {
				if (opts.entity) {
					$.acooly.framework.doCreate(opts.url, "manage_" + opts.entity + "_editform", "manage_" + opts.entity + "_datagrid", opts.title, opts.top, opts.width, opts.height, opts.addButton, opts.reload, opts.maximizable, opts.onSubmit, opts.onSuccess, opts.onFail, opts.onCloseWindow,opts.ajaxData);
				} else {
					$.acooly.framework.doCreate(opts.url, opts.form, opts.datagrid, opts.title, opts.top, opts.width, opts.height, opts.addButton, opts.reload, opts.maximizable, opts.onSubmit, opts.onSuccess, opts.onFail, opts.onCloseWindow,opts.ajaxData);
				}
			},

			
			/**
			 * 执行添加
			 */
			doCreate : function(url, form, datagrid, title, top, width, height, addButton, reload, maximizable, onSubmitCallback, successCallback, failCallback, onCloseWindow,ajaxData) {
				$('#' + datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				var t = top ? top : null;
				var w = width ? width : 500;
				var h = height ? height : 'auto';
				var tt = title ? title : '添加';
				tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o" ></i>'+tt;
				var addB = addButton ? addButton : '增加';
				addB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle" ></i>'+addB;
				var max = maximizable ? maximizable : false;
				url = $.acooly.framework.buildCanonicalUrl(url,ajaxData);
				var d = null;
				$.acooly.divdialog = d = $('<div/>').dialog({
					href : url,
					title : tt,
					top : t,
					width : w,
					height : h,
					modal : true,
					/*iconCls : 'icon-save',*/
					maximizable : max,
					buttons : [ {
						id : 'acooly-framework-add-btn',
						text : addB,
						/*iconCls : 'icon-add',*/
						handler : function() {
							$('#acooly-framework-add-btn').linkbutton('disable');
							var d = $(this).closest('.window-body');
							$('#' + form).ajaxSubmit({
								beforeSerialize : function(formData, options) {
									// 编辑页面beforeSubmit方法
									try {
										var _beforeSubmit = eval(form + "_beforeSubmit");
										if (typeof (_beforeSubmit) == "function") {
											_beforeSubmit.call(this, formData);
										}
									} catch (e) {
									}
								},
								beforeSubmit : function(formData, jqForm, options) {
									// 如果有回调函数则处理回调函数的验证
									if (onSubmitCallback && !onSubmitCallback.call(this, arguments)) {
										$('#acooly-framework-add-btn').linkbutton('enable');
										return false;
									}
									// 编辑页面onSubmit方法
									try {
										var defaultFunc = eval(form + "_onSubmit");
										if (defaultFunc && typeof (defaultFunc) == "function" && !defaultFunc.call(this, arguments)) {
											$('#acooly-framework-add-btn').linkbutton('enable');
											return false;
										}
									} catch (e) {
										// ig
									}
									// 默认easy-ui-validator验证
									var result = $('#' + form).form('validate');
									$('#acooly-framework-add-btn').linkbutton(result ? 'disable' : 'enable');
									return result;
								},
								success : function(result, statusText) {
									$('#acooly-framework-add-btn').linkbutton('enable');
									if (typeof (result) == 'string') {
										result = eval('(' + result + ')');
									}
									try {
										if (result.success) {
											// 如果有回调函数则处理回调函数
											if (successCallback) {
												successCallback.call(d, result);
											}
											$.acooly.framework.onSaveSuccess(d, datagrid, result, reload);
											d.dialog('destroy');
										} else {
											if (failCallback) {
												failCallback.call(d, result);
											}
										}
										if (result.message) {
											$.messager.show({
												title : '提示',
												msg : result.message
											});
										}
									} catch (e) {
										$.messager.alert('提示', e);
									}
									
								},
								error : function(XmlHttpRequest, textStatus, errorThrown) {
									$.messager.alert('提示', errorThrown);
								}
							});
						}
					}, {
						text : '<i class="fa fa-times-circle fa-lg fa-fw fa-col" ></i>关闭',
						/*iconCls : 'icon-cancel',*/
						handler : function() {
							// var d = $(this).closest('.window-body');
							d.dialog('close');

						}
					} ],
					onClose : function() {
						if (onCloseWindow) {
							onCloseWindow();
						}
						$(this).dialog('destroy');
					}
				});
			},

			/**
			 * 新增保存成功后，默认的处理：在datagrid的第一行添加该数据，同时关闭窗口。
			 */
			onSaveSuccess : function(dialog, datagrid, result, reload) {
				var className = $('#' + datagrid).attr('class');
				if(!className || className == ''){
					return;
				}
				if (reload) {
					if (className == 'easyui-treegrid') {
						$('#' + datagrid).treegrid('reload');
					} else {
						$('#' + datagrid).datagrid('reload');
					}
				} else {
					if (className == 'easyui-treegrid') {
						var node = $('#' + datagrid).treegrid('getSelected');
						if (node) {
							if ($('#' + datagrid).treegrid('getChildren', node.id).length > 0) {
								$('#' + datagrid).treegrid('reload', node.id);
							} else {
								$('#' + datagrid).treegrid('reload', node.parentId);
							}
						} else {
							$('#' + datagrid).treegrid('reload');
						}
					} else {
						$('#' + datagrid).datagrid('insertRow', {
							index : 0,
							row : result.entity
						});
					}
				}
			},

			/**
			 * 编辑
			 */
			edit : function(opts) {
				if (opts.entity) {
					$.acooly.framework.doEdit(opts.url, opts.id, "manage_" + opts.entity + "_editform", "manage_" + opts.entity + "_datagrid", opts.title, opts.top, opts.width, opts.height, opts.editButton, opts.reload, opts.maximizable, opts.onSubmit, opts.onSuccess, opts.onFail,
							opts.onCloseWindow,opts.ajaxData);
				} else {
					$.acooly.framework.doEdit(opts.url, opts.id, opts.form, opts.datagrid, opts.title, opts.top, opts.width, opts.height, opts.editButton, opts.reload, opts.maximizable, opts.onSubmit, opts.onSuccess, opts.onFail, opts.onCloseWindow,opts.ajaxData);
				}
			},

			/**
			 * 执行编辑
			 */
			doEdit : function(url, id, form, datagrid, title, top, width, height, editButton, reload, maximizable, onSubmitCallback, successCallback, failCallback, onCloseWindow,ajaxData) {
				// changelog 删除编辑时自动unselect所有的行。 by zhangpu on 2013-06-07
				// $('#'+datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
				var t = top ? top : null;
				var w = width ? width : 500;
				var h = height ? height : 'auto';
				var tt = title ? title : '编辑';
				tt = '<i class="fa fa-lg fa-fw fa-col fa-floppy-o" ></i>'+tt;
				var editB = editButton ? editButton : '修改';
				editB = '<i class="fa fa-lg fa-fw fa-col fa-plus-circle" ></i>'+editB;
				var max = maximizable ? maximizable : false;
				// changelog: 根据当前选中的行获取操作的ID. by zhangpu on 2013-06-07
				if (!id || id == '') {
					var selectedRow = $.acooly.framework.getSelectedRow(datagrid);
					if (selectedRow && selectedRow != null)
						id = selectedRow.id;
				}
				// changelog-end
				if (!id || id == '') {
					$.messager.alert('提示', '请选择需要编辑的数据');
					return;
				}
				var href = $.acooly.framework.getCanonicalUrl(url, id);
				href = $.acooly.framework.buildCanonicalUrl(href,ajaxData);
				var d = null;
				$.acooly.divdialog = d = $('<div/>').dialog({
					href : href,
					title : tt,
					top : t,
					width : w,
					height : h,
					modal : true,
					/*iconCls : 'icon-save',*/
					maximizable : max,
					buttons : [ {
						id : 'acooly-framework-edit-btn',
						text : editB,
						/*iconCls : 'icon-add',*/
						handler : function() {
							$('#acooly-framework-edit-btn').linkbutton('disable');
							var d = $(this).closest('.window-body');
							$('#' + form).ajaxSubmit({
								beforeSerialize : function(formData, options) {
									// 编辑页面beforeSubmit方法
									try {
										var _beforeSubmit = eval(form + "_beforeSubmit");
										if (typeof (_beforeSubmit) == "function") {
											_beforeSubmit.call(this, formData);
										}
									} catch (e) {
									}
								},
								beforeSubmit : function(formData, jqForm, options) {
									// 如果有回调函数则处理回调函数的验证
									if (onSubmitCallback && !onSubmitCallback.call(this, arguments)) {
										$('#acooly-framework-edit-btn').linkbutton('enable');
										return false;
									}
									// 编辑页面onSubmit方法
									try {
										var defaultFunc = eval(form + "_onSubmit");
										if (defaultFunc && typeof (defaultFunc) == "function" && !defaultFunc.call(this, arguments)) {
											$('#acooly-framework-edit-btn').linkbutton('enable');
											return false;
										}
									} catch (e) {
										// ig
									}
									// 默认easy-ui-validator验证
									var result = $('#' + form).form('validate');
									$('#acooly-framework-edit-btn').linkbutton(result ? 'disable' : 'enable');
									return result;
								},
								success : function(result, statusText) {
									$('#acooly-framework-edit-btn').linkbutton('enable');
									if (typeof (result) == 'string') {
										result = eval('(' + result + ')');
									}
									try {
										if (result.success) {
											// 如果有回调函数则处理回调函数
											if (successCallback) {
												successCallback(d, result);
											}
											$.acooly.framework.onUpdateSuccess(d, datagrid, result, id, reload);
											d.dialog('destroy');
										} else {
											if (failCallback) {
												failCallback.call(d, result);
											}
										}
										if (result.message) {
											$.messager.show({
												title : '提示',
												msg : result.message
											});
										}
									} catch (e) {
										$.messager.alert('提示', e);
									}
									
								},
								error : function(XmlHttpRequest, textStatus, errorThrown) {
									var e = XmlHttpRequest.responseText;
									$.messager.alert('提示', e);
								}
							});
						}
					}, {
						text : '<i class="fa fa-times-circle fa-lg fa-fw fa-col" ></i>关闭',
						/*iconCls : 'icon-cancel',*/
						handler : function() {
							var d = $(this).closest('.window-body');
							d.dialog('close');
						}
					} ],
					onClose : function() {
						if (onCloseWindow) {
							onCloseWindow();
						}
						$(this).dialog('destroy');
					}
				});
			},

			/**
			 * 修改更新保存成功后
			 */
			onUpdateSuccess : function(dialog, datagrid, result, id, reload) {
				// changelog: by zhangpu on 2013-5-28, do compatibe with
				// datagrid and treegrid
				var className = $('#' + datagrid).attr('class');
				if(!className || className == ''){
					return;
				}
				if (reload) {
					if (className == 'easyui-treegrid') {
						$('#' + datagrid).treegrid('reload');
					} else {
						$('#' + datagrid).datagrid('reload');
					}
				} else {
					if (className == 'easyui-treegrid') {
						$('#' + datagrid).treegrid('update', {
							id : result.entity.id,
							row : result.entity
						});
					} else {
						$('#' + datagrid).treegrid('updateRow', {
							index : $('#' + datagrid).treegrid('getRowIndex', id),
							row : result.entity
						});
					}
				}
			},

			/**
			 * search
			 * 
			 * @param searchForm
			 * @param datagrid
			 */
			search : function(searchForm, datagrid) {
				// 查询页面beforeSubmit方法
				try {
					var defaultFunc = eval(searchForm + "_beforeSubmit");
					if (defaultFunc && typeof (defaultFunc) == "function" && !defaultFunc.call(this, arguments)) {
						return;
					}
				} catch (e) {
				}
				var queryParams = serializeObject($('#' + searchForm));
				if (isEmptyObject(queryParams)) {
					queryParams = serializeObjectFromContainer($('#' + searchForm));
				}
				$.acooly.framework.afterQueryParams[searchForm] = queryParams;
				$('#' + datagrid).datagrid('load', queryParams);
			},

			/**
			 * 按键回车直接执行查询
			 * 
			 * @param searchForm
			 * @param datagride
			 */
			registerKeydown : function(searchForm, datagride) {
				var form = $('#' + searchForm);
				$('input', form).keydown(function(event) {
					if (event.keyCode == 13) {
						$.acooly.framework.search(searchForm, datagride);
					}
				});
			},

			/**
			 * 查詢并導出數據
			 * 
			 * @param url
			 * @param searchForm
			 * @param fileName
			 */
			exports : function(url, searchForm, fileName, confirmTitle, confirmMessage) {
				var queryParams = $.acooly.framework.afterQueryParams[searchForm];
				if (isEmptyObject(queryParams)) {
					queryParams = serializeObject($('#' + searchForm));
				}
				if(fileName){
					$(queryParams).attr('exportFileName',fileName);					
				}

				$.acooly.framework.createAndSubmitForm(url, queryParams);
			},

			/**
			 * 动态创建表单和提交表单
			 */
			createAndSubmitForm : function(url, inputObjects) {
				var form = $('<form action="' + contextPath + url + '" method="POST"></form>');
				var token = $("meta[name='X-CSRF-TOKEN']").attr("content")
				form.append($('<input type="hidden" name="_csrf" value="' + token + '">'));
				for ( var key in inputObjects) {
					if (inputObjects[key] != '') {
						var inputForm = $('<input type="hidden" name="' + key + '" value="' + inputObjects[key] + '" />');
						form.append(inputForm);
					}
				}
				form.appendTo($('body')).submit();
				form.remove();
			},

			/**
			 * show
			 */
			show : function(url, width, height) {
				$('<div/>').dialog({
					href : contextPath + url,
					width : width,
					height : height,
					modal : true,
					title : '查看',
					buttons : [ {
						text : '关闭',
						iconCls : 'icon-cancel',
						handler : function() {
							var d = $(this).closest('.window-body');
							d.dialog('close');
						}
					} ],
					onClose : function() {
						$(this).dialog('destroy');
					}
				});
			},
			get : function(opts) {
				var url = opts.url;
				var width = opts.width != null ? opts.width : 400;
				var height = opts.height != null ? opts.height : 300;
				var title = opts.title != null ? opts.title : '查看'
				$('<div/>').dialog({
					href : contextPath + url,
					width : width,
					height : height,
					modal : true,
					title : title,
					buttons : [ {
						text : '关闭',
						iconCls : 'icon-cancel',
						handler : function() {
							var d = $(this).closest('.window-body');
							d.dialog('close');
						}
					} ],
					onClose : function() {
						$(this).dialog('destroy');
					}
				});
			},

			/**
			 * 创建uploadify上传组件
			 */
			createUploadify : function(options) {
				// console.info(contextPath +
				// options.url+";jsessionid="+options.jsessionid)
				$('#' + options.uploader).uploadify({
					'buttonText' : '<span style="font-size: 12px;font-weight: normal;">选择文件</span>',
					'height' : 22,
					'width' : 80,
					'auto' : false,
					'multi' : false,
					'queueSizeLimit' : 1,
					'fileTypeDesc' : '支持格式:Excel2000和CSV',
					'fileTypeExts' : '*.xls;*.csv',
					'fileSizeLimit' : '200MB',
					'swf' : '/manage/assert/plugin/jquery-uploadify/uploadify.swf',
					'uploader' : contextPath + options.url + ";jsessionid=" + options.jsessionid,
					'cancelImg' : '/manage/assert/plugin/jquery-uploadify/uploadify-cancel.png',
					'successTimeout' : 600,
					'formData' : options.formData,
					'onUploadSuccess' : function(file, data, response) {
						var result = $.parseJSON(data);
						if (result.success) {
							$('#' + options.messager).html('导入成功.');
						} else {
							$('#' + options.messager).html('导入失败:' + result.message);
						}
						$('#' + options.uploader).uploadify('cancel');
					},
					'onUploadError' : function(file, errorCode, errorMsg, errorString) {
						$('#' + options.messager).html('导入失败 - [' + errorString + ']');
						$('#' + options.uploader).uploadify('cancel');
					},
					'onUploadProgress' : function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
						if (totalBytesUploaded < totalBytesTotal) {
							$('#' + options.messager).html("正在上传文件: [" + totalBytesUploaded / 1024 + "K/" + totalBytesTotal / 1024 + "K]");
						} else {
							$('#' + options.messager).html("正在保存上传数据...");
						}

					}
				});
			},
			imports_dialog : '',
			imports : function(opts) {
				var w = opts.width ? opts.width : 400;
				var h = opts.height ? opts.height : 280;

				$.acooly.framework.imports_dialog = $('<div/>').dialog({
					href : contextPath + opts.url,
					width : w,
					height : h,
					iconCls : 'icon-import',
					modal : true,
					title : '数据导入',
					buttons : [ {
						text : '上传导入',
						iconCls : 'icon-import',
						handler : function() {
							$('#' + opts.uploader).uploadify('upload', '*');
						}
					}, {
						text : '关闭',
						iconCls : 'icon-cancel',
						handler : function() {
							var d = $(this).closest('.window-body');
							d.dialog('close');
						}
					} ],
					onClose : function() {
						$('#' + opts.uploader).uploadify('destroy');
						$(this).dialog('destroy');
					}
				});
			},

			/**
			 * 提交通过ID方式修改记录各种状态的submit。支持删除，移动，状态变更等
			 */
			confirmSubmit : function(url, id, datagrid, confirmTitle, confirmMessage,successCallBack) {
				var title = confirmTitle ? confirmTitle : '确定';
				var message = confirmMessage ? confirmMessage : '您是否要提交该操作？';
				$.messager.confirm(title, message, function(r) {
					if (r) {
						$.ajax({
							url : contextPath + url,
							data : {
								id : id
							},
							success : function(result) {
								if (typeof (result) == 'string')
									result = eval('(' + result + ')');
								if (result.success) {
									var className = $('#' + datagrid).attr('class');
									if (className == 'easyui-treegrid') {
										var node = $('#' + datagrid).treegrid('getSelected');
										var parent = $('#' + datagrid).treegrid('getParent', node.id);
										if (parent) {
											$('#' + datagrid).treegrid('reload', parent.id);
										} else {
											$('#' + datagrid).treegrid('reload');
										}
									} else {
										$('#' + datagrid).datagrid('reload');
									}
									if(successCallBack){
										successCallBack.call(this);
									}
								}
								if (result.message) {
									$.messager.show({
										title : '提示',
										msg : result.message
									});
								}
							}
						});
					}
				});
			},

			confirmRequest : function(url, requestData, datagrid, confirmTitle, confirmMessage, onSuccess, onFailure) {
				var title = confirmTitle ? confirmTitle : '确定';
				var message = confirmMessage ? confirmMessage : '您是否要提交该操作？';
				$.messager.confirm(title, message, function(r) {
					if (r) {
						$.ajax({
							url : contextPath + url,
							data : requestData,
							success : function(result) {
								if (result.success) {
									$('#' + datagrid).datagrid('reload');
									$('#' + datagrid).datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
									if (onSuccess) {
										onSuccess.call(this, arguments);
									}
								} else {
									if (onFailure) {
										onFailure.call(this, arguments);
									}
								}
								if (result.message) {
									$.messager.show({
										title : '提示',
										msg : result.message
									});
								}
							}
						});
					}
				});
			},

			/**
			 * 排序移动，支持置顶和上移 confirmTitle,confirmMessage 为可选
			 */
			move : function(url, id, datagrid, confirmTitle, confirmMessage) {
				$.post(url, {
					id : id
				}, function(result) {
					if (result.success) {
						var className = $('#' + datagrid).attr('class');
						if (className == 'easyui-treegrid') {
							var node = $('#' + datagrid).treegrid('getSelected');
							var parent = $('#' + datagrid).treegrid('getParent', node.id);
							if (parent) {
								$('#' + datagrid).treegrid('reload', parent.id);
							} else {
								$('#' + datagrid).treegrid('reload');
							}
						} else {
							$('#' + datagrid).datagrid('reload');
						}
					}
					if (result.message) {
						$.messager.show({
							title : '提示',
							msg : result.message
						});
					}
				});
			},

			/**
			 * 删除 置顶ID，多个ID可以使用逗号分割
			 */
			remove : function(url, ids, datagrid, confirmTitle, confirmMessage,successCallBack) {
				$.acooly.framework.confirmSubmit(url, ids, datagrid, confirmTitle, confirmMessage,successCallBack);
			},

			/**
			 * 批量刪除
			 * 
			 * @param url
			 * @param datagrid
			 */
			removes : function(url, datagrid, confirmTitle, confirmMessage,successCallBack) {
				var rows = $.acooly.framework._getCheckedRows(datagrid);
				var ids = [];
				if (rows.length > 0) {
					for (var i = 0; i < rows.length; i++) {
						ids.push(rows[i].id);
					}
					$.acooly.framework.remove(url, ids.join(','), datagrid, confirmTitle, confirmMessage);
				} else {
					$.messager.show({
						title : '提示',
						msg : '请勾选要删除的记录！'
					});
				}
			},
			logout : function() {
				$.messager.confirm('提醒', '确定要注销退出吗？', function(r) {
					if (r) {
						window.location.href = contextPath + '/manage/logout.html';
					}
				});
			},
			ajaxLogout : function(queryString) {
				$.messager.confirm('提醒', '确定要注销退出吗？', function(r) {
					if (r) {
						$.ajax({
							url : contextPath + '/manage/logout.html',
							success : function(data, txtStatus) {
								console.info("txtStatus:" + txtStatus);
								if (queryString) {
									window.location.href = contextPath + '/manage/login.html?' + queryString;
								} else {
									window.location.href = contextPath + '/manage/login.html';
								}
							}
						});
					}
				});
			},
			changePassword : function() {
				$('<div/>').dialog({
					href : contextPath + '/manage/system/changePasswordView.html',
					width : 400,
					height : 230,
					modal : true,
					title : '修改密码',
					buttons : [ {
						text : '修改密码',
						iconCls : 'icon-edit',
						handler : function() {
							var d = $(this).closest('.window-body');
							$('#user_changePassword_form').form('submit', {
								onSubmit : function() {
									var isValid = $(this).form('validate');
									if (!isValid) {
										return false;
									}
									// 自定义检查合法性
									if ($('#system_newPassword').val() != $('#system_confirmNewPassword').val()) {
										$.messager.alert('提示', '两次密码输入不一致');
										return false;
									}
									return true;
								},
								success : function(data) {
									try {
										var result = $.parseJSON(data);
										if (result.success) {
											d.dialog('destroy');
										}
										if (result.message) {
											$.messager.show({
												title : '提示',
												msg : result.message
											});
										}
									} catch (e) {
										$.messager.alert('提示', result);
									}
								}
							});
						}
					} ],
					onClose : function() {
						$(this).dialog('destroy');
					},
					onLoad : function() {

					}
				});
			},

			/**
			 * KingEditor富文本框编辑器
			 */
			kingEditor :(opts){
				var uploadUrl = opts.uploadUrl;
				if (!uploadUrl)
					uploadUrl = '/ofile/kindEditor.html';
				var options = {
					themeType : 'default', //指定主题风格，可设置”default”、”simple”，指定simple时需要引入simple.css;
					minHeight : opts.minHeight + 'px',
					resizeType : 1, // 2或1或0，2时可以拖动改变宽度和高度，1时只能改变高度，0时不能拖动;默认值:2
					allowFileManager : true, // true时显示浏览远程服务器按钮 ;默认值: false
					allowMediaUpload : false, // true时显示视音频上传按钮。默认值: true
					allowFlashUpload : false, // true时显示Flash上传按钮;默认值: true
					items : [ 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline', 'removeformat','lineheight', '|', 'justifyleft', 'justifycenter', 'justifyright','anchor', 'plainpaste', 'wordpaste','clearhtml','quickformat','insertorderedlist', 'insertunorderedlist', '|', 'emoticons', 'image', 'multiimage',
						'baidumap','link', 'unlink', 'source', 'preview' ],
					// 上传的url
					uploadJson : uploadUrl,
					// 加载完成后改变皮肤
					afterCreate : function() {
						var color = $('.panel-header').css('background-color');
						$('.ke-toolbar').css('background-color', color);
					},
					// 失去焦点时，保存
					afterBlur : function() {
						this.sync();
					}
				};
				KindEditor.options.filterMode = false;
				return KindEditor.create('#' + opts.textareaId, options);
			},
			
			/******************
			 * 常用easyui工具方法
			 */
			
			/**
			 * 获取Datagrid或TreeGrid当前选中的行
			 */
			getSelectedRow : function(grid) {
				var className = $('#' + grid).attr('class');
				if (className == 'easyui-treegrid') {
					return $('#' + grid).treegrid('getSelected');
				} else {
					return $('#' + grid).datagrid('getSelected');
				}
			},
			
			/**
			 * 选中grid的行，如果未选择则提示错误，否则回调传入的函数逻辑
			 */
			fireSelectRow : function(gridId,selectedCallBack,errorMessage){
				var row = $.acooly.framework.getSelectedRow(gridId);
				if(!row){
					var msg = '请先选择操作的数据行' ;
					if(errorMessage) msg = errorMessage;
					$.messager.show({title : '提示',msg : msg});
				    return null;
				}
				selectedCallBack.call(this,row);
			},			
			
			/**
			 * 获取Datagrid或TreeGrid当前选中checkbox的行
			 */
			_getCheckedRows : function(grid) {
				var className = $('#' + grid).attr('class');
				if (className == 'easyui-treegrid') {
					return $('#' + grid).treegrid('getSelections');
				} else {
					return $('#' + grid).datagrid('getSelections');
				}
			},
			
			/**
			 * 重新加载grid数据
			 */
			reloadGrid : function(grid) {
				var className = $('#' + datagrid).attr('class');
				if (className == 'easyui-treegrid') {
					$('#' + grid).treegrid('reload');
				} else {
					$('#' + grid).datagrid('reload');
				}
			},			
			
			/**
			 * ajax方式手动load datagrid.
			 * 
			 * opts:gridId,url,ajaxData
			 */
			loadGrid : function(opts){
				var data = { "sort":"id", "order":"desc","page":1,"rows":20};
				$.extend(data,opts.ajaxData);
				$.ajax({
					url:opts.url + '?'+new Date(),
					data : data,
					breforeSend:function(){
						$('#'+opts.gridId).datagrid('loading');
					},
					complete: function(){
						$('#'+opts.gridId).datagrid('loaded');
					},
					success : function(result) {
						if(result.success){
							$('#'+opts.gridId).datagrid('loadData',result);
						}else{
							$.messager.show({title : '提示',msg : result.message});
						}
					},
					error:function(r,e){
						$.messager.show({title : '提示',msg : e});
					}
				});				
			},
			
			/**
			 * 组装待ID参数的URL
			 */
			getCanonicalUrl : function(url, id) {
				url = contextPath + url;
				url += (url.indexOf('?') != -1 ? '&' : '?');
				url += 'id=' + id
				return url;
			},
			
			/**
			 * 组装queryString参数的URL
			 */
			buildCanonicalUrl : function(url,ajaxData){
				var data = {};
				$.extend(data,ajaxData);
				url = contextPath + url;
				url += (url.indexOf('?') != -1 ? '' : '?');
				for(var k in data){
					url += ('&' + k + '=' + data[k]);
				}
				return url;
			}
			
			
			
			
			
		}
	});
})(jQuery);
