package com.storeshop.scheduleportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storeshop.scheduleportal.dto.AvailableDateTimeDto;
import com.storeshop.scheduleportal.dto.StaffDto;
import com.storeshop.scheduleportal.entity.AvailableDateTimeModel;
import com.storeshop.scheduleportal.entity.StaffMemberModel;
import com.storeshop.scheduleportal.exceptions.FailedToSaveException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.repository.AvailableDateTimeRepository;
import com.storeshop.scheduleportal.repository.ScheduleRepository;
import com.storeshop.scheduleportal.repository.StaffRepository;

@ExtendWith(MockitoExtension.class)
public class StaffServiceTest {

	@Mock
	StaffRepository staffRepo;

	@Mock
	AvailableDateTimeRepository dateTimeRepo;

	@Mock
	ScheduleRepository scheduleRepo;

	StaffService staffService;

	@BeforeEach
	void setUp() {
		this.staffService = new StaffService(staffRepo, dateTimeRepo, scheduleRepo);
	}

	@Test
	void saveMember_success() throws RequestNotValidException, FailedToSaveException, ResourceNotFoundException {
		StaffMemberModel staff = new StaffMemberModel(1L, "Satvashil", new ArrayList<AvailableDateTimeModel>());
		when(this.staffRepo.save(ArgumentMatchers.any(StaffMemberModel.class))).thenReturn(staff);

		StaffDto staffDto = new StaffDto(1L, "Satvashil", new ArrayList<AvailableDateTimeDto>(), null, null);

		staffService.saveMember(staffDto);

		verify(this.staffRepo).save(ArgumentMatchers.any(StaffMemberModel.class));
	}

	@Test
	void saveMember_requestNotValid() {

		List<AvailableDateTimeDto> dateTimeList = new ArrayList<>();
		/*
		 * Conditions when "RequestNotFoundException" raise: If we pass the date before
		 * the today's date. Start & end date are not between (1 to 24). If start date
		 * is greater than end date.
		 */
		dateTimeList.add(new AvailableDateTimeDto(1L, LocalDate.now(), 18L, 16L, null));

		StaffDto staffDto = new StaffDto(1L, "Satvashil", dateTimeList, null, null);
		assertThrows(RequestNotValidException.class, () -> {
			staffService.saveMember(staffDto);
		});
	}

	@Test
	void saveMember_failedToSave() {
		when(this.staffRepo.save(ArgumentMatchers.any(StaffMemberModel.class))).thenReturn(null);
		
		StaffDto staffDto = new StaffDto(1L, "Satvashil", new ArrayList<AvailableDateTimeDto>(), null, null);
		assertThrows(FailedToSaveException.class, ()->{
			this.staffService.saveMember(staffDto);
		});

		verify(this.staffRepo).save(ArgumentMatchers.any(StaffMemberModel.class));

	}

	@Test
	void updateStaffDetails_success() throws RequestNotValidException, FailedToSaveException, ResourceNotFoundException {
		when(this.staffRepo.save(ArgumentMatchers.any(StaffMemberModel.class))).thenReturn(new StaffMemberModel());
		when(this.staffRepo.findById(1L)).thenReturn(Optional.of(new StaffMemberModel()));
		when(this.scheduleRepo.existsByStaffId(1L)).thenReturn(new ArrayList<Long>());
		
		StaffDto staffDto = new StaffDto(1L, "Satvashil", new ArrayList<AvailableDateTimeDto>(), null, null);
		this.staffService.updateStaffDetails(staffDto);
		
		verify(this.staffRepo).save(ArgumentMatchers.any(StaffMemberModel.class));
		verify(this.staffRepo).findById(1L);
		verify(this.scheduleRepo).existsByStaffId(1L);
	}

	@Test
	void updateStaffDetails_requestNotValid() {
		List<AvailableDateTimeDto> dateTimeList = new ArrayList<AvailableDateTimeDto>();
		dateTimeList.add(new AvailableDateTimeDto(1L, LocalDate.now(), 18L, 16L, null));

		/*
		 * Conditions when "RequestNotValidException" occurred: 1. LocalDate is less
		 * than current date. 2. Start date and end date is not in between 1 to 24. 3.
		 * Start date is greater than end date.
		 */
		StaffDto staffDto = new StaffDto(1L, "Satvashil", dateTimeList, null, null);
		assertThrows(RequestNotValidException.class, () -> {
			staffService.updateStaffDetails(staffDto);
		});
	}

	@Test 
	void updateStaffDetails_requestNotValid_ifStaffScheduled() {
		when(this.staffRepo.findById(1L)).thenReturn(Optional.of(new StaffMemberModel()));
		List<Long> idList = new ArrayList<Long>();
		idList.add(1L);
		when(this.scheduleRepo.existsByStaffId(1L)).thenReturn(idList);
		
		StaffDto staffDto = new StaffDto(1L, "Satvashil", new ArrayList<AvailableDateTimeDto>(), null, null);
		assertThrows(RequestNotValidException.class, ()->{
			this.staffService.updateStaffDetails(staffDto);
		});
		
		verify(this.staffRepo).findById(1L);
		verify(this.scheduleRepo).existsByStaffId(1L);
	}

