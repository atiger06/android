package com.example.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.CheatActivity;
import com.example.geoquiz.Question;
import com.example.geoquiz.R;

import java.util.Arrays;

public class QuizActivity extends Activity {

    private TextView mQuestionTextView;
    private boolean mIsCheater;
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 1;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans,true),
            new Question(R.string.question_mideast,false),
            new Question(R.string.question_africa,false),
            new Question(R.string.question_americas,true),
            new Question(R.string.question_asia,true),
    };
    private static int[] mCheatNumber = new int[]{0,0,0,0,0};
    private int mCurrentIndex = 0;

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResID();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(mCheatNumber[mCurrentIndex] == 1 ){
            messageResId = R.string.judgment_toast;
        }
        else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Restore the data from Bundle if it is not null
        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mCheatNumber = savedInstanceState.getIntArray("cheatarray");
        }

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);

        Button true_button = (Button) findViewById(R.id.true_button);
        true_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        Button false_button = (Button) findViewById(R.id.false_button);
        false_button.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        checkAnswer(false);
                    }
                });

        ImageButton mPrevButton = (ImageButton)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mQuestionBank.length + mCurrentIndex - 1)% mQuestionBank.length;
                updateQuestion();
            }
        });

        ImageButton mNextButton = (ImageButton)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                updateQuestion();
            }
        });


        Button mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this,answerIsTrue,mCurrentIndex);
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHEAT)
        {
            // the value of mIsCheater comes from the result returned from CheatActivity();
            mIsCheater = data.getBooleanExtra("com.example.geoquiz.answer_shown",false);
            //Make sure that the right question number after returning from
            mCurrentIndex = data.getIntExtra("question_number",0);

            // if mIsCheater is true, resume the cheat number array and then let the mIsCheater be false;
            if(mIsCheater)
            {
                mCheatNumber[mCurrentIndex] = 1;
                mIsCheater = false;
            }
        }

    }


    // Before this activity is killed, the method will save the current state ,so it can restore its state in the future .
    // the "KEY_INDEX" represents the number of the current question;
    // the "cheatarray" represents the array of cheat or not, the value "1" represents cheating and "0" does not;
     @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX,mCurrentIndex);
        savedInstanceState.putIntArray("cheatarray",mCheatNumber);
    }
}
