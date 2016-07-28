/**
 * Intenta implementar el patron observer.  Elimina referencias entre "controllers auxiliares" (ver diagrama)  
 * Main controller de toda la lógica de la interface grafica
 * Inicializa el resto de los controllers auxiliares y ofrece metodos para que no existan referencias entre ellos.
 * 
 * <<Diagrama de subsistemas y dependencias entre modulos>>
 * --------------------------------------------------------------------------------------------------------------------------
 * <<main controller>>
 * 
 *  -------------
 *  | UILG-main |
 *  -------------
 *  
 * --------------------------------------------------------------------------------------------------------------------------
 * <<subsistema de controllers auxiliares>>
 * 
 *  --------------   -----------------   ----------------------   --------------   --------------
 *  | UILG-login |   | UILG-registro |   | UILG-confuguracion |   | UILG-juego |   | UILG-pista |
 *  --------------   -----------------   ----------------------   --------------   --------------
 *  
 * --------------------------------------------------------------------------------------------------------------------------
 * <<subsistema de modulos de utileria>>
 *     
 *     -------------
 *     | UILG-ajax |
 *     -------------
 *           ^
 *           |
 *     -----------------
 *     | UILG-dialogos |
 *     -----------------
 *           ^
 *           |
 *     -------------
 *     | UILG-core |
 *     -------------
 * --------------------------------------------------------------------------------------------------------------------------
 * 
 * @see UILG-configuracion.js, UILG-login.js, UILG-juego.js
 */
var MainLG = (function () {
	
	/** Instancia a retornar */
	var my = {};

	

	/**
	 * Inicializa la interface grafica
	 */
	my.init = function() {
		$("#lg_panel_ui_juego").hide();
		UILG.initConfiguracion();
		UILG.initLogin();
		UILG.initRegistro();	
		
		if (UILG.estaLogeado()) {
			$("#lg_panel_login").hide();
			
			if (UILG.getConfiguracionOK()) {
				UILG.resetLangDialogos();				
				UILG.renderJuego();
				UILG.resetLangJuego();
				UILG.resetLangPistas();
			}
		}
	}
	
	
	
	/** -------------------------------------------------------------------------------------------------------------------------------------------------------
	 * Metodos utilizados para eliminar referencias entre "controllers auxiliares"
	 * --------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	/**
	 * Recarga en todos los controllers todo lo dependiente al idioma
	 * @see UILG-dialogos.js, UILG-juego.js, UILG-pista.js
	 * @see UILG-configuracion.resetConfiguracion
	 */
	my.resetLang = function() {
		UILG.resetLangDialogos();
		UILG.resetLangJuego();
		UILG.resetLangPistas();
	}
	
	
	
	/** 
	 * @see UILG-juego.js
	 * @see UILG-configuracion.resetConfiguracion 
	 */
	my.renderJuego = function() { UILG.renderJuego(); }	
	
	/** 
	 * @see UILG-pista.js 
	 * @see UILG-juego.renderPanelJuego
	 */
	my.initPistas = function (data) { UILG.initPistas(data); }	
	
	/** 
	 * @see UILG-pista.js 
	 * @see UILG-juego.reiniciarJuego
	 */
	my.reiniciarPistas = function() { UILG.reiniciarPistas(); }
	
	
	
	/** -------------------------------------------------------------------------------------------------------------------------------------------------------
	 * Main controller de toda la lógica de la interface grafica
	 * Metodos exportados directamente al código html
	 * --------------------------------------------------------------------------------------------------------------------------------------------------------
	 */	
	/**
	 * Pantalla de login
	 */
	/** @see UILG-login.js */
	my.doLogin = function() { UILG.doLogin(); }
	
	/** @see UILG-login.js */
	my.doLoginWithFacebook = function() { UILG.doLoginWithFacebook(); }
	
	/** @see UILG-login.js */
	my.resetClave = function() { UILG.resetClave(); }
	
	
	
	/**
	 * Pantalla de registro
	 */
	/** @see UILG-registro.js */
	my.registrarse = function() { UILG.registrarse(); }
	
	/** @see UILG-registro.js */
	my.cancelRegistro = function() { UILG.cancelRegistro(); }
	
	/** @see UILG-registro.js */
	my.doRegistro = function() { UILG.doRegistro(); }
	
	
	
	/**
	 * Botones de manejo de pistas
	 */	
	/** @see UILG-pista.js */
	my.pistaAnterior = function() { UILG.pistaAnterior(); }
	
	/** @see UILG-pista.js */
	my.pistaSiguiente = function() { UILG.pistaSiguiente(); }
	
	
	
	/**
	 * Barra de herramientas
	 */
	/** @see UILG-juego.js */
	my.toggleRanking = function() { UILG.toggleRanking(); }
	
	/** @see UILG-juego.js */
	my.deshacer = function() { UILG.deshacer(); }
	
	/** @see UILG-juego.js */
	my.rehacer = function() { UILG.rehacer(); }
	
	/** @see UILG-juego.js */
	my.grabar = function() { UILG.grabar(); }
	
	/** @see UILG-configuracion.js */
	my.configurar = function() { UILG.configurar(); }
	
	/** @see UILG-juego.js */
	my.reiniciar = function() { UILG.reiniciar(); }
	
	/** @see UILG-login.js */
	my.cerrarSession = function() { UILG.cerrarSession(); }
	
	
	
	return my;
} (MainLG));



/**
 * JQuery - Ejecución del código JS luego de cargado el DOM.
 * Inicializa controllers auxiliares
 */
$(document).ready(function () {
	MainLG.init(); 		
});