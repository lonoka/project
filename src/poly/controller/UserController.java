package poly.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import poly.dto.MailDTO;
import poly.dto.UserDTO;
import poly.dto.UserListDTO;
import poly.service.IMailService;
import poly.service.IUserService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

@Controller
public class UserController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "MailService")
	private IMailService mailService;

	@Resource(name = "UserService")
	private IUserService userService;

	/*
	 * 회원가입 로직 처리
	 */
	@RequestMapping(value = "insertUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public String insertUserInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		log.info(this.getClass().getName() + "insertUserInfo Start");

		// 회원가입 결과 메시지 전달 변수
		String msg = "";

		// 웹에서 받는 정보를 저장할 변수
		UserDTO pDTO = null;

		/*
		 * 웹 에서 받는 정보를 String 변수에 저장 시작 무조건 웹으로 받는 정보는 DTO에 저장하기 위해 임시로 String에 저장
		 */
		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		String user_name = CmmUtil.nvl(request.getParameter("user_name"));
		String password = CmmUtil.nvl(request.getParameter("password"));
		String user_mail = CmmUtil.nvl(request.getParameter("user_mail"));
		/*
		 * 웹 에서 받는 정보를 String 변수에 저장 끝 무조건 웹으로 받는 정보는 DTO에 저장하기 위해 임시로 String에 저장
		 */

		/*
		 * 값을 받는 경우 반드시 로그를 찍어서 값이 들어오는지 확인
		 */
		log.info("user_id : " + user_id);
		log.info("user_name : " + user_name);
		log.info("password : " + password);
		log.info("user_mail : " + user_mail);

		/*
		 * 웹에서 받는 정보를 DTO에 저장하기 시작 무조건 웹으로 받는 정보는 DTO에 저장
		 */
		// 받는 정보를 저장할 변수를 메모리에 올리기
		pDTO = new UserDTO();
		pDTO.setUser_id(user_id);
		pDTO.setUser_name(user_name);

		// 비밀번호는 절대적으로 복호화되지 않도록 해시 알고리즘으로 암호화함
		pDTO.setPassword(EncryptUtil.encHashSHA256(password));

		// 민감 정보인 이메일은 AES128-CBC로 암호화함
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));
		/*
		 * 웹에서 받는 정보를 DTO에 저장하기 끝 무조건 웹으로 받는 정보는 DTO에 저장
		 */

		// 회원가입
		int res = userService.insertUserInfo(pDTO);

		if (res == 1) {
			msg = "회원가입되었습니다.";
			return "0";
		} else if (res == 2) {
			msg = "이미 가입된 회원입니다.";
			return "1";
		} else {
			msg = "오류로 인해 회원가입이 실패하였습니다.";
			return "2";
		}
	}

	// ID 체크
	@RequestMapping(value = "checkId")
	@ResponseBody
	public int checkId(HttpServletRequest request) throws Exception {

		String user_id = request.getParameter("user_id");

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_id(user_id);

		return userService.ckeckId(pDTO);
	}

	// Email 체크
	@RequestMapping(value = "checkMail")
	@ResponseBody
	public int checkMail(HttpServletRequest request) throws Exception {

		String user_mail = request.getParameter("user_mail");

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));

		return userService.ckeckMail(pDTO);
	}

	// 로그인 버튼
	@RequestMapping(value = "Loginbtn", method = RequestMethod.POST)
	@ResponseBody
	public String Loginbtn(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {
		String user_id = request.getParameter("user_id");
		String password = request.getParameter("password");

		UserDTO uDTO = new UserDTO();
		uDTO.setUser_id(user_id);
		uDTO.setPassword(EncryptUtil.encHashSHA256(password));
		uDTO = userService.getLogin(uDTO);

		if (uDTO == null) {
			return "0";
		} else if (uDTO.getUser_author().equals("1")) {
			session.setAttribute("userAuthor", uDTO.getUser_author());
			session.setAttribute("userId", uDTO.getUser_id());
			session.setAttribute("userName", uDTO.getUser_name());
			return "1";
		} else {
			session.setAttribute("userAuthor", uDTO.getUser_author());
			session.setAttribute("userId", uDTO.getUser_id());
			session.setAttribute("userName", uDTO.getUser_name());
			return "2";
		}

	}

	// 로그아웃 버튼
	@RequestMapping(value = "Logoutbtn")
	public String Logoutbtn(HttpSession session, ModelMap model) throws Exception {
		session.invalidate();

		model.addAttribute("msg", "로그아웃 하셨습니다.");
		model.addAttribute("url", "/index.do");
		return "/redirect";
	}

	// 사용자 정보 업데이트
	@RequestMapping(value = "updateUserInfo", method = RequestMethod.POST)
	@ResponseBody
	public String userModifyProc(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		String user_name = CmmUtil.nvl(request.getParameter("user_name"));
		String password = CmmUtil.nvl(request.getParameter("password"));

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_id(user_id);
		pDTO.setUser_name(user_name);

		// 비밀번호는 절대적으로 복호화되지 않도록 해시 알고리즘으로 암호화함
		pDTO.setPassword(EncryptUtil.encHashSHA256(password));

		int res = userService.updateUserInfo(pDTO);

		if (res == 1) {
			return "1";
		} else {
			return "0";
		}
	}

	// ID찾기
	@RequestMapping(value = "findID", method = RequestMethod.POST)
	@ResponseBody
	public UserDTO findId(HttpServletRequest request, ModelMap model) throws Exception {
		String user_name = request.getParameter("user_name");// userName 은 DTO와 같게 지정 parameter값은 jsp의 name값이랑 같게 지정
		String user_mail = request.getParameter("user_mail");

		UserDTO pDTO = new UserDTO(); // 보내는 통

		pDTO.setUser_name(user_name);
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));

		pDTO = userService.getIdInfo(pDTO);

		if (pDTO == null) {
			pDTO = new UserDTO();
			return pDTO;
		} else {
			return pDTO;
		}

	}

	// PW찾기 버튼 기능/
	@RequestMapping(value = "findPW", method = RequestMethod.POST)
	@ResponseBody
	public String findPW(HttpServletRequest request, ModelMap model) throws Exception {
		String user_name = request.getParameter("user_name");
		String user_id = request.getParameter("user_id");
		String user_mail = request.getParameter("user_mail");
		log.info(user_name);
		log.info(user_id);
		log.info(user_mail);

		UserDTO pDTO = new UserDTO();
		pDTO.setUser_id(user_id);
		pDTO.setUser_name(user_name);
		pDTO.setUser_mail(EncryptUtil.encAES128CBC(user_mail));

		pDTO = userService.getPwInfo(pDTO);

		if (pDTO == null) {
			return "0";
		} else {

			// 10글자 짜리 비밀번호가 NewPw에 들어감
			String NewPw = getNewPw();
			pDTO.setUser_id(user_id);
			pDTO.setPassword(EncryptUtil.encHashSHA256(NewPw));
			int result = 0;
			try {
				result = userService.updatePwInfo(pDTO);
			} catch (Exception e) {
				e.printStackTrace();
				return "1";
			}

			String title = "새로운 비밀번호";
			String contents = "새로운 비밀번호는 " + NewPw + "입니다.";

			// 메일 발송할 정보를 넣을 DTO객체 생성
			MailDTO mDTO = new MailDTO();

			mDTO.setToMail(user_mail); // 받는 사람 정보 DTO에 저장
			mDTO.setTitle(title); // 제목을 DTO에 저장
			mDTO.setContents(contents); // 내용을 DTO에 저장

			// 메일 발송하기
			int res = mailService.doSendMail(mDTO);

			if (res == 1) {
				// 발송 성공 로그 찍기
				log.info(this.getClass().getName() + "mail.sendMail success!!!");
			} else {
				// 발송 실패 로그 찍기
				log.info(this.getClass().getName() + "mail.sendMail fail!!!");
				return "1";
			}
			return "2";
		}

	}

	// 고객문의 메일 발송
	@RequestMapping(value = "contactSend", method = RequestMethod.POST)
	@ResponseBody
	public String contactSend(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		// 로그 찍기
		log.info(this.getClass().getName() + "mail.contactSend start!");

		String contactName = CmmUtil.nvl(request.getParameter("contactName"));
		String contactTel = CmmUtil.nvl(request.getParameter("contactTel"));
		String contactEmail = CmmUtil.nvl(request.getParameter("contactEmail"));
		String contactMessage = CmmUtil.nvl(request.getParameter("contactMessage"));

		// 메일 발송할 정보를 넣을 DTO객체 생성
		MailDTO pDTO = new MailDTO();

		pDTO.setContactName(contactName);
		pDTO.setContactTel(contactTel);
		pDTO.setContactEmail(contactEmail);
		pDTO.setContactMessage(contactMessage);

		// 메일 발송하기
		int res = mailService.doSendContact(pDTO);

		log.info(this.getClass().getName() + "mail.contactSend end!");
		if (res == 1) {
			// 발송 성공 로그 찍기
			log.info(this.getClass().getName() + "mail.contactSend success!!!");
			return "1";
		} else {
			// 발송 실패 로그 찍기
			log.info(this.getClass().getName() + "mail.contactSend fail!!!");
			return "0";
		}
		// 로그 찍기

	}

	// 사용자 정보 조회
	@RequestMapping(value = "getUserInfo")
	@ResponseBody
	public UserDTO getUserInfo(HttpServletRequest request) throws Exception {

		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		UserDTO pDTO = new UserDTO();

		if (user_id.equals("")) {
			pDTO.setUser_name("로그인오류");
			return pDTO;
		}

		pDTO.setUser_id(user_id);

		pDTO = userService.getUserInfo(pDTO);
		if (pDTO == null) {
			pDTO = new UserDTO();
			pDTO.setUser_name("로그인오류");
			return pDTO;
		} else {
			pDTO.setUser_mail(EncryptUtil.decAES128CBC(pDTO.getUser_mail()));
		}
		return pDTO;
	}

	// 사용자 권한 변경
	@RequestMapping(value = "modifyAuthor")
	@ResponseBody
	public String modifyAuthor(HttpServletRequest request) throws Exception {

		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		UserDTO pDTO = new UserDTO();

		if (user_id.equals("")) {
			return "0";
		}

		pDTO.setUser_id(user_id);

		pDTO = userService.getUserInfo(pDTO);
		if (pDTO == null) {
			return "0";
		} else {
			if (pDTO.getUser_author().equals("0")) {
				int result = userService.modifyAuthor(pDTO);
				if (result > 0) {
					return "1";
				} else {
					return "0";
				}
			} else {
				int result = userService.modifyAuthor(pDTO);
				if (result > 0) {
					return "2";
				} else {
					return "0";
				}
			}
		}
	}

	// 사용자 권한 변경
	@RequestMapping(value = "deleteUserInfo")
	@ResponseBody
	public String deleteUserInfo(HttpServletRequest request) throws Exception {

		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		UserDTO pDTO = new UserDTO();

		if (user_id.equals("")) {
			return "0";
		}

		pDTO.setUser_id(user_id);
		int result = userService.deleteUserInfo(pDTO);
		if(result>0) {
			return "1";
		}else {
			return "0";
		}
	}

	// 사용자 정보 조회
	@RequestMapping(value = "getUserList")
	@ResponseBody
	public UserListDTO getUserList(HttpServletRequest request) throws Exception {

		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		String user_author = CmmUtil.nvl(request.getParameter("user_author"));
		String pgNum = CmmUtil.nvl(request.getParameter("pgNum"));
		String searchCont = CmmUtil.nvl(request.getParameter("searchCont"));
		String searchSelect = CmmUtil.nvl(request.getParameter("searchSelect"));
		log.info(searchSelect);
		log.info(searchCont);

		UserListDTO pDTO = new UserListDTO();

		if (user_id.equals("")) {
			pDTO.setCheckNum(0);
			return pDTO;
		}
		if (user_author.equals("")) {
			pDTO.setCheckNum(0);
			return pDTO;
		}
		if (user_author.equals("0")) {
			pDTO.setCheckNum(1);
			return pDTO;
		}
		if (pgNum.equals("")) {
			pgNum = "1";
		}
		if(!searchCont.equals("")) {
			pDTO.setSearchCont(searchCont);
			pDTO.setSearchSelect(searchSelect);
		}else {
			pDTO.setSearchCont("");
			pDTO.setSearchSelect("");
		}
		int totalNum = userService.getTotal(pDTO);
		pDTO.setPgNum((Integer.parseInt(pgNum) - 1) * 10);
		List<UserDTO> pList = userService.getUserList(pDTO);
		if (pList == null) {
			pList = new ArrayList<UserDTO>();
		} else {
			for (int i = 0; i < pList.size(); i++) {
				pList.get(i).setUser_mail(EncryptUtil.decAES128CBC(pList.get(i).getUser_mail()));
				if (pList.get(i).getUser_author().equals("0")) {
					pList.get(i).setUser_author("사용자");
				} else if (pList.get(i).getUser_author().equals("1")) {
					pList.get(i).setUser_author("관리자");
				}
			}
		}
		pDTO.setPgNum(Integer.parseInt(pgNum));
		pDTO.setStartPg(((Integer.parseInt(pgNum) - 1) / 5) * 5 + 1);
		pDTO.setCheckNum(2);
		pDTO.setuList(pList);
		pDTO.setTotalNum(totalNum);
		pDTO.setTotalPg((totalNum - 1) / 10 + 1);

		return pDTO;
	}

	public String getNewPw() throws Exception {
		// 비밀번호 배열을 생성
		char[] charSet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		// 비밀번호를 받기 위한 문자열 버퍼 생성
		// 비밀번호를 담는 틀 생성
		StringBuffer newKey = new StringBuffer();

		// 10번 반복
		for (int i = 0; i < 10; i++) {
			// 비밀번호 배열 길이*랜덤으로 생성된 숫자
			// random() 난수가 복잡하지 않기 떄문에 숫자를 더 복잡하게 해줌
			int idx = (int) (charSet.length * Math.random());
			// 문자열에다가 한글자씩 담는것
			newKey.append(charSet[idx]);
		}

		// 스트링 버퍼를 스트링형태로 바꿔서 반환해줌
		return newKey.toString();
	}
}
