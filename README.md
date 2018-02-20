[![Build Status](https://travis-ci.org/JackWhite20/slug-java.svg?branch=master)](https://travis-ci.org/JackWhite20/slug-java)
[![codecov](https://codecov.io/gh/JackWhite20/slug-java/branch/master/graph/badge.svg)](https://codecov.io/gh/JackWhite20/slug-java)

# slug-java
Java implementation of my interpreted programming language SLUG (Simple Language).

The language is currently under heavy development and syntax and other language related things may change. 

Feature and bug-fix requests are welcome.

# Features

- [ ] Basic structure
- [ ] Functions
- [ ] Variables (int, string, bool)
- [ ] Scopes
- [ ] Boolean expression (myInt == otherInt, "test" == "test", 4 > 3)
- [ ] Conditionals (if, for, while)
- [ ] Internal functions (WriteLine, ReadLine, Random, etc.)
- [ ] Function calls
- [ ] Classes (new instance creation, member variables, auto member variable constructor)
- [ ] Inline string variables (string s = "Hello $myOtherString")
- [ ] Inline string expression evaluation (string s = "Number is ${2 + 9}")

# Example GuessIt game

The GuessIt game is a simple CLI game where the user needs to guess a random generated number from 1-100. If the guessed number is too small, the game prints that and the other way around until correct number has been found.

![slug-guess-it](https://github.com/JackWhite20/slug-java/blob/master/slug-guess-it.png)


# TDD (Test Driven Development)

The goal is to get as near as possible to a 100% test coverage and to test every feature while it is beeing developed.

### License

Licensed under the  Apache License, Version 2.0.
