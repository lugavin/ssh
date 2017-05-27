package com.ssh.common.core.repository.template;

import freemarker.template.Template;

/**
 * 用来保存查询语句类型(HQL/SQL)和查询语句模板
 */
public class StatementTemplate {

    private final Type type;

    private final Template template;

    public StatementTemplate(Type type, Template template) {
        this.type = type;
        this.template = template;
    }

    public Type getType() {
        return type;
    }

    public Template getTemplate() {
        return template;
    }

    public enum Type {
        HQL, SQL
    }

}
