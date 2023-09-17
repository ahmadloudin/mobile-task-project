FAILLE : 
N'importe qui à le ID d'une tâche d'un autre utilisateur 
peut changer le pourcentage de cette tâche.
Donc, on peut essayer avec chaque id pour trouver le bon poste.
Ex: 1, 2, 3
Et finalement on trouve ce qu'on veut chercher.
Pour moi c'était le id 19 qui à le nom 
"Tache pour l'étudiant au post 3"
Dans cette appel : 
http://10.10.39.24/api/progress/{id}/{value}

EXPLOIT :
Donc, on peut essayer avec chaque id pour trouver le bon poste.
Ex: 1, 2, 3
Et finalement on trouve ce qu'on veut chercher.
Pour moi c'était le id 19 qui à le nom
"Tache pour l'étudiant au post 3"
Dans le corps d'une appel http /api/progress/{id}/{value},
on ajoute le id (19) de la tâche de l'utilisateur et on change
son pourcentage (56). Peut importe qui est t'inscrit et
connecter aura la possibilité de modifier la tâche
d'un autre utilisateur.

CORRECTIF :
Donc, on peut essayer avec chaque id pour trouver le bon poste.
Ex: 1, 2, 3
Et finalement on trouve ce qu'on veut chercher.
Pour moi c'était le id 19 qui à le nom
"Tache pour l'étudiant au post 3"
Il faudra ajouter une mesure de sécurité specifiquement
quand l'utilisateur demande de changer son pourcentage
pour qu'il vérifie si c'est bien sa tâche à lui. Ex : 
Vérifier son JSESSIONID
