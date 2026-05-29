package dev.jinulab.isetdx.hfd.service.get_customer_report;

import java.util.List;

import lombok.Data;

@Data
public class GetCustomerReportOut {
	private boolean success = true;
	private List<GetCustomerReportOutDetail> customerReportDataList;
}
