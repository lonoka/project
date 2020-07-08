package poly.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
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

	@RequestMapping(value = "kTest")
	@ResponseBody
	public String kTest(HttpServletRequest request, Model model, HttpSession session) throws Exception {

		commuService.SearchDcComData("폴리텍");
		return "success";
	}

	@RequestMapping(value = "komoran_test")
	@ResponseBody
	public String komoran_test(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		// collection 이름으로 mongoDB에서 데이터 가져오기
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_");
		sList.add("Slr_");
		sList.add("Ppom_");
		sList.add("82Cook_");
		sList.add("Mlb_");
		Map<String, Object> pMap = new HashMap();
		for (int j = 0; j < sList.size(); j++) {
			String colNm = sList.get(j) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> rList = commuService.getData(colNm);
			// 코모란 시작
			// 분석 결과값이 나온 단어 리스트
			List<String> komoranList = new ArrayList<String>();

			// 코모란 분석기 돌리고 나온 단어 리스트 를 카운팅 한 리스트
			List<String> kWordList = new ArrayList<String>();
			List<Integer> kCntList = new ArrayList<Integer>();

			Iterator<CommuDTO> InList = rList.iterator();

			// 리소스 사용량을 줄이기 위해 분석을 나눠서 함
			while (InList.hasNext()) {
				log.info("문장 분석중입니다.");
				CommuDTO pDTO = new CommuDTO();
				pDTO = InList.next();
				Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
				KomoranResult analyKomoranResult = komoran.analyze(pDTO.getTitle());
				komoranList.addAll(analyKomoranResult.getNouns());
				komoran = null;
				analyKomoranResult = null;
				pDTO = null;
			}
			for (int i = 0; i < komoranList.size(); i++) {
				if (komoranList.get(i).length() > 1) {
					if (!kWordList.contains(komoranList.get(i))) {
						kWordList.add(komoranList.get(i));
						int tmp = 0;
						for (int k = 0; k < komoranList.size(); k++) {
							if (komoranList.get(i).equals(komoranList.get(k)))
								tmp++;
						}
						kCntList.add(tmp);
					}
				}
			}

			List<DataDTO> pList = new ArrayList<DataDTO>();
			for (int i = 0; i < kWordList.size(); i++) {
				DataDTO pDTO = new DataDTO();
				pDTO.setWord(kWordList.get(i));
				pDTO.setCount(kCntList.get(i));
				pList.add(pDTO);
				pDTO = null;
			}
			kWordList = null;
			kCntList = null;
			pMap.put(sList.get(j), pList);
			pList = null;
		}
		// 코모란 끝
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

			RConnection c = new RConnection("54.180.67.42", 6311);
			c.login("lonoka", "scarlet14!");

			c.assign("title", title);
			// 형태소 분석
			c.eval("m_df <- title %>% SimplePos09");
			c.close();

		}
		return " ";
	}

}
