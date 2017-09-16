package com.hubert.machinelearning.YiAn;

import java.util.*;

public class PrescriptionClusterCompositeNode {
	public Set<String> getCenter() {
		if (mCenter == null) {
			initLeafNodes();
			List<Set<String>> allHerbs = new ArrayList<Set<String>>();
			for (PrescriptionClusterLeafNode leaf : mLeafNodes) {
				allHerbs.add(leaf.getHerbs());
			}
			CenterCalculator c = new CenterCalculator(allHerbs);
			mCenter = c.getCenter();

		}
		
		String s = "";
		for(String item : mCenter){
			s += " " + item;
		}
		System.out.println(s);

		return mCenter;
	}

	public void add(PrescriptionClusterCompositeNode child) {
		mChildCompositeNodes.add(child);
	}

	public void add(PrescriptionClusterLeafNode node) {
		mLeafNodes.add(node);
	}

	public List<PrescriptionClusterCompositeNode> getCompositeNodes() {
		return mChildCompositeNodes;
	}

	public List<PrescriptionClusterLeafNode> getLeafNodes() {
		initLeafNodes();
		return mLeafNodes;
	}

	private void initLeafNodes() {
		if (!mLeafNodes.isEmpty()) {
			return;
		}

		for (PrescriptionClusterCompositeNode node : mChildCompositeNodes) {
			mLeafNodes.addAll(node.getLeafNodes());
		}

	}

	private List<PrescriptionClusterLeafNode> mLeafNodes = new ArrayList<PrescriptionClusterLeafNode>();

	private List<PrescriptionClusterCompositeNode> mChildCompositeNodes = new ArrayList<PrescriptionClusterCompositeNode>();

	private Set<String> mCenter;

}
