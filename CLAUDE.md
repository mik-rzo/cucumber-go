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

## Git workflow

- Never commit directly to main — always create a feature branch first.
- Never push directly to main — always raise a PR.
- Use `git mv` for renames.
- Prefer non-interactive autosquash for fixups; do not amend commits without confirming first.

## Branch naming

`<type>/<short-kebab-description>`

- `feat` — new functionality
- `fix` — bug fix
- `test` — adds or fixes tests in the codebase
- `refactor` — code restructuring with no behaviour change
- `docs` — documentation only
- `chore` — CI, build, or tooling changes

Always agree the branch name with the user before checking out and starting work.

## Commit and PR title format

Commit messages and PR titles should follow this format:

`<prefix>: <message>`

- Extract the prefix from the branch name (e.g., branch `test/my-feature` → prefix `test`)
- Lowercase only the first letter of the message
- Example: `feat: migrate to IntelliJ Platform Gradle Plugin 2.x and target GoLand 2026.1`

## Naming

- Align fixture, scenario, and test names with the surrounding directory naming conventions.
- Avoid generic placeholder names.

## Reference

- **Reference plugin**: `cucumber-java` in [JetBrains/intellij-plugins](https://github.com/JetBrains/intellij-plugins/tree/master/cucumber-java) — use as the pattern source for test structure, fixture layout, and functional behaviour.
- **Dependent plugin**: `gherkin` in [JetBrains/intellij-plugins](https://github.com/JetBrains/intellij-plugins/tree/master/cucumber) — both the reference plugin `cucumber-java` and this plugin `cucumber-go` rely on `gherkin`

## Test conventions

- **Method naming**: classes extending a platform base (e.g. `GoCodeInsightFixtureTestCase`) discover via JUnit 3 — `test` prefix is mandatory. Pure `@Test`-annotated JUnit 4 tests drop the prefix.
- **Fixture layout**: per-test-method subdir named after `getTestName(true)` (lowercased method name minus `test`) under an area-prefixed `getTestDataPath()`. `myFixture.copyDirectoryToProject` resolves from `getTestDataPath()`, not `getBasePath()`. Mirror `simple/` for layout.
- **Highlighting fixtures** use inline `<info>`/`<error>`/`<warning>` markers; always call `testHighlighting(true, true, true)` and enable `CucumberStepInspection` before asserting unresolved-step errors.
- **Bug-fix PRs include a regression test** that would have caught the bug.
- **Before fixing a bug**, write a failing test that demonstrates it — confirm it fails for the right reason before implementing the fix.
- **After fixing**, re-run the test and confirm it is green.
- **Do not assert on `package` or module import paths** in sandbox tests — the sandbox does not resolve them reliably.
