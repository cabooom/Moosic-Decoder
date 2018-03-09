package com.constantine.moosicdecoder;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.framebody.AbstractFrameBodyTextInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity{

    final static int key[] = {
            0x0d, 0x1e, 0x2f, 0x40, 0x51, 0x62, 0x73, 0x84, 0x95, 0xa6, 0xb7, 0xc8, 0xd9, 0xea, 0xfb, 0x0c,
            0x1d, 0x2e, 0x3f, 0x50, 0x61, 0x72, 0x83, 0x94, 0xa5, 0xb6, 0xc7, 0xd8, 0xe9, 0xfa, 0x0b, 0x1c,
            0x2d, 0x3e, 0x4f, 0x60, 0x71, 0x82, 0x93, 0xa4, 0xb5, 0xc6, 0xd7, 0xe8, 0xf9, 0x0a, 0x1b, 0x2c,
            0x3d, 0x4e, 0x5f, 0x70, 0x81, 0x92, 0xa3, 0xb4, 0xc5, 0xd6, 0xe7, 0xf8, 0x09, 0x1a, 0x2b, 0x3c,
            0x4d, 0x5e, 0x6f, 0x80, 0x91, 0xa2, 0xb3, 0xc4, 0xd5, 0xe6, 0xf7, 0x08, 0x19, 0x2a, 0x3b, 0x4c,
            0x5d, 0x6e, 0x7f, 0x90, 0xa1, 0xb2, 0xc3, 0xd4, 0xe5, 0xf6, 0x07, 0x18, 0x29, 0x3a, 0x4b, 0x5c,
            0x6d, 0x7e, 0x8f, 0xa0, 0xb1, 0xc2, 0xd3, 0xe4, 0xf5, 0x06, 0x17, 0x28, 0x39, 0x4a, 0x5b, 0x6c,
            0x7d, 0x8e, 0x9f, 0xb0, 0xc1, 0xd2, 0xe3, 0xf4, 0x05, 0x16, 0x27, 0x38, 0x49, 0x5a, 0x6b, 0x7c,
            0x8d, 0x9e, 0xaf, 0xc0, 0xd1, 0xe2, 0xf3, 0x04, 0x15, 0x26, 0x37, 0x48, 0x59, 0x6a, 0x7b, 0x8c,
            0x9d, 0xae, 0xbf, 0xd0, 0xe1, 0xf2, 0x03, 0x14, 0x25, 0x36, 0x47, 0x58, 0x69, 0x7a, 0x8b, 0x9c,
            0xad, 0xbe, 0xcf, 0xe0, 0xf1, 0x02, 0x13, 0x24, 0x35, 0x46, 0x57, 0x68, 0x79, 0x8a, 0x9b, 0xac,
            0xbd, 0xce, 0xdf, 0xf0, 0x01, 0x12, 0x23, 0x34, 0x45, 0x56, 0x67, 0x78, 0x89, 0x9a, 0xab, 0xbc,
            0xcd, 0xde, 0xef, 0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99, 0xaa, 0xbb, 0xcc,
            0xdd, 0xee, 0xff, 0x10, 0x21, 0x32, 0x43, 0x54, 0x65, 0x76, 0x87, 0x98, 0xa9, 0xba, 0xcb, 0xdc,
            0xed, 0xfe, 0x0f, 0x20, 0x31, 0x42, 0x53, 0x64, 0x75, 0x86, 0x97, 0xa8, 0xb9, 0xca, 0xdb, 0xec,
            0xfd, 0x0e, 0x1f, 0x30, 0x41, 0x52, 0x63, 0x74, 0x85, 0x96, 0xa7, 0xb8, 0xc9, 0xda, 0xeb, 0xfc
    };

    public void xorFiles(File in, File out)throws IOException
    {
        RandomAccessFile reader=new RandomAccessFile(in,"r");
        RandomAccessFile writer=new RandomAccessFile(out,"rw");
        byte[] buffer  = new byte[256];

        reader.read(buffer);
        xor(buffer);
        writer.write(buffer);
        while(true)
        {
            int o = reader.read(buffer);
            if(o < 0)
                break;
            xor(buffer);
            writer.write(buffer);

        }
        writer.close();
        reader.close();
    }

    public byte[] xor(byte[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] ^= (byte)key[i];
        }
        return a;
    }

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        textView.setText("Moosic decoder by -Constantine-");
        int REQUEST_CODE = 0;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    public int doDecode(File file, File outputDir) throws IOException {
        final String filename = file.getName();
        File output = new File(outputDir + "/" + filename);
        xorFiles(file, output);
        final String newName = renameMp3File(output);

        final String text = "\n" + filename + " --> " + newName;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.append(text);
            }
        });

        return 0;
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private String renameMp3File(File file) throws IOException {
        String artist = "";
        String track = "";
        File tempFile = new File(getApplicationInfo().dataDir + "/temp.mp3");
        copyFile(file, tempFile);
        file.delete();
        try {
            MP3File f = (MP3File)AudioFileIO.read(tempFile);
            Tag tag = f.getTag();

            if(tag != null) {
                artist = tag.getFirst(FieldKey.ARTIST);
                track = tag.getFirst(FieldKey.TITLE);
            }
            else {
                if (f.hasID3v2Tag()) {
                    AbstractID3v2Tag v2tag = f.getID3v2Tag();
                    AbstractID3v2Frame frame = v2tag.getFirstField(ID3v24Frames.FRAME_ID_ARTIST);
                    if (frame.getBody() instanceof AbstractFrameBodyTextInfo) {
                        artist = ((AbstractFrameBodyTextInfo) frame.getBody()).getText();
                    }
                    frame = v2tag.getFirstField(ID3v24Frames.FRAME_ID_TITLE);
                    if (frame.getBody() instanceof AbstractFrameBodyTextInfo) {
                        track = ((AbstractFrameBodyTextInfo) frame.getBody()).getText();
                    }
                }
            }
        } catch (Exception e) {
        }

        String filename = "";
        if ( !artist.isEmpty() && !track.isEmpty()) {
            filename = artist + " - " + track + ".mp3";
            copyFile(tempFile, new File(file.getParent() + '/' + filename));
        } else {
            String filedir = file.getParent();
            filename = file.getName().substring(0, file.getName().lastIndexOf('.'))+".mp3";
            copyFile(tempFile, new File (filedir +'/'+filename));
        }
        return filename;
    }

    public void traverse (File dir, File outputDir) throws IOException {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file: files) {
                doDecode(file, outputDir);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.append("\n---- Декодирование завершено ----");
            }
        });
    }

    public String findExternalDir() {
        for(File dir: getExternalFilesDirs(null)) {
            File moosicDirectory = new File(dir + "/../../ru.mail.mymusic/files/Music");
            if (moosicDirectory.exists() && moosicDirectory.isDirectory() && moosicDirectory.list().length > 0){
                return dir.getAbsolutePath();
            }
        }
        return "";
    }
    public void decode(View view) {
        final String decoderDirectory = findExternalDir();
        if (decoderDirectory.isEmpty()) {
            textView.append("\nФайлов не найдено.");
            return;
        }

        final String moosicDirectory = new File(decoderDirectory).getParentFile().getParentFile() + "/ru.mail.mymusic/files/Music";

        textView.append("\nТреки декодируются из " + moosicDirectory + " в " + decoderDirectory);
        Button btn = (Button) findViewById(R.id.button);
        btn.setEnabled(false);

        final String finalMoosicDirectory = moosicDirectory;
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    traverse(new File(finalMoosicDirectory), new File(decoderDirectory));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
        return;
    }
}
