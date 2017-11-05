#!/bin/sh

MAJOR_HOME="../../../"
SRC_BIN="../bin/"
OUTPUT="../production/"

LOG_FILEPATH="mutants.log"
MUTATED_CLASSPATH="../bin/triangle/Triangle.class"

TEST_CLASSPATH="../bin/triangle/test/TestSuite.class"

echo "- Running Major without mutation..."
${MAJOR_HOME}/bin/javac ${SRC_BIN}/triangle/Triangle.java

echo
echo
echo "- Running Major with mutation..."
${MAJOR_HOME}/bin/javac -XMutator:ALL ${SRC_BIN}/triangle/Triangle.java

echo
echo
echo "- Compiling test case..."
${MAJOR_HOME}/bin/javac -Xlint:unchecked -cp .:${MAJOR_HOME}/config/config.jar:${MAJOR_HOME}/lib/junit-4.11.jar -d ${OUTPUT} Main.java

echo
echo
echo "- Executing test case..."
java -cp ${OUTPUT}:${MAJOR_HOME}/config/config.jar:${MAJOR_HOME}/lib/junit-4.11.jar Main ${LOG_FILEPATH} ${MUTATED_CLASSPATH} ${TEST_CLASSPATH}
