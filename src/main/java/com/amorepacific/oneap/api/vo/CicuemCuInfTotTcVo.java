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
package com.amorepacific.oneap.api.vo;


import java.util.List;

import lombok.Data;

/**
 * <pre>
 * com.amorepacific.oneap.api.vo 
 *    |_ CicuemCuInfTotTcVo.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 9. 14.
 * @version : 1.0
 * @author  : takkies
 */
@Data
public class CicuemCuInfTotTcVo {
	/*
	private String custNm;
	private String athtDtbr; //생년월일
	private String frclCd="K";
	private String sxclCd="M";
	
	private String cellTidn; //010
	private String cellTexn;
	private String cellTlsn;
	
	private String ciNo;
	
	private String joinChCd="000"; //가입경로
	private String joinPrtnId="12000515"; //가입 매장 번호
	
	private String atclCd = "10";
	
	private String fscrId="AC927717";
	private String lschId="AC927717";
	 */
	

	
	////////////////////////////////////////////
	/**
	 * 필수: 고객명
	 */
	private String custNm;
	/**
	 * 필수: 생년월일
	 */
	private String athtDtbr;
	/**
	 * 필수: 내외국인구분코드 (내국인 : K, 외국인 : F)
	 */
	private String frclCd="K";
	/**
	 * 필수: 성별구분코드 (여성 : F, 남성: M)
	 */
	private String sxclCd="M";
	private String pmdcCd="";        // 우편물수신처구분코드 (자택 : H, 직장 : C, 기타)
	private String homeZip="";       // 자택우편번호
	private String homeBscsAddr="";  // 자택기본주소
	private String homeDtlAddr="";   // 자택상세주소
	/**
	 * 필수: 휴대폰식별전화번호
	 */
	private String cellTidn; 
	/**
	 * 필수: 휴대폰국전화번호
	 */
	private String cellTexn; 
	/**
	 * 필수: 휴대폰끝전화번호
	 */
	private String cellTlsn; 
	private String custEmid="";      // 고객이메일계정
	private String custEmdn="";      // 고객이메일번지
	/**
	 * 필수: ci번호 (고객의 개인식별 번호)
	 */
	private String ciNo; 
	private String rnarCd="";        // 실명인증결과코드 (COM_CAT_CD : 10155 (1:실명인증성공, 2:실명인증실패, 3: 해당자료없음, 4:통신오류, 5:체크썸오류, 50:명의도용차단가입자))
	private String rnmAthtDt="";     // 실명인증일자
	private String ofcBscsAddr="";   // 직장기본주소
	private String ofcDtlAddr="";    // 직장상세주소
	private String ofcZip="";        // 직장우편번호
	private String addrXcrd="";      // 주소X좌표
	private String addrYcrd="";      // 주소Y좌표
	private String rfrsCd="";        // 정제결과코드
	private String blmnNo="";        // 건물관리번호
	private String adclCd="";        // 주소구분코드 (1 : 입력지번, 2 : 입력도로명, 3 : 표준지번, 4 : 표준도로명)
	private String adocCd="";        // 주소입수구분코드 (1 : 온라인, 2 : 일괄전환, 3 : 대외, 4 : 기타)
	private String drccCd="";        // 휴면고객구분코드 (Y : 휴면, A : 예정)
	private String drcsRegDt="";     // 휴면고객등록일자 (휴면고객으로 등록되는 날짜(휴면고객구분값이 예정인 경우는 휴면고객등록예정일자))
	/**
	 * 필수: 가입경로구분코드 (가입경로 채널코드)
	 */
	private String joinChCd; 
	/**
	 * 필수: 가입거래처ID (가입경로의 매장코드)
	 */
	private String joinPrtnId="12000515"; 
	private String jndvCd;        // 가입디바이스코드 (고객가입시 사용한 매체 관리를 위한 코드(W:WEB, M:MOBILE, A:APP))
	/**
	 * 필수: 휴대폰인증구분코드 (COM_CAT_CD:10139 (10:소유, 20:점유, 30:조정))
	 */
	private String atclCd= "10";
	private String smsNum="";        // 휴대폰인증번호
	
	
	private String newCustJoinRqSiteCd=""; // 옴니는 사용않음 -> 2021-08-31 사이트 코드 경로시스템 (ex: 아이오페) 적용 시 사용
	private String custJndvOsClCd=""; // 고객가입디바이스운영체제구분코드
	private String snsIdPrcnCd=""; // SNS계정제휴사코드
	private String chcsIntgSwtJoinYn=""; // 경로고객통합전환가입여부
	
	/**
	 * 필수: 최초생성ID (API를 호출한 사번)
	 */
	private String fscrId="AC927717";
	/**
	 * 필수: 최종변경ID (API를 호출한 사번)
	 */
	private String lschId="AC927717";
	private String joinCnclYn=""; // 회원가입망취소시 'Y'
	
	
	//가입채널
	private List<CicuedCuChCsTcVo> CicuedCuChCsTcVo;
	//양음력
	private CicuemCuAdtInfTcVo CicuemCuAdtInfTcVo;
	
	private List<CicuedCuTncaTcVo> CicuedCuTncaTcVo;
	
	private List<CicuemCuOptiCsTcVo> CicuemCuOptiCsTcVo;
	
	//private CicuedCuAddrTcVo CicuedCuAddrTcVo;
}
