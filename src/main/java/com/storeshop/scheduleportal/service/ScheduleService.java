package com.storeshop.scheduleportal.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storeshop.scheduleportal.dto.ScheduleDto;
import com.storeshop.scheduleportal.entity.ShiftAssignmentModel;
import com.storeshop.scheduleportal.entity.ShiftScheduleModel;
import com.storeshop.scheduleportal.entity.StaffMemberModel;
import com.storeshop.scheduleportal.exceptions.InvalidStaffCountException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceAlreadyAssignedException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.repository.ScheduleRepository;
import com.storeshop.scheduleportal.repository.ShiftRepository;
import com.storeshop.scheduleportal.repository.StaffRepository;

@Service
public class ScheduleService {
	@Autowired
	private ScheduleRepository shiftAssignmentRepo;

	@Autowired
	private StaffRepository staffRepo;

	@Autowired
	private ShiftRepository shiftRepo;

//	Assign staff to the list
	public void assignShiftToStaff(ScheduleDto assignedShift) throws ResourceNotFoundException,
			ResourceAlreadyAssignedException, InvalidStaffCountException, RequestNotValidException {

//		Get shift from database (throw ResourceNotExistsException if not present)
		ShiftScheduleModel shift = shiftRepo.findById(assignedShift.getShiftSheduleId())
				.orElseThrow(() -> new ResourceNotFoundException("Shift with shift id: "
						+ assignedShift.getShiftSheduleId() + " is not exist, please enter valid shift id"));

//		Return empty list if the staff is not present
		List<StaffMemberModel> staffList = staffRepo.findAllById(assignedShift.getStaffMemberIds());

//		Validate the Staff-ShiftAssignment request
		boolean validationStatus = validateAssignmentRequest(assignedShift, shift, staffList);

		if (validationStatus) {
			for (StaffMemberModel staff : staffList) {
				shiftAssignmentRepo.save(new ShiftAssignmentModel(shift, staff));
				shiftAssignmentRepo.staffScheduledUpdate(shift.getDate(), staff.getId());
			}
			shiftAssignmentRepo.shiftScheduledUpdate(true, shift.getId());
		}
	}

//	Get staff-shift-assigned details by id
	public List<ShiftAssignmentModel> getAssignedDetails(long shiftId) throws ResourceNotFoundException {

		ShiftScheduleModel shift = new ShiftScheduleModel();
		shift.setId(shiftId);
		List<Long> assignedIds = shiftAssignmentRepo.FindAllByShiftId(shift);
		if (assignedIds.isEmpty()) {
			throw new ResourceNotFoundException("No staff assignment done for the shift whit shift id: " + shiftId
					+ ", please enter valid shift id");
		}
		return shiftAssignmentRepo.findAllById(assignedIds);
	}

//	Get all staff-shift-assigned details
	public List<ShiftAssignmentModel> getAllAssignedDetails() throws ResourceNotFoundException {
		List<ShiftAssignmentModel> shiftAssignList = shiftAssignmentRepo.findAll();
		if (shiftAssignList.isEmpty()) {
			throw new ResourceNotFoundException("Shift not scheduled yet.");
		}
		return shiftAssignList;
	}

//	Delete staff-shift-assigned schedule
	public void deleteAssignedSchedule(long shiftId, LocalDate shiftDate) throws ResourceNotFoundException {
		ShiftScheduleModel shift = new ShiftScheduleModel();
		shift.setId(shiftId);

		List<Long> assignedShiftIds = shiftAssignmentRepo.FindAllByShiftId(shift);
		if (assignedShiftIds.isEmpty()) {
			throw new ResourceNotFoundException("Staff-shift-assignment for shift with shift id: " + shiftId
					+ " is not exists, please enter valid shift id");
		}

//		Get staff id using scheduled shift
		List<Long> staffIds = shiftAssignmentRepo.getStaffIdsByShiftId(shiftId);

		shiftAssignmentRepo.deleteAllById(assignedShiftIds);

//		Update staff availability status
		shiftAssignmentRepo.updateStaffAvailability(staffIds, shiftDate);
//		Update shift availability status
		shiftAssignmentRepo.shiftScheduledUpdate(false, shift.getId());
	}

//	Validate the staff-shift-assignment request
	public boolean validateAssignmentRequest(ScheduleDto assignedShift, ShiftScheduleModel shift,
			List<StaffMemberModel> staffList) throws ResourceAlreadyAssignedException, ResourceNotFoundException,
			InvalidStaffCountException, RequestNotValidException {

//		Check that the shift is already scheduled or not
		List<Long> assignedIdList = shiftAssignmentRepo.existsByShiftId(shift.getId());
		if (!assignedIdList.isEmpty()) {
			throw new ResourceAlreadyAssignedException("Shift with id: " + shift.getId() + " is already schedule");
		}

//		Check that all staff id are valid and not duplicate
		if (staffList.size() < assignedShift.getStaffMemberIds().size()) {
			throw new ResourceNotFoundException(
					"Some requested staffs are not available or you enter invalid/duplicate ids, please enter valid ids");
		}

//		Check that requested and required staff count is greater or not
		if (assignedShift.getStaffMemberIds().size() > shift.getRequiredStaffCount()) {
			throw new InvalidStaffCountException(
					"Enter more staff than required, required staff count: " + shift.getRequiredStaffCount());
		}

//		Check that requested and required staff count is less or not
		if (assignedShift.getStaffMemberIds().size() < shift.getRequiredStaffCount()) {
			throw new InvalidStaffCountException(
					"Enter less staff than required, required staff count: " + shift.getRequiredStaffCount());
		}

//		Check for each staff, is present on required date and time or not
		for (StaffMemberModel staff : staffList) {

//			Check that the requested staff is already assigned to the any shift or not
			if (isStaffAlreadyAssigned(shift, staff)) {
				throw new ResourceAlreadyAssignedException(
						"Staff with staff if: " + staff.getId() + " is already assigned to any other shift");
			}

//			Check the staff is present on shift date
			Long id = shiftAssignmentRepo.findAvailableStaffByDate(staff.getId(), shift.getDate());
			if (id == null) {
				throw new RequestNotValidException(
						"Staff with id: " + staff.getId() + " is not available on date: " + shift.getDate());
			}

//			Check the staff is present on shift's start time and end time
			id = shiftAssignmentRepo.findAvailableStaffByTime(staff.getId(), shift.getStartTime(), shift.getEndTime(),
					id);
			if (id == null) {
				throw new RequestNotValidException(
						"Staff with id: " + staff.getId() + " is not available on given shift time range");
			}
		}

		return true;

	}

//	Check that the staff is assigned to any other shift on same day
	private boolean isStaffAlreadyAssigned(ShiftScheduleModel shift, StaffMemberModel staff) {

		List<Date> staffAssigendDateList = shiftAssignmentRepo.findAllAssignedDateByStaffId(staff.getId());

		Date shiftDate = Date.valueOf(shift.getDate());
		if (staffAssigendDateList.contains(shiftDate)) {
			return true;
		}
		return false;
	}

}

//Get count of assigned staff for the give shift
//long staffAssignedCount = repo.countByShiftSheduleId(shift);

//Get staff list to check that the assigning staff is already present or not
//List<StaffMemberModel> assignedStaffList = repo.findStaffByShift(shift);

//Get AvailableDateTime entity of particular staff
//List<AvailableDateTime> dateTimes = staffRepo.findAllByStaff(staffList);

//List<AvailableDateTimeModel> dateTimes = staff.getAvailableDateTime();
//boolean isAvailableOnDate = false;
//boolean isAvailableOnTime = false;
//
////Check if the staff is present on date or time
//for (AvailableDateTimeModel dt : dateTimes) {
//
//	if (dt.getDate().isEqual(shift.getDate())) {
//		isAvailableOnDate = true;
//
//		if (dt.getStartTime() <= shift.getStartTime() && dt.getEndTime() >= shift.getEndTime()) {
//			isAvailableOnTime = true;
//			break;
//		}
//	}
//}
//
//if (!isAvailableOnDate) {
//	return new AssignmentResultUtil(3, staff.getId() + " : " + staff.getName());
//}
//
//if (!isAvailableOnTime) {
//	return new AssignmentResultUtil(4, staff.getId() + " : " + staff.getName());
//}
