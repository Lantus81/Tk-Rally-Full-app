package com.akristic.www.tkrally;

import android.support.v4.content.ContextCompat;
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
        questions = new ArrayList<>();
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
            scoreTextView.setText(R.string.quiz_current_score_is + score + "/10");


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
        questions.add(new Question(getString(R.string.Q_1_text), getString(R.string.Q_1_answer_1)));
        questions.add(new Question(getString(R.string.Q_2_text), getString(R.string.Q_2_answer_2)));
        questions.add(new Question(getString(R.string.Q_3_text), getString(R.string.Q_3_answer)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_1_text), "1", getString(R.string.MQ_choice_true), getString(R.string.MQ_choice_false)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_2_text), "2", getString(R.string.MQ_choice_true), getString(R.string.MQ_choice_false)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_3_text), "2", getString(R.string.MQ_choice_true), getString(R.string.MQ_choice_false)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_4_text), "2", getString(R.string.MQ_choice_true), getString(R.string.MQ_choice_false)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_5_text), "1", getString(R.string.MQ_choice_true), getString(R.string.MQ_choice_false)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_6_text), "2", getString(R.string.MQ_6_answer_1), getString(R.string.MQ_6_answer_2), getString(R.string.MQ_6_answer_3), getString(R.string.MQ_6_answer_4)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_7_text), "4", getString(R.string.MQ_7_answer_1), getString(R.string.MQ_7_answer_2), getString(R.string.MQ_7_answer_3), getString(R.string.MQ_7_answer_4)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_8_text), "4", getString(R.string.MQ_8_answer_1), getString(R.string.MQ_8_answer_2), getString(R.string.MQ_8_answer_3), getString(R.string.MQ_8_answer_4)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_9_text), "2", getString(R.string.MQ_9_answer_1), getString(R.string.MQ_9_answer_2), getString(R.string.MQ_9_answer_3), getString(R.string.MQ_9_answer_4)));
        questions.add(new MultiChoiceQuestion(getString(R.string.MQ_10_text), "3", getString(R.string.MQ_10_answer_1), getString(R.string.MQ_10_answer_2), getString(R.string.MQ_10_answer_3), getString(R.string.MQ_10_answer_4)));
        questions.add(new MultiAnswerQuestion(getString(R.string.MQ_11_text), "25", getString(R.string.MQ_11_answer_1), getString(R.string.MQ_11_answer_2), getString(R.string.MQ_11_answer_3), getString(R.string.MQ_11_answer_4), getString(R.string.MQ_11_answer_5)));
        questions.add(new MultiAnswerQuestion(getString(R.string.MQ_12_text), "123", getString(R.string.MQ_12_answer_1), getString(R.string.MQ_12_answer_2), getString(R.string.MQ_12_answer_3)));
        questions.add(new MultiAnswerQuestion(getString(R.string.MQ_13_text), "124", getString(R.string.MQ_13_answer_1), getString(R.string.MQ_13_answer_2), getString(R.string.MQ_13_answer_3), getString(R.string.MQ_13_answer_4)));
        questions.add(new MultiAnswerQuestion(getString(R.string.MQ_14_text), "34", getString(R.string.MQ_14_answer_1), getString(R.string.MQ_14_answer_2), getString(R.string.MQ_14_answer_3), getString(R.string.MQ_14_answer_4), getString(R.string.MQ_14_answer_5)));
        questions.add(new MultiAnswerQuestion(getString(R.string.MQ_15_text), "12", getString(R.string.MQ_15_answer_1), getString(R.string.MQ_15_answer_2), getString(R.string.MQ_15_answer_3), getString(R.string.MQ_15_answer_4)));
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
            questionNumberTextView.setText(getString(R.string.question_number) + questionNumber + "/" + NUMBER_OF_QUESTIONS);
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
        return answerEditText.getText().toString();
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
        scoreTextView.setText(getString(R.string.quiz_current_score_is) + score + "/10");
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
            answerEditText.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        } else {
            answerEditText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
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
                    nextChoice.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                } else {
                    nextChoice.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                }
            }
        }
        int i = Integer.parseInt(currentQuestion.getAnswer());
        nextChoice = (RadioButton) choiceRadioGroup.getChildAt(i - 1);
        nextChoice.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
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
                    nextChoice.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                } else {
                    nextChoice.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                }
            }
            if (currentQuestion.getAnswer().contains("" + (i + 1))) {
                nextChoice.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
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
            Toast.makeText(this, R.string.that_is_correct, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.dont_worry_u_get_next_one, Toast.LENGTH_SHORT).show();
        }
        scoreTextView.setText(getString(R.string.quiz_current_score_is) + score + "/10");
    }


    /**
     * Removes current randomly chosen question from ArrayList questions
     * because we dont want to repeat questions
     */
    private void removeQuestionFromList() {
        if (randomIndex < questions.size()) {
            questions.remove(randomIndex);
        } else {
            scoreTextView.setText(R.string.error_no_questions);
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
                questionTextView.setText(R.string.best_5_star_message);
                break;
            case 4:
                questionTextView.setText(R.string.best_4_star_message);
                break;
            case 3:
                questionTextView.setText(R.string.best_3_star_message);
                break;
            case 2:
                questionTextView.setText(R.string.best_2_star_message);
                break;
            case 1:
                questionTextView.setText(R.string.best_1_star_message);
                break;
            default:
                questionTextView.setText(R.string.best_0_star_message);
                break;
        }
    }


}

