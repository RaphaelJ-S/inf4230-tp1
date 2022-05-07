# Rapport pour le tp3

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

## Observations _Iris_

### Commentaires sur les différents nombres d'époques

architecture 4x4x7x1: Moyenne de 42.4/60 pour 10 tests à une époque de 10 (min 31, max 51)

architecture 4x4x7x1: Moyenne de 38.8/60 pour 10 tests à une époque de 4000 (min 14, max 58)


Nous pouvons constater que les résultats peuvent varier de façon importante dans les deux cas et que nous sommes près d'un taux d'erreur de 0.34 autant pour une époque de 10 que de 4000.

### À quoi les variations du taux d'erreur pour un même ensemble de valeurs des (hyper-)paramètres, sont-elles dues?

Les variations du taux d'erreurs dépendent des poids attribués à travers le réseau, ainsi que la valeur reçue en entrée.  En effet, ces valeurs sont les seules qui varient d'un test à l'autre.

### Qu'observez-vous au niveau de ce taux lorsque vous comparez l'effet des valeurs pré-établies à celui d'un jeu de valeurs qui en est différent sur au moins un (hyper-)paramètre?

Le changement au niveau du taux semble dépendre de l'hyper paramètre modifié, par exemple, une augmentation du taux d'apprentissage semble créer des écarts plus grands entre le taux d'échec des tests.  L'augmentation du nombre de couches cachées quant à elle aurait tendance à stabiliser davantage le taux d'échec d'un test à l'autre et il semble en être de même pour le nombre total de neurones par couche.

### Est-ce que le taux diminues systèmatiquement lorsqu'on augmente les valeurs des hyper-paramètres (couches, nombre neurones) et le nombre d'époques?

Avec une augmentation des hyper paramètres tels que le nombre de couches cachées, le nombres de neurones par couche ainsi que le nombre d'époques, le taux d'erreur d'un test à l'autre ne diminue pas systématiquement, il varie toutefois beaucoup moins d'un test à l'autre.  Encore une fois, atteint un d'un taux d'erreur d'environ 0.34

### Observation sur les tendences dans la dépendance

## Expérimentations _Seeds_

Concernant ce dataset, on doit essayer de trouver une configuration d'hyper-paramètres qui maximisent le taux de succès. Pour ce faire, on dispose de 3 valeurs que l'on peut définir : le taux d'apprentissage, le nombre d'époques et l'architecture du réseau.
Nous allons donc empiriquement déterminer des valeurs approchées des hyper-paramètres optimaux, par l'expérience, en commençant par fixer le taux d'apprentissage, le nom bre d'époques et enfin l'architecture du réseau. Les valeurs de taux de succès sont présentées en moyenne calculée à chaque fois sur 10 apprentissages faits dans les mêmes conditions.

Commençons par le taux d'apprentissage, que l'on échantillonne de 0.1 à 0.5 avec un pas de 0.1 : 

| Id  |  Architecture du réseau |  Nombre d'époques |  Taux d'apprentissage | Taux de succès moyen |
| --- | ----------------------- | ----------------- | --------------------- | -------------------  |
| 1   | 7x4x7x1                 | 500               |  0.1                  |  53.8/63 = 0.854     |
| 2   | 7x4x7x1                 | 500               |  0.2                  |  47.7/63 = 0.757     |
| 3   | 7x4x7x1                 | 500               |  0.3                  |  54.1/63 = 0.858     |
| 4   | 7x4x7x1                 | 500               |  0.4                  |  46.1/63 = 0.732     |
| 5   | 7x4x7x1                 | 500               |  0.5                  |  45.3/63 = 0.719     |

D'après ces observations, on conclut que le taux d'apprentissage 0.3 est le meilleur dans ces conditions. On observe toutefois une très grande variabilité dans les résultats obtenus : il semble donc que ce critère ne soit pas le plus déterminant dans l'objectif d'un réseau le plus fiable possible.

On va dès lors fixer le taux d'apprentissage, et essayer plusieurs valeurs de nombre d'époques afin de déterminer, encore une fois empiriquement, le meilleur nombre observé. On a graduellement sélectionné des valeurs de nombre d'époques de l'ordre de grandeur des centaines aux milliers, et avons ainsi choisi les valeurs 128, 256, 512, 1024 et 2048 pour nos observations. Voici les résultats :  

