package fr.echoes.labs.ksf.cc.plugins.dashboard.utils;

import java.text.Normalizer;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.komea.connectors.configuration.model.ConnectorProperty;
import org.komea.organization.model.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import fr.echoes.labs.ksf.cc.plugins.dashboard.entities.GitRepository;

public class DashboardUtils {

	public static String createIdentifier(String projectName) {
		return  Normalizer.normalize(projectName, Normalizer.Form.NFD).replaceAll("[^\\dA-Za-z\\-]", "").replaceAll("\\s+","-" ).toLowerCase();
	}
	
	public static Map<String, Set<String>> splitEntitiesByType(final List<Entity> entities) {
		
		Map<String, Set<String>> result = Maps.newHashMap();
		
		for(Entity entity : entities) {
			String type = entity.getType();
			if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(entity.getKey())) {
				Set<String> keys;
				if (result.containsKey(type)) {
					keys = result.get(type);
				}else{
					keys = Sets.newHashSet();
				}
				keys.add(entity.getKey());
				result.put(type, keys);
			}
		}
		
		return result;
	}
	
	public static List<GitRepository> findRepositoriesByRemoteURL(final Collection<GitRepository> repositories, final String remoteURL) {
		
		Collection<GitRepository> matched = Collections2.filter(repositories, new Predicate<GitRepository>() {
			@Override
			public boolean apply(GitRepository repo) {
				if (repo == null) {
					return false;
				}
				return remoteURL.equals(repo.getRemoteURL());
			}					
		});
		
		return Lists.newArrayList(matched);
	}
	
	public static List<GitRepository> extractGitRepositories(final ConnectorProperty property) {
		
		List<GitRepository> repositories = Lists.newArrayList();
		ObjectMapper mapper = new ObjectMapper();
		
		for (Object object : (List<Object>) property.getValue()) {	
			GitRepository repository = mapper.convertValue(object, GitRepository.class);
			repositories.add(repository);
		}
		
		return repositories;
	}
	
}
