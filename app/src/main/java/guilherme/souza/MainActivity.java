package guilherme.souza;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import guilherme.souza.MainViewModel;

public class MainActivity extends AppCompatActivity {

    //definicao do botao
    BottomNavigationView bottomNavigationView;

    static int RESULT_REQUEST_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        checkForPermissions(permissions);

        //obtendo a referencia do MainViewMode
        final MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);

        //obtendo a referencia do BottonNavigationView
        bottomNavigationView = findViewById(R.id.btnNav);
        //escutador de cliques no btn
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            //apos clicar, essa funcao sera chamada e indicar a opcao que o usuario escolheu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //guardamos a opcao indicada
                vm.setNavigationOpSelected(item.getItemId());
                //aqui é um if/else que muda de acordo com a opcao
                if (item.getItemId() == R.id.gridViewOp) {
                    GridViewFragment gridViewFragment = GridViewFragment.newInstance();
                    setFragment(gridViewFragment);
                } else if (item.getItemId() == R.id.listViewOp) {
                    ListViewFragment listViewFragment = ListViewFragment.newInstance();
                    setFragment(listViewFragment);
                }
                return true;
            }
        });


    }

    // recebe de paramentro um fragment
    void setFragment(Fragment fragment) {
        // gerenciamento de fragmentos
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // to setando esse parametro no elemento fragContainer
        fragmentTransaction.replace(R.id.fragContainer, fragment);
        // o fragmento pertence ao botao voltar
        fragmentTransaction.addToBackStack(null);
        // commit
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();  // aceitamos uma lista de permissões como entrada

        for (String permission : permissions) { // vamos verificar cada permissao
            if (!hasPermission(permission)) { // caso nao tenha sido confirmada
                permissionsNotGranted.add(permission); // sera guarda em uma lista que mantem apenas as desse tipo
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsNotGranted.size() > 0) {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]), RESULT_REQUEST_PERMISSION);
            }
        }
        //se o app ja tiver todas as informacoes que precisa,acessamos o mainviewmodel (leitura das fotos)
        else {
            //obtendo a opcao escolhida pelo usuario
            MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);
            int navigationOpSelected = vm.getNavigationOpSelected();
            //setando essa opçao.
            bottomNavigationView.setSelectedItemId(navigationOpSelected);
        }

    }

    // metodo que verifica se uma determinada permissão já foi concedida ou nao
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    // esse metodo so sera invocado após o usuário conceder ou não as permissões requisitadas
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        if (requestCode == RESULT_REQUEST_PERMISSION) {
            // verifica se cada permissao foi concedida pelo usuario ou nao
            for (String permission : permissions) {
                if (!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }
        // se ha alguma que não foi concedida e eh necessaria, o sistema dispara uma mensagem
        if (permissionsRejected.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) // vamos mostrar a mensagem
                {
                    new AlertDialog.Builder(MainActivity.this).
                            setMessage("Para usar essa app é preciso conceder essas permissões"). // mensagem disparada
                            setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        // vamos pedir novamente a permissao necessaria para o funcionamento do sistema
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            requestPermissions(permissionsRejected.toArray(new
                                    String[permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                        }
                    }).create().show();
                }
            }
        } else {
            MainViewModel vm = new ViewModelProvider(this).get(MainViewModel.class);
            int navigationOpSelected = vm.getNavigationOpSelected();
            bottomNavigationView.setSelectedItemId(navigationOpSelected);
        }
    }
}




