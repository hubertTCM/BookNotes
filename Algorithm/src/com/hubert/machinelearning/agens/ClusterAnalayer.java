package com.hubert.machinelearning.agens;

import java.util.*;

public class ClusterAnalayer<T> {
    public List<CompositeNode<T>> getNodes(CompositeNode<T> root, int count) {

        PriorityQueue<CompositeNode<T>> priorityQueue = new PriorityQueue<CompositeNode<T>>(
                new Comparator<CompositeNode<T>>() {

                    @Override
                    public int compare(CompositeNode<T> x, CompositeNode<T> y) {
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
            CompositeNode<T> node = priorityQueue.remove();
            for (CompositeNode<T> child : node.getCompositeNodes()) {
                priorityQueue.add(child);
            }
        }

        List<CompositeNode<T>> nodes = new ArrayList<CompositeNode<T>>();
        while (!priorityQueue.isEmpty()) {
            CompositeNode<T> tempNode = priorityQueue.remove();
            nodes.add(tempNode);

//            String s = Integer.toString(tempNode.getLeafNodes().size()) + ",";
//            for (T item : tempNode.getCenter()) {
//                s += " " + item;
//            }
//            System.out.println(s);
        }
        return nodes;
    }

}
