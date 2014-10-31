package com.pucara.persistence.mapper;

import java.util.List;

import com.pucara.persistence.domain.DailyExpensesHelper;

public interface ReportMapper {

	public List<DailyExpensesHelper> getDailyReport();

}
