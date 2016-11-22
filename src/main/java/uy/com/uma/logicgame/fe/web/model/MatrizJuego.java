package uy.com.uma.logicgame.fe.web.model;

import java.io.Serializable;

import uy.com.uma.comun.util.UtilString;
import uy.com.uma.logicgame.api.bean.ParametrosJuego;

/**
 * Modelo de objeto que representa la matriz de juego del lado del servidor web
 *
 * @see uy.com.uma.logicgame.resolucion.RazonadorXDeducciones.getSolucion()
 * @author Santiago Dalchiele
 */
public class MatrizJuego implements Serializable {

	private static final long serialVersionUID = 7722562938040660443L;
		
	/** Dimensiones del juego */
	private short cantDimensiones;
	private short cantValores;
	
	/** TRUE si los cuadros de la primer fila están totalmente completos (no necesariamente correctos) */
	private boolean completo = false;
	
	/** 
	 * TRUE si todos los cuadros de la primer fila son correctos y están completos, y en los restantes cuadros
	 * no se ingresan valores erroneos (están en blanco o el valor ingresado es igual a la solución) 
	 */
	private boolean correcto = false;
	
	/** Matriz de (cantDimensiones-1 x cantDimensiones-1) con cada cuadro de juego */
	private CuadroJuego [][] matriz;
	
	/** Maxima cantidad de intentos posibles de solicitar si está bien o mal el juego (seguridad, abuso del servicio) */
	private int maxIntentos;
	
	
	
	/**
	 * Construye la matriz y setea la solución
	 */
	public MatrizJuego (ParametrosJuego params) {
		cantDimensiones = params.getCantDimensiones();
		cantValores = params.getCantValores();
		maxIntentos = params.getMaxIntentos();
		matriz = new CuadroJuego [cantDimensiones-1][cantDimensiones-1];
		
		for (int i = 0; i < (cantDimensiones - 1); i++)
			for (int j = 0; j < (cantDimensiones - (i+1)); j++)
				matriz[i][j] = new CuadroJuego(this);
		
		String [] valores = params.getSolucion().split(";");
		
		for (String val : valores) {
			String [] idVal = val.split(",");
			short valor = Short.parseShort(idVal[1]);
			setSolucion (idVal[0], valor);
		}
	}
	
	
	
	/**
	 * Retorna el identificador de la celda de la forma: filaMatriz.colMatriz.filaCuadro.colCuadro
	 */
	public static String getId (short filaMatriz, short colMatriz, short filaCuadro, short colCuadro) {
		return filaMatriz + "." + colMatriz + "." + filaCuadro + "." + colCuadro; 
	}
	

	
	/**
	 * Metodos de acceso
	 */
	public short getCantDimensiones() {
		return this.cantDimensiones;
	}
	public short getCantValores() {
		return this.cantValores;
	}	
	public int getMaxIntentos() {
		return this.maxIntentos;
	}
	public boolean estaCompleto() {
		return completo;
	}
	public boolean estaCorrecto() {
		return correcto;
	}



	/**
	 * Setea el valor en la matriz de juego y actualiza banderas booleanas
	 */
	public void setValorIngresado (String id, short valor) {
		PosicionMatrizJuego pos = getPosicion(id);
		matriz[pos.filaMatriz][pos.colMatriz].setValorIngresado(pos.filaCuadro, pos.colCuadro, valor);
		correcto = true;
		completo = true;
		
		for (short j = 0; (j < (cantDimensiones - 1)) && completo; j++)
			if (!matriz[0][j].esCompleto())
				completo = false;
			else if (!matriz[0][j].esCorrecto())
				correcto = false;
		
		if (!completo)
			correcto = false;
		else
			for (short i = 1; (i < (cantDimensiones - 1)) && correcto; i++)
				for (short j = 0; (j < (cantDimensiones - (i+1))) && correcto; j++)
					if (!matriz[i][j].esCorrectoParcial())
						correcto = false;
	}
	
	
	
	/**
	 * Retorna los valores de la matriz en una especie de "formato JSON" para persistir el estado actual del juego
	 */
	public String getEstado() {
		StringBuffer estado = new StringBuffer();
		
		for (short i = 0; i < (cantDimensiones - 1); i++)
			for (short j = 0; j < (cantDimensiones - (i+1)); j++)
				estado.append(matriz[i][j].getEstado(i, j));
		
		return UtilString.quitarUltimosCaracteres(estado.toString(), 1);
	}
	
	
	
	/**
	 * Setea todos los valores ingresados a vacio
	 */
	public void vaciar() {
		for (short i = 0; i < (cantDimensiones - 1); i++)
			for (short j = 0; j < (cantDimensiones - (i+1)); j++)
				matriz[i][j].vaciar();
	}
	

	
	/**
	 * Dado un identificar de la forma fila_matriz.col_matriz.fila_cuadro.columna_cuadro
	 * retorna estos datos en un objeto del tipo PosicionMatrizJuego
	 */
	private PosicionMatrizJuego getPosicion (String id) {
		PosicionMatrizJuego pos = new PosicionMatrizJuego();
		
		if ((cantDimensiones <= 10) && (cantValores < 10)) {			
			pos.filaMatriz = (short) (id.charAt(0)-48);
			pos.colMatriz = (short) (id.charAt(2)-48);
			pos.filaCuadro = (short) (id.charAt(4)-48);
			pos.colCuadro = (short) (id.charAt(6)-48);						
		} else {
			String [] numbers = id.split("\\.");
			pos.filaMatriz = Short.parseShort(numbers[0]);
			pos.colMatriz = Short.parseShort(numbers[1]);
			pos.filaCuadro = Short.parseShort(numbers[2]);
			pos.colCuadro = Short.parseShort(numbers[3]);
		}
		
		return pos;
	}

	
	
	/**
	 * Setea los valores de la solución
	 */
	private void setSolucion (String id, short valor) {
		PosicionMatrizJuego pos = getPosicion(id);
		matriz[pos.filaMatriz][pos.colMatriz].setValorSolucion(pos.filaCuadro, pos.colCuadro, valor);
	}
	
	
	
	/**
	 * Encapsula la info de una posición en la matriz de juego, fila y columna en la matriz
	 * y fila y columna dentro de un cuadro
	 *
	 * @author Santiago Dalchiele
	 */
	class PosicionMatrizJuego {
		
		public short filaMatriz;
		public short colMatriz;
		public short filaCuadro;
		public short colCuadro;
		
		
		
		public short getFilaMatriz() {
			return filaMatriz;
		}
		public void setFilaMatriz(short filaMatriz) {
			this.filaMatriz = filaMatriz;
		}
		public short getColMatriz() {
			return colMatriz;
		}
		public void setColMatriz(short colMatriz) {
			this.colMatriz = colMatriz;
		}
		public short getFilaCuadro() {
			return filaCuadro;
		}
		public void setFilaCuadro(short filaCuadro) {
			this.filaCuadro = filaCuadro;
		}
		public short getColCuadro() {
			return colCuadro;
		}
		public void setColCuadro(short colCuadro) {
			this.colCuadro = colCuadro;
		}
	}
}
