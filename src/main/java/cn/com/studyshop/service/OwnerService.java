package cn.com.studyshop.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.studyshop.entity.Owner;
import cn.com.studyshop.mapper.OwnerMapper;

/**
 * 货主Service
 * 
 * @author LIU
 *
 */
@Service
public class OwnerService {

	private Logger logger = LoggerFactory.getLogger(OwnerService.class);

	@Autowired
	private OwnerMapper ownerMapper;

	public List<Owner> getOwnerList(String username) {
		List<Owner> list = new ArrayList<>();

		try {
			list = ownerMapper.getOwnerList(username);
		} catch (Exception e) {
			logger.debug("异常Exception:{}", e);
		}
		return list;
	}
}
