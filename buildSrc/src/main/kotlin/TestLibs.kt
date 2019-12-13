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

object TestLibs {

    const val junit = "junit:junit:${Versions.junitVersion}"

    const val junit5Bom = "org.junit:junit-bom:${Versions.junit5Version}"
    const val junitPlatformLauncher = "org.junit.platform:junit-platform-launcher:${Versions.junitPlatformVersion}"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.junit5Version}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit5Version}"
    const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:${Versions.junit5Version}"

    const val apiGuardianApi = "org.apiguardian:apiguardian-api:${Versions.apiGuardianVersion}"
    const val opentest4j = "org.opentest4j:opentest4j:${Versions.opentest4jVersion}"
}
