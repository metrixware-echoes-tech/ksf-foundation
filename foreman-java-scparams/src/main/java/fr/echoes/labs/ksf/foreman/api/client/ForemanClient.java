package fr.echoes.labs.ksf.foreman.api.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ws.rs.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.echoes.labs.ksf.foreman.api.dto.OverrideValueDto;
import fr.echoes.labs.ksf.foreman.api.dto.SmartClassParameterDto;
import fr.echoes.labs.ksf.foreman.api.model.ForemanApiResult;
import fr.echoes.labs.ksf.foreman.api.model.ForemanApiResultMap;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHost;
import fr.echoes.labs.ksf.foreman.api.model.ForemanHostGroup;
import fr.echoes.labs.ksf.foreman.api.model.PuppetClass;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameter;
import fr.echoes.labs.ksf.foreman.api.model.SmartClassParameterOverrideValue;
import fr.echoes.labs.ksf.foreman.api.utils.ForemanEntities;
import fr.echoes.labs.ksf.foreman.api.utils.PageAggregator;
import fr.echoes.labs.ksf.foreman.api.utils.PuppetClassUtils;
import fr.echoes.labs.ksf.foundation.utils.URLUtils;

public class ForemanClient extends AbstractApiClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(ForemanClient.class);
	
	private static final String API_BASE_PATH = "/api";
	private static final String API_HOSTS = "/hosts";
	private static final String API_HOSTS_GROUP = "/hostgroups";
	private static final String API_SMART_CLASS_PARAMETERS = "/smart_class_parameters";
	private static final String API_PUPPET_CLASSES = "/puppetclasses";
	
	private static final String PARAM_PAGE = "page";
	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_PER_PAGE = "per_page";
	
	private static final String CONTENT_TYPE = "application/json";
	private static final Integer NB_ITEMS_PER_PAGE = 100;
	
	private final String apiUrl;
	private final ObjectMapper mapper;
	
	private Map<String, Integer> nbRequests = Maps.newHashMap();
	
	public ForemanClient(final String url, final String username, final String password) throws MalformedURLException {
		this(new URL(url), username, password);
	}
	
	public ForemanClient(final URL url, final String username, final String password) {
		
		super(url.getProtocol(), url.getPort(), url.getHost(), username, password);
		
		this.apiUrl = URLUtils.addPath(url.toString(), API_BASE_PATH);
		
		this.mapper = new ObjectMapper();
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		this.nbRequests.put(HttpMethod.GET, 0);
		this.nbRequests.put(HttpMethod.POST, 0);
		this.nbRequests.put(HttpMethod.PUT, 0);
		
	}
	
	public Map<String, Integer> getNbRequests() {
		return this.nbRequests;
	}
	
	private void incNbRequest(final String method) {		
		nbRequests.put(method, nbRequests.get(method)+1);
	}
	
	protected String get(final String path, final Map<String, String> params) throws IOException {
		
		this.incNbRequest(HttpMethod.GET);
		final String query = URLUtils.buildQuery(params);
		return super.get(this.apiUrl+path+'?'+query, CONTENT_TYPE);
	}
	
	protected String get(final String path) throws IOException {
		
		this.incNbRequest(HttpMethod.GET);
		return super.get(this.apiUrl+path, CONTENT_TYPE);
	}
	
	protected void put(final String path, final Object entity) throws IOException {
		
		this.incNbRequest(HttpMethod.PUT);
		super.put(this.apiUrl+path, CONTENT_TYPE, this.mapper.writeValueAsString(entity));
	}
	
	protected void post(final String path, final Object entity) throws IOException {
		
		this.incNbRequest(HttpMethod.POST);
		super.post(this.apiUrl+path, CONTENT_TYPE, this.mapper.writeValueAsString(entity));
	}
	
	public ForemanHost getHost(final String targetHost) throws IOException {
		
		final String response = get(API_HOSTS+"/"+targetHost);
		
		return this.mapper.readValue(response, ForemanHost.class);
	}
	
	public List<ForemanHost> getHosts() throws IOException {
		
		final PageAggregator<ForemanHost> aggregator = new PageAggregator<ForemanHost>() {
			@Override
			public List<ForemanHost> executeRequest(int page) throws IOException {
				final String response = get(API_HOSTS, ImmutableMap.of(PARAM_PAGE, Integer.toString(page)));
				return extractResults(response, ForemanHost.class);
			}
		};
		
		return aggregator.execute();
	}
	
	public List<ForemanHostGroup> getHostGroups() throws IOException {
		
		final PageAggregator<ForemanHostGroup> aggregator = new PageAggregator<ForemanHostGroup>() {
			@Override
			public List<ForemanHostGroup> executeRequest(int page) throws IOException {
				final String response = get(API_HOSTS_GROUP, ImmutableMap.of(PARAM_PAGE, Integer.toString(page)));
				return extractResults(response, ForemanHostGroup.class);
			}
		};
		
		return aggregator.execute();
	}
	
	public ForemanHostGroup getHostGroup(final String hostGroupName) throws IOException {
		
		final String searchValue = ForemanEntities.removeParentName(hostGroupName);

		final String response = get(API_HOSTS_GROUP, ImmutableMap.of(PARAM_SEARCH, searchValue));	
		final List<ForemanHostGroup> hostGroups = extractResults(response, ForemanHostGroup.class);
		
		for (final ForemanHostGroup hostGroup : hostGroups) {
			if (searchValue.equals(hostGroup.getName())) {
				return hostGroup;
			}
		}
		
		return null;
	}
	
	public SmartClassParameter getSmartClassParameter(final Integer id) throws IOException {
		
		final String response = get(API_SMART_CLASS_PARAMETERS+"/"+id);
		
		return this.mapper.readValue(response, SmartClassParameter.class);
	}
	
	public List<SmartClassParameter> getSmartClassParameters(int page) throws IOException {
		
		final String response = get(API_SMART_CLASS_PARAMETERS, ImmutableMap.of(
			PARAM_PER_PAGE, Integer.toString(NB_ITEMS_PER_PAGE),
			PARAM_PAGE, Integer.toString(page)
		));
		
		return extractResults(response, SmartClassParameter.class);
	}
	
	public List<SmartClassParameter> getSmartClassParameters() throws IOException {
		
		final PageAggregator<SmartClassParameter> aggregator = new PageAggregator<SmartClassParameter>() {
			public List<SmartClassParameter> executeRequest(int page) throws IOException {
				return Lists.newArrayList(getSmartClassParameters(page));
			}
		};

		return aggregator.execute();		
	}
	
	public List<SmartClassParameter> getSmartClassParametersWithOverrideValues(int page) throws IOException {
		
		final String response = get(API_SMART_CLASS_PARAMETERS, ImmutableMap.of(
				PARAM_SEARCH, "override",
				PARAM_PER_PAGE, Integer.toString(NB_ITEMS_PER_PAGE),
				PARAM_PAGE, Integer.toString(page)
		));
		
		return extractResults(response, SmartClassParameter.class);
	}
	
	public List<SmartClassParameter> getSmartClassParametersWithOverrideValues() throws IOException {
		
		final PageAggregator<SmartClassParameter> aggregator = new PageAggregator<SmartClassParameter>() {
			public List<SmartClassParameter> executeRequest(int page) throws IOException {
				return Lists.newArrayList(getSmartClassParametersWithOverrideValues(page));
			}
		};

		return aggregator.execute();		
	}
	
	public List<SmartClassParameter> getDetails(final List<SmartClassParameter> params) throws IOException {
		
		// collect all parameters ids and remove duplicated values
		final Set<Integer> parameterIds = Sets.newHashSet();
		for(final SmartClassParameter param : params) {
			parameterIds.add(param.getId());
		}
		
		final List<SmartClassParameter> results = Lists.newArrayList();
		
		for(final Integer id : parameterIds) {
			LOGGER.info("Retrieving smart class parameters [{}/{}]...", results.size()+1, parameterIds.size());;
			results.add(getSmartClassParameter(id));
		}
		
		return results;
	}
	
	public PuppetClass findPuppetClass(final String moduleName, final String className) throws IOException {
		
		final String response = get(API_PUPPET_CLASSES, ImmutableMap.of(PARAM_SEARCH, className));
		final ForemanApiResultMap results = this.mapper.readValue(response, ForemanApiResultMap.class);
		
		if (results != null && results.getResults().containsKey(moduleName)) {
			for (final Object obj : results.getResults().get(moduleName)) {
				final PuppetClass puppetClass = this.mapper.convertValue(obj, PuppetClass.class);
				if (className.equals(puppetClass.getName())) {
					return puppetClass;
				}
			}
		}
		
		return null;
	}
	
	public PuppetClass getPuppetClass(final Integer id) throws IOException {
		
		final String response = get(API_PUPPET_CLASSES+"/"+id);
		return this.mapper.readValue(response, PuppetClass.class);
	}
	
	public SmartClassParameter getSmartClassParameterByPuppetClass(final Integer puppetClassId, final String parameter) throws IOException {
		
		final String response = get(API_PUPPET_CLASSES+"/"+puppetClassId+API_SMART_CLASS_PARAMETERS, ImmutableMap.of(PARAM_SEARCH, '='+parameter));
		
		final List<SmartClassParameter> results = extractResults(response, SmartClassParameter.class);
		
		if (!results.isEmpty()) {
			return getSmartClassParameter(results.get(0).getId());
		}
		
		return null;
	}
	
	public void updateSmartClassParameter(final SmartClassParameter param) throws IOException {
		
		if (param.getId() == null) {
			throw new IllegalArgumentException("Cannot update a smart class parameter without an ID.");
		}
		
		LOGGER.info("Updating smart class parameter {}", param.getId());
		put(API_SMART_CLASS_PARAMETERS+"/"+param.getId(), new SmartClassParameterDto(param));
	}
	
	public void updateSmartClassParameterOverrideValue(final Integer scParamId, final SmartClassParameterOverrideValue value) throws IOException {
		
		if (scParamId == null) {
			throw new IllegalArgumentException("Cannot update a smart class parameter without an ID.");
		}
		
		if (value.getId() == null) {
			throw new IllegalArgumentException("Cannot update a smart class parameter override value without its ID.");
		}

		LOGGER.info("Updating override value {} for smart class parameter {}", value.getId(), scParamId);
		put(API_SMART_CLASS_PARAMETERS+"/"+scParamId+"/override_values/"+value.getId(), new OverrideValueDto(value));
	}
	
	public void createSmartClassParameterOverrideValue(final Integer scParamId, final SmartClassParameterOverrideValue value) throws IOException {
		
		if (scParamId == null) {
			throw new IllegalArgumentException("Cannot update a smart class parameter without an ID.");
		}
		
		LOGGER.info("Creating smart class parameter {}", scParamId);
		post(API_SMART_CLASS_PARAMETERS+"/"+scParamId+"/override_values", new OverrideValueDto(value));
	}
	
	public void addPuppetClassToHostGroup(final PuppetClass puppetClass, final ForemanHostGroup hostGroup) throws IOException {
		
		if (puppetClass.getId() == null) {
			throw new IllegalArgumentException("Cannot update a puppet class without its ID.");
		}
		
		final List<ForemanHostGroup> groups = Lists.newArrayList();
		if (puppetClass.getHostGroups() != null) {
			groups.addAll(puppetClass.getHostGroups());
		}
		groups.add(hostGroup);
		
		LOGGER.info("Updating puppet class {}...", puppetClass.getId());
		put(API_PUPPET_CLASSES+"/"+puppetClass.getId(), ImmutableMap.of("hostgroups", groups));
	}
	
	public void addPuppetClassToHost(final PuppetClass puppetClass, final ForemanHost host) throws IOException {
		
		if (puppetClass.getId() == null) {
			throw new IllegalArgumentException("Cannot add a Puppet Class to a host without a Puppet Class ID.");
		}
		
		if (host.getId() == null) {
			throw new IllegalArgumentException("Cannot add a Puppet Class to a host without the host ID.");
		}
		
		post(API_HOSTS+"/"+host.getId()+"/puppetclass_ids", ImmutableMap.of("puppetclass_id", Integer.toString(puppetClass.getId())));
	}
	
	public List<PuppetClass> getPuppetClassesOfHost(final ForemanHost host) throws IOException {
		
		if (host.getId() == null) {
			throw new IllegalArgumentException("Cannot retrieve puppet classes of an host without its ID.");
		}
		
		final String response = get(API_HOSTS+"/"+host.getId()+"/puppetclass_ids");
		
		final List<Integer> ids = extractResults(response, Integer.class);
		final List<PuppetClass> puppetClasses = Lists.newArrayList();
		
		for (final Integer id : ids) {
			final PuppetClass puppetClass = PuppetClassUtils.findPuppetClassById(host.getPuppetclasses(), id);
			if (puppetClass != null) {
				puppetClasses.add(puppetClass);
			}
		}
		
		return puppetClasses;
	}
	
	public List<PuppetClass> getPuppetClassesOfHostGroup(final ForemanHostGroup hostGroup) throws IOException {
		
		if (hostGroup.getId() == null) {
			throw new IllegalArgumentException("Cannot retrieve puppet classes of an host group without its ID.");
		}
		
		final String response = get(API_HOSTS_GROUP+"/"+hostGroup.getId()+"/puppetclasses");
		
		final List<PuppetClass> puppetClasses = Lists.newArrayList();
		final ForemanApiResultMap results = this.mapper.readValue(response, ForemanApiResultMap.class);
		
		for(final Entry<String, List<Object>> entry : results.getResults().entrySet()) {
			for (final Object obj : entry.getValue()) {
				final PuppetClass puppetClass = this.mapper.convertValue(obj, PuppetClass.class);
				puppetClass.setModuleName(entry.getKey());
				puppetClasses.add(puppetClass);
			}
		}
		
		return puppetClasses;
	}
	
	private List<Object> extractResults(final String response) throws IOException {
		
		final ForemanApiResult responseObject = this.mapper.readValue(response, ForemanApiResult.class);
		
		return responseObject.getResults();
	}
	
	private <T> List<T> extractResults(final String response, final Class<T> clazz) throws IOException {
	
		final List<Object> results = extractResults(response);
		
		final List<T> objects = Lists.newArrayList();
		
		for (final Object result : results) {
			objects.add(mapper.convertValue(result, clazz));
		}
		
		return objects;
	}
	
}
