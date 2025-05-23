*Gruppo 63616E617A7A61, Ingegneria del Software (B)*

# **<p style="text-align: center;"> Nonsense generator**  </p>

![presentazione](https://www.svgrepo.com/show/508699/landscape-placeholder.svg)

### **Manuale di utilizzo**

### **Indice**:

+ [Descrizione del Progetto](#descrizione-del-progetto)
+ [Come eseguire il programma](#installazione)
+ [Vincoli di esecuzione](#vincoli-di-esecuzione)
+ [Librerie utilizzate](#librerie-utilizzate)
+ [API esterne](#api-esterne)

<div style="page-break-after: always"></div>

## Descrizione del progetto 

Nonsense generator è un programma capace di generare delle frasi casuali basandosi su una frase scritta dall'utente.  
Il programma presenta un'interfaccia grafica che rende possibile l'inserimento della frase e rende disponibile la frase generata fornendo inoltre l'opzione di visualizzare la tossicità della frase generata e l'albero sintattico della frase fornita.  
Nonsense generator è stato progettato per funzionare unicamente in lingua inglese.  
La frase generata è costruita utilizzando dei template già presenti nel sistema o provvisti dall'utente.

## Come eseguire il programma

1. Scaricare la cartella di progetto
1. Nella cartella di root inserire il json del service account di google api denominato <code>api_key.json</code>
1. Assicurandosi di avere maven installato sul dispositivo, eseguire il seguente comando nella cartella root del programma: <code>mvn javafx:run</code>

## Vincoli di esecuzione

JRE 21 o versioni successive.

## Librerie utilizzate

TO DO

+ **junit-jupiter (org.junit.jupiter) version 5.8.2**  
Framework per unit testing.
     
+ **evo-inflector (org.atteo) version 1.3**
conversione da singolare a plurale.
    
+ **javafx-fxml (org.openjfx)**

+ **javafx-controls (org.openjfx)**


## API esterne

TO DO

+ **google-cloud-language (com.google.cloud) version 2.28.0**  
    Usata per tutte le funzionalità che riguardano l'analisi di frasi e dei suoi elementi.

+ 
