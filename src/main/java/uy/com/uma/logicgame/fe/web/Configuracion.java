package uy.com.uma.logicgame.fe.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.mail.MailParams;
import uy.com.uma.comun.util.EncriptadorString;
import uy.com.uma.comun.util.UtilFormato;
import uy.com.uma.comun.util.UtilIO;
import uy.com.uma.logicgame.api.conf.IConfiguracionConstantes;

/**
 * Implementa singleton con la configuración de la aplicación web.
 * Busca el archivo: WEB-INF/classes/logicgame.properties
 *
 * @author Santiago Dalchiele
 */
public class Configuracion implements IConfiguracionConstantes {
	
	private static final Logger log = LogManager.getLogger(Configuracion.class.getName());

	/** Constantes con los nombres de las propiedades */
	private static final String PROP_ADM_PASSWORD				= "adm.password";
	private static final String PROP_MAX_USERS_RANKING			= "max.users.ranking";
	private static final String PROP_BACKGROUND_THREAD_TIMEOUT	= "background.thread.timeout";
	private static final String PROP_HOST_CORREO				= "correo.host";
	private static final String PROP_PUERTO_CORREO				= "correo.puerto";
	private static final String PROP_USUARIO_CORREO				= "correo.usuario";
	private static final String PROP_CLAVE_CORREO				= "correo.clave";
	private static final String PROP_DIRECCION_CORREO			= "correo.direccion";
	private static final String PROP_TIMEOUT_CORREO				= "correo.timeout";
	private static final String PROP_ASUNTO_CORREO				= "correo.asunto";
	private static final String PROP_CONTENIDO_CORREO			= "correo.contenido";
	
	
	
	/** Unica instancia de la clase */
	private static Configuracion instancia = null;
	
	/** Atributos de la configuracion */
	private String admPassword;
	private int maxUsersRanking;
	private long backgroundThreadTimeout;
	private MailParams parametrosCorreo;
	private String plantillaContenidoCorreo;
	
	
	
	/**
	 * Retorna la única instancia de la clase
	 */
	public static Configuracion getInstancia() throws IOException, KeyException {
		if (instancia == null)
			instancia = new Configuracion();
		
		return instancia;
	}
	
	private Configuracion() throws IOException, KeyException {
		loadConfiguracion();
	} 
	
	
	
	/**
	 * Carga en variables los datos de la configuracion
	 */
	private void loadConfiguracion() throws FileNotFoundException, IOException, KeyException {
		String path = System.getenv(VAR_ENV_LG_HOME);

		if (UtilFormato.esNulo(path)) {
			System.out.println("Debe parametrizar la ruta de la configuración en la variable de environment: " + VAR_ENV_LG_HOME);
			path = ".";
		}
		
		System.out.println("La variable de la ruta de configuracion del environment es: [" + VAR_ENV_LG_HOME + "] y su contenido es [" + path + "]");
		
		if (!path.endsWith(File.separator))
			path += File.separator;
		
		path += LOGICGAME_PROPS_FILE_NAME;		
		log.info("Obteniendo la configuracion del sistema de [" + path + "]");
		Properties p = UtilIO.getProperties(path);
		admPassword = p.getProperty(PROP_ADM_PASSWORD);
		maxUsersRanking = Integer.parseInt(p.getProperty(PROP_MAX_USERS_RANKING));
		backgroundThreadTimeout = Long.parseLong(p.getProperty(PROP_BACKGROUND_THREAD_TIMEOUT));
		EncriptadorString encrip = new EncriptadorString();
		parametrosCorreo = new MailParams();
		parametrosCorreo.setHostCorreo(p.getProperty(PROP_HOST_CORREO));
		parametrosCorreo.setPuertoCorreo(Integer.parseInt(p.getProperty(PROP_PUERTO_CORREO)));
		parametrosCorreo.setUsuarioCorreo(p.getProperty(PROP_USUARIO_CORREO));
		parametrosCorreo.setClaveCorreo(encrip.desencriptar(p.getProperty(PROP_CLAVE_CORREO)));
		parametrosCorreo.setTimeout(Integer.parseInt(p.getProperty(PROP_TIMEOUT_CORREO)));
		parametrosCorreo.setFrom(p.getProperty(PROP_DIRECCION_CORREO));
		parametrosCorreo.setAsunto(p.getProperty(PROP_ASUNTO_CORREO));
		plantillaContenidoCorreo = p.getProperty(PROP_CONTENIDO_CORREO);
	}

	
	
	/**
	 * Metodos de acceso
	 */
	public String getAdmPassword() {
		return admPassword;
	}
	public int getMaxUsersRanking() {
		return maxUsersRanking;
	}
	public long getBackgroundThreadTimeout() {
		return backgroundThreadTimeout;
	}
	public MailParams getParametrosCorreo() {
		return parametrosCorreo;
	}
	public String getPlantillaContenidoCorreo() {
		return plantillaContenidoCorreo;
	}
}
