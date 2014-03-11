/*
 * Copyright 2013 Edgar Andrés Margffoy Tuay 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;


public class LocationFileReader {
	
	private String url;
	private List<String[]> locationList = new ArrayList<String[]>();
	
	public LocationFileReader(String location)
	{
		url = location;
	}
	
	public List<String[]> getFile() throws ClientProtocolException, IOException
	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httppost = new HttpGet(url);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity ht = response.getEntity();

        BufferedHttpEntity buf = new BufferedHttpEntity(ht);
        InputStream is = buf.getContent();
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        StringBuilder total = new StringBuilder();
        String line;
        String[] location;
        while ((line = r.readLine()) != null) {
            total.append(line + "\n");
            location = total.toString().split(" ");
            locationList.add(location);
            total.setLength(0);
        }
        return locationList;
	}
	
	

}
