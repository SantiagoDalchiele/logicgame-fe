package uy.com.uma.logicgame.fe.web.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.ActionsHelper;
import uy.com.uma.logicgame.fe.web.Configuracion;

/**
 * Define la clase base para las acciones que procesa la fachada del juego
 * @see uy.com.uma.logicgame.fe.web.servlets.FachadaJuegoServlet
 *
 * @author Santiago Dalchiele
 */
public abstract class JuegoAbstractAction extends ActionsHelper {
	
	private static final Logger log = LogManager.getLogger(JuegoAbstractAction.class.getName());
	
	
	/** Constantes que definen los requerimientos AJAX */
	protected static final String ID_REQ_LOGIN = "login.do";
	protected static final String ID_REQ_LOGOUT = "logout.do";
	protected static final String ID_REQ_ESTA_LOGEADO = "estaLogeado.do";
	protected static final String ID_REQ_REGISTRO = "registro.do";
	protected static final String ID_REQ_GET_JUEGO = "getJuego.do";
	protected static final String ID_REQ_SET_VALOR = "setValor.do";
	protected static final String ID_REQ_FIN_JUEGO = "finJuego.do";
	protected static final String ID_REQ_GET_CONFIGURACION = "getConfiguracion.do";
	protected static final String ID_REQ_SET_CONFIGURACION = "setConfiguracion.do";
	protected static final String ID_REQ_GET_RANKING = "getRanking.do";
	protected static final String ID_REQ_REINICIAR_JUEGO = "reiniciarJuego.do";
	protected static final String ID_REQ_GRABAR_JUEGO = "grabarJuego.do";
	protected static final String ID_REQ_ENVIAR_TOKEN = "enviarToken.do";
	protected static final String ID_REQ_RECIBIR_TOKEN = "recibirToken.do";
	protected static final String ID_REQ_RESET_CLAVE = "resetClave.do";
	
	
	/** Constantes que definen el nombre de los parámetros */
	public static final String ID_PARAM_ID_USUARIO = "idUsuario";
	public static final String ID_PARAM_CLAVE = "clave";
	public static final String ID_PARAM_ID_CELDA = "idCelda";
	public static final String ID_PARAM_VALOR = "valor";	
	public static final String ID_PARAM_TOKEN = "token";
	protected static final String ID_PARAM_CORREO = "correo";
	protected static final String ID_PARAM_RESULTADO = "resultado";
	protected static final String ID_PARAM_ID_JUEGO = "idJuego";
	protected static final String ID_PARAM_ESTADO = "estado";
	protected static final String ID_PARAM_IDIOMA = "lang";	
	protected static final String ID_OBJ_MATRIZ_JUEGO = "matriz.juego";
	protected static final String ID_USUARIO_TOKEN = "usuario.token";
	
	/** Configuración del sistema */
	protected Configuracion configuracion;
	
	/** Ruta absoluta a la aplicacion */
	protected String appRealPath;
	
	
	
	/** Retorna el nombre de la acción */
	public abstract String getName();
	
	/** Retorna TRUE si utiliza de session el identificador de usuario */
	protected abstract boolean usaIdUsuario();
	
	/** Retorna TRUE si utiliza de session el atributo de la matriz */
	protected abstract boolean usaMatriz();	
	
	/** Resetea la instancia */
	public abstract void reset() throws LogicGameException;
	
	/** Ejecuta la acción */
	public abstract void perform (HttpServletRequest req, PrintWriter out) throws ServletException, IOException;
	
	
	public void setConfiguracion (Configuracion conf) {
		this.configuracion = conf;
	}
	
	
	/** 
	 * Retorna TRUE si en session estan todos los atributos necesarios en session.
	 * De no ser así realiza el logout, remueve el id de usuario de session y retorna un error JSON de usuario no autenticado 
	 */
	public boolean sessionOK (HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		boolean sessionOK = true;
		HttpSession session = req.getSession();
		Object idUsuario = session.getAttribute(ID_PARAM_ID_USUARIO);
		Object matriz = session.getAttribute(ID_OBJ_MATRIZ_JUEGO);
		
		if (usaIdUsuario() && (idUsuario == null))
			sessionOK = false;
		
		if (usaMatriz() && (matriz == null))
			sessionOK = false;
		
		if (!sessionOK) {
			if (idUsuario != null) {				
				try {
					PersistenciaFactory.getInstancia().getManejadorSeguridad().logout(idUsuario.toString());
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
						| PersistenciaException | ConfiguracionException e) {
					log.warn("Error al intentar realizar el logout para el usuario [" + idUsuario + "]", e);
				}
			}
			
			session.removeAttribute(ID_PARAM_ID_USUARIO);
			session.removeAttribute(ID_OBJ_MATRIZ_JUEGO);
			out.write(getErrorJSON(TIPO_ERROR_NO_LOGEADO, 666, "Vuelva a conectarse", ""));
		}
		
		return sessionOK;
	}
	
	
	
	/**
	 * Setea la ruta absoluta de la aplicación
	 */
	public void setAppRealPath(String path) {
		this.appRealPath = path;
	}
}
