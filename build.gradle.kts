import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.2.2.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	kotlin("plugin.serialization") version "1.3.61"
}

group = "io.pemassi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {

	//Spring Boot Libraries
	implementation("org.springframework.boot:spring-boot-starter-batch")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	//Kotlin Helper Libraries
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.14.0")

	//RESTful Helper Libraries
	implementation("com.squareup.retrofit2:retrofit:2.7.0") {
		exclude(module = "okhttp")
	}
	implementation("com.squareup.retrofit2:converter-gson:2.7.0")

	//OkHttp Libraries
	implementation ("com.squareup.okhttp3:okhttp:4.2.2")
	implementation ("com.squareup.okhttp3:logging-interceptor:4.2.2")

	//Json
	implementation("com.google.code.gson", "gson", "2.8.5")

	//Yaml
	//implementation("org.yaml", "snakeyaml", "1.25")
	implementation("com.charleskorn.kaml:kaml:0.15.0")

	//Unit Test Libraries
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("org.springframework.batch:spring-batch-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
