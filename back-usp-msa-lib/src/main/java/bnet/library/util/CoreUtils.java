package bnet.library.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import com.sun.xml.bind.marshaller.DataWriter;

import bnet.library.exception.InvalidationException;
import bnet.library.util.dto.ZipFileInfo;
import bnet.library.util.xml.JaxbCharacterEscapeHandler;

public class CoreUtils {
    protected static final Logger logger = LoggerFactory.getLogger(CoreUtils.class);
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

//    public static class encrypt {
//        public static String generateSalt() {
//            return KeyGenerators.string().generateKey();
//        }
//
//        public static TextEncryptor getTextEncryptor(String pwd, String salt) {
//            return Encryptors.text(pwd, salt);
//        }
//
//        public static BytesEncryptor getBytesEncryptor(String pwd, String salt) {
//            return Encryptors.standard(pwd, salt);
//        }
//
//    }

    public static class aes256 {

    	private static final byte[] DEFAULT_KEY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    	public static class IvKey {
    		public String iv;
    		public Key key;
    		public IvKey(String iv, Key key) {
    			this.iv = iv;
    			this.key = key;
    		}
    	}
    	public static IvKey getIvKey(String key) {
    		if (string.isEmpty(key)) {
    			key = "1234567890123456";
    		}
    		while (key.length() < 16) {
    			key += "1";
    		}
    		byte[] b;
			try {
				b = key.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				b = DEFAULT_KEY;
			}
    		String iv = key.substring(0, 16);
    		byte[] keyBytes = new byte[16];

    		int len = b.length;
    		if (len > keyBytes.length) {
    			len = keyBytes.length;
    		}
    		System.arraycopy(b, 0, keyBytes, 0, len);
    		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

    		IvKey ivKey = new IvKey(iv, keySpec);
    		return ivKey;
    	}

    	/**
    	 * AES256으로 strToEncrypt를 암호화하여 return한다.
    	 *
    	 * @param strToEncrypt 암호화할 문자열
    	 * @param key 암호화 Key. 복호화할 때도 동일한 Key값을 사용해야 한다.
    	 * @return 암호화된 문자열
    	 */
    	public static String encrypt(String strToEncrypt, String key) {
    		if (strToEncrypt == null) {
    			return null;
    		}
    		IvKey ivKey = getIvKey(key);
			try {
				Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    		c.init(Cipher.ENCRYPT_MODE, ivKey.key, new IvParameterSpec(ivKey.iv.getBytes()));
	    		byte[] encrypted = c.doFinal(strToEncrypt.getBytes("UTF-8"));
	    		String enStr = new String(Base64.encodeBase64(encrypted));
	    		return enStr;
			} catch (GeneralSecurityException | UnsupportedEncodingException e) {
				return null;
			}
    	}

    	/**
    	 * AES256으로 암호화된 문자열 strToDecrypt를 복호화한다.
    	 *
    	 * @param strToDecrypt 복호화할 문자열
    	 * @param key 암호화할 때 사용한 Key
    	 * @return 복호화된 문자열을 리턴한다.
    	 */
    	public static String decrypt(String strToDecrypt, String key) {
    		IvKey ivKey = getIvKey(key);
    		try {
    		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
    		c.init(Cipher.DECRYPT_MODE, ivKey.key, new IvParameterSpec(ivKey.iv.getBytes()));
    		byte[] byteStr = Base64.decodeBase64(strToDecrypt.getBytes());
    		return new String(c.doFinal(byteStr), "UTF-8");
    		} catch (GeneralSecurityException | UnsupportedEncodingException e) {
    			return null;
    		}
    	}
    }

    /**
     * Password Utils
     *
     */
    public static class password {
        private static SecureRandom random = new SecureRandom();

        /**
         * 비밀번호가 유효한지 검사한다.
         *
         * @param password 비밀번호
         * @return true: 유효, false: 유효하지 않음.
         */
        public static boolean isValid(String password) {
            // 숫자,영문,특수문자를 모두 포함하면서, 9자리 이상
            String pattern = "^(?=.*\\d)(?=.*[=\\[\\]\\{\\}/>.<,?+_~`!@#$%^&*()-])(?=.*[a-zA-Z]).{9,999}$";
            return Pattern.matches(pattern, password);
        }

        /**
         * 비밀번호가 유효한지 검사한다.
         *
         * @param password 비밀번호
         * @param digit 비밀번호 최소 자리수
         * @return true: 유효, false: 유효하지 않음.
         */
        public static boolean isValid(String password, int digit) {
            // 숫자,영문,특수문자를 모두 포함하면서
            String pattern = "^(?=.*\\d)(?=.*[=\\[\\]\\{\\}/>.<,?+_~`!@#$%^&*()-])(?=.*[a-zA-Z]).{" + digit + ",999}$";
            return Pattern.matches(pattern, password);
        }

        /**
         * 비밀번호를 SHA256으로 hash한다.
         *
         * @param password hash할 비밀번호
         * @return 비밀번호 hash값
         */
        public static String encode(String password) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.encode(password);
        }

