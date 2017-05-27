package com.ssh.common.service;

import com.ssh.common.dto.DTO;
import com.ssh.common.validation.Groups;
import com.ssh.common.validation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

public interface BaseService<T extends DTO> {

    void add(@NotNull @Validated({Groups.Add.class}) T dto);

    void add(@Size(min = 1) @Validated({Groups.Add.class}) Collection<T> collection);

    void update(@NotNull @Validated({Groups.Update.class}) T dto);

    void delete(@NotNull Long id);

    void delete(@Size(min = 1) Long[] ids);

    T getById(@NotNull Long id);

}
