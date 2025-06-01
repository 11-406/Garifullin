import java.util.*;
import java.io.*;

class FibonacciHeap {
    private Node min;
    private int n;

    private static class Node {
        int key;
        int degree;
        Node parent;
        Node child;
        Node left;
        Node right;
        boolean mark;

        Node(int key) {
            this.key = key;
            this.left = this;
            this.right = this;
        }
    }

    private int operationCount = 0;
    private List<Node> allNodes = new ArrayList<>();

    public void insert(int key) {
        operationCount = 0;
        Node node = new Node(key);
        allNodes.add(node);
        operationCount++;
        if (min == null) {
            min = node;
            operationCount++;
        } else {
            insertIntoRootList(node);
            operationCount++;
            if (key < min.key) {
                min = node;
                operationCount++;
            }
        }
        n++;
        operationCount++;
    }

    private void insertIntoRootList(Node node) {
        node.left = min;
        node.right = min.right;
        min.right.left = node;
        min.right = node;
        operationCount += 4;
    }

    public Integer findMin() {
        return (min == null) ? null : min.key;
    }

    public Integer extractMin() {
        operationCount = 0;
        Node z = min;
        if (z != null) {
            if (z.child != null) {
                Node c = z.child;
                do {
                    Node next = c.right;
                    insertIntoRootList(c);
                    c.parent = null;
                    c = next;
                } while (c != z.child);
            }
            z.left.right = z.right;
            z.right.left = z.left;
            if (z == z.right) {
                min = null;
            } else {
                min = z.right;
                consolidate();
            }
            n--;
            allNodes.remove(z);
            return z.key;
        }
        return null;
    }

    private void consolidate() {
        int maxDegree = ((int) Math.floor(Math.log(n) / Math.log(2))) + 1;
        Node[] A = new Node[maxDegree];

        List<Node> rootList = new ArrayList<>();
        Node x = min;
        if (x != null) {
            do {
                rootList.add(x);
                x = x.right;
            } while (x != min);
        }

        for (Node w : rootList) {
            x = w;
            int d = x.degree;
            while (A[d] != null) {
                Node y = A[d];
                if (x.key > y.key) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                A[d] = null;
                d++;
            }
            A[d] = x;
        }

        min = null;
        for (Node a : A) {
            if (a != null) {
                if (min == null) {
                    min = a;
                    a.left = a.right = a;
                } else {
                    insertIntoRootList(a);
                    if (a.key < min.key) {
                        min = a;
                    }
                }
            }
        }
    }

    private void link(Node y, Node x) {
        y.left.right = y.right;
        y.right.left = y.left;
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.left = y.right = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        x.degree++;
        y.mark = false;
    }

    public boolean search(int key) {
        operationCount = 0;
        for (Node node : allNodes) {
            operationCount++;
            if (node.key == key) {
                return true;
            }
        }
        return false;
    }

    public int getLastOperationCount() {
        return operationCount;
    }

    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();

        List<Integer> numberList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("numbers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                for (String part : parts) {
                    try {
                        numberList.add(Integer.parseInt(part));
                    } catch (NumberFormatException e) {
                        System.err.println("Пропущено некорректное значение: " + part);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return;
        }

        long totalInsertTime = 0;
        long totalInsertOps = 0;
        try (PrintWriter writer = new PrintWriter(new FileWriter("insertion_log.txt"))) {
            for (int number : numberList) {
                long insertStart = System.nanoTime();
                heap.insert(number);
                long insertEnd = System.nanoTime();
                int operations = heap.getLastOperationCount();
                totalInsertTime += (insertEnd - insertStart);
                totalInsertOps += operations;
                String output = String.format("Вставлено %d за %d нс (%d операций)", number, (insertEnd - insertStart), operations);
                System.out.println(output);
                writer.println(output);
            }
            String avgTime = String.format("Среднее время вставки: %d нс", totalInsertTime / numberList.size());
            String avgOps = String.format("Среднее количество операций при вставке: %d", totalInsertOps / numberList.size());
            writer.println(avgTime);
            writer.println(avgOps);
            System.out.println(avgTime);
            System.out.println(avgOps);
        } catch (IOException e) {
            System.err.println("Ошибка записи файла журнала вставки: " + e.getMessage());
        }

        try (PrintWriter searchWriter = new PrintWriter(new FileWriter("search_log.txt"))) {
            Random random = new Random();
            long totalSearchTime = 0;
            long totalSearchOps = 0;
            for (int i = 0; i < 100; i++) {
                int index = random.nextInt(numberList.size());
                int keyToSearch = numberList.get(index);
                long searchStart = System.nanoTime();
                boolean found = heap.search(keyToSearch);
                long searchEnd = System.nanoTime();
                int operations = heap.getLastOperationCount();
                totalSearchTime += (searchEnd - searchStart);
                totalSearchOps += operations;
                String result = String.format("Поиск %d: %s за %d нс (%d операций)", keyToSearch, found ? "Найден" : "Не найден", (searchEnd - searchStart), operations);
                System.out.println(result);
                searchWriter.println(result);
            }
            String avgTime = String.format("Среднее время поиска: %d нс", totalSearchTime / 100);
            String avgOps = String.format("Среднее количество операций при поиске: %d", totalSearchOps / 100);
            searchWriter.println(avgTime);
            searchWriter.println(avgOps);
            System.out.println(avgTime);
            System.out.println(avgOps);
        } catch (IOException e) {
            System.err.println("Ошибка записи файла журнала поиска: " + e.getMessage());
        }

        try (PrintWriter deleteWriter = new PrintWriter(new FileWriter("delete_log.txt"))) {
            long totalDeleteTime = 0;
            long totalDeleteOps = 0;
            for (int i = 0; i < 1000; i++) {
                long deleteStart = System.nanoTime();
                Integer deleted = heap.extractMin();
                long deleteEnd = System.nanoTime();
                int operations = heap.getLastOperationCount();
                totalDeleteTime += (deleteEnd - deleteStart);
                totalDeleteOps += operations;
                String result = String.format("Удалено %d за %d нс (%d операций)", deleted, (deleteEnd - deleteStart), operations);
                System.out.println(result);
                deleteWriter.println(result);
            }
            String avgTime = String.format("Среднее время удаления: %d нс", totalDeleteTime / 1000);
            String avgOps = String.format("Среднее количество операций при удалении: %d", totalDeleteOps / 1000);
            deleteWriter.println(avgTime);
            deleteWriter.println(avgOps);
            System.out.println(avgTime);
            System.out.println(avgOps);
        } catch (IOException e) {
            System.err.println("Ошибка записи файла журнала удаления: " + e.getMessage());
        }
    }
}
