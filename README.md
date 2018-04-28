# oauth2-service
认证&amp;校验&amp;资源服务器


1. 客户端需要依赖 oauth-client jar包
2. 本服务需根据OAuth2实现 主要为三个阶段
      1. 获取授权code
      2. 获取服务token(利用授权code)
      3. 获取保护资源信息(利用token可以多次获取各种保护资源)
      
3. 底层采用 shiro + redis(授权code 服务token存放地址) springBoot(部分地方未搭,请参考本人其他项目)


4. 请求的URL
     1. 获取授权CODE的uri: http://127.0.0.1:6666/authorize
      
     2. 获取token的uri: http://127.0.0.1:6666/accessToken   携带：授权code+sessionId(服务器侧sessionId)
     
     3. 获取保护资源的uri: http://127.0.0.1:6666/userInfo    携带：令牌token+sessionId(服务器侧sessionId)
     
5. 实现的单点,部分进行了定制化。

