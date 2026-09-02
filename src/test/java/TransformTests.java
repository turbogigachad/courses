import org.junit.jupiter.api.Test;
import static org.example.Methods.printTransformName;
import static org.example.Methods.transformName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class TransformTests {
    @Test
    public void firstTest(){
        String expectedName = "Николай transformed";
        assertEquals(expectedName, transformName("Николай"));
    }
}
