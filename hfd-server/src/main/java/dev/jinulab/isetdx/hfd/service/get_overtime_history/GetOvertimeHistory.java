package dev.jinulab.isetdx.hfd.service.get_overtime_history;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dev.jinulab.dbist.dml.Query;
import dev.jinulab.framework.util.DbUtils;
import dev.jinulab.framework.util.ValueUtils;
import dev.jinulab.isetdx.hfd.model.HfdDayoffInf;
import dev.jinulab.isetdx.hfd.model.HfdOvertimeInf;

public class GetOvertimeHistory {

	public GetOvertimeHistoryOut getOvertimeHistory(GetOvertimeHistoryIn input) throws Exception {

		Query query = new Query();
		query.addFilter("accountId", input.getAccountId());
		query.addOrder("overtimeDate", false);
		List<HfdOvertimeInf> hfdOvertimeInfList = DbUtils.selectList(HfdOvertimeInf.class, query);
		
		query = new Query();
		query.addFilter("accountId", input.getAccountId());
		query.addOrder("dayoffDate", false);
		List<HfdDayoffInf> hfdDayoffInfList = DbUtils.selectList(HfdDayoffInf.class, query);

		List<GetOvertimeHistoryOutDetail> detailList = new ArrayList<GetOvertimeHistoryOutDetail>();
		for (HfdOvertimeInf hfdOvertimeInf : hfdOvertimeInfList) {
			GetOvertimeHistoryOutDetail detail = new GetOvertimeHistoryOutDetail();
			ValueUtils.populate(hfdOvertimeInf, detail);
			detailList.add(detail);
		}
		
		for (HfdDayoffInf hfdDayoffInf : hfdDayoffInfList) {
			GetOvertimeHistoryOutDetail detail = new GetOvertimeHistoryOutDetail();
			detail.setOvertimeDate(hfdDayoffInf.getDayoffDate());
			detail.setComment(hfdDayoffInf.getComment());
			detail.setOvertimeHours("휴무");
			detailList.add(detail);
		}

		List<GetOvertimeHistoryOutDetail> sortDetailList = detailList.stream().sorted(Comparator.comparing(GetOvertimeHistoryOutDetail::getOvertimeDate).reversed())
				.collect(Collectors.toList());
		
		for (GetOvertimeHistoryOutDetail his : sortDetailList) {
			ZonedDateTime zonedDatetime = ValueUtils.toZonedDateTime(his.getOvertimeDate());
			
			String a = ValueUtils.toDateStr(zonedDatetime, "yyyy-M-dd(E)");
			//String  a = sdfWithDay.format(zonedDatetime);
			his.setOvertimeDate(a);
		}
		
		
		GetOvertimeHistoryOut output = new GetOvertimeHistoryOut();
		output.setSuccess(true);
		output.setOvertimeList(sortDetailList);
		return output;
	}

}
