package com.pucara.core.services.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	// public static DailyExpensesResponse getDailyPurchaseReport() {
	// ResultSet saleReportStatement = MySqlAccess
	// .getDailyPurchaseReportFromView();
	//
	// try {
	// List<PurchaseDailyReport> responses = new
	// ArrayList<PurchaseDailyReport>();
	// String description;
	// double expense;
	// Timestamp date;
	//
	// while (saleReportStatement.next()) {
	// description = saleReportStatement.getString("description");
	// expense = saleReportStatement.getDouble("expense");
	// date = saleReportStatement.getTimestamp("date");
	//
	// responses.add(new PurchaseDailyReport(expense, description,
	// date, null));
	// }
	//
	// return new DailyExpensesResponse(responses);
	// } catch (SQLException e) {
	// // CustomLogger.log(e, LoggerLevel.ERROR,
	// // CommonMessageError.STATEMENT_PURCHASE_ERROR);
	// return new DailyExpensesResponse(new ErrorMessage(
	// ErrorType.STATEMENT_ERROR, e.getMessage()));
	// } finally {
	// MySqlAccess.closeResultSet();
	// }
	// }

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
	public static DailyExpensesResponse getDailyExpensesReport() {
		SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory()
				.openSession();
		try {
			ReportMapper reportMapper = sqlSession
					.getMapper(ReportMapper.class);
			return new DailyExpensesResponse(reportMapper.getDailyReport());
		} finally {
			sqlSession.close();
		}
	}

}
