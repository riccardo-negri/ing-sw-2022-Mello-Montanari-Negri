# Peer-Review 2: Protocollo di Comunicazione

Pietro Mello Rella, Tommaso Montanari, Riccardo Negri

Gruppo 1

Valutazione della documentazione del protocollo di comunicazione del gruppo 69.

## Lati positivi

- Riteniamo che un lato positivo sia la presenza di un messaggio apposito nel caso
l'username scelto sia già preso. Tuttavia implementare questa funzionalità è più
complicato se si vuole mantenere la feature della resilienza alle disconnessioni.

- Il messaggio di errore per InitializeGameMsg può essere un lato positivo se gestito
correttamente. Ad esempio nel caso di un errore di rete si può provare a inviare
nuovamente il messaggio, altrimenti si può creare una nuova istanza della partita
se qualcosa è andato storto.

## Lati negativi

- Abbiamo notato che nel messaggio InitializeGameMsg non vengono mandate le informazioni riguardanti le plance degli altri giocatori (gli studenti presenti nell'entrata, il colore delle torri ecc..). Senza queste informazioni il client non ha modo di vedere lo stato degli altri client e il gioco risulta quindi difficilmente giocabile.

- Abbiamo notato che nei sequence diagram non è trattato il caso in cui i client fanno una mossa non ammessa.  Nel caso in cui non sia presente una logica di controllo lato client è necessario implementare un messaggio di risposta, da parte del server, per segnalare la mossa non valida. Nel caso invece in cui i controlli vengano fatti lato client, potrebbe essere comunque saggio avere un messaggio di errore a fronte di una mossa non valida così da prevenire client malevoli. 

- Segnaliamo che manca UseCharachterCardMsg nella ACTION PHASE 1 visto che i character possono essere usati in qualsiasi momento del proprio turno.

- Vogliamo porre l'attenzione su come viene comunicata la fine del turno di un giocatore. Questo problema si pone visto che non è presente un messaggio di fine turno. Infatti ChoseCloudMsg non può essere valido come messaggio di fine turno dato che UseCharachterCardMsg può essere mandato in qualsiasi momento. Suggeriamo quindi l'implementazione di un messaggio di fine turno.

- Nella sezione ACTION PHASE 3 non ha senso includere nuovamente MoveMotherNatureMsg
perché si tratta dell'azione che viene svolta nella ACTION PHASE 2 e per le regole
del gioco Madre Natura si può muovere una sola volta per turno.

- Il Sequence diagram nella sezione ACTION PHASE 3 è sbagliato e fa riferimento
alla sezione precedente infatti manca ChoseCloudMsg ed è presente CheckProfessorMsg.

## Confronto

Il gruppo 69 ha scelto di creare un thin client, che non ha informazioni sulla fase di gioco, per questo è necessario che sia il server a chiedere al client di giocare la mossa e attendere la sua risposta.

Questo approccio ha il lato positivo di richiedere molta meno computazione nel client, ma rende più complessa la gestione di alcune fasi di gioco, come ad esempio la action phase, in cui possono essere effettuate più operazioni in contemporaneo.

Un evento simile si verifica ad esempio quando si può giocare una carta personaggio, in questo caso andrebbe pensato un messaggio specifico per indicare di non voler giocare la carta personaggio oppure un messaggio di fine turno.

Il nostro approccio è stato invece quello di passare al client l’intero gioco, compreso lo stato corrente e le plancie degli avversari. Questo non solo dà al giocatore una visibilità superiore sulla partita, rendendo l’esperienza più simile al gioco reale, ma permette anche di controllare preventivamente se una mossa sia eseguibile o meno dal giocatore, in maniera da minimizzare il traffico di rete tra i client e il server.

Il nostro protocollo non prevede quindi che sia il server ad aprire la conversazione, chiedendo al client di giocare la mossa, ma è il client stesso ha inviare la sua mossa al server. Il server risponde positivamente se la mossa è andata a buon fine, e allo stesso tempo aggiorna anche gli altri client, che così sono aggiornati sulla situazione attuale del gioco.