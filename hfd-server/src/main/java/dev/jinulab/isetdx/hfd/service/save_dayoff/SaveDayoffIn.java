package dev.jinulab.isetdx.hfd.service.save_dayoff;

import lombok.Data;

@Data
public class SaveDayoffIn {
	private String accountId;
	private String dayoffDate;
	private String dayoffType;
	private String comment;
}
