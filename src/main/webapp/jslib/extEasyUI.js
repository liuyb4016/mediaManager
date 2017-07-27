/**
 * 浣縫anel鍜宒atagrid鍦ㄥ姞杞芥椂鎻愮ず
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.fn.panel.defaults.loadingMessage = '加载中...';
$.fn.datagrid.defaults.loadMsg = '加载中...';

/**
 * @requires jQuery,EasyUI
 * 
 * panel鍏抽棴鏃跺洖鏀跺唴瀛橈紝涓昏鐢ㄤ簬layout浣跨敤iframe宓屽叆缃戦〉鏃剁殑鍐呭瓨娉勬紡闂
 */
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $('iframe', this);
	try {
		if (frame.length > 0) {
			for ( var i = 0; i < frame.length; i++) {
				frame[i].src = '';
				frame[i].contentWindow.document.write('');
				frame[i].contentWindow.close();
			}
			frame.remove();
			if (navigator.userAgent.indexOf("MSIE") > 0) {// IE鐗规湁鍥炴敹鍐呭瓨鏂规硶
				try {
					CollectGarbage();
				} catch (e) {
				}
			}
		}
	} catch (e) {
	}
};

/**
 *  
 * 
 * @requires jQuery,EasyUI
 * 
 * 闃叉panel/window/dialog缁勪欢瓒呭嚭娴忚鍣ㄨ竟鐣�
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 淇闈㈡澘浣嶇疆 */
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/**
 *  
 * 
 * @requires jQuery,EasyUI
 * 
 * 閫氱敤閿欒鎻愮ず
 * 
 * 鐢ㄤ簬datagrid/treegrid/tree/combogrid/combobox/form鍔犺浇鏁版嵁鍑洪敊鏃剁殑鎿嶄綔
 */
