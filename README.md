# Cucumber Go

<!-- Plugin description -->
Cucumber Go plugin provides support for Cucumber testing tools with step definitions written in Go.
<!-- Plugin description end -->

## Features

- Go to step definition
- Syntax highlighting
- Step completion
- Find usages
- Step definition generation
- Gutter icon on step definitions
- Automatic godog framework detection
- Run configurations for features and scenarios

## Installation

1. From the project root, build the plugin with:

   ```sh
   ./gradlew clean buildPlugin
   ```

2. In GoLand, go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>.
3. Select the generated `.zip` archive from `cucumber-go/build/distributions/`.

