package dev.jinulab.isetdx.hfd.service.get_customer_report;

import lombok.Data;

@Data
public class GetCustomerReportIn {
	private String fromDate;
	private String toDate;
	private String dept;
}
