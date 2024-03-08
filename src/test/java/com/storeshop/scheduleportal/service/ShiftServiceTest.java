package com.storeshop.scheduleportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.storeshop.scheduleportal.dto.ShiftDto;
import com.storeshop.scheduleportal.dto.ShiftGetRequestUsingDateDto;
import com.storeshop.scheduleportal.entity.ShiftScheduleModel;
import com.storeshop.scheduleportal.exceptions.FailedToSaveException;
import com.storeshop.scheduleportal.exceptions.RequestNotValidException;
import com.storeshop.scheduleportal.exceptions.ResourceNotFoundException;
import com.storeshop.scheduleportal.repository.ScheduleRepository;
import com.storeshop.scheduleportal.repository.ShiftRepository;

//@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ShiftServiceTest {

	@Mock
	private ShiftRepository shiftRepo;

	@Mock
	private ScheduleRepository scheduleRepo;

	private ShiftService shiftService;

	@BeforeEach
	void setUp() {
		this.shiftService = new ShiftService(this.shiftRepo, this.scheduleRepo);
	}

	@Test
	void saveShift_success() throws RequestNotValidException, FailedToSaveException {
		ShiftScheduleModel expectedShift = new ShiftScheduleModel();
		expectedShift.setDate(LocalDate.now());
		expectedShift.setStartTime(1L);
		expectedShift.setEndTime(2L);
		expectedShift.setRequiredStaffCount(1L);

		when(shiftRepo.save(ArgumentMatchers.any(ShiftScheduleModel.class))).thenReturn(expectedShift);

		ShiftDto dto = new ShiftDto();
		dto.setDate(expectedShift.getDate());
		dto.setStartTime(expectedShift.getStartTime());
		dto.setEndTime(expectedShift.getEndTime());
		dto.setRequiredStaffCount(expectedShift.getRequiredStaffCount());

		shiftService.saveShift(dto);

		verify(shiftRepo).save(ArgumentMatchers.any(ShiftScheduleModel.class));
	}

	@Test
	void saveShift_failedToSave() throws RequestNotValidException, FailedToSaveException {
		ShiftDto dto = new ShiftDto();
		dto.setDate(LocalDate.now());
		dto.setStartTime(1L);
		dto.setEndTime(2L);
		dto.setRequiredStaffCount(1L);

		when(shiftRepo.save(ArgumentMatchers.any(ShiftScheduleModel.class))).thenReturn(null);

		assertThrows(FailedToSaveException.class, () -> {
			shiftService.saveShift(dto);
		});

		verify(shiftRepo).save(ArgumentMatchers.any(ShiftScheduleModel.class));
	}

	@Test
	void saveShift_invalidRequest() {
		ShiftDto dto = new ShiftDto();
		dto.setDate(LocalDate.now());
		dto.setStartTime(2L);
		dto.setEndTime(1L);
		dto.setRequiredStaffCount(1L);

//		When start time is greater than end time then it will give RequestNotValidException
		assertThrows(RequestNotValidException.class, () -> {
			shiftService.saveShift(dto);
		});

	}

	@Test
	void updateShift_success() throws RequestNotValidException, FailedToSaveException {
		when(this.shiftRepo.save(ArgumentMatchers.any(ShiftScheduleModel.class))).thenReturn(new ShiftScheduleModel());
		when(this.scheduleRepo.existsByShiftId(1L)).thenReturn(new ArrayList<Long>(Arrays.asList()));
		
		ShiftDto shiftDto = new ShiftDto(1L, LocalDate.now(), 8L, 16L, 2L, null, null);
		this.shiftService.updateShift(shiftDto);
		
		verify(this.scheduleRepo).existsByShiftId(1L);
		verify(this.shiftRepo).save(ArgumentMatchers.any(ShiftScheduleModel.class));
	}

	@Test
	void updateShift_requestNotValid() {
		/*
		 * Condition when "RequestNotValidException" raise: 1. If shift date is before
		 * the current date. 2. Start and end date is not in 1 to 24. 3. Start date is
		 * greater than end date.
		 */
		ShiftDto shiftDto = new ShiftDto(1L, LocalDate.now(), 18L, 16L, 2L, null, null);
		assertThrows(RequestNotValidException.class, () -> {
			this.shiftService.updateShift(shiftDto);
		});
	}

	@Test
	void updateShift_failedToSave() {
		when(this.shiftRepo.save(ArgumentMatchers.any(ShiftScheduleModel.class))).thenReturn(null);
		
		ShiftDto shiftDto = new ShiftDto(1L, LocalDate.now(), 8L, 16L, 2L, null, null);
		assertThrows(FailedToSaveException.class, () -> {
			this.shiftService.updateShift(shiftDto);
		});
		
		verify(this.shiftRepo).save(ArgumentMatchers.any(ShiftScheduleModel.class));
	}

	@Test
	void getAllShifts_success() throws ResourceNotFoundException {
		ArrayList<ShiftScheduleModel> expectedShiftList = new ArrayList<ShiftScheduleModel>();
		expectedShiftList.add(new ShiftScheduleModel());

		when(shiftRepo.findAll()).thenReturn(expectedShiftList);

		List<ShiftScheduleModel> actualShiftList = shiftService.getAllShifts();
		assertEquals(expectedShiftList, actualShiftList);

		verify(shiftRepo).findAll();
	}

	@Test
	void getAllShifts_notFound() {
		when(shiftRepo.findAll()).thenReturn(new ArrayList<ShiftScheduleModel>());
		assertThrows(ResourceNotFoundException.class, ()->{
			shiftService.getAllShifts();
		});
		verify(shiftRepo).findAll();
	}

	@Test
	void getShiftByDateRange_success() throws ResourceNotFoundException {
		ArrayList<ShiftScheduleModel> expectedShiftList = new ArrayList<ShiftScheduleModel>();
		expectedShiftList.add(new ShiftScheduleModel());

		Set<Long> ids = new HashSet<Long>();
		ids.add(1L);

		when(shiftRepo.findShiftByDateRange(LocalDate.now(), LocalDate.now())).thenReturn(ids);
		when(shiftRepo.findAllById(ids)).thenReturn(expectedShiftList);

		assertEquals(expectedShiftList, shiftService
				.getShiftByDateRange(new ArrayList<LocalDate>(Arrays.asList(LocalDate.now(), LocalDate.now()))));

		verify(shiftRepo).findShiftByDateRange(LocalDate.now(), LocalDate.now());
		verify(shiftRepo).findAllById(ids);
	}

	@Test
	void getShiftByDateRange_notFound() {
		when(shiftRepo.findShiftByDateRange(LocalDate.now(), LocalDate.now())).thenReturn(new HashSet<Long>());
		
		assertThrows(ResourceNotFoundException.class, ()->{
			shiftService.getShiftByDateRange(new ArrayList<LocalDate>(Arrays.asList(LocalDate.now(), LocalDate.now())));
		});

		verify(shiftRepo).findShiftByDateRange(LocalDate.now(), LocalDate.now());
//		Set<Long> ids = new HashSet<>();
//		ids.add(1L);
//		verify(shiftRepo).findAllById(ids);
	}

	@Test
	void getShift_success() throws ResourceNotFoundException {
		ArrayList<ShiftScheduleModel> expectedShiftList = new ArrayList<ShiftScheduleModel>(
				Arrays.asList(new ShiftScheduleModel()));
		when(shiftRepo.findByDate(LocalDate.now())).thenReturn(expectedShiftList);

		assertEquals(expectedShiftList, shiftService.getShift(new ShiftGetRequestUsingDateDto(LocalDate.now())));

		verify(shiftRepo).findByDate(LocalDate.now());
	}

	@Test
	void getShift_notFound() {
		when(shiftRepo.findByDate(LocalDate.now())).thenReturn(new ArrayList<ShiftScheduleModel>());
		
		assertThrows(ResourceNotFoundException.class, ()->{
			shiftService.getShift(new ShiftGetRequestUsingDateDto(LocalDate.now()));
		});
		
		verify(shiftRepo).findByDate(LocalDate.now());
	}

	@Test
	void deleteShift_notFound() {
		Long shiftId = 1L;
		when(shiftRepo.existsById(shiftId)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> {
			shiftService.deleteShift(shiftId);
		});

		verify(shiftRepo).existsById(shiftId);
	}

	@Test
	void isShiftExists_success() {
		ShiftDto dto = new ShiftDto();
		dto.setId(1L);
		when(shiftRepo.existsById(dto.getId())).thenReturn(true);
		assertEquals(true, shiftService.isShiftExists(dto));
		verify(shiftRepo).existsById(dto.getId());
	}
}
