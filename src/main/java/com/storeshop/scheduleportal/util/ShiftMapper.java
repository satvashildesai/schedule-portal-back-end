package com.storeshop.scheduleportal.util;

import com.storeshop.scheduleportal.dto.ShiftDto;
import com.storeshop.scheduleportal.entity.ShiftScheduleModel;

public class ShiftMapper {

	public static ShiftScheduleModel shiftMap(ShiftDto dto, ShiftScheduleModel entity) {
		entity.setDate(dto.getDate());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
		entity.setRequiredStaffCount(dto.getRequiredStaffCount());
		return entity;
	}

	public static ShiftScheduleModel updatedShiftMap(ShiftDto dto, ShiftScheduleModel entity) {
		entity.setId(dto.getId());
		entity.setDate(dto.getDate());
		entity.setStartTime(dto.getStartTime());
		entity.setEndTime(dto.getEndTime());
		entity.setRequiredStaffCount(dto.getRequiredStaffCount());
		return entity;
	}

	public static ShiftDto toDto(ShiftScheduleModel entity, ShiftDto dto) {
		dto.setId(entity.getId());
		dto.setDate(entity.getDate());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		dto.setRequiredStaffCount(entity.getRequiredStaffCount());
		return dto;
	}
}
