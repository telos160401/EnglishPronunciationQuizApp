package com.example.android.englishpronunciationquiz;

import android.media.MediaPlayer;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class RorLActivity extends AppCompatActivity {

    //メディアプレイヤー
    private MediaPlayer mMediaPlayer;
    //クイズ番号
    private int quizNum = 0;
    //正解数
    private int correctNum = 0;
    //rの音声ファイルのリソースID配列
    private int[] rResourceIds;
    //lの音声ファイルのリソースID配列
    private int[] lResourceIds;
    //20回分の問題音声のリソースIDを格納する配列
    private int[] questions;
    //20回分の正解の選択肢を格納する配列
    private int[] answers;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    //ゲームがスタートしたかどうかを表す
    private boolean start = false;

    //再生が終わったらメディアプレイヤーを解放
    private MediaPlayer.OnCompletionListener mMediaCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
        }
    };

    //以下の英語コメントのメディアプレイヤーの解放用メソッドはUdacityのAndroidコース(https://www.udacity.com/course/android-basics-multiscreen-apps--ud839)で提供されたものを使用
    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //rの音声リソースIDの格納
        rResourceIds = new int[7];
        rResourceIds[0] = R.raw.pronunciation_us_male_vote3_arrow_1;
        rResourceIds[1] = R.raw.pronunciation_au_male_vote0_arrow_2;
        rResourceIds[2] = R.raw.pronunciation_au_male_vote1_arrow_1;
        rResourceIds[3] = R.raw.pronunciation_ca_male_vote0_arrow_1;
        rResourceIds[4] = R.raw.pronunciation_uk_female_vote2_arrow_1;
        rResourceIds[5] = R.raw.pronunciation_us_female_vote0_arrow_1;
        rResourceIds[6] = R.raw.pronunciation_us_female_vote0_arrow_2;

        //lの音声リソースIDの格納
        lResourceIds = new int[8];
        lResourceIds[0] = R.raw.pronunciation_au_male_vote0_allow_1;
        lResourceIds[1] = R.raw.pronunciation_uk_female_vote4_allow_1;
        lResourceIds[2] = R.raw.pronunciation_uk_male_vote1_allow_1;
        lResourceIds[3] = R.raw.pronunciation_us_male_vote5_allow_1;
        lResourceIds[4] = R.raw.pronunciation_us_female_vote7_allow_1;
        lResourceIds[5] = R.raw.pronunciation_us_male_vote0_allow_3;
        lResourceIds[6] = R.raw.pronunciation_us_male_vote0_allow_4;
        lResourceIds[7] = R.raw.pronunciation_us_male_vote2_allow_2;

        //20回分のクイズを作成
        answers = new int[20];
        questions = new int[20];
        Random rnd = new Random();
        //左と右のどちらが正解か乱数で決め、ランダムに問題音声を決める
        for(int i=0; i<20; i++){
            answers[i] = rnd.nextInt(2);
            if(answers[i] == LEFT){
                questions[i] = lResourceIds[rnd.nextInt(lResourceIds.length)];
            }
            else if(answers[i] == RIGHT){
                questions[i] = rResourceIds[rnd.nextInt(rResourceIds.length)];
            }
        }

        //左のボタンに文字を設定
        final Button buttonLeft = findViewById(R.id.button_left);
        buttonLeft.setVisibility(View.VISIBLE);
        buttonLeft.setText(R.string.r);

        //右のボタンに文字を設定
        final Button buttonRight = findViewById(R.id.button_right);
        buttonRight.setVisibility(View.VISIBLE);
        buttonRight.setText(R.string.l);

        //クイズ番号を表示
        final TextView textviewQuizNum = findViewById(R.id.quiz_number);
        textviewQuizNum.setVisibility((View.VISIBLE));
        textviewQuizNum.setText(getString(R.string.quiz_number, quizNum + 1));

        //正解率を表示
        final TextView textviewCorrectRatio = findViewById(R.id.correct_ratio);
        textviewCorrectRatio.setVisibility(View.VISIBLE);
        textviewCorrectRatio.setText(getString(R.string.quiz_correct_ratio, correctNum));

        //〇×の非表示
        final TextView textviewMarubatu = findViewById(R.id.marubatu);
        textviewMarubatu.setVisibility(View.INVISIBLE);

        //スタートボタンの表示（もう一度聞くボタンと兼用）
        final Button buttonReplay = findViewById(R.id.button_replay);
        buttonReplay.setVisibility(View.VISIBLE);
        buttonReplay.setText(getString(R.string.quiz_button_start));

        //左ボタンクリック
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!start){
                    Toast.makeText(RorLActivity.this, "スタートを押してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                //正解の場合
                if(answers[quizNum] == LEFT){
                    textviewMarubatu.setText("〇");
                    correctNum += 1;
                }
                //不正解の場合
                else{
                    textviewMarubatu.setText("×");
                }

                quizNum += 1;
                if(quizNum == 19){
                    textviewQuizNum.setVisibility(View.GONE);
                    buttonLeft.setVisibility(View.GONE);
                    buttonRight.setVisibility(View.GONE);
                    buttonReplay.setVisibility(View.GONE);
                    textviewMarubatu.setText(correctNum + "問正解");
                    textviewMarubatu.setTextSize(32);
                }

                //テキストビューの更新
                textviewMarubatu.setVisibility(View.VISIBLE);
                textviewQuizNum.setText(getString(R.string.quiz_number, quizNum + 1));
                textviewCorrectRatio.setText(getString(R.string.quiz_correct_ratio, correctNum));

                //次の問題音声の再生
                releaseMediaPlayer();
                mMediaPlayer = MediaPlayer.create(RorLActivity.this, questions[quizNum]);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mMediaCompletionListener);
            }
        });

        //右ボタンクリック
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!start){
                    Toast.makeText(RorLActivity.this, "スタートを押してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                //正解の場合
                if(answers[quizNum] == RIGHT){
                    textviewMarubatu.setText("〇");
                    correctNum += 1;
                }
                //不正解の場合
                else{
                    textviewMarubatu.setText("×");
                }

                quizNum += 1;
                if(quizNum == 19){
                    textviewQuizNum.setVisibility(View.GONE);
                    buttonLeft.setVisibility(View.GONE);
                    buttonRight.setVisibility(View.GONE);
                    buttonReplay.setVisibility(View.GONE);
                    textviewMarubatu.setText(correctNum + "問正解");
                    textviewMarubatu.setTextSize(32);
                }

                //テキストビューの更新
                textviewMarubatu.setVisibility(View.VISIBLE);
                textviewQuizNum.setText(getString(R.string.quiz_number, quizNum + 1));
                textviewCorrectRatio.setText(getString(R.string.quiz_correct_ratio, correctNum));

                //次の問題音声の再生
                releaseMediaPlayer();
                mMediaPlayer = MediaPlayer.create(RorLActivity.this, questions[quizNum]);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mMediaCompletionListener);
            }
        });

        //もう一度聞くボタンをクリック
        buttonReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ボタンのテキストをスタートからもう一度聞くに変更
                if(!start) {
                    buttonReplay.setText(getString(R.string.quiz_button_replay));
                    start = true;
                }
                //問題音声の再生
                releaseMediaPlayer();
                mMediaPlayer = MediaPlayer.create(RorLActivity.this, questions[quizNum]);
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mMediaCompletionListener);
            }
        });
    }
}