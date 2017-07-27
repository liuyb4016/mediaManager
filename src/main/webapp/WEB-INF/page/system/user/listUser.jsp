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
		<shiro:hasPermission name="user:freezeOrActivate">
			$.canActivate = true;
			$.canFreeze = true;
		</shiro:hasPermission>
			
		<shiro:hasPermission name="user:updateUser">
			$.canEdit = true;
		</shiro:hasPermission>
		
		<shiro:hasPermission name="user:delete">
			$.canDelete = true;
		</shiro:hasPermission>	
		
	</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
	.select-noback{
		background-image:none;
		background-color:transparent;
		color:#000;
	}
</style>
<title>用户管理</title>
	<script type="text/javascript">
	var dataGrid;
	var organizationTree;
	$(function() {
	
		/* organizationTree = $('#organizationTree').tree({
			url : '${ctx}/organization/tree',
			parentField : 'pid',
			lines : true,
			onClick : function(node) {
				dataGrid.datagrid('load', {
				    organizationId: node.id
				});
			}
		}); */
	
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}/user/list.do',
			striped : false,
			//rownumbers : true,
			checkbox:true,
			pagination : true,
			//singleSelect : true,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'asc',
			//checkOnSelect: false,
			 nowrap: true,
			pageSize : 15,
			pageList : [ 10, 20, 30, 40],
			 onSelect: function(index, data){
			        var opt = $(this).datagrid("options");
			        var rows1 = opt.finder.getTr(this, "", "selected", 1);
			        var rows2 = opt.finder.getTr(this, "", "selected", 2);
			        if (rows1.length > 0) {
			            $(rows1).each(function(){
			                var tempIndex = parseInt($(this).attr("datagrid-row-index"));
			                if (tempIndex == index) {
			                    $(this).removeClass("select-noback");
			                }
			                else {
			                    $(this).addClass("select-noback");
			                }
			            });
			        }
			        if (rows2.length > 0) {
			            $(rows2).each(function(){
			                var tempIndex = parseInt($(this).attr("datagrid-row-index"));
			                if (tempIndex == index) {
			                    $(this).removeClass("select-noback");
			                }
			                else {
			                    $(this).addClass("select-noback");
			                }
			            });
			        }
			    },
			    onUnselect: function(index, data){
			        var opt = $(this).datagrid("options");
			        var rows1 = opt.finder.getTr(this, "", "allbody", 1);
			        var rows2 = opt.finder.getTr(this, "", "allbody", 2);
			        if (rows1.length > 0) {
			            $(rows1).each(function(){
			                var tempIndex = parseInt($(this).attr("datagrid-row-index"));
			                if (tempIndex == index) {
			                    $(this).removeClass("select-noback");
			                }
			            });
			        }
			        if (rows2.length > 0) {
			            $(rows2).each(function(){
			                var tempIndex = parseInt($(this).attr("datagrid-row-index"));
			                if (tempIndex == index) {
			                    $(this).removeClass("select-noback");
			                }
			            });
			        }
			    },
			columns : [ [ 
			{
				field:'ck',
				checkbox:true
			},
			{
				width : '100',
				title : '编号',
				field : 'id'
			},
			{
				width : '100',
				title : '真实姓名',
				field : 'username'
			}, {
				width : '120',
				title : '登录账号',
				field : 'telephone'
			},{
				width : '120',
				title : '电子邮件',
				field : 'email'
			},{
				width : '80',
				title : '所属省份',
				field : 'provName'
			},{
				width : '80',
				title : '所属城市',
				field : 'cityName'
			},
			{
				width : '120',
				title : '创建时间',
				field : 'createTime'
			},
			{
				width : '120',
				title : '更新时间',
				field : 'updateTime'
			},
			{
				width : '120',
				title : '拥有角色',
				field : 'role',
				formatter : function(value, row, index) {	
					if(value != null) {
						return value.roleName;
					}else {
						return '<span style="color: red;">*角色已被删除*</span>';
					}
				}
			},
			{
				width : '80',
				title : '状态',
				field : 'locked',
				formatter : function(value, row, index) {
					switch (value) {
					case '0':
						return '正常';
					case '1':
						return '停用';
					}
					return value;
				}
			} , {
				field : 'action',
				title : '操作',
				width : 130,
				formatter : function(value, row, index) {
					var str = '';
					if(row.isdefault!=0){
						if(row.locked == '0') {
							if($.canFreeze) {
								str += $.formatString('<a href="javascript:void(0)" onclick="stateFun(\'{0}\',1);" >冻结</a>', row.id);
							}
						}
						if(row.locked == '1') {
							if($.canActivate) {
								str += $.formatString('<a href="javascript:void(0)" onclick="stateFun(\'{0}\',0);" >激活</a>', row.id);
							}
						}
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						if ($.canEdit) {
							str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >更新</a>', row.id);
						}
						str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
						if ($.canDelete) {
							str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						}
					}
					return str;
				}
			}
			] ],	
			toolbar : '#toolbar'
		});
	});
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 500,
			height : 300,
			href : '${ctx}/user/addUser.do',
			buttons : [ 
			<shiro:hasPermission name="user:add">
			{
				text : '添加',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userAddForm');
					f.submit();
				}
			} 
			</shiro:hasPermission>				
			]
		});
	}
	
	function stateFun(id,state) {
		var prefix = '';
		if(state == '1') {
			prefix = '冻结';
		}else if(state == '0') {
			prefix = '激活';
		}
		
		parent.$.messager.confirm('询问', '您是否要'+prefix+'当前用户？', function(b) {
			if (b) {
				var currentUserId = '${userId}';/*当前登录用户的ID*/
				if (currentUserId != id) {
					progressLoad();
					$.post('${ctx}/user/freezeOrActivate.do', {
						id : id,
						state : state
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
						msg : '不可以'+prefix+'自己！'
					});
				}
			}
		});
	}
	function deleteFun(id) {
	
		parent.$.messager.confirm('询问', '您是否要删除当前用户？', function(b) {
			if (b) {
				var currentUserId = '${userId}';/*当前登录用户的ID*/
				if (currentUserId != id) {
					progressLoad();
					$.post('${ctx}/user/delete.do', {
						id : id
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
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
			href : '${ctx}/user/updateUser?id=' + id,
			buttons : [ 
			<shiro:hasPermission name="user:update">
			{
				text : '更新',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;//因为添加成功之后，需要刷新这个dataGrid，所以先预定义好
					var f = parent.$.modalDialog.handler.find('#userEditForm');
					f.submit();
				}
			} 
			</shiro:hasPermission>         			
			]
		});
	}
	
	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});	
	}
		
	/**
	*批量删除用户
	*/
	function batchDeletaFun() {
		var checkedItems = $('#dataGrid').datagrid('getChecked');
		
		var currentUserId = '${userId}';/*当前登录用户的ID*/
		
		if(checkedItems.length == 0) {
			$.messager.alert('提示', '请选择要删除的用户!', 'info');
			return ;
		}
		
		var ids = [];
		
		var exist = 0;
		$.each(checkedItems, function(index, item){
			ids.push(item.id);
			if(item.id == currentUserId) {
				exist = 1;
			}
		});      
		
		if(exist) {
			$.messager.alert('提示', '删除列表包括您自己!请检查！', 'info');
			return ;
		}
		
		$.messager.confirm('询问', '确认批量删除用户？', function(b) {
			if (b) {
				progressLoad();
				//批量删除请求
				$.post('${ctx}/user/batchDelete.do', {
					ids : ids.join(",")
				}, function(result) {
					if (result.success) {
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload',$.serializeObject($('#searchForm')));
					}
					progressClose();
				}, 'JSON');	 
			}	
		});
	}
	
	/*
	*批量冻结用户
	*/
	function batchFreezeFun() {
		var checkedItems = $('#dataGrid').datagrid('getChecked');
		
		var currentUserId = '${userId}';/*当前登录用户的ID*/
		
		if(checkedItems.length == 0) {
			$.messager.alert('提示', '请选择要冻结的用户!', 'info');
			return ;
		}
		
		var ids = [];
		
		var exist = 0;
		$.each(checkedItems, function(index, item){
			ids.push(item.id);
			if(item.id == currentUserId) {
				exist = 1;
			}
		});      
		
		if(exist) {
			$.messager.alert('提示', '冻结列表包括您自己!请检查！', 'info');
			return ;
		}
		
		$.messager.confirm('询问', '确认批量冻结用户？', function(b) {
			if (b) {
				progressLoad();
				//批量冻结请求
				$.post('${ctx}/user/batchFreeze.do', {
					ids : ids.join(",")
				}, function(result) {
					if (result.success) {
						$('#dataGrid').datagrid('uncheckAll');
						parent.$.messager.alert('提示', result.msg, 'info');
						dataGrid.datagrid('reload',$.serializeObject($('#searchForm')));
					}
					progressClose();
				}, 'JSON');	 
			}	
		});
		
		$('#dataGrid').datagrid('uncheckAll');
	}
	
	/*
	*批量导出
	*/
	function batchExportFun() {
		var checkedItems = $('#dataGrid').datagrid('getChecked');
		
		
		if(checkedItems.length == 0) {
			$.messager.alert('提示', '请选择要导出的用户!', 'info');
			return ;
		}
		
		var ids = [];
		
		$.each(checkedItems, function(index, item){
			ids.push(item.id);
		});      
		
		$.messager.confirm('询问', '确认下载用户信息？', function(b) {
			if (b) {
				window.location.href='${ctx}/user/batchExport.do?ids='+ids.join(",");	
			}	
		});
		
		$('#dataGrid').datagrid('uncheckAll');
	}
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" style="height: 30px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>姓名:</th>
					<td><input name="username" placeholder="请输入用户姓名"/></td>
					<th>登录账号:</th>
					<td><input name="telephone" placeholder="请输入登录账号"/></td>
					<th>创建时间:</th>
					<td><input name="createTimeStart" id="createTimeStart" placeholder="点击选择时间" onclick="WdatePicker({maxDate: '#F{$dp.$D(\'createTimeEnd\')}',readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />至<input  name="createTimeEnd" id="createTimeEnd" placeholder="点击选择时间" onclick="WdatePicker({minDate:'#F{$dp.$D(\'createTimeStart\')}',readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" />
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a></td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'用户列表'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	<!-- <div data-options="region:'west',border:false,split:true,title:'组织机构'"  style="width:200px;overflow: hidden; ">
		<ul id="organizationTree"  style="width:180px;margin: 10px 10px 10px 10px">
		</ul>
	</div> -->
	<div id="toolbar" style="display: none;">
		<shiro:hasPermission name="user:addUser">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="user:batchDelete">
			<a onclick="batchDeletaFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_del'">批量删除</a>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="user:batchFreeze">
			<a onclick="batchFreezeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_cancel'">批量冻结</a>
		</shiro:hasPermission>
		
		<shiro:hasPermission name="user:batchExport">
			<a onclick="batchExportFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_resource'">批量导出</a>
		</shiro:hasPermission>
	</div>
</body>
</html>