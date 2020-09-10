package poly.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;

public class UrlUtil {

	private Logger log = Logger.getLogger(this.getClass());
	
	private String readAll(Reader rd) {
		log.info("readAll Start!");

		StringBuilder sb = null;

		try {
			sb = new StringBuilder();
			int cp = 0;

			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
		} catch (Exception e) {
			log.info("readAll Exception : " + e.toString());
		}
		log.info("readAll end!");

		return sb.toString();
	}

	public String urlReadforString(String url) throws IOException {
		log.info("urlReadforString Start!");
		log.info("urlReadforString url : " + url);

		BufferedReader rd = null;
		InputStream is = null;

		String res = "";

		try {
			is = new URL(url).openStream();
			rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			res = readAll(rd);
		} catch (Exception e) {
			log.info("urlReadforString Exception : " + e.toString());
		} finally {
			is.close();
			is = null;
			rd = null;
		}
		log.info("urlReadforString end!");

		return res;
	}
}
