package com.ssh.sys.api.service;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;

import java.util.List;
import java.util.Map;

public interface Select2Service {

    String BEAN_NAME = "select2Service";

    List<Map> getActorList(ModelMapDTO modelMapDTO);

    List<Map> getFuncList(ModelMapDTO modelMapDTO);

    List<Map> getRoleList(ModelMapDTO modelMapDTO);

    Page<Map> getActorPage(ModelMapDTO modelMapDTO, int offset, int limit);

    Page<Map> getFuncPage(ModelMapDTO modelMapDTO, int offset, int limit);

}
