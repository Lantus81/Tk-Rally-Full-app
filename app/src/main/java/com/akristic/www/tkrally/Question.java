package com.akristic.www.tkrally;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Toni on 2.3.2017..
 */

public class Question implements Parcelable {

    private String mText;
    private String mAnswer;

    public Question(String text, String answer){
        mText=text;
        mAnswer=answer;
    }
    public String getText(){
        return mText;
    }
    public String getAnswer(){
        return mAnswer;
    }
    public void setAnswer(String answer){
        mAnswer=answer;
    }
    public int checkAnswer(String answer){
        answer=answer.toLowerCase();
        return mAnswer.compareTo(answer);
    }

    protected Question(Parcel in) {
        mText = in.readString();
        mAnswer = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
        dest.writeString(mAnswer);
    }

    @SuppressWarnings("unused")
    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

}