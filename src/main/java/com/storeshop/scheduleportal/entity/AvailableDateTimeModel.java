package com.storeshop.scheduleportal.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class AvailableDateTimeModel {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private Long startTime;

	@Column(nullable = false)
	private Long endTime;

	@Column(nullable = false)
	private boolean isScheduled = false;

	@ManyToOne
	@JsonBackReference
	private StaffMemberModel staff;

	public AvailableDateTimeModel() {
		super();
	}

	public AvailableDateTimeModel(Long id, LocalDate date, Long startTime, Long endTime, StaffMemberModel staff) {
		super();
		this.id = id;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.staff = staff;
	}

	public AvailableDateTimeModel(LocalDate date, Long startTime, Long endTime, StaffMemberModel staff) {
		super();
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.staff = staff;
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

	public StaffMemberModel getStaff() {
		return staff;
	}

	public void setStaff(StaffMemberModel staff) {
		this.staff = staff;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	@Override
	public String toString() {
		return "AvailableDateTimeModel [id=" + id + ", date=" + date + ", startTime=" + startTime + ", endTime="
				+ endTime + ", isScheduled=" + isScheduled + ", staff=" + staff + "]";
	}

}
