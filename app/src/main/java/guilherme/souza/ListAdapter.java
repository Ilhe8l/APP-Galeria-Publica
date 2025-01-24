package guilherme.souza;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;

import java.text.SimpleDateFormat;

import guilherme.souza.MyViewHolder;

public class ListAdapter extends PagingDataAdapter<ImageData, MyViewHolder> {

    public ListAdapter(@NonNull DiffUtil.ItemCallback<ImageData> diffCallback) {
        // diffCallback = compara dois elementos da lista
        super(diffCallback);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflador de layouts: ler o arquivo xml
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // cria os elementos que devem ser criados e guarda dentro da View
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        // guarda o objeto view dentro de outro objeto (MyViewHolder), depois retorna a funcao
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // recebemos o item para preencher a UI
        ImageData imageData = getItem(position);

        // preenchendo as sessoes com os dados
        TextView tvName = holder.itemView.findViewById(R.id.tvName);
        tvName.setText(imageData.fileName);

        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        tvDate.setText("Data: " + new SimpleDateFormat("HH:mm dd/MM/yyyy").format(imageData.date));

        TextView tvSize = holder.itemView.findViewById(R.id.tvSize);
        tvSize.setText("Tamanho: " + String.valueOf(imageData.size));

        Bitmap thumb = imageData.thumb;
        ImageView imageView = holder.itemView.findViewById(R.id.imThumb);
        imageView.setImageBitmap(thumb);
    }
}
