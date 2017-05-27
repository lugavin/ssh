package com.ssh.common.core.xml;

public interface Mapper {

    // String PUBLIC_ID = "-//hibernate.org//DTD Mapper 1.0//EN";

    String SYSTEM_ID_PREFIX = "http://hibernate.org/dtd/";

    // String DTD_FILE = "mapper-1.0.dtd";

    String ROOT_ELEMENT = "mapper";

    String HQL_ELEMENT = "hql";

    String SQL_ELEMENT = "sql";

    String ID_ATTRIBUTE = "id";

    String NAMESPACE_ATTRIBUTE = "namespace";

}
