package fr.echoes.labs.ksf.cc.plugins.dashboard.filters;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.komea.commons.model.Aggregation;
import org.komea.metrics.client.MetricsStorageClient;
import org.komea.metrics.model.Frequency;
import org.komea.metrics.model.Metric;
import org.komea.metrics.model.MetricType;
import org.komea.metrics.model.MissingValueStrategy;
import org.komea.metrics.model.ValueType;
import org.komea.organization.model.Entity;
import org.komea.organization.model.EntityType;
import org.komea.organization.storage.client.OrganizationStorageClient;
import org.komea.timeseries.client.TimeSerieStorageClient;
import org.komea.timeseries.model.TimeSerie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.collect.Lists;

import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardClientFactory;
import fr.echoes.labs.ksf.cc.plugins.dashboard.services.DashboardConfigurationService;

@Component
public class TimeOnSiteMetricFilter extends OncePerRequestFilter {

	public static final Logger LOGGER = LoggerFactory.getLogger(TimeOnSiteMetricFilter.class);

	public static final String TIME_REQUEST_PAGE = "timeRequestPage";
	public static final String URL_REQUEST_PAGE = "urlRequestPage";
	public static final String METRIC_KEY = "AverageTimeOnSite";
	public static final String METRIC_TYPE_KEY = "WebPerformance";
	public static final String ENTITY_TYPE_KEY = "webPage";
	
	@Autowired
	private DashboardClientFactory dashboardClientFactory;
	
	@Autowired
	private DashboardConfigurationService configurationService;
	
	public MetricType webPerformanceType = new MetricType()
		.setKey(METRIC_TYPE_KEY)
		.setName("Web Performance")
		.setDescription("Metrics for evaluating the performances of a web application");
	
	public Metric averageTimeOnSiteMetric = new Metric()
		.setKey(METRIC_KEY)
		.setName("Average Time On Site")
		.setDescription("Average Time On site")
		.setFrequency(Frequency.EVENT.toString())
		.setMissingValueStrategy(MissingValueStrategy.NULL_VALUE.toString())
		.setType(METRIC_TYPE_KEY)
		.setUnits("s")
		.setValueType(ValueType.TIME_SECONDS.toString())
		.setValueDirection(Metric.DIRECTION_NONE)
		.setAggregationName(Aggregation.AVG_VALUE.toString());
	
	public EntityType webPageType = new EntityType()
		.setKey(ENTITY_TYPE_KEY)
		.setName(ENTITY_TYPE_KEY)
		.setDescription(ENTITY_TYPE_KEY);
	
	@PostConstruct
	public void init() {
		
		LOGGER.info("Initializing TimeOnSiteMetricFilter...");
		
		if (configurationService.calculateAverageTimeOnSite()) {
		
			MetricsStorageClient storageClient = dashboardClientFactory.metricStorageClient();
			
			try {
				LOGGER.info("Creating metric type {}...", METRIC_TYPE_KEY);
				storageClient.saveMetricType(webPerformanceType);
			} catch (Exception ex) {
				LOGGER.error("Failed to create type {}.", METRIC_TYPE_KEY, ex);
			}
			
			try {
				LOGGER.info("Creating metric {}...", METRIC_KEY);
				storageClient.saveMetric(averageTimeOnSiteMetric);
			} catch (Exception ex) {
				LOGGER.error("Failed to create metric {}.", METRIC_KEY, ex);
			}
			
			try {
				LOGGER.info("Creating entity type {}", ENTITY_TYPE_KEY);
				OrganizationStorageClient organizationStorageClient = dashboardClientFactory.organizationStorageClient();
				organizationStorageClient.createEntityTypesIfNotExist(webPageType);
			} catch (Exception ex) {
				LOGGER.error("Failed to create entity type {}", ENTITY_TYPE_KEY, ex);
			}
			
		}
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String url = request.getRequestURI();
		
		if (url.startsWith("/ui") && configurationService.calculateAverageTimeOnSite()) {
		
			LOGGER.info("Intercepting request {}...", url);
			
			HttpSession session = request.getSession();
	
			Long previousTime = (Long) session.getAttribute(TIME_REQUEST_PAGE);
			String requestPage = (String) session.getAttribute(URL_REQUEST_PAGE);
			
			Long initTime = DateTime.now().getMillis();
			
			if (previousTime != null && !StringUtils.isEmpty(requestPage)) {
				
				Long duration = initTime - previousTime;
				
				pushPageEntity(requestPage);
				pushTimeSerie(duration, requestPage);

			}
					
			requestPage = url;
			
			session.setAttribute(TIME_REQUEST_PAGE, initTime);
			session.setAttribute(URL_REQUEST_PAGE, requestPage);
		
		}
		
		filterChain.doFilter(request, response);
		
	}
	
	private TimeSerie buildTimeSerie(Long duration, String requestPage) {
		
		final String tagKey = ENTITY_TYPE_KEY + "_key";
		
		return new TimeSerie(METRIC_KEY)
			.addMeasure(DateTime.now().toDate(), new Double(duration / 1000))
			.putTag(tagKey, requestPage);
		
	}
	
	@Async 
	private void pushPageEntity(final String requestPage) {
		
		Entity page = new Entity()
			.setKey(requestPage)
			.setName(requestPage)
			.setType(ENTITY_TYPE_KEY)
			.setDescription(requestPage+" [created by Komea Foundation]");
		
		try {
		
			LOGGER.info("Creating entity {} in Komea Dashboard...", page.getKey());
			
			OrganizationStorageClient client = dashboardClientFactory.organizationStorageClient();
			client.addOrUpdatePartialEntities(page);
			
		} catch(Exception ex) {
			LOGGER.error("Failed to create entity {} in Komea Dashboard.", page.getKey(), ex);
		}
		
	}
	
	@Async
	private void pushTimeSerie(final Long duration, String requestPage) {
		
		try {
			
			final TimeSerie timeSerie = buildTimeSerie(duration, requestPage);
			
			LOGGER.info("Sending metric {} to Komea Dashboard...", METRIC_KEY);
			
			TimeSerieStorageClient client = dashboardClientFactory.timeSerieStorageClient();
			client.pushTimeSeries(Lists.newArrayList(timeSerie));
			
		} catch(Exception ex) {
			LOGGER.error("Failed to send metrics to Komea Dashboard.", ex);
		}
		
	}
	
}
