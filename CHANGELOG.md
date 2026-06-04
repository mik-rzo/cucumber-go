<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# cucumber-go Changelog

## [Unreleased]

- fix: stop recognizing regexp.Compile as a ctx.Step pattern by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/69
- fix: replace deprecated UsageType string constructor with lambda by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/70

## [1.1.0] - 2026-06-01

- feat: support regexp.MustCompile/Compile as ctx.Step pattern argument by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/44
- feat: implement step rename for quoted, backtick, and regexp.MustCompile patterns by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/48
- fix: unanchored regex patterns not matched correctly by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/49
- feat: target IntelliJ IDEA Ultimate alongside GoLand and include compatibility from 2024.2 onwards by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/55
- fix: step completion mid-word no longer leaves trailing suffix or breaks capture group caret by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/56

## [1.0.1] - 2026-05-25

- fix: restrict plugin to GoLand by declaring `com.intellij.modules.goland` dependency by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/35

## [1.0.0] - 2026-05-25

- feat: migrate to IntelliJ Platform Gradle Plugin 2.x and target GoLand 2026.1 by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/1
- fix: select step container in the same directory as the feature file by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/18
- fix: creating step definition invalidates existing step definitions by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/19
- fix: restrict step lookup to the directory of the feature file's package by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/20

[Unreleased]: https://github.com/mik-rzo/cucumber-go/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/mik-rzo/cucumber-go/compare/v1.0.1...v1.1.0
[1.0.1]: https://github.com/mik-rzo/cucumber-go/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/mik-rzo/cucumber-go/commits/v1.0.0
