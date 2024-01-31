package com.storeshop.scheduleportal.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ShiftDto {

	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Date can not be null, date formate should be(yyyy-mm-dd)")
	private LocalDate date;

	@NotNull(message = "Start time not be null and in between 1 to 24")
	@Max(24)
	@Min(1)
	private Long startTime;

	@NotNull(message = "End time not be null and in between 1 to 24")
	@Max(24)
	@Min(1)
	private Long endTime;

	@NotNull(message = "Required staff count must be greater than 0")
	@Min(1)
	private Long requiredStaffCount;

	public ShiftDto() {
		super();
	}

	public ShiftDto(Long id,
			@NotNull(message = "Date can not be null, date formate should be(yyyy-mm-dd)") LocalDate date,
			@NotNull(message = "Start time not be null and in between 1 to 24") @Max(24) @Min(1) Long startTime,
			@NotNull(message = "End time not be null and in between 1 to 24") @Max(24) @Min(1) Long endTime,
			@NotNull(message = "Required staff count must be greater than 0") @Min(1) Long requiredStaffCount) {
		super();
		this.id = id;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.requiredStaffCount = requiredStaffCount;
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

	@Override
	public String toString() {
		return "ShiftScheduleDto [id=" + id + ", date=" + date + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", requiredStaffCount=" + requiredStaffCount + "]";
	}

}
