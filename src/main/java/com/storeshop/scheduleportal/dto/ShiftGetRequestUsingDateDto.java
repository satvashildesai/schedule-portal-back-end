package com.storeshop.scheduleportal.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class ShiftGetRequestUsingDateDto {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date;

	public ShiftGetRequestUsingDateDto() {
		super();
	}

	public ShiftGetRequestUsingDateDto(LocalDate date) {
		super();
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "ShiftDate [date=" + date + "]";
	}

}
