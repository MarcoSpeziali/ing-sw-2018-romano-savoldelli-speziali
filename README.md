# ing-sw-2018-romano-savoldelli-speziali

Prova finale di ingegneria del software 2018

## 1. Membri del gruppo

Nome | Cognome | Matricola
-----|---------|----------
Marco | Speziali | 843852
Davide | Savoldelli | 845874
Luca | Romano | 843618

## 2. Coverage dei test

Modulo | Coverage (classi) | Coverage (metodi) | Coverage (istruzioni)
-------|-------------------|-------------------|----------------------
[lib*](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/lib/) | 59% | 49% | 50%
[client**](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/client/) | 0% | 0% | 0%
[server***](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/server/) | 53% | 37% | 35%

*: non sono state escluse le classi relative alla parte web

**: il client contiene unicamente classi che necessitano di un riscontro visivo (GUI/CLI) e di una connessione con il server tramite socket/RMI.

***: non sono state escluse le classi relative alla gestione della lobby e del match, che richiedono una connessione socket/RMI e contengono timer e thread.

## 3. UML iniziale

- [lib](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/lib/docs/initial-lib-uml.pdf)
- [client](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/client/docs/initial-client-uml.pdf)
- [server](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/server/docs/initial-server-uml.pdf)

## 4. UML finale

- [lib](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/lib/docs/final-lib-uml.pdf)
- [client](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/client/docs/final-client-uml.pdf)
- [server](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/server/docs/final-server-uml.pdf)

## 5. Funzionalità implementate

- [x] Regole complete
- [ ] CLI
- [x] GUI
- [x] RMI
- [x] Socket
- [ ] Single player
- [ ] Persistenza
- [x] Carte schema dinamiche
- [x] Partite multiple

## 6. Scelte implementative

Per avviare il server è necessario avere installato PostgreSQL. Una volta installato è sufficiente eseguire questi comandi:

```postgresql
CREATE DATABASE sagrada;
CREATE USER sagrada WITH ENCRYPTED PASSWORD 'jjn6sjI2F34~cicv=aHB]vjqLVw3-CgSbEgFSq}@QMhuuL)DF)zzE$Y5X&FFHGYs';
GRANT ALL PRIVILEGES ON DATABASE sagrada TO sagrada;
```

E successivamente eseguire le query di creazione consultabili [qui](https://github.com/MarcoSpeziali/ing-sw-2018-romano-savoldelli-speziali/tree/master/apps/server/docs/database-creation.sql).
O tramite IntelliJ o tramite il comando (`bash`):

```bash
psql -d sagrada -a -f $PROJECT_ROOT/apps/server/docs/database-creation.sql
```

