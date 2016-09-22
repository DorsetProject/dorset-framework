Eclipse Project Configuration
================================================
There are three Eclipse-specific XML files in this directory: a code style for eclipse's formatter and templates for code and for comments.
The code style is the most important file. The templates make small changes to the default Eclipse templates such as removing @author tags or inserting the copyright comment.

Importing these XML files into your Eclipse projects will make it easier to confirm to our style guide.
We recommend importing the maven modules into their own workspace so that the XML files only have to be imported once rather than once per project.

Import the Code Style
-----------------------
1. Either edit workspace preferences or edit the project properties
2. Navigate to Java > Code Style > Formatter
3. Import eclipse code style XML file in the .developers directory
    * You may need to right click and enable viewing hidden files
4. Select "Dorset" from the Active Profile options and click OK

Import the Templates
------------------------
1. Either edit workspace preferences or edit the project properties
2. Navigate to Java > Code Style > Code Templates
3. Import the template for Comments and the template for Code
4. Click OK

