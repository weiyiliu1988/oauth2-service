package cn.com.studyshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.studyshop.entity.SystemInfo;

public interface SystemInfoMapper {

	@Select("SELECT syscode,cnname,enname FROM t_system WHERE syscode in "//
			+ "(SELECT syscode FROM t_user_system WHERE username = #{userName} AND status = true)")
	public List<SystemInfo> getSystemInfoListByUserName(@Param("userName") String userName);
}
