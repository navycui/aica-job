package aicluster.framework.common.dao;

import org.springframework.stereotype.Repository;

import aicluster.framework.common.entity.UserDto;

@Repository
public interface FwUserDao {
	UserDto select(String memberId);
}
