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

// In the `buildSrc` we can easily provide code for the Gradle build script itself, namely we create custom Gradle tasks here.
// Learn about this feature here: https://zeroturnaround.com/rebellabs/using-buildsrc-for-custom-logic-in-gradle-builds/
// and here: https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
