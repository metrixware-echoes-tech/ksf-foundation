# Fonctionnalités principales de l'outil

* Gestion des utilisateurs
* Gestion des rôles
* Gestion des applications et des versions
* Gestion des composants et des versions
* Gestion des types de composants
* Gestion de projets
* Gestion des requirements/exigences
* Gestion des suites de tests
* Gestion des pas dans les cas de tests
* Génération rapport d'exigences
* Génération cahier de recette pour un projet
* Génération cahier de recette pour une application
* Génération cahier de recette pour un composant
* Gestion des campagnes de tests
* Déroulement d'un test dans une campagne
* Visualisation de l'état d'une campagne de tests
* Génération d'un rapport de bilan de campagne
* Association tests automatisés et exécution d'une campagne
* Compatibilité avec une GED
* Module d'importation des données de testlink

Contraintes :
* Création d'une campagne de test à partir d'une liste de features
* Partage de features entre produits (par référence ou par duplication) ou gérer des branches par produit (en plus du versioning)
* Performance sur la modélisation des graphes (utilisation de git ?)

Plus tard :
 * Module Roadmap
 * Module Calculette de charge / Abaque
 * Ajouter FK  
 * Problème de responsive
 * La partie application n'est pas finie.


Bug  : duplication de role (setActive false)
Bug : Transitions de pages
Bug : Champ login qui devrait être figé lors de l'édition (voire pas du tout modifiable)


version 0.1.0
---------------

* Gestion des utilisateurs
 * [DONE] créer un utilisateur 
 * [DONE] activer un utilisateur 
 * [DONE] désactiver un utilisateur 
 * [DONE] supprimer un utilisateur 
 * [DONE] éditer le profil d'un utilisateur 
 * changer le password d'un utilisateur
* Authentification (Interne, social, ldap)
 * [DONE] S'authentifier via le système interne 
 * [DONE] S'authentifier via le système LDAP 
 * [DONE] S'authentifier via le système Social 
 * [DONE] Se déconnecter 
 * [DONE] Remember me 
* Gestion des rôles
 * [DONE] Créer un nouveau rôle 
 * [DONE] Définir les permissions d'un rôle  
 * [DONE] Supprimer un rôle
 * [DONE] Dupliquer un rôle
 * [DONE] Définir un rôle par défaut
 * Protéger un rôle. 
* Système d'audit (audit trails)
 * Logger une trace
 * Récupérer la liste des traces
 * Récupérer la liste des traces sur une période

version 0.2.0
------------------

* Gestion des applications et des versions

version 0.3.0
------------------

* Gestion des requirements
 * Visualisation des requirements (arbre)
  * Visualisation d'un requirement (un seul)
  * Edition
  * Déplacement
  * Copie 
  * Suppression
  * Renommage
 * Gestion des personnes (approval) qui ont validé la requirement
 * Gestion des commentaires sur un requirement (comme un attachment)
 * Gestion des liens (hypertextes)
 * Gestion des verrous


version 0.4.0
------------------

* Gestion des cas de tests

version 0.5.0
------------------

* Gestion des pas dans les cas de tests

version 0.6.0
------------------

* Gestion des composants et des versions

version 0.7.0
------------------

* Gestion des projets


version 0.8.0
------------------

* Gestion des campagnes de tests

version 0.9.0
------------------

* Déroulement d'un test dans une campagne
* Visualisation de l'état d'une campagne de tests

version 1.0.0
------------------

* Déroulement d'un test dans une campagne
* Visualisation de l'état d'une campagne de tests

