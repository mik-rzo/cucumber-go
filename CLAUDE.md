# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

GoLand IDE plugin (Kotlin, IntelliJ Platform Gradle Plugin 2.x) adding godog/Cucumber support to Go projects. JVM toolchain 21, `platformType=GO`.

## Commands

```sh
./gradlew buildPlugin                          # .zip under build/distributions/
./gradlew runIde                               # launch sandbox GoLand
./gradlew check                                # tests + verifyPlugin

# Single class or method (FQCN = fully-qualified class name, package + class):
./gradlew test --tests "<FQCN>"
./gradlew test --tests "<FQCN>.<methodName>"
```

Tests spin up an in-process IDE sandbox per class — slow. Prefer single-test runs.

## Gotchas

- **Do not add `override` to `CucumberExtension.loadStepsFor(module: Module)`.** Missing modifier is intentional — satisfies a 262.x abstract method via JVM signature resolution while still compiling against 261.x. Adding `override` breaks the build. The scope must stay `getModuleWithDependenciesAndLibrariesScope(true).uniteWith(moduleContentWithDependenciesScope)` so dependency steps are found.
- **Keep the `<!-- Plugin description -->` markers in README.md** — `build.gradle.kts` extracts that block into the published plugin manifest.

## Branch naming

`<type>/<short-kebab-description>`

- `feat` — new functionality
- `fix` — bug fix
- `test` — adds or fixes tests in the codebase
- `refactor` — code restructuring with no behaviour change
- `docs` — documentation only
- `chore` — CI, build, or tooling changes

## PR title format

Pull request titles should follow this format:

`<prefix>: <message>`

- Extract the prefix from the PR's branch name (e.g., branch `test/my-feature` → prefix `test`)
- Lowercase only the first letter of the message
- Example: `feat: migrate to IntelliJ Platform Gradle Plugin 2.x and target GoLand 2026.1`
- This convention applies to PR titles only — commit messages do not follow this format

## Reference

- **Reference plugin**: `cucumber-java` in [JetBrains/intellij-plugins](https://github.com/JetBrains/intellij-plugins/tree/master/cucumber-java) — use as the pattern source for test structure, fixture layout, and functional behaviour.
- **Dependent plugin**: `gherkin` in [JetBrains/intellij-plugins](https://github.com/JetBrains/intellij-plugins/tree/master/cucumber) — both the reference plugin `cucumber-java` and this plugin `cucumber-go` rely on `gherkin`

## Test conventions

- **Method naming**: classes extending a platform base (e.g. `GoCodeInsightFixtureTestCase`) discover via JUnit 3 — `test` prefix is mandatory. Pure `@Test`-annotated JUnit 4 tests drop the prefix.
- **Fixture layout**: per-test-method subdir named after `getTestName(true)` (lowercased method name minus `test`) under an area-prefixed `getTestDataPath()`. `myFixture.copyDirectoryToProject` resolves from `getTestDataPath()`, not `getBasePath()`. Mirror `simple/` for layout.
- **Highlighting fixtures** use inline `<info>`/`<error>`/`<warning>` markers; always call `testHighlighting(true, true, true)` and enable `CucumberStepInspection` before asserting unresolved-step errors.
- **Bug-fix PRs include a regression test** that would have caught the bug.
