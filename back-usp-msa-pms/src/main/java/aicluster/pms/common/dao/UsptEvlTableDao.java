package aicluster.pms.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import aicluster.pms.common.entity.UsptEvlTable;
import aicluster.pms.common.entity.UsptEvlTableSearchParam;

@Repository
public interface UsptEvlTableDao {

	List<UsptEvlTable> selectList(UsptEvlTableSearchParam usptEvlTable);

	Long selectListCount(UsptEvlTableSearchParam usptEvlTable);

	UsptEvlTable select(String evlTableId);

	int insert(UsptEvlTable usptEvlTable);

	void update(UsptEvlTable usptEvlTable);

	int selectModifyEnableCnt(@Param("evlTableId") String evlTableId, @Param("evlSttusCd") String evlSttusCd );

}
