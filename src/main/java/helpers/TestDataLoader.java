package helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.TableRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestDataLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static List<TableRecord> loadExpectedTableData(String resourcePath) {
        try (InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found: " + resourcePath);
            }
            return MAPPER.readValue(is, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data: " + resourcePath, e);
        }
    }
}