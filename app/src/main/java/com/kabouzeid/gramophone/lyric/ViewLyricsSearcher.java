package com.kabouzeid.gramophone.lyric;

import com.kabouzeid.gramophone.lyric.model.Lyric;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewLyricsSearcher {

    private static final String URL = "http://search.crintsoft.com/searchlyrics.htm";

    private static final String SEARCH_LYRICS_URL = "http://search.crintsoft.com/l/";

    private static final String clientUserAgent = "MiniLyrics4Android";

    private static final String clientTag = "client=\"ViewLyricsOpenSearcher\"";

    private static final String searchQueryBase = "<?xml version='1.0' encoding='utf-8' ?><searchV1 artist=\"%s\" title=\"%s\" OnlyMatched=\"1\" %s/>";

    private static final String searchQueryPage = " RequestPage='%d'";

    private static final byte[] magickey = "Mlv1clt4.0".getBytes();

    /**
     * Search method to find Lyrics
     *
     * @param artist artist name
     * @param title  song title
     * @param page   the page
     * @return the lyrics
     * @throws Exception an error occured
     */
    public static Lyric search(final String artist, final String title, int page) throws Exception {
        return searchQuery(String.format(searchQueryBase, artist, title, clientTag + String.format(searchQueryPage, page)));
    }

    /**
     * Search method to find Lyrics
     *
     * @param searchQuery the query
     * @return the lyrics
     * @throws Exception an error occured
     */
    @SuppressWarnings("resource")
    private static Lyric searchQuery(final String searchQuery) throws Exception {
        // Create Client
        final URL url = new URL(URL);
        final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("User-Agent", clientUserAgent);
        final OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(assembleQuery(searchQuery.getBytes("UTF-8")));

        String full;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            // Get the response
            final BufferedReader rd = new BufferedReader
                    (new InputStreamReader(in, "ISO_8859_1"));

            // Get full result
            final StringBuilder builder = new StringBuilder();
            char[] buffer = new char[8192];
            int read;
            while ((read = rd.read(buffer, 0, buffer.length)) > 0) {
                builder.append(buffer, 0, read);
            }
            full = builder.toString();
        } finally {
            urlConnection.disconnect();
        }

        // Decrypt, parse, store, and return the lyrics
        return retrieveLyrics(decryptResult(full));
    }

    /**
     * Add MD5 and Encrypts Search Query
     *
     * @param value the value
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static byte[] assembleQuery(byte[] value) throws NoSuchAlgorithmException, IOException {

        // Create the variable POG to be used in a dirt code
        byte[] pog = new byte[value.length + magickey.length];

        // POG = XMLQuery + Magic Key
        System.arraycopy(value, 0, pog, 0, value.length);
        System.arraycopy(magickey, 0, pog, value.length, magickey.length);

        // POG is hashed using MD5
        byte[] pog_md5 = MessageDigest.getInstance("MD5").digest(pog);

        int j = 0;
        for (byte b : value) {
            j += b;
        }
        int k = (byte) (j / value.length);

        // Value is encrypted
        for (int m = 0; m < value.length; m++)
            value[m] = (byte) (k ^ value[m]);

        // Prepare result code
        final ByteArrayOutputStream result = new ByteArrayOutputStream();

        // Write Header
        result.write(0x02);
        result.write(k);
        result.write(0x04);
        result.write(0x00);
        result.write(0x00);
        result.write(0x00);

        // Write Generated MD5 of POG problaby to be used in a search cache
        result.write(pog_md5);

        // Write encrypted value
        result.write(value);

        // Return magic encoded query
        return result.toByteArray();
    }

    /**
     * Decrypt the result
     *
     * @param value the value to decrypt
     * @return the value decrypted
     */
    public static String decryptResult(String value) {
        // Get Magic key value
        char magickey = value.charAt(1);

        // Prepare output
        final ByteArrayOutputStream neomagic = new ByteArrayOutputStream();

        // Decrypts only the XML
        for (int i = 22; i < value.length(); i++)
            neomagic.write((byte) (value.charAt(i) ^ magickey));

        // Return value
        return neomagic.toString();
    }

    /**
     * Retrieve the Lyrics
     *
     * @param resultToParse the result to parse
     * @return the lyrics
     * @throws IOException an error occured
     */
    public static Lyric retrieveLyrics(String resultToParse) throws IOException {

        final Lyric result = new Lyric();

        resultToParse = resultToParse.replaceAll("\\x00", "*");

        final Pattern patternLrc = Pattern.compile("[a-z0-9/_]*?\\.lrc");
        final Matcher matcherLrc = patternLrc.matcher(resultToParse);

        final Pattern patternTxt = Pattern.compile("[a-z0-9/_]*?\\.txt");
        final Matcher matcherTxt = patternTxt.matcher(resultToParse);

        final String lrc = matcherLrc.find() ? matcherLrc.group(0) : null;
        String txt = null;

        if (lrc == null) {
            txt = matcherTxt.find() ? matcherTxt.group(0) : null;
        }

        if (lrc != null || txt != null) {

            String type = "txt";
            String lyric = txt;

            if (txt == null) {
                type = "lrc";
                lyric = lrc;
            }

            result.setType(type);
            final URL url = new URL(SEARCH_LYRICS_URL + lyric);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String inputLine;
                final StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    response.append("\n\r");
                }
                in.close();

                result.setLyrics(response.toString());
            }

        }

        return result;
    }

}
