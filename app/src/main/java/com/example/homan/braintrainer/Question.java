package com.example.homan.braintrainer;

import android.util.Log;

import java.util.Random;

/**
 * Created by Homan on 8/22/2017.
 * Question class will generate a simple addition question.
 *
 * Features:
 *  1. Automatically generate adder 1 and adder 2 (0 to max input)
 *  2. Automatically generate four choices:
 *      a, randomly set answer location in the choices
 *      b, randomly generate three other boggies in range of
 *          0 to 2 x (sum of adder 1 and adder 2)
 */

public class Question {

    public int adder1;
    public int adder2;
    //correct answer in choices array
    private int answer;

    //answers contains the one correct one and three boggies
    public int[] choices;

    public Question(int max) {
        //initial choices array
        choices = new int[4];

        for (int i=0; i<4; i++) {
            choices[i] = 0;
        }

        generateAdderNChoise(max);
    }

    public String getEquation() {
        return (String.valueOf(adder1) + "+" + String.valueOf(adder2));
    }

    public int getAnswer() {
        return answer;
    }

    public void generateAdderNChoise(int max) {
        //Log.i("qms in Question ", "generating choices");

        Random rand = new Random();
        adder1 = rand.nextInt(max);
        adder2 = rand.nextInt(max);

        int sum = adder1+adder2;
        //answer location in four multiple choices
        answer = rand.nextInt(4);
        choices[answer] = sum;
        //Double sum for guessing
        int expandRange = sum * 2;
        //for small number < 10
        if (expandRange < 10) {
            expandRange = 20;
        }

        Log.i("sms begin", "check");
        for (int i = 0; i < 4; i++) {

            int boggie = rand.nextInt(expandRange);

            //remove duplicate boggie but cannot check 0
            for (int k = 0; k < 4; k++) {
                while ((boggie == choices[k]) ||
                        (boggie == adder1) ||
                        (boggie == adder2) ||
                        (boggie == sum)) {
                    boggie = rand.nextInt(expandRange);
                    Log.i("sgs boggie: ", String.valueOf(boggie)+" : adder1->" +
                        String.valueOf(adder1)+ " : adder2->"+
                        String.valueOf(adder2)+ " : sum->"+
                        String.valueOf(sum));
                }
            }
            if (i != answer) {
                choices[i] = boggie;
            }
            Log.i("sms boggie set: ", String.valueOf(i)+ ". Boggie: "+String.valueOf(boggie));

        }//end for
    }

    public String choiceA() {
        return String.valueOf(choices[0]);
    }

    public String choiceB() {
        return String.valueOf(choices[1]);
    }

    public String choiceC() {
        return String.valueOf(choices[2]);
    }

    public String choiceD() {
        return String.valueOf(choices[3]);
    }
}