

execute: TP1.class 
	java -Xmx2g -cp . TP1

run: execute simulateur

simulateur: ramassage.jar
	java -jar ramassage.jar

TP1.class: TP1.java
	javac TP1.java

clean:
	rm *.class

.PHONY: simulateur execute clean
