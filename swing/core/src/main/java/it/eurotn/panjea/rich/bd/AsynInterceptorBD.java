package it.eurotn.panjea.rich.bd;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.swing.SwingUtilities;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import foxtrot.ConcurrentWorker;
import foxtrot.Task;
import foxtrot.Worker;
import it.eurotn.panjea.exceptions.PanjeaRuntimeException;
import it.eurotn.panjea.utils.PanjeaSwingUtil;
import it.eurotn.security.JecPrincipal;

/**
 * Intercetta i metodi sul business delegate per chiamarli in un thread esterno all' edt tramite foxIt.
 *
 * @author giangi
 * @version 1.0, 23/ott/06
 */
public class AsynInterceptorBD implements MethodInterceptor {

    private class TaskInvoker extends Task {

        private final MethodInvocation invocation;
        private String stackTrace;

        /**
         * Costruttore.
         *
         * @param invocation
         *            metodo da invocare
         * @param stackTrace
         *            strack trace della choamata sul thread awt
         */
        public TaskInvoker(final MethodInvocation invocation, final String stackTrace) {
            super();
            this.invocation = invocation;
            this.stackTrace = stackTrace;
        }

        @Override
        public Object run() throws Exception {
            Object result = null;
            long initialTime = 0;
            initialTime = System.currentTimeMillis();
            try {
                result = invocation.proceed();
            } catch (Exception e) {
                throw e;
            } catch (Throwable e) {
                throw new PanjeaRuntimeException(e);
            }
            saveLog(invocation, initialTime, System.currentTimeMillis(), stackTrace);
            return result;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AsynInterceptorBD.class);

    @Override
    public Object invoke(MethodInvocation method) throws Throwable {
        String stackTrace = Thread.currentThread().getStackTrace()[6].toString();
        if (SwingUtilities.isEventDispatchThread()) {
            LOGGER.debug("--> Chiamo il metodo sul thread di Fox");
            Task taskInvoker = new TaskInvoker(method, stackTrace);
            if (method.getMethod().isAnnotationPresent(AsyncMethodInvocation.class)) {
                return ConcurrentWorker.post(taskInvoker);
            } else {
                return Worker.post(taskInvoker);
            }
        } else {
            long startTime = System.currentTimeMillis();
            Object result = method.proceed();
            saveLog(method, startTime, System.currentTimeMillis(), stackTrace);
            return result;
        }
    }

    /**
     * salva il log in un file.
     *
     * @param method
     *            metodo chiamato
     * @param startTime
     *            startTime
     * @param endTime
     *            endTime
     * @param stackTrace
     *            stackTrace della chiamata
     */
    private void saveLog(MethodInvocation method, long startTime, long endTime, final String stackTrace) {
        Path writeFile = null;
        try {
            writeFile = PanjeaSwingUtil.getHome().resolve("log.txt");
        } catch (Exception ex) {
            // Se ho un errore nel creare il file di log non devo fermarmi. Loggo ed esco, riproverò
            // dopo
            LOGGER.error("-->errore nel creare il file log.txt", ex);
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(writeFile, Charset.defaultCharset(),
                new OpenOption[] { StandardOpenOption.APPEND, StandardOpenOption.CREATE })) {
            JecPrincipal principal = PanjeaSwingUtil.getUtenteCorrente();
            StringBuilder sb = new StringBuilder(2200);
            if (principal != null) {
                sb.append(principal.getCodiceAzienda()).append(",");
                sb.append(principal.getUserName()).append(",");
            } else {
                sb.append(",,");
            }
            sb.append(method.getMethod().getDeclaringClass().getName()).append(",");
            sb.append(method.getMethod().getName()).append(",");
            sb.append(startTime).append(",");
            sb.append(endTime).append(",");
            sb.append(stackTrace.replace(",", "|"));
            for (Object parameter : method.getArguments()) {
                sb.append(" # ").append(parameter);
            }
            writer.append(sb.toString());
            writer.newLine();
            writer.flush();
        } catch (Exception exception) {
            LOGGER.error("-->errore nel scrivere nel file di log", exception);
        }
    }
}