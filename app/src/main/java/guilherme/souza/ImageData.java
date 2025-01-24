package guilherme.souza;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;

public class ImageData {
    //endereço uri do arquivo de foto
    public Uri uri;
    //imagem em minitura da foto
    public Bitmap thumb;
    // nome do arquivo de foto
    public String fileName;
    //data em que a foto foi criada
    public Date date;
    //tamanho em bytes do arquivo de foto
    public int size;

    public ImageData(Uri uri, Bitmap thumb, String fileName, Date date, int size) {
        this.uri = uri;
        this.thumb = thumb;
        this.fileName = fileName;
        this.date = date;
        this.size = size;
    }
}