<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="inc.jsp"></jsp:include>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>视频文件管理平台</title>
<script type="text/javascript">
/* 	function show(){
	    $.messager.show({
	        title:'库存告警',
	        msg:'${content}',
	        showType:'show'
	    });
	} */
	var index_layout;
	var index_tabs;
	var index_tabsMenu;
	var layout_west_tree;
	var layout_west_tree_url = '';
	
	/* var sessionInfo_userId = '${sessionInfo.id}';
	if (sessionInfo_userId) {//如果没有登录,直接跳转到登录页面
		layout_west_tree_url = '${ctx}/resource/tree';
	}else{
		window.location.href='${ctx}/admin/index';
	} */
	$(function() {
		index_layout = $('#index_layout').layout({
			fit : true
		});
		
		index_tabs = $('#index_tabs').tabs({
			fit : true,
			border : false,
			tools : [{
				iconCls : 'icon_home',
				handler : function() {
					index_tabs.tabs('select', 0);
				}
			}, {
				iconCls : 'icon_refresh',
				handler : function() {
					var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
					index_tabs.tabs('getTab', index).panel('refresh');
				}
			}, {
				iconCls : 'icon_del',
				handler : function() {
					var index = index_tabs.tabs('getTabIndex', index_tabs.tabs('getSelected'));
					var tab = index_tabs.tabs('getTab', index);
					if (tab.panel('options').closable) {
						index_tabs.tabs('close', index);
					}
				}
			} ]
		});
		
		layout_west_tree = $('#layout_west_tree').tree({
			//url : layout_west_tree_url,
			url : '${ctx}/user/tree.do',
			parentField : 'pid',
			lines : true,
			loadFilter : function (data, parent) {
				data.sort(displayInOrder);    //排序
				var opt = $(this).data().tree.options;
				var idFiled,
				textFiled,
				parentField;
				if (opt.parentField) {
					idFiled = opt.idFiled || 'id';
					textFiled = opt.textFiled || 'text';
					parentField = opt.parentField;
					
					var i,
					l,
					treeData = [],
					tmpMap = [];
					
					for (i = 0, l = data.length; i < l; i++) {
						tmpMap[data[i][idFiled]] = data[i];
					}
					
					for (i = 0, l = data.length; i < l; i++) {
						if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
							if (!tmpMap[data[i][parentField]]['children'])
								tmpMap[data[i][parentField]]['children'] = [];
							data[i]['text'] = data[i][textFiled];
							tmpMap[data[i][parentField]]['children'].push(data[i]);
						} else {
							data[i]['text'] = data[i][textFiled];
							treeData.push(data[i]);
						}
					}
					return treeData;
				}
				return data;
			},	
			onLoadSuccess : function(node,data) {
				
				var root = $('#layout_west_tree').tree('getRoots');
				$('#layout_west_tree').tree('collapseAll').tree('expand',root[0].target);
			},
			onClick : function(node) {
				//console.log(node);
				if (node.attributes && node.attributes.url) {
					var url = '${ctx}' + node.attributes.url;
					addTab({
						url : url,
						title : node.text,
						iconCls : node.iconCls
					});
				}
			}
		});
		
	});
	
	function displayInOrder(v1,v2) {
		var a1 = parseInt(v1.attributes.seq);
		var a2 = parseInt(v2.attributes.seq);
		if( a1 < a2) {
			return -1;
		}else if( a1 > a2 ){
			return 1;
		}else {
			return 0;
		}
	}
	
	function addTab(params) {
		var iframe = '<iframe src="' + params.url + '" frameborder="0" style="border:0;width:100%;height:98%;"></iframe>';
		var t = $('#index_tabs');
		var opts = {
			title : params.title,
			closable : true,
			iconCls : params.iconCls,
			content : iframe,
			border : false,
			fit : true
		};
		if (t.tabs('exists', opts.title)) {
			t.tabs('select', opts.title);
		} else {
			t.tabs('add', opts);
		}
	}
	
	function logout(){
		$.messager.confirm('提示','确定要退出?',function(r){
			if (r){
				window.location.href='${ctx}/logout.do';
				/* progressLoad();
				$.post( '${ctx}/logout.do', function(result) {
					console.log(result);
					if(result.success){
						progressClose();
						window.location.href='${ctx}/login.do';
					}
				}, 'json'); */
			}
		});
	}
	

	function editUserPwd() {
		parent.$.modalDialog({
			title : '修改密码',
			width : 300,
			height : 250,
			href : '${ctx}/user/editPwdPage',
			buttons : [ {
				text : '修改',
				handler : function() {
					var f = parent.$.modalDialog.handler.find('#editUserPwdForm');
					f.submit();
				}
			} ]
		});
	}

	
	
