# Compile and run
javac Simulator.java

# Do some asinine and pointlessly complex and inelegant
# series of grep and sed commands to sanitize input from a file
sed -e 's:#.*$::g' -e 's/[\t]*$//' input | grep -v -e '^$' > temp

# run program and feed lines
java Simulator < temp

# clean up
rm -rf *.class
rm -rf temp