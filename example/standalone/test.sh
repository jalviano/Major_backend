#!/bin/sh

MAJOR_HOME="../.."

echo
echo "=================================================================="
echo "Compiling and testing the project"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile test.xml project.compile.tests

echo
echo "=================================================================="
echo "Running major standalone backend unit tests"
echo "=================================================================="
echo
${MAJOR_HOME}/bin/ant -buildfile test.xml coverage
