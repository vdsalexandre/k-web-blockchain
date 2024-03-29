import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jjwtVersion: String = "0.11.5"
val gsonVersion: String = "2.10.1"
val junitVersion: String = "5.9.2"
val mockitoVersion: String = "4.11.0"
val springMockkVersion: String = "4.0.0"
val qrcodeKotlinVersion: String = "3.3.0"
val bouncyCastleVersion: String = "1.70"
val mockkVersion: String = "1.13.3"

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "com.vds.wishow"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.google.code.gson:gson:$gsonVersion")
	implementation("io.github.g0dkar:qrcode-kotlin-jvm:$qrcodeKotlinVersion")


	compileOnly("io.jsonwebtoken:jjwt-api:$jjwtVersion")

	runtimeOnly("org.bouncycastle:bcprov-jdk15on:$bouncyCastleVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
	runtimeOnly("io.jsonwebtoken:jjwt-gson:$jjwtVersion")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
	testImplementation("org.mockito:mockito-core:$mockitoVersion")
	testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
	testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
