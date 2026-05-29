package dev.jinulab.isetdx.hfd.service.get_this_month_overtime;

import java.time.ZonedDateTime;
import java.util.List;

import dev.jinulab.dbist.dml.Query;
import dev.jinulab.framework.util.DbUtils;
import dev.jinulab.framework.util.ValueUtils;
import dev.jinulab.isetdx.hfd.model.HfdDayoffInf;
import dev.jinulab.isetdx.hfd.model.HfdOvertimeInf;

public class GetThisMonthOvertime {

	public GetThisMonthOvertimeOut getThisMonthOvertime(GetThisMonthOvertimeIn input) throws Exception {

		double monthOvertime = 0.0;

		String thisYearMonth = ValueUtils.toDateStr(ValueUtils.getZonedDateTime(), "YYYYMM");
		String thisMonth = ValueUtils.toDateStr(ValueUtils.getZonedDateTime(), "MM");

		Query query = new Query();

		query.addFilter("accountId", input.getAccountId());
		query.addFilter("overtimeDate", "like", thisYearMonth + "%");
		List<HfdOvertimeInf> hfdOvertimeInfList = DbUtils.selectList(HfdOvertimeInf.class, query);
		for (HfdOvertimeInf hfdOvertimeInf : hfdOvertimeInfList) {
			monthOvertime += hfdOvertimeInf.getOvertimeHours();
		}

		String dayOffDate = "사용안함";
		query = new Query();
		query.addFilter("accountId", input.getAccountId());
		query.addFilter("dayoffDate", "like", thisYearMonth + "%");
		query.setMaxResultSize(1);
		HfdDayoffInf hfdDayoffInf = DbUtils.select(HfdDayoffInf.class, query);
		if (!ValueUtils.isEmpty(hfdDayoffInf)) {
			ZonedDateTime zonedDatetime = ValueUtils.toZonedDateTime(hfdDayoffInf.getDayoffDate());
			dayOffDate = ValueUtils.toDateStr(zonedDatetime, "yyyy-M-dd(E)");
		}

		GetThisMonthOvertimeOut output = new GetThisMonthOvertimeOut();
		output.setOvertime(ValueUtils.toString(monthOvertime).replace(".0", ""));
		output.setThisMonth(thisMonth);
		output.setDayOffDate(dayOffDate);
		output.setSuccess(true);
		return output;
	}

}
