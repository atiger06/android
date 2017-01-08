package com.example.geoquiz;

/**
 * Created by tiger on 2016/12/31.
 */

public class Question {
  private int mTextResID;
  private boolean mAnswerTrue;

  public Question(int textResID,boolean answerTrue){
    mTextResID = textResID;
    mAnswerTrue = answerTrue;
  }

  public int getTextResID() {
    return mTextResID;
  }

  public void setTextResID(int textResID) {
    mTextResID = textResID;
  }

  public boolean isAnswerTrue() {
    return mAnswerTrue;
  }

  public void setAnswerTrue(boolean answerTrue) {
    mAnswerTrue = answerTrue;
  }
}
