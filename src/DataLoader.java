import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLoader {

    /**
     * Загружает массив целых чисел из текстового файла
     *
     * @param filename путь к файлу с данными
     * @return массив целых чисел
     * @throws FileNotFoundException если файл не найден
     */
    public static int[] loadData(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> list = new ArrayList<>();

        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }
        scanner.close();

        // Конвертируем List<Integer> в int[]
        return list.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Загружает данные из всех файлов в указанной директории
     *
     * @param directoryPath путь к директории с файлами данных
     * @return список массивов целых чисел
     * @throws FileNotFoundException если директория не найдена
     */
    public static List<int[]> loadAllDataFromDirectory(String directoryPath) throws FileNotFoundException {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new FileNotFoundException("Директория не найдена: " + directoryPath);
        }

        List<int[]> datasets = new ArrayList<>();
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));

        if (files != null) {
            for (File file : files) {
                datasets.add(loadData(file.getAbsolutePath()));
            }
        }

        return datasets;
    }
}