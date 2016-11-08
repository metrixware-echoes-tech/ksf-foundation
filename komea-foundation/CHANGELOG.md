# Product ChangeLog

### 0.4.5 November 08 2016
  - Mise à jour version Komea Dashboard
  - [#21022] Amélioration du message d'avertissement à la déletion d'un projet + limitation de la suppression aux admin Redmine
  - [foreman-java-scparams] correction d'un problème lorsque les groupes d'hôtes Foreman ont un parent

### 0.4.4 October 24 2016
  - Hotfix: Correction du fichier application.properties dont les défauts avaient été changés par erreur
  - Hotfix: Correction de l'auth Jenkins
  - Hotfix: Correction des variables acceptées dans les templates Jenkins
  - [#20994] Feature: ajout de la clé Nexus qui avait été omise

### 0.4.3 October 17 2016
  - [#20994] Feature: Stocker en base les clés d'un projet
  - [#20965] Feature: Choix d'une image de VM
  - [#20964] Feature: Choix d'un template de job Jenkins
  - [#20982] Feature: Gestion des suppressions (Jobs / Cibles / Environnements)
  - [#20963] Feature: Prévoir un ascenseur dans les widget de l'onglet principal
  - [#20966] Feature: Une tâche affectée ne doit être visible/démarrable que par la personne à qui elle est affectée au niveau de l'overview
  - [#20962] Feature: Actualiser l'onglet principal des projets (overview) lorsque l'on clique dessus
  - [#20084] Feature: Gestion de l'api key des utilisateurs dans Redmine
  - [#20991] Bugfix: Terminer une release "exemple" supprime toutes les release "exemple" de la bdd
  - [#20968] Bugfix: Les utilisateurs dont le login est en MAJUSCULE dans LDAP font exploser la création de projet dans la partie Foreman
  - [#20601] Bugfix: Supprimer le contournement dans Foundation pour la connexion SSO dans Jenkins
  - [#20546] Bugfix: Lorsque l'on utilise un "_" pour instancier une VM, Foreman renvoie une erreur
  - [#20329] Bugfix: Exception lors de l'afichage des informations d'un projet
  - [#20314] Bugfix: Pas de changement d'URL au changement d'onglet
  - [#20197] Bugfix: Revoir l'encodage des noms de projets Jenkins
  - [#20155] Bugfix: Le tableau présentant les tickets de Redmine n'est pas responsive

### 0.4.2 July 31 2016
  - [#20582] Feature: Permettre d'accéder / de publier des fichiers externes au jar à travers foundation
  - [#20558] Bugfix: Indisponibilité de l'adresse IP utilisée par Foreman lors de l'instantiation d'une nouvelle VM.
  - [#20545] Bugfix: Lors de la création d'un projet, le groupe d'hôte est bien créé mais génère une erreur qui empêche la création du rôle.
  - [#20340] Change: Mise à jour de la documentation.

### 0.4.1 July 07 2016
  - [#20246] Feature: Configuration de cibles dans Foreman à partir d'une image de VM (Non ISO)
  - [#20190] Bugfix: La configuration de Foundation pour la création d'un host est ignorée
  - [#20277] Bugfix: Lors de l'instanciation d'un Host à l'aide d'une image de VM une exception se produit de manière aléatoire
  - [#20233] Bugfix: Impossible de créer une VM à partir d'une Image Debian configurée avec "Données utilisateur"
  - [#20213] Bugfix: La widget Jenkins affiche Failed to retrieve build history après avoir terminé une feature/release
  - [#20196] Bugfix: Les VMs crées par Foundation ne sont pas accessible en dehors du réseau ksf.local 
  - [#20312] Bugfix: Jenkins : besoin de cliquer sur login pour activer le SSO
  - [#20236] Bugfix: Démarrage d'une livraison : message d'erreur "Failed to create a Redmine ticket for the release."
  - [#20217] Bugfix: Le ticket créé lors du démarrage d'une release n'a pas le bon tracker
  - [#20211] Bugfix: La page d'un projet est trop longue à charger
  - [#20187] Bugfix: Régression : les versions dans redmine ne sont plus visibles dans foundation
