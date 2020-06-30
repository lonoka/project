package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.CommuDTO;
import poly.service.ICommuService;
import poly.util.DateUtil;

@Controller
public class CommuController {
	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "CommuService")
	private ICommuService commuService;
	
	/**
	 * 크롤링 시작
	 */
	@RequestMapping(value = "Data/collectDcComData")
	@ResponseBody
	public String collectDcComData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + " collectDcComData start!");

		commuService.collectDcComData();

		log.info(this.getClass().getName() + " collectDcComData end!");

		return "success";
	}
	
	/**
	 * 데이터 가져오는 일반 화면
	 */
	@RequestMapping(value = "Data/DcData")
	public String DcData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + " DcData start!");

		log.info(this.getClass().getName() + " DcData end!");

		return "Data/DcData";
	}

	/**
	 * 정보 가져오기
	 */
	@RequestMapping(value = "Data/getDcData")
	@ResponseBody
	public List<CommuDTO> getDcData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info(this.getClass().getName() + " getDcData start!");

		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		List<CommuDTO> rList = commuService.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}

		log.info(this.getClass().getName() + " getDcData end!");

		return rList;
	}
}
