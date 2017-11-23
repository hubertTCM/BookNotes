package com.hubert.machinelearning;

import java.util.*;
import javafx.util.*;

// http://www.cnblogs.com/pinard/p/6307064.html
public class FPTree<T> {
    public FPTree(Collection<Collection<T>> source, int lowerLimit) {
        mLowerLimit = lowerLimit;
        build(source);
    }
    public FPTree(List<Pair<Integer, List<T>>> source, int lowerLimit, List<T> postfix){
        mLowerLimit = lowerLimit;
        mPostfix.addAll(postfix);
        build(source);
    }

    public List<Pair<Integer, List<T>>> getAll() {
        List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
        for (HeadNode head : mHeads) {
            List<Pair<Integer, List<T>>> pair = get(head);
            result.addAll(pair);
        }
        return result;
    }
    
    private List<Pair<Integer, List<T>>> get(HeadNode head){
        List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
        
        List<T> postfix = new ArrayList<T>();
        postfix.add(head.getValue());
        postfix.addAll(mPostfix);
        
        if (head.isSignlePath()){
            if (postfix.size() > 1){
                result.add(new Pair<Integer, List<T>>(head.getFrequency(), postfix));
            }
            
            for(Pair<Integer, List<T>> prefixPath : head.getAllPrefixPath()){
                List<T> path = new ArrayList<T>();
                path.addAll(postfix);
                for(int i = 0; i < prefixPath.getValue().size(); i++){
                    path.addAll(prefixPath.getValue().subList(i,  prefixPath.getValue().size()));
                    result.add(new Pair<Integer, List<T>>(prefixPath.getKey(), path));
                }
            }
        }
        else{
            for(Pair<Integer, List<T>> prefixPath : head.getAllPrefixPath()){
                List<T> path = new ArrayList<T>();
                path.addAll(prefixPath.getValue());
                Pair<Integer, List<T>> pair = new Pair<Integer, List<T>>(prefixPath.getKey(), path);
                result.add(pair);
            }
            
            if (!result.isEmpty()){
                
                FPTree<T> conditionalTree = new FPTree<T>(result, mLowerLimit, postfix);
                List<Pair<Integer, List<T>>> conditionalResult = conditionalTree.getAll();
                for(Pair<Integer, List<T>> pair : conditionalResult){
                    pair.getValue().addAll(postfix);
                }
                result.addAll(conditionalResult);
            }
            else{
              if (postfix.size() > 1){
                  result.add(new Pair<Integer, List<T>>(head.getFrequency(), postfix));
              }
            }
        }
        
        return result;
    }

    private void build(List<Pair<Integer, List<T>>> source){
        for (Pair<Integer, List<T>> temp : source) {
            buildHead(temp.getValue(), temp.getKey());
        }
        adjustHeads();

        for (Pair<Integer, List<T>> temp : source) {
            updateTree(temp.getValue(), temp.getKey());
        }
    }
    
    private void build(Collection<Collection<T>> source) {
        for (Collection<T> temp : source) {
            buildHead(temp, 1);
        }
        adjustHeads();

        for (Collection<T> item : source) {
            updateTree(item, 1);
        }
    }

