package com.hubert.machinelearning.YiAn;

import java.util.*;

public class ClusterSplitterVisitor {

	public void split(PrescriptionClusterCompositeNode node) {
		// int totalNodes = node.getLeafNodes().size();

		List<PrescriptionClusterCompositeNode> clusters = new ArrayList<PrescriptionClusterCompositeNode>();

		split(node, clusters);

	}

	private void split(PrescriptionClusterCompositeNode node, List<PrescriptionClusterCompositeNode> clusters) {
		for (PrescriptionClusterCompositeNode child : node.getCompositeNodes()) {
			System.out.println(child.getLeafNodes().size());
		}

		for (PrescriptionClusterCompositeNode child : node.getCompositeNodes()) {
			split(child, clusters);
		}
	}

}
