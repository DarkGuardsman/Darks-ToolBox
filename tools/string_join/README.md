# String Join
Simple utility to take in a list of line seperate strings and join them together as a single comma seperate line. Useful for testing SQL quarries by hand that need large lists of data.

## Arguments:
* Select file (file path, arg 0, required)
* Filter unqiue (true/false, arg 1, required)
* Set entry limit (integer, arg 2, optional)

## Arg Example
See features for what args exist

normalJarStuff filePath true/false integer

normalJarStuff input.txt true

normalJarStuff input.txt true 100

normalJarStuff input.txt false
