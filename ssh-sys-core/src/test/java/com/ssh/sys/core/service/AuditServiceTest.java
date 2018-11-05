package com.ssh.sys.core.service;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.util.Constant;
import com.ssh.sys.api.service.AuditService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@ActiveProfiles(Constant.ENV_DEV)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring-core-context.xml",
        "classpath:sys-core-context.xml"
})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditServiceTest {

    @Inject
    private AuditService auditService;

    @Before
    public void setUp() throws Exception {
        Assert.assertNotNull(auditService);
    }

    @Test
    public void test() throws Exception {
        System.err.println(DateUtils.parseDate("2016-07-30 22:00:00", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"));
        System.err.println(DateUtils.truncate(Calendar.getInstance(), Calendar.DATE).getTime());
    }

    @Test
    public void test1GetList() throws Exception {
        ModelMapDTO dto = new ModelMapDTO();
        dto.put("actionStartTime", "2016-07-30");
        dto.put("actionEndTime", "2016-07-30");
        List<Map> list = auditService.getList(dto);
        System.out.println(list.size());
    }

    @Test
    public void test2GetList() throws Exception {
        List<Map> list = auditService.getDetailList(1L);
        System.out.println(list.size());
    }

}
