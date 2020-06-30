package poly.dto;

import java.util.List;

public class UserListDTO {
	private int pgNum;
	private int totalNum;
	private List<UserDTO> uList;
	// 0 : 로그인 오류, 1 : 관리자 권한 오류
	private int checkNum;
	private int startPg;
	private int totalPg;
	private String searchCont;
	private String searchSelect;
	
	public int getPgNum() {
		return pgNum;
	}
	public void setPgNum(int pgNum) {
		this.pgNum = pgNum;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public List<UserDTO> getuList() {
		return uList;
	}
	public void setuList(List<UserDTO> uList) {
		this.uList = uList;
	}
	public int getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}
	public int getStartPg() {
		return startPg;
	}
	public void setStartPg(int startPg) {
		this.startPg = startPg;
	}
	public int getTotalPg() {
		return totalPg;
	}
	public void setTotalPg(int totalPg) {
		this.totalPg = totalPg;
	}
	public String getSearchCont() {
		return searchCont;
	}
	public void setSearchCont(String searchCont) {
		this.searchCont = searchCont;
	}
	public String getSearchSelect() {
		return searchSelect;
	}
	public void setSearchSelect(String searchSelect) {
		this.searchSelect = searchSelect;
	}
	
}
