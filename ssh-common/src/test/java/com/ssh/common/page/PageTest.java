package com.ssh.common.page;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageTest {

    private Page<Map<String, Object>> page;

    @Before
    public void setUp() throws Exception {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("name", "test_" + i);
            list.add(map);
        }
        page = new PageImpl<>(0, 10, list, 25);
    }

    @Test
    public void testPage() throws Exception {
        Assert.isTrue(page.getPageNumber() == 0, "当前页码不正确");
        Assert.isTrue(page.getPageSize() == 10, "每页显示的记录数不正确");
        Assert.isTrue(page.getTotalPages() == 3, "总页数不正确");
        Assert.isTrue(page.getTotalRecords() == 25, "总记录数不正确");
        Assert.isTrue(page.getOffset() == 0, "当前页的偏移量不正确");
        Assert.isTrue(page.getNumberOfRecords() == 10, "当前页的记录数不正确");
        Assert.isTrue(page.hasContent(), "当前页数据为空");
        Assert.isTrue(page.isFirst(), "当前页不是第一页");
        Assert.isTrue(!page.hasPrevious(), "当前页存在上一页");
        Assert.isTrue(page.hasNext(), "当前页不存在下一页");
        Assert.isTrue(!page.isLast(), "当前页是最后一页");
    }

}
