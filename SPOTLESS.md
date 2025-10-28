# Spotless Code Formatting

This project uses the [Spotless Maven Plugin](https://github.com/diffplug/spotless) to enforce consistent code formatting across all Java source files.

## Configuration

- **Plugin Version**: 2.43.0
- **Formatter**: Google Java Format 1.17.0
- **Style**: Google Java Style Guide
- **Lifecycle Integration**: Spotless check runs automatically during the `validate` phase (before compilation)

## Usage

### Check Code Formatting

To check if your code is properly formatted:

```bash
mvn spotless:check
```

This command will fail if any files need formatting and show which files need to be fixed.

### Apply Code Formatting

To automatically format all Java files:

```bash
mvn spotless:apply
```

This command will reformat all Java files according to the Google Java Style Guide.

### Standard Build Workflow

Spotless check is integrated into the standard Maven build lifecycle:

```bash
mvn clean compile    # Spotless check runs during validate phase
mvn clean test       # Spotless check runs before tests
mvn clean install    # Spotless check runs before install
```

If any formatting violations are detected, the build will fail with a helpful error message showing:
- Which files have formatting issues
- The specific changes needed
- Instructions to run `mvn spotless:apply` to fix them

## IDE Integration

### IntelliJ IDEA

1. Install the [google-java-format plugin](https://plugins.jetbrains.com/plugin/8527-google-java-format)
2. Enable it in Settings → Other Settings → google-java-format Settings
3. Set the code style to "AOSP" (Android Open Source Project) which is compatible with Google Java Format

### Eclipse

1. Install the [google-java-format Eclipse plugin](https://github.com/google/google-java-format#eclipse)
2. Configure it in Preferences → Java → Code Style → Formatter

### VS Code

1. Install the [google-java-format extension](https://marketplace.visualstudio.com/items?itemName=hbenl.vscode-google-java-format)
2. Configure format on save in settings

## Best Practices

1. **Run `mvn spotless:apply` before committing** to ensure your code is properly formatted
2. **Don't disable Spotless** - consistent formatting helps the entire team
3. **Trust the formatter** - let Spotless handle formatting so you can focus on logic
4. **Configure your IDE** - IDE integration makes formatting automatic and seamless

## Configuration Details

The Spotless configuration is defined in the parent `pom.xml`:

```xml
<plugin>
  <groupId>com.diffplug.spotless</groupId>
  <artifactId>spotless-maven-plugin</artifactId>
  <version>${spotless-maven-plugin.version}</version>
  <configuration>
    <java>
      <googleJavaFormat>
        <version>1.17.0</version>
        <style>GOOGLE</style>
      </googleJavaFormat>
      <includes>
        <include>src/main/java/**/*.java</include>
        <include>src/test/java/**/*.java</include>
      </includes>
      <importOrder />
      <removeUnusedImports />
    </java>
  </configuration>
</plugin>
```

## Troubleshooting

### Build fails with formatting violations

Run `mvn spotless:apply` to automatically fix all formatting issues.

### Spotless formats code differently than my IDE

Make sure your IDE is configured to use the Google Java Style Guide. See the IDE Integration section above.

### I want to skip Spotless temporarily

While not recommended, you can skip Spotless checks during build:

```bash
mvn clean install -Dspotless.check.skip=true
```

However, CI/CD pipelines will likely enforce formatting, so it's better to fix issues locally.

## Learn More

- [Spotless Maven Plugin Documentation](https://github.com/diffplug/spotless/tree/main/plugin-maven)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [google-java-format](https://github.com/google/google-java-format)
