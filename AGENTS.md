# AGENTS.md

## Project Overview

Handy URI Templates is a Java library implementing [RFC6570 URI Templates](http://tools.ietf.org/html/rfc6570). It provides a fluent API for creating, parsing, and expanding URI templates up to level 4 expressions.

## Build System

This project uses **Apache Maven** with the Maven Wrapper.

```bash
# Build the project
./mvnw package

# Run tests
./mvnw test

# Full build with site/reports
./mvnw install site
```

Do not use `mvn` directly; always use `./mvnw` to ensure a consistent Maven version (3.6.1).

## Java Version

- **Source/Target compatibility:** Java 7 (`1.7`)
- **CI builds:** OpenJDK 8
- Do not use language features or APIs above Java 7 in production code.

## Project Structure

```
src/main/java/com/damnhandy/uri/template/   # Core library
src/main/java/com/damnhandy/uri/template/impl/       # Internal implementation
src/main/java/com/damnhandy/uri/template/jackson/     # Jackson integration (optional)
src/test/java/com/damnhandy/uri/template/             # Tests
src/test/java/com/damnhandy/uri/template/conformance/ # RFC6570 compliance tests
config/checkstyle.xml                                  # Checkstyle rules
uritemplate-test/                                      # RFC6570 test suite (git submodule)
```

## Testing

- **Framework:** JUnit 4 with Hamcrest matchers
- **Run:** `./mvnw test`
- **Code coverage:** JaCoCo with a 76% complexity coverage minimum
- Conformance tests in `src/test/java/.../conformance/` verify RFC6570 spec compliance. These must always pass.

## Code Style

An `.editorconfig` is provided. Key conventions:

- **Indent:** 4 spaces (2 for YAML)
- **Line endings:** LF
- **Charset:** UTF-8
- **Brace style:** Allman (opening brace on its own line)
- **Trim trailing whitespace:** yes
- **Final newline:** yes

### Checkstyle

Checkstyle is configured at `config/checkstyle.xml` and enforces:

- Allman brace style (opening brace on new line via `LeftCurly` option `"nl"`)
- No star imports
- Javadoc required on all public classes and methods
- Max cyclomatic complexity and nesting depth (nested if max 2)
- Standard Java naming conventions

### License Headers

All Java source files must include the Apache License 2.0 header with copyright `Ryan J. McDonough`.

## Key Dependencies

| Dependency | Scope | Notes |
|---|---|---|
| joda-time | compile | Date/time utilities |
| jackson-core/databind/annotations | optional | JSON support |
| junit 4.13.1 | test | Testing framework |
| httpclient | test | Example/integration tests |

## Static Analysis

- **SpotBugs:** Runs automatically on build (max effort, max threshold)
- **PMD:** Configured for reporting (design, basic, braces rulesets)
- **JaCoCo:** Code coverage enforcement (76% minimum)

## CI/CD

GitHub Actions (`.github/workflows/maven.yml`) runs `mvn -B package` on pushes and PRs to `master` using JDK 8.

## Publishing

The project publishes to Maven Central via the Sonatype OSS parent POM. Artifact coordinates:

```
com.damnhandy:handy-uri-templates
```

Packaging type is OSGi bundle (`maven-bundle-plugin`).
