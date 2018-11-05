package com.ssh.sys.web.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.ssh.common.dto.ModelMapDTO;
import com.ssh.sys.api.service.UserService;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("prototype")
public class UserExportAction extends ActionSupport implements ModelDriven<ModelMapDTO> {

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "freemarkerConfiguration")
    private Configuration freemarkerConfiguration;

    private ModelMapDTO model = new ModelMapDTO();

    private InputStream inputStream;
    private String fileName;

    @Override
    public String execute() throws Exception {
        List<Map> list = userService.getList(model);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("list", list);
        fileName = "FreeMarker Export Excel";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 解决在非中文系统环境下的乱码问题
        outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        freemarkerConfiguration.getTemplate("csv.ftl").process(dataModel, new OutputStreamWriter(outputStream));
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        fileName = new String(fileName.getBytes(), "ISO8859-1");
        return SUCCESS;
    }

    /*======== Generate Getters and Setters ========*/

    @Override
    public ModelMapDTO getModel() {
        return model;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getFileName() {
        return fileName;
    }

}
