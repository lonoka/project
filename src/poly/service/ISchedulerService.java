package poly.service;

public interface ISchedulerService {
	//매 시간 정각 크롤링
	public void sDcComCrawlling() throws Exception;

	//매 시간 정각 크롤링
	public void sSlrCrawlling() throws Exception;

	//매 시간 정각 크롤링
	public void sPpomCrawlling() throws Exception;

	//매 시간 정각 크롤링
	public void s82CookCrawlling() throws Exception;

	//매 시간 정각 크롤링
	public void sMlbCrawlling() throws Exception;
	
}
