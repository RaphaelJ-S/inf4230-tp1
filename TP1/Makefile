
run: execute simulateur

simulateur: ramassage.jar
	java -jar ramassage.jar

TP1.class: TP1.java
	javac TP1.java

execute: TP1.class
	java -Xmx2g -cp . TP1

clean:
	rm TP1.class

.PHONY: simulateur execute clean
