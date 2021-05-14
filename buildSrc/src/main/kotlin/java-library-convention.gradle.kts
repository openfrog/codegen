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

plugins {
    `java-library`
}

val isBom = project.name.endsWith("-bom")

java {
    val javaVersion = JavaVersion.toVersion(Versions.javaVersion)

    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    withJavadocJar()
    withSourcesJar()
}

dependencies {
    if (!isBom) {
        constraints {

        }

        compileOnly(Libs.lombok)
        annotationProcessor(Libs.lombok)
        // testAnnotationProcessor(Libs.lombok)

        testImplementation(enforcedPlatform(TestLibs.junit5Bom))
        testImplementation(TestLibs.junit)

        testImplementation("org.junit.platform:junit-platform-launcher")
        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    }
}

tasks {
    withType<JavaCompile> {
        options.isWarnings = true
        options.isDeprecation = true
        options.encoding = Build.buildSourceEncoding
        options.compilerArgs.addAll(
            arrayOf(
                "-Xlint:unchecked", "-parameters"
            )
        )
    }

    withType<Jar> {
        afterEvaluate {
            manifest {
                attributes(
                    "Manifest-Version" to "1.0",
                    "Created-By" to "Gradle ${gradle.gradleVersion}",
                    "Specification-Title" to project.name,
                    "Specification-Vendor" to "",
                    "Specification-Version" to "1.0",
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                    "Implementation-Vendor" to "",
                    "Implementation-Vendor-Id" to project.group,
                    "Build-Jdk" to Manifest.buildJdk,
                    "Build-OS" to Manifest.buildOS,
                    "Build-Time" to Manifest.buildTime,
                    "X-Compile-Source-JDK" to Versions.javaVersion,
                    "X-Compile-Target-JDK" to Versions.javaVersion
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

    test {
        onlyIf {
            !System.getProperty("skip.tests", "true")!!.toBoolean()
        }

        ignoreFailures = true

        useJUnitPlatform {
            excludeTags("slow", "ci")
            includeEngines("junit-jupiter")
            excludeEngines("junit-vintage")
            failFast = true
        }
    }
}
