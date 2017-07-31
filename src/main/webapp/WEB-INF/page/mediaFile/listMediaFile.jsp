<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="../inc.jsp"></jsp:include>
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>视频文件管理</title>
<style  type="text/css">
#upload-buttons a.l-btn span span.l-btn-icon-left{
    padding: 0;
}
</style>
	<script type="text/javascript">
	var dataGrid;
	$(function() {	
		dataGrid = $('#dataGrid').datagrid({
			url : '${ctx}' + '/mediaFile/listMediaFileByQuery.do',
			striped : true,
			nowrap: true, 
			checkbox:true,
			pagination : true,
			//singleSelect : true,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'asc',
			pageSize : 10,
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
			columns:[
			[{field:'ck', checkbox:true,align: 'center'},
			 {title : 'ID', field : 'id', width:'80',align: 'center'},
			 {title : '批次号', field : 'batchNo',  width:'150',align: 'center'},
			 {title : '手机号', field : 'telphone',  width:'100',align: 'center'},
			 {title : '上传时间', field : 'createTime',  width:'200',align: 'center'},
			 {title : '领取状态', width:'100',align: 'center',
				 field : 'exchangeStatus',
					formatter : function(value, row, index) {	
						if(value != null && value != '') {
							if(value == '0'){
								return "未领取";
							}else if(value == '1'){
								return "已领取";
							}else if(value == '2'){
								return "无效";
							}
						}else {
							return '';
						}
					}	 
			 },
			 {title : '领取时间', field : 'exchangeTime',  width:'200',align: 'center'},
			 {
					width : '140',
					title : '操作',
					field : 'isExchangeState',
					align: 'center',
					formatter : function(value, row, index) {
						var str = "";
						if(value == "0") {
							str += $.formatString('<a style="color:red" href="javascript:void(0)" onclick="deleteFun(\'{0}\');" >删除</a>', row.id);
						}
						return str;
					}
			}
			]],
			toolbar : '#toolbar'
		});
	});	

	function searchFun() {
		dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
	}
	
	function cleanFun() {
		$('#searchForm input').val('');
		dataGrid.datagrid('load', {});	
	}


	function uploadWhitelist(){
		$('#uploaddlg').dialog('open').dialog('setTitle','上传文件');
		parent.$.modalDialog.openner_dataGrid = dataGrid;
		$('#uploadFile').form('clear');
	
	}
	

	
	function isvied(){

	    var file=uploadFile.impFile.value;
 		
 		if(file==""){
 			$.messager.alert('提示', '上传文件不能为空', 'warning');
 			return false;
 		}
 		
 		var fileName=file.substring(file.lastIndexOf(".")+1,file.length);
 		if(fileName!="txt"){
 			$.messager.alert('提示', '上传文件格式错误', 'warning');
 			file="";
 			return false;
 		}
 		return true;
   }
	
	
	function whitelistUpload(){
		
		if(isvied()){

		 $.messager.confirm('请确认',"确认导入TXT文件的白名单信息？",function(r){
			if (r){
			    $('#uploadFile').form('submit',{
			    	url: '${ctx}/media/whitelistUpload.do',
			    	
			    	onSubmit: function(){ 
			        	
			        	progressLoad();
						var isValid = $(this).form('validate');
						if (!isValid) {
							progressClose();
						}
						return isValid;
						
						
			        },
			        
			        success: function(result){
			        
			        	$('#uploaddlg').dialog('close');
			        	progressClose();
			        	result = $.parseJSON(result);
						if (result.success) {
							parent.$.messager.alert('提示', result.msg, 'success');
							//parent.$.modalDialog.openner_dataGrid.datagrid('reload');
							parent.$.modalDialog.openner_dataGrid.datagrid('load');
						} else {
							parent.$.messager.alert('提示', result.msg, 'warning');
						}
			       },
			       error:function(){
			    	    $('#uploaddlg').dialog('close');
			        	progressClose();
			    	    $.messager.alert('提示', "导入失败！", 'warning');
			       }
			    });
			}
		});
		
		
		}
	}
	
	function toAdd() {
		parent.$.modalDialog({
			title : '新增白名单',
			width : 630,
			height : 200,
			href : '${ctx}/media/addWhitelist.do',
			buttons : [ 
			{
				text : '提交',
				handler : function() {
					parent.$.modalDialog.openner_dataGrid = dataGrid;
					var f = parent.$.modalDialog.handler.find('#submitApply');
					f.submit();
				}
			} 
			
			]
		});
	}
	
	
	
	function batchDeleteFun() {
		var checkedItems = $('#dataGrid').datagrid('getChecked');
		if(checkedItems.length == 0) {
			$.messager.alert('提示', '请选择要删除的白名单!', 'info');
			return ;
		}
		var ids = [];
		var flag = true;
		$.each(checkedItems, function(index, item){
			if(flag){
				if(item.exchangeStatus == '2'){
					ids.push(item.id);
				}else{
					$.messager.alert('提示', '无效的白名单才可以删除！', 'info');
					console.log("item.exchangeStatus:"+item.exchangeStatus+";id:"+item.id);
					flag = false;
					return;
				}
			}else{
				return;
			}
		});      
		if(flag){
			$.messager.confirm('询问', '确认批量删除所选白名单？', function(b) {
				if (b) {
					progressLoad();
					$.post('${ctx}/media/batchDelete.do', {
						ids : ids.join(",")
					}, function(result) {
						if (result.success) {
							//parent.$.messager.alert('提示', result.msg, 'info');
							$('#dataGrid').datagrid('uncheckAll');
							dataGrid.datagrid('reload',$.serializeObject($('#searchForm')));
						}
						progressClose();
					}, 'JSON');	 
				}	
			});
		}
	}
	
	function deleteFun(id) {
		$.messager.confirm('询问', '确定删除该白名单？', function(b) {
			progressLoad();
			$.post('${ctx}/media/delete.do', {
				id : id
			}, function(result) {
				if (result.success) {
					dataGrid.datagrid('reload');
				}
				progressClose();
			}, 'JSON');
		});
	}
	function deleteWhitelistByBatchno(){
		$('#batchnoDeletedlg').dialog('open').dialog('setTitle','按批次号删除白名单');
		parent.$.modalDialog.openner_dataGrid = dataGrid;
		$('#batchnoDeleteForm').form('clear');
	
	}
	
	</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">

	<div data-options="region:'north',border:false" style="height: 60px; overflow: hidden;background-color: #f4f4f4">
		<form id="searchForm">
			<table>
				<tr>
					<th>手机号:</th>
					<td><input name="telphone" placeholder="请输入手机号"/></td>
					<th>批次号:</th>
					<td><input name="batchNo" placeholder="请输入批次号"/></td>
					
					<td colspan="3">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_search',plain:true" onclick="searchFun();">查询</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_cancel',plain:true" onclick="cleanFun();">清空</a>
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_add',plain:true" onclick="toAdd();">单个新增</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_add',plain:true" onclick="uploadWhitelist();">批量导入</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_resource',plain:true" onclick="exportFun();">EXCEL导出</a>
						<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_del',plain:true" onclick="batchDeleteFun();">批量删除</a>
						<!-- <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon_del',plain:true" onclick="deleteWhitelistByBatchno();">按批次号删除</a> -->
					</td>
				</tr>
			</table>
		</form>
	</div>
	<div data-options="region:'center',border:false,title:'白名单管理'" >
		<table id="dataGrid" data-options="fit:true,border:false"></table>
	</div>
	
	<div id="toolbar" >
			
	</div>
	
	<div id="uploaddlg" class="easyui-dialog" style="width:600px;height:200px" closed="true" buttons="#upload-buttons" modal="true">
		
		<div id='descriptor'>请选择要上传的txt文件：</div>
		<form id="uploadFile" method="post" enctype="multipart/form-data">
			<input type="file" name="impFile" id="importFile" />
		</form>
		<div style="color:red">
		<br/>
		提示：文件格式:一行一个号码的txt文件
		<br/>注：本次更新将不覆盖原先号码，大于活动次数的号码则置为无效
		</div>
	</div>
	<div id="upload-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="whitelistUpload()">上传</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#uploaddlg').dialog('close')">取消</a>
	</div>
	<!-- 
	<div id="batchnoDeletedlg" class="easyui-dialog" style="width:500px;height:150px" closed="true" buttons="#batchnoDelete-buttons" modal="true">
		
		<form id="batchnoDeleteForm" method="post" enctype="multipart/form-data">
			<><input type="text" name="batchNo" id="batchNo" />
			<br/>
			<br/>
			<span style="display:block;">备注：</span>
			<textarea name="remark" id="remark" style="width:80%;"  rows="2" cols="60" class="easyui-validatebox"></textarea>
		</form>
	</div>
	<div id="batchnoDelete-buttons">
		<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="batchNoDelete()">删除</a>
		<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#batchnoDeletedlg').dialog('close')">取消</a>
	</div> -->
	
</body>
</html>