        /**
         * 비밀번호와 hash된 비밀번호가 일치하는 지 검사한다.
         *
         * @param rawPassword 비밀번호
         * @param encodedPassword hash된 비밀번호
         * @return true: 일치, false: 불일치
         */
        public static boolean compare(String rawPassword, String encodedPassword) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches(rawPassword, encodedPassword);
        }

        /**
         * 랜덤 비밀번호를 리턴한다.
         *
         * @return 랜덤 비밀번호
         */
        public static String getRandomPassword() {
            String pwd = new BigInteger(130, random).toString(32);
            if (pwd.length() > 10) {
                pwd = pwd.substring(0, 10);
            }
            return pwd;
        }
    }

    public static class xml {
        public static String getPrettyXML(String xmlInput) {
            try {
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                StreamResult result = new StreamResult(new StringWriter());
                DOMSource source = new DOMSource(parseXml(xmlInput));
                transformer.transform(source, result);
                return result.getWriter().toString();
            } catch (TransformerFactoryConfigurationError | ParserConfigurationException | SAXException | IOException | TransformerException e) {
                return xmlInput;
            }
        }

        private static Document parseXml(String xmlInput) throws ParserConfigurationException, SAXException, IOException {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlInput));
            return db.parse(is);
        }

        public static <T> String marshal( T obj) throws JAXBException {
            return marshal(obj, "UTF-8");
        }

        public static <T> String marshal( T obj, String charset) throws JAXBException {
            JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_ENCODING, charset);
            StringWriter writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            DataWriter dataWriter = new DataWriter(printWriter, charset, new JaxbCharacterEscapeHandler());
            m.marshal(obj, dataWriter);
            return writer.toString();
        }

        public static <T> T unmarshal(String xml, Class<T> cls) throws JAXBException {
            JAXBContext ctx = JAXBContext.newInstance(cls);
            Unmarshaller m = ctx.createUnmarshaller();

            @SuppressWarnings("unchecked")
            T obj = (T)m.unmarshal(new StringReader(xml));
            return obj;
        }
    }

    public static class collection extends CollectionUtils {
        public static String join(Collection<String> col, String separator) {
            if (col.size() == 0) {
                return "";
            }
            StringBuilder b = new StringBuilder();
            for (String item: col) {
                b.append(item + separator);
            }
            b.deleteCharAt(b.length()-1);
            return b.toString();
        }
    }

    public static class array extends ArrayUtils {
        public static String join(String[] arr, String separator) {
            if (arr.length == 0) {
                return "";
            }
            StringBuilder b = new StringBuilder();
            for (String item: arr) {
                b.append(item + separator);
            }
            b.deleteCharAt(b.length()-1);
            return b.toString();
        }

        @SafeVarargs
        public static <T> List<T> toList(T... a) {
            return Arrays.asList(a);
        }
    }

    public static class string extends StringUtils {

        private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        public static String bytestoHexString(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return "";
            }
            char[] hexChars = new char[bytes.length * 2];
            int val = 0;
            for(int i = 0; i < bytes.length; i++) {
                val = bytes[i] & 0xff;
                hexChars[i*2] = HEX_ARRAY[val >> 4];
                hexChars[i*2 + 1] = HEX_ARRAY[val &0x0f];
            }
            return new String(hexChars);
        }

        public static String byteBuffertoHexString(ByteBuffer buffer) {
            if (buffer == null || buffer.capacity() == 0) {
                return "";
            }
            char[] hexChars = new char[buffer.capacity() * 2];
            int val = 0;
            int i = 0;
            while(buffer.hasRemaining()) {
                val = buffer.get() & 0xff;
                hexChars[i*2] = HEX_ARRAY[val >> 4];
                hexChars[i*2 + 1] = HEX_ARRAY[val &0x0f];
                i++;
            }
            return new String(hexChars);
        }

        public static byte[] hexStringToBytes(String hexString) {
            if (hexString == null || hexString.length() == 0) {
                return new byte[0];
            }
            int len = hexString.length();
            byte[] bytes = new byte[len/2];
            for(int i = 0; i < len; i+=2) {
                bytes[i/2] = (byte)((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
            }
            return bytes;
        }

        public static ByteBuffer hexStringToByteBuffer(String hexString) {
            if (hexString == null || hexString.length() == 0) {
                return ByteBuffer.allocate(0);
            }
            int len = hexString.length();
            ByteBuffer buffer = ByteBuffer.allocate(len);
            byte b = 0;
            for(int i = 0; i < len; i+=2) {
                b = (byte)((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
                buffer.put(b);
            }
            return buffer;
        }

        @Deprecated
        public static String underscoreToLowerCamel(String underscore) {
            if( underscore == null) {
                return null;
            }
            underscore = underscore.toLowerCase();
            StringBuilder build = new StringBuilder();
            char c = 'x';
            for (int i = 0; i < underscore.length(); i++) {
                c = underscore.charAt(i);
                if (c == '_') {
                    c = underscore.charAt(++i);
                    if (c >= 'a' && c < 'z') {
                        c += 'A' - 'a';
                    }
                }
                build.append(c);
            }
            return build.toString();
        }

        @Deprecated
        public static String camelToUpperUnderscore(String camel) {
            if (camel == null) {
                return null;
            }

            StringBuilder build = new StringBuilder();
            char c = 'x';
            for (int i = 0; i < camel.length(); i++) {
                c = camel.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    build.append('_');
                }
                build.append(c);
            }
            return build.toString().toUpperCase();
        }

        /**
         * Underscore 형식의 문자열을 Camel형식으로 변환하여 리턴한다. 첫 문자는 대문자로 리턴한다.
         *
         * @param underscoreString Underscore 형식의 문자열
         * @return Camel형식의 문자열
         */
        public static String toUpperCamel(String underscoreString) {
        	return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, underscoreString);
        }

        /**
         * Underscore 형식의 문자열을 Camel형식으로 변환하여 리턴한다. 첫 문자는 소문자로 리턴한다.
         *
         * @param underscoreString Underscore 형식의 문자열
         * @return Camel형식의 문자열
         */
        public static String toLowerCamel(String underscoreString) {
        	return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscoreString);
        }

        /**
         * Camel 형식의 문자열을 underscore 형식의 문자열로 변환하여 리턴한다. 첫 문자는 대문자로 리턴한다.
         *
         * @param camelString Camel형식의 문자열
         * @return Underscore 형식의 문자열
         */
        public static String toUpperUnderscore(String camelString) {
        	return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, camelString);
        }

        /**
         * Camel 형식의 문자열을 Underscore형식의 문자열로 변환하여 리턴한다. 첫 문자는 소문자로 리턴한다.
         *
         * @param camelString Camel형식의 문자열
         * @return Underscore 형식의 문자열
         */
        public static String toLowerUnderscore(String camelString) {
        	return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelString);
        }

        /**
         * HTTP로 다운로드 파일명으로 Encoding한다.
         *
         * @param fileName encoding할 파일명
         * @return encoding된 파일명
         */
        public static String toDownloadFilename(String fileName) {
            String fname;
            try {
                fname = URLEncoder.encode(fileName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                return fileName;
            }
            String[] regexs = new String[] {
                    "\\%29", "\\%28", "\\%3D", "\\%2B", "\\%7B",
                    "\\%7D", "\\%27", "\\%26", "\\%21", "\\%40",
                    "\\%23", "%24", "\\%25", "\\%5E", "\\%26",
                    "\\%2C", "\\%5B", "\\%5D", "\\+", "\\%2F" };
            String[] replchars = new String[] {
                    ")", "(", "=", "+", "{",
                    "}", "'", "&", "!", "@",
                    "#", "\\$", "%", "^", "&",
                    ",", "[", "]", " ", ","};

            for( int i = 0; i < replchars.length; i++ ) {
                fname = fname.replaceAll(regexs[i], replchars[i]);
            }
            return fname;
        }

        /**
         * UUID를 리턴한다.
         * @param prefix UUID 앞에 덧 붙일 Prefix
         * @return prefix를 덧 붙인 UUID
         */
        public static String getNewId(String prefix) {
            return prefix + UUID.randomUUID().toString().replaceAll("-", "");
        }

        public static Integer toInt(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            text = string.removePattern(text, "[^0-9\\.\\-\\+]");
            if (StringUtils.isBlank(text)) {
                return null;
            }
            return (int)NumberUtils.toDouble(text);
        }

        public static Double toDouble(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            text = string.removePattern(text, "[^0-9\\.\\-\\+]");
            if (StringUtils.isBlank(text)) {
                return null;
            }
            return NumberUtils.toDouble(text);
        }

        public static Float toFloat(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            text = string.removePattern(text, "[^0-9\\.\\-\\+]");
            if (StringUtils.isBlank(text)) {
                return null;
            }
            return NumberUtils.toFloat(text);
        }

        public static Long toLong(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            text = string.removePattern(text, "[^0-9\\.\\-\\+]");
            if (StringUtils.isBlank(text)) {
                return null;
            }
            return (long)NumberUtils.toDouble(text);
        }

        public static Short toShort(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            text = string.removePattern(text, "[^0-9\\.\\-\\+]");
            if (StringUtils.isBlank(text)) {
                return null;
            }
            return (short)NumberUtils.toDouble(text);
        }

        public static List<String> splitBySpace(String text) {
            if (string.isEmpty(text)) {
                return new ArrayList<>();
            }
            String[] arr = text.split(" ");
            List<String> result = new ArrayList<>();
            for (String t: arr) {
                if (t.length() == 0) {
                    continue;
                }
                result.add(t);
            }
            return result;
        }

        public static BigDecimal toBigDecimal(String text) {
            if (string.isBlank(text)) {
                return null;
            }
            text = string.removePattern(text, "[^0-9\\.\\-\\+]");
            if (StringUtils.isBlank(text)) {
                return null;
            }
            return new BigDecimal(text);
        }

        public static Date toDate(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            try {
                text = string.getNumberOnly(text);
                if (text.length() == 4) {
                    return date.parseDate(text, "yyyy");
                }
                else if (text.length() == 6) {
                    return date.parseDate(text, "yyyyMM");
                }
                else if (text.length() == 8) {
                    return date.parseDate(text, "yyyyMMdd");
                }
                else if (text.length() == 10) {
                    return date.parseDate(text, "yyyyMMddHH");
                }
                else if (text.length() == 12) {
                    return date.parseDate(text, "yyyyMMddHHmm");
                }
                else if (text.length() == 14) {
                    return date.parseDate(text, "yyyyMMddHHmmss");
                }
                else {
            		try {
            			return new Date(Long.parseLong(text));
        			} catch (NumberFormatException e) {
        				return null;
        			}
                }
            } catch (ParseException e) {
                return null;
            }
        }

        public static Boolean toBoolean(String text) {
            if (StringUtils.isBlank(text)) {
                return null;
            }
            text = text.trim();
            if (text.equalsIgnoreCase("true")) {
                return true;
            }
            if (text.equalsIgnoreCase("Y")) {
                return true;
            }
            if (text.equals("1")) {
                return true;
            }
            if (text.equals("1.0")) {
                return true;
            }
            if (text.equalsIgnoreCase("yes")) {
                return true;
            }
            if (text.equalsIgnoreCase("O")) {
                return true;
            }
            if (text.equalsIgnoreCase("on")) {
                return true;
            }
            return false;
        }

        public static String getNumberOnly(String str) {
            if (string.isEmpty(str)) {
                return str;
            }
            return string.removePattern(str, "[^0-9]");
        }

        public static String removeWhitespace(String str) {
            if (isEmpty(str)) {
                return str;
            }
            StringBuilder builder = new StringBuilder();
            int size = str.length();
            for (int i = 0; i < size; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    builder.append(str.charAt(i));
                }
            }
            return builder.toString();
        }

        public static final char[] DELIMITER_REGEX_CHARS = {'^','$','.','?','*','|','(',')',':','{','}','\\'};
        public static String validateIpStrings(String ipStrings, char delimiter) {
            if (StringUtils.isEmpty(ipStrings)) {
                return "";
            }
            String delimiterString = "" + delimiter;
            if (ArrayUtils.contains(DELIMITER_REGEX_CHARS, delimiter)) {
                delimiterString = "\\" + delimiterString;
            }
            String[] ips = ipStrings.split(delimiterString);
            List<String> ipList = new ArrayList<>();
            for (String ip: ips) {
                if (StringUtils.isBlank(ip)) {
                    continue;
                }
                ip = removeWhitespace(ip);
                if (validation.isIpAddress(ip)) {
                    ipList.add(ip);
                }
            }
            return StringUtils.join(ipList, delimiter);
        }

        public static String validateIpStrings(String ipStrings, char delimiter, char wildcard) {
            if (StringUtils.isBlank(ipStrings)) {
                return "";
            }
            String delimiterString = "" + delimiter;
            if (ArrayUtils.contains(DELIMITER_REGEX_CHARS, delimiter)) {
                delimiterString = "\\" + delimiterString;
            }
            String[] ips = ipStrings.split(delimiterString);
            List<String> ipList = new ArrayList<>();
            char c = 'x';
            boolean skip = false;
            for (String ip: ips) {
                if (StringUtils.isBlank(ip)) {
                    continue;
                }
                ip = string.removeWhitespace(ip);
                skip = false;
                for (int i = 0; i < ip.length(); i++) {
                    c = ip.charAt(i);
                    if ((c < '0' || c >'9') && c != wildcard && c != '.') {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }

                if (ip.split("\\.").length == 4) {
                    ipList.add(ip);
                }
            }
            if (ipList.size() == 0) {
                return "";
            }
            return StringUtils.join(ipList, delimiter);
        }

        public static boolean containsIp(String[] ips, String ip) {
        	for (String item: ips) {
        		boolean val = antpath.match(item, ip);
        		if (val) {
        			return true;
        		}
        	}

        	return false;
        }

        public static String validateFileExtStrings(String fileExtStrings, char delimiter) {
            if (StringUtils.isEmpty(fileExtStrings)) {
                return "";
            }
            String delimiterString = "" + delimiter;
            if (ArrayUtils.contains(DELIMITER_REGEX_CHARS, delimiter)) {
                delimiterString = "\\" + delimiterString;
            }
            String[] exts = fileExtStrings.split(delimiterString);
            List<String> extList = new ArrayList<>();
            for (String ext: exts) {
                ext = ext.replaceAll("[\\.\\*]", "");
                ext = ext.toUpperCase();
                ext = StringUtils.deleteWhitespace(ext);
                if (StringUtils.isNotEmpty(ext)) {
                    extList.add(ext);
                }
            }

            // 필수 금지 확장자 검사
            String[] forbiddenExts = {
                    "JS", "JSP", "CLASS", "JAVA",             // JAVA
                    "PHP",                                    // PHP
                    "ASP", "CS",                              // ASP, C#
                    "EXE", "BAT", "COM", "DLL", "CMD",        // WINDOWS 실행파일
                    "CAB",                                    // ACTIVE-X 압축파일
                    "MSI",                                    // WINDOWS 설치파일
                    "C", "CPP",                               // C, C++
                    "PL",                                     // PERL
                    "PY",                                     // PYTHON
                    "AS"                                      // ACTION SCRIPT
            };
            for (String ext: forbiddenExts) {
                if (extList.contains(ext)) {
                    throw new RuntimeException("프로그램 관련 확장자(" + ext + ")가 포함되어 있습니다. 제거한 후 다시 시도하십시오.");
                }
            }
            return StringUtils.join(extList, delimiter);
        }

        public static boolean containsIgnoreCase(String[] arr, String value) {
            for (String item: arr) {
                if (StringUtils.equalsIgnoreCase(item, value)) {
                    return true;
                }
            }
            return false;
        }

        public static String escapedHtml(String content) {
            if (StringUtils.isEmpty(content)) {
                return content;
            }
            String s = StringEscapeUtils.escapeHtml4(content);
            s = s.replaceAll("\r\n|\n", "<br/>");
            s = s.replaceAll(" ", "&nbsp;");
            return s.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        }

        public static String escapedHtmlForInput(String content) {
            if (StringUtils.isEmpty(content)) {
                return content;
            }
            return StringEscapeUtils.escapeHtml4(content);
        }

        public static String toPhoneFormat(String src) {
            if (src == null) {
              return "";
            }
            src = string.getNumberOnly(src);
            if (src.length() == 8) {
              return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
            } else if (src.length() == 12) {
              return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
            }
            return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
        }

        public static int compare(String s1, String s2) {
        	s1 = (s1 == null) ? "" : s1;
        	s2 = (s2 == null) ? "" : s2;
        	return s1.compareTo(s2);
        }

        public static int compareIgnoreCase(String s1, String s2) {
        	s1 = (s1 == null) ? "" : s1;
        	s2 = (s2 == null) ? "" : s2;
        	return s1.compareToIgnoreCase(s2);
        }

        public static String removePattern(final String text, final String regex) {
        	return RegExUtils.removePattern(text, regex);
        }

        public static String formatFileSize(long fileSize) {
			String unit = "Bytes";
			double size = fileSize;
			if (Math.floor(size / 1024 / 1024 / 1024) > 0) {
				size = size / 1024 / 1024 / 1024;
				unit = "GB";
			}
			else if (Math.floor(size / 1024 / 1024) > 0) {
				size = size / 1024 / 1024;
				unit = "MB";
			}
			else if (Math.floor(size / 1024) > 0) {
				size = size / 1024;
				unit = "KB";
			}
			return String.format("%,.1f %s", size, unit);
        }

        public static String getRandomNo(int digitNo) {
        	if (digitNo < 1) {
        		digitNo = 6;
        	}
			long seed = System.currentTimeMillis();
			Random random = new Random(seed);
			double d = random.nextDouble();
			long l = (long)(d * Math.pow(10, digitNo));
			String fmt = String.format("%%0%dd", digitNo);
			return String.format(fmt, l);
		}
    }

    /**
     * Ant path matcher
     */
    public static class antpath {
        public static boolean match(String pattern, String path) {
            return pathMatcher.match(pattern, path);
        }
        public static boolean matchStart(String pattern, String path) {
            return pathMatcher.matchStart(pattern, path);
        }
    }

    public static class validation {
        /**
         * 주민번호 검사.
         *
         * @param juminNumber: 주민번호
         * @return 검증결과(true: 정상, false:비정상)
         */
        public static boolean isJuminNumber(String juminNumber) {
            String  IDAdd = "234567892345";     // 주민등록번호에 가산할 값

            int count_num = 0;
            int add_num = 0;
            int total_id = 0;      //검증을 위한 변수선언

            if (juminNumber.length() != 13)
            {
                return false;    // 주민등록번호 자리수가 맞는가를 확인
            }

            for (int i = 0; i <12 ; i++){
                if(juminNumber.charAt(i)< '0' || juminNumber.charAt(i) > '9')
                {
                    return false;     //숫자가 아닌 값이 들어왔는지를 확인
                }
                count_num = Character.getNumericValue(juminNumber.charAt(i));
                add_num = Character.getNumericValue(IDAdd.charAt(i));
                total_id += count_num * add_num;      //유효자리 검증식을 적용
            }

            if(Character.getNumericValue(juminNumber.charAt(0)) == 0 || Character.getNumericValue(juminNumber.charAt(0)) == 1){
                if(Character.getNumericValue(juminNumber.charAt(6)) > 4) {
                    return false;
                }
                String temp = "20" + juminNumber.substring(0,6);
                if(!date.isValidDate(temp, "yyyyMMdd")) {
                    return false;
                }
            }
            else{
                if(Character.getNumericValue(juminNumber.charAt(6)) > 2) {
                    return false;
                }
                String temp = "19" + juminNumber.substring(0,6);
                if(!date.isValidDate(temp, "yyyyMMdd")) {
                    return false;
                }
            }   //주민번호 앞자리 날짜유효성체크 & 성별구분 숫자 체크

            if(Character.getNumericValue(juminNumber.charAt(12)) == (11 - (total_id % 11)) % 10) {
                return true;
            }
            else {
                return false;
            }
        }

        /**
         * 법인번호 검사.
         *
         * @param bubinNumber: 법인등록번호
         * @return 검증결과(true:정상, false:비정상)
         */
        public static boolean isBubinNumber(String bubinNumber) {
            int hap = 0;
            int temp = 1;   //유효검증식에 사용하기 위한 변수

            if(bubinNumber.length() != 13) {
                return false;    //법인번호의 자리수가 맞는 지를 확인
            }

            for(int i=0; i < 13; i++){
                if (bubinNumber.charAt(i) < '0' || bubinNumber.charAt(i) > '9') {
                    return false;
                }
            }

            for ( int i=0; i<12; i++){
                if(temp ==3) {
                    temp = 1;
                }
                hap = hap + (Character.getNumericValue(bubinNumber.charAt(i)) * temp);
                temp++;
            }

            if ((10 - (hap%10))%10 == Character.getNumericValue(bubinNumber.charAt(12))) {
                return true;
            }
            else {
                return false;
            }
        }

        /**
         * 사업자번호 검사.
         *
         * @param compNumber: 사업자등록번호
         * @return 검증결과(true:정상, false:비정상)
         */
        public static boolean isCompanyNumber(String compNumber) {
            int hap = 0;
            int temp = 0;
            int check[] = {1,3,7,1,3,7,1,3,5};  //사업자번호 유효성 체크 필요한 수

            if(compNumber.length() != 10) {
                return false;
            }

            for(int i=0; i < 9; i++){
                if(compNumber.charAt(i) < '0' || compNumber.charAt(i) > '9') {
                    return false;
                }

                hap = hap + (Character.getNumericValue(compNumber.charAt(i)) * check[temp]); //검증식 적용
                temp++;
            }

            hap += (Character.getNumericValue(compNumber.charAt(8))*5)/10;

            if ((10 - (hap%10))%10 == Character.getNumericValue(compNumber.charAt(9))) {
                return true;
            }
            else {
                return false;
            }
        }

        /**
         * 외국인 번호 검사.
         *
         * @param foreignNumber: 외국인등록번호
         * @return 검증결과(true:정상, false:비정상)
         */
        public static boolean isForeignNumber( String foreignNumber ) {
            int check = 0;

            if( foreignNumber.length() != 13 ) {
                return false;
            }

            for(int i=0; i < 13; i++){
                if (foreignNumber.charAt(i) < '0' || foreignNumber.charAt(i) > '9') {
                    return false;
                }
            }

            if(Character.getNumericValue(foreignNumber.charAt(0)) == 0 || Character.getNumericValue(foreignNumber.charAt(0)) == 1){
                if(Character.getNumericValue(foreignNumber.charAt(6)) == 5 && Character.getNumericValue(foreignNumber.charAt(6)) == 6) {
                    return false;
                }
                String temp = "20" + foreignNumber.substring(0,6);
                if(!date.isValidDate(temp, "yyyyMMdd")) {
                    return false;
                }
            }
            else {
                if(Character.getNumericValue(foreignNumber.charAt(6)) == 5 && Character.getNumericValue(foreignNumber.charAt(6)) == 6) {
                    return false;
                }
                String temp = "19" + foreignNumber.substring(0,6);
                if(!date.isValidDate(temp, "yyyyMMdd")) {
                    return false;
                }
            }   //외국인등록번호 앞자리 날짜유효성체크 & 성별구분 숫자 체크

            for( int i = 0 ; i < 12 ; i++ ) {
                check += ( ( 9 - i % 8 ) * Character.getNumericValue( foreignNumber.charAt( i ) ) );
            }

            if ( check % 11 == 0 ){
                check = 1;
            }
            else if ( check % 11==10 ){
                check = 0;
            }
            else {
                check = check % 11;
            }

            if ( check + 2 > 9 ){
                check = check + 2- 10;
            }
            else {
                check = check+2;  //검증식을 통합 값의 도출
            }

            if( check == Character.getNumericValue( foreignNumber.charAt( 12 ) ) ) {
                return true;
            }
            else {
                return false;
            }
        }

        private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		public static boolean isEmail(String email) {
			Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
		    return matcher.find();
		}

		public static boolean isIpAddress(String ipAddr) {
		    String validIp = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
		    if (Pattern.matches(validIp, ipAddr )) {
		        return true;
		    }
		    return false;
		}

        public static <T> T nvl(T nullCheckValue, T defalutValue) {
        	if (nullCheckValue == null) {
        		return defalutValue;
        	}
        	return nullCheckValue;
        }
    }

    public static class system {
        public static List<String> getLocalIpAddrs() {
            List<String> ips = new ArrayList<>();
            ips.add("127.0.0.1");
            try {
                for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements();) {
                    NetworkInterface intf = nis.nextElement();
                    for (Enumeration<InetAddress> ipAddrs = intf.getInetAddresses(); ipAddrs.hasMoreElements();) {
                        InetAddress inetAddress = ipAddrs.nextElement();
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                            ips.add(inetAddress.getHostAddress().toString());
                        }
                    }
                }
                return ips;
            } catch (SocketException e) {
                return ips;
            }
        }
    }

    public static class exception {
        public static String getStackTraceString(Throwable e) {
            try (StringWriter sw = new StringWriter();) {
            	e.printStackTrace(new PrintWriter(sw));
            	return sw.toString();
            } catch (IOException e1) {
                logger.error("error:" + e1.getMessage());
                return null;
            }
        }
    }

    public static class filename extends FilenameUtils {

    }

    public static class date extends DateUtils {

    	public static Date  parseDateSilently(String dateStr, String... parsePatterns) {
    		if (CoreUtils.string.isBlank(dateStr)) {
    			return null;
    		}
    		Date dt = null;

    		try {
				dt = DateUtils.parseDate(dateStr, parsePatterns);
			} catch (ParseException e) {
				dt = null;
			}
    		return dt;
    	}

        public static String format(Date date, String pattern) {
            if (date == null) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, LocaleContextHolder.getLocale());
            return sdf.format(date);
        }

    	public static int getDiffDays(Date beginDt, Date endDt) {
    		beginDt = date.truncate(beginDt, Calendar.DATE);
    		endDt = date.truncate(endDt, Calendar.DATE);
    		long diffTime = endDt.getTime() - beginDt.getTime();
    		int diffDays = (int)(diffTime / (1000 * 60 * 60 * 24));
    		return diffDays;
    	}

    	public static int getDiffMonths(Date beginDt, Date endDt) {
    		Calendar cal1 = Calendar.getInstance();
    		Calendar cal2 = Calendar.getInstance();

    		cal1.setTime(beginDt);
    		cal2.setTime(endDt);

    		int dy = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
    		return dy * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);
    	}

    	/**
    	 * beginDt와 endDt 사이의 분 차이를 계산한다.
    	 *
    	 * @param beginDt 시작시간
    	 * @param endDt 종료시간
    	 * @return 분단위의 차이
    	 */
    	public static long getDiffMinutes(Date beginDt, Date endDt) {
        	LocalDateTime ldt1 = date.toLocalDateTime(beginDt);
        	LocalDateTime ldt2 = date.toLocalDateTime(endDt);
        	long diff = ChronoUnit.MINUTES.between(ldt1, ldt2);
        	return diff;
    	}

    	/**
    	 * beginDt와 endDt 사이의 초 차이를 계산한다.
    	 *
    	 * @param beginDt 시작시간
    	 * @param endDt 종료시간
    	 * @return 초단위의 차이
    	 */
    	public static long getDiffSeconds(Date beginDt, Date endDt) {
    		LocalDateTime ldt1 = date.toLocalDateTime(beginDt);
        	LocalDateTime ldt2 = date.toLocalDateTime(endDt);
        	long diff = ChronoUnit.SECONDS.between(ldt1, ldt2);
        	return diff;
    	}

    	public static int compareDay(Date beginDt, Date endDt) {
    		return truncatedCompareTo(beginDt, endDt, Calendar.DATE);
    	}

        public static boolean isValidDate(String date, String format) {

            if (date == null) {
                return false;
            }

            SimpleDateFormat _format = new SimpleDateFormat(format);
            boolean tmp = _format.isLenient();

            // temp for recover lenient status
            _format.setLenient(false);

            try {
                _format.parse(date);
            } catch (ParseException e) {
                return false;
            } finally {
                // recover original lenient status
                _format.setLenient(tmp);
            }
            return true;
        }

		public static String getCurrentDate(String pattern) {
			Date now = new Date();
			return format(now, pattern);
		}

		public static Date getLastDateOfMonth(Date date) {
			LocalDate convertedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			convertedDate = convertedDate.withDayOfMonth(convertedDate.getMonth().length(convertedDate.isLeapYear()));
			return Date.from(convertedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

		public static int getAge(String birthday) {
			if (CoreUtils.string.isBlank(birthday)) {
				return 0;
			}
			Date birthDt = date.parseDateSilently(birthday, "yyyyMMdd");
			if (birthDt == null) {
				return 0;
			}
			return getAge(birthDt);
		}

		public static int getAge(Date birthDt) {
			if (birthDt == null) {
				return 0;
			}
			Date now = new Date();

			Calendar cal = Calendar.getInstance();
			cal.setTime(birthDt);
			int year1 = cal.get(Calendar.YEAR);
			int day1 = cal.get(Calendar.DAY_OF_YEAR);


			cal.setTime(now);
			int year2 = cal.get(Calendar.YEAR);
			int day2 = cal.get(Calendar.DAY_OF_YEAR);

			int age = year2 - year1;
			if (day1 > day2) {
				age--;
			}
			return age;
		}

		/**
		 * 요일 구하기
		 *
		 * @param dt 날짜
		 * @return 0:오류, 1:월, 2:화, 3:수 ~ 7:일
		 */
		public static int getDayOfWeek(Date dt) {
			if (dt == null) {
				return 0;
			}
			LocalDate ldt = dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			DayOfWeek dayOfWeek = ldt.getDayOfWeek();
			int dayNum = dayOfWeek.getValue();
			return dayNum;
		}

		public static LocalDateTime toLocalDateTime(Date dt) {
			return dt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		}
    }

    public static class webutils extends WebUtils {

        public static String getRemoteIp(HttpServletRequest request) {
            String remoteIp = request.getHeader("X-Forwarded-For");
            if (string.isBlank(remoteIp)|| "unknown".equalsIgnoreCase(remoteIp)) {
                remoteIp = request.getHeader("Proxy-Client-IP");
            }
            if (string.isBlank(remoteIp) || "unknown".equalsIgnoreCase(remoteIp)) {
                remoteIp = request.getHeader("WL-Proxy-Client-IP");
            }
            if (string.isBlank(remoteIp) || "unknown".equalsIgnoreCase(remoteIp)) {
                remoteIp = request.getHeader("HTTP_CLIENT_IP");
            }
            if (string.isBlank(remoteIp) || "unknown".equalsIgnoreCase(remoteIp)) {
                remoteIp = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (string.isBlank(remoteIp) || "unknown".equalsIgnoreCase(remoteIp)) {
                remoteIp = request.getRemoteAddr();
            }

            if (string.contains(remoteIp, ",")) {
            	String[] ips = remoteIp.split(",");
            	remoteIp = ips[0].trim();
            }

            return remoteIp;
        }

        public static String getRealPath(ServletContext servletContext, String path) {
            Assert.notNull(servletContext, "ServletContext must not be null");

            if (!(path.startsWith("/"))) {
                path = "/" + path;
            }
            String realPath = servletContext.getRealPath(path);
            return realPath;
        }

        public static void downloadFile(HttpServletResponse response, File file, String filename) {
            if (file == null) {
                throw new InvalidationException("파일을 입력하십시오.");
            }
            if (!file.exists()) {
                throw new InvalidationException("파일이 없습니다.");
            }

            try (FileInputStream fi = new FileInputStream(file)) {
                byte[] buf = new byte[1024*10];
                int len = 0;
                filename = string.toDownloadFilename(filename);
                response.setContentType("application/octet-stream");
                response.setContentLength((int)file.length());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");

                OutputStream fo = response.getOutputStream();
                while ( (len = fi.read(buf)) > 0) {
                    fo.write(buf, 0, len);
                }
                response.flushBuffer();
            } catch (FileNotFoundException e) {
                logger.error("File not found:" + file.getName());
                throw new InvalidationException("파일이 없습니다.");
            } catch (IOException e) {
                logger.error("IOException:" + e.getMessage());
                throw new InvalidationException("파일에 문제가 있어 다운로드할 수 없습니다.");
            }
        }

        public static void downloadFile(HttpServletResponse response, byte[] barr, String filename) {
            if (barr == null) {
                throw new InvalidationException("버퍼를 입력하십시오.");
            }
            if (barr.length == 0) {
                throw new InvalidationException("내용이 없습니다.");
            }

            try {
                ByteArrayInputStream is = new ByteArrayInputStream(barr);
                byte[] buf = new byte[1024*10];
                int len = 0;
                filename = string.toDownloadFilename(filename);
                response.setContentType("application/octet-stream");
                response.setContentLength(barr.length);
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");


                OutputStream fo = response.getOutputStream();
                while ( (len = is.read(buf)) > 0 ) {
                    fo.write(buf, 0, len);
                }
                is.close();
                is = null;
                response.flushBuffer();
            } catch (IOException e) {
                logger.error("IOException:" + e.getMessage());
                throw new InvalidationException("파일에 문제가 있어 다운로드할 수 없습니다.");
            }
        }

        public static boolean isAjax(HttpServletRequest request) {
            String value = request.getHeader("X-Requested-With");
            if (string.isBlank(value)) {
                return false;
            }
            return value.equalsIgnoreCase("XMLHttpRequest");
        }

		public static void downloadFile(HttpServletResponse response, File file, String filename, String contentType) {
            if (file == null) {
                throw new InvalidationException("파일을 입력하십시오.");
            }
            if (!file.exists()) {
                throw new InvalidationException("파일이 없습니다.");
            }

            try (FileInputStream fi = new FileInputStream(file);) {
                byte[] buf = new byte[1024*10];
                int len = 0;
                filename = string.toDownloadFilename(filename);
                response.setContentType(contentType);
                response.setContentLength((int)file.length());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");

                OutputStream fo = response.getOutputStream();
                while ( (len = fi.read(buf)) > 0) {
                    fo.write(buf, 0, len);
                }
                response.flushBuffer();
            } catch (FileNotFoundException e) {
                logger.error("File not found:" + file.getName());
                throw new InvalidationException("파일이 없습니다.");
            } catch (IOException e) {
                logger.error("IOException:" + e.getMessage());
                throw new InvalidationException("파일에 문제가 있어 다운로드할 수 없습니다.");
            }
		}
    }

	@SuppressWarnings("deprecation")
	public static class file extends FileUtils {
        public static byte[] getContent(File file) {
            byte[] content = new byte[(int)file.length()];
            FileInputStream fi = null;
            try {
                fi = new FileInputStream(file);
                byte[] buf = new byte[1024*10];
                int pos = 0;
                int len = 0;
                while ( (len = fi.read(buf)) > 0) {
                    System.arraycopy(buf, 0, content, pos, len);
                    if (pos < (Integer.MAX_VALUE - len)) {
                    	pos += len;
                    }
                    else {
                    	throw new InvalidationException("Number overflow");
                    }
                }
            } catch (IOException e) {
                logger.error(exception.getStackTraceString(e));
            } finally {
                try {
                    if (fi != null) {
                        fi.close();
                    }
                } catch (IOException e) {
                    logger.error(exception.getStackTraceString(e));
                }
            }
            fi = null;
            return content;
        }

        /**
         * Classpath에 있는 text file의 내용 전체를 읽어 return한다.
         *
         * @param classpath: Classpath
         * @return Text 파일 내용
         */
        public static String readFileString(String classpath) {
        	StringBuilder sb = new StringBuilder();
        	try (
        		InputStream stream = CoreUtils.class.getResourceAsStream(classpath);
        		BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        	) {
        		while(reader.ready()) {
        			String line = reader.readLine();
        			sb.append(line).append("\n");
        		}
        	} catch (IOException e) {
        		return null;
        	}

        	if (sb.length() > 0) {
    			sb.deleteCharAt(sb.length()-1);
    		}
        	return sb.toString();
        }
    }

    public static class field extends FieldUtils {

    }

    public static class property extends PropertyUtils {
        /**
         * orig의 property들을 dest에 복사한다.
         *
         * @param dest  대상
         * @param orig  원본
         */
        public static void copyProperties(Object dest, Object orig) {
            try {
                PropertyUtils.copyProperties(dest, orig);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error(exception.getStackTraceString(e));
                throw new RuntimeException(e);
            }
        }
    }

    public static class bean extends BeanUtils {
    	public static <T> boolean equals(T obj1, T obj2, String... excludeFields) {
    		return EqualsBuilder.reflectionEquals(obj1, obj2, excludeFields);
    	}
    	public static <T> boolean equals(T obj1, T obj2, Collection<String> excludeFields) {
    		return EqualsBuilder.reflectionEquals(obj1, obj2, excludeFields);
    	}
    }

    public static class io extends IOUtils {

    }

    public static class json {
        private static final ObjectMapper mapper = new ObjectMapper();

        public static String toString(Object obj) {
            try {
                return mapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        public static <T> T toObject(String json, Class<T> cls) {
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
            try {
                return mapper.readValue(json, cls);
            } catch (IOException e) {
                logger.error(exception.getStackTraceString(e));
                return null;
            }
        }

        public static <T> T mapToObject(Map<String, Object> map, Class<T> cls) {
        	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        	return mapper.convertValue(map, cls);
        }
    }

    public static class number extends NumberUtils {

    }

    public static class zip {
        private static final String[] separator = {"/", "\\"};

        public static void zipfiles(List<ZipFileInfo> infos, File zipfile) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(zipfile);  ZipOutputStream zos = new ZipOutputStream(fos);) {
                for (ZipFileInfo info: infos) {
                    zipfile(zos, info.getFile(), info.getFolderName(), info.getFileName());
                }
            }
        }

        public static void zipfolder(String folderName, File zipfile) throws IOException {
            File folder = new File(folderName);
            List<ZipFileInfo> fileInfos = new ArrayList<>();
            fileInfos = listfiles(folderName, folder, fileInfos);

            zipfiles(fileInfos, zipfile);
        }

        private static List<ZipFileInfo> listfiles(String rootFolderName, File folder, List<ZipFileInfo> fileInfos) {
            File[] files = folder.listFiles();
            for (File file: files) {
                if (file.isFile()) {
                    String filename = file.getName();
                    String foldername = file.getParent() + File.separator;
                    foldername = foldername.substring(rootFolderName.length());
                    fileInfos.add(new ZipFileInfo(foldername, filename, file));
                }
                else if (file.isDirectory()) {
                    listfiles(rootFolderName, file, fileInfos);
                }
            }

            return fileInfos;
        }

        private static void zipfile(ZipOutputStream zos, File inputFile, String foldername, String filename) throws IOException {
            if (string.isNotEmpty(foldername)) {
                if( !array.contains(separator, foldername.charAt(foldername.length()-1))) {
                    foldername += File.separator;
                }
            }

            String myName = foldername + filename;
            ZipEntry fileEntry = new ZipEntry(myName);
            zos.putNextEntry(fileEntry);

            try (FileInputStream fis = new FileInputStream(inputFile);) {
                byte[] buf = new byte[1024 * 4];
                int byteRead = 0;
                while ((byteRead = fis.read(buf)) > 0) {
                    zos.write(buf, 0, byteRead);
                }
                zos.closeEntry();
            }
        }

        public static void unzip(File zipfile, Charset charset, File unzipdir) throws IOException {
            if (!zipfile.exists()) {
                return;
            }
            if (!unzipdir.exists()) {
                unzipdir.mkdirs();
            }

            File file = null;
            try (FileInputStream fis = new FileInputStream(zipfile); ZipInputStream zis = new ZipInputStream(fis, charset);) {
                ZipEntry ze = null;
                while((ze = zis.getNextEntry()) != null) {
                    String filename = ze.getName();
                    file = new File(unzipdir.getAbsolutePath() + File.separator + filename);
                    if (ze.isDirectory()) {
                        file.mkdirs();
                    }
                    else {
                        unzipFile(file, zis);
                    }
                }
            }
        }

        private static void unzipFile( File file, ZipInputStream zis) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buf = new byte[1024];
                int size = 0;
                while ((size = zis.read(buf)) > 0) {
                    fos.write(buf, 0, size);
                }
            }
        }
    }

    public static class image {
    	public static BufferedImage resizeWidth(BufferedImage img, int width) {
    		double ratio = (double)width / img.getWidth();
    		double h = img.getHeight() * ratio;
    		Image tmp = img.getScaledInstance(width, (int)h, Image.SCALE_SMOOTH);
    		BufferedImage resized = new BufferedImage(width, (int)h, BufferedImage.TYPE_INT_ARGB);
    		Graphics2D g2d = resized.createGraphics();
    		g2d.drawImage(tmp, 0, 0, null);
    		g2d.dispose();
    		return resized;
    	}

    	public static BufferedImage resizeHeight(BufferedImage img, int height) {
    		double ratio = (double)height / img.getHeight();
    		double w = img.getWidth() * ratio;
    		Image tmp = img.getScaledInstance((int)w, height, Image.SCALE_SMOOTH);
    		BufferedImage resized = new BufferedImage((int)w, height, BufferedImage.TYPE_INT_ARGB);
    		Graphics2D g2d = resized.createGraphics();
    		g2d.drawImage(tmp, 0, 0, null);
    		g2d.dispose();
    		return resized;
    	}

    	public static BufferedImage combine(BufferedImage img, BufferedImage maskingImg) {
    		BufferedImage combined = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
    		Graphics g = combined.getGraphics();
    		g.drawImage(img, 0, 0, null);
    		g.drawImage(maskingImg, 0, 0, null);

    		return combined;
    	}

    	public static BufferedImage read(File imageFile) {
    		BufferedImage img = null;
    		try {
				img = ImageIO.read(imageFile);
			} catch (IOException e) {
				return null;
			}
    		return img;
    	}
    }

    public static class session {

        public static HttpSession getSession(boolean force) {
            ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder
                    .getRequestAttributes();
            HttpSession session = attr.getRequest().getSession(force);
            return session;
        }

        public static String getSessionId() {
            return getSession(true).getId();
        }

        public static void invalidate() {
            HttpSession session = getSession(false);
            if (session == null) {
                return;
            }

            Enumeration<String> eNum = session.getAttributeNames();
            String attributeName = null;
            while (eNum.hasMoreElements()) {
                attributeName = eNum.nextElement();
                session.removeAttribute(attributeName);
            }
            session.invalidate();
            SecurityContext sc = SecurityContextHolder.getContext();
            if (sc != null) {
                sc.setAuthentication(null);
            }
        }
    }


    public static class masking {
        /**
         * 이메일 마스킹 적용
         *
         * @param email : 이메일(복호화된 이메일)
         * @return 마스킹된 이메일
         */
        public static String maskingEmail(String email)
        {
        	if (string.isBlank(email)) {
        		return null;
        	}

    		String mailHost = "";  // 메일 호스팅 서버 주소(@메일서버)
    		String mailAcct = "";  // 메일 계정

    		// 이메일 계정/호스팅 정규식 추출
    		Pattern pattern = Pattern.compile("@\\w+\\.\\w+(\\.\\w+)?");  // 메일 호스팅주소 정규식 패턴
    		Matcher matcher = pattern.matcher(email);
    		while (matcher.find()) {  // 메일 호스팅주소 추출
    			mailHost = matcher.group(0);
    		}
    		mailAcct = RegExUtils.replaceAll(email, pattern, "");  // 이메일로부터 호스팅주소 정규식 패턴으로 호스팅주소 삭제

    		// 이메일 마스킹 처리(sc****@메일서버)
    		StringBuffer maskingEmail = new StringBuffer(mailAcct.substring(0, 2));
    		maskingEmail.append(RegExUtils.replaceAll(mailAcct.substring(2), Pattern.compile("[\\S+]"), "*"));
    		maskingEmail.append(mailHost);

    		return maskingEmail.toString();
        }

        /**
         * 휴대폰 번호 마스킹 적용
         *
         * @param mobileNo :휴대폰번호(복호화된 휴대폰 번호)
         * @return 마스킹 적용된 휴대폰 번호
         */
        public static String maskingMobileNo(String mobileNo)
        {
        	if (string.isBlank(mobileNo)) {
        		return null;
        	}

            // 숫자를 제외한 모든문자 삭제
            //mobileNo = String.join("", mobileNo.split("[\\D]"));
            mobileNo = RegExUtils.replaceAll(mobileNo, Pattern.compile("[\\D]"), "");

            // 핸드폰 번호 마스킹 처리(010 1**4 ****)
            String[] arrMobileNo = null;
            switch (mobileNo.length()) {
    	        case 10: // 핸드폰 번호가 10자리(구형 번호 패턴)인 경우 010-1*3-****으로 출력
    	        	arrMobileNo = new String[]{mobileNo.substring(0, 3), mobileNo.substring(3, 6)};
    	        	arrMobileNo[1] = arrMobileNo[1].substring(0, 1) + "*" + arrMobileNo[1].substring(2);
    	        	break;
    	        case 11: // 핸드폰 번호가 11자리인 경우 010-1**4-****으로 출력
    	        	arrMobileNo = new String[]{mobileNo.substring(0, 3), mobileNo.substring(3, 7)};
    	        	arrMobileNo[1] = arrMobileNo[1].substring(0, 1) + "**" + arrMobileNo[1].substring(3);
    	        	break;
    	        default: // 그 외에는 null 출력
    	        	return null;
            }
            // 마지막 4자리는 마스킹으로 붙임
            arrMobileNo = ArrayUtils.add(arrMobileNo, "****");

            return String.join("-", arrMobileNo);
        }

        /**
         * 생년월일 마스킹 적용
         *
         * @param birthday : 생년월일(복호화된 생년월일)
         * @param delimiter : 문자 구분자
         * @return 마스킹 적용된 생년월일(****.**.**)
         */
        public static String maskingBirthday(String birthday, String delimiter)
        {
        	if (string.isBlank(birthday)) {
        		return null;
        	}

            // 숫자를 제외한 모든문자 삭제
            //mobileNo = String.join("", mobileNo.split("[\\D]"));
        	birthday = RegExUtils.replaceAll(birthday, Pattern.compile("[\\D]"), "");

        	// 구분자가 비어 있는 경우 .으로 기본값 세팅
        	if (string.isBlank(delimiter)) {
        		delimiter = ".";
        	}

            // 생년월일 마스킹 처리(****.**.**)
            String[] arrBirthday = null;
            switch (birthday.length()) {
    	        case 6: // 생년월일이 6자리인 경우 ****.**.**으로 출력
    	        case 8: // 생년월일이 8자리인 경우 ****.**.**으로 출력
    	        	arrBirthday = new String[]{"****", "**", "**"};
    	        	break;
    	        default: // 그 외에는 null 출력
    	        	return null;
            }

            return String.join(delimiter, arrBirthday);
        }

        /**
         * 로그인ID 마스킹 적용
         *
         * @param loginId : 로그인ID
         * @return 마스킹 적용된 로그인ID(Ai****)
         */
        public static String maskingLoginId(String loginId)
        {
        	if (string.isBlank(loginId)) {
        		return null;
        	}

    		// 로그인ID 마스킹 처리(Ai****)
    		StringBuffer maskingLoginId = new StringBuffer(loginId.substring(0, 2));
    		maskingLoginId.append(RegExUtils.replaceAll(loginId.substring(2), Pattern.compile("[\\S+]"), "*"));

    		return maskingLoginId.toString();
    	}

        /**
         * 명칭 마스킹 적용
         *
         * @param name : 명칭
         * @return 마스킹 적용된 명칭(홍**)
         */
        public static String maskingName(String name)
        {
        	if (string.isBlank(name)) {
        		return null;
        	}

    		// 명칭 마스킹 처리(Ai****)
    		StringBuffer maskingName = new StringBuffer(name.substring(0, 2));
    		maskingName.append(RegExUtils.replaceAll(name.substring(2), Pattern.compile("[\\S+]"), "*"));

    		return maskingName.toString();
    	}
    }
}
