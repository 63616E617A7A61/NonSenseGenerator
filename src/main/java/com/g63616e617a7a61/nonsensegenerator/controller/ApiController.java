package com.g63616e617a7a61.nonsensegenerator.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.g63616e617a7a61.nonsensegenerator.model.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.PartOfSpeech.Number;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.ModerateTextRequest;
import com.google.cloud.language.v1.ModerateTextResponse;
import com.google.cloud.language.v1.Token;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.BucketInfo.PublicAccessPrevention;

/**
 * Controller class for interacting with Google Cloud Natural Language API and Cloud Storage.
 * Provides functionality for text analysis, syntax parsing, toxicity detection, and cloud storage operations.
 * 
 * @author Tommaso Rovoletto
 */

public class ApiController {
    private static String key = "api_key.json";
    private static String bucketName = "g63616e617a7a61-data";

    /**
     * Extracts syntagms (nouns, verbs, adjectives) from a given text using Google Natural Language API.
     * 
     * @param s the input text to analyze
     * @return array of extracted Syntagm objects
     * @throws IOException if there's an error reading credentials or communicating with the API
     */
    public static Syntagm[] extract(String s) throws IOException{
        ArrayList<Syntagm> syntagms = new ArrayList<>();

        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new FileInputStream(key))
            .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        LanguageServiceSettings settings = LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build();

