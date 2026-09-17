# Trayce

Trayce is a task-tracking chatbot with the personality of a friendly trail guide. It helps you
record todos, deadlines, events, and notes while keeping the data between sessions.

For commands and examples, see the [Trayce User Guide](docs/README.md).

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. Run `trayce.ui.Launcher` from IntelliJ to open the Trayce GUI. Alternatively, run
   `./gradlew run` from the project directory.

## Building the application

Create the executable JAR with:

```shell
./gradlew shadowJar
```

The output is `build/libs/trayce.jar`. Run it using Java 25:

```shell
java -jar build/libs/trayce.jar
```

## Acknowledgements

This project began from the NUS CS2103T individual-project template. Its Gradle setup and JavaFX
dialog-box structure were adapted from the course's Duke project resources. See
[ACKNOWLEDGEMENTS.md](ACKNOWLEDGEMENTS.md) for details.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
