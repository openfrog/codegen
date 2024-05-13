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

import groovy.util.Node
import groovy.util.NodeList

plugins {
    `java-library`
    `maven-publish`
    signing
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {

    repositories {
        maven {
            val releasesRepoUrl: Any
            val snapshotsRepoUrl: Any

            val publishToBuildDirectory: Boolean? by rootProject
            if (publishToBuildDirectory != null && publishToBuildDirectory == true) {
                releasesRepoUrl = layout.buildDirectory.dir("repos/releases")
                snapshotsRepoUrl = layout.buildDirectory.dir("repos/snapshots")
            } else {
                releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

                credentials {
                    username = System.getProperty("oss.username")
                    password = System.getProperty("oss.password")
                }
            }

            setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                val projectURL: String? by rootProject
                val projectInceptionYear = "2019"
                val projectDescription = if (project.description.isNullOrEmpty()) project.name else project.description
                val isBom = project.name.endsWith("-bom")

                groupId = project.group as String
                artifactId = project.name
                version = project.version as String

                name.set(project.name)
                description.set(projectDescription)
                inceptionYear.set(projectInceptionYear)
                url.set(projectURL)

                packaging = if (isBom) {
                    "pom"
                } else {
                    "jar"
                }

                scm {
                    connection.set("scm:git:https://github.com/digimono/codegen.git")
                    developerConnection.set("scm:git:https://github.com/digimono/codegen.git")
                    url.set("https://github.com/digimono/codegen")
                }

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("digimono")
                        name.set("Eric Yang")
                        email.set("yanjuyq@gmail.com")
                        url.set("https://github.com/digimono")
                    }
                }

                // http://maven.apache.org/guides/introduction/introduction-to-optional-and-excludes-dependencies.html
                withXml {
                    fun Any?.asNode() = this as Node
                    fun Any?.asNodeList() = this as NodeList

                    val root = asNode()

                    if (isBom) {
                        val coreProjects: List<Project> by rootProject

                        val dependencies = root
                            .appendNode("dependencyManagement")
                            .appendNode("dependencies")

                        coreProjects.forEach {
                            val dependency = dependencies.appendNode("dependency")
                            dependency.appendNode("groupId").setValue(it.group)
                            dependency.appendNode("artifactId").setValue(it.name)
                            dependency.appendNode("version").setValue(it.version)
                        }
                    } else {
                        val dependencies = root["dependencies"].asNodeList().getAt("*") as List<*>
                        dependencies.forEach {
                            val dependency = it.asNode()
                            val scope = dependency["scope"].asNodeList().text()
                            val artifactId = dependency["artifactId"].asNodeList().text()

                            if (scope == "runtime") {
                                if (project.configurations.findByName("implementation")?.allDependencies?.none { dep -> dep.name == artifactId } == false) {
                                    (dependency["scope"].asNodeList() as List<*>).forEach { dep ->
                                        dep.asNode().setValue("compile")
                                    }
                                    dependency.appendNode("optional", true)
                                }
                            }
                        }

                        (root["dependencyManagement"].asNodeList() as List<*>).forEach {
                            root.remove(it.asNode())
                        }
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

tasks {
    withType<Javadoc> {
        if (JavaVersion.current().isJava9Compatible) {
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
        }
    }

    withType<Sign> {
        val isReleaseVersion = !version.toString().endsWith("SNAPSHOT")

        onlyIf {
            isReleaseVersion
        }
    }

    whenTaskAdded {
        if (this.name.contains("signMavenJavaPublication")) {
            this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
        }
    }
}
