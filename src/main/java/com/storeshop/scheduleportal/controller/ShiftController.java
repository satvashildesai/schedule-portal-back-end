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

import com.storeshop.scheduleportal.dto.ShiftDto;
import com.storeshop.scheduleportal.dto.ShiftGetRequestUsingDateDto;
import com.storeshop.scheduleportal.exceptions.FailedToSaveException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.service.ShiftService;
import com.storeshop.scheduleportal.util.SuccessResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/portal")
@CrossOrigin(origins = "http://localhost:4200/")
public class ShiftController {

	@Autowired
	private ShiftService shiftService;

//  Create and save new shift
	@PostMapping("/shift")
	public Object saveShift(@Valid @RequestBody ShiftDto shift) throws RequestNotValidException, FailedToSaveException {

		ShiftDto savedShift = shiftService.saveShift(shift);
		return ResponseEntity.status(HttpStatus.CREATED).body(
				new SuccessResponse("201", "CREATED", "Shift Schedule details saved with id: " + savedShift.getId()));
	}

//	Edit existing shift
	@PutMapping("/shift")
	public Object updateShift(@Valid @RequestBody ShiftDto shift)
			throws ResourceNotFoundException, RequestNotValidException, FailedToSaveException {

		if (!shiftService.isShiftExists(shift)) {
			throw new ResourceNotFoundException("Requested shift with shift id: " + shift.getId()
					+ " for update is not exists, please enter valid shift");
		} else {
			ShiftDto updatedShift = shiftService.updateShift(shift);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new SuccessResponse("200", "OK", "Shift: " + updatedShift.getId() + " Updated Successfully"));
		}
	}

//	Get shifts of given date range
	@GetMapping("/shift/daterange")
	public Object getShiftByDateRange(@RequestParam(value = "sDate") String sDate,
			@RequestParam(value = "eDate") String eDate) throws ResourceNotFoundException {
		List<LocalDate> dateArr = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		dateArr.add(LocalDate.parse(sDate, formatter));
		dateArr.add(LocalDate.parse(eDate, formatter));

		List<Object> shiftList = new ArrayList<>(shiftService.getShiftByDateRange(dateArr));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "OK", "Shift Details found for given date range", shiftList));
	}

//	Get shift details by using date
	@GetMapping("/shift-date")
	public Object getShift(@RequestBody ShiftGetRequestUsingDateDto date) throws ResourceNotFoundException {

		List<Object> shiftList = new ArrayList<>(shiftService.getShift(date));
		return ResponseEntity.status(HttpStatus.OK).body(
				new SuccessResponse("200", "OK", "Shift details found for the date: " + date.getDate(), shiftList));
	}

//	Get all shift details 
	@GetMapping("/shift")
	public Object getAllShifts() throws ResourceNotFoundException {
		List<Object> shiftList = new ArrayList<>(shiftService.getAllShifts());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "OK", "Shift details found.", shiftList));
	}

//	Delete shift by shiftId
	@DeleteMapping("/shift/{shiftId}")
	public Object deleteShift(@PathVariable String shiftId) throws NumberFormatException, ResourceNotFoundException {
		shiftService.deleteShift(Long.parseLong(shiftId));
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "OK", "Shift delete, Shift id: " + shiftId));
	}
}
