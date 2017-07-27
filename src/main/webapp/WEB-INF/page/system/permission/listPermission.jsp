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
	
	<shiro:hasPermission name="permission:updatePermission">
	 	$.canEdit = true;
	</shiro:hasPermission>
	
	
	<shiro:hasPermission name="permission:delete">
		$.canDelete = true;
	</shiro:hasPermission>
	
	   
		
	</script>
<title>权限管理</title>
<script type="text/javascript">
	var treeGrid;
	$(function() {
		
		treeGrid = $('#treeGrid').treegrid({
			url : '${ctx}/permission/list.do?pid=1',
			rownumbers : true,
			idField : 'id',
			treeField : 'text',
			lines : true,
			parentField : 'pid',
			fit : true,
			fitColumns : false,
			border : false,
			//sortName : 'seq',
			//sortOrder : 'asc',
 			/* loadFilter:function(data,parentId) {
				$.each(data,function(i,n) {
					if(n.parent != null) {
						n.pid = n.parent.id;
					}else {
						n.pid = null;
					}
				});
				console.log(data);
				return data;
			}, */
			
			loadFilter : function (data, parent) {
				data.sort(displayInOrder);    //排序
				return data;
			},
			
		   onBeforeLoad:function(row,param){
		     	// 此处就是异步加载地所在
				  if(row) {
				      $(this).treegrid('options').url = '${ctx}/permission/list.do?pid='+row.id;
				  }
		    },
			frozenColumns : [ [ 
			{
				title : '编号',
				field : 'id',
				width : 70,
				hidden : true
			} ] ],
			columns : [ [ {
				field : 'text',
				title : '权限名称',
			}, {
				field : 'action',
				title : '请求路径',
				width : 230
			}, {
				field : 'pkey',
				title : '资源表示符'				
			},
			{
				field : 'seq',
				title : '排序',
				width : 40
			}, {
				field : 'type',
				title : '资源类型',
				width : 80,
				formatter : function(value, row, index) {
					switch (value) {
					case '1':
						return '菜单';
					case '2':
						return '按钮';
					}
				}
			}, {
				field : 'remark',
				title : '备注'
			},
			{
				field : 'pid',
				title : '上级资源ID',
				width : 150,
			    hidden : true
			}, {
				field : 'test',
				title : '操作',
				width : 80,
				formatter : function(value, row, index) {
					var str = '&nbsp;';
					if ($.canEdit) {
					str += $.formatString('<a href="javascript:void(0)" onclick="editFun(\'{0}\');" >更新</a>', row.id);
					}
					str += '&nbsp;&nbsp;|&nbsp;&nbsp;';
					if ($.canDelete) {
					str += $.formatString('<a href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
					}
					return str;
				}
			} ] ],
			toolbar : '#toolbar'
		});
	});
	
	function displayInOrder(v1,v2) {
		var a1 = parseInt(v1.seq);
		var a2 = parseInt(v2.seq);
		if( a1 < a2) {
			return -1;
		}else if( a1 > a2 ){
			return 1;
		}else {
			return 0;
		}
	}
	
	function editFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			parent.$.modalDialog({
				title : '更新',
				width : 500,
				height : 350,
				href : '${ctx}/permission/updatePermission?id=' + node.id,
				buttons : [ 
				<shiro:hasPermission name="permission:update">
				{
					text : '更新',
					handler : function() {
						treeGrid.treegrid('options').url = '${ctx}/permission/list.do?pid=1';
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#resourceEditForm');
						f.submit();
					}
				} 
				</shiro:hasPermission>          
				]
			});
		}
	}
	
	function deleteFun(id) {
		if (id != undefined) {
			treeGrid.treegrid('select', id);
		}
		var node = treeGrid.treegrid('getSelected');
		if (node) {
			parent.$.messager.confirm('询问', '您是否要删除当前资源？删除当前资源会连同子资源一起删除!', function(b) {
				if (b) {
					progressLoad();
					$.post('${pageContext.request.contextPath}/permission/delete.do', {
						id : node.id
					}, function(result) {
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'info');
							treeGrid.treegrid('options').url = '${ctx}/permission/list.do?pid=1';
							treeGrid.treegrid('reload');
							//parent.layout_west_tree.tree('reload');							 
						}
						progressClose();
					}, 'JSON');
				}
			});
		}
	}
	
	function addFun() {
		parent.$.modalDialog({
			title : '添加',
			width : 500,
			height : 350,
			href : '${ctx}/permission/addPermission.do',
			buttons : [
			<shiro:hasPermission name="permission:add"> 
				{
					text : '添加',
					handler : function() {
						treeGrid.treegrid('options').url = '${ctx}/permission/list.do?pid=1';
						parent.$.modalDialog.openner_treeGrid = treeGrid;//因为添加成功之后，需要刷新这个treeGrid，所以先预定义好
						var f = parent.$.modalDialog.handler.find('#resourceAddForm');
						f.submit();
					}
				} 
			</shiro:hasPermission>            	
			]
		});
	}
	</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'center',border:false"  style="overflow: hidden;">
			<table id="treeGrid"></table>
		</div>
	</div>
	
	<div id="toolbar" style="display: none;">
		<shiro:hasPermission name="permission:addPermission">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon_add'">添加</a>
		</shiro:hasPermission>
	</div>
</body>
</html>