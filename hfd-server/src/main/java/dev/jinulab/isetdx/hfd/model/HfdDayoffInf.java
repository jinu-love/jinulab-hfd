package dev.jinulab.isetdx.hfd.model;

import java.time.ZonedDateTime;

import dev.jinulab.dbist.annotation.DbistPrimaryKey;
import lombok.Data;

@Data
public class HfdDayoffInf {
	@DbistPrimaryKey
	private String accountId;
	@DbistPrimaryKey
	private String dayoffDate;
	private String comment;
	private String dayoffType;
	private String name;
	private String company;
	private String dept;
	private String team;
	private String part;
	private String rank;
	private String position;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
}
