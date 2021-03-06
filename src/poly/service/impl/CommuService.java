package poly.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.springframework.stereotype.Service;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import poly.dto.CheckDTO;
import poly.dto.CommuDTO;
import poly.dto.DataDTO;
import poly.persistance.mongo.ICommuMapper;
import poly.service.ICommuService;
import poly.util.CmmUtil;
import poly.util.DateUtil;

@Service("CommuService")
public class CommuService implements ICommuService {

	@Resource(name = "CommuMapper")
	private ICommuMapper commuMapper;

	@Resource(name = "CommuService")
	private ICommuService commuService;

	private Logger log = Logger.getLogger(this.getClass());

	/**
	 * 커뮤니티 크롤링
	 */
	// 컴본갤
	@Override
	public int collectDcComData() throws Exception {
		int res = 0;
		// 컬렉션 이름 지정
		String colNm = "DcCom_" + DateUtil.getDateTime("yyyyMMddHH");
		// 크롤링 한 데이터를 몽고DB에 저장시키기 위한 List변수
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		// 1~20페이지의 글을 크롤링 하기 위한 for문
		for (int i = 1; i <= 20; i++) {
			// 크롤링 할 web page url
			String url = "https://gall.dcinside.com/board/lists/?id=pridepc_new3&list_num=30&page=";
			url = url + Integer.toString(i);

			// 크롤링 결과값 변수
			Document doc = null;

			// Jsoup을 사용한 크롤링
			doc = Jsoup.connect(url).get();

			// 크롤링 결과값에서 table중 class에 gall_list 가 있는 부분만 추출
			Elements element = doc.select("table.gall_list");

			// 추출한 결과물 안에서 tr중 us-post class를 가진 변수들을 추출하여 Iterator로 만듬
			Iterator<Element> postList = element.select("tr.us-post").iterator();

			// Iterator 반복문
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				String title = postInfo.select("td.gall_tit a").eq(0).text();
				String writer = postInfo.select("td.gall_writer").attr("data-nick");
				String time = postInfo.select("td.gall_date").attr("title");
				int views = Integer.parseInt(postInfo.select("td.gall_count").text());
				String link = "https://gall.dcinside.com" + postInfo.select("td.gall_tit a").eq(0).attr("href");

				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setTime(time);
				pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
				pDTO.setWriter(writer.replaceAll("'", "&#39;"));
				pDTO.setViews(views);
				pDTO.setLink(link);

				pList.add(pDTO);
			}
		}

