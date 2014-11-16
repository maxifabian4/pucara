package com.pucara.core.services.configuration;

import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pucara.common.CommonMessageError;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.response.Response;
import com.pucara.core.services.mybatis.MyBatisUtil;
import com.pucara.persistence.mapper.ConfigurationMapper;

/**
 * This service allows to modify all system properties.
 * 
 * @author Maximiliano
 */
public class ConfigurationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigurationService.class);

	/**
	 * Exports an specific entity/table from the database to destination path.
	 * 
	 * @param table
	 *            Entity to be exported.
	 * @param destination
	 *            Destination folder path.
	 * @return Simple {@link Response} object.
	 */
	public static Response exportEntity(String table, String destination) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			ConfigurationMapper configurationMapper = sqlSession
					.getMapper(ConfigurationMapper.class);
			configurationMapper.exportTable(table, destination);

			LOGGER.info("Entity '{}' has been exported into {}", table,
					destination);
			return new Response();
		} catch (PersistenceException pe) {
			final Throwable cause = pe.getCause();

			if (cause instanceof SQLException) {
				SQLException sqle = (SQLException) cause;
				LOGGER.error("SQL error: {} - Error code: {}",
						sqle.getMessage(), sqle.getErrorCode());

				return new Response(new ErrorMessage(ErrorType.MYSQL_ERROR,
						String.format(CommonMessageError.EXPORT_ERROR, table)));
			}

			LOGGER.error("Generic error: {}", cause.getMessage());
			return new Response(new ErrorMessage(ErrorType.MYSQL_ERROR,
					cause.getMessage()));
		} finally {
			sqlSession.close();
		}
	}

}
