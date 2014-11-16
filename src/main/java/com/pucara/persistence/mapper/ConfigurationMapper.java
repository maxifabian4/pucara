package com.pucara.persistence.mapper;

import org.apache.ibatis.annotations.Param;

public interface ConfigurationMapper {

	public void exportTable(@Param("tableName") String tableName,
			@Param("path") String path);

}
