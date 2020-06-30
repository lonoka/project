package poly.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import poly.dto.UserDTO;
import poly.dto.UserListDTO;
import poly.persistance.mapper.IUserMapper;
import poly.service.IUserService;
import poly.util.CmmUtil;

@Service("UserService")
public class UserService implements IUserService {

	@Resource(name = "UserMapper")
	private IUserMapper userMapper;
	
	@Override
	public int insertUserInfo(UserDTO pDTO) throws Exception {
		// 가입성공 : 1, 중복으로 인한 취소 : 2, 기타 에러 : 0
		int res = 0;

		// controller 에서 값이 안넘어오는 경우를 대비
		if (pDTO == null) {
			pDTO = new UserDTO();
		}

		// 중복 방지를 위해 데이터 조회
		int check_res = userMapper.ckeckId(pDTO);

		// 중복된 회원정보가 있는 경우, 결과값을 2로 변경하고 작업을 진행 안함
		if (check_res>0) {
			res = 2;
		}
		// 중복된 정보가 없는경우 작업을 진행
		else {
			// 회원가입
			int success = userMapper.insertUserInfo(pDTO);

			// db에 등록됬는지 확인
			if (success > 0) {
				res = 1;
			} else {
				res = 0;
			}
		}
		return res;
	}

	@Override
	public int ckeckId(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.ckeckId(pDTO);
	}
	
	@Override
	public int ckeckMail(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.ckeckMail(pDTO);
	}

	@Override
	public UserDTO getLogin(UserDTO uDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getLogin(uDTO);
	}

	@Override
	public UserDTO getIdInfo(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getIdInfo(pDTO);
	}

	@Override
	public UserDTO getPwInfo(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getPwInfo(pDTO);
	}

	@Override
	public int updatePwInfo(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.updatePwInfo(pDTO);
	}

	@Override
	public int updateUserInfo(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.updateUserInfo(pDTO);
	}

	@Override
	public UserDTO getUserInfo(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserInfo(pDTO);
	}

	@Override
	public List<UserDTO> getUserList(UserListDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getUserList(pDTO);
	}

	@Override
	public int getTotal(UserListDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.getTotal(pDTO);
	}

	@Override
	public int modifyAuthor(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.modifyAuthor(pDTO);
	}

	@Override
	public int deleteUserInfo(UserDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return userMapper.deleteUserInfo(pDTO);
	}

}
