# Hacker in Java

This repo contains the implementation of the last (5th) task from the
corresponding [hyperskill project][proj_on_hyperskill].

### In contrast to the [parent repo][parent_repo]
* Uses **Maven** for automation
* Canonical & rich development configuration
* Integrated **statical analysis** tools
* No integration with hyperskill ==> can't directly run their checks

## Quickstart

### Installation

Go to a chosen installation directory.
Run in bash or a compatible shell:

```bash
git clone https://github.com/tn-hyperskill/challenging-pwd_hacker-last
```

### Running the app

1. Go to the installation directory.
2. Execute in bash

```bash
cd challenging-pwd_hacker-last
./mvnw clean compile exec:java -Dexec.mainClass="hacker.app.App"
```

## Unit tests

The project has some 70-92% test coverage made of unit tests.
Can be run by executing `./mvnw test` in bash.

## Static analysis (SA)

Configuration files can be found
in [./static_anal/cfg folder](./static_anal/cfg).  
[Project Object Model](./pom.xml) integrates SA tools with Maven.

### View SA report

Open [./target/site](./target/site) directory as a static website on your local machine and navigate to "Project Reports" in navigation box.

### Generating SA report

In bash execute

```bash
./mvnw site
```

to generate {spotbugs.html, pmd.html, checkstyle.html} in
the [./target/site](./target/site) directory.

## Documentation

Note: **Documentation maybe crude** as my mentor
explicitly resigned from the good-docs requirement.

This project has documentation mainly for items with abbreviated names &
overridable methods. Other code items may not be covered.

### How does app work?

If you want to know, feel free to browse the source code
from `App.main` down the invocation tree.


[jb_academy]: https://plugins.jetbrains.com/plugin/10081-jetbrains-academy

[idea]: https://www.jetbrains.com/idea/

[proj_on_hyperskill]: https://hyperskill.org/projects/329

[parent_repo]: https://github.com/tn-hyperskill/challenging-pwd_hacker-solution
