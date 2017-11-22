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

    public List<Pair<Integer, List<T>>> get(T value, int limit) {
        HeadNode<T> head = findHead(value);
        return get(head, limit);
    }

    public List<Pair<Integer, List<T>>> getAll(int limit) {
        List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
        for (HeadNode<T> head : mHeads) {
            result.addAll(get(head, limit));
        }
        return result;
    }

    private List<Pair<Integer, List<T>>> get(HeadNode<T> head, int lowerLimit) {
        List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
        head.merge(result, lowerLimit);
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
                HeadNode<T> h1 = findHead(o1);
                HeadNode<T> h2 = findHead(o2);
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
            HeadNode<T> head = mHeads.get(i);
            if (head.getCount() < mLowerLimit) {
                mHeads.remove(i);
                continue;
            }
            break;
        }
    }

    protected void sortHeads() {
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
        ArrayList<T> temp = new ArrayList<T>();
        temp.addAll(source);
        sort(temp);

        TreeNode<T> currentNode = mRoot;
        for (T data : temp) {
            HeadNode<T> head = findHead(data);
            if (head == null) {
                break;
            }
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

        public TreeNode<T> getParent() {
            return mParent;
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

        public List<Pair<Integer, List<T>>> getAllItems() {
            List<Pair<Integer, List<T>>> result = new ArrayList<Pair<Integer, List<T>>>();
            //List<T> current = new ArrayList<T>();
            //ListNode<T> currentNode = mNext;
            return result;
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

        // public void swap(ListNode<T> to){
        // if (to == null){
        // return;
        // }
        //
        // // just change the data value is OK here
        // TreeNode<T> temp = mValue;
        // mValue = to.getValue();
        // to.mValue = temp;
        // }

        TreeNode<T> mValue;
        ListNode<T> mNext;
    }

    private class HeadNode<T> extends NodeBase<T> {
        public HeadNode(T value, int count) {
            super(value, count);
            mNode = null;
        }

        public void merge(List<Pair<Integer, List<T>>> source, int lowerLimit) {
            if (mCount < lowerLimit) {
                return;
            }

            ListNode<T> node = mNode;
            while (node != null) {
                List<Pair<Integer, List<T>>> temp = node.getAllItems();
                merge(source, temp);
                node = node.getNext();
            }
        }

        private void merge(List<Pair<Integer, List<T>>> to, List<Pair<Integer, List<T>>> from) {
            for (int i = 0; i < from.size(); i++) {
                for (int j = 0; j < to.size(); j++) {
                    Pair<Integer, List<T>> pairI = from.get(i);
                    List<T> valueI = pairI.getValue();
                    Pair<Integer, List<T>> pairJ = to.get(j);
                    List<T> valueJ = pairJ.getValue();
                    if (!valueI.containsAll(valueJ) || !valueJ.contains(valueI)) {
                        continue;
                    }
                    to.set(j, new Pair<>(pairI.getKey() + pairJ.getKey(), valueJ));
                }
            }
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
    private Collection<Collection<T>> mSource;
    private int mLowerLimit = 0;
}
