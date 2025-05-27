# System test document

*Legenda struttura:*
- **User Story**
    - **Acceptance Criteria**
        - Validazione: ok
        - Note/note: ...
        - Date: oggi
    - **Acceptance Criteria**
        - Validazione: ok
        - Note/note: ...
        - Date: oggi
    ...
- **User Story**
    ...


### **1. As a user I want to provide in input a sentence so that I can view in output the nonsense sentence genrated and relative toxicity rate**
- *Given I am a user when I input a sentence and press the generate button or click enter, an output sentence is generated*
	- Validation: ok
	- Note: 
	- Date: 27/5
	
### **2. As a user I want to be able to see the toxicity rate of the generated sentence**
- *Given that I am a user the toxicity rate is displayed upon generation*
	- Validation: ok
	- Note: The toxicity rate is displayed under the output sentence
	- Date: 27/5
		
### **3. As a user I want to be able to see the syntactic tree of the input sentence**
- *Given that I am a user When I provide a sentence in input and I click the "info" button, Then I view the syntactic tree*
	- Validation: ok
	- Note: The ui is responsive even to multiple sentences and trees
	- Date: 27/5
- *Given that I am a user and the tree is already showing, When I click the "info" button again, the tree is hidden*
	- Validation: ok
	- Note: The ui is responsive even to multiple sentences and trees
	- Date: 27/5
	
### **4. As a user I want the system to separate the nouns, adjectives and verbs of the input sentence so that they can be used in the output sentence**
- *Given that I am a user When I provide an input sentence, and I click the generate sentence button, then the generated sentence must include the terms I used in my sentence*
	- Validation: ok
	- Note: if a generated output sentence is then given in input, and than the same templated is chosen, the input and output sentence will be equal. This follows the app requirements.
	- Date: 27/5
- *Given that I am a user When I provide an input sentence, and I click the generate sentence button, if the generated sentence uses more words than given in input, then the generated sentence must include additional terms to complete the template*
	- Validation: ok
	- Note: all input words are used if possibile
	- Date: 27/5
- *Given that I am a user When I provide an input sentence, and I click the generate sentence button, if the generated sentence uses less than given in input, then the generated sentence must include some of terms I used in my input sentence, randomly sampled*
	- Validation: ok
	- Note: equal input generate different output in this scenario even with the same template, as expected
	- Date: 27/5
	
### **5. As a user I want to be able to specify a verb tense so that the output sentence is generated in that tense**
- *Given that I am a user When I specify a verb tense then the generated sentence contains only verbs in that tense*
	- Validation: ok
	- Note: this applies even when different tenses are included in the same input, as expected.
	- Date: 27/5
- *Given that I am a user when I don't specify a verb tense that the default option is the PRESENT*
	- Validation: ok
	- Note:
	- Date: 27/5
- *Given that I am a user and my input contains verbs, they get included in the output, with their tense changed to be consistent with the rest of the output sentence*
	- Validation: ok
	- Note: see acceptance criteria 1
	- Date: 27/5

### **6. As a user I want to be able to choose a template from a list so that it will be used for the output sentence**
- *Given that I am a user When I choose a template the system uses that template for the output sentence*
	- Validation: ok
	- Note: the same input generates different outputs but with the same template if the same is chosen, as expected 
	- Date: 27/5
- *Given that I am a user and I don't choose a template, Then the system will select one randomly*
	- Validation: ok
	- Note: the same input generates outputs with different templates if no one is chosen, as expected
	- Date: 27/5
	
### **7. As a user I want the option to add new words from the input to the dictionary**
- *Given that I am a user If I click the "Save unkown word in the dictonary button", than if a word in the input sentence is not present in Date/, it is added*
	- Validation: ok
	- Note: the word is added at the end of the .txt files not in alphabetical order. This does not influence the app logic.
	- Date: 27/5
