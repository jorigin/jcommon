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
    </profile>
  </profiles>
</settings>
```
Make then the following actions.

__1. Clean the project__:
```console
mvn clean
```
__2. Prepare the release__:
```console
mvn -Dgpg.passphrase="yourpassphrase" -Dnexus.user="your_sonatype_username" -Dnexus.password="your_sonatype_password" release:prepare
```
__3. Perform the release__:
```console
mvn -Dgpg.passphrase="yourpassphrase" -Dnexus.user="your_sonatype_username" -Dnexus.password="your_sonatype_password" release:perform
```
__4. Update the Git project__:
```console
git push --tags
git push origin master
```

__5. In case of <span style="color:red">problem</span> during steps 1 to 4__:

+ 5.1: Undo the release:
```console
git reset --hard HEAD~1
```
_(You may have to do it a second time, depending upon when the error occurred.)_

+ 5.2: Delete the tag.
```console
git tag -d tagName
git push origin :refs/tags/tagName
```

## Changes:

### 1.0.10
  + Added layer handling within Image display (see JImagePanel)

### 1.0.8
  + Updated FileUtil
  + Updated ActivityMonitor
  + Added JThumbnail handling

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