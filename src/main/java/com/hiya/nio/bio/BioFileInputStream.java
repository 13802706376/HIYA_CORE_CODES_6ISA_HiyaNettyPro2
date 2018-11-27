package com.hiya.nio.bio;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BioFileInputStream
{
    public static void method2()
    {
        InputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream("src/nomal_io.txt"));
            byte[] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while (bytesRead != -1)
            {
                for (int i = 0; i < bytesRead; i++)
                    System.out.print((char) buf[i]);
                bytesRead = in.read(buf);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
