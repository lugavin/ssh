package com.ssh.sys.web.controller;

import com.ssh.common.dto.MapDTO;
import com.ssh.common.page.Page;
import com.ssh.common.util.Constant;
import com.ssh.common.web.base.ResponseData;
import com.ssh.common.web.datatable.DataTableRequest;
import com.ssh.common.web.datatable.DataTableResponse;
import com.ssh.common.web.datatable.DataTableUtility;
import com.ssh.common.web.base.BaseWrapper;
import com.ssh.sys.api.dto.UserDTO;
import com.ssh.sys.api.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * (1)@RequestParam("name") = request.getParameter("name")
 * (2)@RequestAttribute("name") = request.getAttribute("name")
 * (3)@SessionAttribute("user") = session.getAttribute("user")
 * (4)@ModelAttribute
 * ①绑定方法的参数
 * A.从Model中获取
 * 如在以下方法中, 方法add(@ModelAttribute("name") String name)的参数来源于方法populateModel(String name, Model model)的model属性.
 * public @ModelAttribute void populateModel(String name, Model model) { model.addAttribute("name", name); }
 * public ResponseData add(@ModelAttribute("name") String name) {}
 * B.从Form表单或URL参数中获取(实际上不用此注解也能获取到对应参数的值)
 * 如以下两种写法是等价的:
 * public ResponseData addSubmit(@ModelAttribute UserDTO userDTO) {}
 * public ResponseData addSubmit(UserDTO userDTO) {}
 * ②绑定方法的返回值(在Controller每个方法执行前执行, 对于一个Controller映射多个URL时要谨慎使用)
 * 如以下两种写法是等价的:
 * public @ModelAttribute void populateModel(String name, Model model) { model.addAttribute("name", name); }
 * public @ModelAttribute("name") String populateModel(String name) { return name; }
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    @RequiresPermissions("user:query")
    public String list() {
        return "user/list";
    }

    @ResponseBody
    @RequiresPermissions("user:query")
    @RequestMapping(value = "/getList", method = {RequestMethod.POST})
    public DataTableResponse<Map> getList(@RequestBody BaseWrapper<MapDTO> wrapper) {
        DataTableRequest dtArgs = wrapper.getDtArgs();
        Page<Map> page = userService.getPage(wrapper.getDto(), dtArgs.getStart(), dtArgs.getLength());
        return DataTableUtility.buildDataTable(dtArgs, page);
    }

    @RequiresPermissions("user:create")
    @RequestMapping(value = "/add", method = {RequestMethod.GET})
    public String add() {
        return "user/add";
    }

    @ResponseBody
    @RequiresPermissions("user:create")
    @RequestMapping(value = "/addSubmit", method = RequestMethod.POST)
    public ResponseData addSubmit(UserDTO userDTO) {
        userService.add(userDTO);
        return ResponseData.newInstance();
    }

    @RequiresPermissions("user:update")
    @RequestMapping(value = "/edit", method = {RequestMethod.GET})
    public ModelAndView edit(@RequestParam Long id) {
        // 将数据填充到Request域并返回指定的逻辑视图
        return new ModelAndView("user/edit", "user", userService.getById(id));
    }

    @ResponseBody
    @RequiresPermissions("user:update")
    @RequestMapping(value = "/editSubmit", method = {RequestMethod.POST})
    public ResponseData editSubmit(UserDTO userDTO) {
        userService.update(userDTO);
        return ResponseData.newInstance();
    }

    @ResponseBody
    @RequiresPermissions("user:delete")
    @RequestMapping(value = "/deleteSubmit", method = {RequestMethod.POST})
    public ResponseData deleteSubmit(@RequestParam Long[] ids) {
        userService.delete(ids);
        return new ResponseData();
    }

    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    public void export(@ModelAttribute UserDTO userDTO, HttpServletResponse response) throws Exception {
        List<UserDTO> list = userService.getList(userDTO);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(response.getOutputStream());
            response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;fileName=export.csv");
            // 解决在非中文系统环境下的乱码问题
            writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            writer.write("用戶编号,用户帐号,用户名,是否锁定\r\n");
            StringBuilder buf = new StringBuilder();
            for (UserDTO dto : list) {
                buf.append(dto.getId()).append(",")
                        .append(dto.getCode()).append(",")
                        .append(dto.getName()).append(",")
                        .append(dto.getStatus()).append("\r\n");
            }
            writer.write(buf.toString());
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    // @RequestMapping(value = "/exportCSV", method = {RequestMethod.POST})
    // public void exportCSV(UserDTO userDTO, HttpServletResponse response) throws Exception {
    //     List<UserDTO> list = userService.getList(userDTO);
    //     Map<String, Object> dataModel = new HashMap<>();
    //     dataModel.put("list", list);
    //     OutputStreamWriter writer = null;
    //     try {
    //         writer = new OutputStreamWriter(response.getOutputStream());
    //         response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
    //         response.setHeader("Content-Disposition", "attachment;fileName=export.csv");
    //         // 解决在非中文系统环境下的乱码问题
    //         writer.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
    //         freemarkerConfiguration.getTemplate("csv.ftl").process(dataModel, writer);
    //         writer.flush();
    //     } finally {
    //         if (writer != null) {
    //             writer.close();
    //         }
    //     }
    // }

    @ResponseBody
    @RequestMapping("/checkCode")
    public ModelMap checkCode(@RequestParam String code) {
        return new ModelMap(Constant.REMOTE_VALIDATION_KEY, userService.getByCode(code) == null);
    }

    @ResponseBody
    @RequestMapping("/checkPass")
    public ModelMap checkPass(@RequestParam String oldPass) {
        return new ModelMap(Constant.REMOTE_VALIDATION_KEY, userService.checkPass(oldPass));
    }

    @ResponseBody
    @RequestMapping("/changePass")
    public ResponseData changePass(@RequestParam String oldPass, @RequestParam String newPass) {
        userService.changePass(oldPass, newPass);
        return ResponseData.newInstance();
    }

    @ResponseBody
    @RequestMapping("/resetPass")
    public ResponseData resetPass(@RequestParam Long[] ids) {
        userService.resetPass(ids);
        return ResponseData.newInstance();
    }

    @ResponseBody
    @RequestMapping(value = "/authSubmit", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData authSubmit(@RequestParam Long userId, @RequestParam Long[] roleIds) {
        userService.assignRoles(userId, roleIds);
        return ResponseData.newInstance();
    }

}
