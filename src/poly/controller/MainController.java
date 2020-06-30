package poly.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.service.ICommuService;
import poly.util.CmmUtil;
import poly.util.DateUtil;

@Controller
public class MainController {
	private Logger log = Logger.getLogger(this.getClass());

	// 크롤링 데이터를 확인 및 들고오기 위한 서비스
	@Resource(name = "CommuService")
	private ICommuService commuService;

	// r 라이브러리 호출
	@RequestMapping(value = "rConnection")
	@ResponseBody
	public String rConnection(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + "rConnection start!");
		//RConnection c = new RConnection("54.180.67.42",6311);
		RConnection c = new RConnection("192.168.170.161",6311);
		c.login("lonoka","scarlet14!");
		c.eval("library(tidyverse)");
		c.eval("library(KoNLP)");
		c.eval("useNIADic()");
		c.eval("library(stringr)");
		c.eval("library(reshape2)");
		c.eval("library(dplyr)");
		c.eval("negative <- readLines('c:\\\\word\\\\negative.txt', encoding = 'UTF-8')");
		c.eval("positive <- readLines('c:\\\\word\\\\positive.txt', encoding = 'UTF-8')");

		c.close();

		log.info(this.getClass().getName() + "rConnection end!");

		return "success";
	}

	// 메인 페이지
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + " index start!");
		if (CmmUtil.nvl(request.getParameter("checkNum")).equals("")) {
			return "/user/checkPage";
		}
		int result = 0;
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_");
		sList.add("Slr_");
		sList.add("Ppom_");
		sList.add("82Cook_");
		sList.add("Mlb_");

		List<Map> pList = new ArrayList<Map>();
		Map<String, Object> pMap = new HashMap();
		List<DataDTO> searchList = new ArrayList<DataDTO>();
		String colNm = "";
		for (int i = 0; i < sList.size(); i++) {
			pMap = new HashMap<String, Object>();
			colNm = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> bList = commuService.getData(colNm);
			if (bList == null) {
				bList = new ArrayList<CommuDTO>();
			}
			if (bList.size() > 0) {
				colNm = "Analysis" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> rList = commuService.getAnalysisData(colNm);
				if (rList == null) {
					rList = new ArrayList<DataDTO>();
				}
				colNm = "Writer" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> wList = commuService.getAnalysisData(colNm);
				if (wList == null) {
					wList = new ArrayList<DataDTO>();
				}
				colNm = "Time" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> tList = commuService.getAnalysisData(colNm);
				if (tList == null) {
					tList = new ArrayList<DataDTO>();
				}
				colNm = "Opinion" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> oList = commuService.getAnalysisData(colNm);
				if (oList == null) {
					oList = new ArrayList<DataDTO>();
				}
				if (rList.size() != 0 && tList.size() != 0 && wList.size() != 0 && oList.size() != 0) {
					pMap.put("bList", bList);
					pMap.put("rList", rList);
					pMap.put("wList", wList);
					pMap.put("tList", tList);
					pMap.put("oList", oList);
					pList.add(pMap);
					searchList.addAll(rList);
				}
				pMap = null;
			}
		}
		
		Collections.sort(searchList, new Comparator<DataDTO>() {
			@Override
			public int compare(DataDTO pDTO, DataDTO rDTO) {
				if (pDTO.getCount() < rDTO.getCount()) {
					return 1;
				} else if (pDTO.getCount() > rDTO.getCount()) {
					return -1;
				}
				return 0;
			}
		});
		List<String> seList = new ArrayList<String>();
		for(int i = 0; i<searchList.size();i++) {
			if(seList.size()>=20)
				break;
			if(!seList.contains(searchList.get(i).getWord()))
				seList.add(searchList.get(i).getWord());
		}
		model.addAttribute("pList", pList);
		model.addAttribute("seList", seList);

		log.info(this.getClass().getName() + " index end!");

		return "/user/index";

	}

	// 크롤링 중복 방지
	@RequestMapping(value = "DataCheck")
	@ResponseBody
	public String DataCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + " DataCheck start!");
		int result = 0;
		int tmp = 0;
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_");
		sList.add("Slr_");
		sList.add("Ppom_");
		sList.add("82Cook_");
		sList.add("Mlb_");
		// 크롤링 중인지 확인
		boolean crawling = false;
		while (crawling == false) {
			if (tmp == 1) {
				Thread.sleep(2000);
			}
			if (tmp == 0) {
				tmp = 1;
			}
			crawling = commuService.checkCrawling(sList);
		}
		// 값 있는지 확인
		result = commuService.checkCrawlingData(sList);
		result = commuService.checkAnalysisData(sList);

		log.info(this.getClass().getName() + " DataCheck end!");

		return "1";
	}

	// 검색 데이터 확인
	@RequestMapping(value = "searchCheck")
	@ResponseBody
	public String searchCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {

		log.info(this.getClass().getName() + " searchCheck start!");
		String str = CmmUtil.nvl(request.getParameter("searchTitle"));
		int result = 0;
		int tmp = 0;
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_" + str + "_");
		sList.add("Slr_" + str + "_");
		sList.add("Ppom_" + str + "_");
		sList.add("82Cook_" + str + "_");
		sList.add("Mlb_" + str + "_");
		// 크롤링 중인지 확인
		boolean crawling = false;
		while (crawling == false) {
			if (tmp == 1) {
				Thread.sleep(2000);
			}
			if (tmp == 0) {
				tmp = 1;
			}
			crawling = commuService.checkCrawling(sList);
		}
		commuService.checkSearchCrawlingData(sList, str);
		commuService.checkAnalysisData(sList);

		log.info(this.getClass().getName() + " searchCheck end!");

		return "1";
	}

	// 메인 페이지
	@RequestMapping(value = "searchPage")
	public String searchPage(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + " searchPage start!");
		log.info(CmmUtil.nvl(request.getParameter("checkNum")));
		log.info(CmmUtil.nvl(request.getParameter("searchValue")));
		if (CmmUtil.nvl(request.getParameter("checkNum")).equals("")) {
			log.info("checkNum 없음");
			return "/user/checkPage";
		}
		String str = CmmUtil.nvl(request.getParameter("searchValue"));
		if (str.equals("")) {
			log.info("searchValue 없음");
			return "/user/checkPage";
		}
		int result = 0;
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_" + str + "_");
		sList.add("Slr_" + str + "_");
		sList.add("Ppom_" + str + "_");
		sList.add("82Cook_" + str + "_");
		sList.add("Mlb_" + str + "_");

		List<Map> pList = new ArrayList<Map>();
		Map<String, Object> pMap = new HashMap();
		List<DataDTO> searchList = new ArrayList<DataDTO>();
		String colNm = "";
		for (int i = 0; i < sList.size(); i++) {
			pMap = new HashMap<String, Object>();
			colNm = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> bList = commuService.getData(colNm);
			if (bList == null) {
				bList = new ArrayList<CommuDTO>();
			}
			if (bList.size() > 0) {
				colNm = "Analysis" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> rList = commuService.getAnalysisData(colNm);
				if (rList == null) {
					rList = new ArrayList<DataDTO>();
				}
				colNm = "Writer" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> wList = commuService.getAnalysisData(colNm);
				if (wList == null) {
					wList = new ArrayList<DataDTO>();
				}
				colNm = "Time" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> tList = commuService.getAnalysisData(colNm);
				if (tList == null) {
					tList = new ArrayList<DataDTO>();
				}
				colNm = "Opinion" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
				List<DataDTO> oList = commuService.getAnalysisData(colNm);
				if (oList == null) {
					oList = new ArrayList<DataDTO>();
				}
				if (rList.size() != 0 && tList.size() != 0 && wList.size() != 0 && oList.size() != 0) {
					pMap.put("bList", bList);
					pMap.put("rList", rList);
					pMap.put("wList", wList);
					pMap.put("tList", tList);
					pMap.put("oList", oList);
					pList.add(pMap);
					searchList.addAll(rList);
				}
				pMap = null;
			}
		}
		// 있는경우 값 들고와서 워드클라우드 보여주기
		
		Collections.sort(searchList, new Comparator<DataDTO>() {
			@Override
			public int compare(DataDTO pDTO, DataDTO rDTO) {
				if (pDTO.getCount() < rDTO.getCount()) {
					return 1;
				} else if (pDTO.getCount() > rDTO.getCount()) {
					return -1;
				}
				return 0;
			}
		});
		List<String> seList = new ArrayList<String>();
		for(int i = 0; i<searchList.size();i++) {
			if(seList.size()>=20)
				break;
			if(!seList.contains(searchList.get(i).getWord()))
				seList.add(searchList.get(i).getWord());
		}

		model.addAttribute("pList", pList);
		model.addAttribute("sValue", str);
		model.addAttribute("seList", seList);

		log.info(this.getClass().getName() + " searchPage end!");

		return "/user/index";

	}

}
