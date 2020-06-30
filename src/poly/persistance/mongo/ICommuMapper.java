package poly.persistance.mongo;

import java.util.ArrayList;
import java.util.List;

import poly.dto.CheckDTO;
import poly.dto.CommuDTO;
import poly.dto.DataDTO;

public interface ICommuMapper {
	/**
	 * MongoDB 컬랙션 생성하기
	 * 
	 * @param colNm 생성하는 컬렉션 이름
	 */
	public boolean createCollection(String colNm) throws Exception;

	/**
	 * MongoDB 크롤링 데이터 저장하기
	 */
	public int insertData(List<CommuDTO> pList, String colNm) throws Exception;

	/**
	 * MongoDB 크롤링 데이터 가져오기
	 * 
	 * @param colNm 가져올 컬랙션 이름
	 */
	public List<CommuDTO> getData(String colNm) throws Exception;

	/**
	 * MongoDB 분석 데이터 저장하기
	 */
	public int insertAnalysisData(List<DataDTO> pList, String colNm);

	/**
	 * MongoDB 분석 데이터 가져오기
	 */
	public List<DataDTO> getAnalysisData(String colNm) throws Exception;
	/**
	 * MongoDB 크롤링 확인데이터 체크
	 */
	public boolean checkData(String string);
	/**
	 * MongoDB 크롤링 확인데이터 저장
	 */
	public int insertCheckData(String colNm);
	/**
	 * MongoDB 크롤링 확인데이터 업데이트
	 */
	public int updateCheckData(String colNm);
}
