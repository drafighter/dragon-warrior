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


function formsubmit(){
	var f = document.writeSubmit;
	var str;
	
	str = document.getElementById("userName").value
	str = str.trim();
	if(!str) {
		alert("생성 이름을 입력하세요.");
		f.userName.focus();
		return false;
	}
	if (str.length<2 || str.length>4) {
        alert("이름을 2~4자까지 입력해주세요.")
        f.userName.focus();
        return false;
    }
	
	str = document.getElementById("userId").value
	str = str.trim();
	if(!str) {
		alert("사용 아이디를 입력하세요.");
		f.userId.focus();
		return false;
	}	
	
	if (str.length<3 || str.length>11) {
        alert("아이디를 3~11자까지 입력해주세요.")
        f.userId.focus();
        return false;
    }
	for (var i = 0; i < str.length; i++) {
        ch = str.charAt(i)
        if (!(ch >= '0' && ch <= '9') && !(ch >= 'a' && ch <= 'z')&&!(ch >= 'A' && ch <= 'Z')) {
            alert("아이디는 영문 대소문자, 숫자만 입력가능합니다.")
            f.userId.focus();
            return false;
        }
    }
	str = $("input[name='usertype']").is(":checked");
	if(str == false){
        alert("유형을 선택하세요.");
        return false;
    }
	
	var str1 =$("input[id='usertype1']").is(":checked");
	var str2=document.getElementById("row1").value;
	 if(str2==""){
		 $("#row1").val("0");
		 str2=document.getElementById("row1").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype1").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row1").focus();
		return false;
	}
	 str1 =$("input[id='usertype2']").is(":checked");
	 str2=document.getElementById("row2").value;
	 if(str2==""){
		 $("#row2").val("0");
		 str2=document.getElementById("row2").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype2").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row2").focus();
		return false;
	}
	str1 =$("input[id='usertype3']").is(":checked");
	 str2=document.getElementById("row3").value;
	 if(str2==""){
		 $("#row3").val("0");
		 str2=document.getElementById("row3").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype3").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row3").focus();
		return false;
	}
	str1 =$("input[id='usertype4']").is(":checked");
	 str2=document.getElementById("row4").value;
	 if(str2==""){
		 $("#row4").val("0");
		 str2=document.getElementById("row4").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype4").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row4").focus();
		return false;
	}
	str1 =$("input[id='usertype5']").is(":checked");
	 str2=document.getElementById("row5").value;
	 if(str2==""){
		 $("#row5").val("0");
		 str2=document.getElementById("row5").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype5").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row5").focus();
		return false;
	}
	str1 =$("input[id='usertype6']").is(":checked");
	 str2=document.getElementById("row6").value;
	 if(str2==""){
		 $("#row6").val("0");
		 str2=document.getElementById("row6").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype6").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row6").focus();
		return false;
	}
	str1 =$("input[id='usertype7']").is(":checked");
	 str2=document.getElementById("row7").value;
	 if(str2==""){
		 $("#row7").val("0");
		 str2=document.getElementById("row7").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype7").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row7").focus();
		return false;
	}
	str1 =$("input[id='usertype8']").is(":checked");
	 str2=document.getElementById("row8").value;
	 if(str2==""){
		 $("#row8").val("0");
		 str2=document.getElementById("row8").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype8").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row8").focus();
		return false;
	}
	str1 =$("input[id='usertype9']").is(":checked");
	 str2=document.getElementById("row9").value;
	 if(str2==""){
		 $("#row9").val("0");
		 str2=document.getElementById("row9").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype9").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row9").focus();
		return false;
	}
	str1 =$("input[id='usertype10']").is(":checked");
	 str2=document.getElementById("row10").value;
	 if(str2==""){
		 $("#row10").val("0");
		 str2=document.getElementById("row10").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype10").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row10").focus();
		return false;
	}
	str1 =$("input[id='usertype11']").is(":checked");
	 str2=document.getElementById("row11").value;
	 if(str2==""){
		 $("#row11").val("0");
		 str2=document.getElementById("row11").value;
	 }
	if(str1==false && str2 !="0"){
		alert("올바르게 선택해주세요.");
		document.getElementById("usertype11").focus();
		return false;
	}else if(str1==true && str2 =="0"){
		alert("올바르게 입력해주세요.");
		document.getElementById("row11").focus();
		return false;
	}
	

}

