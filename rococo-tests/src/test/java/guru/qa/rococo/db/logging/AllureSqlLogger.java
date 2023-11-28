package guru.qa.rococo.db.logging;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class AllureSqlLogger extends StdoutLogger {

    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();
    private final AttachmentRenderer<AttachmentData> attachmentRenderer = new FreemarkerAttachmentRenderer("sql-query.ftl");

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared, String sql, String url) {
        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);
        if (isNotEmpty(sql) && isNotEmpty(prepared)) {
            SqlAttachment sqlAttachment = new SqlAttachment("SQL query", sql, prepared);
            attachmentProcessor.addAttachment(sqlAttachment, attachmentRenderer);
        }
    }
}
