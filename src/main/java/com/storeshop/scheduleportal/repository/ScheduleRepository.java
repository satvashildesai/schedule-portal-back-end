package com.storeshop.scheduleportal.repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.storeshop.scheduleportal.entity.ShiftAssignmentModel;
import com.storeshop.scheduleportal.entity.ShiftScheduleModel;
import com.storeshop.scheduleportal.entity.StaffMemberModel;

@Repository
public interface ScheduleRepository extends JpaRepository<ShiftAssignmentModel, Long> {

	@Query("select staffMemberId from ShiftAssignmentModel where shiftSheduleId = ?1")
	List<StaffMemberModel> findStaffByShift(ShiftScheduleModel shift);

	@Query(nativeQuery = true, value = "select ssm.date from shift_assignment_model sam inner join shift_schedule_model ssm on sam.shift_shedule_id_id = ssm.id where sam.staff_member_id_id = ?1")
	List<Date> findAllAssignedDateByStaffId(Long staffId);

	@Query(nativeQuery = true, value = "select adtm.id from available_date_time_model adtm where adtm.staff_id = ?1 and adtm.date =?2")
	Long findAvailableStaffByDate(Long id, LocalDate date);

	@Query(nativeQuery = true, value = "select adtm.id from available_date_time_model adtm where adtm.staff_id = ?1 and adtm.start_time <= ?2 and adtm.end_time >= ?3 and adtm.id = ?4")
	Long findAvailableStaffByTime(Long id, Long startTime, Long endTime, Long dateTimeId);

//	Check if shift is scheduled or not
	@Query(nativeQuery = true, value = "select sam.id from shift_assignment_model sam where sam.shift_shedule_id_id = ?1")
	List<Long> existsByShiftId(Long shiftId);

//	Check if staff is scheduled or not
	@Query(nativeQuery = true, value = "select sam.id from shift_assignment_model sam where sam.staff_member_id_id = ?1")
	List<Long> existsByStaffId(Long staffId);

//	Find all staff assign to the particular shift
	@Query("select id from ShiftAssignmentModel where shiftSheduleId = ?1")
	List<Long> FindAllByShiftId(ShiftScheduleModel shift);

//	Find all assigned shift from given date range
	@Query(nativeQuery = true, value = "select sam.id from shift_assignment_model sam inner join shift_schedule_model ssm on sam.shift_shedule_id_id = ssm.id where date between ?1 and ?2")
	Set<Long> findAssignedShiftByDateRange(LocalDate startDate, LocalDate endDate);

//	Return all staff id assigned to shift
	@Query(nativeQuery = true, value = "select staff_member_id_id from shift_assignment_model where shift_shedule_id_id = ?1")
	List<Long> getStaffIdsByShiftId(long shiftId);

//	Update the staff is scheduled or not.
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update available_date_time_model set is_scheduled = true where date = ?1 and staff_id = ?2")
	void staffScheduledUpdate(LocalDate date, Long id);

//	Update the staff after delete schedule
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "update available_date_time_model set is_scheduled = false where date = ?2 and staff_id in ?1")
	void updateStaffAvailability(List<Long> staffIds, LocalDate shiftDate);

//	Update the shift schedule status
	@Modifying
	@Transactional
	@Query("update ShiftScheduleModel set isScheduled = ?1, scheduledAt = ?2 where id = ?3")
	void shiftScheduledUpdate(boolean status, Timestamp timestamp, Long id);

}
