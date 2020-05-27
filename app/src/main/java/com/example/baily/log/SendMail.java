package com.example.baily.log;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "bailyyy2020@gmail.com"; // 보내는 계정의 id
    String password = "qpdlffl5!";// 보내는 계정의 pw
    String radomNum = "";//난수 생성하여 담아지는 변수


    public void sendSecurityCode(Context context, String sendTo) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("인증링크 입니다", "인증 번호 : " + RandomNum(), sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //난수 생성함수
    public static String numberGen(int len) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<len;i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
            //numStr + ran >> append

        }

        return numStr;
    }

    public String RandomNum()
    {
        radomNum = numberGen(6);
        return radomNum;
    }
}
