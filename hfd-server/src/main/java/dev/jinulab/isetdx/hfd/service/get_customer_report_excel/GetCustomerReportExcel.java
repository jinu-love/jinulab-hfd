package dev.jinulab.isetdx.hfd.service.get_customer_report_excel;

import java.util.List;

import dev.jinulab.dbist.dml.Query;
import dev.jinulab.framework.exception.BizException;
import dev.jinulab.framework.util.DbUtils;
import dev.jinulab.framework.util.ValueUtils;

public class GetCustomerReportExcel {

	public GetCustomerReportExcelOut getCustomerReportExcel(GetCustomerReportExcelIn input) throws Exception {
		
		if(ValueUtils.isEmpty(input.getFromDate())) {
			throw new BizException("ERR-006", "From Date를 선택해주세요.");
		}
		
		if(ValueUtils.isEmpty(input.getToDate())) {
			throw new BizException("ERR-007", "To Date를 선택해주세요.");
		}
		
		if(ValueUtils.isEmpty(input.getDept())) {
			throw new BizException("ERR-008", "사업부를 선택해주세요.");
		}
		
		Query query = new Query();
		query.addFilter("fromDate", input.getFromDate());
		query.addFilter("toDate", input.getToDate());
		query.addFilter("dept", input.getDept());
		List<GetCustomerReportExcelOutDetail> detailList = DbUtils.selectList(DbUtils.toSqlPath(this.getClass(), "select_customer_report_excel.sql"),
				query, GetCustomerReportExcelOutDetail.class);

		GetCustomerReportExcelOut output = new GetCustomerReportExcelOut();
		output.setSuccess(true);
		output.setCustomerReportDataList(detailList);
		return output;
	}

}
