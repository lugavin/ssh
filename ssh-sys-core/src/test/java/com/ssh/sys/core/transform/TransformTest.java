package com.ssh.sys.core.transform;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.util.BeanMapper;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.core.entity.UserEntity;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransformTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransformTest.class);

    public static UserDTO map(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setPass(entity.getPass());
        dto.setStatus(entity.getStatus());
        dto.setSalt(entity.getSalt());
        return dto;
    }

    public static List<UserDTO> map(List<UserEntity> entities) {
        List<UserDTO> dtos = new ArrayList<>();
        for (UserEntity entity : entities) {
            dtos.add(map(entity));
        }
        return dtos;
    }

    @Test
    public void test1BeanToMap() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setId(1001L);
        dto.setName("King");
        MapDTO map = new MapDTO();
        BeanMapper.map(dto, map);
        LOGGER.debug("Bean --> Map : {}", map);
    }

    @Test
    public void test2MapToBean() throws Exception {
        MapDTO map = new MapDTO();
        map.put("id", 1001L);
        map.put("name", "King");
        map.put("createDate", Calendar.getInstance().getTime());
        UserDTO dto = new UserDTO();
        BeanUtils.populate(dto, map);
        LOGGER.debug("Map --> Bean : {}", dto);
    }

}
