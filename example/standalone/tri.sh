#!/bin/sh

MAJOR_HOME="../.."
OUTPUT="production/"

OUTPUT_FULL_KILL_MATRIX=true
SORT_OPTIMIZATION=true
OFFSET=1
FACTOR=2
LOG_FILEPATH="mutants.log"
TEST_DIRECTORY="bin/triangle/test/"

echo
echo "=================================================================="
echo "Compiling backend"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile tri.xml compile.analysis

echo
echo "=================================================================="
echo "Compiling and mutating project"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile tri.xml clean init compile

echo
echo "=================================================================="
echo "Compiling tests"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile tri.xml compile.tests

echo
echo "=================================================================="
echo "Run tests without mutation analysis"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile tri.xml test

echo
echo "=================================================================="
echo "Run tests with mutation analysis"
echo "=================================================================="
echo
java -cp ${OUTPUT}:${MAJOR_HOME}/config/config.jar:${MAJOR_HOME}/lib/junit-4.11.jar Main ${OUTPUT_FULL_KILL_MATRIX} ${SORT_OPTIMIZATION} ${OFFSET} ${FACTOR} ${LOG_FILEPATH} ${TEST_DIRECTORY}
# ${MAJOR_HOME}/bin/ant -buildfile tri.xml mutation.test
