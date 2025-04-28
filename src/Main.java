import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        try {
            RandomDataSetGenerator generator = new RandomDataSetGenerator("Papka");
            generator.generateRandomDataSets(50, 100, 100, 10000);
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        try {
            List<int[]> allData = DataLoader.loadAllDataFromDirectory("Papka");
            for (int[] data : allData) {
                int size = data.length;
                long startTime = System.nanoTime();
                Smoothsort.sort(data);
                long endTime = System.nanoTime();
                long elapsedTime = (endTime - startTime);
                System.out.println("Количество элементов " + size + " Время " + elapsedTime + " (нс) "+" Количество итераций " + Smoothsort.getSiftIterations());

            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }
}