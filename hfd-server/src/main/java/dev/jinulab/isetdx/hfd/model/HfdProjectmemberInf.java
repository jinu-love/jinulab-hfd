package dev.jinulab.isetdx.hfd.model;

import java.time.ZonedDateTime;

import dev.jinulab.dbist.annotation.DbistPrimaryKey;
import lombok.Data;

@Data
public class HfdProjectmemberInf {
	@DbistPrimaryKey
	private String accountId;
	@DbistPrimaryKey
	private String project_name;
	private String system_name;
	private String manager_name;
	private String manager_dept;
	private String manager_rank;
	private String manager_position;
	private String br_name;
	private String br_dept;
	private String br_rank;
	private String br_position;
	private ZonedDateTime createdDate;
	private ZonedDateTime updatedDate;
}
