package helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.TableRecord;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class TestDataLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T load(String resourcePath, Class<T> type) {
        return readFrom(resourcePath, is -> MAPPER.readValue(is, type));
    }

    public static <T> List<T> loadList(String resourcePath, Class<T> elementType) {
        JavaType listType = MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
        return readFrom(resourcePath, is -> MAPPER.readValue(is, listType));
    }

    private static <T> T readFrom(String resourcePath, IOFunction<InputStream, T> reader) {
        try (InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found: " + resourcePath);
            }
            return reader.apply(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data: " + resourcePath, e);
        }
    }

    public static String resolveTestFile(String resourcePath) {
        URL url = TestDataLoader.class.getClassLoader().getResource(resourcePath);
        if (url == null) {
            throw new IllegalStateException("Test file not found: " + resourcePath);
        }
        try {
            return Paths.get(url.toURI()).toAbsolutePath().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to resolve test file: " + resourcePath, e);
        }
    }

    @FunctionalInterface
    private interface IOFunction<A, R> {
        R apply(A a) throws IOException;
    }
}