package dev.jinulab.isetdx.hfd.service.get_customer_report_excel;
import java.util.List;

import lombok.Data;
@Data
public class GetCustomerReportExcelOut {
	private boolean success = true;
	private List<GetCustomerReportExcelOutDetail> customerReportDataList;
}
