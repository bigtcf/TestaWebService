package testawebservice.com.br.testawebservice;

import android.util.Base64;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class UsuarioDAO {

    private static final String URL = "http://192.168.188.2:8080/ExemploWS/services/UsuarioDAO?wsdl";
    private static final String NAMESPACE = "http://exemploWS.videoaula.com.br";

    private static final String INSERIR = "inserirUsuario";
    private static final String EXCLUIR = "excluirUsuario";
    private static final String ATUALIZAR = "atualizarUsuario";
    private static final String BUSCAR_TODOS_USUARIOS = "buscarTodosUsuarios";
    private static final String BUSCAR_POR_ID = "buscarUsuarioPorID";


    public Usuario inserirUsuario(Usuario user){

        SoapObject inserirUsuario = new SoapObject(NAMESPACE, INSERIR);

        SoapObject usuario = new SoapObject(NAMESPACE,"user");

        usuario.addProperty("id", user.getId());
        usuario.addProperty("nome", user.getNome());
        usuario.addProperty("idade", user.getIdade());
        usuario.addProperty("foto", user.getFoto());

        inserirUsuario.addSoapObject(usuario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirUsuario);

        new MarshalBase64().register(envelope);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call("urn:" + INSERIR, envelope);

            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();

            int id = Integer.parseInt(resposta.toString());

            if(id > 0){
                user.setId(id);
                return user;
            }else{
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean excluirUsuario(Usuario user){

        SoapObject inserirUsuario = new SoapObject(NAMESPACE, EXCLUIR);

        SoapObject usuario = new SoapObject(NAMESPACE,"user");

        usuario.addProperty("id", user.getId());
        usuario.addProperty("nome", user.getNome());
        usuario.addProperty("idade", user.getIdade());

        inserirUsuario.addSoapObject(usuario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirUsuario);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call("urn:" + EXCLUIR, envelope);

            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(resposta.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Usuario> buscarTodosUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        SoapObject buscarTodosUsuarios = new SoapObject(NAMESPACE, BUSCAR_TODOS_USUARIOS);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarTodosUsuarios);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call("urn:" + BUSCAR_TODOS_USUARIOS, envelope);

            if(envelope.getResponse() instanceof SoapObject){
                SoapObject resposta = (SoapObject) envelope.getResponse();

                Usuario user = new Usuario();
                user.setId(Integer.parseInt(resposta.getProperty("id").toString()));
                user.setNome(resposta.getProperty("nome").toString());
                user.setIdade(Integer.parseInt(resposta.getProperty("idade").toString()));

                String foto = resposta.getProperty("foto").toString();

                byte[] bt = Base64.decode(foto, Base64.DEFAULT);
                user.setFoto(bt);
                lista.add(user);
            }else{
                Vector<SoapObject> retorno = (Vector<SoapObject>)envelope.getResponse();

                for (SoapObject resposta: retorno) {
                    Usuario user = new Usuario();
                    user.setId(Integer.parseInt(resposta.getProperty("id").toString()));
                    user.setNome(resposta.getProperty("nome").toString());
                    user.setIdade(Integer.parseInt(resposta.getProperty("idade").toString()));

                    String foto = resposta.getProperty("foto").toString();

                    byte[] bt = Base64.decode(foto, Base64.DEFAULT);
                    user.setFoto(bt);

                    lista.add(user);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return lista;
    }

    public Usuario buscarUsuarioPorID(int id){

        Usuario user = null;

        SoapObject buscarUsuarioPorId = new SoapObject(NAMESPACE, BUSCAR_POR_ID);
        buscarUsuarioPorId.addProperty("id", id);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(buscarUsuarioPorId);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call("urn:" + BUSCAR_POR_ID, envelope);

            SoapObject resposta = (SoapObject) envelope.getResponse();

            user = new Usuario();

            user.setId(Integer.parseInt(resposta.getProperty("id").toString()));
            user.setNome(resposta.getProperty("nome").toString());
            user.setIdade(Integer.parseInt(resposta.getProperty("idade").toString()));

            String foto = resposta.getProperty("foto").toString();
            byte[] bt = Base64.decode(foto, Base64.DEFAULT);
            user.setFoto(bt);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public boolean atualizarUsuario(Usuario user) {

        SoapObject inserirUsuario = new SoapObject(NAMESPACE, ATUALIZAR);

        SoapObject usuario = new SoapObject(NAMESPACE,"user");

        usuario.addProperty("id", user.getId());
        usuario.addProperty("nome", user.getNome());
        usuario.addProperty("idade", user.getIdade());

        inserirUsuario.addSoapObject(usuario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirUsuario);
        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call("urn:" + ATUALIZAR, envelope);

            SoapPrimitive resposta = (SoapPrimitive) envelope.getResponse();
            return Boolean.parseBoolean(resposta.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
