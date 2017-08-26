package com.xj.scud.monitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/25 11:41
 * 方法性能数据
 */
public class PerformanceData {
    private String methodName;//方法名，不支持重载方法

    public PerformanceData(String methodName) {
        this.methodName = methodName;
    }

    private long totalCostTime;//总耗时
    private int size;//链表长度
    private Node root = new Node(-1);//链表

    public void add(int costTime) {
        size++;
        totalCostTime += costTime;
        Node newNode = new Node(costTime);
        if (root.getCostTime() == -1) {
            root = newNode;
        } else {
            this.insert(newNode);
        }
    }

    private synchronized void insert(Node newNode) {
        Node head = root;
        while (true) {
            if (head.getCostTime() > newNode.getCostTime()) {
                if (head.getLeft() == null) {
                    head.setLeft(newNode);
                    break;
                } else {
                    head = head.getLeft();
                }
            } else if (head.getCostTime() < newNode.getCostTime()) {
                if (head.getRight() == null) {
                    head.setRight(newNode);
                    break;
                } else {
                    head = head.getRight();
                }
            } else if (head.getCostTime() == newNode.getCostTime()) {
                head.setCount(head.getCount() + 1);
                break;
            }
        }
    }

    public TopPercentile getTP() {
        BigDecimal tp50Dec = new BigDecimal(this.size * 0.5);
        int tp50Index = tp50Dec.setScale(0, RoundingMode.UP).intValue();
        BigDecimal tp90Dec = new BigDecimal(this.size * 0.9);
        int tp90Index = tp90Dec.setScale(0, RoundingMode.UP).intValue();
        BigDecimal tp99Dec = new BigDecimal(this.size * 0.99);
        int tp99Index = tp99Dec.setScale(0, RoundingMode.UP).intValue();
        BigDecimal tp999Dec = new BigDecimal(this.size * 0.999);
        int tp999Index = tp999Dec.setScale(0, RoundingMode.UP).intValue();
        int tp50 = -1, tp90 = -1, tp99 = -1, tp999 = -1, min = -1, max = -1;

        List<Node> stack = new ArrayList<>(this.size);
        Node node = root;
        int i = -1;
        int count = 0;
        while (node != null || stack.size() > 0) {
            while (node != null) {
                stack.add(node);
                i++;
                node = node.getLeft();
            }
            if (stack.size() > 0) {
                node = stack.remove(i--);
                count += node.getCount();
                if (min == -1) {
                    min = node.getCostTime();
                }
                if (tp50 == -1) {
                    if (count >= tp50Index) {
                        tp50 = node.getCostTime();
                    }
                }
                if (tp90 == -1) {
                    if (count >= tp90Index) {
                        tp90 = node.getCostTime();
                    }
                }
                if (tp99 == -1) {
                    if (count >= tp99Index) {
                        tp99 = node.getCostTime();
                    }
                }
                if (count >= tp999Index) {
                    tp999 = node.getCostTime();
                }
                max = node.getCostTime();
                node = node.getRight();
            }
        }
        return new TopPercentile(tp50, tp90, tp99, tp999, size, min, max);
    }

    class TopPercentile {
        private int count;
        private int tp50;
        private int tp90;
        private int tp99;
        private int tp999;
        private int min;
        private int max;

        public TopPercentile(int tp50, int tp90, int tp99, int tp999, int count, int min, int max) {
            this.tp50 = tp50;
            this.tp90 = tp90;
            this.tp99 = tp99;
            this.tp999 = tp999;
            this.count = count;
            this.min = min;
            this.max = max;
        }

        public int getTp50() {
            return tp50;
        }

        public void setTp50(int tp50) {
            this.tp50 = tp50;
        }

        public int getTp90() {
            return tp90;
        }

        public void setTp90(int tp90) {
            this.tp90 = tp90;
        }

        public int getTp99() {
            return tp99;
        }

        public void setTp99(int tp99) {
            this.tp99 = tp99;
        }

        public int getTp999() {
            return tp999;
        }

        public void setTp999(int tp999) {
            this.tp999 = tp999;
        }

        public int getCount() {
            return count;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }
    }

    class Node {
        private Node left;
        private Node right;
        private int costTime;
        private int count = 1;

        public Node(int costTime) {
            this.costTime = costTime;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public int getCostTime() {
            return costTime;
        }

        public void setCostTime(int costTime) {
            this.costTime = costTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public long getTotalCostTime() {
        return totalCostTime;
    }

    public int getSize() {
        return size;
    }

    public static void main(String[] args) {
        PerformanceData data = new PerformanceData("test");
        data.add(123);
        data.add(13);
        data.add(23);
        data.add(12);
        data.add(5);
        data.add(231);
        data.add(25);
        data.add(78);
        data.add(14);
        data.add(687);
        data.add(84);
        data.add(16);
        data.add(13);
        data.getTP();
    }
}
