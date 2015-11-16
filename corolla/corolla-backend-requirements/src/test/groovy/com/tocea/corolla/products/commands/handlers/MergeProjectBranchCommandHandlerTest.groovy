package com.tocea.corolla.products.commands.handlers

import org.junit.Rule

import spock.lang.Specification

import com.tocea.corolla.cqrs.gate.Gate
import com.tocea.corolla.products.commands.MergeProjectBranchCommand
import com.tocea.corolla.products.dao.IProjectBranchDAO
import com.tocea.corolla.products.domain.ProjectBranch
import com.tocea.corolla.products.exceptions.MissingProjectBranchInformationException
import com.tocea.corolla.requirements.commands.CreateRequirementCommand
import com.tocea.corolla.requirements.commands.EditRequirementCommand
import com.tocea.corolla.requirements.commands.handlers.MergeProjectBranchCommandHandler
import com.tocea.corolla.requirements.dao.IRequirementDAO
import com.tocea.corolla.requirements.domain.Requirement
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO
import com.tocea.corolla.requirements.trees.domain.RequirementNode
import com.tocea.corolla.requirements.trees.domain.RequirementsTree
import com.tocea.corolla.requirements.trees.exceptions.RequirementsTreeNotFoundException
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.trees.domain.FolderNode
import com.tocea.corolla.utils.functests.FunctionalTestDoc

@FunctionalTestDoc(requirementName = "MERGE_PROJECT_BRANCH")
class MergeProjectBranchCommandHandlerTest extends Specification {
	
	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def IProjectBranchDAO branchDAO = Mock(IProjectBranchDAO)
	def IRequirementsTreeDAO requirementsTreeDAO = Mock(IRequirementsTreeDAO)
	def IRequirementDAO requirementDAO = Mock(IRequirementDAO)
	def Gate gate = Mock(Gate)
	def MergeProjectBranchCommandHandler handler
	def IRevisionService revisionService = Mock(IRevisionService)
	
	def setup() {
		handler = new MergeProjectBranchCommandHandler(
				requirementsTreeDAO : requirementsTreeDAO,
				requirementDAO : requirementDAO,
				gate : gate
				)
	}
	
	def "it should import new requirements created in the origin branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		def originRequirements = [new Requirement(id: "1", key: "R1", projectBranchId: "2")]
		def branchRequirements = []
		def originRequirementsTree = new RequirementsTree(nodes: [])
		def branchRequirementsTree = new RequirementsTree(nodes: [])
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		1 * requirementDAO.findByProjectBranchId(origin.id) >> originRequirements
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		1 * requirementsTreeDAO.findByBranchId(origin.id) >> originRequirementsTree
		1 * requirementsTreeDAO.findByBranchId(branch.id) >> branchRequirementsTree
		
		then:
		1 * gate.dispatchAsync {
			it instanceof CreateRequirementCommand && !it.requirement.id  && it.requirement.key == originRequirements[0].key
		}
		
		then:
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		
	}
	
	def "it should import changes done to the requirements in the origin branch"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		def originRequirements = [new Requirement(id: "1", key: "R1", projectBranchId: "2", name: "Not so awesome req")]
		def branchRequirements = [new Requirement(id: "2", key: "R1", projectBranchId: "1", name: "Awesome req")]
		def originRequirementsTree = new RequirementsTree(nodes: [])
		def branchRequirementsTree = new RequirementsTree(nodes: [])
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		1 * requirementDAO.findByProjectBranchId(origin.id) >> originRequirements
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		1 * requirementsTreeDAO.findByBranchId(origin.id) >> originRequirementsTree
		1 * requirementsTreeDAO.findByBranchId(branch.id) >> branchRequirementsTree
		
		then:
		1 * gate.dispatchAsync {
			it instanceof EditRequirementCommand && it.requirement.id == branchRequirements[0].id && it.requirement.projectBranchId == branch.id  && it.requirement.name == originRequirements[0].name
		}
		
		then:
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		
	}
	
	def "it should merge requirements trees"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		def originRequirements = [new Requirement(id: "1", key: "R1", projectBranchId: "2")]
		def branchRequirements = []
		def originRequirementsTree = new RequirementsTree(
				nodes: [
					new FolderNode(
					id: 1,
					text: "TEST",
					nodes:[
						new RequirementNode(id: 2, requirementId: "1", nodes:[])
					]
					)
					
				]
				)
		def branchRequirementsTree = new RequirementsTree(nodes: [])
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		1 * requirementDAO.findByProjectBranchId(origin.id) >> originRequirements
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		1 * requirementsTreeDAO.findByBranchId(origin.id) >> originRequirementsTree
		1 * requirementsTreeDAO.findByBranchId(branch.id) >> branchRequirementsTree
		
		then:
		1 * requirementDAO.findByProjectBranchId(branch.id) >> originRequirements
		1 * requirementsTreeDAO.save(branchRequirementsTree)
		
		then:
		branchRequirementsTree.nodes.size() == 1
		branchRequirementsTree.nodes[0].nodes.size() == 1
		
	}
	
	def "it should not insert nodes attached to deleted requirements in the requirements tree"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		def originRequirements = []
		def branchRequirements = []
		def originRequirementsTree = new RequirementsTree(
				nodes: [
					new FolderNode(
					id: 1,
					text: "TEST",
					nodes:[
						new RequirementNode(id: 2, requirementId: "1", nodes:[])
					]
					)
					
				]
				)
		def branchRequirementsTree = new RequirementsTree(nodes: [])
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		notThrown(Exception.class)
		
		then:
		1 * requirementDAO.findByProjectBranchId(origin.id) >> originRequirements
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		1 * requirementsTreeDAO.findByBranchId(origin.id) >> originRequirementsTree
		1 * requirementsTreeDAO.findByBranchId(branch.id) >> branchRequirementsTree
		
		then:
		1 * requirementDAO.findByProjectBranchId(branch.id) >> branchRequirements
		1 * requirementsTreeDAO.save(branchRequirementsTree)
		
		then:
		branchRequirementsTree.nodes.size() == 1
		branchRequirementsTree.nodes[0].nodes.size() == 0
		
	}
	
	def "it should throw an exception if the origin branch is missing"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = null
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		thrown(MissingProjectBranchInformationException.class)
		
	}
	
	def "it should throw an exception if the destination branch is missing"() {
		
		given:
		def branch = null
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		thrown(MissingProjectBranchInformationException.class)
		
	}
	
	def "it should throw an exception if the origin requirements tree cannot be found"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		def originRequirements = []
		def branchRequirements = []
		def originRequirementsTree = null
		def branchRequirementsTree = new RequirementsTree(nodes: [])
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		thrown(RequirementsTreeNotFoundException.class)
		
	}
	
	def "it should throw an exception if the destination requirements tree cannot be found"() {
		
		given:
		def branch = new ProjectBranch(id: "1", name:"Master", projectId: "35")
		def origin = new ProjectBranch(id: "2", name:"Dev", projectId: "35")
		def originRequirements = []
		def branchRequirements = []
		def originRequirementsTree = new RequirementsTree(nodes: [])
		def branchRequirementsTree = null
		
		when:
		handler.handle new MergeProjectBranchCommand(origin, branch)
		
		then:
		1 * requirementsTreeDAO.findByBranchId(origin.id) >> originRequirementsTree
		thrown(RequirementsTreeNotFoundException.class)
		
	}
	
}