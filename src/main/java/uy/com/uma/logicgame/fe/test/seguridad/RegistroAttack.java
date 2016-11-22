package uy.com.uma.logicgame.fe.test.seguridad;

import java.util.Map;

/**
 *
 * @author Santiago Dalchiele
 */
public class RegistroAttack extends LoginAttack {
	
	private String idioma;
	private String correo;
	
	

	public RegistroAttack(String url, String charset) {
		super(url, charset);
	}

	public RegistroAttack(String url) {
		super(url);
	}

	public RegistroAttack(String url, String charset, String usuario, String clave) {
		super(url, charset, usuario, clave);
	}

	
	
	public String getIdioma() {
		return idioma;
	}
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	
	
	@Override
	public void setParameters() {
		Map<String, Object> params = getParametros();
		params.clear();
		params.put("lang", idioma);
		params.put("idUsuario", usuario);
		params.put("correo", correo);
		params.put("clave", clave);
	}	
}
