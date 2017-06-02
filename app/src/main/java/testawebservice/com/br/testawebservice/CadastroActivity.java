package testawebservice.com.br.testawebservice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class CadastroActivity extends AppCompatActivity{

    private static final int REQUEST_CODE = 5678;
    private ImageView ivFoto;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro);

        if(Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        final EditText edNome = (EditText) findViewById(R.id.etNome);
        final EditText edIdade = (EditText) findViewById(R.id.etIdade);
        Button btnSelecionarFoto = (Button) findViewById(R.id.btnSelecionarImagem);
        Button btnCadastrar = (Button) findViewById(R.id.btnCadastro);

        ivFoto = (ImageView) findViewById(R.id.ivSelecioneImagem);


        btnSelecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPermissao(CadastroActivity.this);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = ((BitmapDrawable)ivFoto.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, stream);

                byte[] byteArray =stream.toByteArray();

                UsuarioDAO userDao = new UsuarioDAO();
                Usuario user = new Usuario();
                user.setNome(edNome.getText().toString());
                user.setIdade(Integer.parseInt(edIdade.getText().toString()));
                user.setFoto(byteArray);

                user = userDao.inserirUsuario(user);

                if(user != null){
                    finish();
                }else{
                    Toast.makeText(CadastroActivity.this, "Usuário não inserido", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public static void verificarPermissao(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null && data.getData() != null) {

            Uri uri = data.getData();

            Cursor cursor = getContentResolver().query(uri,	new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },	null, null, null);
            cursor.moveToFirst();

            final String imageFilePath = cursor.getString(0);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);

            ivFoto.setImageBitmap(bitmap);
        }
    }
}
