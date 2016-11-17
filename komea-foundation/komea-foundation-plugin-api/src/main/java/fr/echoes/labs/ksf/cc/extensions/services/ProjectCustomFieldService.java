package fr.echoes.labs.ksf.cc.extensions.services;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tocea.corolla.products.domain.ProjectCustomField;

@Service
public class ProjectCustomFieldService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCustomField.class);
	
	private List<ProjectCustomField> customFields = Lists.newArrayList();
	
	public void register(final ProjectCustomField customField) {
		
		final String name = customField.getName();
		Validate.notBlank(name);
		Validate.isTrue(getCustomField(name) == null);
		
		LOGGER.info("Registering project custom field {}", customField);
		customFields.add(customField);
	}
	
	public List<ProjectCustomField> getCustomFields() {
		
		return ImmutableList.copyOf(customFields);
	}
	
	public ProjectCustomField getCustomField(final String name) {
		
		final Collection<ProjectCustomField> matched = Collections2.filter(this.customFields, new Predicate<ProjectCustomField>() {
			@Override
			public boolean apply(final ProjectCustomField field) {
				return field.getName().equals(name);
			}
		});
		
		return matched.isEmpty() ? null : matched.iterator().next();
	}
	
}
