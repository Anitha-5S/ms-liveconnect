package com.c2.lc.ms.master.repos.mysql;

import com.c2.lc.ms.master.entities.mysql.UrlConfig;
import com.c2.lc.ms.master.entities.mysql.UrlConfigPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlConfigRepository extends JpaRepository<UrlConfig, UrlConfigPK> {}
