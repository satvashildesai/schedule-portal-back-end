package com.storeshop.scheduleportal.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storeshop.scheduleportal.dto.AvailableDateTimeDto;
import com.storeshop.scheduleportal.dto.StaffDto;
import com.storeshop.scheduleportal.entity.StaffMemberModel;
import com.storeshop.scheduleportal.exceptions.FailedToSaveException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.repository.AvailableDateTimeRepository;
import com.storeshop.scheduleportal.repository.ScheduleRepository;
import com.storeshop.scheduleportal.repository.StaffRepository;
import com.storeshop.scheduleportal.util.StaffMapper;

@Service
public class StaffService {

	@Autowired
	private StaffRepository repo;

	@Autowired
	private AvailableDateTimeRepository dateTimeRepo;

	@Autowired
	private ScheduleRepository scheduleRepo;

//	Create and save new staff
	public void saveMember(StaffDto staffDto)
			throws RequestNotValidException, FailedToSaveException, ResourceNotFoundException {

		if (isRequestValid(staffDto)) {
			StaffMemberModel staffEntity = StaffMapper.staffMap(staffDto, new StaffMemberModel());
			StaffMemberModel savedStaff = repo.save(staffEntity);

			if (savedStaff == null) {
				throw new FailedToSaveException("Failed to register staff member on portal, please try once again");
			}
		} else {
			throw new RequestNotValidException("Staff member faild to save, enter valid date and time");
		}
	}

//	Update staff details of particular staff 
	public void updateStaffDetails(StaffDto staffDto)
			throws RequestNotValidException, ResourceNotFoundException, FailedToSaveException {
		if (isRequestValid(staffDto)) {

//			Handle java.util.NoSuchElementException: No value present
			StaffMemberModel dbStaff = repo.findById(staffDto.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Staff member of Id: " + staffDto.getId()
							+ " not exist to update, please enter valid staff member"));

//			Staff will not edit if it is scheduled
			if (!(scheduleRepo.existsByStaffId(staffDto.getId())).isEmpty()) {
				throw new RequestNotValidException("Can't edit staff, staff is scheduled.");
			}

			dateTimeRepo.deleteAllByStaffId(dbStaff);

//			Update the AvailableDateTime list
//			List<AvailableDateTimeDto> dateTime = staffDto.getAvailableDateTime();
//			List<AvailableDateTimeModel> dbDateTime = dbStaff.getAvailableDateTime();
//			int dateTimeCount = dbDateTime.size();
//			for (int i = 0; i < dateTimeCount; i++) {
//				dateTime.get(i).setId(dbDateTime.get(i).getId());
//			}

			StaffMemberModel staffEntity = StaffMapper.updatedStaffMap(staffDto, new StaffMemberModel());

			StaffMemberModel updatedStaff = repo.save(staffEntity);
			if (updatedStaff == null) {
				throw new FailedToSaveException(
						"Failed to update staff member detals, please try once again with valid details");
			}
		} else {
			throw new RequestNotValidException(
					"Failed to update staff details, please validate the staff details once again");
		}
	}

//	Get staff by date range
	public List<StaffMemberModel> getStaffByDateRange(List<LocalDate> dateArr) {
		Set<Long> staffIds = repo.getStaffByDateRange(dateArr.get(0), dateArr.get(1));
		return repo.findAllById(staffIds);
	}

//	Get staff by staffId
	public StaffMemberModel getStaff(long staffId) throws ResourceNotFoundException {
		return repo.findById(staffId).orElseThrow(() -> new ResourceNotFoundException(
				"Not found staff member with id: " + staffId + " please enter valid staff id"));
	}

//	Get all staff
	public List<StaffMemberModel> getAllStaff() throws ResourceNotFoundException {

		List<StaffMemberModel> staffList = repo.findAll();
		if (staffList.isEmpty()) {
			throw new ResourceNotFoundException("No staff is present on the portal, add new staff to see");
		}
		return staffList;
	}

//	Delete staff by staffId
	public void deleteStaff(long staffId) throws ResourceNotFoundException {
		repo.findById(staffId).orElseThrow(() -> new ResourceNotFoundException(
				"Requested staff for delete with id: " + staffId + " is not exist, please enter valid staff id"));
		repo.deleteById(staffId);
	}

//	Validate parameter of request before use save or update the staff details
	private boolean isRequestValid(StaffDto staffDto) {

		List<AvailableDateTimeDto> dateTimeList = staffDto.getAvailableDateTime();

		LocalDate date = LocalDate.now();

		for (AvailableDateTimeDto dt : dateTimeList) {
			if (dt.getDate().isBefore(date)) {
				return false;
			}
			if (dt.getStartTime() < 0 || dt.getEndTime() > 24 || dt.getStartTime() > dt.getEndTime()) {
				return false;
			}
		}
		return true;
	}

	public List<StaffMemberModel> getAvailableStaff(LocalDate date, long startTime, long endTime)
			throws ResourceNotFoundException {
		List<StaffMemberModel> availableStaffList = repo.findAvailableStaffForShift(date, startTime, endTime);
		if (availableStaffList.isEmpty()) {
			throw new ResourceNotFoundException("No staff is available for the given shift.");
		}
		return availableStaffList;
	}

}
