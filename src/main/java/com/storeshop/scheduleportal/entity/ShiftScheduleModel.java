package com.storeshop.scheduleportal.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class ShiftScheduleModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private LocalDate date;

	@Column(nullable = false)
	private Long startTime;

	@Column(nullable = false)
	private Long endTime;

	@Column(nullable = false)
	private Long requiredStaffCount;

	@Column(nullable = false)
	private boolean isScheduled = false;

	@Column(nullable = false)
	private Timestamp createdAt;

	@Column(nullable = false)
	private Timestamp updatedAt;

	@Column(nullable = true)
	private Timestamp scheduledAt;

	public ShiftScheduleModel() {
		super();
	}

	public ShiftScheduleModel(Long id, LocalDate date, Long startTime, Long endTime, Long requiredStaffCount) {
		super();
		this.id = id;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.requiredStaffCount = requiredStaffCount;
	}

	public ShiftScheduleModel(Long id, LocalDate date, Long startTime, Long endTime, Long requiredStaffCount,
			boolean isScheduled, Timestamp createdAt, Timestamp updatedAt, Timestamp scheduledAt) {
		super();
		this.id = id;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.requiredStaffCount = requiredStaffCount;
		this.isScheduled = isScheduled;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.scheduledAt = scheduledAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getRequiredStaffCount() {
		return requiredStaffCount;
	}

	public void setRequiredStaffCount(Long requiredStaffCount) {
		this.requiredStaffCount = requiredStaffCount;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Timestamp getScheduledAt() {
		return scheduledAt;
	}

	public void setScheduledAt(Timestamp scheduledAt) {
		this.scheduledAt = scheduledAt;
	}

	@Override
	public String toString() {
		return "ShiftScheduleModel [id=" + id + ", date=" + date + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", requiredStaffCount=" + requiredStaffCount + ", isScheduled=" + isScheduled + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + ", scheduledAt=" + scheduledAt + "]";
	}

}
