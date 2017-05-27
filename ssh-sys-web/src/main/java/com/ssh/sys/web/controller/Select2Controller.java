package com.ssh.sys.web.controller;

import com.ssh.common.dto.ModelMapDTO;
import com.ssh.common.page.Page;
import com.ssh.sys.api.service.Select2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/select2")
public class Select2Controller {

    @Autowired
    private Select2Service select2Service;

    @ResponseBody
    @RequestMapping(value = "/getActorPage", method = RequestMethod.POST)
    public Page<Map> getActorPage(@RequestParam Map<String, String> params,
                                  @RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer limit) {
        return select2Service.getActorPage(new ModelMapDTO(params), (page - 1) * limit, limit);
    }

    @ResponseBody
    @RequestMapping(value = "/getFuncPage", method = RequestMethod.POST)
    public Page<Map> getFuncPage(@RequestParam Map<String, String> params,
                                 @RequestParam(defaultValue = "1") Integer page,
                                 @RequestParam(defaultValue = "10") Integer limit) {
        return select2Service.getFuncPage(new ModelMapDTO(params), (page - 1) * limit, limit);
    }

    @ResponseBody
    @RequestMapping(value = "/getRoleList", method = RequestMethod.POST)
    public List<Map> getRoleList(@RequestParam Map<String, String> params) {
        return select2Service.getRoleList(new ModelMapDTO(params));
    }

}
