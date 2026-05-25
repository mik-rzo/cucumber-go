<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# cucumber-go Changelog

## [Unreleased]

- fix: restrict plugin to GoLand by declaring `com.intellij.modules.goland` dependency

## [1.0.0] - 2026-05-25

- feat: migrate to IntelliJ Platform Gradle Plugin 2.x and target GoLand 2026.1 by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/1
- fix: select step container in the same directory as the feature file by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/18
- fix: creating step definition invalidates existing step definitions by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/19
- fix: restrict step lookup to the directory of the feature file's package by @mik-rzo in https://github.com/mik-rzo/cucumber-go/pull/20

[Unreleased]: https://github.com/mik-rzo/cucumber-go/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/mik-rzo/cucumber-go/commits/v1.0.0