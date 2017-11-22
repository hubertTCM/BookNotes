package com.hubert.machinelearning;

import java.util.*;
import javafx.util.*;

// http://www.cnblogs.com/pinard/p/6307064.html
public class FPTree<T> {
    public FPTree(Collection<Collection<T>> source, int lowerLimit) {
        mSource = source;
        mLowerLimit = lowerLimit;
        build();
    }

    public List<Pair<Integer, List<T>>> getAll() {
        List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
//        for (HeadNode head : mHeads) {
//            List<Pair<Integer, List<T>>> pair = head.get(mLowerLimit);
//            result.addAll(pair);
//        }
        return result;
    }

    private void build() {
        for (Collection<T> temp : mSource) {
            buildHead(temp);
        }
        adjustHeads();

        for (Collection<T> item : mSource) {
            updateTree(item);
        }
    }

    protected void sort(ArrayList<T> temp) {
        temp.sort(new Comparator<T>() {

            @Override
            public int compare(T o1, T o2) {
                HeadNode h1 = findHead(o1);
                HeadNode h2 = findHead(o2);
                if (h1.getCount() > h2.getCount()) {
                    return 1;
                }
                if (h1.getCount() < h2.getCount()) {
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
            if (head.getCount() < mLowerLimit) {
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
                int diff = o1.getCount() - o2.getCount();
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

    private void buildHead(Collection<T> source) {
        for (T data : source) {
            HeadNode head = null;
            for (HeadNode temp : mHeads) {
                if (temp.getValue() == data) {
                    head = temp;
                    head.increaseCount();
                    break;
                }
            }
            if (head == null) {
                head = new HeadNode(data, 1);
                mHeads.add(head);
            }
        }
    }

    private void updateTree(Collection<T> source) {
        ArrayList<T> temp = new ArrayList<T>();
        temp.addAll(source);
        sort(temp);

        TreeNode currentNode = mRoot;
        for (T data : temp) {
            HeadNode head = findHead(data);
            if (head == null) {
                break;
            }
            TreeNode child = currentNode.findChild(data);
            if (child == null) {
                child = currentNode.addChild(data);
                head.insert(child);
            } else {
                child.increaseCount();
            }
            currentNode = child;
        }

    }

    private class NodeBase {
        public NodeBase(T value, int count) {
            mValue = value;
            mCount = count;
        }

        public T getValue() {
            return mValue;
        }

        public int getCount() {
            return mCount;
        }

        public void increaseCount() {
            mCount += 1;
        }

        protected int mCount;
        protected T mValue;
    }

    private class TreeNode extends NodeBase {
        public TreeNode(T value) {
            super(value, 1);
        }

//        public TreeNode getParent() {
//            return mParent;
//        }

//        public Pair<Integer, List<T>> getAllItems() {
//            List<T> temp = new ArrayList<T>();
//            temp.add(mValue);
//            TreeNode node = mParent;
//            while (node.getParent() != null) {
//                temp.add(node.getValue());
//                node = node.getParent();
//            }
//            return new Pair<>(getCount(), temp);
//        }

        public TreeNode findChild(T value) {
            for (TreeNode child : mChildren) {
                if (child.getValue() == value) {
                    return child;
                }
            }
            return null;
        }

        public TreeNode addChild(T value) {
            TreeNode node = new TreeNode(value);
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

//        public Pair<Integer, List<T>> getAllItems() {
//            return mValue.getAllItems();
//        }

        public int getCount() {
            return mValue.getCount();
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

//        public List<Pair<Integer, List<T>>> get(int lowerLimit) {
//            ListNode node = mNode;
//            List<Pair<Integer, List<T>>> result  = new ArrayList<Pair<Integer, List<T>>>();
//            while (node != null) {
//                Pair<Integer, List<T>> singlePath = node.getAllItems();
//                result.add(singlePath);
//                node = node.getNext();
//            }
//            return result;
//        }

        public void insert(TreeNode node) {
            if (mNode == null) {
                mNode = new ListNode(node);
                return;
            }
            ListNode current = mNode;
            while (true) {
                ListNode next = current.getNext();
                if (next == null || next.getCount() < node.getCount()) {
                    current.insertAfter(node);
                    break;
                }
                current = current.getNext();
            }
        }

        ListNode mNode;
    }

    private List<HeadNode> mHeads = new Vector<HeadNode>();
    private TreeNode mRoot = new TreeNode(null);
    private Collection<Collection<T>> mSource;
    private int mLowerLimit = 0;
}
