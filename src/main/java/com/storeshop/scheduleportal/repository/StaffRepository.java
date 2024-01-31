package com.storeshop.scheduleportal.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.storeshop.scheduleportal.entity.StaffMemberModel;

@Repository
public interface StaffRepository extends JpaRepository<StaffMemberModel, Long> {

//	Find staff of specific date range (26-jan-2024 to 31-jan-2024)
	@Query(nativeQuery = true, value = "select adtm.staff_id from available_date_time_model adtm where adtm.date between ?1 and ?2")
	Set<Long> getStaffByDateRange(LocalDate date1, LocalDate date2);

//	Find available staffs to assign for the given shift
	@Query("select staff from AvailableDateTimeModel where date=?1 and startTime<=?2 and endTime>=?3 and isScheduled = false")
	List<StaffMemberModel> findAvailableStaffForShift(LocalDate date, long startTime, long endTime);

}
