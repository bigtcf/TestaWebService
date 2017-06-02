package testawebservice.com.br.testawebservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UsuarioAdapter extends BaseAdapter{

    private List<Usuario> users;
    private Context context;

    public UsuarioAdapter(List<Usuario> users, Context context) {
        this.users = users;
        this.context = context;
    }


    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.lista_de_usuarios, parent, false);

        ImageView ivFoto = (ImageView) view.findViewById(R.id.ivFoto);
        TextView tvNome = (TextView) view.findViewById(R.id.tvNome);
        TextView tvIdade = (TextView) view.findViewById(R.id.tvIdade);

        Usuario usuarioDaVez = users.get(position);

        tvNome.setText(usuarioDaVez.getNome());
        tvIdade.setText(usuarioDaVez.getIdade() + " Anos");

        Bitmap bitmap = BitmapFactory.decodeByteArray(usuarioDaVez.getFoto(), 0, usuarioDaVez.getFoto().length);
        ivFoto.setImageBitmap(bitmap);

        return view;
    }
}
