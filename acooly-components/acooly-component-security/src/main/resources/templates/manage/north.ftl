<div class="acooly-top">
    <div class="bg">
        <div class="header">
            <div class="header-left">
                <div id="acooly_title" class="text-logo"></div>
                <div id="acooly_logo" style="border-bottom: 1px solid #99BBE8;">
                    <img src="" style="height: 35px;">
                </div>
            </div>
            <div class="header-right">
                <div class="userinfo">
                    <@shiroPrincipal/>
                </div>
                <div style="float: right;">
                    <a href="javascript:;" onclick="$.acooly.framework.changePassword();" class="easyui-linkbutton"
                       style="width: 80px; height: 24px;" data-options="iconCls:'icon-password'">修改密码</a> <a href="javascript:;"
                                                                                                             onclick="$.acooly.framework.logout();"
                                                                                                             class="easyui-linkbutton"
                                                                                                             style="width: 60px; height: 24px;"
                                                                                                             data-options="iconCls:'icon-logout',width:'80'">注销</a>
                </div>
            </div>
        </div>
        <div id="mainMenu" class="nav"></div>
        <div style="position: absolute; right: 0px; bottom: 0px;">
            <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_menu_theme',iconCls:'icon-theme'"
               style="color: #fefefe;">主题 (theme)</a>
        </div>
        <div id="layout_menu_theme" style="width: 100px; display: none;">
            <div id="theme_acooly" onclick="changeTheme('acooly');">acooly</div>
            <div id="theme_acooly4" onclick="changeTheme('acooly4');">acooly4</div>
            <div id="theme_bootstrap" onclick="changeTheme('bootstrap');">bootstrap</div>
            <div id="theme_metro" onclick="changeTheme('metro');">metro</div>
        </div>
    </div>
</div>
<script type="text/javascript">
    $(function () {
        $.acooly.layout.loadMenu();
        loadTheme();
        //console.info("logo",$.acooly.system.config.logo)
        if ($.acooly.system.config.logo) {
            $('#acooly_logo img').attr('src', $.acooly.system.config.logo)
            $('#acooly_title').hide();
        } else {
            if ($.acooly.system.config.title) {
                $('#acooly_title').text($.acooly.system.config.title);
            }
        }
    });
</script>