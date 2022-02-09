

execute: compiler 
	java -Xmx2g -cp . TP1

run: execute simulateur

simulateur: ramassage.jar
	java -jar ramassage.jar

compiler: Successeur.class Route.class Ramassage.class ParseurH.class Heuristique.class HeuristiqueV1.class HeuristiqueV2.class HeuristiqueV3.class Etat.class Emplacement.class Colis.class But.class AEtoile.class TP1.class

TP1.class: TP1.java
	javac TP1.java

Successeur.class : Successeur.java
	javac Successeur.java

Route.class: Route.java
	javac Route.java

Ramassage.class: Ramassage.java
	javac Ramassage.java

ParseurH.class: ParseurH.java
	javac ParseurH.java

Heuristique.class: Heuristique.java
	javac Heuristique.java

HeuristiqueV1.class: HeuristiqueV1.java
	javac HeuristiqueV1.java

HeuristiqueV2.class: HeuristiqueV2.java
	javac HeuristiqueV2.java

HeuristiqueV3.class: HeuristiqueV3.java
	javac HeuristiqueV3.java

Etat.class: Etat.java
	javac Etat.java

Emplacement.class: Emplacement.java
	javac Emplacement.java

Colis.class: Colis.java
	javac Colis.java

But.class: But.java
	javac But.java

AEtoile.class: AEtoile.java
	javac AEtoile.java



clean:
	rm *.class

.PHONY: simulateur execute clean compiler run
