*Gruppo 63616E617A7A61, Ingegneria del Software (B)*

<br>

<!--![presentazione](https://www.svgrepo.com/show/508699/landscape-placeholder.svg)-->

## Manuale di utilizzo di NonSenseGenerator

### Indice:
+ [Descrizione del Progetto](#descrizione-del-progetto)
+ [Come eseguire il programma](#installazione)
+ [Vincoli di esecuzione](#vincoli-di-esecuzione)
+ [Librerie utilizzate](#librerie-utilizzate)
+ [API esterne](#api-esterne)

<br>
<br>
<br>

# Descrizione del progetto 

NonSenseGenerator è un generatore casuale di frasi. A partire da un input testuale fornito dall'utente il programma genera delle frasi casuali seguendo una logica di elaborazione linguistica interna. 

<br>

## Funzionalità principali fornite all'utente

### Generazione di frasi
Il processo di generazione si basa su tre fasi:
- Scelta di un **template**: Viene selezionata casualmente una struttura predefinita per la frase da generare.
- **Estrazione** lessicale: Dall'input vengono estratti nomi, aggettivi e verbi tramite analisi grammaticale.
- **Composizione** della frase: Gli elementi estratti vengono inseriti nei campi corrispondenti del template. Se una categoria (nomi, verbi, aggettivi) non contiene elementi a sufficienza, il sistema integra parole casuali prese da un dataset lessicale locale.

### Analisi tossicità
La frase generata dall'algoritmo viene sottoposta a un'analisi della tossicità, dove con tossicità si intendono contenuti espliciti, violenti o volgari; più in generali tutti i contenuti ritenuti inappropriati avranno un _toxicity rate_ superiore alle altre frasi. 

### Visualizzazione albero sintattico
Il programma fornisce inoltre un'analisi approfondita della frase di input, fornendo all'utente un albero sintattico della stessa. 

<br>
<br>
<br>

## Come eseguire il programma
Per avviare l'applicazione:
1. **Clona o scarica** il progetto sul tuo dispositivo
2. Nella directory principale del progetto (root), inserisci il file JSON del service account di Google Cloud, rinominandolo in: <code>api_key.json</code>
3. Verifica di avere **Maven** installato e correttamente configurato nel tuo sistema.
4. Dalla root del progetto, esegui il seguente comando per avviare l'applicazione: <code>mvn javafx:run</code>
> ℹ️ Assicurati di utilizzare JDK 21 o versione successiva per garantire la corretta esecuzione del programma

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
- **Google Cloud Storage API**: per il salvataggio delle interazioni tra gli utenti e il sistema. Ogni interazione viene registrata in un log.

**Altri plugin**: Vengono inoltre utilizzati altri plugin come _JavaFX Maven Plugin_ o _Maven Javadoc Plugin_

<br>
<br>
<br>


