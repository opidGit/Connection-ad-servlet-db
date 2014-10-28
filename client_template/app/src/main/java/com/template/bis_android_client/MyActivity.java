package com.template.bis_android_client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MyActivity extends Activity {
    EditText edSendingData;
    TextView tvReceiveData;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findViewsById();

        // TEST
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));
        edSendingData.setText(currentTime);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> param = new ArrayList<String>();

                param.add(edSendingData.getText().toString());
                param.add("Test2");
                param.add("Test3");

                /* 1. execute */
                TemplateServiceTask task = new TemplateServiceTask(getApplicationContext(), listener);
                task.execute(param);
            }
        });
    }

    /* 4. Use data obtained from the server. */
    TemplateServiceListener listener = new TemplateServiceListener() {
        @Override
        public void onTemplateActionComplete(ArrayList<String> arrayList) {
            String response = arrayList.get(0);
            tvReceiveData.setText(response);
        }
    };

    private void findViewsById() {
        edSendingData = (EditText) findViewById(R.id.ed_sending);
        tvReceiveData = (TextView) findViewById(R.id.tv_received);
        btnSubmit = (Button) findViewById(R.id.bt_submit);
    }

    // This UI handler is not use, because of the TemplateServiceListener.
    public Handler uiChangingHandler = new Handler() {
        public void handleMessage(Message msg) {
            // UI control
            // tvReceiveData.setText(receivedData);
        }
    };

}
