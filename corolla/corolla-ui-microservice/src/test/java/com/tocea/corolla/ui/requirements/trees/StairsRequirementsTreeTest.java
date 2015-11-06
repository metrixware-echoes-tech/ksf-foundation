package com.tocea.corolla.ui.requirements.trees;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tocea.corolla.cqrs.gate.Gate;
import com.tocea.corolla.products.dao.IProjectBranchDAO;
import com.tocea.corolla.requirements.trees.dao.IRequirementsTreeDAO;
import com.tocea.corolla.requirements.trees.domain.RequirementNode;
import com.tocea.corolla.requirements.trees.domain.RequirementsTree;
import com.tocea.corolla.trees.domain.TreeNode;
import com.tocea.corolla.ui.AbstractSpringTest;

public class StairsRequirementsTreeTest extends AbstractSpringTest {

	@Autowired
	private IProjectBranchDAO branchDAO;
	
	@Autowired
	private IRequirementsTreeDAO requirementsTreeDAO;
	
	@Autowired
	private Gate gate;
	
	private static String treeID;
	
	@Before
	public void setUp() throws Exception {
		
//		Project project = new Project();
//		project.setKey("TEST");
//		project.setName("Test project");
//		
//		gate.dispatch(new CreateProjectCommand(project));
//		
//		ProjectBranch master = Lists.newArrayList(branchDAO.findByProjectId(project.getId())).get(0);		
//		
//		RequirementNode parentNode = null;
//		
//		for(int i=0; i<6000; i++) {
//			
//			Requirement requirement = new Requirement();
//			requirement.setKey("REQ"+i);
//			requirement.setName("REQ"+i);
//			requirement.setProjectBranchId(master.getId());
//			
//			gate.dispatch(new CreateRequirementCommand(requirement, parentNode != null ? parentNode.getId() : null));
//			
//			RequirementsTree masterTree = requirementsTreeDAO.findByBranchId(master.getId());
//			
//			parentNode = (RequirementNode) RequirementsTreeUtils.getNodeByRequirementId(requirement.getId(), masterTree.getNodes());
//			
//		}
//			
//		RequirementsTree tree = requirementsTreeDAO.findByBranchId(master.getId());
//		
//		Gson gson = new Gson();
//		System.out.println(gson.toJson(tree));
			
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("big-tree3.json");
		
		Gson gson = new Gson();
		RequirementsTree tree = gson.fromJson(new InputStreamReader(is), RequirementsTree.class);
		
		requirementsTreeDAO.save(tree);	
		
		assertNotNull(tree);
		/*assertEquals(6000, tree.getNodes().size());
		
		List<TreeNode> nodes = Lists.newArrayList(tree.getNodes());
		
		TreeNode parentNode = null;
		Integer maxChildren = 60;
		int i = maxChildren;
		
		for(TreeNode node : tree.getNodes()) {
			
			if (parentNode != null) {
				nodes.remove(node);
				parentNode.getNodes().add(node);				
			}
			i++;
			if (i >= maxChildren) {
				parentNode = node;
				i = 0;
			}			
			
		}
		
		tree.setNodes(nodes);
		System.out.println(gson.toJson(tree));*/
		
		treeID = tree.getId();
		
	}
	
	@Test
	//@UsingDataSet(locations = {"/big-tree.json"}, loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
	public void testFindTree() {	
		
		DateTime t0 = DateTime.now();
		
		RequirementsTree tree = requirementsTreeDAO.findOne(treeID);
		
		DateTime t1 = DateTime.now();
		
		assertNotNull(tree);
		
		long time = t1.getMillis() - t0.getMillis();
		System.out.println("[read] query time: "+time+" ms");
		
	}
	
	@Test
	public void testInsertNodeTree() {
		
		RequirementsTree tree = requirementsTreeDAO.findOne(treeID);
		
		DateTime t0 = DateTime.now();
		
		RequirementNode node = new RequirementNode("NR");
		tree.getNodes().add(node);
		
		requirementsTreeDAO.save(tree);
		
		DateTime t1 = DateTime.now();
		
		long time = t1.getMillis() - t0.getMillis();
		System.out.println("[insert] query time: "+time+" ms");
		
	}
	
	@Test
	public void testMoveNode() {
		
		RequirementsTree tree = requirementsTreeDAO.findOne(treeID);
		
		DateTime t0 = DateTime.now();
		
		List<TreeNode> nodes = Lists.newArrayList(tree.getNodes());
		
		List<TreeNode> children = Lists.newArrayList(nodes.get(0).getNodes());
		
		TreeNode node = children.get(0);
		children.remove(node);
		children.get(1).getNodes().add(node);
		
		nodes.get(0).setNodes(children);
		tree.setNodes(nodes);
		
		requirementsTreeDAO.save(tree);
		
		DateTime t1 = DateTime.now();
		
		long time = t1.getMillis() - t0.getMillis();
		System.out.println("[move] query time: "+time+" ms");
		
	}
	
	@Test
	public void testRemoveNode() {
		
		RequirementsTree tree = requirementsTreeDAO.findOne(treeID);
		
		DateTime t0 = DateTime.now();
		
		List<TreeNode> nodes = Lists.newArrayList(tree.getNodes());
		
		TreeNode node = Lists.newArrayList(nodes.get(0).getNodes()).get(3);
		nodes.get(0).getNodes().remove(node);
		
		requirementsTreeDAO.save(tree);
		
		DateTime t1 = DateTime.now();
		
		long time = t1.getMillis() - t0.getMillis();
		System.out.println("[remove] query time: "+time+" ms");
		
	}
	
}