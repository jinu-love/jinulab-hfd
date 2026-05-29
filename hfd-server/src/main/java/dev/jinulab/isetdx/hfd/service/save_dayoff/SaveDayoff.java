package dev.jinulab.isetdx.hfd.service.save_dayoff;

import java.time.ZonedDateTime;

import dev.jinulab.dbist.dml.Query;
import dev.jinulab.framework.exception.BizException;
import dev.jinulab.framework.util.DbUtils;
import dev.jinulab.framework.util.ValueUtils;
import dev.jinulab.isetdx.hfd.model.HfdDayoffInf;
import dev.jinulab.isetdx.hfd.model.HfdOvertimeInf;

public class SaveDayoff {

	public SaveDayoffOut saveDayoff(SaveDayoffIn input) throws Exception {

		if (ValueUtils.isEmpty(input.getDayoffDate())) {
			throw new BizException("ERR-003", "해피휴무 날짜를 입력해주세요.");
		}

		ZonedDateTime date = ValueUtils.getZonedDateTime();

		//주말에도 등록할 수 있도록 변경..
//		DayOfWeek dayOfWeek = ValueUtils.toZonedDateTime(input.getDayOffDate()).getDayOfWeek();
//		int dayOfWeekNumber = dayOfWeek.getValue();
//		if (dayOfWeekNumber >= 6) {
//			throw new BizException("ERR-002", "주말에는 해피휴무를 등록할 수 없습니다.");
//		}

		HfdOvertimeInf hfdOvertimeInf = DbUtils.select(HfdOvertimeInf.class,
				DbUtils.toId(input.getAccountId(), input.getDayoffDate()));

		if (!ValueUtils.isEmpty(hfdOvertimeInf)) {
			throw new BizException("ERR-004", "입력한 날짜에 초과근무가 등록되어 있습니다.");
		}

		String thisYearMonth = ValueUtils.toDateStr(ValueUtils.getZonedDateTime(), "YYYYMM");

		Query query = new Query();
		query.addFilter("accountId", input.getAccountId());
		query.addFilter("dayoffDate", "like", thisYearMonth + "%");
		DbUtils.deleteList(HfdDayoffInf.class, query);

		HfdDayoffInf hfdDayoffInf = new HfdDayoffInf();
		hfdDayoffInf.setAccountId(input.getAccountId());
		hfdDayoffInf.setDayoffDate(input.getDayoffDate());
		hfdDayoffInf.setComment(input.getComment());
		hfdDayoffInf.setCreatedAt(date);
		hfdDayoffInf.setUpdatedAt(date);
		hfdDayoffInf.setDayoffType(input.getDayoffType());
		DbUtils.insert(hfdDayoffInf);

		SaveDayoffOut output = new SaveDayoffOut();
		output.setSuccess(true);
		return output;

	}

}
