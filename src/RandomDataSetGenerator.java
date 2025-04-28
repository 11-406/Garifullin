import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomDataSetGenerator {
    private final Random random;
    private final String outputFolder;

    public RandomDataSetGenerator(String outputFolder) {
        this.random = new Random();
        this.outputFolder = outputFolder;
        createOutputFolder();
    }

    public void generateRandomDataSets(int minSets, int maxSets, int minSize, int maxSize) throws IOException {
        int datasetsCount = minSets + random.nextInt(maxSets - minSets + 1);
        System.out.println("Generating " + datasetsCount + " random datasets...");

        for (int i = 1; i <= datasetsCount; i++) {
            int datasetSize = minSize + random.nextInt(maxSize - minSize + 1);
            int[] dataset = generateRandomArray(datasetSize);
            saveToFile(dataset, i, datasetSize);
        }

        System.out.println("Datasets saved to: " + new File(outputFolder).getAbsolutePath());
    }

    private int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100000);
        }
        return array;
    }

    private void saveToFile(int[] array, int datasetNumber, int datasetSize) throws IOException {
        String fileName = outputFolder + "/dataset_" + datasetNumber + "_size_" + datasetSize + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int num : array) {
                writer.write(num + "\n");
            }
        }
    }

    private void createOutputFolder() {
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}