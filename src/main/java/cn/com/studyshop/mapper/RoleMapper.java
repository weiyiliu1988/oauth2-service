package cn.com.studyshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.studyshop.entity.Role;

public interface RoleMapper {

	@Select("SELECT rolelevel,rolecode,cnname,enname,roleinfo FROM t_role WHERE rolecode in "//
			+ "(SELECT rolecode FROM t_user_role WHERE username = #{userName} AND status = true)")
	public List<Role> getRoleListByUserName(@Param("userName") String userName);
}
