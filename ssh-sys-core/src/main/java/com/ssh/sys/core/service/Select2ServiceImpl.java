package com.ssh.sys.core.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.PageRequest;
import com.ssh.sys.api.service.Select2Service;
import com.ssh.sys.core.repository.Select2Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@Service(Select2Service.BEAN_NAME)
public class Select2ServiceImpl implements Select2Service {

    @Autowired
    protected Select2Repository select2Repository;

    @Override
    public List<Map> getActorList(MapDTO mapDTO) {
        return select2Repository.getActorListSelective(mapDTO);
    }

    @Override
    public List<Map> getFuncList(MapDTO mapDTO) {
        return select2Repository.getFuncListSelective(mapDTO);
    }

    @Override
    public List<Map> getRoleList(MapDTO mapDTO) {
        return select2Repository.getRoleListSelective(mapDTO);
    }

    @Override
    public Page<Map> getActorPage(MapDTO mapDTO, int offset, int limit) {
        return select2Repository.getActorPageSelective(PageRequest.newInstance(mapDTO, offset, limit));
    }

    @Override
    public Page<Map> getFuncPage(MapDTO mapDTO, int offset, int limit) {
        return select2Repository.getFuncPageSelective(PageRequest.newInstance(mapDTO, offset, limit));
    }

}
