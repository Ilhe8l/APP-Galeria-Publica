package guilherme.souza;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import guilherme.souza.ImageData;
import kotlinx.coroutines.CoroutineScope;

public class MainViewModel extends AndroidViewModel {
    //estamos guardando um dado, é a opcao escolhida pelo usuario
    int navigationOpSelected = R.id.gridViewOp;
    LiveData<PagingData<ImageData>> pageLv;

    //estamos usando uma extensao do ViewModel, parametro de entrada = solicitacao do usuario
    public MainViewModel(@NonNull Application application) {
        super(application);
        // GalleryRepository = vai ler um bloco de fotos
        GalleryRepository galleryRepository = new GalleryRepository(application);
        // GalleryPagingSource = calcular qual bloco o acima vai querer, tambem dos proximos pedidos
        GalleryPagingSource galleryPagingSource = new GalleryPagingSource(galleryRepository);
        // configuracao do pading3 > apenas 10 fotos p/ pagina
        Pager<Integer, ImageData> pager = new Pager(new PagingConfig(10), () -> galleryPagingSource);
        // obteno o objeto de MainViewModel (vai guardar páginas de dados no cache)
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        // obtendo o LiveData e guardando os dados no MainViewModel
        pageLv = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);

    }

    public LiveData<PagingData<ImageData>> getPageLv() {
        return pageLv;
    }

    //pegando esse dado
    public int getNavigationOpSelected() {
        return navigationOpSelected;
    }
    //setando esse dado
    public void setNavigationOpSelected(int navigationOpSelected) {
        this.navigationOpSelected = navigationOpSelected;
    }
}