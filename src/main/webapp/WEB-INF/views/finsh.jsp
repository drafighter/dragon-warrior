<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.8.8/i18n/jquery.ui.datepicker-ko.js"></script>
<script>
	function makeSubmit() {
		var f = document.make;
		var str;

		str = document.getElementById("chCd").value;
		str = str.trim();
		if (!str) {
			alert("채널 코드를 입력하세요.");
			f.chCd.focus();
			return false;
		}
		str = document.getElementById("file1").value;
		str = str.trim();
		if (!str) {
			alert("파일을 선택해주세요.");
			f.chCd.focus();
			return false;
		}
	}
</script>
</head>
<body>
	<form class="make" id="make" name="make" method="post"
		action="/makeUsers" onsubmit="return makeSubmit();"
		enctype="multipart/form-data">
		<div>
			<div style="margin: 20px;">
				<div>
					<p style="font-size: 20px;">[임시 계정 생성 완료]<sub> - (바탕화면에 생성)</sub></p>
				</div>
				채널 코드 : <input type="text" id="chCd" name="chCd"> <input type="checkbox" id="offlinecheck" name="offlinecheck" value="check">오프라인<br> <br>
				파일(바탕화면 파일로 선택) : <input type="file" id="file1" name="file1" title="첨부파일"
					accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" /><br>
				<br> <input type="submit" value="테스트 데이터 생성하기">
			</div>

		</div>


	</form>
</body>
</html>