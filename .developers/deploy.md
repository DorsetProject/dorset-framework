Deploying to Sonatype's maven central repository
================================================
1. Add your Sonatype OSSRH user credentials to your maven settings.xml file
    ```
    <servers>
      <server>
        <id>ossrh</id>
        <username>username</username>
        <password>password</password>
      </server
    </servers>
    ```
    Restrict access to the settings.xml to your user account only.

2. Create and distribute a public key. Details at http://central.sonatype.org/pages/working-with-pgp-signatures.html

    You may need to authenticate before running maven with something like:
    ```
    gpg --output /dev/null --sign [some file]
    ```

3. Snapshots can be deployed with
    ```
    mvn clean deploy -P release
    ```

4. Update version in README

5. Releases are deployed to the staging server with
    ```
    mvn release:clean release:prepare -P release
    mvn release:perform -P release
    ```
