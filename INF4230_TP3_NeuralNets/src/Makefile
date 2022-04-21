

run: compile execute

compile: Main.java
	javac Main.java

execute: Main.class
	java -cp . Main -e 500 --lrate 0.3 --ratio 0.7 --architecture 4x4x7x1 --dataset iris

gui: InterfaceGraphique.java
	javac InterfaceGraphique.java
	java -cp . InterfaceGraphique

clean:
	rm */*.class
	rm *.class

.PHONY: clean execute run