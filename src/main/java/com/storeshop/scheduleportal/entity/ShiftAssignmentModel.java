package com.storeshop.scheduleportal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ShiftAssignmentModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private ShiftScheduleModel shiftSheduleId;

	@ManyToOne
	private StaffMemberModel staffMemberId;

	public ShiftAssignmentModel(ShiftScheduleModel shiftSheduleId, StaffMemberModel staffMemberId) {
		super();
		this.shiftSheduleId = shiftSheduleId;
		this.staffMemberId = staffMemberId;
	}

	public ShiftAssignmentModel() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ShiftScheduleModel getShiftSheduleId() {
		return shiftSheduleId;
	}

	public void setShiftSheduleId(ShiftScheduleModel shiftSheduleId) {
		this.shiftSheduleId = shiftSheduleId;
	}

	public StaffMemberModel getStaffMemberId() {
		return staffMemberId;
	}

	public void setStaffMemberId(StaffMemberModel staffMemberId) {
		this.staffMemberId = staffMemberId;
	}

	@Override
	public String toString() {
		return "ShiftAssignmentModel [id=" + id + ", shiftSheduleId=" + shiftSheduleId + ", staffMemberId="
				+ staffMemberId + "]";
	}
}
