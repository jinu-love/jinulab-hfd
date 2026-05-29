package dev.jinulab.isetdx.hfd.service.get_quarterly_customer_report;

import java.util.List;

import lombok.Data;

@Data
public class GetQuarterlyCustomerReportIn {
	private String selectMonth;
	private String selectToMonth;
	private List<String> month;
	private String dept;
}
