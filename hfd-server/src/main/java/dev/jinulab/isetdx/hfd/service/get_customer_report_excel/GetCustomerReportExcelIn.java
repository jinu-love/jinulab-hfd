package dev.jinulab.isetdx.hfd.service.get_customer_report_excel;

import lombok.Data;

@Data
public class GetCustomerReportExcelIn {
	private String fromDate;
	private String toDate;
	private String dept;
}
