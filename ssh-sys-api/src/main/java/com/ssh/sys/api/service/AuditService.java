package com.ssh.sys.api.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;

import java.util.List;
import java.util.Map;

public interface AuditService {

    String BEAN_NAME = "auditService";

    List<Map> getDetailList(Long auditLogId);

    List<Map> getList(MapDTO mapDTO);

    Page<Map> getPage(MapDTO mapDTO, int offset, int limit);

}
