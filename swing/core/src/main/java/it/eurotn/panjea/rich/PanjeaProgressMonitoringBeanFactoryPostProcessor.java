/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package it.eurotn.panjea.rich;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.richclient.application.ProgressMonitoringBeanFactoryPostProcessor;
import org.springframework.richclient.progress.ProgressMonitor;
import org.springframework.util.Assert;

public class PanjeaProgressMonitoringBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private class ProgressMonitoringBeanPostProcessor implements BeanPostProcessor {
        private final ConfigurableBeanFactory beanFactory;

        private ProgressMonitoringBeanPostProcessor(ConfigurableBeanFactory paramConfigurableBeanFactory) {
            Assert.notNull(paramConfigurableBeanFactory, "The bean factory cannot be null");
            this.beanFactory = paramConfigurableBeanFactory;
        }

        private String getLoadingBeanMessage(String beanName) {
            String defaultMessage = "Loading " + beanName + " ...";

            if (PanjeaProgressMonitoringBeanFactoryPostProcessor.this.messageSource == null) {
                return defaultMessage;
            }

            Object[] args = { beanName };

            return PanjeaProgressMonitoringBeanFactoryPostProcessor.this.messageSource
                    .getMessage("progress.loading.bean", args, defaultMessage, null);
        }

        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (PanjeaProgressMonitoringBeanFactoryPostProcessor.logger.isTraceEnabled()) {
                PanjeaProgressMonitoringBeanFactoryPostProcessor.logger
                        .trace("BEGIN: postProcessBeforeInitialization(" + beanName + ")");
            }

            if (this.beanFactory.containsLocalBean(beanName)) {
                String loadingBeanMessage = getLoadingBeanMessage(beanName);
                PanjeaProgressMonitoringBeanFactoryPostProcessor.this.progressMonitor
                        .subTaskStarted(loadingBeanMessage);
                PanjeaProgressMonitoringBeanFactoryPostProcessor.this.progressMonitor.worked(1);
            }
            PanjeaProgressMonitoringBeanFactoryPostProcessor.logger.trace("END: postProcessBeforeInitialization()");
            return bean;
        }
    }

    public static final String LOADING_APP_CONTEXT_KEY = "progress.loading.applicationContext";
    public static final String LOADING_BEAN_KEY = "progress.loading.bean";
    private static final Log logger = LogFactory.getLog(ProgressMonitoringBeanFactoryPostProcessor.class);
    private final ProgressMonitor progressMonitor;
    private final MessageSource messageSource;

    private final String loadingAppContextMessage;

    public PanjeaProgressMonitoringBeanFactoryPostProcessor(ProgressMonitor progressMonitor,
            MessageSource messageSource) {
        Assert.notNull(progressMonitor, "The ProgressMonitor cannot be null");

        this.progressMonitor = progressMonitor;
        this.messageSource = messageSource;
        this.loadingAppContextMessage = getLoadingAppContextMessage();
    }

    private String getLoadingAppContextMessage() {
        String defaultMessage = "Loading Application Context ...";

        if (this.messageSource == null) {
            return defaultMessage;
        }

        return this.messageSource.getMessage("progress.loading.applicationContext", null, defaultMessage, null);
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory == null) {
            return;
        }

        String[] beanNames = beanFactory.getBeanDefinitionNames();
        int singletonBeanCount = 0;

        for (int i = 0; i < beanNames.length; ++i) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanNames[i]);

            if (beanDefinition.isSingleton()) {
                ++singletonBeanCount;
            }

        }

        this.progressMonitor.taskStarted(this.loadingAppContextMessage, singletonBeanCount);

        beanFactory.addBeanPostProcessor(new ProgressMonitoringBeanPostProcessor(beanFactory));
    }
}