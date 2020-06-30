package poly.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.dto.MailDTO;
import poly.service.IMailService;
import poly.util.CmmUtil;

@Service("MailService") // 컨트롤러에서 찾을 이름
public class MailService implements IMailService {

	final String user = "dabookshelf2019@gmail.com";
	final String password = "scarlet14!";

	// 로그 파일 생성 및 로그 출력을 위한 log4h 프레임워크의 자바 객체
	private Logger log = Logger.getLogger(this.getClass());

	@Override
	public int doSendContact(MailDTO pDTO) throws Exception {
		// 로그 찍기
		log.info(this.getClass().getName() + ".doSendContact start!");

		// 메일 발송 성공여부 확인
		int res = 1;

		// 전달 받은 DTO로부터 데이터 받기
		if (pDTO == null) {
			pDTO = new MailDTO();
		}

		// SMTP 서버 정보를 설정한다.
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.socketFactory.fallback", "false");
		prop.setProperty("mail.smtp.quitwait", "false");

		// 네이버 SMTP서버 인증 처리 로직
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("dabookshelf2019@gmail.com"));

			// 메일 제목
			message.setSubject("CA. 고객문의 이메일입니다.");

			// 메일 내용
			message.setText(" 문의자 : " + pDTO.getContactName() + "\n 전화번호 : " + pDTO.getContactTel() + "\n 이메일 : "
					+ pDTO.getContactEmail() + "\n 문의내용 : " + pDTO.getContactMessage());

			// 메일 발송
			Transport.send(message);

		} catch (MessagingException e) {
			res = 0;
			log.info("[ERROR]" + this.getClass().getName() + ".doSendContact : " + e);
		} catch (Exception e) {
			res = 0;
			log.info("[ERROR]" + this.getClass().getName() + ".doSendContact : " + e);
		}
		pDTO = null;
		// 로그 찍기
		log.info(this.getClass().getName() + ".doSendContact end!");
		return res;
	}

	@Override
	public int doSendMail(MailDTO pDTO) throws Exception {
		// 로그 찍기
		log.info(this.getClass().getName() + ".doSendMail start!");

		// 메일 발송 성공여부 확인
		int res = 1;

		// 전달 받은 DTO로부터 데이터 받기
		if (pDTO == null) {
			pDTO = new MailDTO();
		}

		String toMail = CmmUtil.nvl(pDTO.getToMail()); // 받는사람
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		prop.put("mail.smtp.socketFactory.fallback", "false");
		prop.setProperty("mail.smtp.quitwait", "false");

		// 네이버 SMTP서버 인증 처리 로직
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));

			// 메일 제목
			message.setSubject(CmmUtil.nvl(pDTO.getTitle()));

			// 메일 내용
			message.setText(CmmUtil.nvl(pDTO.getContents()));

			// 메일 발송
			Transport.send(message);

		} catch (MessagingException e) {
			res = 0;
			log.info("[ERROR]" + this.getClass().getName() + ".doSendMail : " + e);
		} catch (Exception e) {
			res = 0;
			log.info("[ERROR]" + this.getClass().getName() + ".doSendMail : " + e);
		}

		// 로그 찍기
		log.info(this.getClass().getName() + ".doSendMail end!");
		return res;
	}

}
