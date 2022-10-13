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

import io.digimono.gradle.plugin.MBGeneratorExtension

plugins {
    id("io.github.digimono.mybatis-generator") version "0.0.7"
}

val mybatisGenerator by configurations

dependencies {
    mybatisGenerator(project(":mybatis-codegen"))
    mybatisGenerator(Libs.mybatisGeneratorCore)
    mybatisGenerator(Libs.h2)
    mybatisGenerator(Libs.mysql)

    implementation("org.mybatis:mybatis:3.5.10")
    implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.4.0")
}

configure<MBGeneratorExtension> {
    configFile = "${project.projectDir}/src/main/resources/META-INF/mybatis/mybatis-generator.xml"
}

