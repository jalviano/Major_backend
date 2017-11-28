#!/bin/sh

MAJOR_HOME="../.."
OUTPUT="production/"

OUTPUT_FULL_KILL_MATRIX=true
OFFSET=1
FACTOR=2
LOG_FILEPATH="mutants.log"
TEST_DIRECTORY="bin/org/apache/commons/lang3/"

echo
echo "=================================================================="
echo "Compiling backend"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile com.xml compile.analysis

echo
echo "=================================================================="
echo "Compiling and mutating project"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile com.xml clean init compile

echo
echo "=================================================================="
echo "Compiling tests"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile com.xml compile.tests

echo
echo "=================================================================="
echo "Run tests without mutation analysis"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile com.xml test

echo
echo "=================================================================="
echo "Run tests with mutation analysis"
echo "=================================================================="
echo
# java -cp ${OUTPUT}:${MAJOR_HOME}/config/config.jar:${MAJOR_HOME}/lib/junit-4.11.jar Main ${OUTPUT_FULL_KILL_MATRIX} ${OFFSET} ${FACTOR} ${LOG_FILEPATH} ${TEST_DIRECTORY}
${MAJOR_HOME}/bin/ant -buildfile com.xml mutation.test
