package org.openrepose.filters.urinormalization;

import org.openrepose.core.filter.FilterConfigHelper;
import org.openrepose.core.filter.logic.impl.FilterLogicHandlerDelegate;
import org.openrepose.core.services.config.ConfigurationService;
import org.openrepose.core.services.context.ServletContextHelper;
import org.openrepose.core.services.reporting.metrics.MetricsService;
import org.openrepose.filters.urinormalization.config.UriNormalizationConfig;
import org.slf4j.Logger;

import javax.servlet.*;
import java.io.IOException;
import java.net.URL;

public class UriNormalizationFilter implements Filter {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UriNormalizationFilter.class);
    private static final String DEFAULT_CONFIG = "uri-normalization.cfg.xml";
    private String config;
    private UriNormalizationHandlerFactory handlerFactory;
    private ConfigurationService configurationManager;
    private MetricsService metricsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        new FilterLogicHandlerDelegate(request, response, chain).doFilter(handlerFactory.newHandler());
    }

    @Override
    public void destroy() {
        configurationManager.unsubscribeFrom(config, handlerFactory);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = new FilterConfigHelper(filterConfig).getFilterConfig(DEFAULT_CONFIG);
        LOG.info("Initializing filter using config " + config);
        configurationManager = ServletContextHelper.getInstance(filterConfig.getServletContext()).getPowerApiContext().configurationService();
        metricsService = ServletContextHelper.getInstance(filterConfig.getServletContext()).getPowerApiContext()
                .metricsService();
        handlerFactory = new UriNormalizationHandlerFactory(metricsService);
        URL xsdURL = getClass().getResource("/META-INF/schema/config/uri-normalization-configuration.xsd");
        configurationManager.subscribeTo(filterConfig.getFilterName(),config,xsdURL, handlerFactory, UriNormalizationConfig.class);
    }
}
