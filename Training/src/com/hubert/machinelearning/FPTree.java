package com.hubert.machinelearning;

import java.util.*;
import javafx.util.*;

// http://www.cnblogs.com/pinard/p/6307064.html
public class FPTree<T> {
    
    public List<Pair<Integer, Collection<T>>> get(Collection<T> source){
        return null;
    }

    private class TreeNode<T> {
        public TreeNode(T value, int count) {
            mValue = value;
            mCount = count;
        }

        public void increaseCount(int count) {
            mCount += count;
        }

        private int mCount;
        private T mValue;
        private TreeNode<T> mParent;
        private List<TreeNode<T>> mChildren = new ArrayList<TreeNode<T>>();
        // private Node<T> mNext;
    }

    private class ListNode<T> {
        public ListNode(TreeNode<T> node) {
            mNext = null;
            mValue = node;
        }

        public ListNode<T> getNext() {
            return mNext;
        }

        public void setNext(ListNode<T> next) {
            mNext = next;
        }

        public TreeNode<T> getValue() {
            return mValue;
        }

        TreeNode<T> mValue;
        ListNode<T> mNext;
    }

    private class HeadNode<T> {
        public HeadNode(T value, int count) {
            mValue = value;
            mCount = count;
            mNode = null;
        }

        public ListNode<T> getNode() {
            return mNode;
        }

        public void setNode(ListNode<T> node) {
            mNode = node;
        }

        public T getValue() {
            return mValue;
        }

        private T mValue;
        private int mCount;
        ListNode<T> mNode;
    }

    private List<HeadNode<T>> mHeads = new ArrayList<HeadNode<T>>();
}
