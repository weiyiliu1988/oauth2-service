package cn.com.studyshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.studyshop.oauth2.entity.OAuthUser;
import cn.com.studyshop.service.UserService;

/**
 * 根据token令牌 获取保护资源
 * 
 * @author LIU
 *
 */
@RestController
public class UserInfoController {

	private Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("/userInfo")
	public HttpEntity userInfo(HttpServletRequest request) throws OAuthSystemException {

		try {

			System.out.println(request.getHeader("oauth_js_id"));

			Session session = SecurityUtils.getSecurityManager()
					.getSession(new DefaultSessionKey(request.getHeader("oauth_js_id")));
			// 构建OAuth资源请求
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
			// 获取Access Token
			String accessToken = oauthRequest.getAccessToken();
			logger.debug("令牌获取token:{}", accessToken);
			logger.debug("校验Session Id{}", session.getId());
			logger.debug("令牌获取token的值:{}", session.getAttribute(accessToken));
			// 验证Access Token
			if (null == session.getAttribute(accessToken)
					|| session.getAttribute(accessToken).toString().trim().length() == 0) {

				// if (null == session.getId()) {
				// 如果不存在/过期了，返回未验证错误，需重新验证
				OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.ResourceResponse.INVALID_TOKEN).buildHeaderMessage();

				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
						oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
			}
			String username = (String) session.getAttribute(accessToken);
			OAuthUser oAuthUser = userService.makeOAuthUser(username);
			return new ResponseEntity(oAuthUser, HttpStatus.OK);
		} catch (OAuthProblemException e) {
			// 检查是否设置了错误码
			String errorCode = e.getError();
			if (OAuthUtils.isEmpty(errorCode)) {
				OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setRealm("error realm").buildHeaderMessage();

				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
						oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
			}

			OAuthResponse oauthResponse = OAuthRSResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
					.setRealm("error realm").setError(e.getError()).setErrorDescription(e.getDescription())
					.setErrorUri(e.getUri()).buildHeaderMessage();

			HttpHeaders headers = new HttpHeaders();
			headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
