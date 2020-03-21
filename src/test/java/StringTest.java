import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StringTest {
    @Test
    public void concatStrings() throws IOException {
        String filePath = "/home/andrey/IdeaProjects/OPD-2020/src/test/resources/concatTest.txt";

        String content = Files
                .lines(Paths.get(filePath))
                .reduce("", String::concat);
//        System.out.println(content);
    }

    @Test
    public void stringBuilderTest() throws IOException {
        String filePath = "/home/andrey/IdeaProjects/OPD-2020/src/test/resources/concatTest.txt";

        BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        String res = sb.toString();
//        System.out.println(res);
    }
}
