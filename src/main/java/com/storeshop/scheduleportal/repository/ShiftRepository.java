package com.storeshop.scheduleportal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.storeshop.scheduleportal.entity.ShiftScheduleModel;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftScheduleModel, Long> {
	public List<ShiftScheduleModel> findByDate(LocalDate date);

//	Find all shift present on given date range
//	@Query("select id from ShiftScheduleModel where date between ?1 and ?2")
//	public List<ShiftScheduleModel> findShiftByDateRange(LocalDate date1, LocalDate date2);

	@Query(nativeQuery = true, value = "select id from shift_schedule_model where date between ?1 and ?2")
	public Set<Long> findShiftByDateRange(LocalDate date1, LocalDate date2);

}
