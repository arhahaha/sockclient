package com.naotakapp.socsrv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Socket Server
 *
 */
public class socsrv 
{
    private static int port = 9998;
    
    public static void main( String[] args ) throws IOException
    {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket( port );

            while( true ){
                Socket socket     = serverSocket.accept();
                ServiceThread srv = new ServiceThread( socket );
                srv.start();
                SendThread    snd = new SendThread( socket );
                snd.start();
            }
    
        } catch( Exception e ) {
            e.printStackTrace();

        } finally {
            if( serverSocket != null ) {
                serverSocket.close();
            }
        }

    }
}

class ServiceThread extends Thread {
    
    private static int intsize = 4;

    Socket srvsoc = null;

    public ServiceThread( Socket soc ){
        srvsoc = soc;
    }

    public void run() {

        System.out.println("Client Connected : Thread=[" + getName() + "]");
        InputStream  in = null;
    
        try {
            in = srvsoc.getInputStream();
            
            while( !srvsoc.isClosed() ) {

                int     ret    = 0;
                byte[]  lendat = new byte[intsize];
                
                ret = readData( srvsoc, in, lendat );
                if( ret != lendat.length ){
                    System.out.println("Socket Error [Length Read].");
                    break;
                }
                ByteBuffer buf = ByteBuffer.wrap( lendat );
                buf.order( ByteOrder.BIG_ENDIAN );
                int soclen = buf.getInt(0);
                System.out.println("Socket Data Length : [" + soclen + "].");

                byte[]  bodydat = new byte[soclen];
                ret = readData( srvsoc, in, bodydat );
                if( ret != bodydat.length ){
                    System.out.println("Socket Error [Body Read].");
                    break;
                }

                String sb = new String( bodydat, "UTF-8" );
                System.out.println("Socket Data Body   : [" + sb + "].");

                if( "end".equals( sb ) == true ){
                    System.out.println("Received terminate command : [" + sb + "].");
                    break;
                }
            }

        } catch ( Exception e ) {
            e.printStackTrace();

        } finally {
            try {
                if( in != null ) {
                    in.close();
                }
                if( srvsoc != null ) {
                    srvsoc.close();
                }
            } catch ( final IOException e ) { 
            }
        }   
        System.out.println("Service Terminate : [" + getName() + "]");
    }

    private static int readData( Socket soc, InputStream in, byte[] data ) throws IOException
    {
        int     datsiz = data.length;
        int     total  = 0;

        while( !soc.isClosed() ) {

            int ret = in.read( data, total, datsiz - total );
            if( ret <= 0 ){
                return -1;
            }
            total += ret;

            if( total >= datsiz ){
                break;
            }
        }
        return total;
    }
}

class SendThread extends Thread {

    private static int intsize = 4;

    Socket sndsoc = null;

    public SendThread( Socket soc ){
        sndsoc = soc;
    }

    public void run() {

        System.out.println("Server [Send Message Thread] Start : Thread=[" + getName() + "]");
        OutputStream   out  = null;
        BufferedReader br   = null;
        Path           file = Paths.get("D:/TEMP/MsgDat01.txt");
    
        try {
            out = sndsoc.getOutputStream();

            br = Files.newBufferedReader( file, Charset.forName("SJIS") );
            String text;
            while ( !sndsoc.isClosed() ) {

                text = br.readLine();
                if( text == null ){
                    br.close();
                    br = Files.newBufferedReader( file, Charset.forName("SJIS") );
                    continue;
                }

                if( text.startsWith( "#" ) == true ){
                    continue;
                }

                final byte[] strbuf  = text.getBytes( "SJIS" );
                final ByteBuffer buf = ByteBuffer.allocate(intsize);
                buf.putInt(strbuf.length);
                out.write(buf.array());
                out.write( strbuf );

                System.out.println( "Send Msg : len=[" + strbuf.length + "] body=[" + text + "]" );

                Thread.sleep( 2000 );
            }

        } catch ( Exception e ) {
            e.printStackTrace();

        } finally {
            try {
                if( br != null ) {
                    br.close();
                }
                if( sndsoc != null ) {
                    sndsoc.close();
                }
            } catch ( final IOException e ) { 
            }
        }

        System.out.println("Send Terminate : [" + getName() + "]");  
    }
}
