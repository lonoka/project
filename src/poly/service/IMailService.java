package poly.service;

import poly.dto.MailDTO;

public interface IMailService {

	int doSendContact(MailDTO pDTO) throws Exception;

	int doSendMail(MailDTO pDTO) throws Exception;

}