        try (LanguageServiceClient language = LanguageServiceClient.create(settings)) {
            Document doc = Document.newBuilder()
                .setContent(s)
                .setType(Document.Type.PLAIN_TEXT)
                .build();

            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                .setDocument(doc)
                .setEncodingType(com.google.cloud.language.v1.EncodingType.UTF16)
                .build();

            AnalyzeSyntaxResponse response = language.analyzeSyntax(request);
            for (Token token : response.getTokensList()) {
                switch (token.getPartOfSpeech().getTag()) {
                    case ADJ:
                        syntagms.add(new Adjective(token.getText().getContent()));
                        break;
                    case NOUN:
                        if(token.getPartOfSpeech().getNumber() == Number.PLURAL){
                            syntagms.add(new Noun(token.getLemma()));
                        }else{
                            syntagms.add(new Noun(token.getText().getContent()));
                        }
                        break;
                    case VERB:
                        syntagms.add(new Verb(token.getText().getContent()));
                        break;
                    default:
                        break;
                }
            }
        }
        Syntagm[] out = new Syntagm[syntagms.size()];
        return syntagms.toArray(out);
    }

    /**
     * Generates a syntax tree from the input text with dependency relationships.
     * 
     * @param s the input text to analyze
     * @return List of SyntaxElements representing the syntax tree with dependency edges
     * @throws IOException if there's an error reading credentials or communicating with the API
     */
    public static List<SyntaxElement> getSyntaxTree(String s) throws IOException{
        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new FileInputStream(key))
            .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        LanguageServiceSettings settings = LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build();

        try (LanguageServiceClient language = LanguageServiceClient.create(settings)) {
            Document doc = Document.newBuilder()
                .setContent(s)
                .setType(Document.Type.PLAIN_TEXT)
                .build();

            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                .setDocument(doc)
                .setEncodingType(com.google.cloud.language.v1.EncodingType.UTF16)
                .build();

            AnalyzeSyntaxResponse response = language.analyzeSyntax(request);

            int[] dependencies = new int[response.getTokensList().size()];
            List<SyntaxElement> tree = new ArrayList<>();
            int index = 0;
            for (Token token : response.getTokensList()) {
                SyntaxElement se = new SyntaxElement(
                    token.getText().getContent(),
                    token.getDependencyEdge().getLabel().name(),
                    index, 
                    token.getPartOfSpeech().getPerson().name(), 
                    token.getPartOfSpeech().getNumber().name());
                
                tree.add(se);
                if(index != token.getDependencyEdge().getHeadTokenIndex())
                    dependencies[index] = token.getDependencyEdge().getHeadTokenIndex();
                else dependencies[index] = -1; //I don't want any self-referential links
                index++;
            }

            for(SyntaxElement e : tree){  //I insert the "links" between the elements
                ArrayList<Integer> buff = new ArrayList<>();
                for(int i = 0; i < dependencies.length; i++){
                    if (dependencies[i] == e.getIndex()) {
                        buff.add(i);
                    }
                }
                if (!buff.isEmpty()) {
                    e.setEdges(buff);
                }
            }
            
            return tree;
        }
    }

    /**
     * Finds the subject of a given verb in a sentence by analyzing the syntax tree.
     * 
     * @param sentence the complete sentence to analyze
     * @param verb the verb to find the subject for
     * @return SyntaxElement representing the subject, or null if not found
     * @throws IOException if there's an error analyzing the syntax
     */
    public static SyntaxElement getSubject(String sentence, String verb) throws IOException{
        List<SyntaxElement> lse = getSyntaxTree(sentence);
        for(SyntaxElement e : lse){                         
            if(e.getValue().equals(verb)){                  //I'm looking for my verb
                if(e.getEdges() == null) return null;
                for(int i : e.getEdges()){                  //I check the connections it has
                    SyntaxElement buff = lse.get(i);        
                    if (buff.getSyntax_value().contains("SUBJ")) { //I check all the arches if one of them is labeled with SUBJ
                        return buff;                        //our subject returns
                    }
                }
                
            }
        }
        return null;
    }

    /**
     * Analyzes text for toxic content using Google's Perspective API.
     * 
     * @param s the text to analyze
     * @return toxicity rate between 0.0 (non-toxic) and 1.0 (highly toxic)
     * @throws IOException if there's an error reading credentials or communicating with the API
     */
    public static double getToxicity(String s) throws IOException{
        GoogleCredentials credentials = GoogleCredentials.fromStream(
            new FileInputStream(key))
            .createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));

        LanguageServiceSettings settings = LanguageServiceSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build();

        try (LanguageServiceClient language = LanguageServiceClient.create(settings)) {
            Document doc = Document.newBuilder()
                    .setContent(s)
                    .setType(Type.PLAIN_TEXT)
                    .build();

            ModerateTextRequest request = ModerateTextRequest.newBuilder()
                    .setDocument(doc)
                    .build();

            ModerateTextResponse response = language.moderateText(request);

            if (response.getModerationCategoriesCount() > 0) {
                for (ClassificationCategory category : response.getModerationCategoriesList()) {
                    if (category.getName().equals("Toxic")) {
                        return category.getConfidence();
                    }
                }
            }
            return 0.0f;
        }
    }

    /**
     * Uploads a JSON file to Google Cloud Storage bucket.
     * Creates the bucket if it doesn't exist and generates a unique filename for store data.
     * 
     * @param jString the JSON content to upload
     * @return true if upload was successful, false otherwise
     */
    public static boolean uploadJsonToBucket(String jString){
        // Authenticate with service account
        GoogleCredentials credentials = null;
        try {
            credentials = GoogleCredentials.fromStream(
                    new FileInputStream(key))
                    .createScoped("https://www.googleapis.com/auth/cloud-platform");
        } catch (Exception e) {
            System.err.println("Error on authentication process: " + e.getMessage());
            return false;
        }
        try {
            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(credentials)
                    .build()
                    .getService();


            // Check if bucket exists, create if not
            Bucket bucket = storage.get(bucketName);
            if (bucket == null) {
                try {
                    bucket = storage.create(
                    BucketInfo.newBuilder(bucketName)
                        .setLocation("europe-west1") 
                        .setIamConfiguration(
                            BucketInfo.IamConfiguration.newBuilder()
                                .setPublicAccessPrevention(PublicAccessPrevention.ENFORCED) 
                                .build()
                        )
                        .build()
                );
                    System.out.println("Bucket creato: " + bucketName);
                } catch (StorageException e) {
                    System.err.println("Errore nella creazione del bucket: " + e.getMessage());
                    return false;
                }
            }

            // Create blob info with JSON content type
            BlobId blobId = BlobId.of(bucketName, "data_" + UUID.randomUUID() + ".json");
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/json")
                    .build();

            // Upload the JSON string
            storage.create(blobInfo, jString.getBytes(StandardCharsets.UTF_8));
            
            return true;
            
        } catch (StorageException e) {
            System.err.println("Error uploading JSON to GCS: " + e.getMessage());
            return false;
        }
    }
}
