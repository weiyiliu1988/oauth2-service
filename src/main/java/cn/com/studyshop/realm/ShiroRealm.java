package cn.com.studyshop.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.studyshop.service.UserService;

/**
 * accessControl
 * 
 * @author LIU
 *
 */
public class ShiroRealm extends AuthorizingRealm {

	private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordService passwordService;

	/**
	 * 角色赋予
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		// String loginEmail = principals.getPrimaryPrincipal().toString();

		Session session = SecurityUtils.getSubject().getSession();
		logger.debug("[角色赋予机制] 角色赋予开始!sessionid:{}", session.getId().toString());

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		// User user = (User) session.getAttribute(session.getId().toString());
		// user.getRole().stream().forEach(o -> info.addRole(o));

		// 赋予权限
		// for(Permission
		// permission:permissionService.getByUserId(user.getId())){
		//// if(StringUtils.isNotBlank(permission.getPermCode()))
		// info.addStringPermission(permission.getName());
		// }

		return info;
	}

	// 核心
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken upToken = (UsernamePasswordToken) token;
		String userName = upToken.getUsername();
		String encodePassword = passwordService.encryptPassword(upToken.getPassword());
		logger.debug("shiroRealm---encodePassword---->{}", encodePassword);
		String loginPassword = userService.getUserPass(userName); // encrypt
		if (null == loginPassword || loginPassword.trim().length() == 0) {
			throw new IncorrectCredentialsException("The username or password is incorrect.");
		}

		if (encodePassword.equals(loginPassword)) {

			// Session session = SecurityUtils.getSubject().getSession();
			// logger.debug("[用户登录] token-sessionid:" +
			// session.getId().toString());
			// logger.debug("shiro-realm username:" + upToken.getUsername());
			return new SimpleAuthenticationInfo(userName, encodePassword, getName());
		} else {
			throw new IncorrectCredentialsException("The username or password is incorrect.");
		}
	}
}
