

execute : compiler
	java -Xmx2g -cp . Connect5Game/Main

simulateur : Connect5Game/Connect5.jar
	java -jar Connect5Game/Connect5.jar

compiler: Connect5Game
	javac Connect5Game/*.java



clean:
	rm Connect5Game/*.class

.PHONY: simulateur execute clean compiler

