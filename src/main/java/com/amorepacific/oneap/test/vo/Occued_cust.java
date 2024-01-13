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
 * Date   	          : 2021. 10. 22..
 * Description       : OMNIMP v1 옴니회원플랫폼 구축 프로젝트.
 * </pre>
 */
package com.amorepacific.oneap.test.vo;

import lombok.Data;

/**
 * <pre>
 * com.amorepacific.oneap.test.vo 
 *    |_ occued_cust.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 10. 22.
 * @version : 1.0
 * @author  : takkies
 */
@Data
public class Occued_cust {
	private String incs_no; //고객통합번호
	private String tcat_cd; //IO1, IO2 
	private String chg_ch_cd; //채널코드
	private String tncv_no="1";
	private String fscr_id="OCP";
	private String lsch_id="OCP";

}
