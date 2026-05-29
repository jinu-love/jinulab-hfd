package dev.jinulab.isetdx.hfd.service.save_overtime;

import java.time.ZonedDateTime;

import dev.jinulab.framework.util.DbUtils;
import dev.jinulab.framework.util.ValueUtils;
import dev.jinulab.isetdx.hfd.model.HfdOvertimeInf;

public class SaveOvertime {

	public SaveOvertimeOut saveOvertime(SaveOvertimeIn input) throws Exception {

		ZonedDateTime date = ValueUtils.getZonedDateTime();
	
//		DayOfWeek dayOfWeek = ValueUtils.toZonedDateTime(input.getOvertimeDate()).getDayOfWeek();
//	
//		int dayOfWeekNumber = dayOfWeek.getValue();
//
//		if (dayOfWeekNumber >= 6) {
//			throw new BizException("ERR-001", "주말에는 초과근무 시간을 등록할 수 없습니다.");
//		}
		
		HfdOvertimeInf hfdOvertimeInf = DbUtils.select(HfdOvertimeInf.class,
				DbUtils.toId(input.getAccountId(), input.getOvertimeDate()));

		if (ValueUtils.isEmpty(hfdOvertimeInf)) {
			hfdOvertimeInf = new HfdOvertimeInf();
			hfdOvertimeInf.setAccountId(input.getAccountId());
			hfdOvertimeInf.setOvertimeDate(input.getOvertimeDate());
			hfdOvertimeInf.setCreatedAt(date);
			//2026-04-24 L.JW: 아이템 추가
			hfdOvertimeInf.setName(input.getName());
			hfdOvertimeInf.setCompany(input.getCompany());
			hfdOvertimeInf.setDept(input.getDept());
			hfdOvertimeInf.setTeam(input.getTeam());
			hfdOvertimeInf.setPart(input.getPart());
			hfdOvertimeInf.setRank(input.getRank());
			hfdOvertimeInf.setPosition(input.getPosition());
		}
		
		hfdOvertimeInf.setOvertimeHours(input.getOvertimeHours());
		hfdOvertimeInf.setComment(input.getComment());
		hfdOvertimeInf.setUpdatedAt(date);
		DbUtils.upsert(hfdOvertimeInf);

		if (input.getOvertimeHours() == 0.0) {
			DbUtils.delete(hfdOvertimeInf);
		}

		SaveOvertimeOut output = new SaveOvertimeOut();
		output.setSuccess(true);
		return output;
	}

}
