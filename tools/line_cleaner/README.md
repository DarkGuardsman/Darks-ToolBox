# Line Cleaner
Simple utility to take in a list of line seperate strings and process them to allow them to be feed into other programs. Meant to act as a replacement for knowing or building regex for simple tasks.

## Arguments:
* Select file (file path, arg 0, required)
* Commands (name[context], arge 1...n)

## Commands
* Remove - removes an exact string
* Remove-until - removes everything up to string, including string input
* Remove-after - removes everything after string, including string input
* Trim - removes leading and trailing spaces
* More commands can easily be added as needed


Each command will run in order it is listed. This will result in a modified string being processed by the next command.

### Example:
Input: File.js:345 Function#subFunction: 34.947998046875ms

This is a string from a list of data recorded about console.time(label) console.timeEnd(label) in javascript. The goal is to process it to only the whole number to allow generating performance data graphs.

* Command: remove-until[:]
* Output: 34.947998046875ms
* Command: remove-after[.]
* Output: 34

Regex could easily do this as well using a number start and then a until '.'. However, this is easier to understand and use.

## Arg Example
See features for what args exist

normalJarStuff filePath remove[string] remove-until[:] remove-after[.]
