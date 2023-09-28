package bnet.library.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bnet.library.util.CoreUtils.exception;

public class HttpResponseUtils {

    protected static final Logger logger = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static String getContentString(HttpResponse response) {
        if (response == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            int len = 0;
            char[] cbuf = new char[1024*10];
            while ((len = reader.read(cbuf)) > 0) {
                builder.append(cbuf, 0, len);
            }
        } catch (Throwable e) {
            logger.error(exception.getStackTraceString(e));
        } finally {
        	try {
        		if(reader != null) {
        			reader.close();
        		}
			} catch (IOException e) {
				logger.error(exception.getStackTraceString(e));
			}
		}
        return builder.toString();
    }

    public static void getContentFile(HttpResponse response, File file) {
        if (response == null) {
            return;
        }
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = response.getEntity().getContent();
            os = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024*10];
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len );
            }
        } catch (Throwable e) {
            logger.error(exception.getStackTraceString(e));
        } finally {
            try {
            	if(os != null) {
            		os.close();
            	}
            	if(is != null) {
            		is.close();
            	}
            } catch (IOException e) {
            	logger.error(exception.getStackTraceString(e));
            }
        }
    }

    public static StatusLine getStatusLine(HttpResponse response) {
        return response.getStatusLine();
    }

    public static Header[] getAllHeaders(HttpResponse response) {
        return response.getAllHeaders();
    }

    public static Header[] getHeaders(HttpResponse response, String headerName) {
        return response.getHeaders(headerName);
    }

    public static String getDownloadFilename(HttpResponse response) {
        Header[] headers = getHeaders(response, "Content-Disposition");
        if (headers == null || headers.length == 0) {
            return null;
        }
        String[] values = headers[0].getValue().split("=");
        String fname = values[1];
        if (fname.startsWith("\"")) {
            fname = fname.substring(1, fname.length()-1);
        }
        try {
            fname = URLDecoder.decode(fname, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(exception.getStackTraceString(e));
        }
        return fname;
    }
}
