package com.ssh.common.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;

public abstract class IOUtils {

    public static void closeQuietly(final Reader input) {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(final Writer output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(final InputStream input) {
        closeQuietly((Closeable) input);
    }

    public static void closeQuietly(final OutputStream output) {
        closeQuietly((Closeable) output);
    }

    public static void closeQuietly(final Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (final IOException ioe) {
            // ignore
        }
    }

    public static void closeQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (final Closeable closeable : closeables) {
            closeQuietly(closeable);
        }
    }

    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    public static void closeQuietly(final Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (final IOException ioe) {
                // ignored
            }
        }
    }

    public static void closeQuietly(final ServerSocket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (final IOException ioe) {
                // ignored
            }
        }
    }

    /**
     * Copies from one stream to another.
     *
     * @param in       InputStrem to read from
     * @param out      OutputStream to write to
     * @param buffSize the size of the buffer
     */
    public static void copyBytes(InputStream in, OutputStream out, int buffSize) throws IOException {
        PrintStream ps = out instanceof PrintStream ? (PrintStream) out : null;
        byte buf[] = new byte[buffSize];
        int bytesRead = in.read(buf);
        while (bytesRead >= 0) {
            out.write(buf, 0, bytesRead);
            if ((ps != null) && ps.checkError()) {
                throw new IOException("Unable to write to output stream.");
            }
            bytesRead = in.read(buf);
        }
    }

    public static void copyBytes(InputStream in, OutputStream out, int buffSize, boolean close) throws IOException {
        try {
            copyBytes(in, out, buffSize);
            if (close) {
                out.close();
                out = null;
                in.close();
                in = null;
            }
        } finally {
            if (close) {
                closeQuietly(out);
                closeQuietly(in);
            }
        }
    }

    /**
     * Copies count bytes from one stream to another.
     *
     * @param in    InputStream to read from
     * @param out   OutputStream to write to
     * @param count number of bytes to copy
     * @param close whether to close the streams
     * @throws IOException if bytes can not be read or written
     */
    public static void copyBytes(InputStream in, OutputStream out, long count, boolean close) throws IOException {
        byte buf[] = new byte[4096];
        long bytesRemaining = count;
        int bytesRead;
        try {
            while (bytesRemaining > 0) {
                int bytesToRead = (int) (bytesRemaining < buf.length ? bytesRemaining : buf.length);
                bytesRead = in.read(buf, 0, bytesToRead);
                if (bytesRead == -1) {
                    break;
                }
                out.write(buf, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
            if (close) {
                out.close();
                out = null;
                in.close();
                in = null;
            }
        } finally {
            if (close) {
                closeQuietly(out);
                closeQuietly(in);
            }
        }
    }

}
