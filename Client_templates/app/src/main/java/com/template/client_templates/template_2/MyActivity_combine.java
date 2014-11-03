package com.template.client_templates.template_2;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.template.client_templates.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyActivity_combine extends Activity {

    EditText edSendingData;
    TextView tvReceiveData;
    Button btnSubmit;


    // This UI handler is not use, because of the TemplateServiceListener.
    public Handler uiChangingHandler = new Handler() {
        public void handleMessage(Message msg) {
            // UI control
            // tvReceiveData.setText(receivedData);
        }
    };

    /* 4. Use data obtained from the server. */
    TemplateServiceListener listener = new TemplateServiceListener() {
        @Override
        public void onTemplateActionComplete(ArrayList<String> arrayList) {
            String response = arrayList.get(0);
            tvReceiveData.setText(response);
        }
    };

    private void findViewsById() {
        edSendingData = (EditText) findViewById(R.id.ed_sending_2);
        tvReceiveData = (TextView) findViewById(R.id.tv_received_2);
        btnSubmit = (Button) findViewById(R.id.bt_submit_2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity_combine);
        findViewsById();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));
        Log.d("MyActivity", "currentTime : " + currentTime);
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


    /* Class and Interface for use at http request */
    private interface TemplateServiceListener {
        public void onTemplateActionComplete(ArrayList<String> arrayList);
    }

    private class TemplateServiceTask extends AsyncTask<ArrayList<String>, Object, ArrayList<String>> {
        final String APP_TAG = "TemplateServiceTask";
        private String scheme = "http";
        private String host = "127.0.0.1";
        private int port = 80;
        private String path = "server_template/GetData";

        private Context mContext = null;
        private TemplateServiceListener mlistener = null;

        TemplateServiceTask(Context context, TemplateServiceListener listener) {
            this.mContext = context;
            this.mlistener = listener;
        }

        @Override
        protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        /* 2. Call requestGetHttp */
            return requestGetHttp(params[0]);
        }

        // Request Get method
        private ArrayList<String> requestGetHttp(ArrayList<String> parameters) {
            URI uri = null;
            HttpGet httpGet = null;
            HttpResponse response = null;
            HttpClient client = new DefaultHttpClient();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); // http parameter
            ArrayList<String> result = null; // return value


            try {
                ////////////////////////////////////////////////////////////////////
                // Add parameters
                nameValuePairs.add(new BasicNameValuePair("data", parameters.get(0)));
                String qParams = URLEncodedUtils.format(nameValuePairs, "UTF-8");

                ////////////////////////////////////////////////////////////////////
                // set timeout
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000); // 서버가 응답하는 시간 한도
                HttpConnectionParams.setSoTimeout(params, 5000); // 서버가 응답하지 않는 경우

                uri = URIUtils.createURI(scheme, host, port, path, qParams, null);
                httpGet = new HttpGet(uri);

                ////////////////////////////////////////////////////////////////////
                // sending and receive
                response = client.execute(httpGet); // 발신

                HttpEntity httpEntity = response.getEntity(); // 수신
                String output = EntityUtils.toString(httpEntity);

                ////////////////////////////////////////////////////////////////////
                // Parses the results.
                // ArrayList<String> results = resultParsing(output);
                result = new ArrayList<String>();
                result.add(output.toString());

                ////////////////////////////////////////////////////////////////////
                // Check result status of the request.
                if (response.getStatusLine().getStatusCode() == 200) {
                    Log.d(APP_TAG, "HTTP GET succeeded");

                    StringBuilder strBuilder = new StringBuilder();
                    HttpEntity messageEntity = response.getEntity();
                    InputStream is = messageEntity.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = br.readLine()) != null) {
                        strBuilder.append(line);
                    }
                    Log.d(APP_TAG, strBuilder.toString());
                }

            } catch (URISyntaxException e) {
                Log.e("URISyntaxException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

            return result;

        }

        // TODO: post 방식은 아직 실험 안해봄
        private HttpResponse requestPostHttp() {
            HttpResponse response = null;
            return response;
        }

        /* 3. Return to Activity. */
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // It returns the results to the Activity that called this class.
            mlistener.onTemplateActionComplete(result);
        }

    }
}