    protected void sort(ArrayList<T> temp) {
        temp.sort(new Comparator<T>() {

            @Override
            public int compare(T o1, T o2) {
                HeadNode h1 = findHead(o1);
                HeadNode h2 = findHead(o2);
                if (h1.getFrequency() > h2.getFrequency()) {
                    return 1;
                }
                if (h1.getFrequency() < h2.getFrequency()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    private void adjustHeads() {
        sortHeads();
        for (int i = mHeads.size() - 1; i >= 0; --i) {
            HeadNode head = mHeads.get(i);
            if (head.getFrequency() < mLowerLimit) {
                mHeads.remove(i);
                continue;
            }
            break;
        }
    }

    protected void sortHeads() {
        mHeads.sort(new Comparator<HeadNode>() {
            @Override
            public int compare(FPTree<T>.HeadNode o1, FPTree<T>.HeadNode o2) {
                int diff = o1.getFrequency() - o2.getFrequency();
                if (diff > 0) {
                    return 1;
                }
                if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        });
    }

    private HeadNode findHead(T data) {
        for (HeadNode temp : mHeads) {
            if (data.equals(temp.getValue())) {
                return temp;
            }
        }
        return null;
    }

    private void buildHead(Collection<T> source, int frequency) {
        for (T data : source) {
            HeadNode head = null;
            for (HeadNode temp : mHeads) {
                if (temp.getValue() == data) {
                    head = temp;
                    head.increaseFrequency(frequency);
                    break;
                }
            }
            if (head == null) {
                head = new HeadNode(data, frequency);
                mHeads.add(head);
            }
        }
    }

    private void updateTree(Collection<T> source, int frequency) {
        ArrayList<T> temp = new ArrayList<T>();
        temp.addAll(source);
        sort(temp);

        TreeNode currentNode = mRoot;
        for (T data : temp) {
            HeadNode head = findHead(data);
            if (head == null) {
                break;
            }
            mRoot.increaseFrequency(frequency);
            TreeNode child = currentNode.findChild(data);
            if (child == null) {
                child = currentNode.addChild(data, frequency);
                head.insert(child);
            } else {
                child.increaseFrequency(frequency);
            }
            currentNode = child;
        }

    }

    private class NodeBase {
        public NodeBase(T value, int frequency) {
            mValue = value;
            mFrequency = frequency;
        }

        public T getValue() {
            return mValue;
        }

        public int getFrequency() {
            return mFrequency;
        }

        public void increaseFrequency(int frequency) {
            mFrequency += frequency;
        }

        protected int mFrequency;
        protected T mValue;
    }

    private class TreeNode extends NodeBase {
        public TreeNode(T value, int frequency) {
            super(value, frequency);
        }

        public TreeNode getParent() {
            return mParent;
        }
        
        public List<T> getPrefixPath(){
            List<T> path = new ArrayList<T>();
            TreeNode node = mParent;
            while(node.mParent !=  null){
                path.add(node.getValue());
                node = node.mParent;
            }
            return path;
        }

        public TreeNode findChild(T value) {
            for (TreeNode child : mChildren) {
                if (child.getValue() == value) {
                    return child;
                }
            }
            return null;
        }

        public TreeNode addChild(T value, int frequency) {
            TreeNode node = new TreeNode(value, frequency);
            node.mParent = this;
            this.mChildren.add(node);
            return node;
        }

        private TreeNode mParent;
        private List<TreeNode> mChildren = new ArrayList<TreeNode>();
    }

    private class ListNode {
        public ListNode(TreeNode node) {
            mNext = null;
            mValue = node;
        }

        public ListNode getNext() {
            return mNext;
        }

        public boolean isSignlePath(){
            if (mNext != null) {
                return false;
            }
            TreeNode current = mValue;
            
            while(current.getParent()!= null){
                if (current.mChildren.size() > 0){
                    return false;
                }
                current = current.getParent();
            }
            return true;
        }

        public Pair<Integer, List<T>> getPrefixPath() {
            List<T> path = mValue.getPrefixPath();
            if (path.isEmpty()){
                return null;
            }

            return new Pair<Integer, List<T>>(mValue.getFrequency(), path);
        }

        public int getCount() {
            return mValue.getFrequency();
        }

        public ListNode insertAfter(TreeNode value) {
            ListNode node = new ListNode(value);
            node.mNext = mNext.mNext;
            mNext.mNext = node;
            return node;
        }

        TreeNode mValue;
        ListNode mNext;
    }

    private class HeadNode extends NodeBase {
        public HeadNode(T value, int count) {
            super(value, count);
            mNode = null;
        }
        
        public boolean isSignlePath(){
            if (mNode == null){
                return true;
            }
            return mNode.isSignlePath();
        }
        
        public List<Pair<Integer, List<T>>> getAllPrefixPath(){
            List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
            ListNode node = mNode;
            while(node != null){
                Pair<Integer, List<T>> prefixPath = node.getPrefixPath();
                if (prefixPath != null) {
                    result.add(prefixPath);
                }
                node = node.getNext();
            }
            return result;
        }

        public void insert(TreeNode node) {
            if (mNode == null) {
                mNode = new ListNode(node);
                return;
            }
            ListNode current = mNode;
            while (true) {
                ListNode next = current.getNext();
                if (next == null || next.getCount() < node.getFrequency()) {
                    current.insertAfter(node);
                    break;
                }
                current = current.getNext();
            }
        }

        ListNode mNode;
    }

    private List<HeadNode> mHeads = new Vector<HeadNode>();
    private TreeNode mRoot = new TreeNode(null, 0);
    private int mLowerLimit = 0;
    private List<T> mPostfix = new Vector<T>();
}
