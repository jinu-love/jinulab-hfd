package dev.jinulab.isetdx.hfd.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.jinulab.isetdx.hfd.service.get_customer_report.GetCustomerReportIn;
import dev.jinulab.isetdx.hfd.service.get_customer_report.GetCustomerReportOut;
import dev.jinulab.isetdx.hfd.service.get_customer_report_excel.GetCustomerReportExcelIn;
import dev.jinulab.isetdx.hfd.service.get_customer_report_excel.GetCustomerReportExcelOut;
import dev.jinulab.isetdx.hfd.service.get_overtime_history.GetOvertimeHistoryIn;
import dev.jinulab.isetdx.hfd.service.get_overtime_history.GetOvertimeHistoryOut;
import dev.jinulab.isetdx.hfd.service.get_select_overtime_status.GetSelectOvertimeStatusIn;
import dev.jinulab.isetdx.hfd.service.get_select_overtime_status.GetSelectOvertimeStatusOut;
import dev.jinulab.isetdx.hfd.service.get_this_month_overtime.GetThisMonthOvertimeIn;
import dev.jinulab.isetdx.hfd.service.get_this_month_overtime.GetThisMonthOvertimeOut;
import dev.jinulab.isetdx.hfd.service.save_dayoff.SaveDayoffIn;
import dev.jinulab.isetdx.hfd.service.save_dayoff.SaveDayoffOut;
import dev.jinulab.isetdx.hfd.service.save_overtime.SaveOvertimeIn;
import dev.jinulab.isetdx.hfd.service.save_overtime.SaveOvertimeOut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RequestMapping(path = "api/v1/")
public interface HappyFridayController {

	// static final String RESOURCE = "/overtime";
	@GetMapping(path = "get/overtime/history")
	GetOvertimeHistoryOut getOvertimeHistory(@ParameterObject GetOvertimeHistoryIn input) throws Exception;
	
	@GetMapping(path = "get/this/month/overtime")
	GetThisMonthOvertimeOut getThisMonthOvertime(@ParameterObject GetThisMonthOvertimeIn input) throws Exception;

	@PostMapping(path = "save/overtime")
	SaveOvertimeOut saveOvertime(@RequestBody SaveOvertimeIn input) throws Exception;
	
	@PostMapping(path = "save/dayoff")
	@Operation(summary = "휴가 등록", security = @SecurityRequirement(name = "Bearer Authentication"))
	SaveDayoffOut saveDayoff(@RequestBody SaveDayoffIn input) throws Exception;

	@GetMapping(path = "get/select/overtime/status")
	@Operation(summary = "휴가 등록", security = @SecurityRequirement(name = "Bearer Authentication"))
	GetSelectOvertimeStatusOut getSelectOvertimeStatus(@ParameterObject GetSelectOvertimeStatusIn input)
			throws Exception;
	
	@GetMapping(path = "get/customer/report")
	@Operation(
	        summary  = "휴가 등록",
	        security = @SecurityRequirement(name = "Bearer Authentication")
	    )
	GetCustomerReportOut getCustomerReport(@ParameterObject GetCustomerReportIn input) throws Exception;
	
	@GetMapping(path = "get/customer/report/excel")
	@Operation(
	        summary  = "휴가 등록",
	        security = @SecurityRequirement(name = "Bearer Authentication")
	    )
	GetCustomerReportExcelOut getCustomerReportExcel(@ParameterObject GetCustomerReportExcelIn input) throws Exception;

}
