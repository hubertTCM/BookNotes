package com.hubert.YiAn;

import java.util.*;


public class PrescriptionClusterCompositeNode {
	
	public int childCount(){
		return mChildCompositeNodes.size();
	}
	
	public void add(PrescriptionClusterCompositeNode child){
		mChildCompositeNodes.add(child);
	}
	
	public void add(PrescriptionClusterLeafNode node){
		mLeafNodes.add(node);
	}
	
	public List<PrescriptionClusterLeafNode>  getLeafNodes(){
		return mLeafNodes;
	}
	
	private List<PrescriptionClusterLeafNode> mLeafNodes = new ArrayList<PrescriptionClusterLeafNode>();

	private List<PrescriptionClusterCompositeNode> mChildCompositeNodes =
			new ArrayList<PrescriptionClusterCompositeNode>();
}
