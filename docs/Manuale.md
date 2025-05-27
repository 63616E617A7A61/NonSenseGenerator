*Gruppo 63616E617A7A61, Ingegneria del Software (B)*

<br>

<!--![presentazione](https://www.svgrepo.com/show/508699/landscape-placeholder.svg)-->

## Manuale di utilizzo di NonSenseGenerator

### Indice:
+ [Descrizione del Progetto](#descrizione-del-progetto)
+ [Come eseguire il programma](#come-eseguire-il-programma)
  + [Vincoli di esecuzione](#come-eseguire-il-programma)
+ [Librerie utilizzate  ed API esterne](#tecnologie-utilizzate)
+ [Funzioni principali librerie esterne](#principali-funzioni-utilizzate-da-librerie)
<br>
<br>
<br>

# Descrizione del progetto 

NonSenseGenerator è un generatore casuale di frasi. A partire da un input testuale fornito dall'utente il programma genera delle frasi casuali seguendo una logica di elaborazione linguistica interna. 

<br>

## Funzionalità principali fornite all'utente

![Home screen](home.png)
Nell'interfaccia troviamo in ordine: un campo di testo dov'è possibile scrivere la frase in input, sucessivamente un menù a tendina che permette di scegliere la struttura che deve avere la frase generata ed un menù per la selezione del tempo verbale che dev'essere usato nella frase in output, infine un checkbox che permette di salvare nel dizionario interno i termini sconosciuti presenti nella frase data in input.

### Generazione di frasi
Il processo di generazione si basa su tre fasi:
- Scelta di un **template**: Viene selezionata dall'utente o casualmente una struttura predefinita per la frase da generare.
- **Estrazione** lessicale: Dall'input vengono estratti nomi, aggettivi e verbi tramite analisi grammaticale.
- **Composizione** della frase: Gli elementi estratti vengono inseriti nei campi corrispondenti del template. Se una categoria (nomi, verbi, aggettivi) non contiene elementi a sufficienza, il sistema integra parole casuali prese da un dataset lessicale locale.

### Analisi tossicità
La frase generata dall'algoritmo viene sottoposta a un'analisi della tossicità, dove con tossicità si intendono contenuti espliciti, violenti o volgari; più in generali tutti i contenuti ritenuti inappropriati avranno un _toxicity rate_ superiore alle altre frasi. 

### Visualizzazione albero sintattico
Il programma fornisce inoltre un'analisi approfondita della frase di input, fornendo all'utente un albero sintattico della stessa. <br>
![Syntax tree example](syntaxtree.png) <br>
<i>L'albero è rappresentato in senso "orizzontale" per questioni di spazi.</i>
<br>
<br>
<br>

## Come eseguire il programma
Per avviare l'applicazione:
1. **Clona o scarica** il progetto sul tuo dispositivo
2. Nella directory principale del progetto (root), inserisci il file JSON del service account di Google Cloud Console, rinominandolo in: <code>api_key.json</code>
3. Verifica di avere **Maven** installato e correttamente configurato nel tuo sistema.
4. Dalla root del progetto, esegui il seguente comando per avviare l'applicazione: <code>mvn javafx:run</code>
> ℹ️ Assicurati di utilizzare JDK 21 o versione successiva per garantire la corretta esecuzione del programma

**Come configurare Google Cloud Console**
1. **Crea un Service Account**
- Vai su "IAM e amministrazione" > "Service Accounts"
- Clicca "+ CREA SERVICE ACCOUNT"
- Inserisci nome e descrizione
- Assegna il ruolo "Storage Admin" (o almeno "Storage Object Admin" per i permessi di scrittura)
> ℹ️ *Questo è necessario per abilitare l'utilizzo dei bucket e quindi salvare tutte le frasi generate su Google Cloud*
- Clicca "Fatto"
2. **Crea una chiave per il Service Account**
- Nella lista dei Service Account, clicca sul tuo appena creato
- Vai alla scheda "KEYS"
- Clicca "ADD KEY" > "Create new key"
- Scegli formato JSON e scarica il file 

<br>
<br>
<br>

## Tecnologie utilizzate 
Il progetto sfrutta diverse tecnologie per garantire uno sviluppo efficiente e un programma ben funzionante: 

**Linguaggio e ambiente di sviluppo**
- **Java 21**: linguaggi di programmazione principale
- **Maven**: per la gestione delle dipendenze nel progetto


**Interfaccia grafica**
- **JavaFX (v23.0.1)**: utilizzato per la creazione della GUI


**Testing**
- **JUnit 5 (Jupiter v5.8.2)**: per la scrittura e l'esecuzione di test automatici del codice


**Elaboraione del testo**
- **Google Cloud Natural Language API**: per analisi sintattica, estrazione di informazioni e valutazione della tossicità
- **Simple NLG**: per la generazione linguistica, come il cambio di tempo verbale 
- **Evo Inflector**: libreria per la gestione del passaggio tra la forma singolare e la forma plurale di una parola



**Servizi Cloud**
- **Google Cloud Storage API**: per il salvataggio delle frasi generate dal sistema. Ogni frase viene registrata in un log sotto formato json.

**Altri plugin**: Vengono inoltre utilizzati altri plugin come _JavaFX Maven Plugin_ o _Maven Javadoc Plugin_

<br>
<br>
<br>

## Principali funzioni utilizzate da librerie
Il progetto sfrutta diverse librerie per garantire il corretto funzionamento dle programma:

**Simple NLG** 

I seguenti metodi vengono applicati add'oggetto `SPhraseSpec` che rappresenta a tutti gli effetti una frase ovvero un wrapper di `PhraseElement`, questa libreria viene usata nel progetto solo ed esclusivamente per coniugare correttamente i verbi della frase di output (se possibile) analizzando persona e numero del soggetto.
- `setVerb(Object verb)` aggiunge il verbo al nostro oggetto SPhraseSpec
- `setFeature(String featureName, Object featureValue)` usato in più varianti, serve ad aggiungere informazioni sulla frase, nel programma viene utilizzato per aggiumgere persona, numero e tempo al nostro verbo, così che venga coniugato nella forma più corretta.

esempio: (supponendo `sentence` oggetto della classe SPhraseSpec)<br>
`sentence.setVerb("was");`<br>
`sentence.setFeature(Feature.TENSE, Tense.PRESENT);`<br>
`System.out.println(realiser.realiseSentence(sentence));`<br>
l'output che vedremo nella console sarà <b>is</b>.

**Evo Inflector**

Fornisce metodi per la pluralizzazione di nomi. Questa libreria è stata scelta dato che è stata fatta la scelta di introdurre nomi plurali all'interno dei template. Quindi ogni volta che dovrebbe esssere inserito un nome plurale il programma prima di tutto controlla che il nome sia pluralizzabile (ovvero che non sia un nome proprio), dopodiché se lo è utilizza la seguente libreria per pluralizzarlo; altrimenti il programma prevede di scartare il nome non pluralizzabile e di scegliere dal dizionario un nome che sia pluralizzabile.
- `English.plural(String word)` pluralizza word.
- `English.plural(String word, int count)` in base al valore di cont pluralizza o meno la parola: <b>cont = 1</b> mantiene la parola al singolare; <b>cont = 2</b> pluralizza la parola.

Non sempre i termini pluralizzati sono corretti, infatti gli sviluppatori di questa libreria dichiarano: *"Overall it returns a correct answer 69.02782% of the time"*.

**Google Cloud Natural Language API**

API usate per l'analisi delle parte sintattica di una frase e per validare alcuni parametri legati alle proprietà della frase ad esempio la tossicità. Nel progetto sono state fatte chiamate a due API di Google Cloud Natural Language API, nello specifico:
- **Analyzing Syntax** la risposta a questa API ritorna una collezione di oggetti `Token` che contengono preziose informazioni per il funzionamento del programma, tra le funzioni più usate: `token.getPartOfSpeech()` e `token.getDependencyEdge()`.
- **Moderate text** la risposta a questa API ritorna una collezione di oggetti `ClassificationCategory` che contengono vari parametri di moderazione della frase.

**Google Cloud Storage API**

Usato per la gestione dei `Bucket`, così da salvare ogni volta la frase generata dal programma. Un Bucket è un particolare oggetto che può contenere altri oggetti i `Blob`, ognuno di questi permette di gestire e rappresentare una risorsa, nel programma utilizziamo i Blob per caricare all'interno del Bucket le frasi generate dal programma in formato json. Il metood principale che permette questo è:
- `create(BlobInfo blob, byte[] content)` che accetta un oggetto BlobInfo correttamente inizializzato (quindi già associato ad un Bucket) e un array di byte che sarà il file di cui vogliamo fare l'upload. Questo metodo è un metodo della classe `Storage` che si occupa di autenticarsi al servizio di Google Cloud Storage.