| Id  |  Architecture du réseau |  Nombre d'époques |  Taux d'apprentissage | Taux de succès moyen |
| --- | ----------------------- | ----------------- | --------------------- | -------------------  |
| 1   | 7x4x7x1                 | 128               |  0.3                  |  46.7/63 = 0.741     |
| 2   | 7x4x7x1                 | 256               |  0.3                  |  48.6/63 = 0.771     |
| 3   | 7x4x7x1                 | 512               |  0.3                  |  50.2/63 = 0.797     |
| 4   | 7x4x7x1                 | 1024              |  0.3                  |  51.1/63 = 0.811     |
| 5   | 7x4x7x1                 | 2048              |  0.3                  |  51.9/63 = 0.824     |

D'après ces résultats, on constate que plus le nombre d'époques augmente, et plus le taux de succès s'améliore. Toutefois, ces résultats sont à prendre avec précaution puisqu'ils sont calculés sur de petits échantillons et présentent encore ici une grande variance dans leurs valeurs individuelles. De ce fait, 2048 époques semble la meilleure option étant donné les expériences menées, mais ce paramètre ne semble pas non plus être le plus déterminant.

Il nous reste la possibilité de modifier maintenant l'architecture du réseau pour tenter d'améliorer son apprentissage. Nous ne changerons pas le nombre de couches en entrée et en sortie, qui dépendent directement de la formulation du problème et de ses données. On peut donc jouer sur le nombre de couches internes ainsi que leur taille. Jusqu'ici, nous avons expérimenté 2 couches internes et respectivement 4 et 7 neurones par couche. Nous pouvons essayer de modifier d'abord le nombre de couches internes puis ensuite leur taille : essayons avec 1, 2 et 3 couches internes, puis avec 5, 10 et 20 neurones par couche pour chacun des cas. On choisi donc 2048 époques et un taux d'apprentissage de 0.3. Voici les résultats : 

Une couche interne :

| Id  |  Architecture du réseau |  Nombre d'époques |  Taux d'apprentissage | Taux de succès moyen |
| --- | ----------------------- | ----------------- | --------------------- | -------------------  |
| 1   | 7x4x5x1                 | 2048               |  0.3                  |  49.2/63 = 0.781     |
| 2   | 7x4x5x1                 | 2048               |  0.3                  |  49.6/63 = 0.787     |
| 3   | 7x4x5x1                 | 2048               |  0.3                  |  45.3/63 = 0.719     |

Deux couches internes :

| Id  |  Architecture du réseau |  Nombre d'époques |  Taux d'apprentissage | Taux de succès moyen |
| --- | ----------------------- | ----------------- | --------------------- | -------------------  |
| 1   | 7x4x5x2                 | 2048               |  0.3                  |  53.1/63 = 0.843     |
| 2   | 7x4x5x2                 | 2048               |  0.3                  |  54.3/63 = 0.862     |
| 3   | 7x4x5x2                 | 2048               |  0.3                  |  50.1/63 = 0.795     |

Trois couches internes :

| Id  |  Architecture du réseau |  Nombre d'époques |  Taux d'apprentissage | Taux de succès moyen |
| --- | ----------------------- | ----------------- | --------------------- | -------------------  |
| 1   | 7x4x5x3                 | 2048               |  0.3                  |  46.6/63 = 0.740     |
| 2   | 7x4x5x3                 | 2048               |  0.3                  |  47.3/63 = 0.751     |
| 3   | 7x4x5x3                 | 2048               |  0.3                  |  45.0/63 = 0.714     |


On observe dans les trois nombres de couches une même tendance : le taux de succès moyen est le plus haut pour 10 neurones, proche de celui à 5 neurones, puis on observe enfin un taux de succès pour 20 neurones significativement plus bas. Maintenant, concernant le nombre de couches internes, on observe un maximum de taux de succès pour deux couches internes.

Ainsi, le taux de succès maximal observé est de 86.2% pour 2 couches internes de 10 neurones chacune, avec un taux d'apprentissage de 0.3 et un nombre d'époques de 2048. Ces valeurs d'hyper-paramètres sont toutefois loin d'être optimales, car basées sur une évaluation empirique lui même considéré sur un échantillonnage d'une plage de valeur qui limite forcément les résultats. On observe enfin que c'est surtout le nombre de couches internes et le nombre de neurones associés qui caractérise le plus le taux de succès de la classification, pour ce jeu de données en particulier et sur les plages de valeurs choisies pour l'échantillonnage. 
