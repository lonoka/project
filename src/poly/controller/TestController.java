package poly.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.util.UrlUtil;

public class TestController {
	private Logger log = Logger.getLogger(this.getClass());

	@RequestMapping(value = "pTest")
	public String pTest(HttpServletRequest request, Model model, HttpSession session) throws Exception {
		UrlUtil uu = new UrlUtil();

		String url = "http://127.0.0.1:8000";
		String api = "/myTextAPIForJSON";
		String pName = "?pText=";
		String pText = "이협건은 한국폴리텍대학 서울강서캠퍼스 교수이다.";

		String res = uu.urlReadforString(url + api + pName + URLEncoder.encode(pText, "UTF-8"));

		System.out.println("res : " + res);

		uu = null;

		JSONParser parser = new JSONParser();

		JSONObject json = (JSONObject) parser.parse(res);

		List<String> rList = (List<String>) json.get("word");

		if (rList == null) {
			rList = new ArrayList<String>();
		}

		Iterator<String> it = rList.iterator();

		while (it.hasNext()) {
			String word = (String) it.next();
		}

		return json.toString();
	}

}
