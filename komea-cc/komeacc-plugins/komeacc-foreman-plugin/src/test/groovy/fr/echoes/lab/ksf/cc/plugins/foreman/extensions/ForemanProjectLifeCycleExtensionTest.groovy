package fr.echoes.lab.ksf.cc.plugins.foreman.extensions

import spock.lang.Specification

import com.tocea.corolla.products.dao.IProjectDAO
import com.tocea.corolla.products.domain.Project

import fr.echoes.labs.foremanapi.IForemanApi
import fr.echoes.labs.foremanclient.IForemanService
import fr.echoes.labs.ksf.cc.plugins.foreman.dao.IForemanTargetDAO
import fr.echoes.labs.ksf.cc.plugins.foreman.extensions.ForemanProjectLifeCycleExtension
import fr.echoes.labs.ksf.cc.plugins.foreman.model.ForemanTarget
import fr.echoes.labs.ksf.cc.plugins.foreman.services.ForemanClientFactory
import fr.echoes.labs.ksf.extensions.projects.ProjectDto
import fr.echoes.labs.ksf.users.security.api.ICurrentUserService

class ForemanProjectLifeCycleExtensionTest extends Specification {

	ForemanProjectLifeCycleExtension projectLifeCycleExtension
	ForemanClientFactory foremanClientFactory = Mock(ForemanClientFactory)
	IForemanApi foremanApi = Mock(IForemanApi)
	IForemanService foremanService = Mock(IForemanService)
	ICurrentUserService currentUserService = Mock(ICurrentUserService)
	IForemanTargetDAO targetDAO = Mock(IForemanTargetDAO)
	IProjectDAO projectDAO = Mock(IProjectDAO)

	def setup() {

		projectLifeCycleExtension = new ForemanProjectLifeCycleExtension(
			foremanClientFactory: foremanClientFactory,
			foremanService : foremanService,
			currentUserService : currentUserService,
			targetDAO : targetDAO,
			projectDAO : projectDAO
		)

	}

	def "it should call the Foreman service when a project is created"() {

		given:
			def project = new ProjectDto()
			project.key = "TestProjectKey"
			project.name = "TestProjectName"

		and:
			def username = "smb"

		when:
			projectLifeCycleExtension.notifyCreatedProject(project)

		then:
			currentUserService.getCurrentUserLogin() >> username

		then:
			foremanClientFactory.createForemanClient() >> foremanApi

		then:
			1 * foremanService.createProject(foremanApi, project.name, username)

		then:
			notThrown Exception.class

	}

	def "it should not try to call the Foreman service when the current user cannot be authenticated"() {

		given:
			def project = new ProjectDto()

		and:
			def username = null

		when:
			projectLifeCycleExtension.notifyCreatedProject(project)

		then:
			currentUserService.getCurrentUserLogin() >> username

		then:
			0 * foremanService.createProject(_, _, _)

		then:
			notThrown Exception.class

	}

	def "it should delete all the associated targets when a project is deleted"() {

		given:
			def project = new Project()
			def projectDTO = new ProjectDto(id: "1")

		and:
			def targets = [Mock(ForemanTarget), Mock(ForemanTarget)]

		when:
			projectLifeCycleExtension.notifyDeletedProject(projectDTO)

		then:
			1 * projectDAO.findOne(projectDTO.id) >> project

		then:
			1 * targetDAO.findByProject(project) >> targets

		then:
			1 * targetDAO.delete(targets)

		then:
			notThrown Exception.class

	}

	def "it should call the Foreman service when a project is deleted"() {

		given:
			def project = new Project()
			def projectDTO = new ProjectDto(
				id: "1",
				name: "something"
			)

		when:
			projectLifeCycleExtension.notifyDeletedProject(projectDTO)

		then:
			foremanClientFactory.createForemanClient() >> foremanApi

		then:
			1 * foremanService.deleteProject(foremanApi, projectDTO.name)

		then:
			notThrown Exception.class

	}

}
