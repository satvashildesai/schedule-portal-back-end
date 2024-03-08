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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.storeshop.scheduleportal.dto.ScheduleDto;
import com.storeshop.scheduleportal.entity.ShiftAssignmentModel;
import com.storeshop.scheduleportal.exceptions.InvalidStaffCountException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceAlreadyAssignedException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.service.ScheduleService;
import com.storeshop.scheduleportal.util.SuccessResponse;

@RestController
@RequestMapping("/portal")
@CrossOrigin(origins = "http://localhost:4200/")
public class ScheduleController {

	@Autowired
	private ScheduleService shiftAssignService;

//	Schedule new staff-shift 
	@PutMapping("/assign")
	public Object assignShiftToStaff(@RequestBody ScheduleDto assignedShift) throws ResourceNotFoundException,
			ResourceAlreadyAssignedException, InvalidStaffCountException, RequestNotValidException {

		shiftAssignService.assignShiftToStaff(assignedShift);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff assigned to shift successfully"));
	}

//	Get assigned shift from given range of date
	@GetMapping("assign/date")
	public Object getAssignedShiftByDateRange(@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate) throws ResourceNotFoundException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<ShiftAssignmentModel> assignedShiftList = shiftAssignService.getAssignedShiftByDateRange(
				LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));

		return ResponseEntity.status(HttpStatus.OK).body(
				new SuccessResponse("200", "SUCCESS", "Assigned shift found.", new ArrayList<>(assignedShiftList)));
	}

//	Get assigned details by id
	@GetMapping("/assign/{shiftId}")
	public Object getAssignedDetails(@PathVariable String shiftId)
			throws NumberFormatException, ResourceNotFoundException {

		List<Object> assignedModel = new ArrayList<>(shiftAssignService.getAssignedDetails(Long.parseLong(shiftId)));
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("200", "SUCCESS",
				"Staff-shif-assignment details found for shift id: " + shiftId, assignedModel));
	}

//	Get all staff-shift assigned details
	@GetMapping("/assign")
	public Object getAllAssignedDetails() throws ResourceNotFoundException {

		List<Object> staffShiftAssignedList = new ArrayList<>(shiftAssignService.getAllAssignedDetails());
		return ResponseEntity.status(HttpStatus.OK).body(
				new SuccessResponse("200", "SUCCESS", "Staff-shift-assigned details found", staffShiftAssignedList));
	}

//	Delete staff-shift schedule
	@DeleteMapping("/assign")
	public Object deleteAssignedSchedule(@RequestParam(value = "id") String shiftId,
			@RequestParam(value = "date") String shiftDate) throws NumberFormatException, ResourceNotFoundException {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate sDate = LocalDate.parse(shiftDate, formatter);

		shiftAssignService.deleteAssignedSchedule(Long.parseLong(shiftId), sDate);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse("200", "SUCCESS", "Staff-shift-assignment deleted"));
	}
}

//switch (res.getStatusCode()) {
//case 0:
//	return new ResponseUtil<ShiftAssignmentModel>(new ResponseEntity<String>(
//			"Requested staff is not present or entered duplicate ids, please enter valid staff id",
//			HttpStatus.BAD_REQUEST), null);
//
//case 1:
//	return new ResponseUtil<ShiftAssignmentModel>(new ResponseEntity<String>(
//			"You can try to add more staff members than required. Required staff members: " + res.getMsg(),
//			HttpStatus.BAD_REQUEST), null);
//
//case 2:
//	return new ResponseUtil<ShiftAssignmentModel>(new ResponseEntity<String>(
//			"Nedd more staff members for this shift. Required staff members: " + res.getMsg(),
//			HttpStatus.BAD_REQUEST), null);
//
//case 3:
//	return new ResponseUtil<ShiftAssignmentModel>(new ResponseEntity<String>(
//			"Staff member '" + res.getMsg() + "' is not available on the given shift date",
//			HttpStatus.BAD_REQUEST), null);
//
//case 4:
//	return new ResponseUtil<ShiftAssignmentModel>(new ResponseEntity<String>(
//			"Staff member '" + res.getMsg() + "' is not available for the shift time range",
//			HttpStatus.BAD_REQUEST), null);
//
//case 5:
//	return new ResponseUtil<ShiftAssignmentModel>(
//			new ResponseEntity<String>("Staff assigned to shift successfully", HttpStatus.OK), null);
//
//case 6:
//	return new ResponseUtil<ShiftAssignmentModel>(new ResponseEntity<String>(
//			"Staff '" + res.getMsg() + "' is already assigned to the shift", HttpStatus.BAD_REQUEST), null);
//
//case 7:
//	return new ResponseUtil<>(new ResponseEntity<String>(
//			"Shift is already scheduled, validate schedule id once again", HttpStatus.BAD_REQUEST), null);
//
//default:
//	return new ResponseUtil<ShiftAssignmentModel>();
//}