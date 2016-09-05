# Product ChangeLog

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
