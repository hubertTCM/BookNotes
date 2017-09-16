package com.hubert.machinelearning.YiAn;

import java.util.*;

public class PrescriptionClusterCompositeNode {

	public int childCount() {
		return mChildCompositeNodes.size();
	}

	public void add(PrescriptionClusterCompositeNode child) {
		mChildCompositeNodes.add(child);
	}

	public void add(PrescriptionClusterLeafNode node) {
		mLeafNodes.add(node);
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
}
