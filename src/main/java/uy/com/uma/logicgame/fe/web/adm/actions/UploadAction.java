package uy.com.uma.logicgame.fe.web.adm.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.comun.util.UtilIO;
import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.bean.IJSONObject;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.ICargadorRecursos;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;

/**
 * Acción que implementa la carga de datos en el sistema
 * @see uy.com.uma.logicgame.persistencia.CargadorRecursos;
 *
 * @author Santiago Dalchiele
 */
public class UploadAction extends GetDatosAbstractAction {

	private static final Logger log = LogManager.getLogger(UploadAction.class.getName());
	
	/** Constantes para los parametros de la página */
	private static final String PARAM_FILE = "file";
	
	/** Identificador de la coleccion a retornar */ 
	private static final String TAG_COLECCION = "archivos";
	
	
	/** Atributos */
	private Part filePart;
	
	
	@Override
	public String getName() {
		return "";
	}

	@Override
	protected String getTagColeccion() {
		return TAG_COLECCION;
	}
	
	

	/**
	 * Obtiene del request los parametros del archivo y el idioma
	 */
	@Override
	public void perform(HttpServletRequest req, PrintWriter out) throws ServletException, IOException {
		this.filePart = req.getPart(PARAM_FILE);
		super.perform(req, out);
	}

	
	
	/**
	 * Carga el archivo .zip enviado y retorna los archivos procesados
	 */
	@Override
	protected Collection<IJSONObject> getDatos() throws PersistenciaException, InstantiationException,
												IllegalAccessException, ClassNotFoundException, ConfiguracionException {
	    final String fileName = filePart.getSubmittedFileName();
	    final String extArch = UtilIO.extFile(fileName);
	    log.debug("Extension=" + extArch + ", Archivo=" + fileName);	    

    	if (extArch.equalsIgnoreCase("zip")) {
	        ICargadorRecursos loader = PersistenciaFactory.getInstancia().getCargadorRecursos();
	        
	        try {
				loader.setInputStream(filePart.getInputStream());				
		        loader.cargar();
		        Collection<IJSONObject> col = new ArrayList<IJSONObject>();
		        
		        for (String arch : loader.getArchivosProcesados())
		        	col.add(new Id(arch));     
		        
		        return col;
			} catch (IOException | LogicGameException e) {
				throw new PersistenciaException(e);
			}	        
    	} else
    		throw new PersistenciaException("La extensión del archivo debe ser .zip");
	}	
}
