package bnet.library.client;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Form;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class UnirestUtils {

	public HttpResponse<String> get(String url) {
		return Unirest.get(url).asString();
	}

	public HttpResponse<String> get(String url, Form form) {
		List<NameValuePair> params = form.build();
		GetRequest req = Unirest.get(url);
		for (NameValuePair param : params) {
			req.queryString(param.getName(), param.getValue());
		}
		return req.asString();
	}
}
