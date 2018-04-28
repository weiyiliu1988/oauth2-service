package cn.com.studyshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.studyshop.mapper.ClientMapper;

/**
 * 客户端确认
 * 
 * @author LIU
 *
 */
@Service
public class ClientService {

	@Autowired
	private ClientMapper clientMapper;

	public Boolean checkClientId(String clientId) {
		return clientMapper.countClientId(clientId) == 1;
	}

	public Boolean checkClientSecret(String clientId, String clientSecret) {
		return clientMapper.countClientIdSecret(clientId, clientSecret) == 1;
	}

}
