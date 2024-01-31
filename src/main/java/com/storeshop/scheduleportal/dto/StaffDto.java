package com.storeshop.scheduleportal.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class StaffDto {

	private Long id;

	@NotBlank(message = "Name must not empty.")
	private String name;

	@NotEmpty(message = "Date should greater than current date and time is between 1 to 24.")
	private List<AvailableDateTimeDto> availableDateTime;

	public StaffDto() {
		super();
	}

	public StaffDto(@NotBlank(message = "Name must not empty.") String name,
			@NotNull(message = "Date should greater than current date and time is between 1 to 24.") List<AvailableDateTimeDto> availableDateTime) {
		super();
		this.name = name;
		this.availableDateTime = availableDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<AvailableDateTimeDto> getAvailableDateTime() {
		return availableDateTime;
	}

	public void setAvailableDateTime(List<AvailableDateTimeDto> availableDateTime) {
		this.availableDateTime = availableDateTime;
	}

	@Override
	public String toString() {
		return "StaffMemberDto [name=" + name + ", availableDateTime=" + availableDateTime + "]";
	}
}
