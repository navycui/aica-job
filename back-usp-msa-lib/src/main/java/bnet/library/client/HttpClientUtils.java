package bnet.library.client;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bnet.library.client.dto.FileForm;
import bnet.library.client.dto.FileParam;
import bnet.library.util.CoreUtils.exception;

public class HttpClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static Form getNewForm() {
        return Form.form();
    }

    public static HttpResponse get(String url) {
        return get(url, null, Charset.forName(DEFAULT_CHARSET));
    }

    public static HttpResponse get(String url, Charset charset) {
        return get(url, null, charset);
    }

    public static HttpResponse get(String url, Form form) {
        return get(url, form, Charset.forName(DEFAULT_CHARSET));
    }
    public static HttpResponse get(String url, Form form, Charset charset) {
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setCharset(charset);
            if (form != null) {
                builder.addParameters(form.build());
            }
            URI uri = builder.build();
            Request request = Request.Get(uri);
            request.addHeader("X-Requested-With","XMLHttpRequest");
            return request.execute().returnResponse();
        } catch (Throwable e) {
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    public static HttpResponse getWithHeader(String url, Map<String, String> headers) {
        return getWithHeader(url, null, headers, Charset.forName(DEFAULT_CHARSET));
    }

    public static HttpResponse getWithHeader(String url, Form form, Map<String, String> headers) {
        return getWithHeader(url, form, headers, Charset.forName(DEFAULT_CHARSET));
    }
    public static HttpResponse getWithHeader(String url, Form form, Map<String, String> headers, Charset charset) {
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setCharset(charset);
            if (form != null) {
                builder.addParameters(form.build());
            }
            URI uri = builder.build();
            Request request = Request.Get(uri);
            request.addHeader("X-Requested-With","XMLHttpRequest");
            Set<String> keys = headers.keySet();
            for (String key: keys) {
                String value = headers.get(key);
                request.addHeader(key, value);
            }
            return request.execute().returnResponse();
        } catch (Throwable e) {
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    public static HttpResponse post(String url) {
        return post(url, null, Charset.forName(DEFAULT_CHARSET));
    }

    public static HttpResponse post(String url, Form form) {
        return post(url, form, Charset.forName(DEFAULT_CHARSET));
    }

    public static HttpResponse post(String url, Form form, Charset charset) {
        Request request = Request.Post(url);
        request.addHeader("X-Requested-With","XMLHttpRequest");

        if (form != null) {
            request.bodyForm(form.build(), charset);
        }

        try {
            return request.execute().returnResponse();
        } catch (Throwable e) {
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    public static HttpResponse post(String url, String bodyString, ContentType contentType) {
        Request request = Request.Post(url);
        request.addHeader("X-Requested-With","XMLHttpRequest");
        request.bodyString(bodyString, contentType);
        try {
            return request.execute().returnResponse();
        } catch (Throwable e) {
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    public static HttpResponse postWithHeader(String url, Map<String, String> headers) {
        return postWithHeader(url, null, headers, Charset.forName(DEFAULT_CHARSET));
    }
    public static HttpResponse postWithHeader(String url, Map<String, String> headers, Charset charset) {
        return postWithHeader(url, null, headers, charset);
    }

    public static HttpResponse postWithHeader(String url, Form form, Map<String, String> headers) {
        return postWithHeader(url, form, headers, Charset.forName(DEFAULT_CHARSET));
    }
    public static HttpResponse postWithHeader(String url, Form form, Map<String, String> headers, Charset charset) {
        Request request = Request.Post(url);
        request.addHeader("X-Requested-With","XMLHttpRequest");

        if (form != null) {
            request.bodyForm(form.build(), charset);
        }

        Set<String> keys = headers.keySet();
        for (String key: keys) {
            String value = headers.get(key);
            request.addHeader(key, value);
        }

        try {
            return request.execute().returnResponse();
        } catch (Throwable e) {
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    public static HttpResponse postWithHeader(String url, String bodyString, ContentType contentType, Map<String, String> headers) {
        Request request = Request.Post(url);
        request.addHeader("X-Requested-With","XMLHttpRequest");

        Set<String> keys = headers.keySet();
        for (String key: keys) {
            String value = headers.get(key);
            request.addHeader(key, value);
        }
        request.bodyString(bodyString, contentType);
        try {
            return request.execute().returnResponse();
        } catch (Throwable e) {
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    public static HttpResponse upload(String url, FileForm fileForm) {
        return upload(url, null, fileForm, null, Charset.forName(DEFAULT_CHARSET));
    }
    public static HttpResponse upload(String url, FileForm fileForm, Charset charset) {
        return upload(url, null, fileForm, null, charset);
    }
    public static HttpResponse upload(String url, Form form, FileForm fileForm) {
        return upload(url, form, fileForm, null, Charset.forName(DEFAULT_CHARSET));
    }
    public static HttpResponse upload(String url, Form form, FileForm fileForm, Charset charset) {
        return upload(url, form, fileForm, null, charset);
    }
    public static HttpResponse upload(String url, Form form, FileForm fileForm, Map<String, String> headers, Charset charset) {
        List<NameValuePair> params = null;
        if (form == null) {
            params = new ArrayList<>();
        } else {
            params = form.build();
        }

        List<FileParam> files = null;
        if (fileForm == null) {
            files = new ArrayList<>();
        } else {
            files = fileForm.build();
        }

        MultipartEntityBuilder eb = MultipartEntityBuilder
                .create()
                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(charset);

        for (NameValuePair nv: params) {
            eb.addTextBody(nv.getName(), nv.getValue(), ContentType.create("text/plain", charset));
        }

        for (FileParam fp: files) {
            eb.addBinaryBody(fp.getName(), fp.getFile(), ContentType.MULTIPART_FORM_DATA, fp.getFilename());
        }

        Request request = Request.Post(url);

        if ( headers  != null ) {
            Set<String> keys = headers.keySet();
            for (String key: keys) {
                String value = headers.get(key);
                request.addHeader(key, value);
            }
        }

        try {
            return request.body(eb.build()).execute().returnResponse();
        } catch (Throwable e) {
            logger.error(exception.getStackTraceString(e));
            return makeErrorResponse("Communication fail with the server", e);
        }
    }

    private static HttpResponse makeErrorResponse(String message, Throwable e) {
        logger.error(message, e);
        ProtocolVersion ver = new ProtocolVersion("http", 1, 1);
        return new BasicHttpResponse(new BasicStatusLine(ver, 400, message));
    }

    //    public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
    //        Form form = HttpClientUtils.getNewForm();
    //        form.add("a", "b");
    //        HttpResponse response = HttpClientUtils.get("http://172.19.5.105:8081/nexus", form);
    //        if (response.getStatusLine().getStatusCode() == 200) {
    //            System.out.println(HttpResponseUtils.getContentString(response));
    //        }
    //        else {
    //            System.out.println("STATUS:" + response.getStatusLine().getStatusCode());
    //        }
    //    }

    //    public static void main(String[] args) throws ClientProtocolException, IOException, URISyntaxException {
    //        Map<String, String> headers = new HashMap<String, String>();
    //        headers.put("Cookie", "JSESSIONID_COWEB=74B2D536729A786EA2B8B75A14AA1A63");
    //        HttpResponse response = HttpClientUtils.getWithHeader(url, headers);
    //        //        HttpResponse response = HttpClientUtils.get(url);
    //        String html = HttpResponseUtils.getContentString(response);
    //        System.out.println(html);
    //    }

}
