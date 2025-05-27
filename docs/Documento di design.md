*Gruppo 63616E617A7A61, Elementi di ingegneria del Software*.

# Documento di design

## *Domain model*
![img](./Domain%20model/Domain%20model.png)

## *Class diagram*
![img](./Class%20diagram/Class%20diagram.png)

### Descrizione del sistema:

+ **Syntagm:** Classe usata per rappresentare un'unità sintattica della frase (verbi, nomi, aggettivi). Il campo ***value*** contiene il sintagma scritto in modo letterale.
***Syntagm*** è una classe astratta su cui si basano le classi ***Verb***,***Noun*** e ***Adjective***. Il principale utilizzo della classe ***Syntagm*** avviene tramite il metodo ***extract()*** della classe ***InputSentence*** per estrarre e categorizzare i sintagmi componenti la frase fornita dall'utente.

+ **Verb, Noun, Adjective:** Classi che estendono la classe astratta ***Syntagm*** e che formano il dizionario interno del sistema di verbi, nomi e aggettivi rispettivamente.
Ognuna di queste classsi contiente una lista interna di oggetti di tipo String come campo ***data*** caricata da file di testo durante l'inizializzazione. Per riempire ***data*** ognuna di queste classi fa uso del metodo ***load(String filePath)*** fornito dalla classe ***DataUtils*** specificando la locazione di ciascun file tramite il campo interno ***dataPath***. Ognuna di queste classi supporta la generazione di verbi, nomi o aggettivi casuali nel proprio costruttore tramite il metodo ***getRandom(ArrayList<String> list)*** della classe ***DataUtils*** qualora non venga specificato diversamente durante la dichiarazione di nuove variabili.

+ **ApiController:**
Utility Class che si occupa di interfacciarsi direttamente con la Google cloud API a cui effettua
richieste in base alle istruzioni ricevute dalle classi ***InputSentence*** e ***OutputSentence***.
Fornsice funzionalità riguardanti l'estrazione e categorizzazione dei sintagmi di una frase, la creazione di alberi sintattici, la tossicità della frase e il salvataggio nel cloud bucket delle frasi generate.

+ **SyntaxElement:**
Classe rappresentante un elemento sintattico e usata nella creazione dell'albero sintattico. Contiene i campi descrittivi ***value*** e ***syntax_value***. Il campo ***index*** specifica la posizione di un nodo di tipo ***SyntaxElement*** in una ***ArrayList<SyntaxElement>*** mentre ***edges*** quella dei nodi che formano dei lati con il nodo selezionato.

+ **DataUtils**:
Utility Class usata nell'inizializzazione delle liste di verbi, nomi e aggettivi che formano il
dizionario interno del sistema. DataUtils fornisce inoltre metodi che implementano il salvataggio
di termini nel dizionario interno e su file.

+ **Template:**
Classe usata per gestire i vari template usati nella costruzione della frase generata. La classe
fa uso di funzionalità fornite da DataUtils per gestire una lista interna di templates caricata da file di testo.
Un ***Template*** contiene un campo interno in cui vengono salvate delle frasi costruite in modo tale da avere verbi, nomi e aggettivi rimossi e sostituiti con %v, %n, %v rispettivamente.

+ **SentenceController:**
Classe che gestisce le interazioni tra InputSentence e OutputSentence in base alle istruzioni ricevute dall'UI layer.

+ **InputSentence:**
Classe usata nella gestione della frase fornita dall'utente tramite interazioni con la classe ApiController. Durante l'inizializzazione la frase fornita dall'utente viene salvata in un campo value.  
Il metodo privato ***extract()*** viene eseguito durante l'inizializzazione dell'oggetto InputSentence e delega ad ApiController l'operazione necessaria a riconoscere i vari sintagmi componenti la frase fornita e categorizzarli come Verb, Noun e Adjective.
Il metodo ***getSyntaxTree()*** viene usato per ottenere l'albero sintattico della frase fornita.

+ **OutputSentence:**
Classe che gestisce la generazione di una frase casuale basandosi su un oggetto di tipo ***InputSentence***. 
***OutputSentence*** tramite il metodo privato ***generate()*** usa le funzionalità della classe ***InputSentence*** per suddividerne i sintagmi, successivamente fa uso di un oggetto ***Template*** (casuale o fornito) che riempie usando i termini estratti e termini aggiuntivi se questi non sono sufficienti. 

## **Sequence diagrams**
### System sequence Diagram
Qui sono riportate le principali interazioni tra utente e sistema:  

![img](./Sequence%20diagrams/Diagrams/SystemSequenceDiagram.png)

### Internal sequence diagrams
+ #### **Inizializzazione tramite DataUtils**
Il seguente Sequence diagram mostra la creazione del dizionario interno in inizializzaione per ciascuna categoria di sintagma.

![img](./Sequence%20diagrams/Diagrams/SyntagmInitialization.png) 


![img](./Sequence%20diagrams/Diagrams/Template.png)

+ #### **SentenceController**
![img](./Sequence%20diagrams/Diagrams/SentenceController.png) 

+ #### **InputSentence**
![img](./Sequence%20diagrams/Diagrams/InputSentence.png)

#### Descrizione: 
La prima parte del squence diagram mostra la creazione di un oggetto ***InputSentence*** su cui si basa il funzionamento delle altre classi. InputSentence contiene diversi costruttori in base alle esigenza dell'utente che specifica se salvare o meno i termini di una frase digitata.
La classe si interfaccia direttamente con ***ApiController*** a cui delega il compito di estrarre i sintagmi e categorizzarli per poi salvarli in un array. ***ApiCOntroller*** esegue questo compito compilando una richiesta da inoltrare a Google Cloud API. Nella risposta sono presenti diversi token con un tag che specifica il tipo di sintagma a cui appartengono. ***ApiController*** usa questo campo per differenziare i sintagmi e riempire l'array per poi restituirlo.

Il metodo ***getSyntaxTree()*** è responsabile della creazione dell'albero sintattico. ***ApiController*** crea uno specifico tipo di richiesta da inviare server Google e dalla risposta genera l'albero sintattico usando come nodi oggetti di tipo ***SyntaxElement***.

+ #### **OutputSentence**
![img](./Sequence%20diagrams/Diagrams/OutputSentence.png) 

#### Descrizione: 
La generazione della frase avviene durante l'inizializzazione della classe ***OutputSentence***. Viene fornito un oggetto ***InputSentence*** già inizializzato, inoltre vengono considerate ulteriori necessità come quella di usare un template personalizzato o un tempo verbale specifico.
Vengono salvati i termini usati nella frase in input in delle liste interne per renderli disponibili nella frase da generare. Nel caso i termini non siano sufficienti si generano nuovi termini tramite i costruttori di ***Verb***,***Noun*** e ***Adjective***. L'ultima operazione è quella di riempimento del template sostituendo i marcatori con i termini disponibili con successivo salvataggio della frase generata all'interno di ***OutputSentence***.
Il campo interno contenente la frase generata viene usato per ottenere la toxicity di questa tramite la classe ***ApiController***

