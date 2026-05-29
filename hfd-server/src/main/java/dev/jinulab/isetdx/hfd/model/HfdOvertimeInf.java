package dev.jinulab.isetdx.hfd.model;

import java.time.ZonedDateTime;

import dev.jinulab.dbist.annotation.DbistPrimaryKey;
import lombok.Data;

@Data
public class HfdOvertimeInf {
	@DbistPrimaryKey
	private String accountId;
	@DbistPrimaryKey
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
	private ZonedDateTime createdAt;
	private String createdBy;
	private ZonedDateTime updatedAt;
	private String updatedBy;
}
