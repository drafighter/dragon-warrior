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

import lombok.Data;

/**
 * <pre>
 * com.amorepacific.oneap.api.vo 
 *    |_ CicuedCuChCsTcVo.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 9. 14.
 * @version : 1.0
 * @author  : takkies
 */
@Data
public class CicuedCuChCsTcVo {
	private String chCd;
	private String prtnNm;
	
	private String chcsNo;
	//private String userPwdEc="2BBE0C48B91A7D1B8A6753A8B9CBE1DB16B84379F3F91FE115621284DF7A48F1CD71E9BEB90EA614C7BD924250AA9E446A866725E685A65DF5D139A5CD180DC9"; //test1234
	//private String userPwdEc="test1234";
	private String userPwdEc;
	
	private String fscrId="AC927717";
	private String lschId="AC927717";
	
	
}



