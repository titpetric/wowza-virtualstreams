Getting started
===============

Requirements
------------

To compile VirtualStreams plugin for Wowza, you need to provide the following
software:

  - Java run time environment (JRE)
  - Java development kit (JDK)
  - Ant (Java "make")

To install most of the required packages do this for Ubuntu/Debian:

  # apt-get install ant openjdk-6-jdk

This should install the required packages and dependencies for you.


Building the project
--------------------

Just run "ant" in the base folder of the project, where build.xml is located.

This will build a "wms-plugin-virtualstreams.jar" file in the lib/ folder.


Installation
------------

You must copy this file into your Wowza installation lib folder, usually
located under /usr/lib/WowzaMediaServer/lib. You can then use the plugin
in accordance with the documentation.


