# jorigin-common
Jaba based common tools and API

Jorigin Common

## Buid and deploy
Edit the maven settings file (by default located at ~/.m2/settings.xml) and add following entries:
```xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>${nexus.user}</username>
      <password>${nexus.password}</password>
    </server>
  </servers>
  
  <profiles>
    <profile>
      <id>ossrh</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        
      </properties>
    </profile>
  </profiles>
</settings>
```
Make then the following actions.

Clean the project:
```console
mvn clean
```
Prepare the release:
```console
mvn -Dgpg.passphrase="yourpassphrase" -Dnexus.user="your_sonatype_username" -Dnexus.password="your_sonatype_password" release:prepare
```
Perform the release:
```console
mvn -Dgpg.passphrase="yourpassphrase" -Dnexus.user="your_sonatype_username" -Dnexus.password="your_sonatype_password" release:perform
```
Update the Git project:
```console
git push --tags
git push origin master
```

In case of problem:

Step 1: Undo the release:

git reset –hard HEAD~1 (You may have to do it a second time, depending upon when the error occurred.)

git reset –hard HEAD~1

Step 2: Delete the tag.

git tag -d tagName

git push origin :refs/tags/tagName

## Changes:

### 1.0.7
  + Maven Central deploy integration
  + Java 9/10/11 compliance changes (removed calls to deprecated methods / classes)

### 1.0.5-SNAPSHTOT
  + Added public static void showImageFrame(String, Dimension, Point, BufferedImage, boolean) method to JImageFrame in order
    to quickly display images.

### 1.0.4:
  + Added List<Class<?>> listClasses(String) method within ClassUtil.
    This mlethod enable to list classes from a given package.
  + Added JImagePanel image visualization component that enable to display images and features
  + Added JImageFeature that enable to display feature within JImagePanel
  + Added JImageToolbar and JImageFrame helper classes

### 1.0.3:
  + Added ClassUtil that enable to manages classes