package com.ssh.common.web.base;

import com.ssh.common.enums.StatusCode;

public abstract class AbstractController {

    public static final String FORWARD_PREFIX = "forward:";
    public static final String REDIRECT_PREFIX = "redirect:";

    // @InitBinder
    // public void initBinder(WebDataBinder binder, WebRequest request) {
    //     binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(Constant.DATETIME_FORMAT), Boolean.TRUE));
    // }

    // @RequestMapping(value = "/{root}/{page}", method = {RequestMethod.GET})
    // protected String toPage(@PathVariable String root, @PathVariable String page) throws Exception {
    //     return root + "/" + page;
    // }

    protected ResponseData setData(Object data) {
        return setData(true, Integer.toString(StatusCode.SUCCESS.getCode()), StatusCode.SUCCESS.getReasonPhrase(), data);
    }

    protected ResponseData setData(boolean success, String code, String message) {
        return setData(success, code, message, null);
    }

    protected ResponseData setData(boolean success, String code, String message, Object data) {
        return ResponseData.newInstance()
                .setSuccess(success)
                .setCode(code)
                .setMessage(message)
                .setData(data);
    }

    protected String forward(String view) {
        return FORWARD_PREFIX + view;
    }

    protected String redirect(String view) {
        return REDIRECT_PREFIX + view;
    }

}
