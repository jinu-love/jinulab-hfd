package dev.jinulab.isetdx.hfd.service.get_select_overtime_status;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class OvertimeStatus {
	private String name;
	private String dept;
	private String overtimeDate;
	private String overtimeHours;
	private ZonedDateTime changedDate;
	private String dayOffDate;
	private String comment;
}
