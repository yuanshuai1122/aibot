package com.aibot.utils;

import java.io.*;

/**
 * @program: aibot-server
 * @description: StreamUtil
 * @author: yuanshuai
 * @create: 2023-04-23 13:28
 **/
public class StreamUtil {

    public static String readText(InputStream in) throws IOException {
        return readText(in, (String)null, -1);
    }

    public static String readText(InputStream in, String encoding, int bufferSize) throws IOException {
        Reader reader = encoding == null ? new InputStreamReader(in) : new InputStreamReader(in, encoding);
        return readText(reader, bufferSize);
    }

    public static String readText(Reader reader, int bufferSize) throws IOException {
        StringWriter writer = new StringWriter();
        io((Reader)reader, (Writer)writer, bufferSize);
        return writer.toString();
    }

    public static void io(Reader in, Writer out) throws IOException {
        io((Reader)in, (Writer)out, -1);
    }

    public static void io(Reader in, Writer out, int bufferSize) throws IOException {
        if (bufferSize == -1) {
            bufferSize = 4096;
        }

        char[] buffer = new char[bufferSize];

        int amount;
        while((amount = in.read(buffer)) >= 0) {
            out.write(buffer, 0, amount);
        }

    }

}
