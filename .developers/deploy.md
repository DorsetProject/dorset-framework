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

4. Releases are deployed to the staging server with
    ```
    mvn release:clean release:prepare -P release
    mvn release:perform -P release
    ```

5. Approve the release using the web interface at https://oss.sonatype.org/
    Login, select Staging Repositories, find dorset at the bottom of the list, click the Release button at the top

