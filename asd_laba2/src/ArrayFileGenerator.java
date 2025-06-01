import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class ArrayFileGenerator {
    public static void main(String[] args) {
        String filename = "numbers.txt";
        int size = 10000;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Random random = new Random();

            for (int i = 0; i < size; i++) {
                int number = random.nextInt(10000);
                writer.write(Integer.toString(number));


                if (i < size - 1) {
                    writer.write(" ");
                }
            }

            System.out.println("Файл " + filename + " успешно создан с " + size + " числами.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}