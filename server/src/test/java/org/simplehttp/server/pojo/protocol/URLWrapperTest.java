package org.simplehttp.server.pojo.protocol;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class URLWrapperTest {
    String url = "http://mail.google.com:80/context/?mailid=10001&userid=1234";
    String bad_url_1 = "http://mail.google.com:80/context/?mailid=10001userid=1234";
    String bad_url_2 = "http://mail.google.com:80/context/?mailid=10001&&userid=1234";
    String bad_url_3 = "http://mail.google.com:80/context/?mailid=10001&userid1234";
    String bad_url_unencoding = "http://mail.google.com:80/context/?邮箱id=10001&userid1234";
    String url_empty_param = "http://mail.google.com:80/context/index.html";

    @Test
    public void testURLWrapper() throws MalformedURLException {
        URLWrapper urlWrapper = new URLWrapper(new URL(url));
        assertEquals("10001",urlWrapper.getQueryValue("mailid"));
        assertEquals("1234",urlWrapper.getQueryValue("userid"));
    }

    @Test
    public void badUrlTest() throws MalformedURLException {
        assertThrows(RuntimeException.class, () -> new URLWrapper(new URL(bad_url_1)));
        assertThrows(RuntimeException.class, () -> new URLWrapper(new URL(bad_url_2)));
        assertThrows(RuntimeException.class, () -> new URLWrapper(new URL(bad_url_3)));
    }

    @Test
    public void urlUnEncodingTest() throws MalformedURLException {
        assertThrows(RuntimeException.class, () -> new URLWrapper(new URL(bad_url_unencoding)));
    }

    @Test
    public void urlEmptyParamTest() throws MalformedURLException {
        new URLWrapper(new URL(url_empty_param));
    }
}