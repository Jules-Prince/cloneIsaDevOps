# isa-devops-22-23-team-a-23a

### Builder les images Docker

Pour démarrer le projet, il faut d'abord build les images docker avec le script suivant :     
```shell
./build-all.sh
```

### Pousser les images sur Docker Hub
```shell
# TAG
docker tag devopsteama/backend:latest devopsteama/backend:2.0
docker tag devopsteama/cli:latest devopsteama/cli:2.0
docker tag devopsteama/iswypls:latest devopsteama/iswypls:2.0
docker tag devopsteama/bank:latest devopsteama/bank:2.0
# PUSH
docker push devopsteama/backend:2.0
docker push devopsteama/cli:2.0
docker push devopsteama/iswypls:2.0
docker push devopsteama/bank:2.0
```

### Lancer les containers
Ensuite, il faut lancer les containers avec la commande suivante après avoir démarré Docker Desktop :         
```shell
docker-compose up -d
```

### Tester le projet
Pour lancer des commandes sur la cli, il faut lancer la commande suivante :     
```shell
docker attach cli
```

Pour lancer le fichier `demo.txt`, il faut lancer la commande suivante dans le shell de la cli :     
```shell
shell:> script createConsumer.txt
shell:> script createPartnerSociety.txt
shell:> script scenarioAdvantageWithValidityParking.txt
shell:> script scenarioSondage.txt
shell:> script scenarioStatistics.txt
```


