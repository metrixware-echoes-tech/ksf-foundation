package com.tocea.corolla.products.utils;

import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tocea.corolla.products.domain.Project;

import fr.echoes.labs.ksf.extensions.projects.ProjectDto;

public final class ProjectDtoFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDtoFactory.class);
	
	private ProjectDtoFactory() {
		// default constructor
	}

	public static ProjectDto convert(final Project project) {

        try {
            final ProjectDto projectDto = new ProjectDto();
            final BeanMap projectMap = new BeanMap(project);
            final BeanMap dtoMap = new BeanMap(projectDto);
            dtoMap.putAllWriteable(projectMap);
            return (ProjectDto) dtoMap.getBean();
        } catch (final Exception e) {
            LOGGER.error("Could not convert project to Project DTO", e);
        }
        return null;
		
	}
	
}
