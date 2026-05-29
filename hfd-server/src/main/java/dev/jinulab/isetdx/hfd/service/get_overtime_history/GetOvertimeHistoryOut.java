package dev.jinulab.isetdx.hfd.service.get_overtime_history;

import java.util.List;

import lombok.Data;

@Data
public class GetOvertimeHistoryOut {
	private boolean success;
	private List<GetOvertimeHistoryOutDetail> overtimeList;
}