var easyuiErrorFunction = function(XMLHttpRequest) {
	parent.$.messager.alert('閿欒', XMLHttpRequest.responseText);
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;

/**
 *  
 * 
 * @requires jQuery,EasyUI
 * 
 * 涓篸atagrid銆乼reegrid澧炲姞琛ㄥご鑿滃崟锛岀敤浜庢樉绀烘垨闅愯棌鍒楋紝娉ㄦ剰锛氬喕缁撳垪涓嶅湪姝よ彍鍗曚腑
 */
var createGridHeaderContextMenu = function(e, field) {
	e.preventDefault();
	var grid = $(this);/* grid鏈韩 */
	var headerContextMenu = this.headerContextMenu;/* grid涓婄殑鍒楀ご鑿滃崟瀵硅薄 */
	if (!headerContextMenu) {
		var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
		var fields = grid.datagrid('getColumnFields');
		for ( var i = 0; i < fields.length; i++) {
			var fildOption = grid.datagrid('getColumnOption', fields[i]);
			if (!fildOption.hidden) {
				$('<div iconCls="tick" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
			} else {
				$('<div iconCls="bullet_blue" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
			}
		}
		headerContextMenu = this.headerContextMenu = tmenu.menu({
			onClick : function(item) {
				var field = $(item.target).attr('field');
				if (item.iconCls == 'tick') {
					grid.datagrid('hideColumn', field);
					$(this).menu('setIcon', {
						target : item.target,
						iconCls : 'bullet_blue'
					});
				} else {
					grid.datagrid('showColumn', field);
					$(this).menu('setIcon', {
						target : item.target,
						iconCls : 'tick'
					});
				}
			}
		});
	}
	headerContextMenu.menu('show', {
		left : e.pageX,
		top : e.pageY
	});
};
$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;

/**
 * grid tooltip鍙傛暟
 * 
 *  
 */
var gridTooltipOptions = {
	tooltip : function(jq, fields) {
		return jq.each(function() {
			var panel = $(this).datagrid('getPanel');
			if (fields && typeof fields == 'object' && fields.sort) {
				$.each(fields, function() {
					var field = this;
					bindEvent($('.datagrid-body td[field=' + field + '] .datagrid-cell', panel));
				});
			} else {
				bindEvent($(".datagrid-body .datagrid-cell", panel));
			}
		});

		function bindEvent(jqs) {
			jqs.mouseover(function() {
				var content = $(this).text();
				if (content.replace(/(^\s*)|(\s*$)/g, '').length > 5) {
					$(this).tooltip({
						content : content,
						trackMouse : true,
						position : 'bottom',
						onHide : function() {
							$(this).tooltip('destroy');
						},
						onUpdate : function(p) {
							var tip = $(this).tooltip('tip');
							if (parseInt(tip.css('width')) > 500) {
								tip.css('width', 500);
							}
						}
					}).tooltip('show');
				}
			});
		}
	}
};
/**
 * Datagrid鎵╁睍鏂规硶tooltip 鍩轰簬Easyui 1.3.3锛屽彲鐢ㄤ簬Easyui1.3.3+
 * 
 * 绠�崟瀹炵幇锛屽闇�珮绾у姛鑳斤紝鍙互鑷敱淇敼
 * 
 * 浣跨敤璇存槑:
 * 
 * 鍦╡asyui.min.js涔嬪悗瀵煎叆鏈琷s
 * 
 * 浠ｇ爜妗堜緥:
 * 
 * $("#dg").datagrid('tooltip'); 鎵�湁鍒�
 * 
 * $("#dg").datagrid('tooltip',['productid','listprice']); 鎸囧畾鍒�
 * 
 *  
 */
$.extend($.fn.datagrid.methods, gridTooltipOptions);

/**
 * Treegrid鎵╁睍鏂规硶tooltip 鍩轰簬Easyui 1.3.3锛屽彲鐢ㄤ簬Easyui1.3.3+
 * 
 * 绠�崟瀹炵幇锛屽闇�珮绾у姛鑳斤紝鍙互鑷敱淇敼
 * 
 * 浣跨敤璇存槑:
 * 
 * 鍦╡asyui.min.js涔嬪悗瀵煎叆鏈琷s
 * 
 * 浠ｇ爜妗堜緥:
 * 
 * $("#dg").treegrid('tooltip'); 鎵�湁鍒�
 * 
 * $("#dg").treegrid('tooltip',['productid','listprice']); 鎸囧畾鍒�
 * 
 *  
 */
$.extend($.fn.treegrid.methods, gridTooltipOptions);

/**
 *  
 * 
 * @requires jQuery,EasyUI
 * 
 * 鎵╁睍validatebox锛屾坊鍔犻獙璇佷袱娆″瘑鐮佸姛鑳�
 */
$.extend($.fn.validatebox.defaults.rules, {
	eqPwd : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '瀵嗙爜涓嶄竴鑷达紒'
	}
});

//鎵╁睍tree锛屼娇鍏跺彲浠ヨ幏鍙栧疄蹇冭妭鐐�
$.extend($.fn.tree.methods, {
	getCheckedExt : function(jq) {// 鑾峰彇checked鑺傜偣(鍖呮嫭瀹炲績)
		var checked = $(jq).tree("getChecked");
		var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
		$.each(checkbox2, function() {
			var node = $.extend({}, $.data(this, "tree-node"), {
				target : this
			});
			checked.push(node);
		});
		return checked;
	},
	getSolidExt : function(jq) {// 鑾峰彇瀹炲績鑺傜偣
		var checked = [];
		var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
		$.each(checkbox2, function() {
			var node = $.extend({}, $.data(this, "tree-node"), {
				target : this
			});
			checked.push(node);
		});
		return checked;
	}
});

//鎵╁睍tree锛屼娇鍏舵敮鎸佸钩婊戞暟鎹牸寮�
$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [], tmpMap = [];
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
};

// 鎵╁睍treegrid锛屼娇鍏舵敮鎸佸钩婊戞暟鎹牸寮�
$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
	var opt = $(this).data().treegrid.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [], tmpMap = [];
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
};

// 鎵╁睍combotree锛屼娇鍏舵敮鎸佸钩婊戞暟鎹牸寮�
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;

/**
 * 
 * @requires jQuery,EasyUI
 * 
 * 鍒涘缓涓�釜妯″紡鍖栫殑dialog
 * 
 * @returns $.modalDialog.handler 杩欎釜handler浠ｈ〃寮瑰嚭鐨刣ialog鍙ユ焺
 * 
 * @returns $.modalDialog.xxx 杩欎釜xxx鏄彲浠ヨ嚜宸卞畾涔夊悕绉帮紝涓昏鐢ㄥ湪寮圭獥鍏抽棴鏃讹紝鍒锋柊鏌愪簺瀵硅薄鐨勬搷浣滐紝鍙互灏唜xx杩欎釜瀵硅薄棰勫畾涔夊ソ
 */
$.modalDialog = function(options) {
	if ($.modalDialog.handler == undefined) {// 閬垮厤閲嶅寮瑰嚭
		var opts = $.extend({
			title : '',
			width : 840,
			height : 680,
			modal : true,
			onClose : function() {
				$.modalDialog.handler = undefined;
				$(this).dialog('destroy');
			},
			onOpen : function() {
			}
		}, options);
		opts.modal = true;// 寮哄埗姝ialog涓烘ā寮忓寲锛屾棤瑙嗕紶閫掕繃鏉ョ殑modal鍙傛暟
		return $.modalDialog.handler = $('<div/>').dialog(opts);
	}
};

