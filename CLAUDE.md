# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

GoLand IDE plugin (Kotlin, IntelliJ Platform Gradle Plugin 2.x) adding godog/Cucumber support to Go projects. JVM toolchain 21, `platformType=GO`.

## Commands

```sh
./gradlew buildPlugin                          # .zip under build/distributions/
./gradlew runIde                               # launch sandbox GoLand
./gradlew check                                # tests

# Single class or method (FQCN = fully-qualified class name, package + class):
./gradlew test --tests "<FQCN>"
./gradlew test --tests "<FQCN>.<methodName>"
```

Tests spin up an in-process IDE sandbox per class — slow. Prefer single-test runs.

## Git workflow

- Never commit directly to main — always create a feature branch first.
- Never push directly to main — always raise a PR.
- Use `git mv` for renames.
- Prefer non-interactive autosquash for fixups; do not amend commits without confirming first.
- Before committing, renaming, or deleting any file, show the intended change and wait for explicit approval.

## Change types

The type describes the nature of the change and is used consistently across branch names, commit messages, and PR titles:

- `feat` — new functionality
- `fix` — bug fix
- `test` — adds or fixes tests in the codebase
- `refactor` — code restructuring with no behaviour change
- `docs` — documentation only
- `chore` — CI, build, or tooling changes

**Branch names:** `<type>/<short-kebab-description>` — always agree the branch name with the user before checking out and starting work.

**Commit messages and PR titles:** `<type>: <message>` — lowercase only the first letter of the message (e.g. `feat: add step rename support`).

## Debugging & diagnosis

- Before concluding a root cause, verify the hypothesis empirically — browse GitHub source, inspect plugin.xml, decompile bytecode, or run a probe.
- For API questions, browse GitHub source first (see Browsing plugin & platform sources); only decompile bytecode if the class is not available there.
- Never act on an unverified advisor conclusion. State your confidence level before proposing a fix.

## Naming conventions

- Align fixture, scenario, and test names with the surrounding directory naming conventions.
- Avoid generic placeholder names.
- `module`/`package` names follow `lowercase(areaDir) + lowercase(leafDir)` for all two-level fixture hierarchies under `src/test/testData/` and `sandbox/`. Leaf directory names must not redundantly repeat the area prefix.
  - e.g. `area=resolve, leaf=quotePattern` → `module/package: resolvequotepattern`
- Kotlin test method names must stay in lockstep with fixture leaf directory names — `getTestName(true)` derives fixture paths dynamically from the method name.

## Browsing plugin & platform sources

Prefer browsing GitHub source over decompiling local jars — source has comments and Javadoc, class hierarchies are easier to navigate, and build-versioned branches allow exact version matching.

- **`intellij-community`** ([JetBrains/intellij-community](https://github.com/JetBrains/intellij-community)) — IntelliJ Platform (`com.intellij.*`)
- **`intellij-plugins`** ([JetBrains/intellij-plugins](https://github.com/JetBrains/intellij-plugins)) — Gherkin (`gherkin`) and Cucumber for Java (`cucumber-java`) plugins

Both repos have versioned branches matching IntelliJ build numbers exactly (e.g. `262.6228`). When investigating a plugin verifier deprecation, browse the branch that matches the flagged build rather than `master`.

### Browsing a class on GitHub

1. Find the file path by searching the repo:
   ```sh
   gh api "search/code?q=<ClassName>+repo:JetBrains/intellij-plugins" --jq '.items[].path'
   # or for platform classes:
   gh api "search/code?q=<ClassName>+repo:JetBrains/intellij-community" --jq '.items[].path'
   ```
2. Get the raw URL for the file at a specific build branch, then fetch it:
   ```sh
   gh api "repos/JetBrains/intellij-plugins/contents/<path/to/File.java>?ref=<build>" --jq '.download_url'
   curl -s "<url>"
   ```

Example — `CucumberJvmExtensionPoint` on build `262.6228`:
```sh
gh api "search/code?q=CucumberJvmExtensionPoint+repo:JetBrains/intellij-plugins" --jq '.items[].path'
gh api "repos/JetBrains/intellij-plugins/contents/cucumber/src/org/jetbrains/plugins/cucumber/CucumberJvmExtensionPoint.java?ref=262.6228" --jq '.download_url'
```

### Decompiling a local jar

Use when the class is not in `intellij-community` or `intellij-plugins` (e.g. Go plugin APIs).

1. Find the jar in the Gradle transform cache. Go plugin classes are inside the GoLand
   distribution transform under `plugins/go-plugin/lib/`:
   ```sh
   # For Go plugin classes:
   find ~/.gradle/caches -path "*/goland-*/plugins/go-plugin/lib/*.jar"
   # For other plugins (e.g. gherkin):
   find ~/.gradle/caches -name "*.jar" -path "*<plugin-name>*" | grep -v sources
   ```
2. Confirm the class is present:
   ```sh
   jar tf <path/to/plugin.jar> | grep ClassName
   ```
3. Extract and decompile:
   ```sh
   cd /tmp && mkdir decompile && cd decompile
   jar xf <path/to/plugin.jar> <org/example/ClassName.class>
   javap -p <org/example/ClassName.class>
   ```

**Exception:** Go plugin APIs (`com.goide.*`, `org.jetbrains.plugins.go`) are not open source — decompile the local jar for those.

## Test conventions

### Fixture layout

- **Method naming**: classes extending a platform base (e.g. `GoCodeInsightFixtureTestCase`) discover via JUnit 3 — `test` prefix is mandatory. Pure `@Test`-annotated JUnit 4 tests drop the prefix.
- **Fixture layout**: per-test-method subdir named after `getTestName(true)` (lowercased method name minus `test`) under `getTestDataPath()`, which is usually area-prefixed (e.g. `src/test/testData/resolve`) but is the bare `src/test/testData` root for some classes. `myFixture.copyDirectoryToProject` resolves from `getTestDataPath()`, not `getBasePath()`.
- **Inspection fixtures** use inline `<info>`/`<error>`/`<warning>` markers; always call `testHighlighting(true, true, true)` and enable `CucumberStepInspection` before asserting unresolved-step errors.
- **Do not assert on `package` or module import paths** in sandbox tests — the sandbox does not resolve them reliably.

### Bug-fix workflow

- **Bug-fix PRs include a regression test** that would have caught the bug.
- **Before fixing a bug**, write a failing test that demonstrates it — confirm it fails for the right reason before implementing the fix.
- **After fixing**, re-run the test and confirm it is green.
