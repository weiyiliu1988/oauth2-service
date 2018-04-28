package cn.com.studyshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

	@Autowired
	private ClientService clientService;

	/**
	 * 客户端ID存在check
	 * 
	 * @param clientId
	 * @return
	 */
	public Boolean checkClientId(String clientId) {
		return clientService.checkClientId(clientId);
	}

	public Boolean checkClientSecret(String clientId, String clientSecret) {
		return clientService.checkClientSecret(clientId, clientSecret);
	}

}
