/*
 * <pre>
 * Copyright (c) 2020 Amore Pacific.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Amore
 * Pacific. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Amore Pacific.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author	          : takkies
 * Date   	          : 2021. 9. 12..
 * Description       : OMNIMP v1 옴니회원플랫폼 구축 프로젝트.
 * </pre>
 */
package com.amorepacific.oneap.excelwrite.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.amorepacific.oneap.excelwrite.vo.UmUserData;
import com.amorepacific.oneap.excelwrite.vo.idCheck;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * com.amorepacific.oneap.excelwrite.web 
 *    |_ UserAPIController.java
 * </pre>
 *
 * @desc :
 * @date : 2021. 9. 12.
 * @version : 1.0
 * @author : takkies
 */
@Slf4j
@Controller
public class UserAPIController {

	@GetMapping("/makeExcel")
	public String makeExcel() {

		return "writeExcel";
	}

	@RequestMapping(value = "/excelWrite")
	public String writeExcel(HttpServletRequest request, ArrayList<UmUserData> list, UmUserData userData, Model model, @RequestParam(value = "usertype", required = false, defaultValue = "0") List<String> usertype) {
		// String path = UserAPIController.class.getResource("").getPath(); // 저장할 파일 경로
		String path = System.getProperty("user.home")+"/Desktop/";
		
		String row11=request.getParameter("row1");
		String row22=request.getParameter("row2");
		String row33=request.getParameter("row3");
		String row44=request.getParameter("row4");
		String row55=request.getParameter("row5");
		String row66=request.getParameter("row6");
		String row77=request.getParameter("row7");
		String row88=request.getParameter("row8");
		String row99=request.getParameter("row9");
		String row1010=request.getParameter("row10");
		String row1111=request.getParameter("row11");
		
		boolean rowscheck=  (row11==null || row11.equals("") || row11.equals("null")) || (row22==null || row22.equals("") || row22.equals("null")) || (row33==null || row33.equals("") || row33.equals("null"))
				|| (row44==null || row44.equals("") || row44.equals("null"))|| (row55==null || row55.equals("") || row55.equals("null"))|| (row66==null || row66.equals("") || row66.equals("null"))
				|| (row77==null || row77.equals("") || row77.equals("null"))|| (row88==null || row88.equals("") || row88.equals("null"))|| (row99==null || row99.equals("") || row99.equals("null"))
				|| (row1010==null || row1010.equals("") || row1010.equals("null") || (row1111==null || row1111.equals("") || row1111.equals("null")));
		if(usertype==null || usertype.isEmpty() || usertype.contains("0") || rowscheck==true) {
			model.addAttribute("msg", "잘못된 접근입니다.");
			return "error";
		}
		int row1 = Integer.parseInt(request.getParameter("row1"));
		int row2 = Integer.parseInt(request.getParameter("row2"));
		int row3 = Integer.parseInt(request.getParameter("row3"));
		int row4 = Integer.parseInt(request.getParameter("row4"));
		int row5 = Integer.parseInt(request.getParameter("row5"));
		int row6 = Integer.parseInt(request.getParameter("row6"));
		int row7 = Integer.parseInt(request.getParameter("row7"));
		int row8 = Integer.parseInt(request.getParameter("row8"));
		int row9 = Integer.parseInt(request.getParameter("row9"));
		int row10 = Integer.parseInt(request.getParameter("row10"));
		int row111 = Integer.parseInt(request.getParameter("row11"));
		int row = row1 + row2 + row3 + row4 + row5 + row6 + row7 + row8 + row9 + row10+row111;
		
		rowscheck = row1>=0 && row2>=0 && row3>=0 && row4>=0 && row5>=0 && row6>=0 && row7>=0 && row8>=0 && row9>=0 && row10>=0 && row111>=0  && row>0;
		if(rowscheck==false) {
			model.addAttribute("msg", "잘못된 접근입니다.");
			return "error";
		}
		// 전체 데이터 수
		try {
			int userIdnum = 1;
			// 랜덤 list
			for (int i = 0; i < row; i++) {
				UmUserData umUser = new UmUserData();
				// 사용자 이름 생성
				umUser.setUserName(request.getParameter("userName"));
				umUser.setUserNameError("불일치");
				// 랜덤 날짜 생성
				String afterDate = randomBirth();
				umUser.setUserBirth(afterDate);
				// 리스트 날짜 중복 검색
				goback: for (int n = 0; n < list.size(); n++) {
					if (list.get(n).getUserBirth() != afterDate) {
						umUser.setUserBirth(afterDate); // 자바 랜던 날짜
					} else {
						afterDate = randomBirth();
						break goback;
					}
				}
				// 날짜 불일치
				String birthError = randomBirth();
				umUser.setUserBirthError(birthError);
				goback1: for (int n = 0; n < list.size(); n++) {
					if (list.get(n).getUserBirthError() != birthError) {
						umUser.setUserBirthError(birthError); // 자바 랜던 날짜
					} else {
						birthError = randomBirth();
						break goback1;
					}
				}

				// 랜덤 핸드폰 번호
				String phoneNumber = "010" + ThreadLocalRandom.current().nextInt(10000000, 99999999);
				umUser.setUserPhone(phoneNumber);
				goback2: for (int n = 0; n < list.size(); n++) {
					if (list.get(n).getUserPhone() != phoneNumber) {
						umUser.setUserPhone(phoneNumber); // 자바 랜던 날짜
					} else {
						phoneNumber = "010" + ThreadLocalRandom.current().nextInt(10000000, 99999999);
						break goback2;
					}
				}

				// 랜덤 CI
				String value = randomCi();
				umUser.setUserCi(value + "==");
				goback3: for (int n = 0; n < list.size(); n++) {
					if (list.get(n).getUserCi() != value) {
						umUser.setUserCi(value + "==");
					} else {
						value = randomCi();
						break goback3;
					}
				}
				// 랜덤 CI 불일치
				String errorCi = randomCi();
				umUser.setUserCiError(errorCi + "==");
				goback3_1: for (int n = 0; n < list.size(); n++) {
					if (list.get(n).getUserCi() != errorCi) {
						if (list.get(n).getUserCiError() != errorCi) {
							umUser.setUserCiError(errorCi + "==");
						} else {
							value = randomCi();
							break goback3_1;
						}
					} else {
						value = randomCi();
						break goback3_1;
					}
				}
				String userId = request.getParameter("userId") + userIdnum;
				umUser.setUserId(userId);
				if (row4 > 0 || row5 > 0 || row6 > 0 || row7 > 0) {
					if (idcheck(userId).indexOf("NOK") > 0) {
						model.addAttribute("msg", "뷰티포인트 아이디 중복으로 다른 아이디를 입력하세요.");// 뷰포 시에
						return "error";
					}
				}
				userIdnum += 1;
				list.add(umUser);
			}
//			Calendar cal = Calendar.getInstance();
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//			String time = formatter.format(cal.getTime());
			File file = new File(path + "테스트데이터.xlsx");
			FileOutputStream fileout = new FileOutputStream(file);

			XSSFWorkbook User = new XSSFWorkbook();

			for (String t : usertype) {
				XSSFSheet xsheet = User.createSheet("test_" + t); // 시트 생성
				XSSFRow curRow;
				Cell cell = null;

				// Title @@@@채널코드 입력
				curRow = xsheet.createRow(0);
				cell = curRow.createCell(0);
				if (t.equals("1")) {
					cell.setCellValue("타 오프라인 경로(KT CLIP : 083) 가입 고객");
				} else if (t.equals("2")) {
					cell.setCellValue("타 오프라인 경로(KT CLIP : 083) 가입 고객 - CI는 다르지만 이름+생년월일+휴대폰번호 일치 정보");
				} else if (t.equals("3")) {
					cell.setCellValue("해당 경로만 가입된 고객");
				} else if (t.equals("4")) {
					cell.setCellValue("경로 첫 방문 뷰티포인트 고객");
				} else if (t.equals("5")) {
					cell.setCellValue("이미 가입된 고객 - 뷰티포인트ID가 1개");
				} else if (t.equals("6")) {
					cell.setCellValue("이미 가입된 고객 - 뷰티포인트ID가 2개이상");
				} else if (t.equals("7")) {
					cell.setCellValue("이미 가입된 고객 - 휴면상태 정보");
				} else if (t.equals("8")) {
					cell.setCellValue("경로 자체고객 - CI (일치), 고객명 (불일치), 생년월일 (일치), 휴대폰번호 (일치)");
				} else if (t.equals("9")) {
					cell.setCellValue("경로 자체고객 - CI (일치), 고객명 (불일치), 생년월일 (불일치)");
				} else if (t.equals("10")) {
					cell.setCellValue("경로 자체고객 - CI (불일치), 고객명 (일치), 생년월일 (일치), 휴대폰번호 (일치)");
				} else if (t.equals("11")) {
					cell.setCellValue("휴대폰 로그인 한 고객 뷰티포인트ID가 1개 (휴대폰번호 가운데 0000)");
				}

				if (t.equals("1")) {
					int start = 0;
					for (int i = 2; i < row1 + 2; i++) {
						curRow = xsheet.createRow(1);
						cell = curRow.createCell(1);
						cell.setCellValue("이름");
						cell = curRow.createCell(2);
						cell.setCellValue("생년월일");
						cell = curRow.createCell(3);
						cell.setCellValue("휴대폰번호");
						cell = curRow.createCell(4);
						cell.setCellValue("CI");
						cell = curRow.createCell(5);
						cell.setCellValue("고객통합번호");

						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						start += 1;
					}
				} else if (t.equals("2")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");
					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");
					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");
					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("CI불일치");
					cell = curRow.createCell(6);
					cell.setCellValue("고객통합번호");
					int start = row1;
					for (int i = 2; i < row2 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(5);
						cell.setCellValue(list.get(start).getUserCiError());
						start += 1;
					}
				} else if (t.equals("3")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");

					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");

					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");

					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("고객통합번호");

					cell = curRow.createCell(6);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2;
					for (int i = 2; i < row3 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserId());

						start += 1;
					}
				} else if (t.equals("4")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");

					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");

					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");

					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("고객통합번호");
					cell = curRow.createCell(6);
					cell.setCellValue("뷰티포인트");
					int start = row1 + row2 + row3;
					for (int i = 2; i < row4 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserId());
						start += 1;
					}
				} else if (t.equals("5")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");

					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");

					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");

					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("고객통합번호");
					cell = curRow.createCell(6);
					cell.setCellValue("뷰티포인트");
					cell = curRow.createCell(7);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2 + row3 + row4;
					for (int i = 2; i < row5 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserId());
						cell = curRow.createCell(7);
						cell.setCellValue(list.get(start).getUserId()+"C");
						start += 1;
					}
				} else if (t.equals("6")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");

					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");

					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");

					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("고객통합번호");
					cell = curRow.createCell(6);
					cell.setCellValue("뷰티포인트");
					cell = curRow.createCell(7);
					cell.setCellValue("뷰티포인트1");
					cell = curRow.createCell(8);
					cell.setCellValue("뷰티포인트2");
					cell = curRow.createCell(9);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2 + row3 + row4 + row5;
					for (int i = 2; i < row6 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserId());
						cell = curRow.createCell(7);
						cell.setCellValue(list.get(start).getUserId() + "A");
						cell = curRow.createCell(8);
						cell.setCellValue(list.get(start).getUserId() + "B");
						cell = curRow.createCell(9);
						cell.setCellValue(list.get(start).getUserId() + "C");

						start += 1;
					}
				} else if (t.equals("7")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");

					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");

					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");

					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("고객통합번호");
					cell = curRow.createCell(6);
					cell.setCellValue("뷰티포인트");
					cell = curRow.createCell(7);
					cell.setCellValue("경로고객ID");

					int start = row1 + row2 + row3 + row4 + row5 + row6;
					for (int i = 2; i < row7 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserId());
						cell = curRow.createCell(7);
						cell.setCellValue(list.get(start).getUserId()+"C");

						start += 1;

					}
				} else if (t.equals("8")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");
					cell = curRow.createCell(2);
					cell.setCellValue("이름불일치");
					cell = curRow.createCell(3);
					cell.setCellValue("생년월일");
					cell = curRow.createCell(4);
					cell.setCellValue("휴대폰번호");
					cell = curRow.createCell(5);
					cell.setCellValue("CI");
					cell = curRow.createCell(6);
					cell.setCellValue("고객통합번호");

					cell = curRow.createCell(7);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2 + row3 + row4 + row5 + row6 + row7;
					for (int i = 2; i < row8 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserNameError());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(5);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(7);
						cell.setCellValue(list.get(start).getUserId());
						start += 1;
					}
				} else if (t.equals("9")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");
					cell = curRow.createCell(2);
					cell.setCellValue("이름 불일치");
					cell = curRow.createCell(3);
					cell.setCellValue("생년월일");
					cell = curRow.createCell(4);
					cell.setCellValue("생년월일 불일치");
					cell = curRow.createCell(5);
					cell.setCellValue("휴대폰번호");
					cell = curRow.createCell(6);
					cell.setCellValue("CI");
					cell = curRow.createCell(7);
					cell.setCellValue("고객통합번호");

					cell = curRow.createCell(8);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2 + row3 + row4 + row5 + row6 + row7 + row8;
					for (int i = 2; i < row9 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserNameError());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserBirthError());
						cell = curRow.createCell(5);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(8);
						cell.setCellValue(list.get(start).getUserId());
						start += 1;
					}
				} else if (t.equals("10")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");
					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");
					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");
					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("CI불일치");
					cell = curRow.createCell(6);
					cell.setCellValue("고객통합번호");
					cell = curRow.createCell(7);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2 + row3 + row4 + row5 + row6 + row7 + row8 + row9;
					for (int i = 2; i < row10 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						cell.setCellValue(list.get(start).getUserPhone());
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(5);
						cell.setCellValue(list.get(start).getUserCiError());
						cell = curRow.createCell(7);
						cell.setCellValue(list.get(start).getUserId());
						start += 1;
					}
				}else if (t.equals("11")) {
					curRow = xsheet.createRow(1);
					cell = curRow.createCell(1);
					cell.setCellValue("이름");

					cell = curRow.createCell(2);
					cell.setCellValue("생년월일");

					cell = curRow.createCell(3);
					cell.setCellValue("휴대폰번호");

					cell = curRow.createCell(4);
					cell.setCellValue("CI");
					cell = curRow.createCell(5);
					cell.setCellValue("고객통합번호");
					cell = curRow.createCell(6);
					cell.setCellValue("뷰티포인트");
					cell = curRow.createCell(7);
					cell.setCellValue("경로고객ID");
					int start = row1 + row2 + row3 + row4 + row5 + row6 + row7 + row8 + row9+row10;
					for (int i = 2; i < row111 + 2; i++) {
						curRow = xsheet.createRow(i); // row 생성

						cell = curRow.createCell(1);
						cell.setCellValue(list.get(start).getUserName());
						cell = curRow.createCell(2);
						cell.setCellValue(list.get(start).getUserBirth());
						cell = curRow.createCell(3);
						String userPhone=list.get(start).getUserPhone();
						String userPhoneafter=userPhone.substring(0,3)+"0000"+userPhone.substring(7,11);
						cell.setCellValue(userPhoneafter);
						cell = curRow.createCell(4);
						cell.setCellValue(list.get(start).getUserCi());
						cell = curRow.createCell(6);
						cell.setCellValue(list.get(start).getUserId());
						cell = curRow.createCell(7);
						cell.setCellValue(list.get(start).getUserId()+"C");
						start += 1;
					}
				}
			}

			User.write(fileout);
			fileout.close();

			return "finsh";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "finsh";

	}

	public String randomBirth() {
		LocalDate currentDate = LocalDate.now();
		int year = (int) (Math.random() * ((currentDate.getYear() - 15) - (currentDate.getYear() - 100)) + (currentDate.getYear() - 100));
		int[] maxDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int iMinMonth = 1;
		int iMaxMonth = 12;
		int iRandomMonth = (int) (Math.random() * iMaxMonth - iMinMonth + 1) + iMinMonth;
		int iRandomDay = (int) (Math.random() * (maxDays[iRandomMonth - 1] - 2) + 1);
		LocalDate predate = LocalDate.of(year, iRandomMonth, iRandomDay);
		String afterDate = predate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return afterDate;
	}

	public String randomCi() {
		String value = "";

		for (int a = 0; a < 86; a++) { // 원하는 난수의 길이
			int rndVal = (int) (Math.random() * 62);
			if (rndVal < 10) {
				value += rndVal;
			} else if (rndVal > 35) {
				value += (char) (rndVal + 61);
			} else {
				value += (char) (rndVal + 55);
			}
		}

		return value;
	}

	public static String sendREST(String sendUrl, String jsonValue) throws IllegalStateException {
		StringBuffer outResult = new StringBuffer();

		try {
			URL url = new URL(sendUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(100000);
			conn.setReadTimeout(100000);

			OutputStream os = conn.getOutputStream();
			os.write(jsonValue.getBytes("UTF-8"));
			os.flush();

			// 보내고 결과값 받기
			int responseCode = conn.getResponseCode();
			if (responseCode == 400) {
				System.out.println("400 ");
			} else if (responseCode == 401) {
				System.out.println("401");
			} else if (responseCode == 500) {
				System.out.println("500:: ");
				return readBody(conn.getErrorStream());
			} else { // 성공 후 응답 JSON 데이터받기
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				// throw new Exception(sb.toString());
				return sb.toString();

			}
			/*
			 * // 리턴된 결과 읽기 BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); while ((inputLine =
			 * in.readLine()) != null) { outResult.append(inputLine); }
			 */
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outResult.toString();
	}

	// 온라인 회원ID 중복 체크 API
	public String idcheck(String id) {
		try {
			Gson gson = new Gson();
			idCheck idCheck = new idCheck();
			idCheck.setId(id);
			String json = gson.toJson(idCheck);

			String msgMap = sendREST("http://10.155.8.16:8207/member/idcheck.do", json);

			return msgMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "error";
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

}
