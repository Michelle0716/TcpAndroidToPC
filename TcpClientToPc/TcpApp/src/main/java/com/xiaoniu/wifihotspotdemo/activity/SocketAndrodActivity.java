package com.xiaoniu.wifihotspotdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaoniu.wifihotspotdemo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * author Created by michelle on 2019/5/23.
 * email: 1031983332@qq.com
 * TCP局域网通信，需要手机端和PC端都处于统一网络的情况下
 */

public class SocketAndrodActivity extends Activity {

    private static final int CONNECTING = 0;
    private static final int SENDING = 1;
    private static final int CLOSE = 2;
    private static final int RECEIVE = 3;

    TextView text;
    EditText input, address, port;
    Socket socket;

    String addStr, sendMsg, receiveMsg;
    int portStr;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button button = (Button) this.findViewById(R.id.btn_send);
        text = (TextView) findViewById(R.id.receive);
        input = (EditText) findViewById(R.id.input);
        address = (EditText) findViewById(R.id.address);
        port = (EditText) findViewById(R.id.port);
        address.setText("172.30.202.199");
        port.setText("3333");
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addStr = address.getText().toString().trim();
                portStr = Integer.parseInt(port.getText().toString().trim());
                new WorkThread().start();
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CONNECTING) {
                text.setText("正在连接中......");
            } else if (msg.what == SENDING) {
                text.setText("Client Sending: '" + sendMsg + "'");
            } else if (msg.what == CLOSE) {
                text.append("socket close");
            } else if (msg.what == RECEIVE) {
                text.setText("Client Receiveing: '" + receiveMsg + "'");
            }


        }
    };


    //工作线程
    private class WorkThread extends Thread {
        @Override
        public void run() {
            //处理比较耗时的操作
            //数据处理完成后，关于UI的更新要通过handler发送消息
            Message msg = new Message();
            Message msg1 = new Message();
            Message msg2 = new Message();
            Message msg3 = new Message();
            msg.what = CONNECTING;
            handler.sendMessage(msg);
            try {
                socket = new Socket(addStr, portStr);
                if (socket == null) {
                    Log.e("error", "socket  null");
                    return;
                }
                //发送给服务端的消息
                sendMsg = input.getText().toString();
                msg1.what = SENDING;
                handler.sendMessage(msg1);
                //socket.getOutputStream  out是个字符输出流，后面true说明执行后自动刷新
                PrintWriter out = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())), true);
                out.println(sendMsg);


                // 接收服务器信息
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                // 得到服务器信息
                receiveMsg = in.readLine();
                msg3.what = RECEIVE;
                handler.sendMessage(msg3);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //关闭Socket
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                msg2.what = CLOSE;
                handler.sendMessage(msg2);
            }


        }
    }


}
