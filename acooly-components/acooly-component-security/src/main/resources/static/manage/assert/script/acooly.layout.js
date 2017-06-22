(function($) {
	$.extend($.acooly, {
		layout : {
			menus : [],
			selectedMenu : {
				id : null,
				name : null
			},
			loadMenu : function(defaultMenuId){
				$("#mainMenu").empty();
				$.ajax({
					url : contextPath + "/manage/system/authorisedMenus.html",
					async : false,
					success : function(data, textStatus) {
						if (typeof (data) == 'string') {
							data = eval('(' + data + ')');
						}
						var a = '';
						$(data).each(
								function(i, domEle) {
									if (domEle.children && domEle.children.length > 0) {
										$.acooly.layout.menus.push(domEle);
										a += "<a id='mainMenu_" + domEle.id + "' href=\"javascript:$.acooly.layout.loadTree('" + domEle.id + "','" + domEle.name + "');\" class=\"" + (i == 1 ? 'buttonaft' : 'button') + "\"><span><img src='" + $.acooly.layout.getIconPath(domEle.icon) + "'/>"
												+ domEle.name + "</span></a>";
										if (defaultMenuId && defaultMenuId == domEle.id) {
											$.acooly.layout.selectedMenu = domEle;
										}
										if (!defaultMenuId && i == 0) {
											$.acooly.layout.selectedMenu = domEle;
										}
									}
								});
						$("#mainMenu").html(a);
					}
				});				
			},
			

			/**
			 * 显示子菜单
			 * 
			 * 加载左边子菜单
			 */
			loadTree : function(parentId, name) {
				if (!parentId) {
					parentId = $.acooly.layout.selectedMenu.id
				}
				// 改变主菜单样式
				if (parentId) {
					$("#mainMenu").children("a").attr("class", "button");
					$("#mainMenu_" + parentId).attr("class", "buttonaft");
				}

				var setting = {
					view : {
						showLine : true
					},
					callback : {
						onClick : function(event, treeId, treeNode, clickFlag) {
							$.acooly.layout.accessResource(treeNode);
						}
					}
				};
				var data = null, i = 0;
				while (data == null && i < 3) {
					data = $.acooly.layout.getTree(parentId)
					i++;
				}
				$.fn.zTree.init($("#layout_west_tree"), setting, data.children);
				var zTree = $.fn.zTree.getZTreeObj("layout_west_tree");
				zTree.expandAll(true);
			},

			reloadMenu : function() {
				$.acooly.layout.loadMenu($.acooly.layout.selectedMenu.id);
				$.acooly.layout.loadTree($.acooly.layout.selectedMenu.id);
			},
			getTree : function(id) {
				var selectMenu = $.acooly.layout.menus[0];
				$($.acooly.layout.menus).each(function(i, menu) {
					if (menu.id == id) {
						selectMenu = menu;
					}
				});
				$.acooly.layout.selectedMenu = selectMenu;
				return selectMenu
			},

			accessResource : function(node) {
				var type = node.type
				if (type == 'MENU')
					return;
				var url = node.value;
				if (!url || url == '')
					return;
				var opts = {
					title : node.name,
					closable : true,
					iconCls : node.icon
				};
				if (node.showMode == 1) {
					opts.href = contextPath + url;
				} else {
					opts.content = '<iframe src="' + contextPath + url + '" frameborder="0" style="border:0;width:100%;height:99%;background: url(/manage/assert/plugin/jquery-easyui/themes/default/images/loading.gif) center center no-repeat;" ></iframe>'
				}
				$.acooly.layout.addTab(opts);
			},

			createTree : function() {
				$.acooly.westTree = $('#layout_west_tree').tree({
					parentField : 'id',
					lines : true,
					onClick : function(node) {
						var url = node.attributes.url;
						if (!url || url == '')
							return;
						var opts = {
							title : node.text,
							closable : true,
							iconCls : node.iconCls
						};
						if (node.attributes.loadMode == 1) {
							opts.href = contextPath + url;
						} else {
							opts.content = '<iframe src="' + contextPath + url + '" frameborder="0" style="border:0;width:100%;height:99%;"></iframe>'
						}
						$.acooly.layout.addTab(opts);
					}
				});
				// $.acooly.layout.loadTree($.acooly.layout.selectedMenu.id,$.acooly.layout.selectedMenu.text);
			},
			addTab : function(opts) {
                //var jq = top.jQuery;
                var t = $('#layout_center_tabs');
				if (t.tabs('exists', opts.title)) {
					t.tabs('select', opts.title);
				} else {
					t.tabs('add', opts);
				}
			},
			selectTab : function(title) {
				$('#layout_center_tabs').tabs('select', title);
			},
			getSelectTab : function() {
				return $('#layout_center_tabs').tabs('getSelected').panel('options');
			},

			refreshTab : function(title) {
				$('#layout_center_tabs').tabs('getTab', title).panel('refresh');
			},

			getIconPath : function(icoCls) {
				if (!icoCls || icoCls.indexOf('-') == -1) {
					return null;
				}
				return '/manage/assert/style/' + icoCls.split('-').join('/') + '.png';
			}
		}
	});
})(jQuery);