package com.pucara.core.services.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.report.ChartInfoElement;
import com.pucara.core.response.ChartInfoResponse;
import com.pucara.core.response.DailyExpensesResponse;
import com.pucara.core.response.SaleDailyReportResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;
import com.pucara.core.services.mybatis.MyBatisUtil;
import com.pucara.persistence.mapper.ReportMapper;

/**
 * 
 * @author Maximiliano Fabian
 */
public class ReportService {

	/**
	 * 
	 * @return
	 */
	public static SaleDailyReportResponse getDailySaleReport() {
		ResultSet saleReportStatement = MySqlAccess
				.getDailySaleReportFromView();

		try {
			double gain = 0;
			double sold = 0;
			int quantity = 0;

			while (saleReportStatement.next()) {
				gain += saleReportStatement.getDouble("gain");
				sold += saleReportStatement.getDouble("sold");
				quantity += saleReportStatement.getInt("count");
			}

			return new SaleDailyReportResponse(gain, sold, quantity);
		} catch (SQLException e) {
			return new SaleDailyReportResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static ChartInfoResponse getChartByDayInfo(int prevDays,
			String statemenType) {
		ResultSet chartReportStatement = MySqlAccess
				.getCharInformationFromSchema(String.format(statemenType,
						prevDays));
		List<ChartInfoElement> responses = new ArrayList<ChartInfoElement>();
		String dayName;
		Double value;

		try {
			while (chartReportStatement.next()) {
				dayName = chartReportStatement.getString("day");
				value = chartReportStatement.getDouble("value");

				responses.add(new ChartInfoElement(dayName, value));
			}

			return new ChartInfoResponse(responses);
		} catch (SQLException e) {
			// CustomLogger.log(e, LoggerLevel.ERROR,
			// CommonMessageError.STATEMENT_CHART_ERROR);
			return new ChartInfoResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	public static ChartInfoResponse getChartByYearInfo(int currentYear,
			String statemenType) {
		ResultSet chartReportStatement = MySqlAccess
				.getCharInformationFromSchema(String.format(statemenType,
						currentYear));
		List<ChartInfoElement> responses = new ArrayList<ChartInfoElement>();
		String monthName;
		Double value;

		try {
			while (chartReportStatement.next()) {
				monthName = chartReportStatement.getString("month");
				value = chartReportStatement.getDouble("value");

				responses.add(new ChartInfoElement(monthName, value));
			}

			return new ChartInfoResponse(responses);
		} catch (SQLException e) {
			// CustomLogger.log(e, LoggerLevel.ERROR,
			// CommonMessageError.STATEMENT_CHART_ERROR);
			return new ChartInfoResponse(new ErrorMessage(
					ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * This method uses mappers.
	 */
	public static DailyExpensesResponse getDailyExpensesReport(Date date) {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			ReportMapper reportMapper = sqlSession
					.getMapper(ReportMapper.class);
			return new DailyExpensesResponse(reportMapper.getDailyReport(date));
		} finally {
			sqlSession.close();
		}
	}

}
