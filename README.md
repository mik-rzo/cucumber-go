# Cucumber Go

<!-- Plugin description -->
<p>Cucumber Go plugin provides support for Cucumber testing tools with step definitions written in Go.</p>
<h2>Features</h2>
<ul>
  <li>Go to step definition</li>
  <li>Step completion</li>
  <li>Find usages</li>
  <li>Step renaming</li>
  <li>Step definition creation</li>
  <li>Gutter icon on step definitions</li>
  <li>Automatic godog framework detection</li>
  <li>Run configurations for features and scenarios</li>
</ul>
<!-- Plugin description end -->

## Installation

The plugin works with both GoLand and IntelliJ IDEA Ultimate, and requires the following plugins to be installed:

- **[Go](https://plugins.jetbrains.com/plugin/9568-go)** — bundled with GoLand; install manually on IntelliJ IDEA Ultimate.
- **[Gherkin](https://plugins.jetbrains.com/plugin/9164-gherkin)** — install manually on both GoLand and IntelliJ IDEA Ultimate.

### From the JetBrains Marketplace

The plugin is available in the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/31954-cucumber-for-go/). Install it
directly in GoLand or IntelliJ IDEA Ultimate from <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd>,
then search for _Cucumber for Go_.

### Manual installation

1. From the project root, build the plugin with:

   ```sh
   ./gradlew clean buildPlugin
   ```

2. In GoLand, go to <kbd>Settings</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>.
3. Select the generated `.zip` archive from `cucumber-go/build/distributions/`.
