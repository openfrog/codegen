plugins {
    groovy
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.1"
}

description = "Mybatis Generator Gradle Plugin"

dependencies {
    compile(gradleApi())
    compile(localGroovy())

    testCompile(gradleTestKit())
    testCompile(TestLibs.junit)
}

gradlePlugin {
    plugins {
        create("mbGeneratorPlugin") {
            id = "io.lollipok.mybatis-generator-plugin"
            implementationClass = "io.lollipok.gradle.plugin.MBGeneratorPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lollipok/codegen.git"
    vcsUrl = "https://github.com/lollipok/codegen.git"

    (plugins){
        "mbGeneratorPlugin"{
            displayName = "Mybatis Generator Plugin"
            description = "${project.description}"
            tags = listOf("mybatis", "generator", "mybatis generator")
            version = "${project.version}"
        }
    }
}
