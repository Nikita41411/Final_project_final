package praktikum;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertTrue;

public class PraktikumTest {

    @Test
    public void testMainMethod() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            Praktikum.main(new String[]{});

            String output = outputStream.toString();

            assertTrue(output.contains("(==== black bun ====)"));
            assertTrue(output.contains("Price:"));
        } finally {
            System.setOut(originalOut);
        }
    }
}