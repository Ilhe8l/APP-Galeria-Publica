package guilherme.souza;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

// objetivo: mostrar ao Adapter como verificar se dois itens do tipo ImageData são iguais
class ImageDataComparator extends DiffUtil.ItemCallback<ImageData> {
    @Override
    // recebe 2 objetos (tipo ImageData) > retorna True se for igual; False se nao
    public boolean areItemsTheSame(@NonNull ImageData oldItem, @NonNull ImageData newItem) {
        // Id is unique.
        return oldItem.uri.equals(newItem.uri);
    }

    @Override
    // recebe 2 objetos (tipo ImageData) > retorna True se tiverem o mesmo conteudo; False se nao
    public boolean areContentsTheSame(@NonNull ImageData oldItem, @NonNull ImageData newItem) {
        return oldItem.uri.equals(newItem.uri);
    }
}
