plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("checkstyle")
	id("com.github.spotbugs") version "6.0.7"
	id("com.diffplug.spotless") version "6.25.0"
	jacoco
}

springBoot {
	mainClass.set("com.example.registrationservice.RegistrationServiceApplication")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.liquibase:liquibase-core")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	
	compileOnly("org.projectlombok:lombok")
	compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.3")
	
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
	
	runtimeOnly("org.postgresql:postgresql")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("com.h2database:h2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	
	// Kafka
	implementation("org.springframework.kafka:spring-kafka")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
		html.required = true
	}
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.8".toBigDecimal()
			}
		}
	}
}

spotbugs {
	toolVersion = "4.8.3"
	ignoreFailures = false
	excludeFilter = file("config/spotbugs/exclude.xml")
}

tasks.spotbugsMain {
	reports {
		create("html") {
			required = true
		}
	}
}

tasks.check {
	dependsOn(tasks.jacocoTestCoverageVerification)
}

checkstyle {
	toolVersion = "10.12.7"
	configFile = file("config/checkstyle/checkstyle.xml")
	maxWarnings = 0
	isShowViolations = true
}

tasks.withType<Checkstyle>().configureEach {
	reports {
		xml.required = false
		html.required = true
	}
}

spotless {
	java {
		googleJavaFormat()
		trimTrailingWhitespace()
		endWithNewline()
		importOrder("java", "javax", "jakarta", "org", "com", "")
		removeUnusedImports()
	}
}
