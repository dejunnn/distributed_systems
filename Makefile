
.PHONY : all
all: centralserver field sensor

.PHONY : centralserver
centralserver:
	@echo "Building Central Server..."; \
	cd common; \
	javac -g -classpath . *.java; \
	cd ../centralserver; \
	javac -g -classpath .:.. *.java; \
	cd ..; \

.PHONY : field
field:
		@echo "Building Field Unit..."; \
		cd common; \
		javac -g -classpath . *.java; \
		cd ../field; \
		javac -g -classpath .:.. *.java; \


.PHONY : sensor
sensor:
	@echo "Building Sensor..."; \
	cd common; \
	javac -g -classpath . *.java; \
	cd ../sensor; \
	javac -g -classpath .:.. *.java; \


.PHONY : clean
clean:
	rm sensor/*.class field/*.class common/*.class centralserver/*.class
