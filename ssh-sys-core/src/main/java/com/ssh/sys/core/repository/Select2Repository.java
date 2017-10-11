package com.ssh.sys.core.repository;

import com.ssh.common.core.annotation.Query;
import com.ssh.common.core.annotation.RepositoryBean;
import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.page.Pageable;

import java.util.List;
import java.util.Map;

@RepositoryBean
public interface Select2Repository {

    @Query(transformer = Map.class)
    List<Map> getActorListSelective(MapDTO mapDTO);

    @Query(transformer = Map.class)
    List<Map> getFuncListSelective(MapDTO mapDTO);

    @Query(transformer = Map.class)
    List<Map> getRoleListSelective(MapDTO mapDTO);

    @Query(value = "getActorListSelective", transformer = Map.class)
    Page<Map> getActorPageSelective(Pageable pageable);

    @Query(value = "getFuncListSelective", transformer = Map.class)
    Page<Map> getFuncPageSelective(Pageable pageable);

}
