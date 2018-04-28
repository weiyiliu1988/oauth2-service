package cn.com.studyshop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import cn.com.studyshop.entity.Owner;

/**
 * 货主Mapper
 * 
 * @author LIU
 *
 */
@Mapper
public interface OwnerMapper {

	@Select("SELECT owner,cnname,enname,ownerinfo,ownerlevel FROM t_owner WHERE owner in (SELECT owner FROM t_user_owner"//
			+ " WHERE username = #{username})")
	public List<Owner> getOwnerList(@Param("username") String username);
}
