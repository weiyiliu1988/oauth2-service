package cn.com.studyshop.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.studyshop.entity.User;

public interface UserMapper {

	@Select("SELECT userpass FROM t_user WHERE username = #{userName} and status = true")
	public String getUserPass(@Param("userName") String userName);

	// reserve 组织code
	@Select("SELECT username,email,phone,imagesrc,reserve FROM t_user WHERE username = #{userName}")
	public User getUserByName(@Param("userName") String userName);
}
