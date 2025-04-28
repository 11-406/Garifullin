public class Smoothsort {
    private static int siftIterations = 0;
    private static int mergeIterations = 0;

    // Числа Леонардо
    private static final int[] leonardoNumbers = {
            1, 1, 3, 5, 9, 15, 25, 41, 67, 109, 177, 287, 465, 753, 1219, 1973,
            3193, 5167, 8361, 13529, 21891, 35421, 57313, 92735, 150049, 242785,
            392835, 635621, 1028457, 1664079, 2692537, 4356617, 7049155, 11405773,
            18454929, 29860703, 48315633, 78176337, 126491971, 204668309, 331160281,
            535828591, 866988873 // достаточно для массивов размером до 2^32 элементов
    };

    public static void sort(int[] array) {
        siftIterations = 0;
        mergeIterations = 0;

        int heapSize = 0; // размер кучи в элементах
        int[] heap = new int[array.length]; // массив для хранения размеров куч

        // Шаг 1: Построение кучи Леонардо
        for (int i = 0; i < array.length; i++) {
            if (heapSize >= 2 && heap[heapSize-2] == heap[heapSize-1] + 1) {
                // Объединяем две предыдущие кучи
                heapSize--;
                heap[heapSize-1]++;
                siftIterations++; // учитываем итерацию при объединении
            } else {
                if (heapSize >= 1 && heap[heapSize-1] == 1) {
                    heap[heapSize++] = 0;
                } else {
                    heap[heapSize++] = 1;
                }
            }
            sift(array, i, heap[heapSize-1]);
        }

        // Шаг 2: Разборка кучи Леонардо
        for (int i = array.length - 1; i > 0; i--) {
            if (heap[heapSize-1] <= 1) {
                heapSize--;
            } else {
                int k = heap[heapSize-1];
                int left, right;

                heapSize--;
                left = k - 1;
                right = k - 2;

                heap[heapSize++] = left;
                heap[heapSize++] = right;

                // Просеивание в левой подкуче
                sift(array, i - leonardoNumbers[right] - 1, left);
                mergeIterations++;

                // Просеивание в правой подкуче
                sift(array, i - 1, right);
                mergeIterations++;
            }
        }
    }

    // Просеивание (sift) для кучи Леонардо
    private static void sift(int[] array, int root, int size) {
        siftIterations++;

        int current = root;
        while (size > 1) {
            int right = current - 1;
            int left = current - 1 - leonardoNumbers[size - 2];

            // Выбираем наибольшего из текущего элемента и его детей
            if (array[current] >= array[left] && array[current] >= array[right]) {
                break;
            }

            if (array[left] >= array[right]) {
                swap(array, current, left);
                current = left;
                size -= 1;
            } else {
                swap(array, current, right);
                current = right;
                size -= 2;
            }
            siftIterations++;
        }
    }

    private static void swap(int[] array, int a, int b) {
        int temp = array[a];
        array[a] = array[b];
        array[b] = temp;
    }

    public static int getSiftIterations() {
        return siftIterations;
    }

    public static int getMergeIterations() {
        return mergeIterations;
    }
    public static int getIterations() {
        return siftIterations + mergeIterations;
    }

    public static void main(String[] args) {
        int[] array = {5, 3, 8, 1, 9, 4, 7, 2, 6};

        System.out.println("До сортировки:");
        for (int num : array) {
            System.out.print(num + " ");
        }

        sort(array);

        System.out.println("\nПосле сортировки:");
        for (int num : array) {
            System.out.print(num + " ");
        }

        System.out.println("\nКоличество итераций просеивания: " + getSiftIterations());
        System.out.println("Количество итераций слияния: " + getMergeIterations());
    }
}