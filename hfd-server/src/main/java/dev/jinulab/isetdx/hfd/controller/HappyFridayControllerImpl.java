package dev.jinulab.isetdx.hfd.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import dev.jinulab.framework.util.BeanUtils;
import dev.jinulab.isetdx.hfd.service.get_customer_report.GetCustomerReport;
import dev.jinulab.isetdx.hfd.service.get_customer_report.GetCustomerReportIn;
import dev.jinulab.isetdx.hfd.service.get_customer_report.GetCustomerReportOut;
import dev.jinulab.isetdx.hfd.service.get_customer_report_excel.GetCustomerReportExcel;
import dev.jinulab.isetdx.hfd.service.get_customer_report_excel.GetCustomerReportExcelIn;
import dev.jinulab.isetdx.hfd.service.get_customer_report_excel.GetCustomerReportExcelOut;
import dev.jinulab.isetdx.hfd.service.get_overtime_history.GetOvertimeHistory;
import dev.jinulab.isetdx.hfd.service.get_overtime_history.GetOvertimeHistoryIn;
import dev.jinulab.isetdx.hfd.service.get_overtime_history.GetOvertimeHistoryOut;
import dev.jinulab.isetdx.hfd.service.get_select_overtime_status.GetSelectOvertimeStatus;
import dev.jinulab.isetdx.hfd.service.get_select_overtime_status.GetSelectOvertimeStatusIn;
import dev.jinulab.isetdx.hfd.service.get_select_overtime_status.GetSelectOvertimeStatusOut;
import dev.jinulab.isetdx.hfd.service.get_this_month_overtime.GetThisMonthOvertime;
import dev.jinulab.isetdx.hfd.service.get_this_month_overtime.GetThisMonthOvertimeIn;
import dev.jinulab.isetdx.hfd.service.get_this_month_overtime.GetThisMonthOvertimeOut;
import dev.jinulab.isetdx.hfd.service.save_dayoff.SaveDayoff;
import dev.jinulab.isetdx.hfd.service.save_dayoff.SaveDayoffIn;
import dev.jinulab.isetdx.hfd.service.save_dayoff.SaveDayoffOut;
import dev.jinulab.isetdx.hfd.service.save_overtime.SaveOvertime;
import dev.jinulab.isetdx.hfd.service.save_overtime.SaveOvertimeIn;
import dev.jinulab.isetdx.hfd.service.save_overtime.SaveOvertimeOut;

@CrossOrigin
@RestController
public class HappyFridayControllerImpl implements HappyFridayController {

	@Override
	public GetOvertimeHistoryOut getOvertimeHistory(GetOvertimeHistoryIn input) throws Exception {
		return BeanUtils.get(GetOvertimeHistory.class).getOvertimeHistory(input);
	}

	@Override
	public GetThisMonthOvertimeOut getThisMonthOvertime(GetThisMonthOvertimeIn input) throws Exception {
		return BeanUtils.get(GetThisMonthOvertime.class).getThisMonthOvertime(input);
	}

	@Override
	public SaveOvertimeOut saveOvertime(SaveOvertimeIn input) throws Exception {
		return BeanUtils.get(SaveOvertime.class).saveOvertime(input);
	}

	@Override
	public SaveDayoffOut saveDayoff(SaveDayoffIn input) throws Exception {
		return BeanUtils.get(SaveDayoff.class).saveDayoff(input);
	}

	@Override
	public GetSelectOvertimeStatusOut getSelectOvertimeStatus(GetSelectOvertimeStatusIn input) throws Exception {
		return BeanUtils.get(GetSelectOvertimeStatus.class).getSelectOvertimeStatus(input);
	}

	@Override
	public GetCustomerReportOut getCustomerReport(GetCustomerReportIn input) throws Exception {
		return BeanUtils.get(GetCustomerReport.class).getCustomerReport(input);
	}

	@Override
	public GetCustomerReportExcelOut getCustomerReportExcel(GetCustomerReportExcelIn input) throws Exception {
		return BeanUtils.get(GetCustomerReportExcel.class).getCustomerReportExcel(input);
	}
	
}
