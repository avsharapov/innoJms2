package ru.inno;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "browser", urlPatterns = {"/browser"}, loadOnStartup = 1)
public class BrowserServlet extends HttpServlet {
    private static final Logger     logger = LoggerFactory.getLogger(BrowserServlet.class);
    @Inject
    private              JMSContext context;

    @Resource(lookup = "java:/jms/queue/ExpiryQueue")
    private Queue exQueue;

    @Resource(lookup = "java:/jms/queue/INNOQueue")
    private Queue queue;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html;charcet=UTF-8");
        try (PrintWriter out = response.getWriter();
             QueueBrowser innoQueueBrowser = context.createBrowser(queue);
             QueueBrowser expiryQueueBrowser = context.createBrowser(exQueue)) {
            out.print("<html><body>");

            out.print("<h1>INNOQueue</h1>");
            printQueueMessage(out, innoQueueBrowser);

            out.print("<h1>ExpiryQueue</h1>");
            printQueueMessage(out, expiryQueueBrowser);

            out.print("</body></html>");
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void printQueueMessage(PrintWriter out, QueueBrowser queueBrowser) throws JMSException {
        Enumeration enumeration = queueBrowser.getEnumeration();
        out.print("<ul>");
        while (enumeration.hasMoreElements()) {
            out.print("<li>" + enumeration.nextElement() + "</li>");
        }
        out.print("</ul>");
    }
}
