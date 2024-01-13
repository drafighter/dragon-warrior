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
 * Date   	          : 2021. 9. 14..
 * Description       : OMNIMP v1 옴니회원플랫폼 구축 프로젝트.
 * </pre>
 */
package com.amorepacific.oneap.api.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amorepacific.oneap.api.vo.CicuedCuChCsTcVo;
import com.amorepacific.oneap.api.vo.CicuedCuTncaTcVo;
import com.amorepacific.oneap.api.vo.CicuemCuAdtInfTcVo;
import com.amorepacific.oneap.api.vo.CicuemCuInfTotTcVo;
import com.amorepacific.oneap.api.vo.CicuemCuOptiCsTcVo;
import com.amorepacific.oneap.api.vo.wso2RestApiGetUserVo;
import com.amorepacific.oneap.api.vo.onlinesign;
import com.amorepacific.oneap.excelwrite.vo.UmUserData;
import com.amorepacific.oneap.test.service.testService;
import com.amorepacific.oneap.test.vo.Occued_chcs;
import com.amorepacific.oneap.test.vo.Occued_cust;
import com.amorepacific.oneap.test.vo.UserData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * com.amorepacific.oneap.api.web 
 *    |_ apiController.java
 * </pre>
 *
 * @desc :
 * @date : 2021. 9. 14.
 * @version : 1.0
 * @author : takkies
 */
@Slf4j
@Controller
public class apiController {

	@Autowired
	testService testService;

