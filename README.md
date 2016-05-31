## Goals - What does this do ##

A scalable, extendable Web-Application using Java EE technologies managed via OSGi.
Using the following technologies:


- Jersey 2 providing JAX-RS REST services
- Narayana (JBoss Transaction Manager) providing JTA transactions
- Hibernate providing JPA persistence
- Wildfly as hosting application server
- Karaf as hosting container
- Standalone hosting using Apache Felix as OSGi-Container

This setup also illustrates how to deploy the same functionality in an App Server or self-hosted or in a Karaf/ServiceMix environment.

OSGi-wise I use

- The newest and bestest OSGi-spec
- Declarative Services using annotations
- bnd via the bnd-maven-plugin

Bonus Features: 
JTA/JPA without persistence.xml
Our application can also run inside a Java EE 7 container with DI managed by CDI

## Why ##

Java and the JVM is here to stay. There may be better languages, but Java is often reality.

I sincerely believe that for an organisation that wants to build maintainable, testable and extendable applications in a JVM environment, learning to use and apply OSGi is a potentially revolutionary step.

OSGi is, however, hard to learn. It is very well specified and as such documented by itself, but as with many mosty 'enterprisey' technologies, the scope is much too broad and deep to be grasped by reading and imitating some stackoverflow and blog entries. There is also lots if legacy code and outdated approaches out there - OSGi is quite old!

I also don't believe in (too) opinionated and proprietary setups. Bndtools, Enroute, Eclipse, PDE might be great tools - but if they are mandatory for my build I won't use them.

It boils down to what I know, and what I believe is trivially introduced in any Java Shop - maven and its plugin architecture.

Maven is a great build tool, and together with bnd it is, for me where I stand right now, the future of Java development.

## Project Organization ##

Our project is organized in a two-level folder hierarchy. On each level there is a README explaining the current level.

On this highest level of organisation we have our reactor-pom (declaring all modules needed for a build) and four folders in which we organize our modules:

- Feature
- App
- Bundle
- Wrapped

### Bundles ###

The core functionality of our application, separated across multiple bundles - high cohesion, low coupling apply here.
Bundles depend on/relate to each other and thirdparty-libraries never (ideally) directly, but via Api-Jars.

### Apps ###

How we run our functionality. We need to start the OSGi Framework, collect and load our bundles, configure our application and somehow connect with the world.

### Features ###

These modules just collect bundles which are necessary to provide certain high-level features - like persistence, rest services, consoles to control our apps at runtime.
This is conceptionally related to Karaf-Features - just implemented using pure Maven.

### Wrapped ###

More often than not we must depend on functionality that is available in Jars which are not properly built as bundles - for whatever reasons. We can wrap these as bundles - most of the time easily, sometimes with more effort.

Remark

Features and Wrapped might as well live in seperate repositories, for convenience and illustration I keep them here.

## Versioning ##

There are several versioning schemes

Features are versioned individually - incrementing the version manually when needed 
Wrapped Jars are versioned individually - using the base jar's version with a suffix
Our Application has its own version number - managed by the maven release plugin
Our Application API has its own version - effectively controlling application bundle compatibility

Semantic versioning applies.


