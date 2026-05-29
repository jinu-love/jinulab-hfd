package dev.jinulab.isetdx.hfd.model;

import java.time.ZonedDateTime;

import dev.jinulab.dbist.annotation.DbistPrimaryKey;
import lombok.Data;

@Data
public class OvertimeHis {
	@DbistPrimaryKey
	private String email;
	@DbistPrimaryKey
	private String overtimeDate;
	@DbistPrimaryKey
	private String timekey;
	private String txnCd;
	private double overtimeHours;
	private String comment;
	private ZonedDateTime createdDate;
	private ZonedDateTime updatedDate;
}
