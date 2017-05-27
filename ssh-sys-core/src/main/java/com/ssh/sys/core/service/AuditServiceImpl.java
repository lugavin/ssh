package com.ssh.sys.core.service;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.PageRequest;
import com.ssh.sys.api.service.AuditService;
import com.ssh.sys.core.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service(AuditService.BEAN_NAME)
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public List<Map> getList(ModelMapDTO modelMapDTO) {
        return auditLogRepository.getListSelective(modelMapDTO);
    }

    @Override
    public List<Map> getDetailList(Long auditLogId) {
        return auditLogRepository.getDetailListAuditLogId(auditLogId);
    }

    @Override
    public Page<Map> getPage(ModelMapDTO modelMapDTO, int offset, int limit) {
        return auditLogRepository.getPageSelective(PageRequest.newInstance(modelMapDTO, offset, limit));
    }

}
