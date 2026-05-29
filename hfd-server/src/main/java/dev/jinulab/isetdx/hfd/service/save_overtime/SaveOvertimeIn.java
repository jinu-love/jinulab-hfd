package dev.jinulab.isetdx.hfd.service.save_overtime;

import lombok.Data;

@Data
public class SaveOvertimeIn {
	private String accountId;
	private String overtimeDate;
	private double overtimeHours;
	private String comment;
	private String name;
	private String company;
	private String dept;
	private String team;
	private String part;
	private String rank;
	private String position;
}
