<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">

	$(function() {		
		$.extend($.fn.validatebox.defaults.rules, {
		     validateCardBatch : {
				 validator: function (value, param) {
					var rules = $.fn.validatebox.defaults.rules;
					var flag = true;
					if(value.length == 0) {
						rules.validateCardBatch.message = '手机号不能为空';
						flag = false;
						return flag;
					}					
					var telphone = $("#telphone").val();
					var card = trim(telphone);
					var cards = card.split(',');
					for (var i = 0; i < cards.length; i++) {
						/* 
						if(/^1[3-9]\d{9}$/.test(account)) {
				  			isIeger = true;
				  		} */
						flag = true;
						//console.log("cards[i]"+cards[i]+"reg.test(cards[i])"+reg.test(cards[i]));
					    if('' == cards[i] || ' '== cards[i] || !/^1[3-9]\d{9}$/.test(cards[i])){
					    	rules.validateCardBatch.message = '手机号码格式错误，请输入合法手机号！';
					    	flag = false;
							return flag;
					    }
				    }
					return flag;
				 }
			}
		 });
		$('#submitApply').form({
			url : '${ctx}/media/saveWhitelist.do',
			onSubmit : function(param) {
				param.id = '${id}';
				progressLoad();
				var isValid = $(this).form('validate');
				if (!isValid) {
					progressClose();
				}
				return isValid;
			},
			success : function(result) {
				progressClose();
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.messager.alert('提示', result.msg, 'success');
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('提示', result.msg, 'warning');
				}
			}
		});
		
	});
function trim(str){ //删除左右两端的空格
	return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str){ //删除左边的空格
	return str.replace(/(^\s*)/g,"");
}
function rtrim(str){ //删除右边的空格
	return str.replace(/(\s*$)/g,"");
}


</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;padding: 3px;">
		<form id="submitApply" method="post">
			<table class="grid">
				<tr>
					<td style="text-align:center;">白名单:<br/>（手工输入手机号码）</td>
					<td colspan="3">
						<textarea name="telphone" id="telphone" style="width:100%;"  rows="5" cols="60" class="easyui-validatebox" data-options="required:true" validType="validateCardBatch"></textarea>
					  <br/><span style="color:red">请以逗号 ,英文格式来隔开手机号码。 此功能是手动输入，直接增加在列表中</span>
					</td>
				</tr>
				<!-- <tr>
					<td style="text-align:center;">备注:</td>
					<td colspan="3">
						<textarea name="remark" id="remark" style="width:100%;"  rows="3" cols="60" class="easyui-validatebox"></textarea>
					</td>
				</tr> -->	
			</table>
		</form>
	</div>

</div>
