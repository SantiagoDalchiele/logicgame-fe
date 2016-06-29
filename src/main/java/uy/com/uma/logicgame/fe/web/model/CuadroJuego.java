package uy.com.uma.logicgame.fe.web.model;

import java.io.Serializable;

import uy.com.uma.comun.util.UtilJSON;
import uy.com.uma.logicgame.api.IValoresCuadroDecision;
import uy.com.uma.logicgame.fe.web.actions.JuegoAbstractAction;

/**
 * Encapsula la info de un cuadro de juego dentro de la matriz
 * 
 * 
 * @author Santiago Dalchiele
 */
class CuadroJuego implements IValoresCuadroDecision, Serializable {

	private static final long serialVersionUID = -9122415353756104051L;

	/** Matriz que encapsula este cuadro */
	private MatrizJuego matriz;
	
	/** Matriz de enteros con los valores correctos de la solución de este juego */
	private short [][] valoresCorrectos;
	
	/** Matriz de enteros con los valores ingresados por el usuario */
	private short [][] valoresIngresados;
	
	/** Se setea a TRUE si todos los valores ingresados de la matriz son distintos de vacio */	
	private boolean completo = false;
	
	/** Se setea a TRUE si todos los valores ingresados son iguales a los correctos */
	private boolean correcto = false;
	
	/** Se setea a TRUE si todos los valores ingresados son iguales a los correctos o si están en blanco */
	private boolean correctoParcial = true;
	
	
	
	/**
	 * Constructor, setea quien lo contiene
	 */
	public CuadroJuego (MatrizJuego parent) {
		this.matriz = parent;
		short cantValores = matriz.getCantValores();
		this.valoresCorrectos = new short[cantValores][cantValores];
		this.valoresIngresados = new short[cantValores][cantValores];
		
		for (short i = 0; i < cantValores; i++)
			for (short j = 0; j < cantValores; j++)
				valoresIngresados[i][j] = VACIA;			
	}
	
	
	
	/**
	 * Metodos de acceso
	 */
	public boolean esCompleto() {
		return completo;
	}
	public boolean esCorrecto() {
		return correcto;
	}
	public boolean esCorrectoParcial() {
		return correctoParcial;
	}

	
	
	/**
	 * Setea un valor de la solución en la matriz
	 */
	public void setValorSolucion (short fila, short col, short valor) {
		valoresCorrectos[fila][col] = valor;
	}
	
	
	
	/**
	 * Setea un valor ingresado y actualiza completo y correcto
	 */
	public void setValorIngresado (short fila, short col, short valor) {
		valoresIngresados[fila][col] = valor;
		
		if (valoresCorrectos[fila][col] != valor)
			correcto = false;
		else {
			correcto = true;
			
			for (int i = 0; i < matriz.getCantValores() && correcto; i++)
				for (int j = 0; j < matriz.getCantValores() && correcto; j++)
					correcto = (valoresIngresados[i][j] == valoresCorrectos[i][j]);
		}
		
		completo = true;
		correctoParcial = true;
		
		for (int i = 0; i < matriz.getCantValores() && completo; i++)
			for (int j = 0; j < matriz.getCantValores() && completo; j++)
				completo = (valoresIngresados[i][j] != VACIA);
		
		for (int i = 0; i < matriz.getCantValores() && correctoParcial; i++)
			for (int j = 0; j < matriz.getCantValores() && correctoParcial; j++)
				correctoParcial = (valoresIngresados[i][j] == VACIA) || (valoresIngresados[i][j] == valoresCorrectos[i][j]);
	}
	
	
	
	/**
	 * Retorna los valores de la matriz en una especie de "formato JSON" para persistir el estado actual del juego
	 */
	public String getEstado(short filaMatriz, short colMatriz) {
		String estado = "";
		
		for (short i = 0; i < matriz.getCantValores(); i++)
			for (short j = 0; j < matriz.getCantValores(); j++)
				if (valoresIngresados[i][j] != VACIA)
					estado += "{" + UtilJSON.getPropJSON(JuegoAbstractAction.ID_PARAM_ID_CELDA) + 
									UtilJSON.getValorJSON(MatrizJuego.getId(filaMatriz, colMatriz, i, j)) + 
									UtilJSON.getPropJSON(JuegoAbstractAction.ID_PARAM_VALOR) + valoresIngresados[i][j] + "},";
		
		return estado;
	}
	
	
	
	/**
	 * Setea todos los valores ingresados a vacío
	 */
	public void vaciar() {
		for (short i = 0; i < matriz.getCantValores(); i++)
			for (short j = 0; j < matriz.getCantValores(); j++)
				valoresIngresados[i][j] = VACIA;
	}
}
