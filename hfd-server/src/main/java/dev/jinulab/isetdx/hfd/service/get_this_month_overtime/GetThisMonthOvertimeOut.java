package dev.jinulab.isetdx.hfd.service.get_this_month_overtime;

import lombok.Data;

@Data
public class GetThisMonthOvertimeOut {
	private boolean success = true;
	private String overtime;
	private String thisMonth;
	private String dayOffDate;
}
