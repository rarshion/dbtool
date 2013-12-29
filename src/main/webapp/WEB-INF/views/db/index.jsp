<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx_path" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据库结构比较工具</title>
<link type="text/css" rel="stylesheet" href="${ctx_path}/css/bootstrap.min.css" />
<script type="text/javascript" src="${ctx_path}/js/jquery/jquery.js"></script>
<script>
var CTX_PATH = '${ctx_path}';
$(function(){
	initShowSqlBtn();
	initDbNameSel('#J_sampleAddress', '#J_sampleDbName');
	initDbNameSel('#J_targetAddress', '#J_targetDbName');
	initClearBtn();
});

function initShowSqlBtn() {
	var url = CTX_PATH + '/db/compare',
		$sqlPre = $('#J_sqlPre');
	var $sampleAddressSel = $('#J_sampleAddress'),
		$sampleDbNameSel = $('#J_sampleDbName'),
		$targetAddressSel = $('#J_targetAddress'),
		$targetDbNameSel = $('#J_targetDbName');
	$('#J_compareBtn').on('click', function(){
		var $this = $(this);
		var params = {
			sampleAddress: $sampleAddressSel.val(),
			sampleDbName: $sampleDbNameSel.val(),
			targetAddress: $targetAddressSel.val(),
			targetDbName: $targetDbNameSel.val()
		};
		for(var key in params) {
			if(!params[key]) {
				alert(key + '不能为空!');
				return;
			}
		}
		$this.attr({disabled: true});
		$.getJSON(url, params, function(data){
			$this.attr({disabled: false});
			$sqlPre.empty();
			if(!data || !data.sqlList) {
				return;
			}
			$.each(data.sqlList, function(i, sql){
				$sqlPre.append(sql + "\n");
			});
		});
	});
}

function initDbNameSel(addressSelSelector, dbNameSelSelector) {
	if(!addressSelSelector || !dbNameSelSelector) {
		return;
	}
	var $addressSel = $(addressSelSelector),
		$dbNameSel = $(dbNameSelSelector);
	if($addressSel.size() == 0 || $dbNameSel.size() == 0) {
		return;
	}
	var url = CTX_PATH + '/db/list';
	$dbNameSel.attr({disabled: true});
	$addressSel.on('change', function(){
		if(!this.value) {
			$dbNameSel.attr({disabled: true});
			$dbNameSel.val('');
			return;
		}
		$.getJSON(url, {
			address: this.value
		}, function(data){
			if(!data || !data.dbList) {
				return;
			}
			$dbNameSel.empty().append('<option value="">请选择...</option>');
			var optionContentArr = [];
			$.each(data.dbList, function(i, db){
				var dbName = db.schemaName;
				optionContentArr.push('<option value="' + dbName + '">' + dbName + '</option>');
			});
			$dbNameSel.append(optionContentArr.join(''));
			$dbNameSel.attr({disabled: false});
		});
	});
}

function initClearBtn(){
	var $sqlPre = $('#J_sqlPre'),
		$sampleAddressSel = $('#J_sampleAddress'),
		$targetAddressSel = $('#J_targetAddress');
	$('#J_clearBtn').on('click', function(){
		$sqlPre.empty();
		$sampleAddressSel.val('');
		$sampleAddressSel.trigger('change');
		$targetAddressSel.val('');
		$targetAddressSel.trigger('change');
	});
}
</script>
</head>
<body>
<div style="margin:50px auto; width: 1000px;">
	<div style="text-align:center;">
		<h2 style="font-family: 微软雅黑">数据库结构比较工具</h2>
		<table class="table table-bordered" style="width: 660px; margin: 30px auto 0px;">
			<tbody>
				<tr>
					<td>
						<label style="display: inline-block; margin-left: 15px;">目标数据库地址: </label>
						<select id="J_targetAddress" style="margin-bottom: 0px; width: 180px;">
							<option value="">请选择...</option>
							<option value="localhost">localhost</option>
						</select>
					</td>
					<td>
						<label style="display: inline-block; margin-left: 15px;">目标数据库名称: </label>
						<select id="J_targetDbName" style="margin-bottom: 0px; width: 180px;">
							<option value="">请选择...</option>
							<option value="miaov1">miaov1</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<label style="display: inline-block; margin-left: 15px;">范本数据库地址: </label>
						<select id="J_sampleAddress" style="margin-bottom: 0px; width: 180px;">
							<option value="">请选择...</option>
							<option value="localhost">localhost</option>
						</select>
					</td>
					<td>
						<label style="display: inline-block; margin-left: 15px;">范本数据库名称: </label>
						<select id="J_sampleDbName" style="margin-bottom: 0px; width: 180px;">
							<option value="">请选择...</option>
							<option value="miaov2">miaov2</option>
						</select>
					</td>
				</tr>
				<tr>
					<td colspan="4" style="text-align: center;">
						<button id="J_compareBtn" class="btn btn-primary">比较差异</button>
						&nbsp;&nbsp;
						<button id="J_clearBtn" class="btn">&nbsp;清&nbsp;&nbsp;除&nbsp;</button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<hr/>
	<div style="margin-top: 30px;">
		<pre id="J_sqlPre" style="height: 550px; overflow-y:auto;"></pre>
	</div>
</div>
</body>
</html>