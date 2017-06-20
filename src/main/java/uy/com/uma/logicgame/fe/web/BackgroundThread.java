package uy.com.uma.logicgame.fe.web;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uy.com.uma.logicgame.api.LogicGameException;
import uy.com.uma.logicgame.api.conf.ConfiguracionException;
import uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad;
import uy.com.uma.logicgame.api.persistencia.PersistenciaException;
import uy.com.uma.logicgame.api.persistencia.PersistenciaFactory;
import uy.com.uma.logicgame.fe.web.model.MatrizJuego;

/**
 * Ejecuta tareas de mantenimiento en back-ground.
 * Persiste cada un intervalo regular el estado del juego del usuario.
 *
 * @author Santiago Dalchiele
 */
public class BackgroundThread extends Thread implements Serializable {
	
	private static final long serialVersionUID = -414095301465335195L;

	private static final Logger log = LogManager.getLogger(BackgroundThread.class.getName());
	

	/** Fachada de servicios de persistencia */
	private IManejadorSeguridad ms;
	
	/** Identificador del usuario */
	private String idUsuario;
	
	/** Timeout de espera */
	private long timeout;
	
	/** Factor que multiplica el timeout en la medida que el estado no cambie */
	private int factorTimeout = 1;
	
	/** Matriz del juego */
	private MatrizJuego matriz;
	
	/** Ultimo estado persistido */
	private String ultimoEstado = "";
	
	
	
	/**
	 * Constructor por defecto, toma el timeout, setea la prioridad del Thread a mínima
	 */
	public BackgroundThread (String idUsuario, long timeout) throws LogicGameException {
		try {
			ms = PersistenciaFactory.getInstancia().getManejadorSeguridad();
			log.debug("Creando un nuevo BackgroundThread " + getId() + " - " + hashCode());
			this.idUsuario = idUsuario;
			this.timeout = timeout;
			super.setPriority(MIN_PRIORITY);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al obtener conexion a la base de datos", e);		
		}
	}


	
	/**
	 * Resetea la conexión a la base
	 */
	public synchronized void reset() throws LogicGameException {
		try {
			ms = PersistenciaFactory.getInstancia().getManejadorSeguridad();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | ConfiguracionException e) {
			throw new LogicGameException("Error al resetear conexion a la base de datos", e);
		}
	}
	
	
	
	/**
	 * Retorna el identificador del usuario
	 */
	public synchronized String getIdUsuario() {
		return this.idUsuario;			
	}
	
	
	
	/**
	 * Setea la matriz del juego
	 */
	public synchronized void setMatriz (MatrizJuego matriz) {
		this.matriz = matriz;
	}



	/**
	 * Ejecuta las tareas de mantenimiento.  Persiste el estado del juego.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				long timeToSleep = 0;
				
				synchronized (this) {
					timeToSleep = factorTimeout * timeout;
					log.debug("Ejecutando el Thread de tareas en background para el usuario [" + idUsuario +  "] - esperando " + timeToSleep + " milisegundos");
				}				
				
				Thread.sleep(timeToSleep);
				
				synchronized (this) {
					try {
						if (!ms.estaLogeado(idUsuario)) {
							this.idUsuario = null;
							this.matriz = null;
							this.interrupt();
						}
					} catch (PersistenciaException pe) {
						log.warn("Error al obtener si esta logeado o no el usuario [" + idUsuario + "]", pe);
					}
					
					if (matriz != null) {
						try {
							if (idUsuario != null) {
								String estado = matriz.getEstado();
								
								if (! ultimoEstado.equals(estado)) {
									log.debug("Ejecutando el Thread de tareas en background - Usuario: " + idUsuario + ", persistiendo el estado del juego [" + estado + "]");
									ultimoEstado = estado;
									factorTimeout = 1;
									ms.setEstado(idUsuario, estado);
								} else
									factorTimeout++;
							}
						} catch (PersistenciaException pe) {
							log.warn("Error al persistir el estado del juego", pe);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			log.debug("Thread de tareas en background - hilo interrumpido [" + getIdUsuario() + "]");
		}
	}	
}
