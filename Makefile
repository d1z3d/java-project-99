setup:
	./gradlew wrapper --gradle-version 8.6

clean:
	./gradlew clean

build:
	./gradlew clean build --stacktrace

install:
	./gradlew installDist

buildProd:
	./gradlew build --args='--spring.profiles.active=production'

run-dist:
	./build/install/app/bin/app

run:
	./gradlew run

test:
	./gradlew test jacocoTestReport

lint:
	./gradlew checkstyleMain

check-deps:
	./gradlew dependencyUpdates -Drevision=release

report:
	./gradlew jacocoTestReport
