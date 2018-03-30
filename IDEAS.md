# Idee e guide

## 1. Come trovare i client configurati come 'server'? (Marco)
Quando un client si configura come server non è in grado di comunicare il suo indirizzo ip ad altre istanze del gioco, non essendoci un server centralizzato avente un indirizzo ip statico.
La soluzione a cui ho pensato è di utilizzare un broadcast UDP al quale risponderanno solamente quei client che si sono configurati come server. Ovviemente il metodo presentato è utile solo nel caso in cui il server sia nella rete locale.

Il client manderà un messaggio in broadcast con il seguente contenuto `json` (o comunque simile a questo):

```json
{
    "header": {
        "message-type": "server-discovery.request"
    },
    "body": {
        "client-ip": "%{L'indirizzo ip del server}",
        "waiting-on-port": "%{La porta sulla quale il client si aspetta una risposta}"
    }
}
```

Il server risponderà in TCP, sulla porta specificata nel `body` della richiesta, con il seguente messaggio json:

```json
{
    "header": {
        "message-type": "server-discovery.response"
    },
    "body": {
        "server-ip": "%{L'indirizzo ip del server}",
        "listening-on-port": "%{La porta sulla quale il server è in ascolto}",
        "server-name": "%{Il nome del server}"
    }
}
```

## 2. Sincronizzazione degli oggetti automatica (Marco)
Pensavo che per evitare di dover scrivere sempre lo stesso codice nelle classi da sincronizzare sarebbe stato bello creare delle annotazioni personalizzate con le quali marcare le classi e gli attributi da sincronizzare.

La classe da sincronizzare potrebbe avere il seguente aspetto:

```java
@NetworkObject
public class Player {

    @NetworkSynchronize(NetworkSynchronizationSide.Client)
    private Point _coordinate;

    @NetworkSynchronize(NetworkSynchronizationSide.Server)
    private int _goldCount;
}
```

Le classi annotate con `@NetworkObject` verranno sincronizzate in automatico con il server.
Gli attributi annotati con `@NetworkSynchronize()` saranno gli unici ad essere sincronizzati. Il parametro (il cui valore di default è `NetworkSynchronizationSide.Server`) serve a stabilire chi, tra server e client, "possegga" il dato, questo in modo che non sia possibile modificare una variabile lato client quando è controllata dal server (all'atto pratico impedisce all'utente di modificare i valori in memoria di alcuni campi, perché il valore presente su server è quello che fa fede).
> Esempio: Se il valore dell'oro in possesso dal giocatore fosse "posseduo" dal client potrebbe essere modificato (modificando il valore in memoria) dall'utente permettendogli di spendere l'oro senza esserne effettivamente in possesso. Se invece il valore dell'oro fosse "posseduto" dal server questa problematica non si presenterebbe.