# How to create a new Joala Module

Here are the steps to perform if you want to create a new Joala module. Let's assume you want to create a new
module named `dolor`.

1. **Create a folder** below `parent/` named `dolor`
2. **Use this initial POM** (a typical IDE will create it for you automatically):

    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <!--
      ~ Copyright ...
      -->

    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <parent>
        <artifactId>joala-parent</artifactId>
        <groupId>net.joala</groupId>
        <version>0.3.0-SNAPSHOT</version>
      </parent>

      <artifactId>joala-dolor</artifactId>

    </project>
    ```
3. **Set a name** prefixed with *Joala*. Here: *Joala Dolor*.
    This name will be used especially on site generation.
4. **Set a description.** It is recommended to place it into `CDATA`:

    ```xml
    <description><![CDATA[
      Provides some dolor for ipsum and sit amet... Lorem!
    ]]></description>

   The description will be automatically rendered in the Maven site as description of this module. (Default behavior
   of `maven-site-plugin`.)
5. **Create a Java package** corresponding to the module name. Thus here `net.joala.dolor` is recommended.
    This eases locating classes in module hierarchy a lot.