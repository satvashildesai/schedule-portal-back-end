package com.storeshop.scheduleportal.util;

import java.util.List;

public class ScheduleShiftUtil {
	private Long shiftSheduleId;
	private List<Long> staffMemberIds;

	public ScheduleShiftUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScheduleShiftUtil(Long shiftSheduleId, List<Long> staffMemberIds) {
		super();
		this.shiftSheduleId = shiftSheduleId;
		this.staffMemberIds = staffMemberIds;
	}

	public Long getShiftSheduleId() {
		return shiftSheduleId;
	}

	public void setShiftSheduleId(Long shiftSheduleId) {
		this.shiftSheduleId = shiftSheduleId;
	}

	public List<Long> getStaffMemberIds() {
		return staffMemberIds;
	}

	public void setStaffMemberIds(List<Long> staffMemberIds) {
		this.staffMemberIds = staffMemberIds;
	}

	@Override
	public String toString() {
		return "AssignedStaffShiftUtil [shiftSheduleId=" + shiftSheduleId + ", staffMemberIds=" + staffMemberIds + "]";
	}

}
