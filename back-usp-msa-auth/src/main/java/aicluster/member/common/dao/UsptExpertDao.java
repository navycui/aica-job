package aicluster.member.common.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import aicluster.member.common.entity.UsptExpert;

@Repository
public interface UsptExpertDao {
	List<UsptExpert> selectList_expertNm(String expertNm);
}
