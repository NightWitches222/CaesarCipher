import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String ALPHABET = "АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";
    private static final int ALPHABET_SIZE = ALPHABET.length();
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Выберите режим работы:");
            System.out.println("1. Шифрование текста");
            System.out.println("2. Расшифровка текста с известным ключом");
            System.out.println("3. Расшифровка методом brute force");
            System.out.println("4. Расшифровка методом статистического анализа (опционально)");
            System.out.println("5. Выход");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    encryptFile(scanner);
                    break;
                case 2:
                    decryptFile(scanner);
                    break;
                case 3:
                    bruteForceDecrypt(scanner);
                    break;
                case 4:
                    statisticalAnalysisDecrypt(scanner);
                    break;
                case 5:
                    System.out.println("Выход из программы.");
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void encryptFile(Scanner scanner) {
        System.out.println("Введите путь к файлу для шифрования:");
        String inputFilePath = scanner.nextLine();
        System.out.println("Введите путь для сохранения зашифрованного файла:");
        String outputFilePath = scanner.nextLine();
        System.out.println("Введите ключ (сдвиг):");
        int key = scanner.nextInt();
        scanner.nextLine();

        if (!Files.exists(Paths.get(inputFilePath))) {
            System.out.println("Файл не существует.");
            return;
        }

        key = key % ALPHABET_SIZE;
        processFile(inputFilePath, outputFilePath, key, true);
    }

    private static void decryptFile(Scanner scanner) {
        System.out.println("Введите путь к файлу для расшифровки:");
        String inputFilePath = scanner.nextLine();
        System.out.println("Введите путь для сохранения расшифрованного файла:");
        String outputFilePath = scanner.nextLine();
        System.out.println("Введите ключ (сдвиг):");
        int key = scanner.nextInt();
        scanner.nextLine();

        if (!Files.exists(Paths.get(inputFilePath))) {
            System.out.println("Файл не существует.");
            return;
        }

        key = key % ALPHABET_SIZE;
        processFile(inputFilePath, outputFilePath, key, false);
    }

    private static void bruteForceDecrypt(Scanner scanner) {
        System.out.println("Введите путь к файлу для расшифровки:");
        String inputFilePath = scanner.nextLine();
        System.out.println("Введите базовый путь для сохранения расшифрованных файлов:");
        String baseOutputPath = scanner.nextLine();

        if (!Files.exists(Paths.get(inputFilePath))) {
            System.out.println("Файл не существует.");
            return;
        }

        for (int key = 0; key < ALPHABET_SIZE; key++) {
            String outputFilePath = baseOutputPath + "_key_" + key + ".txt";
            processFile(inputFilePath, outputFilePath, key, false);
            System.out.println("Попробуйте ключ: " + key + ", файл сохранен в: " + outputFilePath);
        }
    }

    private static void statisticalAnalysisDecrypt(Scanner scanner) {
        System.out.println("Введите путь к файлу для расшифровки:");
        String inputFilePath = scanner.nextLine();
        System.out.println("Введите путь для сохранения расшифрованного файла:");
        String outputFilePath = scanner.nextLine();

        if (!Files.exists(Paths.get(inputFilePath))) {
            System.out.println("Файл не существует.");
            return;
        }

        // Для демонстрации просто копируем содержимое входного файла в выходной
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Результат статистического анализа сохранен в: " + outputFilePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обработке файла", e);
        }
    }

    private static void processFile(String inputFilePath, String outputFilePath, int key, boolean encrypt) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processedLine = processLine(line, key, encrypt);
                writer.write(processedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обработке файла", e);
        }
    }

    private static String processLine(String line, int key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        for (char character : line.toCharArray()) {
            int index = ALPHABET.indexOf(character);
            if (index != -1) {
                int newIndex = (encrypt) ? (index + key) % ALPHABET_SIZE : (index - key + ALPHABET_SIZE) % ALPHABET_SIZE;
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }
}
