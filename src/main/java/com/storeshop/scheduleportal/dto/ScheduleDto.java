package com.storeshop.scheduleportal.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class ScheduleDto {
	@NotEmpty(message = "Shift Id is appropriate and not null")
	private Long shiftSheduleId;

	@NotEmpty(message = "Staff Id list contains exactly requaired no. of staff and it should not null")
	private List<Long> staffMemberIds;

	public ScheduleDto() {
		super();
	}

	public ScheduleDto(@NotEmpty(message = "Shift Id is appropriate and not null") Long shiftSheduleId,
			@NotEmpty(message = "Staff Id list contains exactly requaired no. of staff and it should not null") List<Long> staffMemberIds) {
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
		return "ShiftAssignmentRequestDto [shiftSheduleId=" + shiftSheduleId + ", staffMemberIds=" + staffMemberIds
				+ "]";
	}

}