</script>
</head>
<body>
	<div id="loading" style="position: fixed;top: -50%;left: -50%;width: 200%;height: 200%;background: #fff;z-index: 100;overflow: hidden;">
	<img src="${ctx}/style/images/index/ajax-loader.gif" style="position: absolute;top: 0;left: 0;right: 0;bottom: 0;margin: auto;"/>
	</div>
	<div id="index_layout">
		<div data-options="region:'north'" style=" overflow: hidden;" id="header">
			<span style="float: right; padding-right: 20px;">
			<%-- 欢迎 <b>${sessionInfo.name}</b>&nbsp;&nbsp; <a href="javascript:void(0)" onclick="editUserPwd()" style="color: #fff">修改密码</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="logout()" style="color: #fff">安全退出</a> --%>
			<shiro:user>
				欢迎[<shiro:principal/>用户]登录，&nbsp;&nbsp;<a href="javascript:void(0)" onclick="logout()" style="color: #fff">安全退出</a>
			</shiro:user>
        	&nbsp;&nbsp;&nbsp;&nbsp;
    		</span>
    		<span class="header"></span>
		</div>
		<div data-options="region:'west',split:true" title="主导航" style="width: 200px; overflow: hidden;overflow-y:auto;">
			<div class="well well-small" style="padding: 10px 5px 5px 5px;">
				<ul id="layout_west_tree"></ul>
			</div>
		</div>
		<div data-options="region:'center'" style="overflow: hidden;">
			<div id="index_tabs" style="overflow: hidden;">
				<div title="首页" data-options="border:false" style="overflow: hidden;">
					<div style="padding:10px 0 10px 10px">
						<h2>系统介绍</h2>
						<div class="light-info">
							<div class="light-tip icon-tip"></div>
							<div>欢迎您使用建行商城管理平台</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div data-options="region:'south',border:false" style="height: 30px;line-height: 30px; overflow: hidden;text-align: center;background-color: #daeef5" >建行商城管理平台</div>
	</div>
	
	<!--[if lte IE 7]>
	<div id="ie6-warning"><p>您正在使用 低版本浏览器，在本页面可能会导致部分功能无法使用。建议您升级到 <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">Internet Explorer 8</a> 或以下浏览器：
	<a href="http://www.mozillaonline.com/" target="_blank">Firefox</a> / <a href="http://www.google.com/chrome/?hl=zh-CN" target="_blank">Chrome</a> / <a href="http://www.apple.com.cn/safari/" target="_blank">Safari</a> / <a href="http://www.operachina.com/" target="_blank">Opera</a></p></div>
	<![endif]-->
	
	<style>
	/*ie6提示*/
	#ie6-warning{width:100%;position:absolute;top:0;left:0;background:#fae692;padding:5px 0;font-size:12px}
	#ie6-warning p{width:960px;margin:0 auto;}
	</style>
	<script>
	jQuery(function ($) {
	 if ( jQuery.browser.msie && ( jQuery.browser.version == "6.0" )&& ( jQuery.browser.version == "7.0" ) && !jQuery.support.style ){
	  jQuery('#ie6-warning').css({'top':jQuery(this).scrollTop()+'px'});
	 }
	});
	</script>
</body>
</html>