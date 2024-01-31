package com.storeshop.scheduleportal.dto;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;

public class DateRangeDto {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Date can not be null, date formate should be(yyyy-mm-dd)")
	private LocalDate dateRange[];

	public DateRangeDto() {
		super();
	}

	public DateRangeDto(
			@NotNull(message = "Date can not be null, date formate should be(yyyy-mm-dd)") LocalDate[] dateRange) {
		super();
		this.dateRange = dateRange;
	}

	public LocalDate[] getDateRange() {
		return dateRange;
	}

	public void setDateRange(LocalDate[] dateRange) {
		this.dateRange = dateRange;
	}

	@Override
	public String toString() {
		return "DateRangeDto [dateRange=" + Arrays.toString(dateRange) + "]";
	}

}
