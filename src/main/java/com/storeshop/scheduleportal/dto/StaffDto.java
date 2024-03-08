package com.storeshop.scheduleportal.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class StaffDto {

	private Long id;

	@NotBlank(message = "Name must not empty.")
	private String name;

	@NotEmpty(message = "Date should greater than current date and time is between 1 to 24.")
	@JsonManagedReference
	private List<AvailableDateTimeDto> availableDateTime;

	private Timestamp createdAt;

	private Timestamp updatedAt;

	public StaffDto() {
		super();
	}

	public StaffDto(Long id, @NotBlank(message = "Name must not empty.") String name,
			@NotEmpty(message = "Date should greater than current date and time is between 1 to 24.") List<AvailableDateTimeDto> availableDateTime,
			Timestamp createdAt, Timestamp updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.availableDateTime = availableDateTime;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
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

	@Override
	public String toString() {
		return "StaffDto [id=" + id + ", name=" + name + ", availableDateTime=" + availableDateTime + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + "]";
	}
}
