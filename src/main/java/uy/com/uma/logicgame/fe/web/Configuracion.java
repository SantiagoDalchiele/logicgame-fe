package uy.com.uma.logicgame.fe.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static final String PROP_ADM_DB_USER				= "adm.db.user";
	private static final String PROP_ADM_DB_PASSWORD			= "adm.db.password";
	private static final String PROP_ADM_PASSWORD				= "adm.password";
	private static final String PROP_WEB_DB_USER				= "web.db.user";
	private static final String PROP_WEB_DB_PASSOWORD			= "web.db.password";
	private static final String PROP_MAX_USERS_RANKING			= "max.users.ranking";
	private static final String PROP_BACKGROUND_THREAD_TIMEOUT	= "background.thread.timeout";
	
	
	/** Unica instancia de la clase */
	private static Configuracion instancia = null;
	
	/** Atributos de la configuracion */
	private String admDBUser;
	private String admDBPassword;
	private String admPassword;
	private String webDBUser;
	private String webDBPassword;
	private int maxUsersRanking;
	private long backgroundThreadTimeout;
	
	
	
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
		final EncriptadorString crip = new EncriptadorString();
		
		if (p.containsKey(PROP_ADM_DB_USER)) 
			admDBUser = crip.desencriptar(p.getProperty(PROP_ADM_DB_USER));
		
		if (p.containsKey(PROP_ADM_DB_PASSWORD))
			admDBPassword = crip.desencriptar(p.getProperty(PROP_ADM_DB_PASSWORD));
		
		if (p.containsKey(PROP_WEB_DB_USER))
			webDBUser = crip.desencriptar(p.getProperty(PROP_WEB_DB_USER));
		
		if (p.containsKey(PROP_WEB_DB_PASSOWORD))
			webDBPassword = crip.desencriptar(p.getProperty(PROP_WEB_DB_PASSOWORD));
		
		admPassword = p.getProperty(PROP_ADM_PASSWORD);
		maxUsersRanking = Integer.parseInt(p.getProperty(PROP_MAX_USERS_RANKING));
		backgroundThreadTimeout = Long.parseLong(p.getProperty(PROP_BACKGROUND_THREAD_TIMEOUT));		
	}

	
	
	/**
	 * Metodos de acceso
	 */
	public String getAdmDBUser() {
		return admDBUser;
	}
	public String getAdmDBPassword() {
		return admDBPassword;
	}
	public String getAdmPassword() {
		return admPassword;
	}
	public String getWebDBUser() {
		return webDBUser;
	}
	public String getWebDBPassword() {
		return webDBPassword;
	}
	public int getMaxUsersRanking() {
		return maxUsersRanking;
	}
	public long getBackgroundThreadTimeout() {
		return backgroundThreadTimeout;
	}
}
