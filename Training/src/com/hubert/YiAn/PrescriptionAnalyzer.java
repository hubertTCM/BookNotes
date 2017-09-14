package com.hubert.YiAn;

import java.util.*;

import com.hubert.dal.entity.*;
import com.hubert.training.*;

public class PrescriptionAnalyzer {
	public PrescriptionAnalyzer(List<YiAnPrescriptionEntity> source) {
		for (YiAnPrescriptionEntity entity : source) {
			PrescriptionClusterLeafNode leaf = new PrescriptionClusterLeafNode(entity);
			PrescriptionClusterCompositeNode node = new PrescriptionClusterCompositeNode();
			node.add(leaf);
			mCompositeNodes.add(node);
		}
	}

	public PrescriptionClusterCompositeNode analyze(IDistanceCalculator<PrescriptionClusterCompositeNode> calculator) {
		return mRoot;
	}

	private List<PrescriptionClusterCompositeNode> mCompositeNodes = new ArrayList<PrescriptionClusterCompositeNode>();
	private PrescriptionClusterCompositeNode mRoot = null;
}
