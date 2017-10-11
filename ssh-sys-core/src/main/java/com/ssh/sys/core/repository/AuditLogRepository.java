package com.ssh.sys.core.repository;

import com.ssh.common.core.annotation.Param;
import com.ssh.common.core.annotation.Query;
import com.ssh.common.core.annotation.RepositoryBean;
import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.Pageable;

import java.util.List;
import java.util.Map;

@RepositoryBean
public interface AuditLogRepository {

    @Query(transformer = Map.class)
    List<Map> getListSelective(MapDTO mapDTO);

    @Query(value = "getListSelective", transformer = Map.class)
    Page<Map> getPageSelective(Pageable pageable);

    @Query(transformer = Map.class)
    List<Map> getDetailListAuditLogId(@Param("auditLogId") Long auditLogId);

}
