# IntelliJ Setup

1. At start of IntelliJ, browse to the root `pom.xml` and open it as project.
2. Enable checkstyle:
  - Install the [IntelliJ CheckStyle-IDEA Plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea). It can be found via plug-in repository  
  (File > Settings > Plugins > Marketplace; **Mac**: IntelliJ IDEA > Preferences > Plugins > Marketplace).  
    ![checkstyle](graphics/checkstyle.png)
    
  - Install the CheckStyle-IDEA Plugin, click "Apply" and restart the project upon request.
  - Repeat the previous steps for the Lombok Plugin
  - Open the Settings (by pressing <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>; **Mac**: <kbd>command</kbd> + <kbd>,</kbd>)
  - Go to "Settings > Checkstyle".
  - Click on "+" under Configuration File and add `checkstyle.xml`. It is located in `docs/dev/config/IntelliJ`. Confirm.
  
    ![checkstyle](graphics/checkstyle-config.png)
    
  - Activate the settings and confirm:
  
    ![checkstyle](graphics/checkstyle-active.png)  
   
3. Enable Lombok:
  - Install the [IntelliJ Lombok-IDEA Plugin](https://plugins.jetbrains.com/plugin/6317-lombok). It can be found via plug-in repository  
      (File > Settings > Plugins > Marketplace; **Mac**: IntelliJ IDEA > Preferences > Plugins > Marketplace).
    ![checkstyle](graphics/lombok-config.png)
  - Install the CheckStyle-IDEA Plugin, click "Apply" and restart the project upon request.    
  
  
4. Configure the code style (Source: <https://youtrack.jetbrains.com/issue/IDEA-61520#comment=27-1292600>)  
  - Open the Settings (by pressing <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>; **Mac**: <kbd>command</kbd> + <kbd>,</kbd>)  
  - Go to "Editor > Code Style"  
  - Click on the gear icon (right of "Scheme:")  
  - Click "Import Scheme"  
  - Choose "IntelliJ IDEA code style XML"
  - Navigate to `intellij-idea-code-style.xml`. It is located in `docs/dev/config/IntelliJ IDEA`.  
  - Click "Apply"
  - Click "OK"  
  - Click "Close"  
  
5. Setup code headers to be inserted automatically  
    ![copyright-profile](graphics/copyright-profile.png)  
  - Open the Settings (by pressing <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>; **Mac**: <kbd>command</kbd> + <kbd>,</kbd>)  
  - Go to "Editor > Copyright > Copyright Profiles"  
  - Click the "+"  
  - Name "Atlas"  
  - Copyright text from [CodeHeaders](CodeHeaders.md)  
  - Click "Apply"
  - Go to "Editor > Copyright > Formatting"
  - Adjust copyright formatting settings
    
     ![checkstyle](graphics/formatting-copyright.png)
       - Change to `Use block comments` with `Prefix each line`
       - Set `Relative Location` to `Before other comments`
       - Set `Separator before`to `80` and `Separator after` to `81`
  - Go to "Editor > Copyright"
  - Set "Atlas" as Default project copyright
  - Click "Apply"
  
6. Configure Git to handle line endings
  - Insert the following commands in your console:  
  **For Windows**: `git config --global core.autocrlf true`  
  **For Mac/Linux**: `git config --global core.autocrlf input`
  
7. Set up database:
    - Open Terminal in IntelliJ
    ![checkstyle](graphics/terminal.png)
    - Navigate to directory  ``..\.docker\``
    - Insert the following commands ``docker-compose up -d``
    - Open "Database"
    ![checkstyle](graphics/open-database.png)
    - Click the "+"
    - Go to "Data Source > PostgresSQL"
    - Enter username and password: "postgres" (both)
    ![checkstyle](graphics/postgres-setup.png)
    - Click "Apply"
    
8. Save to database:
    - Open "Database"
    ![checkstyle](graphics/open-database.png)
    - Go to "postgres@localhost > databases > postgres > schemas > public > tables"
    - Right-Click on "tables"
    ![checkstyle](graphics/dump-data.png)
    - Go to "Dump Data to File(s)"
    - Click on "SQL Inserts"
    - Click on the "Output directory"
    - Choose the target sql file
    - Hit "Enter"

