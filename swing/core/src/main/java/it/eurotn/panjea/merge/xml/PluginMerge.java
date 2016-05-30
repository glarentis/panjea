package it.eurotn.panjea.merge.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class PluginMerge {

    public void merge() {

        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        Resource[] resoourceToMerge = null;
        ;
        try {
            resoourceToMerge = resolver.getResources("classpath*:/META-INF/**/*-context.xml");
        } catch (IOException e) {
            // logger.error("-->errore nel caricare le risorse panjea*-context", e);
            throw new RuntimeException("-->errore nel caricare le risorse *-context", e);
        }
        Map<String, List<Resource>> resources = new HashMap<>();
        for (Resource resource : resoourceToMerge) {
            List<Resource> sameResource = ObjectUtils.defaultIfNull(resources.get(resource.getFilename()),
                    new ArrayList<Resource>());
            sameResource.add(resource);
            resources.put(resource.getFilename(), sameResource);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        long time = Calendar.getInstance().getTimeInMillis();

        try {
            if (!(new File("spring-beans.dtd").exists())) {
                InputStream source = PluginMerge.class.getResource("/META-INF/core/spring-beans.dtd").openStream();
                Files.copy(source, new File("spring-beans.dtd").toPath());
            }
        } catch (Exception e) {
            // logger.error("errore durante la copia del file dtd", e);
            throw new RuntimeException("-->errore durante la copia del file dtd", e);
        }

        for (Entry<String, List<Resource>> resourceEntry : resources.entrySet()) {
            ModuleMergeWork work = new ModuleMergeWork(resourceEntry.getValue(), resourceEntry.getKey());
            executor.execute(work);
        }
        executor.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executor.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        System.out.println("TEMPO TOTALE " + (Calendar.getInstance().getTimeInMillis() - time));
    }

}