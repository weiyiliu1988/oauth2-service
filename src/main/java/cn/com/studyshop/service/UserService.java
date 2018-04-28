package cn.com.studyshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.studyshop.entity.User;
import cn.com.studyshop.mapper.UserMapper;
import cn.com.studyshop.oauth2.entity.OAuthUser;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private RoleService roleService;

	@Autowired
	private SystemInfoService systemInfoService;

	@Autowired
	private OwnerService ownerService;

	public String getUserPass(String userName) {
		return userMapper.getUserPass(userName);
	}

	public User getUserByName(String userName) {
		return userMapper.getUserByName(userName);
	}

	public OAuthUser makeOAuthUser(String userName) {
		OAuthUser oAuthUser = new OAuthUser();
		oAuthUser.setUser(getUserByName(userName));
		oAuthUser.setRolelist(roleService.getRoleListByUserName(userName));
		oAuthUser.setSysteminfolist(systemInfoService.getSystemInfoListByUserName(userName));
		oAuthUser.setOwnerlist(ownerService.getOwnerList(userName));
		return oAuthUser;

	}
}
