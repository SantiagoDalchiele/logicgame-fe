package uy.com.uma.logicgame.fe.test.seguridad;

import java.util.Map;


/**
 * Encapsula la lógica de un ataque al login del juego LogicGame
 *
 * @author Santiago Dalchiele
 */
class LoginAttack extends AbstractAttack {
	
	protected String usuario;
	protected String clave;	
	
	
	
	public LoginAttack(String url, String charset) {
		super(url, charset);
	}

	public LoginAttack(String url) {
		super(url);
	}

	public LoginAttack(String url, String charset, String usuario, String clave) {
		this(url, charset);
		this.usuario = usuario;
		this.clave = clave;
	}



	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}	
	
	
	
	@Override
	public void setParameters() {
		Map<String, Object> params = getParametros();
		params.clear();
		params.put("idUsuario", usuario);
		params.put("clave", clave);		
	}
}
