/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.jfrog.bintray.gradle.BintrayExtension
import make.getBuildJdk
import make.getBuildOS
import make.getBuildTime
import make.getUsername
import mvn.configurePublishing
import net.researchgate.release.ReleaseExtension

buildscript {
    repositories {
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath(BuildPlugins.gradlePluginPublishPlugin)
        classpath(BuildPlugins.gradleReleasePlugin)
        classpath(BuildPlugins.gradleBintrayPlugin)
    }
}

plugins {
    java
    signing
    `maven-publish`

    // id("com.jfrog.bintray") version Versions.gradleBintrayVersion
    id("net.researchgate.release") version Versions.gradleReleaseVersion
}

tasks.withType<Wrapper> {
    group = "build setup"
    description = "The Gradle Wrapper is the preferred way of starting a Gradle build."
    gradleVersion = System.getProperty("gradle.version")
    distributionType = Wrapper.DistributionType.ALL
}

val projectInceptionYear: String by extra { "2019" }
val projectURL: String by extra { "https://github.com/digimono/codegen" }

val projectNamesToPublish = listOf(
    "mybatis-codegen"
)

val projectsToPublish: List<Project> by extra {
    subprojects.filter {
        projectNamesToPublish.contains(it.name)
    }
}

val gradlePluginProjects: List<Project> by extra {
    subprojects.filter {
        it.name.endsWith("-gradle-plugin")
    }
}

allprojects {
    group = "io.github.digimono"

    description = "MyBatis Generator Plugin"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    val javaVersion = JavaVersion.toVersion(Versions.javaVersion)

    apply {
        plugin("java")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks {
        withType<JavaCompile> {
            options.isDeprecation = true
            options.encoding = Build.buildSourceEncoding
            options.compilerArgs.addAll(arrayOf("-Xlint:unchecked", "-parameters"))
        }

        withType<Jar> {
            afterEvaluate {
                manifest {
                    attributes(
                        "Manifest-Version" to "1.0",
                        "X-Compile-Source-JDK" to Versions.javaVersion,
                        "X-Compile-Target-JDK" to Versions.javaVersion,
                        "Build-Time" to getBuildTime(),
                        "Specification-Version" to "1.0",
                        "Specification-Title" to project.name,
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to project.version,
                        "Implementation-Vendor-Id" to project.group,
                        "Built-By" to getUsername(),
                        "Build-Jdk" to getBuildJdk(),
                        "Build-OS" to getBuildOS(),
                        "Created-By" to "Gradle ${gradle.gradleVersion}"
                    )
                }
            }
        }

        withType<Javadoc> {
            options {
                encoding = Build.buildJavadocEncoding
                isFailOnError = false
            }

            (options as? StandardJavadocDocletOptions)?.also {
                it.charSet = Build.buildJavadocEncoding
                it.isAuthor = true
                it.isVersion = true

                if (JavaVersion.current().isJava9Compatible) {
                    it.addBooleanOption("html5", true)
                }
            }
        }
    }
}

configure(projectsToPublish) {

    // val isGradlePlugin: Boolean by extra { gradlePluginProjects.contains(this) }

    apply {
        plugin("signing")
        plugin("maven")
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
        plugin("net.researchgate.release")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
        // from(sourceSets["main"].allJava)
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks[JavaPlugin.JAVADOC_TASK_NAME])
        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    }

    tasks {
        artifacts {
            add("archives", sourcesJar)
            add("archives", javadocJar)
        }

        whenTaskAdded {
            if (this.name.contains("signMavenJavaPublication")) {
                this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
            }
        }

        afterReleaseBuild {

        }
    }

    publishing {
        configurePublishing(
            project,
            "mavenJava",
            true,
            listOf(sourcesJar, javadocJar)
        )
    }

    configure<BintrayExtension> {
        configureBintray(project, this, "mavenJava")
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["mavenJava"])
    }

    release {
        customizeRelease(this)
    }
}

configure(gradlePluginProjects) {

    apply {
        plugin("signing")
        plugin("maven")
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
        plugin("net.researchgate.release")

        plugin("groovy")
        plugin("java-gradle-plugin")
        plugin("com.gradle.plugin-publish")
    }

    dependencies {
        compile(gradleApi())
        compile(localGroovy())

        testCompile(gradleTestKit())
        testCompile(TestLibs.junit)
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks[JavaPlugin.JAVADOC_TASK_NAME])
        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    }

    val groovydocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("groovydoc")
        from(tasks[GroovyPlugin.GROOVYDOC_TASK_NAME])
        dependsOn(GroovyPlugin.GROOVYDOC_TASK_NAME)
    }

    tasks {
        artifacts {
            add("archives", sourcesJar)
            add("archives", javadocJar)
            add("archives", groovydocJar)
        }

        whenTaskAdded {
            if (this.name.contains("signPluginMavenPublication")) {
                this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
            }
        }

        afterReleaseBuild {

        }
    }

    publishing {
        configurePublishing(
            project,
            "pluginMaven",
            false,
            listOf(sourcesJar, javadocJar, groovydocJar)
        )
    }

    configure<BintrayExtension> {
        configureBintray(project, this, "pluginMaven")
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["pluginMaven"])
    }

    release {
        customizeRelease(this)
    }
}

fun customizeRelease(extension: ReleaseExtension) {
    extension.tagTemplate = """v${"$"}version"""
    extension.pushReleaseVersionBranch = false
    extension.preTagCommitMessage = "[gradle-release-plugin] prepare release "
    extension.tagCommitMessage = "[gradle-release-plugin] release "
    extension.newVersionCommitMessage = "[gradle-release-plugin] prepare for next development iteration "
}

fun configureBintray(project: Project, extension: BintrayExtension, publication: String) {
    extension.user = project.property("bintray.user") as String?
    extension.key = project.property("bintray.key") as String?

    extension.dryRun = false
    extension.publish = true

    extension.setPublications(publication)

    extension.pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = project.name

        userOrg = "digimono"
        websiteUrl = projectURL
        githubRepo = "digimono/codegen"
        vcsUrl = "https://github.com/digimono/codegen.git"
        desc = project.description
        publicDownloadNumbers = true

        setLabels("java", "mybatis", "mybatis-generator", "code-generator", "gradle-plugin")
        setLicenses("Apache-2.0")

        version(delegateClosureOf<BintrayExtension.VersionConfig> {
            name = project.version as String
            desc = project.description
            vcsTag = "v${project.version}"

            gpg(delegateClosureOf<BintrayExtension.GpgConfig> {
                sign = true
                passphrase = project.property("signing.password") as String?
            })

            mavenCentralSync(delegateClosureOf<BintrayExtension.MavenCentralSyncConfig> {
                sync = true
                user = System.getProperty("oss.username")
                password = System.getProperty("oss.password")
            })
        })
    })
}
