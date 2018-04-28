package cn.com.studyshop.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.studyshop.entity.Role;
import cn.com.studyshop.mapper.RoleMapper;

@Service
public class RoleService {

	private Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleMapper roleMapper;

	public List<Role> getRoleListByUserName(String userName) {
		List<Role> roleList = new ArrayList<>();
		try {
			roleList = roleMapper.getRoleListByUserName(userName);
		} catch (Exception e) {
			logger.error("异常Exception:{}", e);
		}
		return roleList;

	}
}
