package com.hubert.machinelearning;

import java.util.*;
import javafx.util.*;

// http://www.cnblogs.com/pinard/p/6307064.html
public class FPTree<T> {
    public List<Pair<Integer, Collection<T>>> build(Collection<Collection<T>> source) {
        // TODO: sort source
        for (Collection<T> temp : source) {
            buildHead(temp);
        }
        for (Collection<T> temp : source) {
            updateTree(temp);
        }

        mHeads.sort(new Comparator<HeadNode<T>>() {
            @Override
            public int compare(FPTree<T>.HeadNode<T> o1, FPTree<T>.HeadNode<T> o2) {
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

        List<Pair<Integer, Collection<T>>> result = new ArrayList<Pair<Integer, Collection<T>>>();
        return result;
    }

    private HeadNode<T> findHead(T data) {
        for (HeadNode<T> temp : mHeads) {
            if (temp.getValue() == data) {
                return temp;
            }
        }
        return null;
    }

    private void buildHead(Collection<T> source) {
        for (T data : source) {
            HeadNode<T> head = null;
            for (HeadNode<T> temp : mHeads) {
                if (temp.getValue() == data) {
                    head = temp;
                    head.increaseCount();
                    break;
                }
            }
            if (head == null) {
                head = new HeadNode<T>(data, 1);
                mHeads.add(head);
            }
        }
    }

    private void updateTree(Collection<T> source) {
        TreeNode<T> currentNode = mRoot;
        for (T data : source) {
            HeadNode<T> head = findHead(data);
            TreeNode<T> child = currentNode.findChild(data);
            if (child == null) {
                child = currentNode.addChild(data);
                head.insert(child);
            } else {
                child.increaseCount();
            }
            currentNode = child;
        }

    }

    private class NodeBase<T> {
        protected NodeBase(T value, int count) {
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

    private class TreeNode<T> extends NodeBase<T> {
        public TreeNode(T value) {
            super(value, 1);
        }

        public TreeNode<T> findChild(T value) {
            for (TreeNode<T> child : mChildren) {
                if (child.getValue() == value) {
                    return child;
                }
            }
            return null;
        }

        public TreeNode<T> addChild(T value) {
            TreeNode<T> node = new TreeNode<T>(value);
            node.mParent = this;
            this.mChildren.add(node);
            return node;
        }

        private TreeNode<T> mParent;
        private List<TreeNode<T>> mChildren = new ArrayList<TreeNode<T>>();
    }

    private class ListNode<T> {
        public ListNode(TreeNode<T> node) {
            mNext = null;
            mValue = node;
        }

        public ListNode<T> getNext() {
            return mNext;
        }

        public TreeNode<T> getValue() {
            return mValue;
        }

        public int getCount() {
            return mValue.getCount();
        }

        public ListNode<T> insertAfter(TreeNode<T> value) {
            ListNode<T> node = new ListNode<T>(value);
            node.mNext = mNext.mNext;
            mNext.mNext = node;
            return node;
        }

        TreeNode<T> mValue;
        ListNode<T> mNext;
    }

    private class HeadNode<T> extends NodeBase<T> {
        public HeadNode(T value, int count) {
            super(value, count);
            mNode = null;
        }

        public ListNode<T> getNode() {
            return mNode;
        }

        @Override
        public void increaseCount() {
            super.increaseCount();
            // TODO: ensure the list is sort by count desc
        }

        public void insert(TreeNode<T> node) {
            if (mNode == null) {
                mNode = new ListNode<T>(node);
                return;
            }
            ListNode<T> current = mNode;
            while (true) {
                ListNode<T> next = current.getNext();
                if (next == null || next.getCount() < node.getCount()) {
                    current.insertAfter(node);
                    break;
                }
                current = current.getNext();
            }
        }

        ListNode<T> mNode;
    }

    private List<HeadNode<T>> mHeads = new ArrayList<HeadNode<T>>();
    private TreeNode<T> mRoot = new TreeNode<T>(null);
}
