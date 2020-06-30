package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Controller
public class RController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "CommuService")
	private ICommuService commuService;

	@RequestMapping(value = "cTest")
	@ResponseBody
	public String cTest(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		
		commuService.SearchDcComData("폴리텍");
		return "success";
	}

	@RequestMapping(value = "rtest2")
	@ResponseBody
	public String rtest2(HttpServletRequest request, Model model, HttpSession session) throws Exception {

		log.info(this.getClass().getName() + " rtest2 start!");
		List<CommuDTO> rList = commuService.getData("DcCom_2020062513");
		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		if (rList.size() > 0) {
			String[] title = new String[rList.size()];
			for (int i = 0; i < rList.size(); i++) {
				String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
				title[i] = rList.get(i).getTitle().replaceAll(match, "").toLowerCase();
			}
			
			RConnection c = new RConnection("54.180.67.42",6311);
			c.login("lonoka","scarlet14!");
			
			c.assign("title", title);
			// 형태소 분석
			c.eval("m_df <- title %>% SimplePos09");
			c.close();
			
		}
		return " ";
	}
	
	
}
