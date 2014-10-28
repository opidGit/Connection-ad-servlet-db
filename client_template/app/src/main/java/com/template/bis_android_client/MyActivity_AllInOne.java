package com.template.bis_android_client;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class MyActivity_AllInOne extends Activity {
    EditText edSendingData;
    TextView tvReceiveData;
    Button btnSubmit;

    String data;
    String receivedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        findViewsById();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date(System.currentTimeMillis()));
        edSendingData.setText(currentTime);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = edSendingData.getText().toString();
                RequestWebServiceTask task = new RequestWebServiceTask();
                task.execute(null, null, null);
                tvReceiveData.setText(receivedData);
            }
        });
    }

    private void findViewsById() {
        edSendingData = (EditText) findViewById(R.id.ed_sending);
        tvReceiveData = (TextView) findViewById(R.id.tv_received);
        btnSubmit = (Button) findViewById(R.id.bt_submit);
    }

    public Handler uiChangingHandler = new Handler() {
        public void handleMessage(Message msg) {
            tvReceiveData.setText(receivedData);
        }
    };

    private class RequestWebServiceTask extends AsyncTask<String, Object, HttpResponse> {
        final String APP_TAG = "Request WebService";
        private String scheme = "http";
        private String host = "127.0.0.1";
        private int port = 80;
        private String path = "template_server/GetData";

        @Override
        protected HttpResponse doInBackground(String... urls) {
            HttpResponse response = requestGetHttp();
            return response;
        }

        // Request Get method
        private HttpResponse requestGetHttp() {
            URI uri = null;
            HttpGet httpGet = null;
            HttpResponse response = null;

            // Add parameters
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("data", data));
            String qParams = URLEncodedUtils.format(nameValuePairs, "UTF-8");


            /* 전송하는 부분 */
            // setConnectionTimeout : 클라이언트가 요청을 보냈을 때 서버가 응답하는 시간의 한도 설정
            // 요청이 있지만 서버가 응답이 없는 경우
            // setSoTimeout : 일정시간 클라이언트와 서버와의 교신이 없다면 소켓의 연결을 끊음.
            // 둘 사이에 아무런 요청이나 응답이 없는 경우
            try {
                HttpClient client = new DefaultHttpClient();
                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, 5000);
                HttpConnectionParams.setSoTimeout(params, 5000);

                uri = URIUtils.createURI(scheme, host, port, path, qParams, null);
                httpGet = new HttpGet(uri);


                response = client.execute(httpGet); // 발신

                HttpEntity httpEntity = response.getEntity(); // 수신
                String output = EntityUtils.toString(httpEntity);

                receivedData = output.toString();
                uiChangingHandler.sendEmptyMessage(0);


                // 상태확인
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
                Log.e("Exception", "URISyntaxException");
            } catch (IOException e) {
                Log.e("Exception", "IOException");
            } catch (Exception e) {

            }


            return response;

        }

        // TODO: post 방식은 아직 실험 안해봄
        private HttpResponse requestPostHttp() {
            HttpResponse response = null;

            return response;
        }

        @Override
        protected void onPostExecute(HttpResponse result) {
            // 백그다운드 작업이 끝난 후 UI 스레드에서 실행된다. 인수로 작업의 결과가 전달되는데
            // 취소되었거나 예외가 발생했으면 null이 전달된다.
        }
    }
}