	@Test 
	void updateStaffDetails_failedToSave() {
		when(this.staffRepo.save(ArgumentMatchers.any(StaffMemberModel.class))).thenReturn(null);
		when(this.staffRepo.findById(1L)).thenReturn(Optional.of(new StaffMemberModel()));
		when(this.scheduleRepo.existsByStaffId(1L)).thenReturn(new ArrayList<Long>());
		
		StaffDto staffDto = new StaffDto(1L, "Satvashil", new ArrayList<AvailableDateTimeDto>(), null, null);
		assertThrows(FailedToSaveException.class, ()->{
			this.staffService.updateStaffDetails(staffDto);
		});
		
		verify(this.staffRepo).save(ArgumentMatchers.any(StaffMemberModel.class));
		verify(this.staffRepo).findById(1L);
		verify(this.scheduleRepo).existsByStaffId(1L);
	}

	@Test
	void updateStaffDetails_resourceNotFound() {
		when(this.staffRepo.findById(1L)).thenReturn(Optional.empty());
		
		StaffDto staffDto = new StaffDto(1L, "Satvashil", new ArrayList<AvailableDateTimeDto>(), null, null);
		assertThrows(ResourceNotFoundException.class, ()->{
			this.staffService.updateStaffDetails(staffDto);
		});
		
		verify(this.staffRepo).findById(1L);
	}

	@Test
	void getStaffByDateRange_success() throws ResourceNotFoundException {
		List<StaffMemberModel> expectedStaffList = new ArrayList<StaffMemberModel>();
		Set<Long> ids = new HashSet<Long>();
		ids.add(1L);
		when(this.staffRepo.getStaffByDateRange(LocalDate.now(), LocalDate.now())).thenReturn(ids);
		when(this.staffRepo.findAllById(ids)).thenReturn(expectedStaffList);

		List<LocalDate> dateList = new ArrayList<LocalDate>();
		dateList.add(LocalDate.now());
		dateList.add(LocalDate.now());

		List<StaffMemberModel> actualStaffList = this.staffService.getStaffByDateRange(dateList);
		assertEquals(expectedStaffList, actualStaffList);

		verify(this.staffRepo).getStaffByDateRange(LocalDate.now(), LocalDate.now());
		verify(this.staffRepo).findAllById(ids);
	}

	@Test
	void getStaffByDateRange_notFound() {
		when(this.staffRepo.getStaffByDateRange(LocalDate.now(), LocalDate.now())).thenReturn(new HashSet<Long>());

		List<LocalDate> dateList = new ArrayList<LocalDate>();
		dateList.add(LocalDate.now());
		dateList.add(LocalDate.now());

		assertThrows(ResourceNotFoundException.class, () -> {
			this.staffService.getStaffByDateRange(dateList);
		});

		verify(this.staffRepo).getStaffByDateRange(LocalDate.now(), LocalDate.now());
	}

	@Test
	void getStaff_success() throws ResourceNotFoundException {
		StaffMemberModel expectedStaff = new StaffMemberModel();
		when(this.staffRepo.findById(1L)).thenReturn(Optional.of(expectedStaff));
		assertEquals(expectedStaff, this.staffService.getStaff(1L));
		verify(this.staffRepo).findById(1L);
	}

	@Test
	void getStaff_notFound() {
		when(this.staffRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, ()->{
			this.staffService.getStaff(1L);
		});
		verify(this.staffRepo).findById(1L);
	}

	@Test
	void getAllStaff_success() throws ResourceNotFoundException {
		List<StaffMemberModel> expectedStaffList = new ArrayList<StaffMemberModel>();
		expectedStaffList.add(new StaffMemberModel());
		when(this.staffRepo.findAll()).thenReturn(expectedStaffList);

		assertEquals(expectedStaffList, this.staffService.getAllStaff());

		verify(this.staffRepo).findAll();
	}

	@Test
	void getAllStaff_notFound() {
		
		when(this.staffRepo.findAll()).thenReturn(new ArrayList<StaffMemberModel>());
		assertThrows(ResourceNotFoundException.class, ()->{
			this.staffService.getAllStaff();
		});
		verify(this.staffRepo).findAll();
	}

	@Test
	void deleteStaff_notFound() {
		when(this.staffRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, ()->{
			this.staffService.deleteStaff(1L);
		});
		verify(this.staffRepo).findById(1L);
	}

	@Test
	void getAvailableStaff_success() throws ResourceNotFoundException {
		List<StaffMemberModel> expectedStaffList = new ArrayList<StaffMemberModel>();
		expectedStaffList.add(new StaffMemberModel());
		when(this.staffRepo.findAvailableStaffForShift(LocalDate.now(), 8L, 18L)).thenReturn(expectedStaffList);

		assertEquals(expectedStaffList, this.staffService.getAvailableStaff(LocalDate.now(), 8L, 18L));

		verify(this.staffRepo).findAvailableStaffForShift(LocalDate.now(), 8L, 18L);
	}

	@Test
	void getAvailableStaff_notFound() {
		when(this.staffRepo.findAvailableStaffForShift(LocalDate.now(), 8L, 16L)).thenReturn(new ArrayList<StaffMemberModel>());
		assertThrows(ResourceNotFoundException.class, ()->{
			this.staffService.getAvailableStaff(LocalDate.now(), 8L, 16L);
		});
		verify(this.staffRepo).findAvailableStaffForShift(LocalDate.now(), 8L, 16L);
	}

}
