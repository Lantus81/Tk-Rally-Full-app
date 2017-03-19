package com.akristic.www.tkrally;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class QuizActivity extends AppCompatActivity {

    private ArrayList<Question> questions;
    private Random generator = new Random();
    private TextView questionTextView;
    private TextView questionNumberTextView;
    private TextView scoreTextView;
    private EditText answerEditText;
    private Question currentQuestion;
    private RadioGroup choiceRadioGroup;
    private RatingBar scoreRatingBar;
    private Button nextButton;
    private Button submitButton;
    private Button tryAgain;
    private int randomIndex;
    private int score = 0;
    private final int NUMBER_OF_QUESTIONS = 10;
    private int questionNumber = 1;
    private boolean isPhoneTurned = false;
    private boolean submitPressed = false;
    private boolean quizEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        questionTextView = (TextView) findViewById(R.id.question_text);
        answerEditText = (EditText) findViewById(R.id.question_answer);
        scoreTextView = (TextView) findViewById(R.id.current_score);
        questionNumberTextView = (TextView) findViewById(R.id.question_number);
        choiceRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        scoreRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        nextButton = (Button) findViewById(R.id.button_next_question);
        submitButton = (Button) findViewById(R.id.button_submit_question);
        tryAgain = (Button) findViewById(R.id.button_try_again);


        //** Creates and populates questions in ArrayList
        questions = new ArrayList<Question>();
        populateQuestionArrayList();
        if (savedInstanceState != null) {
            // Then the application is being reloaded
            isPhoneTurned = true;
            randomIndex = savedInstanceState.getInt("RANDOM_INDEX");
            score = savedInstanceState.getInt("SCORE");
            questionNumber = savedInstanceState.getInt("QUESTION_NUMBER");
            questions = savedInstanceState.getParcelableArrayList("QUESTIONS");
            submitPressed = savedInstanceState.getBoolean("SUBMIT_PRESSED");
            quizEnd = savedInstanceState.getBoolean("QUIZ_END");
            scoreTextView.setText("Your score is: " + score + "/10");


        }
        //** set first question
        displayQuestion();
        if (submitPressed) {
            submitButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
            if (currentQuestion instanceof MultiChoiceQuestion) {
                RadioButton nextChoice;
                for (int i = 0; i < choiceRadioGroup.getChildCount(); i++) {
                    nextChoice = (RadioButton) choiceRadioGroup.getChildAt(i);
                    nextChoice.setEnabled(false);
                }
            }
            if (currentQuestion instanceof MultiAnswerQuestion) {
                CheckBox nextChoice;
                for (int i = 0; i < choiceRadioGroup.getChildCount(); i++) {
                    nextChoice = (CheckBox) choiceRadioGroup.getChildAt(i);
                    nextChoice.setEnabled(false);
                }
            }

        }
        if (quizEnd) {
            endOfQuiz();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("RANDOM_INDEX", randomIndex);
        outState.putInt("SCORE", score);
        outState.putInt("QUESTION_NUMBER", questionNumber);
        outState.putParcelableArrayList("QUESTIONS", questions);
        outState.putBoolean("SUBMIT_PRESSED", submitPressed);
        outState.putBoolean("QUIZ_END", quizEnd);
        super.onSaveInstanceState(outState);
    }


    /**
     * clears and populate questions arrayList
     * Add new questions here
     */
    private void populateQuestionArrayList() {
        questions.clear();
        questions.add(new Question("Missing both first and second serves is called a:", "double fault"));
        questions.add(new Question("The player who deliver the ball to start the points is called the ......?", "server"));
        questions.add(new Question("Serve that is good and untouched by the returner is called ...", "ace"));
        questions.add(new MultiChoiceQuestion("Answer true or false:\nThe game of tennis can be played with 2 to 4 players.", "1", "True", "False"));
        questions.add(new MultiChoiceQuestion("Answer true or false:\nThe game of tennis can be played with 1 to 6 players.", "2", "True", "False"));
        questions.add(new MultiChoiceQuestion("Answer true or false:\nIf the ball lands on the line, it is out.", "2", "True", "False"));
        questions.add(new MultiChoiceQuestion("No Ad scoring refers to a scoring system that adds up all the game scores to determine the winner.", "2", "True", "False"));
        questions.add(new MultiChoiceQuestion("Answer true or false:\nThe server gets two attempts to make a good serve.", "1", "True", "False"));
        questions.add(new MultiChoiceQuestion("Correct scorekeeping order is:", "2", "45, 35, 20, 10", "love, 15, 30, 40", "15, 30, 45, love", "15, 30, 40, love"));
        questions.add(new MultiChoiceQuestion("In tennis \"love\" means:", "4", "both teams are tied at 40", "I just won but I still want to be friends", "the same as 15", "zero"));
        questions.add(new MultiChoiceQuestion("If I'm serving and I win the first 2 points of the game, the game score is now:", "4", "love, 30", "20, love", "deuce", "30, love"));
        questions.add(new MultiChoiceQuestion("How do you begin the game of tennis?", "2", "run a mile", "toss a coin", "duck walk", "who ever wants to go first"));
        questions.add(new MultiChoiceQuestion("An ace is a", "3", "serve that goes out", "serve that hits the net and then goes in the box", "serve that is good and untouched by the returner", "serve that is hit with slice"));
        questions.add(new MultiAnswerQuestion("The types of hittings swings include (more than one answer may apply):", "25", "Underhand", "Forehand", "Lefthand", "Righthand", "Backhand"));
        questions.add(new MultiAnswerQuestion("Playing tennis is good for", "123", "Eye hand coordination", "Quickness", "Endurance"));
        questions.add(new MultiAnswerQuestion("Types of court surface used to play tennis are", "124", "clay", "carpet", "wood", "rubber"));
        questions.add(new MultiAnswerQuestion("Different types of tennis strokes include:", "34", "overhands", "rallies", "backhands", "forehands", "underhands"));
        questions.add(new MultiAnswerQuestion("What are some ways tennis can be played?", "12", "single", "double", "mixed gender", "all of the above"));
    }

    /**
     * Generates random numbers from 0 to the size of questions ArrayList -1
     *
     * @return random integer number
     */

    private int getRandomIndex() {
        if (isPhoneTurned) {
            isPhoneTurned = false;
            return randomIndex; //** if phone is turned than we don't want to change randomNumber because we dont want to change question

        }
        if (questions.size() > 0) {
            randomIndex = generator.nextInt(questions.size());
            return randomIndex;
        } else {
            return -1;
        }

    }

    /**
     * Display question in TextView with data from ArraList questions
     */
    private void displayQuestion() {
        if (questions.size() > 0) {
            currentQuestion = questions.get(getRandomIndex());
            questionTextView.setText(currentQuestion.getText());
            answerEditText.setText("");
            choiceRadioGroup.clearCheck();
            questionNumberTextView.setText("Question number: " + questionNumber + "/" + NUMBER_OF_QUESTIONS);
            if (currentQuestion instanceof MultiChoiceQuestion) {
                answerEditText.setVisibility(View.INVISIBLE);
                choiceRadioGroup.setVisibility(View.VISIBLE);
                ArrayList choices = ((MultiChoiceQuestion) currentQuestion).getChoices();
                for (int i = 0; i < choices.size(); i++) {
                    RadioButton nextChoice = new RadioButton(this);
                    nextChoice.setText(choices.get(i).toString());
                    choiceRadioGroup.addView(nextChoice, i);
                }
            } else if (currentQuestion instanceof MultiAnswerQuestion) {
                answerEditText.setVisibility(View.INVISIBLE);
                choiceRadioGroup.setVisibility(View.VISIBLE);
                ArrayList choices = ((MultiAnswerQuestion) currentQuestion).getChoices();
                for (int i = 0; i < choices.size(); i++) {
                    CheckBox nextChoice = new CheckBox(this);
                    nextChoice.setText(choices.get(i).toString());
                    choiceRadioGroup.addView(nextChoice, i);
                }
            } else {
                answerEditText.setVisibility(View.VISIBLE);
                choiceRadioGroup.setVisibility(View.INVISIBLE);
            }
        }
    }


    /**
     * Gets user answer to question
     *
     * @return String answer that user has put in EditText R.id.question_answer
     */
    private String getUserAnswer() {
        String answer = answerEditText.getText().toString();
        return answer;
    }

    /**
     * Manage what happens when user clicks Next button
     *
     * @param v button Next
     */
    public void nextQuestion(View v) {
        submitPressed = false;
        nextButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        choiceRadioGroup.removeAllViews();
        nextQuestion();
    }

    public void submitQuestion(View v) {
        nextButton.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.GONE);
        submitPressed = true;
        if (currentQuestion instanceof MultiChoiceQuestion) {
            checkMultiQuestion();
        } else if (currentQuestion instanceof MultiAnswerQuestion) {
            checkMultiAnswerQuestion();
        } else {
            checkSimpleQuestion();
        }
    }

    /**
     * Restart the quiz and try again
     *
     * @param v
     */
    public void tryAgain(View v) {
        quizEnd = false;
        score = 0;
        questionNumber = 1;
        populateQuestionArrayList();
        tryAgain.setVisibility(View.GONE);
        submitButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);
        choiceRadioGroup.setVisibility(View.INVISIBLE);
        scoreRatingBar.setVisibility(View.GONE);
        questionNumberTextView.setVisibility(View.VISIBLE);
        answerEditText.setVisibility(View.INVISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        displayQuestion();
        scoreTextView.setText("Your score is: " + score + "/10");
    }

    /**
     * Simple question method
     */
    private void checkSimpleQuestion() {
        String answer = getUserAnswer();
        //* check answer in question class and if it is good then give points to user
        addScore(answer);
        answerEditText.setText(currentQuestion.getAnswer());
        if (answer.compareTo(currentQuestion.getAnswer()) == 0) {
            answerEditText.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            answerEditText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    /**
     * Multi question method
     */
    private void checkMultiQuestion() {
        String answer = "";
        RadioButton nextChoice;
        for (int i = 0; i < choiceRadioGroup.getChildCount(); i++) {
            nextChoice = (RadioButton) choiceRadioGroup.getChildAt(i);
            nextChoice.setEnabled(false);
            if (nextChoice.isChecked()) {
                answer = "" + (i + 1);
                if (answer.compareTo(currentQuestion.getAnswer()) == 0) {
                    nextChoice.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    nextChoice.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        }
        int i = Integer.parseInt(currentQuestion.getAnswer());
        nextChoice = (RadioButton) choiceRadioGroup.getChildAt(i - 1);
        nextChoice.setTextColor(getResources().getColor(R.color.colorAccent));
        addScore(answer);
    }

    /**
     * Multi answer question method
     */
    private void checkMultiAnswerQuestion() {
        String answer = "";
        CheckBox nextChoice;
        for (int i = 0; i < choiceRadioGroup.getChildCount(); i++) {
            nextChoice = (CheckBox) choiceRadioGroup.getChildAt(i);
            nextChoice.setEnabled(false);
            if (((CheckBox) choiceRadioGroup.getChildAt(i)).isChecked()) {
                answer += (i + 1);//make answer string like "23" if second and third answer are correct
                if (currentQuestion.getAnswer().contains(answer)) {
                    nextChoice.setTextColor(getResources().getColor(R.color.colorAccent));
                } else {
                    nextChoice.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            if (currentQuestion.getAnswer().contains("" + (i + 1))) {
                nextChoice.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }
        addScore(answer);
    }

    /**
     * show next question on screen
     */
    private void nextQuestion() {

        if (questionNumber >= NUMBER_OF_QUESTIONS) { //** check if this is last question and than show score
            endOfQuiz();
        } else {
            removeQuestionFromList(); //*first remove current question from list
            questionNumber++; //* increase number of question counter
            displayQuestion(); //* now get and show next question
        }
    }

    /**
     * increase score if answer is correct
     *
     * @param answer is String answer from user
     */
    private void addScore(String answer) {
        int i = currentQuestion.checkAnswer(answer);
        if (i == 0) {
            score++;
            Toast.makeText(this, "That is correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Don't worry, you will get next one", Toast.LENGTH_SHORT).show();
        }
        scoreTextView.setText("Your score is: " + score + "/10");
    }


    /**
     * Removes current randomly chosen question from ArrayList questions
     * because we dont want to repeat questions
     */
    private void removeQuestionFromList() {
        if (randomIndex < questions.size()) {
            questions.remove(randomIndex);
        } else {
            scoreTextView.setText("Error: no more questions");
        }
    }

    /**
     * manage what happens when user answers on all questions
     */

    private void endOfQuiz() {
        quizEnd = true;
        tryAgain.setVisibility(View.VISIBLE);
        scoreRatingBar.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        choiceRadioGroup.setVisibility(View.GONE);
        questionNumberTextView.setVisibility(View.GONE);
        answerEditText.setVisibility(View.GONE);
        int stars = score / (NUMBER_OF_QUESTIONS / 5);
        scoreRatingBar.setRating(stars);
        switch (stars) {
            case 5:
                questionTextView.setText("You are the best of the best! Perfect score! Can you do it again?");
                break;
            case 4:
                questionTextView.setText("OMG Almost perfect score.You are awesome!");
                break;
            case 3:
                questionTextView.setText("Excellent score! Keep up the good work.");
                break;
            case 2:
                questionTextView.setText("Good job! I bet you can do better...");
                break;
            case 1:
                questionTextView.setText("You are trying. Practice more and make perfect score!");
                break;
            default:
                questionTextView.setText("You did this on purpose!");
                break;
        }
    }


}

