package com.c2.lc.ms.customer.repos.customer;

import com.c2.lc.ms.customer.entities.customer.ContactDetailEntity;
import com.c2.lc.ms.customer.entities.customer.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("OtherDocumentsRepo")
public interface OtherDocumentsRepo extends JpaRepository<DocumentEntity, Long> {

}
