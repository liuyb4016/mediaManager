<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
	<script type="text/javascript">
	<shiro:hasPermission name="role:updateRole">
		$.canEdit = true;
	</shiro:hasPermission>	
	
	<shiro:hasPermission name="role:delete">
		$.canDelete = true;
	</shiro:hasPermission>	

	<shiro:hasPermission name="role:grantRole">
		$.canGrant = true;
	</shiro:hasPermission>	
	
		
		
	</script>
<title>角色管理</title>
	<script type="text/javascript">
	var dataGrid;
	$(function() {
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}' + '/role/list.do',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'asc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [ [ 
			{
				width : '100',
				title : 'id',
				field : 'id',
				hidden :true
			}, 
			{
				width : '80',
				title : '名称',
				field : 'roleName'
			},
			{
				width : '170',
				title : '审批标识(审批角色需配置)',
				field : 'roleKey',
				hidden : true
			},
			{
				width : '200',
				title : '描述',
				field : 'remark'
			} , {
				field : 'action',
				title : '操作',
				width : 120,
				formatter : function(value, row, index) {
					var str = '&nbsp;';
					
					if(row.id != '1') {
						
						if ($.canGrant) {
							str += $.formatString('<a href="javascript:void(0)" onclick="grantFun(\'{0}\');" >授权</a>', row.id);
						}
						if(row.isdefault!=0){
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							if ($.canEdit) {
								str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >更新</a>', row.id);
							}
							str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
							if ($.canDelete) {
								str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
							}
						}
					}
					
					return str;
				}
			} ] ],
			toolbar : '#toolbar'
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 500,
			height : 300,
			href : '${ctx}/role/addRole.do',
			buttons : [ 
		    <shiro:hasPermission name="role:add">
			    {
					text : '添加',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#roleAddForm');
						f.submit();
					}
				 } 
			</shiro:hasPermission>           
			 ]
		});
	}
	
	function deleteFun(id) {
		if (id == undefined) {//点击右键菜单才会触发这个
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {//点击操作里面的删除图标会触发这个
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.messager.confirm('询问', '您是否要删除当前角色？', function(b) {
			if (b) {
				var currentUserId = '${sessionInfo.id}';/*当前登录用户的ID*/
				if (currentUserId != id) {
					progressLoad();
					$.post('${ctx}/role/delete.do', {
						id : id
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('reload');
						}
						progressClose();
					}, 'JSON');
				} else {
					parent.$.messager.show({
						title : '提示',
						msg : '不可以删除自己！'
					});
				}
			}
		});
	}
	
	function editFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		parent.$.modalDialog({
			title : '更新',
			width : 500,
			height : 300,
			href : '${ctx}/role/updateRole.do?id=' + id,
			buttons : [ 
			<shiro:hasPermission name="role:update"> 
				{
					text : '更新',
					handler : function() {
						parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#roleEditForm');
						f.submit();
					}
				}
			</shiro:hasPermission>			 
			]
		});
	}
	
	function grantFun(id) {
		if (id == undefined) {
			var rows = dataGrid.datagrid('getSelections');
			id = rows[0].id;
		} else {
			dataGrid.datagrid('unselectAll').datagrid('uncheckAll');
		}
		
		parent.$.modalDialog({
			title : '授权',
			width : 300,
			height : 500,
			href : '${ctx}/role/grantRole?id=' + id,
			buttons : [ {
				text : '授权',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#roleGrantForm');
					f.submit();
				}
			} ]
		});
	}
	
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',fit:true,border:false">
		<table id="dataGrid" data-options="fit:true,border:false">
			
		</table>
	</div>
	<div id="toolbar" style="display: none;">
		<shiro:hasPermission name="role:addRole">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</shiro:hasPermission>		
	</div>
</body>
</html>