*Gruppo 63616E617A7A61, Elementi di ingegneria del Software*.

# Design document - Nonsense generator

## Index: 
+ ### [<ins>Domain model</ins>](#domain-model-1)
+ ### [<ins>Class diagram</ins>](#class-diagram-1)  
    + [System description](#system-description)
+ ### [<ins>Sequence diagrams</ins>](#sequence-diagrams-1)
    + #### [System sequence diagram](#system-sequence-diagram-1)
    + #### [Internal sequence diagrams](#internal-sequence-diagrams-1)
        + ##### [SentenceController](#sentencecontroller-1)
        + ##### [InputSentence](#inputsentence-1)
        + ##### [OutputSentence](#outputsentence-1)
        + ##### [Initialization of built-in dictionary and template list](#initialization-of-built-in-dictionary-and-template-list-1)

## *Domain model*
![img](./Domain%20model/Domain%20model.png)

#### Description:   
>The user of Nonsense generator interacts directly with a UI layer. below it there are multiple controllers that forward the requests of the user to the logical components of the system and makes it so the data is formatted correctly when needed to be presented to the user.
The majority of the actors are those that constitute the internal layer of the system that is responsable for the actual logical computation requiered to execute all operations.

## *Class diagram*
![img](./Class%20Diagram/Class%20Diagram.png)

### System description:

+  > <ins>**Syntagm</ins>:**  
Abstract class used to represent a syntagm of a sentence that is later categorized int a verb, noun or adjective. Syntagm stores a human readable word in its ***value*** field.
Syntagm is mainly used in the <code>extract()</code> method in the ***InputSentence*** class to divide the syntagms of a given sentence.

+ > <ins>**Verb</ins>, <ins>Noun</ins>, <ins>Adjective</ins>:** 
***Verb***, ***Noun*** and ***Adjective*** are all classes that extend the abstract class ***Syntagm*** and are used to provide an internal dictionary for the system.
Each one of these classes contains a ***data*** of ArrayList<String> used to save a list of words loaded from a text file via the ***load(String filePath)*** provided by the utility class ***DataUtils***.
The generation of a random word from the loaded list is supported in the constructor of these classes by using the ***getRandom(ArrayList<String> list)*** of class ***DataUtils*** should the new object be instantiated without a specific word.

+ > <ins> **ApiController:** </ins>
Utility class that interacts directly with Google Cloud API by compiling and sending different types of requests based by instructions received from ***InputSentence*** and ***OutputSentence***.
The class provides also the ability to log generated sentences in a json file that is stored in a cloud bucket via the method ***uploadjsonToBucket()***. 

+ > <ins> **SyntaxElement:** </ins>
Class that represent a single syntactic element used in the creation of the syntax tree of the sentence. The class contains descriptive fields ***value*** and ***syntax_value***. The ***index*** field is meant to specify the position of a ***SyntaxElement*** in a ***ArrayList<SyntaxElement>*** while ***edges*** contains the positions of other nodes that form edges with the selected one.

+ > <ins> **DataUtils**: </ins>
Utility class used to provide functionalities to lists of verbs, nouns, adjectives as well as templates. Its methods <code>load(String filePAth)</code> and <code>getRandom(ArrayList<String> list)</code> are used to initialize a list of string from a file and pick a random element from it. This class provides the functionality for saving new words in a text whose location is specified by the argument filePath in the method <code>append(String s, String filePath)</code>

+ > <ins> **Template:** </ins>
Class used to manage a list of templates which are used when generating new sentences. Each object of type ***Template*** has an internal field <code>template</code> use to save the literal template in a human readable format.
Templates are sentences which have their verbs, nouns, adjectives removed and replaced by %v, %n, %a respectively. This allows the system to recognize which type of word to use when filling a template to generate a sentence.

+ > <ins> **SentenceController:** </ins>
Class used to manage interactins between an ***InputSentence*** and an ***OutputSentence*** base on which demands it gets from the UI layer.
The methods it contains delegate the requiered operations to the individual classes.  
***SentenceController*** provides the functionality requiered to extract the chosen template and rewrite it to be human-readable via the method <code>rewriteTemplate(String template)</code>.

+ > <ins> **InputSentence:** </ins>
Class used to 
The method <code>extract()</code> is called during initalization and returns an array of syntagms containing all categorized syntagms of the sentence provided by the user.
The <code>***getSyntaxTree()***</code> method is used to get the syntactic tree of the given sentence by having ***ApiController*** sending a specific request to the Google Cloud Natural Language API.

+ > <ins> **OutputSentence:** </ins>
Class that supports the generation of a template-based random sentence by utilizing a given ***InputSentence***.
The generation of the sentence is coded via the private method <code>generate()</code> that executes operations on an ***InputSentence*** to extract its syntagms and uses them to fill a template that may be specified or picked randomly from the internal list of the system.  

## **Sequence diagrams**

+ ### System sequence Diagram:
![img](./Sequence%20diagrams/Diagrams/SystemSequenceDiagram.png)

> Here are reported the main interactions between a User and the system. While a sentence is always requiered to create a generated sentence there are some additional options that the user may decide to use that affect how the system responds.

+ ### Internal sequence diagrams

+ ### <ins>**SentenceController**</ins>
![img](./Sequence%20diagrams/Diagrams/SentenceController.png) 

#### Description: 

> This diagram illustrates the creation of ***SentenceCoontroller*** and shows the reliance this class has towards ***InputSentence*** and ***OutputSentence*** for generating a sentence and retrieving the requiered data from it. The main operations that are managed by ***SentenceController*** are:
> + extraction of syntagms
> + generation of a sentence
> + creation of a syntactic tree
> + calculation of the generated sentence toxicity  
> 
> These operation are shown in greater detail in later diagrams as they are implemented in ***InputSentence*** and ***OutputSentence*** while ***SentenceController*** only delegates work to these classes.


+ ### <ins>**InputSentence*</ins>*
![img](./Sequence%20diagrams/Diagrams/InputSentence.png)

#### Description: 

> This diagram shows the steps executed by ***InputSentence*** during initialization to extract the syntagms from the given sentence by utilizing Functionalities provided by ***ApiController***. Each token received from the response given by the Google cloud Natural Language API is used to create a syntagm and categorize appropriately.
Below is shown the internal working of the <code>getSyntaxTree()</code> method used to generate the syntactic tree from nodes of type ***SyntaxElement*** created from the sentence given by the user. 

+ ### <ins>**OutputSentence**</ins>
![img](./Sequence%20diagrams/Diagrams/OutputSentence.png) 

#### Description: 

> The diagram shows the steps requiered to generate a random sentence from a template. This is done during the initialization of an object of type ***OutputSentence*** and requieres an ***InputSentence*** to provide the resulting sentence. **OutputSentence** saves verbs, nouns and adjectives of the provided sentence internally so that it can use them later while filling the template by replacing the placeholders.
***OutputSentence*** is capable of generating more terms by utilizing the constructors of ***Verb***,***Noun*** and ***Adjective*** should the number of terms extracted from ***InputSentence*** be insufficient. 
The getToxicity() functionality of ***OutputSentence*** is implemented by relying on ***ApiController*** to make a custom request to the server Google cloud server and returning the result as a number.

+ ### <ins>**Initialization of built-in dictionary and template list:**</ins>
#### Description: 
> The diagram shows the interactions between the classes that need some provided data to work properly and how ***DataUtils*** is used to retrieve that data and manipulate it.

![DataInitialization](./Sequence%20diagrams/Diagrams/DataInitialization.png)
