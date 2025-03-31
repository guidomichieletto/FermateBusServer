# Fermate autobus in Italia: Server
## Protocollo di comunicazione

Il protocollo di comunicazione tra client e server è basato su protocollo TCP con scambio di messaggi in formato JSON.

### Formato messaggi client -> server
Ogni messaggio spedito da un client deve avere al suo interno il campo `request` che identifica la tipologia di richiesta inviata ed il campo `id` che identifica univocamente la richiesta. Di seguito le tipologie di richieste inviabili con gli eventuali campi aggiuntivi da inserire:

|   Tipo richiesta   |                                                Descrizione                                                |                                                   Esempio                                                    |
|--------------------|-----------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| ping               | Chiede la conferma della connessione al server                                                            | {"id": 0, "request": "ping"}                                                                                 |
| query              | Richiede la lista degli oggetti di tipologia indicata con gli eventuali criteri contenuti nel campo where | {"id": 1, "request": "query", "type": "fermate", "where": ["provincia":"venezia", "comune": "trebaseleghe"]} |
| query_stops_coords | Richiede la lista delle fermate nel raggio indicato (in km) con centro le coordinate specificate          | {"id": 2, "request": "query_coords", "lat": 42.500000, "lon": 12.500000, "radius": 2.5}                      |

> Tipologie di oggetti richiedibili attraverso la richiesta query: fermate, comuni, province o regioni


### Formato messaggi server -> client
Il server risponde sempre con il campo `response` che identifica la tipologia di risposta e con il campo `inResponseTo` che contiene l'id della richiesta corrispondente inviata dal client.
Di seguito le risposte possibili:

| Tipo risposta |                                    Descrizione                                     |                                                                                         Esempio                                                                                         |
|---------------|------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| pong          | Risponde a seguito di una richiesta ping                                           | {"inResponseTo": 0, "response": "pong"}                                                                                                                                                 |
| list          | Ritorna una lista di oggetti a seguito di una richiesta query o query_stops_coords | {"inResponseTo": 1, "response": "list", "list": [{"nome": "Mestre Centro B1", "regione": "veneto", "provincia": "venezia", "comune": "venezia", "lat": 45.123354, "lon": 12.43342234}]} |
| error         | Ritorna un errore causato da una richiesta fatta                                   | {"inResponseTo": 2, "response": "error", "description": "richiesta non valida"}                                                                                                      |
