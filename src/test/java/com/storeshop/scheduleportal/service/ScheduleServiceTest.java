package com.storeshop.scheduleportal.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

	@Mock
	private ScheduleRepository scheduleRepo;

	@Mock
	private StaffRepository staffRepo;

	@Mock
	private ShiftRepository shiftRepo;

	private ScheduleService scheduleService;

	@BeforeEach
	void setUp() {
		this.scheduleService = new ScheduleService(scheduleRepo, staffRepo, shiftRepo);
	}

	@Test
	void assignShiftToStaff_success() throws ResourceNotFoundException, ResourceAlreadyAssignedException,
			InvalidStaffCountException, RequestNotValidException {
//		Staff member ids used for dummy ids.
		List<Long> ids = new ArrayList<Long>();

		when(this.shiftRepo.findById(1L)).thenReturn(Optional.of(new ShiftScheduleModel()));
		when(this.staffRepo.findAllById(ids)).thenReturn(new ArrayList<StaffMemberModel>());
		when(this.scheduleRepo.save(ArgumentMatchers.any(ShiftAssignmentModel.class)))
				.thenReturn(new ShiftAssignmentModel());

		ScheduleDto scheduleDto = new ScheduleDto();
		scheduleDto.setShiftSheduleId(1L);
		scheduleDto.setStaffMemberIds(ids);
		this.scheduleService.assignShiftToStaff(scheduleDto);

		verify(this.shiftRepo).findById(1L);
		verify(this.staffRepo).findAllById(ids);
		verify(this.scheduleRepo).save(ArgumentMatchers.any(ShiftAssignmentModel.class));
	}

}
