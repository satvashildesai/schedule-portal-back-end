package com.storeshop.scheduleportal.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storeshop.scheduleportal.dto.ShiftGetRequestUsingDateDto;
import com.storeshop.scheduleportal.dto.ShiftDto;
import com.storeshop.scheduleportal.entity.ShiftScheduleModel;
import com.storeshop.scheduleportal.exceptions.FailedToSaveException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.repository.ScheduleRepository;
import com.storeshop.scheduleportal.repository.ShiftRepository;
import com.storeshop.scheduleportal.util.ShiftMapper;

@Service
public class ShiftService {

	@Autowired
	private ShiftRepository repo;

	@Autowired
	private ScheduleRepository scheduleRepo;

//	This constructor is used to create mock repository for the unit testing
	public ShiftService(ShiftRepository shiftRepo, ScheduleRepository scheduleRepo) {
		this.repo = shiftRepo;
		this.scheduleRepo = scheduleRepo;
	}

	// Create and save the new shift
	public ShiftDto saveShift(ShiftDto shift) throws RequestNotValidException, FailedToSaveException {

		if (isRequestValid(shift)) {
			ShiftScheduleModel shiftEntity = ShiftMapper.shiftMap(shift, new ShiftScheduleModel());
			Timestamp dateTime = Timestamp.from(Instant.now());
			shiftEntity.setCreatedAt(dateTime);
			shiftEntity.setUpdatedAt(dateTime);

			ShiftScheduleModel savedShift = repo.save(shiftEntity);
			if (savedShift == null) {
				throw new FailedToSaveException("Failed to save shift on portal, please try once again");
			}
			return ShiftMapper.toDto(savedShift, shift);
		} else {
			throw new RequestNotValidException(
					"Request not valid, to save shift enter valid date, time and required count");
		}
	}

//	Update shift 
	public ShiftDto updateShift(ShiftDto shift) throws RequestNotValidException, FailedToSaveException {

		if (isRequestValid(shift)) {
			ShiftScheduleModel shiftEntity = ShiftMapper.updatedShiftMap(shift, new ShiftScheduleModel());

//			Shift will not edit if it is scheduled
			if (!scheduleRepo.existsByShiftId(shiftEntity.getId()).isEmpty()) {
				throw new RequestNotValidException("Can't edit shift, shift is scheduled.");
			}

			Timestamp dateTime = Timestamp.from(Instant.now());
			shiftEntity.setUpdatedAt(dateTime);

			ShiftScheduleModel updatedShift = repo.save(shiftEntity);
			if (updatedShift == null) {
				throw new FailedToSaveException(
						"Failed to update shift with shift id: " + shift.getId() + ", please try once again");
			}
			return ShiftMapper.toDto(updatedShift, shift);
		} else {
			throw new RequestNotValidException("Request not valid, enter valid date, time and required count");
		}
	}

//	Return the list of shift from specific date range
	public List<ShiftScheduleModel> getShiftByDateRange(List<LocalDate> dateArr) throws ResourceNotFoundException {
//		return repo.findShiftByDateRange(dateArr.get(0), dateArr.get(1));

		Set<Long> shiftIds = repo.findShiftByDateRange(dateArr.get(0), dateArr.get(1));
		if (shiftIds.isEmpty()) {
			throw new ResourceNotFoundException(
					"No shift is found for the given date range(" + dateArr.get(0) + " to " + dateArr.get(1) + ").");
		}
		return repo.findAllById(shiftIds);
	}

//	Return the list of shift data which are schedule for that particular date
	public List<ShiftScheduleModel> getShift(ShiftGetRequestUsingDateDto date) throws ResourceNotFoundException {
		List<ShiftScheduleModel> shiftList = repo.findByDate(date.getDate());
		if (shiftList.isEmpty()) {
			throw new ResourceNotFoundException("No shift found for the date: " + date);
		}
		return shiftList;
	}

//	Return all shifts
	public List<ShiftScheduleModel> getAllShifts() throws ResourceNotFoundException {
		List<ShiftScheduleModel> shiftList = repo.findAll();
		if (shiftList.isEmpty()) {
			throw new ResourceNotFoundException("No shift on the portal, add new shift to see");
		}
		return shiftList;
	}

//	Delete shift by shift id
	public void deleteShift(long shiftId) throws ResourceNotFoundException {
		if (!repo.existsById(shiftId)) {
			throw new ResourceNotFoundException(
					"Requested shift for delete with id: " + shiftId + " is not exist, please enter valid shift id");
		}
		repo.deleteById(shiftId);
	}

//	Check if the shift is exists or not
	public boolean isShiftExists(ShiftDto shift) {
		return repo.existsById(shift.getId());
	}

//	Check all the data of shift is valid or not before save
	private boolean isRequestValid(ShiftDto shift) {
		LocalDate date = LocalDate.now();

		if (shift.getDate().isBefore(date)) {
			return false;
		}
		if (shift.getStartTime() > shift.getEndTime()) {
			return false;
		}
		if (shift.getRequiredStaffCount() < 1) {
			return false;
		}
		return true;
	}

}
