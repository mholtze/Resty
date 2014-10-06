package us.monoid.web;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static us.monoid.web.Resty.*;

import org.junit.Test;

import sun.net.www.protocol.https.HttpsURLConnectionImpl;
import us.monoid.web.Resty;
import us.monoid.web.ssl.AllowAllHostnameVerifier;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.Socket;

public class RestyTest {
	
	private static final String GOOGLE_QUERY_DATA = "q=Resty&hl=en&num=20";

	static {
		System.setProperty("content.types.user.table","src/test/java/us/monoid/web/mimecap.properties");
	}
	@Test
	public void sampleUse() throws Exception {
        Resty r = new Resty();
        int result = r.json(getClass().getResource("test.json").toString()).object()
                .getAsJsonObject("key")
                .getAsJsonObject("subkey")
                .getAsJsonPrimitive("secret").getAsInt();
        assertEquals(result, 42);
	}

//	@Test
//	public void geoNames() throws Exception {
//		Resty r = new Resty();
//		Object name = r.json("http://ws.geonames.org/postalCodeLookupJSON?postalcode=66780&country=DE").get("postalcodes[0].placeName");
//		assertEquals(name, "Rehlingen-Siersburg");
//	}
//	
	@Test
	public void formDataGet() throws Exception {
		Resty r = new Resty();
		String t = r.text("http://www.google.com/search?" +  GOOGLE_QUERY_DATA).toString();
		System.out.println(t);
		assertTrue(t.contains("resty"));
	}
 
	@Test
	public void formDataPost() throws Exception {
		Resty r = new Resty();
		String t = r.text("http://www.cs.tut.fi/cgi-bin/run/~jkorpela/echoraw.cgi",
				form(GOOGLE_QUERY_DATA)).toString();
		System.out.println(t);
		assertTrue(t.contains(GOOGLE_QUERY_DATA));
	}
	
//	@Test  Search API is not available anymore
//	public void postYahoo() throws Exception {
//		Resty r = new Resty();
//		String firstResult = r.xml("http://api.search.yahoo.com/WebSearchService/V1/webSearch",
//				form("appid=YahooDemo&query=Resty+java&results=10")).get("/ResultSet/Result[1]/Title",String.class);
//		System.out.println(firstResult);
//		
//	}
//	
//	@Test  this search API is not available anymore
//	public void postMultipartYahoo() throws Exception {
//		Resty r = new Resty();
//		String firstResult = r.xml("http://api.search.yahoo.com/WebSearchService/V1/webSearch",
//				form(data("appid","YahooDemo"),data("query", "Resty+java"), data("results", "10"))).get("/ResultSet/Result[1]/Title",String.class);
//		System.out.println(firstResult);
//		
//	}


    @Test
    public void shouldSetDefaultHostnameVerifierToAllowAllHostnamesWhenIgnoreAllCertsIsSet() {
        Resty.ignoreAllCerts();

        HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();

        assertThat("Should be static AllowAllHostnameVerifier", defaultHostnameVerifier, equalTo(AllowAllHostnameVerifier.ALLOW_ALL_HOSTNAMES));
    }
	
}