</script>

</head>
<body>

<div>
	<div style="height: 30px; "><div style="width: 300px; margin: 10px auto; font-size: 20px;"><p>테스트 데이터 목록 작성</p></div></div>
	<form class="writeSubmit" id="writeSubmit" name="writeSubmit" method="get"  action="/excelWrite" onsubmit="return formsubmit();">
		<div><br>
			<div style="border: 1px solid black; padding: 10px; width: 600px; margin: 10px auto;">
			사용자 이름 : <input type="text" id = "userName"  name ="userName" style="width: 150px;">
			사용 아이디(영문) : <input type="text" id = "userId"  name ="userId"  style="width: 150px;">
			</div><br><br>
			<div style="width:1100px; margin: 10px auto;">
			<input type="checkbox"  id="usertype1" name="usertype" value = "1">타 오프라인 경로(KT CLIP : 083) 가입 고객
			<br>
			건 수 : <input type="number" id = "row1"  name ="row1"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype2" name="usertype" value = "2">타 오프라인 경로(KT CLIP : 083) 가입 고객 - CI(불일치)
			<br>
			건 수 : <input type="number" id = "row2"  name ="row2"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype3" name="usertype" value = "3">해당 경로만 가입된 고객 <sub> (000, 해당 경로) </sub>
			<br>
			건 수 : <input type="number" id = "row3"  name ="row3"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype4" name="usertype" value = "4">경로 첫 방문 뷰티포인트 고객 <sub>(뷰티포인트 + 고객 통합) </sub>
			<br>
			건 수 : <input type="number" id = "row4" name ="row4"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype5" name="usertype" value = "5">이미 가입된 고객 - 뷰티포인트ID가 1개
			<br>
			건 수 : <input type="number" id = "row5"  name ="row5"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype6" name="usertype" value = "6">이미 가입된 고객 - 뷰티포인트ID가 2개이상
			<br>
			건 수 : <input type="number" id = "row6"  name ="row6"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype7" name="usertype" value = "7">이미 가입된 고객 - 휴면상태 정보
			<br>
			건 수 : <input type="number" id = "row7"  name ="row7"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype8" name="usertype" value = "8">경로 자체고객 - CI (일치), 고객명 (불일치), 생년월일 (일치), 휴대폰번호 (일치)
			<br>
			건 수 : <input type="number" id = "row8"  name ="row8"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"  id="usertype9"  name="usertype" value = "9">경로 자체고객 - CI (일치), 고객명 (불일치), 생년월일 (불일치)
			<br>
			건 수 : <input type="number" id = "row9"  name ="row9"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"   id="usertype10" name="usertype" value = "10">경로 자체고객 - CI (불일치), 고객명 (일치), 생년월일 (일치), 휴대폰번호 (일치)
			<br>
			건 수 : <input type="number" id = "row10"  name ="row10"  value="0" min="0">
			<br>
			<br>
			<input type="checkbox"   id="usertype11" name="usertype" value = "11">휴대폰 로그인 한 고객 뷰티포인트ID가 1개 (휴대폰번호 가운데 0000)
			<br>
			건 수 : <input type="number" id = "row11"  name ="row11"  value="0" min="0">
			<br>
			<br>
			
			<input type="submit" value="생성하기"  style="width: 100px; height: 30px; margin-left: 20px; ">
			</div>
			<div style="height: 50px;"></div>
		
		</div>
	</form>




</div>









</body>
</html>