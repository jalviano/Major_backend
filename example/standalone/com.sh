#!/bin/sh

MAJOR_HOME="../.."

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
${MAJOR_HOME}/bin/ant -buildfile com.xml run.analysis
# ${MAJOR_HOME}/bin/ant -buildfile com.xml mutation.test
