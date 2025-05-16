package com.g63616e617a7a61.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.g63616e617a7a61.Model.Adjective;
import com.g63616e617a7a61.Model.Noun;
import com.g63616e617a7a61.Model.Syntagm;
import com.g63616e617a7a61.Model.SyntaxElement;
import com.g63616e617a7a61.Model.Verb;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.LanguageServiceSettings;
import com.google.cloud.language.v1.ModerateTextRequest;
import com.google.cloud.language.v1.ModerateTextResponse;
import com.google.cloud.language.v1.Token;

public class ApiController {
    private static String key = "nice-etching-459813-s4-7e025d2aaa2c.json";
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
                        syntagms.add(new Noun(token.getText().getContent()));
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
                    index);
                
                tree.add(se);
                if(index != token.getDependencyEdge().getHeadTokenIndex())
                    dependencies[index] = token.getDependencyEdge().getHeadTokenIndex();
                else dependencies[index] = -1; //non voglio nessun collegamento autoreferenziale
                index++;
            }

            for(SyntaxElement e : tree){  // inserisco i "collegamenti" tra gli elementi
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
}
