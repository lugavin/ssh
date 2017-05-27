package com.ssh.common.core.repository.template;

import com.ssh.common.util.Constant;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @see org.apache.ibatis.scripting.xmltags.TrimSqlNode.FilteredDynamicContext#applyAll
 */
public class WhereDirective implements TemplateDirectiveModel {

    private static final String SPACE = Constant.SPACE;
    private static final String PREFIX = "WHERE";
    private static final List<String> PREFIX_LIST = Arrays.asList("AND ", "OR ", "AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        // Check if no parameters were given:
        if (!params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        // If there is non-empty nested content:
        if (body != null) {
            StringWriter out = null;
            try {
                // Executes the nested body. Same as <#nested> in FTL, except that we use our own writer instead of the current output writer.
                out = new StringWriter();
                // Renders the body of the directive body to the specified writer. The
                // writer is not flushed after the rendering. If you pass the environment's
                // writer, there is no need to flush it. If you supply your own writer, you
                // are responsible to flush/close it when you're done with using it (which
                // might be after multiple renderings).
                body.render(out);
                // Apply WHERE prefix
                StringBuilder sqlBuffer = new StringBuilder(out.toString().trim());
                String trimmedUppercaseSql = sqlBuffer.toString().toUpperCase(Locale.ENGLISH);
                if (trimmedUppercaseSql.length() > 0) {
                    for (String toRemove : PREFIX_LIST) {
                        if (trimmedUppercaseSql.startsWith(toRemove)) {
                            sqlBuffer.delete(0, toRemove.trim().length());
                            break;
                        }
                    }
                    // sqlBuffer.insert(0, SPACE);
                    sqlBuffer.insert(0, PREFIX);
                    sqlBuffer.insert(0, SPACE);
                    sqlBuffer.append(SPACE);
                }
                env.getOut().write(sqlBuffer.toString());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

}