		// 몽고DB에 컬랙션 생성 후 데이터 삽입
		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// slr클럽
	@Override
	public int collectSlrData() throws Exception {
		String colNm = "Slr_" + DateUtil.getDateTime("yyyyMMddHH");
		Document odoc = Jsoup.connect("http://www.slrclub.com/bbs/zboard.php?id=free&page=1").get();
		String page = odoc.select("span#actpg").text();
		int res = 0;
		String tmp = "";
		Calendar cal = Calendar.getInstance();
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for (int i = 0; i <= 19; i++) {
			String url = "http://www.slrclub.com/bbs/zboard.php?id=free&page=";
			url = url + Integer.toString(Integer.parseInt(page) - i);
			Document doc = null;
			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table#bbs_list");

			Iterator<Element> postList = element.select("tbody > tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("공지")
						&& !postInfo.select("td").eq(0).text().equals("")) {
					String title = postInfo.select("td.sbj > a").text();
					String writer = postInfo.select("td.list_name > span").text();
					String time = postInfo.select("td.list_date").text();

					if (time.contains(":")) {
						if (tmp.equals("")) {
							tmp = time.substring(0, 2);
						}
						if (tmp.equals("00") && time.substring(0, 2).equals("23")) {
							cal.add(Calendar.DATE, -1);
							tmp = time.substring(0, 2);
						} else {
							tmp = time.substring(0, 2);
						}
						int year = cal.get(cal.YEAR);
						int month = (cal.get(cal.MONTH) + 1);
						int date = cal.get(cal.DATE);
						String datetime = "";
						if (month < 10) {
							datetime = Integer.toString(year) + "-0" + Integer.toString(month) + "-";
						} else {
							datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-";
						}
						if (date < 10) {
							datetime = datetime + "0" + Integer.toString(date) + " ";
						} else {
							datetime = datetime + Integer.toString(date) + " ";
						}
						time = datetime + time;

					} else {
						time = time + " 00:00:00";
					}

					int views = Integer.parseInt(postInfo.select("td.list_click").text());
					String link = "http://www.slrclub.com" + postInfo.select("td.sbj > a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("SLR클럽");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
					pDTO = null;
				}
			}
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// 뽐뿌
	@Override
	public int collectPpomData() throws Exception {
		int res = 0;
		String colNm = "Ppom_" + DateUtil.getDateTime("yyyyMMddHH");
		List<CommuDTO> pList = new ArrayList<CommuDTO>();

		for (int i = 1; i <= 20; i++) {

			String url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=freeboard&page=";
			url = url + Integer.toString(i);
			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table#revolution_main_table > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (postInfo.select("td").eq(0).hasClass("list_vspace")) {
					String title = postInfo.select("td").eq(2).select("a").text();
					String writer = "";
					if (postInfo.select("td").eq(1).select("a").hasText()) {
						writer = postInfo.select("td").eq(1).select("span.list_name").text();
					} else {
						writer = postInfo.select("td").eq(1).select("img").attr("alt");
					}
					String time = postInfo.select("td").eq(3).attr("title");
					int views = Integer.parseInt(postInfo.select("td").eq(5).text());
					String link = "http://www.ppomppu.co.kr/zboard/"
							+ postInfo.select("td").eq(2).select("a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("뽐뿌");
					pDTO.setTime("20" + time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}

		for (int i = 1; i <= 5; i++) {

			String url = "http://www.ppomppu.co.kr/zboard/zboard.php?id=issue&page=";
			url = url + Integer.toString(i);

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table#revolution_main_table > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (postInfo.select("td").eq(0).hasClass("list_vspace")) {
					String title = postInfo.select("td").eq(2).select("a").text();
					String writer = "";
					if (postInfo.select("td").eq(1).select("a").hasText()) {
						writer = postInfo.select("td").eq(1).select("span.list_name").text();
					} else {
						writer = postInfo.select("td").eq(1).select("img").attr("alt");
					}
					String time = postInfo.select("td").eq(3).attr("title");
					int views = Integer.parseInt(postInfo.select("td").eq(5).text());
					String link = "http://www.ppomppu.co.kr/zboard/"
							+ postInfo.select("td").eq(2).select("a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("뽐뿌");
					pDTO.setTime("20" + time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}
		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// 82쿡
	@Override
	public int collect82CookData() throws Exception {
		String colNm = "82Cook_" + DateUtil.getDateTime("yyyyMMddHH");
		int res = 0;
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		for (int i = 1; i <= 20; i++) {
			String url = "https://www.82cook.com/entiz/enti.php?bn=15&page=";
			url = url + Integer.toString(i);

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("form#bbs > table > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (postInfo.select("td").eq(0).hasClass("numbers")) {
					String title = postInfo.select("td.title a").text();
					String writer = postInfo.select("td.user_function").text();
					String time = postInfo.select("td.regdate").attr("title");
					int views = Integer.parseInt(StringReplace(postInfo.select("td").eq(4).text()));
					String link = "https://www.82cook.com/entiz/" + postInfo.select("td.title a").attr("href");

					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("82쿡");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
					pDTO = null;
				}
			}
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// MLBPARK
	@Override
	public int collectMPData() throws Exception {
		int res = 0;
		String colNm = "Mlb_" + DateUtil.getDateTime("yyyyMMddHH");
		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		String tmp1 = "";
		Calendar cal1 = Calendar.getInstance();
		String tmp2 = "";
		Calendar cal2 = Calendar.getInstance();

		for (int i = 1; i <= 20; i++) {

			String url = "http://mlbpark.donga.com/mp/b.php?b=kbotown&p=";
			url = url + Integer.toString((i - 1) * 3) + "1";

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table.tbl_type01 > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("공지")
						&& !postInfo.select("span.date").text().contains("-")) {
					String title = CmmUtil.nvl(postInfo.select("span.word").text())
							+ postInfo.select("a").attr("title");
					String writer = postInfo.select("span.nick").text();
					String time = postInfo.select("span.date").text();
					int views = Integer.parseInt(StringReplace(postInfo.select("span.viewV").text()));
					String link = postInfo.select("a").attr("href");

					postInfo = null;

					if (time.contains(":")) {
						if (tmp1.equals("")) {
							tmp1 = time.substring(0, 2);
						}
						if (tmp1.equals("00") && time.substring(0, 2).equals("23")) {
							cal1.add(Calendar.DATE, -1);
							tmp1 = time.substring(0, 2);
						} else {
							tmp1 = time.substring(0, 2);
						}
						int year = cal1.get(cal1.YEAR);
						int month = (cal1.get(cal1.MONTH) + 1);
						int date = cal1.get(cal1.DATE);
						String datetime = "";
						if (month < 10) {
							datetime = Integer.toString(year) + "-0" + Integer.toString(month) + "-";
						} else {
							datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-";
						}
						if (date < 10) {
							datetime = datetime + "0" + Integer.toString(date) + " ";
						} else {
							datetime = datetime + Integer.toString(date) + " ";
						}
						time = datetime + time;

					} else {
						time = time + " 00:00:00";
					}

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("MLBPARK");
					pDTO.setTime(time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}
		for (int i = 1; i <= 20; i++) {

			String url = "http://mlbpark.donga.com/mp/b.php?b=bullpen&p=";
			url = url + Integer.toString((i - 1) * 3) + "1";

			Document doc = null;

			doc = Jsoup.connect(url).get();

			Elements element = doc.select("table.tbl_type01 > tbody");

			Iterator<Element> postList = element.select("tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("공지")
						&& !postInfo.select("span.date").text().contains("-")) {
					String title = postInfo.select("a").attr("title");
					String writer = postInfo.select("span.nick").text();
					String time = postInfo.select("span.date").text();
					int views = Integer.parseInt(StringReplace(postInfo.select("span.viewV").text()));
					String link = postInfo.select("a").attr("href");

					postInfo = null;

					if (time.contains(":")) {
						if (tmp2.equals("")) {
							tmp2 = time.substring(0, 2);
						}
						if (tmp2.equals("00") && time.substring(0, 2).equals("23")) {
							cal2.add(Calendar.DATE, -1);
							tmp2 = time.substring(0, 2);
						} else {
							tmp2 = time.substring(0, 2);
						}
						int year = cal2.get(cal2.YEAR);
						int month = (cal2.get(cal2.MONTH) + 1);
						int date = cal2.get(cal2.DATE);
						String datetime = "";
						if (month < 10) {
							datetime = Integer.toString(year) + "-0" + Integer.toString(month) + "-";
						} else {
							datetime = Integer.toString(year) + "-" + Integer.toString(month) + "-";
						}
						if (date < 10) {
							datetime = datetime + "0" + Integer.toString(date) + " ";
						} else {
							datetime = datetime + Integer.toString(date) + " ";
						}
						time = datetime + time;

					} else {
						time = time + " 00:00:00";
					}

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("MLBPARK");
					pDTO.setTime(time.replaceAll("\\.", "\\-"));
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return res;
	}

	// 크롤링 데이터 가져오기
	@Override
	public List<CommuDTO> getData(String colNm) throws Exception {
		List<CommuDTO> rList = commuMapper.getData(colNm);

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		return rList;
	}

	// 분석 데이터 가져오기
	@Override
	public List<DataDTO> getAnalysisData(String colNm) throws Exception {
		List<DataDTO> rList = commuMapper.getAnalysisData(colNm);

		if (rList == null) {
			rList = new ArrayList<DataDTO>();
		}
		return rList;
	}

	// 크롤링 데이터 있는지 없는지 확인
	// 커뮤니티 전체
	@Override
	public int checkCrawlingData(List<String> sList) throws Exception {
		for (int i = 0; i < sList.size(); i++) {
			String colNm = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> rList = commuMapper.getData(colNm);

			if (rList == null) {
				rList = new ArrayList<CommuDTO>();
			}
			if (rList.size() == 0) {
				switch (sList.get(i)) {
				case "DcCom_":
					commuMapper.insertCheckData(colNm);
					commuService.collectDcComData();
					break;
				case "Slr_":
					commuMapper.insertCheckData(colNm);
					commuService.collectSlrData();
					break;
				case "Ppom_":
					commuMapper.insertCheckData(colNm);
					commuService.collectPpomData();
					break;
				case "82Cook_":
					commuMapper.insertCheckData(colNm);
					commuService.collect82CookData();
					break;
				case "Mlb_":
					commuMapper.insertCheckData(colNm);
					commuService.collectMPData();
					break;
				}
			}
			rList = null;
		}
		return 1;
	}

	// 분석 데이터 있는지 없는지 확인
	@Override
	public int checkAnalysisData(List<String> sList) throws Exception {
		// 변수로 받아온 sList의 크기 만큼 for문 반복
		for (int i = 0; i < sList.size(); i++) {
			String colNm = "Analysis" + sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			String str = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> rList = commuMapper.getAnalysisData(colNm);

			if (rList == null) {
				rList = new ArrayList<DataDTO>();
			}
			if (rList.size() == 0) {
				// 분석된 결과가 없는 경우 데이터 분석 시작
				commuService.AnalysisData(sList.get(i));
			}
			// 분석이 완료된경우 check컬랙션에 있는 false값을 true로 바꿈
			commuMapper.updateCheckData(str);
		}
		return 1;
	}

	// 데이터 분석하기
	@Override
	public int AnalysisData(String str) throws Exception {

		String comu = "";

		if (str.contains("DcCom_")) {
			comu = "컴퓨터 본체 갤러리";
		} else if (str.contains("Slr_")) {
			comu = "SLR클럽";
		} else if (str.contains("Ppom_")) {
			comu = "뽐뿌";
		} else if (str.contains("82Cook_")) {
			comu = "82쿡";
		} else if (str.contains("Mlb_")) {
			comu = "MLBPARK";
		}

		// R 연결 후 라이브러리 추가
		// RConnection c = new RConnection("54.180.67.42",6311);
		RConnection c = new RConnection("192.168.170.161", 6311);
		c.login("lonoka", "scarlet14!");

		// 데이터를 가져올 컬렉션 지정
		String colNm = str + DateUtil.getDateTime("yyyyMMddHH");

		// 컬렉션에 저장된 정보 가져오기
		List<CommuDTO> rList = commuService.getData(colNm);

		// null값 체크
		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		if (rList.size() > 1) {
			// Rserve에 넣기 위해 List를 String배열로 바꾸기 위한 변수
			String[] title = new String[rList.size()];
			String[] writer = new String[rList.size()];
			String[] time = new String[rList.size()];
			// 컬렉션에서 가져온 정보를 분석하기 위해 각각의 String배열로 변경
			for (int i = 0; i < rList.size(); i++) {
				// 특수기호 제거를 위한 정규식
				String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
				title[i] = rList.get(i).getTitle().replaceAll(match, "").toLowerCase();
				writer[i] = rList.get(i).getWriter();
				time[i] = rList.get(i).getTime().substring(0, 15) + "0:00";
			}
			// R 문법
			// Rserve에 String배열을 넘김
			c.assign("title", title);
			// 형태소 분석
			c.eval("m_df <- title %>% SimplePos09 %>% melt %>% as_tibble %>% select(3,1)");
			c.eval("m_df <- m_df %>% mutate(noun=str_match(value, '([A-Z|a-z|0-9|가-힣]+)/N')[,2]) %>% na.omit");
			// 형태소 분석 중 긍정,부정 분석을 위해 새로운 wordList 란 이름의 리스트 생성
			c.eval("wordList <- m_df$noun");
			c.eval("m_df <- m_df %>% count(noun, sort = TRUE)");
			c.eval("m_df <- filter(m_df,nchar(noun)>=2)");
			c.eval("m_df <- filter(m_df,n>=2)");
			// 긍정 부정 분석
			c.eval("wordList = unlist(wordList)");
			// wordList에 있는 단어가 postive에 있는 경우 true, 없는경우 na 즉 결측치 반환
			c.eval("posM = match(wordList, positive)");
			// 결측치 제거
			c.eval("posM = !is.na(posM)");
			c.eval("negM = match(wordList, negative)");
			c.eval("negM = !is.na(negM)");

			// 형태소 분석 결과 몽고DB에 넣기
			// Rserve에서 값을 받아오기 위해 REXP 사용
			// REXP 는 여러 형태로 변환 가능
			// m_df의 noun은 형태소 분석 결과값중 길이가 2 이상인 단어
			// m_df의 n은 형태소 분셕 결과값중 빈도수가 2 이상인 단어의 빈도수
			// m_df의 결측치를 제거하여 noun와 n은 길이가 같음
			REXP x = c.eval("m_df$noun");
			REXP y = c.eval("m_df$n");
			
			// 분석 결과를 저장하기 위한 컬랙션 지정
			colNm = "Analysis" + str + DateUtil.getDateTime("yyyyMMddHH");
			List<DataDTO> pList = new ArrayList<DataDTO>();
			DataDTO pDTO = new DataDTO();
			String[] noun = x.asStrings();
			String[] count = y.asStrings();

			// 형태소 분석 결과값을 몽고DB에 저장하기 위해 pList에 하나 하나 저장
			for (int i = 0; i < noun.length; i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(noun[i]);
				pDTO.setCount(Integer.parseInt(count[i]));
				pList.add(pDTO);
				pDTO = null;
			}
			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 긍정 부정 결과 몽고DB에 넣기
			
			colNm = "Opinion" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();
			x = c.eval("sum(posM)");
			y = c.eval("sum(negM)");

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name(comu);
			pDTO.setWord("긍정");
			pDTO.setCount(Integer.parseInt(x.asString()));
			pList.add(pDTO);
			pDTO = null;

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name(comu);
			pDTO.setWord("부정");
			pDTO.setCount(Integer.parseInt(y.asString()));
			pList.add(pDTO);
			pDTO = null;

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 글쓴이 분석 결과 몽고 DB에 넣기
			
			colNm = "Writer" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			List<String> writer_name = new ArrayList<String>();
			List<Integer> writer_count = new ArrayList<Integer>();

			/**
			 * 글쓴이 이름 워드카운트 writer_name 리스트에 이름이 없는 경우 writer_name 리스트에 이름을 넣고 있는경우 for문 진행
			 * writer_name리스트에 이름이 들어간 경우 writer[]배열 내부에 들어간 이름과 같은게 몇개 있는지 카운트 하여 writer_count 리스트에 넣음
			 * */ 
			for (int i = 0; i < writer.length; i++) {
				if (!writer_name.contains(writer[i])) {
					writer_name.add(writer[i]);
					int cnt = 0;
					for (int j = i; j < writer.length; j++) {
						if (writer[i].equals(writer[j])) {
							cnt++;
						}
					}
					writer_count.add(cnt);
				}
			}
			for (int i = 0; i < writer_name.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(writer_name.get(i));
				pDTO.setCount(writer_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;
			// 글쓴 시간대별 분석 결과 몽고 DB에 넣기

			colNm = "Time" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			List<String> time_section = new ArrayList<String>();
			List<Integer> time_count = new ArrayList<Integer>();

			for (int i = 0; i < time.length; i++) {
				if (!time_section.contains(time[i])) {
					time_section.add(time[i]);
					int cnt = 0;
					for (int j = i; j < time.length; j++) {
						if (time[i].equals(time[j])) {
							cnt++;
						}
					}
					time_count.add(cnt);
				}
			}
			for (int i = 0; i < time_section.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(time_section.get(i));
				pDTO.setCount(time_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

		}
		// 여러 크롤링 하나하나 찾아서 넣기
		c.close();

		return 0;
	}

	// 데이터 분석 테스트
	@Override
	public int AnalysisTest(String str) throws Exception {

		String comu = "";

		if (str.contains("DcCom_")) {
			comu = "컴퓨터 본체 갤러리";
		} else if (str.contains("Slr_")) {
			comu = "SLR클럽";
		} else if (str.contains("Ppom_")) {
			comu = "뽐뿌";
		} else if (str.contains("82Cook_")) {
			comu = "82쿡";
		} else if (str.contains("Mlb_")) {
			comu = "MLBPARK";
		}

		// R 연결 후 라이브러리 추가
		// RConnection c = new RConnection("54.180.67.42",6311);
		RConnection c = new RConnection("192.168.170.161", 6311);
		c.login("lonoka", "scarlet14!");

		String colNm = str + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> rList = commuService.getData(colNm);

		// 형태소 분석 결과값이 저장될 리스트
		List<String> sList = new ArrayList<String>();
		List<Integer> iList = new ArrayList<Integer>();

		// 감성분석 결과값이 저장될 변수
		int pos = 0;
		int neg = 0;

		if (rList == null) {
			rList = new ArrayList<CommuDTO>();
		}
		if (rList.size() > 1) {
			String[] title = null;
			String[] writer = new String[rList.size()];
			String[] time = new String[rList.size()];
			String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
			int Cnt = 0;

			for (int i = 0; i < rList.size(); i++) {
				if (i == 0) {
					title = new String[50];
				}
				title[i % 50] = rList.get(i).getTitle().replaceAll(match, "").toLowerCase();
				writer[i] = rList.get(i).getWriter();
				time[i] = rList.get(i).getTime().substring(0, 15) + "0:00";
				if (Cnt % 50 == 49) {
					Cnt = 0;
					c.assign("title", title);
					// 형태소 분석
					c.eval("m_df <- title %>% SimplePos09 %>% melt %>% as_tibble %>% select(3,1)");
					c.eval("m_df <- m_df %>% mutate(noun=str_match(value, '([A-Z|a-z|0-9|가-힣]+)/N')[,2]) %>% na.omit");
					c.eval("wordList <- m_df$noun");
					c.eval("m_df <- m_df %>% count(noun, sort = TRUE)");
					c.eval("m_df <- filter(m_df,nchar(noun)>=2)");
					c.eval("m_df <- filter(m_df,n>=2)");
					// 긍정 부정 분석
					c.eval("wordList = unlist(wordList)");
					c.eval("posM = match(wordList, positive)");
					c.eval("posM = !is.na(posM)");
					c.eval("negM = match(wordList, negative)");
					c.eval("negM = !is.na(negM)");

					// 형태소 분석 결과 몽고DB에 넣기
					REXP x = c.eval("m_df$noun");
					REXP y = c.eval("m_df$n");

					// 형태소 분석 결과 배열로 변경
					String[] noun = x.asStrings();
					String[] count = y.asStrings();

					// 형태소 분석 결과값이 sList에 있는지 확인 후 넣기
					for (int j = 0; j < noun.length; j++) {
						if (sList.contains(noun[j])) {
							iList.set(j, iList.get(j) + Integer.parseInt(count[j]));
						} else {
							sList.add(noun[j]);
							iList.add(Integer.parseInt(count[j]));
						}
					}

					// 감성분석 결과값
					x = c.eval("sum(posM)");
					y = c.eval("sum(negM)");

					pos += Integer.parseInt(x.toString());
					neg += Integer.parseInt(y.toString());

					title = new String[50];
				} else {
					Cnt += 1;
				}
			}

			// 분석 결과 저장 컬렉션 지정
			colNm = "Analysis" + str + DateUtil.getDateTime("yyyyMMddHH");

			// 분석 결과 저장하기 위한 DataDTO 타입 List 선언
			List<DataDTO> pList = new ArrayList<DataDTO>();
			DataDTO pDTO = new DataDTO();

			// 분석 결과 하나하나 DTO에 넣어서 pList에 넣기
			for (int i = 0; i < sList.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(sList.get(i));
				pDTO.setCount(iList.get(i));
				pList.add(pDTO);
				pDTO = null;
			}
			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 긍정 부정 결과 몽고DB에 넣기

			colNm = "Opinion" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name(comu);
			pDTO.setWord("긍정");
			pDTO.setCount(pos);
			pList.add(pDTO);
			pDTO = null;

			pDTO = new DataDTO();
			pDTO.setAnalysis_time(colNm);
			pDTO.setCommu_name(comu);
			pDTO.setWord("부정");
			pDTO.setCount(neg);
			pList.add(pDTO);
			pDTO = null;

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

			// 글쓴이 분석 결과 몽고 DB에 넣기

			colNm = "Writer" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			List<String> writer_name = new ArrayList<String>();
			List<Integer> writer_count = new ArrayList<Integer>();

			for (int i = 0; i < writer.length; i++) {
				if (!writer_name.contains(writer[i])) {
					writer_name.add(writer[i]);
					int cnt = 0;
					for (int j = i; j < writer.length; j++) {
						if (writer[i].equals(writer[j])) {
							cnt++;
						}
					}
					writer_count.add(cnt);
				}
			}
			for (int i = 0; i < writer_name.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(writer_name.get(i));
				pDTO.setCount(writer_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;
			// 글쓴 시간대별 분석 결과 몽고 DB에 넣기

			colNm = "Time" + str + DateUtil.getDateTime("yyyyMMddHH");
			pList = new ArrayList<DataDTO>();

			List<String> time_section = new ArrayList<String>();
			List<Integer> time_count = new ArrayList<Integer>();

			for (int i = 0; i < time.length; i++) {
				if (!time_section.contains(time[i])) {
					time_section.add(time[i]);
					int cnt = 0;
					for (int j = i; j < time.length; j++) {
						if (time[i].equals(time[j])) {
							cnt++;
						}
					}
					time_count.add(cnt);
				}
			}
			for (int i = 0; i < time_section.size(); i++) {
				pDTO = new DataDTO();
				pDTO.setAnalysis_time(colNm);
				pDTO.setCommu_name(comu);
				pDTO.setWord(time_section.get(i));
				pDTO.setCount(time_count.get(i));
				pList.add(pDTO);
				pDTO = null;
			}

			commuMapper.createCollection(colNm);
			commuMapper.insertAnalysisData(pList, colNm);

			pList = null;

		}
		// 여러 크롤링 하나하나 찾아서 넣기
		c.close();

		return 0;
	}

	// 특수기호 제거 함수
	public static String StringReplace(String str) {
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		str = str.replaceAll(match, "");
		return str;
	}
	
	// 정렬 함수
	public static List<CommuDTO> sortCollection(List<CommuDTO> pList){
		// collection.sort에서 commuDTO내부의 값으로 비교하기 위해
		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				//compareTo 함수는 문자열과 문자열의 크기 비교 가능
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		return pList;
	}

	// 크롤링이 진행중인지 완료되었는지 확인
	@Override
	public boolean checkCrawling(List<String> sList) throws Exception {
		for (int i = 0; i < sList.size(); i++) {
			boolean check = commuMapper.checkData(sList.get(i));
			if (check == false) {
				return false;
			}
		}
		return true;
	}

	// 검색 크롤링 실행
	@Override
	public int checkSearchCrawlingData(List<String> sList, String str) throws Exception {
		// 크롤링 검색 결과가 있는지 없는지 확인
		// 크롤링 검색 결과가 있는 경우 1 반환
		// 크롤링 검색 결과가 없는 경우 체크컬렉션에 검색어 입력 후 검색 시작 
		for (int i = 0; i < sList.size(); i++) {
			String colNm = sList.get(i) + DateUtil.getDateTime("yyyyMMddHH");
			List<CommuDTO> rList = commuMapper.getData(colNm);

			if (rList == null) {
				rList = new ArrayList<CommuDTO>();
			}
			if (rList.size() == 0) {
				if (sList.get(i).contains("DcCom_")) {
					commuMapper.insertCheckData(colNm);
					commuService.SearchDcComData(str);
				} else if (sList.get(i).contains("Slr_")) {
					commuMapper.insertCheckData(colNm);
					commuService.SearchSlrData(str);
				} else if (sList.get(i).contains("Ppom_")) {
					commuMapper.insertCheckData(colNm);
					commuService.SearchPpomData(str);
				} else if (sList.get(i).contains("82Cook_")) {
					commuMapper.insertCheckData(colNm);
					commuService.Search82CookData(str);
				} else if (sList.get(i).contains("Mlb_")) {
					commuMapper.insertCheckData(colNm);
					commuService.SearchMPData(str);
				}
			}
			rList = null;
		}
		return 1;
	}
	
	
	// 검색시 로그인이 필요하여 header를 채크해주고 loginCookie 가져와서 크롤링
	@Override
	public int SearchSlrData(String str) throws Exception {
		Map<String, String> header = new HashMap<String, String>();
		header.put("origin", "http://www.slrclub.com");
		header.put("Referer", "http://www.slrclub.com");
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		header.put("Accept-Encoding", "gzip, deflate, br");
		header.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
		Map<String, String> data = new HashMap<String, String>();
		data.put("user_id", "lonoka");
		data.put("password", "scarlet14!");
		data.put("autologin", "1");
		data.put("group_no", "1");
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36";
		Connection.Response response = Jsoup.connect("https://www.slrclub.com/login/process.php").userAgent(userAgent)
				.headers(header).data(data).method(Connection.Method.POST).execute();

		Map<String, String> loginCookie = response.cookies();
		Map<String, String> sheader = new HashMap<String, String>();
		sheader.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		sheader.put("Accept-Encoding", "gzip, deflate");
		sheader.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

		Document odoc = Jsoup.connect("http://www.slrclub.com/service/search/?keyword=" + str).userAgent(userAgent)
				.headers(sheader).cookies(loginCookie).get();
		Elements e = odoc.select("td.list_num");
		Iterator<Element> pageList = e.select("> a").iterator();
		int page = Integer.parseInt(odoc.select("span#actpg").text());
		int tmp = page;
		while (pageList.hasNext()) {
			Element pInfo = pageList.next();
			log.info(pInfo);
			if (pInfo.hasText()) {
				int pNum = Integer.parseInt(pInfo.text());
				if (pNum > page) {
					tmp = pNum;
				}
			}
		}

		String colNm = "Slr_" + str + "_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> pList = new ArrayList<CommuDTO>();
		if (tmp > 40) {
			for (int i = 0; i < 40; i++) {
				Document doc = Jsoup
						.connect("http://www.slrclub.com/service/search/?page=" + (tmp - i) + "&keyword=" + str)
						.userAgent(userAgent).headers(sheader).cookies(loginCookie).get();

				Elements element = doc.select("ul.list");

				Iterator<Element> postList = element.select("> li").iterator();
				while (postList.hasNext()) {
					Element postInfo = postList.next();
					String title = postInfo.select("p.title > a").text();
					String writer = postInfo.select("span.name > span").text();
					String time = postInfo.select("span.date").text();
					if (time.contains(":")) {
						time = DateUtil.getDateTime("yyyy-MM-dd") + " 00:00:00";
					} else {
						time = time.replaceAll("/", "-");
						time = "20" + time + " 00:00:00";
					}
					if (postInfo.select("div.info > span").eq(3).text().substring(6).equals("")) {
						break;
					}
					int views = Integer.parseInt(postInfo.select("div.info > span").eq(3).text().substring(6));
					String link = "http://www.slrclub.com/" + postInfo.select("p.title > a").attr("href");
					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("SLR클럽");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}

			}
		} else {
			for (int i = 0; i < tmp; i++) {
				Document doc = Jsoup.connect(
						"http://www.slrclub.com/service/search/?page=" + Integer.toString(i) + "&keyword=" + str)
						.userAgent(userAgent).headers(sheader).cookies(loginCookie).get();

				Elements element = doc.select("ul.list");

				Iterator<Element> postList = element.select("> li").iterator();
				while (postList.hasNext()) {
					Element postInfo = postList.next();
					String title = postInfo.select("p.title > a").text();
					String writer = postInfo.select("span.name > span").text();
					String time = postInfo.select("span.date").text();
					if (postInfo.select("div.info > span").eq(3).text().substring(6).equals("")) {
						break;
					}
					int views = Integer.parseInt(postInfo.select("div.info > span").eq(3).text().substring(6));
					String link = "http://www.slrclub.com/" + postInfo.select("p.title > a").attr("href");
					time = time.replaceAll("/", "-");
					time = "20" + time + " 00:00:00";
					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("SLR클럽");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
				}

			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return 0;
	}

	@Override
	public int SearchDcComData(String str) throws Exception {
		String colNm = "DcCom_" + str + "_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> pList = new ArrayList<CommuDTO>();

		for (int i = 1; i <= 20; i++) {
			Document doc = Jsoup.connect(
					"https://gall.dcinside.com/board/lists/?id=pridepc_new3&search_pos=&s_type=search_all&s_keyword="
							+ str + "&page=" + i)
					.get();
			Elements element = doc.select("table.gall_list");
			int tmp = 0;
			Iterator<Element> postList = element.select("tr.us-post").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				String title = postInfo.select("td.gall_tit a").eq(0).text();
				String writer = postInfo.select("td.gall_writer").attr("data-nick");
				String time = postInfo.select("td.gall_date").attr("title");
				int views = Integer.parseInt(postInfo.select("td.gall_count").text());
				String link = "https://gall.dcinside.com" + postInfo.select("td.gall_tit a").eq(0).attr("href");

				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("컴퓨터 본체 갤러리");
				pDTO.setTime(time);
				pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
				pDTO.setWriter(writer.replaceAll("'", "&#39;"));
				pDTO.setViews(views);
				pDTO.setLink(link);

				pList.add(pDTO);
				tmp += 1;
			}
			if (tmp != 20) {
				break;
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return 0;
	}

	@Override
	public int SearchPpomData(String str) throws Exception {
		String colNm = "Ppom_" + str + "_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> pList = new ArrayList<CommuDTO>();

		for (int i = 1; i <= 12; i++) {
			Document doc = Jsoup.connect(
					"http://www.ppomppu.co.kr/search_bbs.php?page_size=50&bbs_cate=2&order_type=date&search_type=sub_memo&keyword="
							+ str + "&page_no=" + i)
					.get();
			Elements element = doc.select("div#result-tab2");
			int tmp = 0;
			Iterator<Element> postList = element.select("form > div.item").iterator();
			while (postList.hasNext()) {
				tmp++;
				Element postInfo = postList.next();
				String title = postInfo.select("span.title > a").text();
				String writer = postInfo.select("p.desc > span").eq(0).text();
				String time = postInfo.select("p.desc > span").eq(2).text();
				if (postInfo.select("p.desc > span").eq(1).text().contains("정보없음")) {
					break;
				}
				int views = Integer.parseInt(postInfo.select("p.desc > span").eq(1).text().substring(5));
				String link = postInfo.select("span.title > a").attr("href");
				time = time.replaceAll("\\.", "-");
				time = time + " 00:00:00";
				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("뽐뿌");
				pDTO.setTime(time);
				pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
				pDTO.setWriter(writer.replaceAll("'", "&#39;"));
				pDTO.setViews(views);
				pDTO.setLink(link);

				pList.add(pDTO);
			}
			if (tmp != 50) {
				break;
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return 0;
	}

	@Override
	public int Search82CookData(String str) throws Exception {
		String colNm = "82Cook_" + str + "_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> pList = new ArrayList<CommuDTO>();

		for (int i = 1; i <= 24; i++) {
			Document doc = Jsoup.connect("https://www.82cook.com/entiz/enti.php?bn=15&searchType=search&search1=1&keys="
					+ str + "&page=" + i).get();
			Elements element = doc.select("table");
			int tmp = 0;
			Iterator<Element> postList = element.select("tbody > tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				if (!postInfo.select("td").eq(0).text().equals("검색결과가 없습니다")) {
					String title = postInfo.select("td.title > a").text();
					String writer = postInfo.select("td.user_function").text();
					String time = postInfo.select("td.regdate").attr("title");
					int views = Integer.parseInt(StringReplace(postInfo.select("td").eq(4).text()));
					String link = "https://www.82cook.com/entiz/" + postInfo.select("td.title > a").attr("href");
					time = time.substring(0, 10);
					time = time + " 00:00:00";
					postInfo = null;

					CommuDTO pDTO = new CommuDTO();

					pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
					pDTO.setCommu_name("82쿡");
					pDTO.setTime(time);
					pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
					pDTO.setWriter(writer.replaceAll("'", "&#39;"));
					pDTO.setViews(views);
					pDTO.setLink(link);

					pList.add(pDTO);
					tmp += 1;
				}
			}
			if (tmp != 25) {
				break;
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return 0;
	}

	@Override
	public int SearchMPData(String str) throws Exception {
		String colNm = "Mlb_" + str + "_" + DateUtil.getDateTime("yyyyMMddHH");

		List<CommuDTO> pList = new ArrayList<CommuDTO>();

		for (int i = 1; i <= 20; i++) {
			Document doc = Jsoup
					.connect("http://mlbpark.donga.com/mp/b.php?select=sct&m=search&b=bullpen&select=sct&query=" + str
							+ "&p=" + (i - 1) * 3 + "1")
					.get();
			Elements element = doc.select("table.tbl_type01");
			int tmp = 0;
			Iterator<Element> postList = element.select("tbody > tr").iterator();
			while (postList.hasNext()) {
				Element postInfo = postList.next();
				String title = postInfo.select("td").eq(1).select("a").attr("alt");
				String writer = postInfo.select("span.nick").text();
				String time = postInfo.select("span.date").text();
				if (time.contains(":")) {
					time = DateUtil.getDateTime("yyyy-MM-dd");
				}
				int views = 0;
				if (StringReplace(postInfo.select("span.viewV").text()).equals("")) {
					break;
				}
				views = Integer.parseInt(StringReplace(postInfo.select("span.viewV").text()));
				String link = postInfo.select("td").eq(1).select("a").attr("href");
				time = time + " 00:00:00";
				postInfo = null;

				CommuDTO pDTO = new CommuDTO();

				pDTO.setCollect_time(DateUtil.getDateTime("yyyyMMddHHmmss"));
				pDTO.setCommu_name("MLBPARK");
				pDTO.setTime(time);
				pDTO.setTitle(title.replaceAll("'", "&#39;").replaceAll("&nbsp", " ").replaceAll("\n", ""));
				pDTO.setWriter(writer.replaceAll("'", "&#39;"));
				pDTO.setViews(views);
				pDTO.setLink(link);

				pList.add(pDTO);
				tmp += 1;
			}
			if (tmp != 30) {
				break;
			}
		}

		Collections.sort(pList, new Comparator<CommuDTO>() {
			@Override
			public int compare(CommuDTO pDTO, CommuDTO rDTO) {
				if (pDTO.getTime().compareTo(rDTO.getTime()) < 0) {
					return 1;
				} else if (pDTO.getTime().compareTo(rDTO.getTime()) > 0) {
					return -1;
				}
				return 0;
			}
		});
		if (pList.size() > 600) {
			pList = pList.subList(0, 600);
		}

		commuMapper.createCollection(colNm);
		commuMapper.insertData(pList, colNm);

		return 0;
	}

	@Override
	public Map<String, Object> komoran(String str) throws Exception {

		Map<String, Object> pMap = new HashMap<String, Object>();

		String comu = "";

		if (str.contains("DcCom_")) {
			comu = "컴퓨터 본체 갤러리";
		} else if (str.contains("Slr_")) {
			comu = "SLR클럽";
		} else if (str.contains("Ppom_")) {
			comu = "뽐뿌";
		} else if (str.contains("82Cook_")) {
			comu = "82쿡";
		} else if (str.contains("Mlb_")) {
			comu = "MLBPARK";
		}

		List<CommuDTO> rList = commuService.getData(str);
		// 코모란 시작
		// 분석 결과값이 나온 단어 리스트
		List<String> komoranList = new ArrayList<String>();

		// 코모란 분석기 돌리고 나온 단어 리스트 를 카운팅 한 리스트
		List<String> kWordList = new ArrayList<String>();
		List<Integer> kCntList = new ArrayList<Integer>();

		Iterator<CommuDTO> InList = rList.iterator();
		String komo = "";

		// 분석할 문장을 하나의 긴 문장으로 변경
		while (InList.hasNext()) {
			CommuDTO pDTO = new CommuDTO();
			pDTO = InList.next();
			komo += pDTO.getTitle() + " ";
			pDTO = null;

		}
		InList = null;
		// 코모란 분석을 위한 Komoran객체
		Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
		KomoranResult analyKomoranResult = komoran.analyze(komo);
		// 분석된 결과중 단어들 만 나온 리스트
		komoranList.addAll(analyKomoranResult.getNouns());
		// 메모리 누수를 막기 위한 초기화
		komoran = null;
		analyKomoranResult = null;
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
		pMap.put(comu, pList);
		pList = null;

		return pMap;
	}
}
