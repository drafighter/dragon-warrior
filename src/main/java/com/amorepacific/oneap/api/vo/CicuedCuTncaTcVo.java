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
 * Date   	          : 2021. 9. 15..
 * Description       : OMNIMP v1 옴니회원플랫폼 구축 프로젝트.
 * </pre>
 */
package com.amorepacific.oneap.api.vo;

import lombok.Data;

/**
 * <pre>
 * com.amorepacific.oneap.api.vo 
 *    |_ CicuedCuTncaTcVo.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 9. 15.
 * @version : 1.0
 * @author  : takkies
 */
@Data
public class CicuedCuTncaTcVo {
	
	private String tcatCd;  //약관유형관리코드
	private String tncvNo="1"; //개정버전번호
	private String tncAgrYn="Y"; //약관동의 여부

	private String fscrId="AC927717";
	private String lschId="AC927717";
	
}
