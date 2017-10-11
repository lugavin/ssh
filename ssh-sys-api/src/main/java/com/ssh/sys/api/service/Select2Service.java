package com.ssh.sys.api.service;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;

import java.util.List;
import java.util.Map;

public interface Select2Service {

    String BEAN_NAME = "select2Service";

    List<Map> getActorList(MapDTO mapDTO);

    List<Map> getFuncList(MapDTO mapDTO);

    List<Map> getRoleList(MapDTO mapDTO);

    Page<Map> getActorPage(MapDTO mapDTO, int offset, int limit);

    Page<Map> getFuncPage(MapDTO mapDTO, int offset, int limit);

}
