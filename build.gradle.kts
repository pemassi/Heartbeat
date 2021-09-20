import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.21"
	kotlin("plugin.spring") version "1.5.21"
	kotlin("plugin.serialization") version "1.5.21"
}

group = "io.pemassi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven(url = "https://jitpack.io")
    maven(url = "https://plugins.gradle.org/m2/")
}

configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports(delegateClosureOf<io.spring.gradle.dependencymanagement.dsl.ImportsHandler> {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.2")
    })
}

dependencies {

	//Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-validation")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    //Spring Cloud
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")

    //Database
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.h2database:h2")

	//Kotlin Helper
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//	implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")

	//RESTful Helper
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("com.squareup.retrofit2:retrofit:2.9.0") {
		exclude(module = "okhttp")
	}
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Monitor
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

	//Helper
	implementation("com.github.pemassi:pemassi-kotlin-extensions:1.0.4")

	//OkHttp
	implementation ("com.squareup.okhttp3:okhttp:4.9.1")
	implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")

	//Json
	implementation("com.google.code.gson", "gson", "2.8.5")

	//Yaml
	//implementation("org.yaml", "snakeyaml", "1.25")
	implementation("com.charleskorn.kaml:kaml:0.35.3")

	//Unit Test
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.batch:spring-batch-test")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
