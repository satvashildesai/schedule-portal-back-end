package com.storeshop.scheduleportal.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class StaffMemberModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<AvailableDateTimeModel> availableDateTime;

	public StaffMemberModel() {
		super();
	}

	public StaffMemberModel(Long id, String name, List<AvailableDateTimeModel> availableDateTime) {
		super();
		this.id = id;
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

	public List<AvailableDateTimeModel> getAvailableDateTime() {
		return availableDateTime;
	}

	public void setAvailableDateTime(List<AvailableDateTimeModel> availableDateTime) {
		this.availableDateTime = availableDateTime;
	}

	@Override
	public String toString() {
		return "StaffMemberModel [id=" + id + ", name=" + name + ", availableDateTime=" + availableDateTime + "]";
	}
}

//@Column(nullable = false)
//@ElementCollection
//@CollectionTable(name = "available_dates", joinColumns = @JoinColumn(name = "staffId", referencedColumnName = "id"))
//private List<LocalDate> dates;
//
//@Column(nullable = false)
//@ElementCollection
//@CollectionTable(name = "available_dates", joinColumns = @JoinColumn(name = "staffId", referencedColumnName = "id"))
//private List<Long> startTimes;
//
//@Column(nullable = false)
//@ElementCollection
//@CollectionTable(name = "available_dates", joinColumns = @JoinColumn(name = "staffId", referencedColumnName = "id"))
//private List<Long> endTimes;