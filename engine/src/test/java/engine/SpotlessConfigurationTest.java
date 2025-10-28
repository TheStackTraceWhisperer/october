package engine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/** Test to verify Spotless Maven plugin is properly configured. */
class SpotlessConfigurationTest {

  @Test
  void parentPomShouldContainSpotlessPlugin() throws Exception {
    Path pomPath = Paths.get("").toAbsolutePath().getParent().resolve("pom.xml");
    assertTrue(Files.exists(pomPath), "Parent pom.xml should exist");

    String pomContent = Files.readString(pomPath);
    assertTrue(
        pomContent.contains("spotless-maven-plugin"),
        "Parent pom.xml should contain spotless-maven-plugin");
    assertTrue(
        pomContent.contains("googleJavaFormat"),
        "Parent pom.xml should contain googleJavaFormat configuration");
  }

  @Test
  void enginePomShouldReferenceSpotlessPlugin() throws Exception {
    Path pomPath = Paths.get("pom.xml");
    assertTrue(Files.exists(pomPath), "Engine pom.xml should exist");

    String pomContent = Files.readString(pomPath);
    assertTrue(
        pomContent.contains("spotless-maven-plugin"),
        "Engine pom.xml should reference spotless-maven-plugin");
  }

  @Test
  void spotlessConfigurationFileShouldNotExist() {
    // Spotless configuration should be in pom.xml, not in a separate file
    File spotlessConfig = new File("spotless.gradle");
    assertTrue(!spotlessConfig.exists(), "Should not have a separate Spotless config file");
  }
}
