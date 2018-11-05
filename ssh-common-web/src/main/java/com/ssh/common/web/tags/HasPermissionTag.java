package com.ssh.common.web.tags;

import com.ssh.common.subject.ActiveUser;
import com.ssh.common.util.Constant;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class HasPermissionTag extends TagSupport {

    private String code;

    @Override
    public int doStartTag() throws JspException {
        return onDoStartTag();
    }

    public int onDoStartTag() throws JspException {
        String permissionCode = getCode();
        boolean show = showTagBody(permissionCode);
        if (show) {
            return TagSupport.EVAL_BODY_INCLUDE;
        } else {
            return TagSupport.SKIP_BODY;
        }
    }

    protected boolean showTagBody(String code) {
        return isPermitted(code);
    }

    protected boolean isPermitted(String code) {
        ActiveUser subject = getSubject();
        return subject != null && subject.isPermitted(code);
    }

    protected ActiveUser getSubject() {
        return (ActiveUser) pageContext.getSession().getAttribute(Constant.USER_SESSION_ATTRIBUTE);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
