package org.jabref.logic.net;

import org.jabref.gui.fieldeditors.URLUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class URLUtilTest {

    @ParameterizedTest
    @MethodSource("googleSearchURLArguments")
    void cleanGoogleSearchURL(String expectedString, String inputString) throws Exception {
        assertEquals(expectedString, URLUtil.cleanGoogleSearchURL(inputString));
    }

    private static Stream<Arguments> googleSearchURLArguments() {
        return Stream.of(
                // empty text
                Arguments.of("", ""),
                Arguments.of(" ", " "),
                // no URL
                Arguments.of("this is no url!", "this is no url!"),
                // no Google search URL
                Arguments.of("http://dl.acm.org/citation.cfm?id=321811", "http://dl.acm.org/citation.cfm?id=321811"),
                // malformed Google URL
                Arguments.of("https://www.google.de/url♥", "https://www.google.de/url♥"),
                // no queries
                Arguments.of("https://www.google.de/url", "https://www.google.de/url"),
                Arguments.of("https://www.google.de/url?", "https://www.google.de/url?"),
                // no multiple queries
                Arguments.of("https://www.google.de/url?key=value", "https://www.google.de/url?key=value"),
                // no key values
                Arguments.of("https://www.google.de/url?key", "https://www.google.de/url?key"),
                Arguments.of("https://www.google.de/url?url", "https://www.google.de/url?url"),
                Arguments.of("https://www.google.de/url?key=", "https://www.google.de/url?key="),
                // no url param
                Arguments.of("https://www.google.de/url?key=value&key2=value2", "https://www.google.de/url?key=value&key2=value2"),
                // no url param value
                Arguments.of("https://www.google.de/url?url=", "https://www.google.de/url?url="),
                // url param value no URL
                Arguments.of("https://www.google.de/url?url=this+is+no+url", "https://www.google.de/url?url=this+is+no+url"),
                // Http
                Arguments.of(
                        "http://moz.com/ugc/the-ultimate-guide-to-the-google-search-parameters",
                        "http://www.google.de/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAAahUKEwjJurHd2sfHAhWBsxQKHSrEAaM&url=http%3A%2F%2Fmoz.com%2Fugc%2Fthe-ultimate-guide-to-the-google-search-parameters&ei=0THeVYmOJIHnUqqIh5gK&usg=AFQjCNHnid_r_d2LP8_MqvI7lQnTC3lB_g&sig2=ICzxDroG2ENTJSUGmdhI2w"),
                // Https
                Arguments.of(
                        "https://moz.com/ugc/the-ultimate-guide-to-the-google-search-parameters",
                        "https://www.google.de/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAAahUKEwjJurHd2sfHAhWBsxQKHSrEAaM&url=https%3A%2F%2Fmoz.com%2Fugc%2Fthe-ultimate-guide-to-the-google-search-parameters&ei=0THeVYmOJIHnUqqIh5gK&usg=AFQjCNHnid_r_d2LP8_MqvI7lQnTC3lB_g&sig2=ICzxDroG2ENTJSUGmdhI2w"),
                // root domain
                Arguments.of(
                        "https://moz.com/ugc/the-ultimate-guide-to-the-google-search-parameters",
                        "https://google.de/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAAahUKEwjJurHd2sfHAhWBsxQKHSrEAaM&url=https%3A%2F%2Fmoz.com%2Fugc%2Fthe-ultimate-guide-to-the-google-search-parameters&ei=0THeVYmOJIHnUqqIh5gK&usg=AFQjCNHnid_r_d2LP8_MqvI7lQnTC3lB_g&sig2=ICzxDroG2ENTJSUGmdhI2w"),
                // foreign domain
                Arguments.of(
                        "https://moz.com/ugc/the-ultimate-guide-to-the-google-search-parameters",
                        "https://www.google.fr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAAahUKEwjJurHd2sfHAhWBsxQKHSrEAaM&url=https%3A%2F%2Fmoz.com%2Fugc%2Fthe-ultimate-guide-to-the-google-search-parameters&ei=0THeVYmOJIHnUqqIh5gK&usg=AFQjCNHnid_r_d2LP8_MqvI7lQnTC3lB_g&sig2=ICzxDroG2ENTJSUGmdhI2w"),
                // foreign domain co.uk
                Arguments.of(
                        "https://moz.com/ugc/the-ultimate-guide-to-the-google-search-parameters",
                        "https://www.google.co.uk/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAAahUKEwjJurHd2sfHAhWBsxQKHSrEAaM&url=https%3A%2F%2Fmoz.com%2Fugc%2Fthe-ultimate-guide-to-the-google-search-parameters&ei=0THeVYmOJIHnUqqIh5gK&usg=AFQjCNHnid_r_d2LP8_MqvI7lQnTC3lB_g&sig2=ICzxDroG2ENTJSUGmdhI2w"),
                // accept ftp results
                Arguments.of(
                        "ftp://moz.com/ugc/the-ultimate-guide-to-the-google-search-parameters",
                        "https://www.google.fr/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAAahUKEwjJurHd2sfHAhWBsxQKHSrEAaM&url=ftp%3A%2F%2Fmoz.com%2Fugc%2Fthe-ultimate-guide-to-the-google-search-parameters&ei=0THeVYmOJIHnUqqIh5gK&usg=AFQjCNHnid_r_d2LP8_MqvI7lQnTC3lB_g&sig2=ICzxDroG2ENTJSUGmdhI2w")
        );
    }

    @Test
    void isURLshouldAcceptValidURL() {
        assertTrue(URLUtil.isURL("http://www.google.com"));
        assertTrue(URLUtil.isURL("https://www.google.com"));
    }

    @Test
    void isURLshouldRejectInvalidURL() {
        assertFalse(URLUtil.isURL("www.google.com"));
        assertFalse(URLUtil.isURL("google.com"));
    }

}
