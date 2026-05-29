package dev.jinulab.isetdx.hfd.service.get_select_overtime_status;

import java.util.List;

import lombok.Data;

@Data
public class GetSelectOvertimeStatusOut {
	private boolean success = true;
	private List<OvertimeStatus> overtimeStatusList;
}
