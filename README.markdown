#DASH for Java [![Creative Commons Attribution 4.0 International License](http://i.creativecommons.org/l/by/4.0/88x31.png "Creative Commons Attribution 4.0 International")](http://creativecommons.org/licenses/by/4.0/)
This is a Java implementation of a subset of [DASH][1] developed by Kasper Føns and Jacob Hougaard during their master thesis, [A study of the DASH algorithm for software property checking][2], created while studying Computer Science at [Aarhus University](http://cs.au.dk/).

##What is DASH?
[DASH][1] is an algoritmh that can check if a program satisﬁes a given safety property. DASH both concretly and symbolically executes the program being tested. A unique feature of the DASH algorithm is that it uses only test generation operations, and it reﬁnes and maintains a sound program abstraction as a consequence of failed test generations. DASH was developed by Microsoft for analyzing device drivers written in the programming language C.

##Implementation
The implementation supports interprocedural analysis of integer programs. There are numerous examples in the [tests directory][3]. It does not support pointers (as [DASH][1] does), objects, doubles and many other features.

###Requirements for running
- [Java SDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 64 bit
- Windows 64bit

Windows 64bit and a 64bit version of Java is required since [Z3](https://z3.codeplex.com/), a theorem prover, was built for Windows 64bit. If you build the Z3 binary yourself, the project should be able to run on Linux as well. The Z3 binaries in the repository is built from the `unstable` branch of Z3 with the commit hash `b1b349f49662d16c87060f82ddcf8204ce4f7b`. Loading of libz3 and dependencies are performed in [Native.java](program/src/main/java/com/microsoft/z3/Native.java). Building newer versions of Z3 might require that the [Z3 JNI wrappers](program/src/main/java/com/microsoft/z3) are upgraded as well.

###Run all tests
Executing all the [tests][3] in the project can be achieved by invoking gradle: `gradlew test`. It is easy to add new tests to the project.

Graphs can be generated for each iteration of DASH by setting the `log level` to `trace` in [log4j.xml](program/src/test/resources/log4j.xml). Be prepared to take a coffee break, as graph output is not fast. Notice that [GraphViz dot](http://www.graphviz.org/) is used to generate the graphs, and the binary contained in the repository is built for Windows.

##Thesis
The thesis is written in [LaTeX](http://www.latex-project.org/) and can be built on Windows by the script `thesis/build.bat`. For convenience it is also found in compiled form: [A study of the DASH algorithm for software property checking][2].

##License
All work performed by third party is licensed under their respective licenses.

Work performed by us is licensed under a [Creative Commons Attribution 4.0 International License.](http://creativecommons.org/licenses/by/4.0/). For fun, we would appreciate if you notified us if you use this project.

  [1]: https://research.microsoft.com/pubs/102699/tse.pdf
  [2]: thesis.pdf?raw=true
  [3]: program/src/test/java/dk/au/cs/dash/test/dash
