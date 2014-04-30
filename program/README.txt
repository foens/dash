Generate Intellij project:
gradlew idea

Run tests:
gradlew test

Set library path such that z3 can be loaded (also in set it in run configuration in IntelliJ):
-Djava.library.path=libs/;C:\Windows\System32