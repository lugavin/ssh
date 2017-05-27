package com.ssh.common.core.xml;

import java.util.Map;

public interface MapperBuilder extends Mapper {

    void init() throws Exception;

    Map<String, String> getHqlMap();

    Map<String, String> getSqlMap();

}
