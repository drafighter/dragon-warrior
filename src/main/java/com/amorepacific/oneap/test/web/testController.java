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
package com.amorepacific.oneap.test.web;

import java.time.temporal.UnsupportedTemporalTypeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.amorepacific.oneap.test.service.testService;
import com.amorepacific.oneap.test.vo.UserData;

import lombok.extern.slf4j.Slf4j;




/**
 * <pre>
 * com.amorepacific.oneap 
 *    |_ testController.java
 * </pre>
 *
 * @desc    :
 * @date    : 2021. 9. 9.
 * @version : 1.0
 * @author  : takkies
 */
@Slf4j
@Controller
public class testController {
	
	@Autowired
	testService testService;
	
	@GetMapping("/test")
	public String testSearch(Model model) throws UnsupportedTemporalTypeException{
		UserData userId = testService.selectUserId("jdh12188");
		log.debug("@@@@@@@@@@@@@"+userId.getUmUserName());
		
		model.addAttribute("userId",userId.getUmUserName());
		model.addAttribute("userPwd",userId.getUmUserPassword());
		return "sample";
	}
	
	
}
