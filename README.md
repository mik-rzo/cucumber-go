# Cucumber Go

<!-- Plugin description -->
<p>Cucumber Go plugin provides support for Cucumber testing tools with step definitions written in Go.</p>
<h2>Features</h2>
<ul>
  <li>Go to step definition</li>
  <li>Syntax highlighting</li>
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

1. From the project root, build the plugin with:

   ```sh
   ./gradlew clean buildPlugin
   ```

2. In GoLand, go to <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>.
3. Select the generated `.zip` archive from `cucumber-go/build/distributions/`.

