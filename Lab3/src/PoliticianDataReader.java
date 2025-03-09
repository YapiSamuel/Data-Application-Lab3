import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PoliticianDataReader {
    public static void main(String[] args) {
        String filePath = "src/Politicians where gender equals female.csv"; // Update file path

        List<Politician> politicians = loadData(filePath);

        if (politicians.isEmpty()) {
            System.out.println("No data found.");
            return;
        }

        // 1. Print attributes of the first data item
        System.out.println("First Politician: " + politicians.get(0));

        // 2. Print attributes of the 10th data item (if exists)
        if (politicians.size() >= 10) {
            System.out.println("Tenth Politician: " + politicians.get(9));
        } else {
            System.out.println("Less than 10 politicians in the dataset.");
        }

        // 3. Display the total number of entries
        System.out.println("Total number of politicians: " + politicians.size());
    }

    public static List<Politician> loadData(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            return lines.skip(1) // Skip header
                    .map(line -> safeSplit(line)) // Use safe CSV splitting
                    .filter(parts -> parts.length >= 4)
                    .map(parts -> new Politician(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[4].trim()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return List.of();
        }
    }

    private static String[] safeSplit(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handles quoted CSV fields properly
    }
}
