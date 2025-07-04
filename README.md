# jorigin-common
Java based common tools and API. The maine functionnalities provided by this library are:
- Some File / Streams utilities
- A dynamic plugin framework for making an application modular
- Some interface to handle objects states
JCommon also include tools and utilities for JavaFX and SWING frameworks. 

## Integration
JCommon can be used as a Maven dependency or as a standalone library.

### Maven
JCommon is available at [Maven Central](https://search.maven.org/search?q=org.jorigin.jcommon). 

To import the library, add the following parts to the maven project:
```xml

<!-- You can update the properties section with Jeometry version -->
<properties>
  <jcommon.version>2.0.1</jcommon.version> 
</properties>

<!-- The JCommon core module that contains basics utilities -->
<dependency>
  <groupId>org.jorigin</groupId>
  <artifactId>jcommon-core</artifactId>
  <version>${jcommon.version}</version>
</dependency>

<!-- (Optional) The JCommon module dedicated to JavaFX -->
<dependency>
  <groupId>org.jorigin</groupId>
  <artifactId>jcommon-jfx</artifactId>
  <version>${jcommon.version}</version>
</dependency>

<!-- (Optional) The JCommon module dedicated to SWING -->
<dependency>
  <groupId>org.jorigin</groupId>
  <artifactId>jcommon-swing</artifactId>
  <version>${jcommon.version}</version>
</dependency>
```

### Standalone
JCommon can be used as standalone library by integrating the jars provided by a [release](https://github.com/jorigin/jcommon/releases) to the classpath. 

## Usage
For a quick overwiew ot the library, please refer to the [Getting Started](https://github.com/jorigin/jcommon/wiki/Getting-Started).

For more information, tutorials and advanced uses, please check the [Wiki](https://github.com/jorigin/jcommon/wiki).

## Changes:

see [changelog](CHANGELOG.md) for details.
