package com.template.client_templates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.template.client_templates.template_1.MyActivity;
import com.template.client_templates.template_2.MyActivity_combine;

public class StartActivity extends Activity {
    LinearLayout ml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ml = (LinearLayout) findViewById(R.id.mainlayout);

        createButton("template 1", MyActivity.class);
        createButton("template 2", MyActivity_combine.class);

    }

    private void createButton(String title, final Class<?> cls) {
        Button bt1 = new Button(this);
        bt1.setText(title);

        bt1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ml.addView(bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(), cls);
                startActivity(it);
                //finish();
            }
        });

    }


}
