package com.tocea.corolla.revisions.services

import org.javers.core.Javers;
import org.javers.core.diff.Diff
import org.junit.Rule;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User

import spock.lang.Specification;

import com.tocea.corolla.requirements.exceptions.*;
import com.tocea.corolla.revisions.domain.Commit
import com.tocea.corolla.revisions.services.IRevisionService
import com.tocea.corolla.test.utils.FunctionalDocRule
import com.tocea.corolla.utils.functests.FunctionalTestDoc
import fr.echoes.lab.ksf.users.security.api.ICurrentUserService

class JaversRevisionServiceTest extends Specification {

	@Rule
	def FunctionalDocRule rule	= new FunctionalDocRule()
	def Javers javers = Mock(Javers)
	def IRevisionService revisionService
	def ICurrentUserService currentUserService = Mock(ICurrentUserService)
	
	class DomainObject {
		def String id;
	}
	
	def setup() {
		revisionService = new JaversRevisionService(
				javers: javers,
				currentUserService: currentUserService
		)
	}
	
	def "it should call Javers commit method"() {
		
		given:
			def object = new DomainObject(id: "1")
		
		when:
			revisionService.commit(object)
			
		then:
			currentUserService.getCurrentUserLogin() >> "somebody"
	
		then:
			notThrown(Exception.class)
			1 * javers.commit(_, object)

	}
	
	def "it should retrieve the history of an object"() {
		
		given:
			def object = new DomainObject(id: "1")
		
		when:
			revisionService.getHistory(object.id, object.getClass())
	
		then:
			notThrown(Exception.class)
			1 * javers.findSnapshots(_)
		
	}
	
	def "it should compare two versions of the same object"() {
		
		given:
			def old = new DomainObject(id: "1")
			def current = new DomainObject(id: "1")
			def Diff diff = Mock(Diff)
		
		when:
			revisionService.compare(old, current)
	
		then:
			notThrown(Exception.class)
			javers.compare(old, current) >> diff
		
	}
	
	def "it should retrieve a commit in the history from its ID"() {
		
		given:
			def objectID = "1"
			def objectClass = DomainObject.class
			def commitID = "2"
			def history = [new Commit(id: "3"), new Commit(id: "2"), new Commit(id: "1")]
					
		and:
			def JaversRevisionService service = Spy()
					
		when:
			def commit = service.findCommitByID(objectID, objectClass, commitID)
					
		then:			
			service.getHistory(objectID, objectClass) >> history
			
		then:
			commit == history[1]
			notThrown(Exception.class)
		
	}
	
	def "it should return the previous commit from a commit ID"() {
		
		given:
			def objectID = "1"
			def objectClass = DomainObject.class
			def commitID = "2"
			def history = [new Commit(id: "3"), new Commit(id: "2"), new Commit(id: "1")]
					
		and:
			def JaversRevisionService service = Spy()
					
		when:
			def commit = service.getPreviousCommit(objectID, objectClass, commitID)
					
		then:		
			service.getHistory(objectID, objectClass) >> history
			
		then:
			commit == history[2]
			notThrown(Exception.class)
					
	}
	
	def "it should find a Javers CdoSnapshot instance by commit data"() {
		
		given:
			def commit = new Commit(id: "1", objectID: "1", objectClass: DomainObject.class)
			
		when:
			revisionService.findCdoSnapshotByCommit(commit)
			
		then:
			1 * javers.findSnapshots(_)
			notThrown(Exception.class)
	}
	
}