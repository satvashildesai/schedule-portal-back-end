package com.storeshop.scheduleportal.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AvailableDateTimeDto {

	private Long id;

	@NotNull(message = "Date should not blank and greater than current date ( Date format must be like: yyyy-mm-dd).")
	private LocalDate date;

	@NotNull(message = "Start time must be in between 1 to 24.")
	@Min(1)
	@Max(24)
	private Long startTime;

	@NotNull(message = "Start time must be in between 1 to 24.")
	@Min(1)
	@Max(24)
	private Long endTime;

	private StaffDto staff;

	public AvailableDateTimeDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AvailableDateTimeDto(Long id,
			@NotNull(message = "Date should not blank and greater than current date ( Date format must be like: yyyy-mm-dd).") LocalDate date,
			@NotNull(message = "Start time must be in between 1 to 24.") @Min(1) @Max(24) Long startTime,
			@NotNull(message = "Start time must be in between 1 to 24.") @Min(1) @Max(24) Long endTime,
			StaffDto staff) {
		super();
		this.id = id;
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

	public StaffDto getStaff() {
		return staff;
	}

	public void setStaff(StaffDto staff) {
		this.staff = staff;
	}

	@Override
	public String toString() {
		return "AvailableDateTimeDto [id=" + id + ", date=" + date + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", staff=" + staff + "]";
	}

}
