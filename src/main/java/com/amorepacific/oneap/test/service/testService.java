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
 * Date   	          : 2021. 9. 9..
 * Description       : OMNIMP v1 옴니회원플랫폼 구축 프로젝트.
 * </pre>
 */
package com.amorepacific.oneap.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amorepacific.oneap.test.mapper.testMapper;
import com.amorepacific.oneap.test.vo.Occued_chcs;
import com.amorepacific.oneap.test.vo.Occued_cust;
import com.amorepacific.oneap.test.vo.UserData;



/**
 * <pre>
 * com.amorepacific.oneap.test.service 
 *    |_ testservice.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 9. 9.
 * @version : 1.0
 * @author  : takkies
 */

@Service
public class testService{
	
	@Autowired
	testMapper testMapper;
	
	public UserData selectUserId(String um_user_name) {
		return this.testMapper.selectUserId(um_user_name);
	}
	public void addChcd(Occued_chcs occued_chcs) {
		 this.testMapper.addChcd(occued_chcs);
		return;
	}
	
	public void addcust(Occued_cust occued_cust) {
		this.testMapper.addcust(occued_cust);
		return;
	}
	
	
	
	
}
