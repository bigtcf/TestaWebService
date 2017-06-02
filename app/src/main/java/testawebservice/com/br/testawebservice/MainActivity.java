package testawebservice.com.br.testawebservice;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvUsuarios;
    private UsuarioDAO dao;
    private List<Usuario> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        dao = new UsuarioDAO();

        Button btnAbreTelaCadastro = (Button) findViewById(R.id.btnAbreTelaCadastro);
        lvUsuarios = (ListView) findViewById(R.id.lvUsuarios);

        btnAbreTelaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        lvUsuarios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                boolean result = dao.excluirUsuario(lista.get(position));
                if(result){
                    onResume();
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        lista = dao.buscarTodosUsuarios();

        UsuarioAdapter userAdapter = new UsuarioAdapter(lista, this);

        lvUsuarios.setAdapter(userAdapter);
    }
}
