package com.storeshop.scheduleportal.util;

import java.util.ArrayList;
import java.util.List;

import com.storeshop.scheduleportal.dto.AvailableDateTimeDto;
import com.storeshop.scheduleportal.dto.StaffDto;
import com.storeshop.scheduleportal.entity.AvailableDateTimeModel;
import com.storeshop.scheduleportal.entity.StaffMemberModel;

public class StaffMapper {

	public static StaffMemberModel staffMap(StaffDto dto, StaffMemberModel entity) {
		entity.setName(dto.getName());
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());

		List<AvailableDateTimeModel> modelDateTime = new ArrayList<>();
		for (AvailableDateTimeDto dt : dto.getAvailableDateTime()) {
			modelDateTime.add(new AvailableDateTimeModel(dt.getDate(), dt.getStartTime(), dt.getEndTime(), entity));
		}
		entity.setAvailableDateTime(modelDateTime);
		return entity;
	}

	public static StaffMemberModel updatedStaffMap(StaffDto dto, StaffMemberModel entity) {
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());

		List<AvailableDateTimeModel> modelDateTime = new ArrayList<>();
		for (AvailableDateTimeDto dt : dto.getAvailableDateTime()) {
			modelDateTime.add(new AvailableDateTimeModel(dt.getDate(), dt.getStartTime(), dt.getEndTime(), entity));
		}
		entity.setAvailableDateTime(modelDateTime);
		return entity;
	}

}
