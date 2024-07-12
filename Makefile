main:
	@echo Make what? Build or clean?

build:
	mvn clean compile assembly:single

clean:
	mvn clean
