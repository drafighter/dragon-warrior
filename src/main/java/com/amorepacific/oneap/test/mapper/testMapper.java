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
package com.amorepacific.oneap.test.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.amorepacific.oneap.test.vo.Occued_chcs;
import com.amorepacific.oneap.test.vo.Occued_cust;
import com.amorepacific.oneap.test.vo.UserData;


/**
 * <pre>
 * com.amorepacific.oneap.test.mapper 
 *    |_ testImpl.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 9. 9.
 * @version : 1.0
 * @author  : takkies
 */
@Mapper
public interface testMapper {
	
	UserData selectUserId(String um_user_name);
	void addChcd(Occued_chcs occued_chcs);
	void addcust(Occued_cust occued_cust);
	
	
}
