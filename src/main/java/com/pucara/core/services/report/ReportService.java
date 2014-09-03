package com.pucara.core.services.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pucara.common.CommonMessageError;
import com.pucara.common.CustomLogger;
import com.pucara.common.CustomLogger.LoggerLevel;
import com.pucara.core.database.MySqlAccess;
import com.pucara.core.entities.report.ChartInfoElement;
import com.pucara.core.entities.report.PurchaseDailyReport;
import com.pucara.core.response.ChartInfoResponse;
import com.pucara.core.response.PurchaseDailyReportResponse;
import com.pucara.core.response.SaleDailyReportResponse;
import com.pucara.core.response.ErrorMessage;
import com.pucara.core.response.ErrorType;

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
		ResultSet saleReportStatement = MySqlAccess.getDailySaleReportFromView();

		try {
			saleReportStatement.next();
			double gain = saleReportStatement.getDouble("gain");
			int quantity = saleReportStatement.getInt("count");

			return new SaleDailyReportResponse(gain, quantity);
		} catch (SQLException e) {
			CustomLogger.log(e, LoggerLevel.ERROR, CommonMessageError.STATEMENT_SALE_ERROR);
			return new SaleDailyReportResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @return
	 */
	public static PurchaseDailyReportResponse getDailyPurchaseReport() {
		ResultSet saleReportStatement = MySqlAccess.getDailyPurchaseReportFromView();

		try {
			List<PurchaseDailyReport> responses = new ArrayList<PurchaseDailyReport>();
			String description;
			double expense;

			while (saleReportStatement.next()) {
				description = saleReportStatement.getString("description");
				expense = saleReportStatement.getDouble("expense");

				responses.add(new PurchaseDailyReport(expense, description, null));
			}

			return new PurchaseDailyReportResponse(responses);
		} catch (SQLException e) {
			CustomLogger.log(e, LoggerLevel.ERROR, CommonMessageError.STATEMENT_PURCHASE_ERROR);
			return new PurchaseDailyReportResponse(new ErrorMessage(ErrorType.STATEMENT_ERROR,
					e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static ChartInfoResponse getChartByDayInfo(int prevDays, String statemenType) {
		ResultSet chartReportStatement = MySqlAccess.getCharInformationFromSchema(String.format(
				statemenType, prevDays));
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
			CustomLogger.log(e, LoggerLevel.ERROR, CommonMessageError.STATEMENT_CHART_ERROR);
			return new ChartInfoResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

	public static ChartInfoResponse getChartByYearInfo(int currentYear, String statemenType) {
		ResultSet chartReportStatement = MySqlAccess.getCharInformationFromSchema(String.format(
				statemenType, currentYear));
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
			CustomLogger.log(e, LoggerLevel.ERROR, CommonMessageError.STATEMENT_CHART_ERROR);
			return new ChartInfoResponse(
					new ErrorMessage(ErrorType.STATEMENT_ERROR, e.getMessage()));
		} finally {
			MySqlAccess.closeResultSet();
		}
	}

}