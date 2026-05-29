package dev.jinulab.isetdx.hfd.service.get_quarterly_customer_report;

import java.util.List;

import lombok.Data;

@Data
public class GetQuarterlyCustomerReportOut {
	private boolean success = true;
	private List<GetQuarterlyCustomerReportOutDetail> GetQuarterlyCustomerReportOutList;
}
