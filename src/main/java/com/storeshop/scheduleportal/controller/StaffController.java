package com.storeshop.scheduleportal.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storeshop.scheduleportal.dto.StaffDto;
import com.storeshop.scheduleportal.entity.StaffMemberModel;
import com.storeshop.scheduleportal.exceptions.FailedToSaveException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.service.StaffService;
import com.storeshop.scheduleportal.util.SuccessResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/portal")
@CrossOrigin(origins = "http://localhost:4200/")
public class StaffController {

	@Autowired
	private StaffService staffService;

//	Crate and save new staff member
	@PostMapping("/staff")
	public Object saveMember(@Valid @RequestBody StaffDto member)
			throws RequestNotValidException, FailedToSaveException, ResourceNotFoundException {

		staffService.saveMember(member);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new SuccessResponse("201", "CREATED", "Staff save successfully in database"));
	}

//	Update staff member details
	@PutMapping("/staff")
	public Object updateStaffDetails(@Valid @RequestBody StaffDto member)
			throws RequestNotValidException, ResourceNotFoundException, FailedToSaveException {

		staffService.updateStaffDetails(member);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff member updated successfully."));
	}

//	Get staff by given date range
	@GetMapping("/staff/date/{dateRange}")
	public Object getStaffByDateRange(@PathVariable String dateRange) {
		String[] splitDateString = dateRange.split(",");
		List<LocalDate> dateArr = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		dateArr.add(LocalDate.parse(splitDateString[0], formatter));
		dateArr.add(LocalDate.parse(splitDateString[1], formatter));

		List<StaffMemberModel> staffMembers = staffService.getStaffByDateRange(dateArr);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff found.", new ArrayList<Object>(staffMembers)));
	}

//	Get staff by staffId
	@GetMapping("/staff/{staffId}")
	public Object getStaff(@PathVariable String staffId) throws NumberFormatException, ResourceNotFoundException {
		StaffMemberModel optionalStaffMember = staffService.getStaff(Long.parseLong(staffId));

		List<Object> staff = new ArrayList<>();
		staff.add(optionalStaffMember);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff member found.", staff));
	}

//	Get all staffs
	@GetMapping("/staff")
	public Object getAllStaff() throws ResourceNotFoundException {

		List<StaffMemberModel> staffList = staffService.getAllStaff();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff found.", new ArrayList<Object>(staffList)));

	}

//	Get available staff for the given shift
	@GetMapping("/staff/available")
	public Object getAvailableStaff(@RequestParam(value = "date") String date,
			@RequestParam(value = "startTime") String startTime, @RequestParam(value = "endTime") String endTime)
			throws NumberFormatException, ResourceNotFoundException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		List<StaffMemberModel> availableStaffList = staffService.getAvailableStaff(LocalDate.parse(date, formatter),
				Long.parseLong(startTime), Long.parseLong(endTime));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff found.", new ArrayList<>(availableStaffList)));
	}

//	Delete staff by staffId
	@DeleteMapping("/staff/{staffId}")
	public Object deleteStaff(@PathVariable String staffId) throws NumberFormatException, ResourceNotFoundException {
		staffService.deleteStaff(Long.parseLong(staffId));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff delted, Staff id: " + staffId));
	}
}
