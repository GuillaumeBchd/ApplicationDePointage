# ProjetS6Java
Application permettant de gérer les pointages d'employés (+ simulateur de pointages) en réseau.

Troisemaine Colin - Bouchard Guillaume

Important : Pour changer l'ip de la centrale que connait la pointeuse, il faut le faire dans le main de TimeTracker (donc TimeTracker.java dans le package main) et renseigner ligne 22 l'adresse ip de la centrale.

Notre projet se trouve en un seul projet Eclipse car elles utilisent des classes communes. 
Les méthodes mains se trouvent dans un package à part prénommé "mains". Elles se nomment "Central.java" et "TimeTracker.java"
Les autres classes sont réparties selon le modèle MVC dans des packages "Modèle", "Controleur" et "Vue". Certaines de ces classes sont dans des sous packages de ces trois packages principaux. 
A coté du dossier "src" où se trouvent nos différentes classes, doit se trouver un dossier "ressources" dans lequel se trouve les quatres fichiers de sauvegardes de nos applications. 
Ainsi pour charger une sauvegarde, il suffit de se rendre dans ce dossier et de remplacer le fichier par une autre version précédemment créée.
A noter que cette opération doit s'effectuer pendant que l'application est éteinte au risque de de perdre le fichier de sauvegarde.

Lorsque le projet est exporté en .jar ou .exe :
Pour utiliser un fichier de sauvegarde, placez dans le même fichier contenat le jar/exe un fichier nommé "ressources" contenant les fichiers "data_centrale.txt", "data_timetracker.txt", "parameters_centrale.txt" et "parameters_timetracker.txt".
