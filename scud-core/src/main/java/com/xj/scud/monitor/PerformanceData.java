package com.xj.scud.monitor;

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

    /**
     * 添加调用时间到链表
     *
     * @param costTime 方法耗时
     */
    public void add(int costTime) {
        size++;
        totalCostTime += costTime;
        Node newNode = new Node(costTime);
        if (root.costTime == -1) {
            root = newNode;
        } else {
            this.insertRBT(newNode);
        }
    }

    /**
     * 二叉树插入
     *
     * @param newNode
     */
    private synchronized void insert(Node newNode) {
        Node head = root;
        while (true) {
            if (head.costTime > newNode.costTime) {
                if (head.left == null) {
                    head.left = newNode;
                    break;
                } else {
                    head = head.left;
                }
            } else if (head.costTime < newNode.costTime) {
                if (head.right == null) {
                    head.right = newNode;
                    break;
                } else {
                    head = head.right;
                }
            } else if (head.costTime == newNode.costTime) {
                head.count = head.count + 1;
                break;
            }
        }
    }

    /**
     * 红黑树插入
     *
     * @param newNode
     */
    private synchronized void insertRBT(Node newNode) {
        Node t = root;
        Node parent;
        int cmp;
        do {
            parent = t;
            cmp = newNode.costTime - t.costTime;
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                t.count++;
                return;
            }
        } while (t != null);
        newNode.parent = parent;
        if (cmp < 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        fixAfterInsertion(newNode);
    }

    private void fixAfterInsertion(Node x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                Node y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private static boolean colorOf(Node p) {
        return (p == null ? BLACK : p.color);
    }

    private static Node parentOf(Node p) {
        return (p == null ? null : p.parent);
    }

    private static void setColor(Node p, boolean c) {
        if (p != null) {
            p.color = c;
        }
    }

    private static Node leftOf(Node p) {
        return (p == null) ? null : p.left;
    }

    private static Node rightOf(Node p) {
        return (p == null) ? null : p.right;
    }

    private void rotateLeft(Node p) {
        if (p != null) {
            Node r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }
            r.parent = p.parent;
            if (p.parent == null) {
                root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }
            r.left = p;
            p.parent = r;
        }
    }

    private void rotateRight(Node p) {
        if (p != null) {
            Node l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }
            l.parent = p.parent;
            if (p.parent == null) {
                root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }
            l.right = p;
            p.parent = l;
        }
    }

    /**
     * 获取系统性能数据
     *
     * @return
     */
    public TopPercentile getTP() {
        int tp50Index = this.size * 5;
        tp50Index = tp50Index % 10 == 0 ? tp50Index / 10 : tp50Index / 10 + 1;
        int tp90Index = this.size * 9;
        tp90Index = tp90Index % 10 == 0 ? tp90Index / 10 : tp90Index / 10 + 1;
        int tp99Index = this.size * 99;
        tp99Index = tp99Index % 100 == 0 ? tp99Index / 100 : tp99Index / 100 + 1;
        int tp999Index = this.size * 999;
        tp999Index = tp999Index % 1000 == 0 ? tp999Index / 1000 : tp999Index / 1000 + 1;
        int tp50 = -1, tp90 = -1, tp99 = -1, tp999 = -1, min = -1, max = -1;

        Node[] stack = new Node[32];
        Node node = root;
        int i = -1;
        int count = 0;
        while (node != null || i >= 0) {
            while (node != null) {
                stack[++i] = node;
                node = node.left;
            }
            if (i >= 0) {
                node = stack[i--];
                count += node.count;
                //System.out.println(node.costTime + "  " + count);
                if (min == -1) {
                    min = node.costTime;
                }
                if (tp50 == -1 && count >= tp50Index) {
                    tp50 = node.costTime;
                }
                if (tp90 == -1 && count >= tp90Index) {
                    tp90 = node.costTime;
                }
                if (tp99 == -1 && count >= tp99Index) {
                    tp99 = node.costTime;
                }
                if (tp999 == -1 && count >= tp999Index) {
                    tp999 = node.costTime;
                }
                max = node.costTime;
                node = node.right;
            }
        }
        return new TopPercentile(tp50, tp90, tp99, tp999, size, min, max);
    }

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    class Node {
        private Node left;
        private Node right;
        private int costTime;
        private int count = 1;
        private Node parent;
        boolean color = BLACK;

        public Node(int costTime) {
            this.costTime = costTime;
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
        data.add(8);
        data.add(2);
        data.add(3);
        data.add(6);
        data.add(5);
        data.add(23);
        data.add(7);
        data.add(8);
        data.add(45);
        data.add(3);
        data.add(32);
        data.add(1);
        data.add(13);
        data.add(36);
        System.out.println(data.getTP());
    }
}
