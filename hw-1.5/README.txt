Packaged jar can be used in two ways:

1) To run tests on one test class use compiled jar package with a
fully qualified class name argument, e.g. java -jar MyUnit.jar my.package.ClassName;
2) To run tests on all classes in a package use the package name and an asterisk at the end,
e.g. java -jar MyUnit.jar my.package.*