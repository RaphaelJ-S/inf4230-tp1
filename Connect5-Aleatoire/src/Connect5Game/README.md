# Travail pratique 2 - INF4230 - gr.10

## Identification des membres de l'équipe

1.  - Code permanent : JACR26038907
    - Nom : Jacob-Simard
    - Prénom : Raphaël

2.  - Code permanent : STRF06089401
    - Nom : Stromei
    - Prénom : Francis

3.  - Code permanent : GAUA83030100
    - Nom : Gauquier
    - Prénom : Antoine

## Identification des fichiers ajoutés

- AlphaBetaConfig : Paramètres de recherche pour l'algorithme
- ConditionArret : Interface de condition d'arrêt de l'algorithme
  - ConditionArretRegleJeu : Implémentation avec les règles du jeu et la profondeur dans l'algorithme.
- EvaluationChoix : Interface d'algorithme de recherche de décision.
  - EvaluationChoixAleatoire : Implémentation d'algorithme de décision aléatoire.
  - EvaluationChoixAlphaBeta : Implémentation d'algorithme de décision Alpha-Beta.
- Utilite : Interface de fonction de calcul d'utilité
  - UtiliteTousJoueurs : Implémentation selon tous les coups de tous les joueurs.

## Modifications des fichiers existants

- Grille : Ajout de la variable d'instance 'joueurCourant' pour suivre le bon joueur dans l'algorithme.
