package com.akristic.www.tkrally;



import java.util.ArrayList;

/**
 * Created by Toni on 2.3.2017..
 */

public class MultiChoiceQuestion extends Question {
    ArrayList<String> choices;
    public MultiChoiceQuestion(String questionText, String answer, String... userChoices){
        super(questionText, answer);
        choices=new ArrayList<String>();
        for (int i=0; i<userChoices.length;i++) {
            choices.add(userChoices[i]);
        }

    }

    public ArrayList getChoices(){
       return choices;
    }

}
