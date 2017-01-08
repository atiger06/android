package com.example.geoquiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

    private static final String EXTRA_ANSWER_IS_TRUE="com.example.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN="com.example.geoquiz.answer_shown";
    private static final String  EXTRA_QUESTION_NUMBER="question_number";

    private static  boolean mAnswerIsTrue;
    private static boolean mCheatIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private static int number=-1;


    public static Intent newIntent(Context packageContext,boolean answerIsTrue,int index){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        i.putExtra(EXTRA_QUESTION_NUMBER,index);
        return i;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);

        // Handle the data that comes from QuizActivity
        if(getIntent() != null) {
            mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
            number = getIntent().getIntExtra("question_number",-1);
            getIntent().getExtras().clear();
            Log.d("Cheat Activity","getIntent清理后:---> "+getIntent().getExtras().toString());
        }

        // Handle the data that comes from this Activity after it was stopped and created again
        if(savedInstanceState != null){
            mCheatIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_SHOWN,false);
            mAnswerIsTrue = savedInstanceState.getBoolean(EXTRA_ANSWER_IS_TRUE,false);
            number =  savedInstanceState.getInt("number",-1);
            setAnswerShownResult(mCheatIsTrue);
            Log.d("Cheat activity","savedInstanceState-->"+savedInstanceState.toString());

            // this processing is used to showing the answer after cheated and activity recreated
            // 下面处理用于在作弊后且由于旋转屏幕等因素导致本activity重建时，显示问题答案
            if(mCheatIsTrue){
                if(mAnswerIsTrue)
                {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
            }
        }

        mShowAnswer = (Button)findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mAnswerIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mCheatIsTrue=true;
                //Called the callback method with args after cheated
                setAnswerShownResult(true);
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown){
        //mCheatIsTrue = isAnswerShown;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        data.putExtra(EXTRA_QUESTION_NUMBER,number);
        setResult(RESULT_OK,data);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(EXTRA_ANSWER_IS_TRUE,mCheatIsTrue);
        savedInstanceState.putBoolean(EXTRA_ANSWER_SHOWN,mAnswerIsTrue);
        savedInstanceState.putInt("number",number);
        mCheatIsTrue = false;
    }
}
