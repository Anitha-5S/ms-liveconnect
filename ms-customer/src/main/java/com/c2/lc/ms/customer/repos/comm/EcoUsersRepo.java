package com.c2.lc.ms.customer.repos.comm;

import com.c2.lc.ms.customer.entities.comm.EcoUsers;
import com.c2.lc.ms.customer.entities.comm.EcoUsersPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("EcoUsersRepository")
public interface EcoUsersRepo extends JpaRepository<EcoUsers, EcoUsersPK> {
}
