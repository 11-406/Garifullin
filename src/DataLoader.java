import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataLoader {

   
    public static int[] loadData(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> list = new ArrayList<>();

        while (scanner.hasNextInt()) {
            list.add(scanner.nextInt());
        }
        scanner.close();

        
        return list.stream().mapToInt(i -> i).toArray();
    }

    
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
