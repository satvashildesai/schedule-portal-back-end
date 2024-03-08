package com.storeshop.scheduleportal.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
	private ScheduleRepository repo;

	@Autowired
	private StaffRepository staffRepo;

	@Autowired
	private ShiftRepository shiftRepo;

//	
	public ScheduleService(ScheduleRepository scheduleRepo, StaffRepository staffRepo, ShiftRepository shiftRepo) {
		this.repo = scheduleRepo;
		this.staffRepo = staffRepo;
		this.shiftRepo = shiftRepo;
	}

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
				repo.save(new ShiftAssignmentModel(shift, staff));
				repo.staffScheduledUpdate(shift.getDate(), staff.getId());
			}
			repo.shiftScheduledUpdate(true, Timestamp.from(Instant.now()), shift.getId());
		}
	}

//	Get assigned shift details from given date range
	public List<ShiftAssignmentModel> getAssignedShiftByDateRange(LocalDate startDate, LocalDate endDate)
			throws ResourceNotFoundException {
		Set<Long> assignedShiftIds = repo.findAssignedShiftByDateRange(startDate, endDate);
		if (assignedShiftIds.isEmpty()) {
			throw new ResourceNotFoundException(
					"No shift assigned in the given date range(" + startDate + " to " + endDate + ").");
		}
		System.err.println(assignedShiftIds);
		return repo.findAllById(assignedShiftIds);
	}

//	Get assigned shift details by id
	public List<ShiftAssignmentModel> getAssignedDetails(long shiftId) throws ResourceNotFoundException {

		ShiftScheduleModel shift = new ShiftScheduleModel();
		shift.setId(shiftId);
		List<Long> assignedIds = repo.FindAllByShiftId(shift);
		if (assignedIds.isEmpty()) {
			throw new ResourceNotFoundException("No staff assignment done for the shift whit shift id: " + shiftId
					+ ", please enter valid shift id");
		}
		return repo.findAllById(assignedIds);
	}

//	Get all assigned shift details
	public List<ShiftAssignmentModel> getAllAssignedDetails() throws ResourceNotFoundException {
		List<ShiftAssignmentModel> shiftAssignList = repo.findAll();
		if (shiftAssignList.isEmpty()) {
			throw new ResourceNotFoundException("Shift not scheduled yet.");
		}
		return shiftAssignList;
	}

//	Delete assigned shift
	public void deleteAssignedSchedule(long shiftId, LocalDate shiftDate) throws ResourceNotFoundException {
		ShiftScheduleModel shift = new ShiftScheduleModel();
		shift.setId(shiftId);

		List<Long> assignedShiftIds = repo.FindAllByShiftId(shift);
		if (assignedShiftIds.isEmpty()) {
			throw new ResourceNotFoundException("Staff-shift-assignment for shift with shift id: " + shiftId
					+ " is not exists, please enter valid shift id");
		}

//		Get staff id using scheduled shift
		List<Long> staffIds = repo.getStaffIdsByShiftId(shiftId);

		repo.deleteAllById(assignedShiftIds);

//		Update staff availability status
		repo.updateStaffAvailability(staffIds, shiftDate);
//		Update shift availability status
		repo.shiftScheduledUpdate(false, null, shift.getId());
	}

//	Validate the staff-shift-assignment request
	public boolean validateAssignmentRequest(ScheduleDto assignedShift, ShiftScheduleModel shift,
			List<StaffMemberModel> staffList) throws ResourceAlreadyAssignedException, ResourceNotFoundException,
			InvalidStaffCountException, RequestNotValidException {

//		Check that the shift is already scheduled or not
		List<Long> assignedIdList = repo.existsByShiftId(shift.getId());
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
			Long id = repo.findAvailableStaffByDate(staff.getId(), shift.getDate());
			if (id == null) {
				throw new RequestNotValidException(
						"Staff with id: " + staff.getId() + " is not available on date: " + shift.getDate());
			}

//			Check the staff is present on shift's start time and end time
			id = repo.findAvailableStaffByTime(staff.getId(), shift.getStartTime(), shift.getEndTime(), id);
			if (id == null) {
				throw new RequestNotValidException(
						"Staff with id: " + staff.getId() + " is not available on given shift time range");
			}
		}

		return true;

	}

//	Check that the staff is assigned to any other shift on same day
	private boolean isStaffAlreadyAssigned(ShiftScheduleModel shift, StaffMemberModel staff) {

		List<Date> staffAssigendDateList = repo.findAllAssignedDateByStaffId(staff.getId());

		Date shiftDate = Date.valueOf(shift.getDate());
		if (staffAssigendDateList.contains(shiftDate)) {
			return true;
		}
		return false;
	}

}