#!/bin/sh

MAJOR_HOME="../../../"
OUTPUT="../production/"

SRC_PATH="triangle/"
TST_PATH="triangle/test/"
BIN_PATH="../bin/"
SRC_FULL=${BIN_PATH}/${SRC_PATH}
TST_FULL=${BIN_PATH}/${TST_PATH}

LOG_FILEPATH="mutants.log"
MUTATED_CLASSPATH="../bin/triangle/Triangle.class"
TEST_CLASSPATH="../bin/triangle/test/TestSuite.class"

echo "- Compiling test suite..."
${MAJOR_HOME}/bin/javac -Xlint:none -sourcepath ${BIN_PATH} -cp .:${SRC_PATH}/*.class:${MAJOR_HOME}/lib/junit-4.11.jar ${TST_FULL}/*.java

echo
echo
echo "- Running Major without mutation..."
${MAJOR_HOME}/bin/javac -Xlint:none ${SRC_FULL}/*.java

echo
echo
echo "- Running Major with mutation..."
${MAJOR_HOME}/bin/javac -Xlint:none -XMutator="$MAJOR_HOME/mml/tutorial.mml.bin" ${SRC_FULL}/*.java

echo
echo
echo "- Compiling backend..."
${MAJOR_HOME}/bin/javac -Xlint:none -cp .:${MAJOR_HOME}/config/config.jar:${MAJOR_HOME}/lib/junit-4.11.jar -d ${OUTPUT} Main.java

echo
echo
echo "- Performing analysis..."
java -cp ${OUTPUT}:${MAJOR_HOME}/config/config.jar:${MAJOR_HOME}/lib/junit-4.11.jar Main ${LOG_FILEPATH} ${MUTATED_CLASSPATH} ${TEST_CLASSPATH}
