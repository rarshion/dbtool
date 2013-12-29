<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx_path" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>index</title>
<link type="text/css" rel="stylesheet" href="${ctx_path}/css/bootstrap.min.css" />
<script type="text/javascript" src="${ctx_path}/js/jquery/jquery.js"></script>
<script>
var CTX_PATH = '${ctx_path}';
$(function(){
	initShowSqlBtn();
});

function initShowSqlBtn() {
	var url = CTX_PATH + '/test/getSql',
		$sqlPre = $('#J_sqlPre');
	$('#J_showSqlBtn').on('click', function(){
		$.getJSON(url, function(data){
			$sqlPre.empty();
			var sqlList = data.sqlList;
			if(!sqlList) {
				return;
			}
			$.each(sqlList, function(i, sql){
				$sqlPre.append(sql + "\n");
			})
		});
	});
}
</script>
</head>
<body>
<div style="margin:100px auto; width: 1000px;">
	<div style="text-align:center;">
		<button id="J_showSqlBtn" class="btn btn-primary">获取sql</button>
	</div>
	<div style="margin-top: 30px;">
		<pre id="J_sqlPre" style="height: 350px; overflow-y:auto;	"></pre>
	</div>
</div>
</body>
</html>