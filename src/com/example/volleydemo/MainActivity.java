package com.example.volleydemo;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private RequestQueue Queue;
	private ImageView iv;
	private NetworkImageView netIv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv = (ImageView) findViewById(R.id.volleyimage);
		netIv = (NetworkImageView) findViewById(R.id.network_image_view);
		Queue = MyApplication.getmQueue();
		strReqMethod(Method.GET ,"http://www.baidu.com", null);
		//jsonReqMethod(Method.GET, "http://m.weather.com.cn/data/101010100.html", null);
		//imageRequestMethod("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
		imageLoaderMethod("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
		networkImageViewMethod("http://img.my.csdn.net/uploads/201404/13/1397393290_5765.jpeg");
	}
	
	/**
	 * NetworkImageView的用法
	 * @param url
	 */
	public void networkImageViewMethod(String url){
		ImageLoader loader = new ImageLoader(Queue, new BitmapCache());
		netIv.setImageUrl(url,  loader);  
	}
	
	/**
	 * ImageLoader的用法
	 * @param url
	 */
	public void imageLoaderMethod(String url){
		ImageLoader loader = new ImageLoader(Queue, new BitmapCache());
		//后面两个参数依次是加载中的图片和加载失败的图片
		ImageListener listener = ImageLoader.getImageListener(iv, R.drawable.ic_launcher, R.drawable.ic_launcher);
		loader.get(url, listener);
	}
	/**
	 * ImageRequest的用法
	 * @param url 请求图片的网址
	 */
	public void imageRequestMethod(String url){
		ImageRequest imageReq = new ImageRequest(url, new Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap response) {
				// TODO Auto-generated method stub
				iv.setImageBitmap(response);
			}
		}, 0, 0, Config.RGB_565, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "图片加载失败", Toast.LENGTH_SHORT).show();
			}
		});
		Queue.add(imageReq);
	}
	
	/**
	 * StringRequest方法
	 * @param type 判断是get方法还是post方法
	 * @param url  请求网址
	 * @param map  post方法需要的的数据,无就写null
	 */
	public void strReqMethod(final int type ,String url, final Map<String,String> map){
		StringRequest strReq = new StringRequest(type, url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				Log.d(type + "STRTAG", response);
				//Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				if(type == Method.POST){
					return map;
				}else return super.getParams();
			}
		};
		Queue.add(strReq);
	}
	
	/**
	 * JsonRequest的GET方法
	 * @param type	判断是get方法还是post方法
	 * @param url 请求的网址地址
	 * @param jsonObject post方法需要传递的数据
	 */
	public void jsonReqMethod(final int type, String url, JSONObject jsonObject){
		JsonObjectRequest jsonReq = new JsonObjectRequest(type, url, jsonObject, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				Log.d(type + "JSONTAG", response.toString());
				Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
			}
		}){
			@Override
		    public Map<String, String> getHeaders() throws AuthFailureError {
				if(type == Method.POST){
			        HashMap<String, String> headers = new HashMap<String, String>();
			        headers.put("Accept", "application/json");
			        headers.put("Content-Type", "application/json; charset=UTF-8");
			        return headers;
				}else return super.getHeaders();
		    }
		};
		Queue.add(jsonReq);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
