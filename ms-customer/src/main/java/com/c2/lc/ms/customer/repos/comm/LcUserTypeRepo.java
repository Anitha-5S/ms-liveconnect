package com.c2.lc.ms.customer.repos.comm;

import com.c2.lc.ms.customer.entities.comm.LcUserType;
import com.c2.lc.ms.customer.entities.comm.LcUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Repository("LcUserTypeRepository")
public interface LcUserTypeRepo extends JpaRepository<LcUserType, Long> {

    @Query("SELECT l FROM LcUserType l WHERE l.mobileNo = :mobileNo")
    List<LcUserType> countMobileNumber(String mobileNo);

    @Query("SELECT COUNT(l.mobileNo) FROM LcUserType l WHERE l.mobileNo = :mobileNo AND l.type = :type")
    int checkMobileNumberWithTypeExists(String mobileNo, String type);

    @Query(value = "select * from lc_user_type lut where c_mobile_no = :mobileNumber and c_type = :type ",nativeQuery = true)
    LcUserType getByMobileAndType(String mobileNumber, String type);

    //void updateMobileNumber(String c2code, String c_mobile_no);
}
