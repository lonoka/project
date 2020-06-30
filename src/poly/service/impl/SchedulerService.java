package poly.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import poly.service.ICommuService;
import poly.service.ISchedulerService;

@Component
@Service("SchedulerService")
public class SchedulerService implements ISchedulerService {

	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "CommuService")
	private ICommuService commuService;
	
	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sDcComCrawlling() throws Exception {
		List<String> sList = new ArrayList<String>();
		sList.add("DcCom_");
		boolean crawling = commuService.checkCrawling(sList);
		if(crawling==true) {
			commuService.checkCrawlingData(sList);
			commuService.checkAnalysisData(sList);
		}
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sSlrCrawlling() throws Exception {
		List<String> sList = new ArrayList<String>();
		sList.add("Slr_");
		boolean crawling = commuService.checkCrawling(sList);
		if(crawling==true) {
			commuService.checkCrawlingData(sList);
			commuService.checkAnalysisData(sList);
		}
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sPpomCrawlling() throws Exception {
		List<String> sList = new ArrayList<String>();
		sList.add("Ppom_");
		boolean crawling = commuService.checkCrawling(sList);
		if(crawling==true) {
			commuService.checkCrawlingData(sList);
			commuService.checkAnalysisData(sList);
		}
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void s82CookCrawlling() throws Exception {
		List<String> sList = new ArrayList<String>();
		sList.add("82Cook_");
		boolean crawling = commuService.checkCrawling(sList);
		if(crawling==true) {
			commuService.checkCrawlingData(sList);
			commuService.checkAnalysisData(sList);
		}
		
	}

	@Scheduled(cron = "0 5 0/1 * * ?")
	@Override
	public void sMlbCrawlling() throws Exception {
		List<String> sList = new ArrayList<String>();
		sList.add("Mlb_");
		boolean crawling = commuService.checkCrawling(sList);
		if(crawling==true) {
			commuService.checkCrawlingData(sList);
			commuService.checkAnalysisData(sList);
		}
		
	}

}
