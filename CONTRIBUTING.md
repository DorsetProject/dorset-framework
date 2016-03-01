# How to contribute

We like pull requests. For small changes, fork our repository and make the pull request. For larger changes, please open an [issue](https://github.com/DorsetProject/dorset-framework/issues) so that we can discuss your proposed changes. If you're not familiar with pull requests, Github has some good [documentation](https://help.github.com/articles/using-pull-requests/) on this.

## Unit Tests

Please run the unit tests before making a pull request: `mvn test`. If you are adding new functionality, include unit tests with your pull request.

## Coding Style Guidelines

We follow the [Google Java Style](https://google.github.io/styleguide/javaguide.html) with the exception of using 4 spaces instead of 2 for indentation. We additionally use the [Jode javadoc style](http://blog.joda.org/2012/11/javadoc-coding-standards.html) for javadoc. Pull requests are checked for Google style compliance using Travis CI. You can also run the check manually with `mvn checkstyle:check`.

## Contributor License Agreement

You need to sign the [Contributor License Agreement](https://cla-assistant.io/DorsetProject/dorset-framework) before we can accept any pull requests from you. You will receive a reminder on your pull request if you have not signed it.
