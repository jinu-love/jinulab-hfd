package dev.jinulab.isetdx.hfd.service.get_select_overtime_status;

import java.util.ArrayList;
import java.util.List;

import dev.jinulab.dbist.dml.Query;
import dev.jinulab.framework.exception.BizException;
import dev.jinulab.framework.util.DbUtils;
import dev.jinulab.framework.util.ValueUtils;
import dev.jinulab.isetdx.hfd.model.HfdDayoffInf;
import dev.jinulab.isetdx.hfd.model.HfdOvertimeInf;

public class GetSelectOvertimeStatus {

	public GetSelectOvertimeStatusOut getSelectOvertimeStatus(GetSelectOvertimeStatusIn input) throws Exception {
		
		if(ValueUtils.isEmpty(input.getName())) {
			throw new BizException("ERR-005", "이름 입력해주세요.");
		}

		List<OvertimeStatus> overtimeStatusList = new ArrayList<OvertimeStatus>();
		
		Query query = new Query();
		query.addFilter("name", input.getName());
		if (ValueUtils.equals("초과근무", input.getRadioValue())) {
			query.addOrder("overtimeDate", false);
			List<HfdOvertimeInf> hfdOvertimeInfList = DbUtils.selectList(HfdOvertimeInf.class, query);
			for(HfdOvertimeInf hfdOvertimeInf: hfdOvertimeInfList) {
				OvertimeStatus overtimeStatus = new OvertimeStatus();
				overtimeStatus.setName(hfdOvertimeInf.getName());
				overtimeStatus.setDept(hfdOvertimeInf.getDept());
				overtimeStatus.setOvertimeDate(hfdOvertimeInf.getDept());
				overtimeStatus.setChangedDate(hfdOvertimeInf.getUpdatedAt());
				overtimeStatus.setComment(hfdOvertimeInf.getComment());
				overtimeStatusList.add(overtimeStatus);
			}
		} else {
			query.addOrder("dayoffDate", false);
			List<HfdDayoffInf> hfdDayoffInfList = DbUtils.selectList(HfdDayoffInf.class, query);
			for(HfdDayoffInf hfdDayoffInf: hfdDayoffInfList) {
				OvertimeStatus overtimeStatus = new OvertimeStatus();
				overtimeStatus.setName(hfdDayoffInf.getName());
				overtimeStatus.setDept(hfdDayoffInf.getDept());
				overtimeStatus.setOvertimeDate(hfdDayoffInf.getDept());
				overtimeStatus.setChangedDate(hfdDayoffInf.getUpdatedAt());
				overtimeStatus.setComment(hfdDayoffInf.getComment());
				overtimeStatusList.add(overtimeStatus);
			}
		}
		

		
//		if (ValueUtils.equals("초과근무", input.getRadioValue())) {
//			overtimeStatusList = DbUtils.selectList(DbUtils.toSqlPath(this.getClass(), "select_overtime_status.sql"),
//					query, OvertimeStatus.class);
//		} else {
//			overtimeStatusList = DbUtils.selectList(DbUtils.toSqlPath(this.getClass(), "select_dayoff_status.sql"),
//					query, OvertimeStatus.class);
//		}
		

		GetSelectOvertimeStatusOut output = new GetSelectOvertimeStatusOut();
		output.setOvertimeStatusList(overtimeStatusList);
		output.setSuccess(true);
		return output;

	}

}
