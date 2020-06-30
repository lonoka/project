package poly.service;

import java.util.List;

import poly.dto.CommuDTO;
import poly.dto.DataDTO;

public interface ICommuService {
	// 크롤링 하기
	public int collectSlrData() throws Exception;
	// 크롤링 하기
	public int collectDcComData() throws Exception;
	// 크롤링 하기
	public int collectPpomData() throws Exception;
	// 크롤링 하기
	public int collect82CookData() throws Exception;
	// 크롤링 하기
	public int collectMPData() throws Exception;

	// 크롤링 데이터 가져오기
	public List<CommuDTO> getData(String colNm) throws Exception;

	// 분석 데이터 가져오기
	public List<DataDTO> getAnalysisData(String colNm) throws Exception;

	// 크롤링 데이터 있는지 없는지 확인
	public int checkCrawlingData(List<String> sList) throws Exception;

	// 분석 데이터 있는지 없는지 확인
	public int checkAnalysisData(List<String> sList) throws Exception;

	// 데이터 분석
	public int AnalysisData(String str) throws Exception;

	public void cTest() throws Exception;
	
	//크롤링 중인지 확인
	public boolean checkCrawling(List<String> sList) throws Exception;
	
	// 검색결과 크롤링 데이터 있는지 없는지 확인
	public int checkSearchCrawlingData(List<String> sList, String str) throws Exception;

	// 검색 크롤링 하기
	public int SearchSlrData(String str) throws Exception;
	// 검색 크롤링 하기
	public int SearchDcComData(String str) throws Exception;
	// 검색 크롤링 하기
	public int SearchPpomData(String str) throws Exception;
	// 검색 크롤링 하기
	public int Search82CookData(String str) throws Exception;
	// 검색 크롤링 하기
	public int SearchMPData(String str) throws Exception;
	

}
