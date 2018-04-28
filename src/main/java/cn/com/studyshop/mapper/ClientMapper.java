package cn.com.studyshop.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientMapper {

	@Select("SELECT COUNT(1) FROM oauth2_client WHERE client_id = #{clientId}")
	public int countClientId(@Param("clientId") String clientId);

	@Select("SELECT COUNT(1) FROM oauth2_client WHERE client_id = #{clientId} AND client_secret = #{clientSecret}")
	public int countClientIdSecret(@Param("clientId") String clientId, @Param("clientSecret") String clientSecret);

}
