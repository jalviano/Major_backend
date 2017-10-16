#!/bin/sh

MAJOR_HOME="../../../"

echo "- Running Major without mutation"
$MAJOR_HOME/bin/javac triangle/Triangle.java

echo
echo
echo "- Running Major with mutation"
$MAJOR_HOME/bin/javac -XMutator:ALL triangle/Triangle.java

echo
echo
echo "- Compiling test case (config.jar has to be on the classpath!)"
$MAJOR_HOME/bin/javac -Xlint:unchecked -cp .:$MAJOR_HOME/config/config.jar:$MAJOR_HOME/lib/junit-4.11.jar Main.java

echo
echo
echo "- Executing test case (config.jar has to be on the classpath!)"
java -cp .:$MAJOR_HOME/config/config.jar:$MAJOR_HOME/lib/junit-4.11.jar Main arg1 arg2