	@PostMapping("/makeUsers")
	public String makeUsers(HttpServletRequest request, Model model,@RequestParam("file1") MultipartFile file1,@RequestParam(value = "offlinecheck", required = false, defaultValue = "0") List<String> offline) {

        String originFileName = file1.getOriginalFilename();
        
        // String path = UserAPIController.class.getResource("").getPath();
		String path = System.getProperty("user.home")+"/Desktop/";
		String chCd = request.getParameter("chCd");

		try {
			File file = new File(path + originFileName);
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			FileOutputStream outStream = null;

			int rowindex = 0;

			int sheetCn = workbook.getNumberOfSheets(); // 시트 수

			for (int s = 0; s < sheetCn; s++) { // 시트 수만큼 반복
				ArrayList<UmUserData> list = new ArrayList<UmUserData>();
				if (workbook.getSheetName(s).equals("test_1")) { // test_1일 경우
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 Ci 오류 : " + userData.getUserCi());
							return "error";
						}
						list.add(userData);
					}

					// test
					ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
					CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
					cidCvo1.setChCd("000");
					cidCvo1.setPrtnNm("아모레퍼시픽");
					// cidCvo1.setUserPwdEc(getEncodedWso2Password("test1234"));
					volist.add(cidCvo1);
					CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
					cidCvo2.setChCd("083");
					cidCvo2.setPrtnNm("KT CLIP");
					// cidCvo2.setUserPwdEc(getEncodedWso2Password("test1234"));
					volist.add(cidCvo2);

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd("083");
					tclist.add(test33);

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd("083");

						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						// CicuedCuAddrTcVo test4 = new CicuedCuAddrTcVo();
						// cVo.setCicuedCuAddrTcVo(test4);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, "083", cVo); // 고객통합 admin

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
							}
						} else {
							adminnum = adnum;
						}

						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}

				} else if (workbook.getSheetName(s).equals("test_2")) { // test_1일 경우
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserCiError(String.valueOf(row.getCell(5)));

						list.add(userData);
					}

					// test
					ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
					CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
					cidCvo1.setChCd("000");
					cidCvo1.setPrtnNm("아모레퍼시픽");
					// cidCvo1.setUserPwdEc(getEncodedWso2Password("test1234"));
					volist.add(cidCvo1);
					CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
					cidCvo2.setChCd("083");
					cidCvo2.setPrtnNm("KT CLIP");
					// cidCvo2.setUserPwdEc(getEncodedWso2Password("test1234"));
					volist.add(cidCvo2);

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd("083");
					tclist.add(test33);

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCiError()); // CI 불일치 값등록
						cVo.setJoinChCd("083");

						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, "083", cVo); // 고객통합 admin

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
							}
						} else {
							adminnum = adnum;
						}

						sheet.getRow(nowrow).createCell(6).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}

				} else if (workbook.getSheetName(s).equals("test_3")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사  
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(6)));
						list.add(userData);
					}
					if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
						sheet.getRow(1).createCell(6).setCellValue("");
					}
					
					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd(chCd);
					tclist.add(test33);

					for (UmUserData a : list) {
						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);
						
						// test
						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객통합 admin

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId());
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
							}
						} else {
							adminnum = adnum;
																					
						}

						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
							sheet.getRow(nowrow).createCell(6).setCellValue("");
						}
						log.debug("@@@@@@@"+offline);
						nowrow += 1;
						

						
					}

				} else if (workbook.getSheetName(s).equals("test_4")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(6)));

						list.add(userData);
					}

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
//					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
//					test33.setChCd(chCd);
//					tclist.add(test33);						

					for (UmUserData a : list) {

						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd("000");
						
						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
//						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
//						cidCvo2.setChCd(chCd);
//						cidCvo2.setPrtnNm(readchCd(chCd));
//						cidCvo2.setChcsNo(a.getUserId());
//						volist.add(cidCvo2);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						// CicuedCuAddrTcVo test4 = new CicuedCuAddrTcVo();
						// cVo.setCicuedCuAddrTcVo(test4);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, "000", cVo); // 고객 통합 api
						
						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								// 뷰티 포인트 api
								onlinesign onlinesign = new onlinesign();
								onlinesign.setIncsNo(adminnum);
								onlinesign.setCstmid(a.getUserId());
								String result = beautyApi(onlinesign);
								if (result.indexOf("정상 가입") < 0) {
									sheet.getRow(nowrow).createCell(7).setCellValue("실패, " + result);
								}
								//옴니 api
								omniapi(a, adminnum);
							}
						} else {
							adminnum = adnum;
						}						
						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}

				} else if (workbook.getSheetName(s).equals("test_5")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(6)));

						list.add(userData);
					}

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test31 = new CicuemCuOptiCsTcVo();
					test31.setChCd(chCd);
					tclist.add(test31);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd("031");
					tclist.add(test33);
					CicuemCuOptiCsTcVo test34 = new CicuemCuOptiCsTcVo();
					test34.setChCd("043");
					tclist.add(test34);
					CicuemCuOptiCsTcVo test35 = new CicuemCuOptiCsTcVo();
					test35.setChCd("070");
					tclist.add(test35);
					CicuemCuOptiCsTcVo test36 = new CicuemCuOptiCsTcVo();
					test36.setChCd("100");
					tclist.add(test36);
					CicuemCuOptiCsTcVo test37 = new CicuemCuOptiCsTcVo();
					test37.setChCd("099");
					tclist.add(test37);
					

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);
						
						// test
						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						CicuedCuChCsTcVo cidCvo4 = new CicuedCuChCsTcVo();
						cidCvo4.setChCd("031");
						cidCvo4.setPrtnNm("APMall");
						cidCvo4.setChcsNo(a.getUserId());
						volist.add(cidCvo4);
						CicuedCuChCsTcVo cidCvo5 = new CicuedCuChCsTcVo();
						cidCvo5.setChCd("043");
						cidCvo5.setPrtnNm("리리코스마린플러스");
						cidCvo5.setChcsNo(a.getUserId());
						volist.add(cidCvo5);
						CicuedCuChCsTcVo cidCvo6 = new CicuedCuChCsTcVo();
						cidCvo6.setChCd("070");
						cidCvo6.setPrtnNm("방판APP");
						cidCvo6.setChcsNo(a.getUserId());
						volist.add(cidCvo6);
						CicuedCuChCsTcVo cidCvo7 = new CicuedCuChCsTcVo();
						cidCvo7.setChCd("100");
						cidCvo7.setPrtnNm("아모레 성수");
						cidCvo7.setChcsNo(a.getUserId());
						volist.add(cidCvo7);
						CicuedCuChCsTcVo cidCvo8 = new CicuedCuChCsTcVo();
						cidCvo8.setChCd("099");
						cidCvo8.setPrtnNm("에스트라 쇼핑몰");
						cidCvo8.setChcsNo(a.getUserId());
						volist.add(cidCvo8);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						// CicuedCuAddrTcVo test4 = new CicuedCuAddrTcVo();
						// cVo.setCicuedCuAddrTcVo(test4);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객 통합 api

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId()+"C");
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
								// 뷰티 포인트 api
								onlinesign onlinesign = new onlinesign();
								onlinesign.setIncsNo(adminnum);
								onlinesign.setCstmid(a.getUserId());
								String result = beautyApi(onlinesign);
								if (result.indexOf("정상 가입") < 0) {
									sheet.getRow(nowrow).createCell(7).setCellValue("실패, " + result);
								}
								//옴니 api
								omniapi(a, adminnum);
							}
						} else {
							adminnum = adnum;
						}						
						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}

				} else if (workbook.getSheetName(s).equals("test_6")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(6)));

						list.add(userData);
					}

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test31 = new CicuemCuOptiCsTcVo();
					test31.setChCd(chCd);
					tclist.add(test31);
					CicuemCuOptiCsTcVo test32 = new CicuemCuOptiCsTcVo();
					test32.setChCd("030");
					tclist.add(test32);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd("031");
					tclist.add(test33);
					CicuemCuOptiCsTcVo test34 = new CicuemCuOptiCsTcVo();
					test34.setChCd("043");
					tclist.add(test34);
					CicuemCuOptiCsTcVo test35 = new CicuemCuOptiCsTcVo();
					test35.setChCd("070");
					tclist.add(test35);
					CicuemCuOptiCsTcVo test36 = new CicuemCuOptiCsTcVo();
					test36.setChCd("100");
					tclist.add(test36);
					CicuemCuOptiCsTcVo test37 = new CicuemCuOptiCsTcVo();
					test37.setChCd("099");
					tclist.add(test37);
					
					
					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);
						
						// test
						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						CicuedCuChCsTcVo cidCvo3 = new CicuedCuChCsTcVo();
						cidCvo3.setChCd("030");
						cidCvo3.setPrtnNm("APMall");
						cidCvo3.setChcsNo(a.getUserId()+"A");
						volist.add(cidCvo3);
						CicuedCuChCsTcVo cidCvo4 = new CicuedCuChCsTcVo();
						cidCvo4.setChCd("031");
						cidCvo4.setPrtnNm("APMall");
						cidCvo4.setChcsNo(a.getUserId()+"A");
						volist.add(cidCvo4);
						CicuedCuChCsTcVo cidCvo5 = new CicuedCuChCsTcVo();
						cidCvo5.setChCd("043");
						cidCvo5.setPrtnNm("리리코스마린플러스");
						cidCvo5.setChcsNo(a.getUserId()+"B");
						volist.add(cidCvo5);
						CicuedCuChCsTcVo cidCvo6 = new CicuedCuChCsTcVo();
						cidCvo6.setChCd("070");
						cidCvo6.setPrtnNm("방판APP");
						cidCvo6.setChcsNo(a.getUserId());
						volist.add(cidCvo6);
						CicuedCuChCsTcVo cidCvo7 = new CicuedCuChCsTcVo();
						cidCvo7.setChCd("100");
						cidCvo7.setPrtnNm("아모레 성수");
						cidCvo7.setChcsNo(a.getUserId());
						volist.add(cidCvo7);
						CicuedCuChCsTcVo cidCvo8 = new CicuedCuChCsTcVo();
						cidCvo8.setChCd("099");
						cidCvo8.setPrtnNm("에스트라 쇼핑몰");
						cidCvo8.setChcsNo(a.getUserId());
						volist.add(cidCvo8);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객 통합 api

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd("036");
									chcs.setChcs_web_id(a.getUserId()+"C");
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd("036");
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd("036");
									testService.addcust(cust2);
								}
								// 뷰티 포인트 api 는 안함

								//옴니api
								String b=a.getUserId();
								UmUserData a1 = a;
								UmUserData a2 = a;
								a1.setUserId(a.getUserId()+"A");
								omniapi(a1, adminnum);
								a2.setUserId(b+"B");
								omniapi(a2, adminnum);
							}
						} else {
							adminnum = adnum;
						}						
						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}
				
				} else if (workbook.getSheetName(s).equals("test_7")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(6)));

						list.add(userData);
					}
					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test31 = new CicuemCuOptiCsTcVo();
					test31.setChCd(chCd);
					tclist.add(test31);
					/*
					CicuemCuOptiCsTcVo test32 = new CicuemCuOptiCsTcVo();
					test32.setChCd("030");
					tclist.add(test32);
					*/
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd("031");
					tclist.add(test33);
					CicuemCuOptiCsTcVo test34 = new CicuemCuOptiCsTcVo();
					test34.setChCd("043");
					tclist.add(test34);
					CicuemCuOptiCsTcVo test35 = new CicuemCuOptiCsTcVo();
					test35.setChCd("070");
					tclist.add(test35);
					CicuemCuOptiCsTcVo test36 = new CicuemCuOptiCsTcVo();
					test36.setChCd("100");
					tclist.add(test36);
					CicuemCuOptiCsTcVo test37 = new CicuemCuOptiCsTcVo();
					test37.setChCd("099");
					tclist.add(test37);

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);

						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						/*
						CicuedCuChCsTcVo cidCvo3 = new CicuedCuChCsTcVo();
						cidCvo3.setChCd("030");
						cidCvo3.setPrtnNm("온라인 사이트");
						cidCvo3.setChcsNo(a.getUserId());
						volist.add(cidCvo3);
						*/
						CicuedCuChCsTcVo cidCvo4 = new CicuedCuChCsTcVo();
						cidCvo4.setChCd("031");
						cidCvo4.setPrtnNm("APMall");
						cidCvo4.setChcsNo(a.getUserId());
						volist.add(cidCvo4);
						CicuedCuChCsTcVo cidCvo5 = new CicuedCuChCsTcVo();
						cidCvo5.setChCd("043");
						cidCvo5.setPrtnNm("리리코스마린플러스");
						cidCvo5.setChcsNo(a.getUserId());
						volist.add(cidCvo5);
						CicuedCuChCsTcVo cidCvo6 = new CicuedCuChCsTcVo();
						cidCvo6.setChCd("070");
						cidCvo6.setPrtnNm("방판APP");
						cidCvo6.setChcsNo(a.getUserId());
						volist.add(cidCvo6);
						CicuedCuChCsTcVo cidCvo7 = new CicuedCuChCsTcVo();
						cidCvo7.setChCd("100");
						cidCvo7.setPrtnNm("아모레 성수");
						cidCvo7.setChcsNo(a.getUserId());
						volist.add(cidCvo7);
						CicuedCuChCsTcVo cidCvo8 = new CicuedCuChCsTcVo();
						cidCvo8.setChCd("099");
						cidCvo8.setPrtnNm("에스트라 쇼핑몰");
						cidCvo8.setChcsNo(a.getUserId());
						volist.add(cidCvo8);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						// CicuedCuAddrTcVo test4 = new CicuedCuAddrTcVo();
						// cVo.setCicuedCuAddrTcVo(test4);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객 통합 api

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId()+"C");
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
								// 뷰티 포인트 api
								onlinesign onlinesign = new onlinesign();
								onlinesign.setIncsNo(adminnum);
								onlinesign.setCstmid(a.getUserId());
								String result = beautyApi(onlinesign);
								if (result.indexOf("정상 가입") < 0) {
									sheet.getRow(nowrow).createCell(7).setCellValue("실패, " + result);
								}
								//옴니 api
								omniapi(a, adminnum);
							}
						} else {
							adminnum = adnum;
						}						
						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}
				} else if (workbook.getSheetName(s).equals("test_8")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserNameError(String.valueOf(row.getCell(2)));
						userData.setUserBirth(String.valueOf(row.getCell(3)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(4)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(5)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(7)));
						list.add(userData);
					}
					if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
						sheet.getRow(1).createCell(7).setCellValue("");
					}

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd(chCd);
					tclist.add(test33);

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm("불일치");
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);

						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객통합 admin

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId());
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
							}
						} else {
							adminnum = adnum;
						}

						sheet.getRow(nowrow).createCell(6).setCellValue(adminnum); // 리턴된 통합번호
						if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
							sheet.getRow(nowrow).createCell(7).setCellValue("");
						}
						nowrow += 1;
					}
				} else if (workbook.getSheetName(s).equals("test_9")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserNameError(String.valueOf(row.getCell(2)));
						userData.setUserBirth(String.valueOf(row.getCell(3)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserBirthError(String.valueOf(row.getCell(4)));
						userData.setUserPhone(String.valueOf(row.getCell(5)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(6)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(8)));
						list.add(userData);
					}
					if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
						sheet.getRow(1).createCell(8).setCellValue("");
					}

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd(chCd);
					tclist.add(test33);

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm("불일치");
						cVo.setAthtDtbr(a.getUserBirthError());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);
						
						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						// CicuedCuAddrTcVo test4 = new CicuedCuAddrTcVo();
						// cVo.setCicuedCuAddrTcVo(test4);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객통합 admin

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId());
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
							}
						} else {
							adminnum = adnum;
						}

						sheet.getRow(nowrow).createCell(7).setCellValue(adminnum); // 리턴된 통합번호
						if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
							sheet.getRow(nowrow).createCell(8).setCellValue("");
						}
						nowrow += 1;
					}

				} else if (workbook.getSheetName(s).equals("test_10")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserCiError(String.valueOf(row.getCell(5)));
						userData.setUserId(String.valueOf(row.getCell(7)));
						list.add(userData);
					}
					if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
						sheet.getRow(1).createCell(7).setCellValue("");
					}
					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd(chCd);
					tclist.add(test33);

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCiError());
						cVo.setJoinChCd(chCd);

						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						// CicuedCuAddrTcVo test4 = new CicuedCuAddrTcVo();
						// cVo.setCicuedCuAddrTcVo(test4);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객통합 admin

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId());
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
							}
						} else {
							adminnum = adnum;
						}

						sheet.getRow(nowrow).createCell(6).setCellValue(adminnum); // 리턴된 통합번호
						if(!(offline==null || offline.isEmpty() || offline.contains("0"))) {
							sheet.getRow(nowrow).createCell(7).setCellValue("");
						}
						nowrow += 1;
					}
				}else if (workbook.getSheetName(s).equals("test_11")) {
					int nowrow = 2;
					// 시트 수
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					for (rowindex = 2; rowindex < rows; rowindex++) {
						UmUserData userData = new UmUserData();
						// 행읽기
						XSSFRow row = sheet.getRow(rowindex);
						userData.setUserName(String.valueOf(row.getCell(1)));
						userData.setUserBirth(String.valueOf(row.getCell(2)));
						// 생년월일 검사
						if (userData.getUserBirth().length() != 8) {
							model.addAttribute("msg", "파일 내 생년월일 오류 : " + userData.getUserBirth());
							return "error";
						}
						userData.setUserPhone(String.valueOf(row.getCell(3)));
						// 휴대폰 번호 검사
						if (userData.getUserPhone().length() != 11) {
							model.addAttribute("msg", "파일 내 휴대폰번호 오류 : " + userData.getUserPhone());
							return "error";
						}
						userData.setUserCi(String.valueOf(row.getCell(4)));
						// CI 검사
						if (userData.getUserCi().length() != 88) {
							model.addAttribute("msg", "파일 내 CI 오류 : " + userData.getUserCi());
							return "error";
						}
						userData.setUserId(String.valueOf(row.getCell(6)));

						list.add(userData);
					}

					// ?CicuedCuTncaTcVo
					ArrayList<CicuedCuTncaTcVo> tntclist = new ArrayList<CicuedCuTncaTcVo>();
					CicuedCuTncaTcVo tntcvo = new CicuedCuTncaTcVo();
					tntcvo.setTcatCd("010");
					tntclist.add(tntcvo);
					CicuedCuTncaTcVo tntcvo2 = new CicuedCuTncaTcVo();
					tntcvo2.setTcatCd("030");
					tntclist.add(tntcvo2);

					ArrayList<CicuemCuOptiCsTcVo> tclist = new ArrayList<CicuemCuOptiCsTcVo>();
					CicuemCuOptiCsTcVo test3 = new CicuemCuOptiCsTcVo();
					test3.setChCd("000");
					tclist.add(test3);
					CicuemCuOptiCsTcVo test31 = new CicuemCuOptiCsTcVo();
					test31.setChCd(chCd);
					tclist.add(test31);
					CicuemCuOptiCsTcVo test33 = new CicuemCuOptiCsTcVo();
					test33.setChCd("031");
					tclist.add(test33);
					CicuemCuOptiCsTcVo test34 = new CicuemCuOptiCsTcVo();
					test34.setChCd("043");
					tclist.add(test34);
					CicuemCuOptiCsTcVo test35 = new CicuemCuOptiCsTcVo();
					test35.setChCd("070");
					tclist.add(test35);
					CicuemCuOptiCsTcVo test36 = new CicuemCuOptiCsTcVo();
					test36.setChCd("100");
					tclist.add(test36);
					CicuemCuOptiCsTcVo test37 = new CicuemCuOptiCsTcVo();
					test37.setChCd("099");
					tclist.add(test37);
					

					for (UmUserData a : list) {

						// test
						CicuemCuInfTotTcVo cVo = new CicuemCuInfTotTcVo();
						cVo.setCustNm(a.getUserName());
						cVo.setAthtDtbr(a.getUserBirth());
						cVo.setCellTidn(a.getUserPhone().substring(0, 3));
						cVo.setCellTexn(a.getUserPhone().substring(3, 7));
						cVo.setCellTlsn(a.getUserPhone().substring(a.getUserPhone().length() - 4));
						cVo.setCiNo(a.getUserCi());
						cVo.setJoinChCd(chCd);
						
						// test
						ArrayList<CicuedCuChCsTcVo> volist = new ArrayList<CicuedCuChCsTcVo>();
						CicuedCuChCsTcVo cidCvo1 = new CicuedCuChCsTcVo();
						cidCvo1.setChCd("000");
						cidCvo1.setPrtnNm("아모레퍼시픽");
						volist.add(cidCvo1);
						CicuedCuChCsTcVo cidCvo2 = new CicuedCuChCsTcVo();
						cidCvo2.setChCd(chCd); // 해당 채널 코드
						cidCvo2.setPrtnNm(readchCd(chCd));
						cidCvo2.setChcsNo(a.getUserId());
						volist.add(cidCvo2);
						CicuedCuChCsTcVo cidCvo4 = new CicuedCuChCsTcVo();
						cidCvo4.setChCd("031");
						cidCvo4.setPrtnNm("APMall");
						cidCvo4.setChcsNo(a.getUserId());
						volist.add(cidCvo4);
						CicuedCuChCsTcVo cidCvo5 = new CicuedCuChCsTcVo();
						cidCvo5.setChCd("043");
						cidCvo5.setPrtnNm("리리코스마린플러스");
						cidCvo5.setChcsNo(a.getUserId());
						volist.add(cidCvo5);
						CicuedCuChCsTcVo cidCvo6 = new CicuedCuChCsTcVo();
						cidCvo6.setChCd("070");
						cidCvo6.setPrtnNm("방판APP");
						cidCvo6.setChcsNo(a.getUserId());
						volist.add(cidCvo6);
						CicuedCuChCsTcVo cidCvo7 = new CicuedCuChCsTcVo();
						cidCvo7.setChCd("100");
						cidCvo7.setPrtnNm("아모레 성수");
						cidCvo7.setChcsNo(a.getUserId());
						volist.add(cidCvo7);
						CicuedCuChCsTcVo cidCvo8 = new CicuedCuChCsTcVo();
						cidCvo8.setChCd("099");
						cidCvo8.setPrtnNm("에스트라 쇼핑몰");
						cidCvo8.setChcsNo(a.getUserId());
						volist.add(cidCvo8);
						cVo.setCicuedCuChCsTcVo(volist);

						CicuemCuAdtInfTcVo test2 = new CicuemCuAdtInfTcVo();
						test2.setPsnDtbr(a.getUserBirth());
						cVo.setCicuemCuAdtInfTcVo(test2);

						cVo.setCicuemCuOptiCsTcVo(tclist);

						cVo.setCicuedCuTncaTcVo(tntclist);

						String adnum = adminapi(a, chCd, cVo); // 고객 통합 api

						log.debug("*** adnum parameter : " + adnum);
						String adminnum = "";
						if (adnum.indexOf("rsltMsg") > 0) {
							adminnum = adnum.substring((adnum.indexOf("rsltMsg") + 10), (adnum.indexOf("cnt") - 3));
							if (adnum.indexOf("정상처리되었습니다") > 0) {
								adminnum = adnum.substring((adnum.indexOf("incsNo") + 9), (adnum.indexOf("custNm") - 3));
								//경로 가입
								if(chCd.equals("036")) {
									Occued_chcs chcs=new Occued_chcs();
									chcs.setCh_cd(chCd);
									chcs.setChcs_web_id(a.getUserId()+"C");
									chcs.setIncs_no(adminnum);
									testService.addChcd(chcs);
									
									Occued_cust cust1=new Occued_cust();
									cust1.setIncs_no(adminnum);
									cust1.setTcat_cd("I01");
									cust1.setChg_ch_cd(chCd);
									testService.addcust(cust1);
									Occued_cust cust2=new Occued_cust();
									cust2.setIncs_no(adminnum);
									cust2.setTcat_cd("I02");
									cust2.setChg_ch_cd(chCd);
									testService.addcust(cust2);
								}
								// 뷰티 포인트 api
								onlinesign onlinesign = new onlinesign();
								onlinesign.setIncsNo(adminnum);
								onlinesign.setCstmid(a.getUserId());
								String result = beautyApi(onlinesign);
								if (result.indexOf("정상 가입") < 0) {
									sheet.getRow(nowrow).createCell(7).setCellValue("실패, " + result);
								}
								//옴니 api
								omniapi(a, adminnum);
							}
						} else {
							adminnum = adnum;
						}						
						sheet.getRow(nowrow).createCell(5).setCellValue(adminnum); // 리턴된 통합번호
						nowrow += 1;
					}

				}
			}

			outStream = new FileOutputStream(path + "테스트데이터2.xlsx");

			workbook.write(outStream);
			//전달 파일 따로 생성
			//sendfilelist();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			model.addAttribute("msg", "파일을 찾을 수 없습니다.");
			return "error";
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "sample";
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
				System.out.println("400:: ");
			} else if (responseCode == 401) {
				System.out.println("401:: X-Auth-Token Header가 잘못됨");
			} else if (responseCode == 500) {
				System.out.println("500:: 서버 에러, 문의 필요");
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

	// 고객통합 api
	public String adminapi(UmUserData userData, String chCd, CicuemCuInfTotTcVo cVo) {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();

			String json = gson.toJson(cVo);

			log.debug("*** json parameter : [{}]", json);

			String msgMap = sendREST("https://on-cit-ddev.amorepacific.com/cip/cit/custmgnt/custmgnt/svc/custinfrcommgnt/createcicuemcuinfrjoin/v1.30", json);
			log.debug("*** 고객통합 api msgMap : [{}]",  msgMap);

			if (msgMap.indexOf("존재하는 고객") > 0) {
				return "존재하는 고객";
			} else if (msgMap.indexOf("약관동의") > 0) {
				return "약관 동의 필요";
			}
			return msgMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "finsh";
	}

	// 뷰티포인트 API 호출
	public String beautyApi(onlinesign onlinesign) {
		try {
			Gson gson = new Gson();
			String json2 = gson.toJson(onlinesign);

			log.debug("*** onlinesign parameter : [{}]", json2);

			String msgMap2 = sendREST("http://10.155.8.16:8207/member/onlinesign.do", json2);
			log.debug("*** 뷰티포인트 msgMap2 : [{}]",  msgMap2);

			if (msgMap2.indexOf("000") > 0) {
				return "정상 가입";
			} else if (msgMap2.indexOf("070") > 0) {
				return "ID 중복";
			} else if (msgMap2.indexOf("010") > 0) {
				return "약관 미동의";
			} else if (msgMap2.indexOf("030") > 0) {
				return "온라인 회원정보 존재";
			} else if (msgMap2.indexOf("050") > 0) {
				return "이미 온라인에 가입된 통합회원";
			} else if (msgMap2.indexOf("060") > 0) {
				return "존재하지 않는 통합고객번호";
			} else if (msgMap2.indexOf("080") > 0) {
				return "ID 형식이 맞지 않음";
			} else {
				return "에러발생";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "가입오류";
	}
	public static String sendOmniREST(String sendUrl) throws IllegalStateException {
		StringBuffer outResult = new StringBuffer();

		try {
			URL url = new URL(sendUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");	//생략시 GET
			conn.setRequestProperty("accept", "application/scim+json");
			conn.setRequestProperty("X-API-KEY", "IBweveYghKeE439odiNwcw==");
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setConnectTimeout(100000);
			conn.setReadTimeout(100000);

			OutputStream os = conn.getOutputStream();
			//os.write(jsonValue.getBytes("UTF-8"));
			os.flush();

			// 보내고 결과값 받기
			int responseCode = conn.getResponseCode();
			StringBuilder sb = new StringBuilder();
			if (responseCode == 400) {
				System.out.println("400:: ");
				return readBody(conn.getErrorStream());
			} else if (responseCode == 401) {
				System.out.println("401");
				return readBody(conn.getErrorStream());
			} else if (responseCode == 500) {
				System.out.println("500");				  
				return readBody(conn.getErrorStream());
			} else { // 성공 후 응답 JSON 데이터받기
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				// throw new Exception(sb.toString());
				return sb.toString();

			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outResult.toString();
	}
	// 옴니 api
		public String omniapi(UmUserData userData, String incsNo) {
			try {
				wso2RestApiGetUserVo omnidata = new wso2RestApiGetUserVo();
				omnidata.setFullName(URLEncoder.encode(userData.getUserName(), "UTF-8"));
				omnidata.setIncsNo(incsNo);
				omnidata.setUserName(userData.getUserId());
				omnidata.setUserPassword("test1234");
				
				log.debug("*** omnidata parameter : [{}]", omnidata);
				String msgMap = sendOmniREST("http://localhost:8083/v1/wso2rest/Users?fullName="+omnidata.getUserName()+"&incsNo="+omnidata.getIncsNo()+"&userName="+omnidata.getUserName()+"&userPassword="+omnidata.getUserPassword());
				log.debug("*** omni  msgMap : [{}]", msgMap);

				return msgMap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "finsh";
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

	public static String readchCd(String chCd) {
		String chName = "";

		if (chCd.equals("036")) {
			chName = "이니스프리 쇼핑몰";
		} else if (chCd.equals("042")) {
			chName = "에스쁘아쇼핑몰";
		} else if (chCd.equals("099")) {
			chName = "에스트라 쇼핑몰";
		} else if (chCd.equals("106")) {
			chName = "일리윤몰";
		} else if (chCd.equals("039")) {
			chName = "오설록온라인몰";
		} else if (chCd.equals("008")) { //오프라인
			chName = "백화점 오설록";
		} else if (chCd.equals("012")) {//오프라인
			chName = "오설록티하우스";
		}else {
			chName = "";
		}

		return chName;
	}

	//요청 파일
	public static void sendfilelist() {
		String path = System.getProperty("user.home")+"/Desktop/";
		try {
			File file = new File(path + "테스트데이터2.xlsx");
			FileInputStream fis = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(fis);

			int sheetCn = workbook.getNumberOfSheets();
			for (int s = 0; s < sheetCn; s++) { // 시트 수만큼 반복
				if (workbook.getSheetName(s).equals("test_7")) {
					FileOutputStream outStream = null;
					
					XSSFWorkbook User = new XSSFWorkbook();
					XSSFSheet xsheet =User.createSheet("휴면계정 처리 list");
							
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					int nums=0;
					for ( int rowindex = 0; rowindex < rows; rowindex++) {
						// 행읽기
						XSSFRow row = sheet.getRow(nums);
						XSSFRow curRow;
						Cell cell = null;
						curRow = xsheet.createRow(nums);
						for(int i = 0;i<7;i++) {
							cell = curRow.createCell(i);
							if(row.getCell(i)!=null) {
								cell.setCellValue(String.valueOf(row.getCell(i)));								
							}else {
							}
						}
						nums++;
					}
					outStream = new FileOutputStream(path + "휴면계정 처리 list.xlsx");
					
					User.write(outStream);
					outStream.close();
				}else if(workbook.getSheetName(s).equals("test_6")) {
					FileOutputStream outStream = null;
					
					XSSFWorkbook User = new XSSFWorkbook();
					XSSFSheet xsheet =User.createSheet("뷰티포인트 ID 2개 list");
							
					//xsheet=workbook.getSheetAt(s);
					XSSFSheet sheet = workbook.getSheetAt(s);
					// 행의 수
					int rows = sheet.getPhysicalNumberOfRows();
					int nums=0;
					for ( int rowindex = 0; rowindex < rows; rowindex++) {
						// 행읽기
						XSSFRow row = sheet.getRow(nums);
						XSSFRow curRow;
						Cell cell = null;
						curRow = xsheet.createRow(nums);
						for(int i = 0;i<9;i++) {
							cell = curRow.createCell(i);
							if(row.getCell(i)!=null) {
								cell.setCellValue(String.valueOf(row.getCell(i)));								
							}else {
							}
						}
						nums++;
					}
					outStream = new FileOutputStream(path + "뷰티포인트 ID 2개 list.xlsx");
					
					User.write(outStream);
					outStream.close();
				}
			}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
}
