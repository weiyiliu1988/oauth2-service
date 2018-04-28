package cn.com.studyshop.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.studyshop.entity.SystemInfo;
import cn.com.studyshop.mapper.SystemInfoMapper;

@Service
public class SystemInfoService {

	private Logger logger = LoggerFactory.getLogger(SystemInfoService.class);

	@Autowired
	private SystemInfoMapper systemInfoMapper;

	public List<SystemInfo> getSystemInfoListByUserName(String userName) {
		List<SystemInfo> list = new ArrayList<>();

		try {
			list = systemInfoMapper.getSystemInfoListByUserName(userName);
		} catch (Exception e) {
			logger.error("异常Exception:{}", e);
		}
		return list;
	}
}
