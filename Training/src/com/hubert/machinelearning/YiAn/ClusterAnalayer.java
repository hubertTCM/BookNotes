package com.hubert.machinelearning.YiAn;

import java.util.*;

public class ClusterAnalayer {
	public List<PrescriptionClusterCompositeNode> getNodes(PrescriptionClusterCompositeNode root, int count) {

		PriorityQueue<PrescriptionClusterCompositeNode> priorityQueue = new PriorityQueue<PrescriptionClusterCompositeNode>(
				new Comparator<PrescriptionClusterCompositeNode>() {

					@Override
					public int compare(PrescriptionClusterCompositeNode x, PrescriptionClusterCompositeNode y) {
						if (x.getId() == y.getId()) {
							return 0;
						}
						if (x.getId() < y.getId()) {
							return 1;
						}
						return -1;
					}
				});

		priorityQueue.add(root);
		while (priorityQueue.size() < count) {
			PrescriptionClusterCompositeNode node = priorityQueue.remove();
			for (PrescriptionClusterCompositeNode child : node.getCompositeNodes()) {
				priorityQueue.add(child);
			}
		}

		List<PrescriptionClusterCompositeNode> nodes = new ArrayList<PrescriptionClusterCompositeNode>();
		while (!priorityQueue.isEmpty()) {
			nodes.add(priorityQueue.remove());
		}
		return nodes;
	}

}
