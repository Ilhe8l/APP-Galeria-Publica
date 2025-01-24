package guilherme.souza;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryRepository {
    Context context;
    // obtendo o context - atraves dele conseguimos o conteudo da galeria
    public GalleryRepository(Context context) {
        this.context = context;
    }
    // definindo método loadImageData - ele indica qual ordem e qtd de fotos deve aparecer
    public List<ImageData> loadImageData(Integer limit, Integer offSet) throws FileNotFoundException {
        //criando a lista do metodo acima
        List<ImageData> imageDataList = new ArrayList<>();
        //pegando as dimensoes de cada foto
        int w = 100;
        int h = 100;
        //vamos guardar as imagens atraves do mediaStore
        String[] projection = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE
        };
        //definindo que queremos todas as fotos
        String selection = null;
        String selectionArgs[] = null;
        // ordenando de acordo com a data de criacao
        String sort = MediaStore.Images.Media.DATE_ADDED;

        //verifica qual versao do android esta
        Cursor cursor = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            //tupla
            Bundle queryArgs = new Bundle();

            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
            // sort
            queryArgs.putString(ContentResolver.QUERY_ARG_SORT_COLUMNS, sort);
            queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_ASCENDING);
            // limit, offset
            queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, limit);
            queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, offSet);
            //a consulta eh aqui
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    queryArgs,
                    null
            );
        } else {
            cursor = context.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    sort + " ASC LIMIT " + String.valueOf(limit) + " OFFSET " + String.valueOf(offSet)
            );
        }
        //a partir daqui eh obtido dados para as fotos
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);

        while (cursor.moveToNext()) {
            // Get values of columns for a given image.
            long id = cursor.getLong(idColumn);
            Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            String name = cursor.getString(nameColumn);
            int dateAdded = cursor.getInt(dateAddedColumn);
            int size = cursor.getInt(sizeColumn);
            Bitmap thumb = Util.getBitmap(context, contentUri, w, h);

            // Stores column values and the contentUri in a local object
            // that represents the media file.
            imageDataList.add(new ImageData(contentUri, thumb, name, new Date(dateAdded * 1000L), size));
        }
        return imageDataList;
    }
}