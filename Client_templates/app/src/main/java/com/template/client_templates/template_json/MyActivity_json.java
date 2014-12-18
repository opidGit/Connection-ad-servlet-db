package com.template.client_templates.template_json;

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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;

public class MyActivity_json extends Activity {

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

    /**
     * ***********************
     * 서버로부터 받은 데이터를 문자열로 변환하여 받은 곳이다.
     * 이 곳에서 데이터를 알맞게 사용하면된다.
     * ***********************
     */
    TemplateServiceListener listener = new TemplateServiceListener() {
        @Override
        public void onTemplateActionComplete(String args) {
            tvReceiveData.setText(args);
        }
    };

    private void findViewsById() {
        edSendingData = (EditText) findViewById(R.id.ed_sending_json);
        tvReceiveData = (TextView) findViewById(R.id.tv_received_json);
        btnSubmit = (Button) findViewById(R.id.bt_submit_json);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity_json);
        findViewsById();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> param = new ArrayList<String>();

                param.add(edSendingData.getText().toString());

                /* 1. execute */
                HTTPServiceTask task = new HTTPServiceTask(getApplicationContext(), listener);
                task.execute(param);
            }
        });
    }


    /* Class and Interface for use at http request */
    private interface TemplateServiceListener {
        public void onTemplateActionComplete(String args);

    }


    /**
     * ***********************
     * HTTP 웹서버와 통신하기 *
     * ***********************
     */
    private class HTTPServiceTask extends AsyncTask<ArrayList<String>, Object, String> {
        final String APP_TAG = "TemplateServiceTask";
        private String scheme = "http";
        private String host = "203.247.240.62";
        private int port = 80;
        private String path = "server_template/GetDataJSON";

        private Context mContext = null;
        private TemplateServiceListener mlistener = null;

        HTTPServiceTask(Context context, TemplateServiceListener listener) {
            this.mContext = context;
            this.mlistener = listener;
        }

        @Override
        protected String doInBackground(ArrayList<String>... params) {
            return requestPOST(params[0]);
        }

        /**
         * ****************
         * POST 방식 사용 *
         * ****************
         */
        private String requestPOST(ArrayList<String> parameters) {
            final String TAG = "REQUEST_POST_HTTP_JSON";

            InputStream inputStream = null;
            URI uri = null;
            String result = "";
            HttpClient httpClient = new DefaultHttpClient();

            try {

                /**
                 * *************
                 * 연결 *
                 * *************
                 */
                uri = URIUtils.createURI(scheme, host, port, path, null, null);
                HttpPost httpPost = new HttpPost(uri);

                /**
                 * **************
                 * 파라미터 추가 *
                 * **************
                 */
                JSONObject jobj = new JSONObject();
                jobj.put("data", parameters.get(0));
                StringEntity se = new StringEntity(jobj.toString());

                /**
                 * *************
                 * 각종 시간 설정 *
                 * *************
                 */
                HttpParams params = httpClient.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000); // 서버가 응답하는 시간 한도
                HttpConnectionParams.setSoTimeout(params, 5000); // 서버가 응답하지 않는 경우

                /**
                 * *************
                 * 데이터 발신 *
                 * *************
                 */
                httpPost.setEntity(se);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                HttpResponse httpResponse = httpClient.execute(httpPost);

                /**
                 * *************
                 * 데이터 수신 *
                 * *************
                 */
                inputStream = httpResponse.getEntity().getContent();

                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            return result;

        }


        /* 3. Return to Activity. */
        @Override
        protected void onPostExecute(String result) {
            // It returns the results to the Activity that called this class.
            mlistener.onTemplateActionComplete(result);
        }

    }


    /**
     * **************************************************
     * 서버로부터 받은 데이터를 문자열로 바꾸어주는 메서드 *
     * **************************************************
     */
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}
