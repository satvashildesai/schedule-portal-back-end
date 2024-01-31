package com.storeshop.scheduleportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.storeshop.scheduleportal.entity.AvailableDateTimeModel;
import com.storeshop.scheduleportal.entity.StaffMemberModel;

@Repository
public interface AvailableDateTimeRepository extends JpaRepository<AvailableDateTimeModel, Long> {

	@Modifying
	@Transactional
	@Query("delete from AvailableDateTimeModel where staff = ?1")
	void deleteAllByStaffId(StaffMemberModel dbStaff);

}
