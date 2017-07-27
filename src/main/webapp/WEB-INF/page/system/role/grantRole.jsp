<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	var resourceTree;
	$(function() {
		resourceTree = $('#resourceTree').tree({
			url : '${ctx}/permission/listAll.do?',
			cascadeCheck: true,		
			parentField : 'pid',
			lines : true,
			checkbox : true,
/* 			onBeforeExpand:function(node,param){
			   $('#resourceTree').tree('options').url = "${ctx}/permission/list.do?pid=" + node.id;                    
			},  */            
			onLoadSuccess : function(node, data) {
				progressLoad();
				
/* 				$.post( '${ctx}/role/getPermissionIds.do', {
					id : '${role.id}'
				}, function(result) {
					var ids;
					if (result.data!= undefined) {
						ids = $.stringToList(result.data);
					}
					if (ids.length > 0) {
						for ( var i = 0; i < ids.length; i++) {
							if (resourceTree.tree('find', ids[i])) {
								resourceTree.tree('check', resourceTree.tree('find', ids[i]).target);
							}
						}
					}
				}, 'json'); */
				resourceTree.tree('options').cascadeCheck = false;
				var ids;
				if ('${ids}' != '') {
					ids = $.stringToList('${ids}');
				}
				if (ids != null && ids.length > 0) {
					for ( var i = 0; i < ids.length; i++) {
						var value = resourceTree.tree('find', ids[i]);
						if (value) {
							resourceTree.tree('check',value.target);
						}
					}
				}
				resourceTree.tree('options').cascadeCheck = true;
				progressClose();
			},
			toolbar : '#toolbar'
		});

		$('#roleGrantForm').form({
			url : '${ctx}/role/grant.do',
			onSubmit : function() {
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				var checknodes = resourceTree.tree('getChecked');
				checknodes = checknodes.concat(resourceTree.tree('getChecked', 'indeterminate')); 
				var ids = [];
				if (checknodes && checknodes.length > 0) {
					for ( var i = 0; i < checknodes.length; i++) {
						ids.push(checknodes[i].id);
					}
				}

				$('#resourceIds').val(ids);
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('授权成功', result.msg, 'success');
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});

	function checkAll() {
		var nodes = resourceTree.tree('getChecked', 'unchecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				resourceTree.tree('check', nodes[i].target);
			}
		}
	}
	function uncheckAll() {
		var nodes = resourceTree.tree('getChecked');
		if (nodes && nodes.length > 0) {
			for ( var i = 0; i < nodes.length; i++) {
				resourceTree.tree('uncheck', nodes[i].target);
			}
		}
	}
	function checkInverse() {
		var unchecknodes = resourceTree.tree('getChecked', 'unchecked');
		var checknodes = resourceTree.tree('getChecked');
		if (unchecknodes && unchecknodes.length > 0) {
			for ( var i = 0; i < unchecknodes.length; i++) {
				resourceTree.tree('check', unchecknodes[i].target);
			}
		}
		if (checknodes && checknodes.length > 0) {
			for ( var i = 0; i < checknodes.length; i++) {
				resourceTree.tree('uncheck', checknodes[i].target);
			}
		}
	}
</script>
<!--
<div id="roleGrantLayout" class="easyui-layout" data-options="fit:true,border:false">
	
 	<div data-options="region:'west'" title="系统资源" style="width: 300px; padding: 1px;">
		<div class="well well-small">
			<form id="roleGrantForm" method="post">
				<input name="id" type="hidden"  value="${role.id}" readonly="readonly">
				<ul id="resourceTree"></ul>
				<input id="resourceIds" name="ids" type="hidden" />
			</form>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
	</div> 
	
	
	<div data-options="region:'center'" title="" style="overflow: hidden; padding: 10px;">
		<div>
			<a class="easyui-linkbutton" href="javascript:void(0)" onclick="checkAll();">全选</a>
			<br /> <br />
			<a class="easyui-linkbutton" href="javascript:void(0)" onclick="checkInverse();">反选</a>
			<br /> <br />
			<a class="easyui-linkbutton" href="javascript:void(0)" onclick="uncheckAll();">取消</a>
		</div>
	</div>
</div>
-->
	 <div id="toolbar">
			<a onclick="checkAll();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_resource'">全选</a>
			<a onclick="checkInverse();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_resource'">反选</a>
			<a onclick="uncheckAll();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_resource'">取消</a>
	</div>

	<div class="easyui-panel" style="width:270px;height:400px;">
		<form id="roleGrantForm" method="post">
				<input name="id" type="hidden"  value="${role.id}" readonly="readonly">
				<div id="resourceTree"></div>
				<input id="resourceIds" name="ids" type="hidden" />
		</form>
	</div>
	

