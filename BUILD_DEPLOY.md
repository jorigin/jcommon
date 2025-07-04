# Buid and deploy
In order to build an deploy a distribution of JCommon with a version X.Y.Z, all the following steps have to be performed.

## 1. Preparing Maven configuration
Edit the maven settings file (by default located at ~/.m2/settings.xml) and add following entries:
```xml
<settings>

	<!-- Maven Central publishing -->
	<!-- https://central.sonatype.org/publish/generate-portal-token/ -->
	<servers>
		<server>
			<id>central</id>
			<username>YOUR ID</username>
			<password>YOUR PASSPHRASE</password>
		</server>
	</servers>
</settings>
```

## 2. Project update and compilation

### 2.1. Code update
Before a release, some changes has to be done:
- Ensure that the `org.jorigin.Common.BUILD` variable is up to date
- Ensure that the `org.jorigin.Common.version` variable is set to "X.Y.Z"
- Ensure that the README.md file section "Usage" is up to date with new version number
- Ensure that the CHANGELOG.md file has an entry dedicated to the new version (with anticipated link)

### 2.2. Project compilation and testing
Ensuring that the project is compiling and that all related tests are passing 
```console
mvn clean
mvn compile
mvn test
```

## 3. Release preparation

### 3.1. Git commit and push
From the main directory, run:
```console
git commit -m "Release X.Y.Z"
```
Then run
```console
git push -u origin master
```

### 3.2. Maven release preparation
From the main directory, run:
```console
mvn -Dgpg.passphrase="yourpassphrase" release:prepare
```
### 4. Release perform
From the main directory, run:
```console
mvn -Dgpg.passphrase="yourpassphrase" release:perform
```
### 5. Git project update
From the main directory, run:
```console
git push --tags
git push origin master
```

### 5. Problem resolution
Actions described here have to be performed if an error has occured during previous steps 3 to 5.
## 5.1. Undo the release
From the main directory, run:
```console
git reset --hard HEAD~1
```
_(You may have to do it a second time, depending upon when the error occurred.)_

### 5.2. Delete the tag
From the main directory, run:
```console
git tag -d tagName
git push origin :refs/tags/tagName
```
