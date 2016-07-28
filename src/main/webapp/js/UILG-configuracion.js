/**
 * Define los métodos de la configuracion del sistema y su gestión
 * 
 * @see UILG-core.js, UILG-ajax.js, UILG-main.js
 * @see jquery.js
 */
var UILG = (function (my) {
	
	/** 
	 * Nombre de los requerimientos enviados por AJAX
	 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction 
	 */
	var ID_REQ_GET_CONFIGURACION = "getConfiguracion.do";
	var ID_REQ_SET_CONFIGURACION = "setConfiguracion.do";
	
	/** Cargo OK la configuracion? */
	var confOK = false;
	
	
	
	/**
	 * Obtiene la configuracion (el lenguaje) del usuario
	 */
	my.initConfiguracion = function() {		
		UILG.ajaxPost (ID_REQ_GET_CONFIGURACION, {}, false, setConfiguracionAjax);
	}
	
	
	
	/**
	 * Retorna si cargo la configuracion de forma exitosa
	 */
	my.getConfiguracionOK = function() {
		return confOK;
	}
	
	
	/**
	 * Ventana de configuración
	 */
	my.configurar = function() {
		UILG.ocultarDialogos();
		dialogoConfiguracion();
	}
	
	
	
	/**
	 * Muestra el dialogo de configuracion
	 */
	function dialogoConfiguracion() {		
		initDialogo();
		
		$("#lg_dialogoConfiguracion").dialog({
			modal: true,
			resizable: false,
			closeOnEscape: false,
			open: function(event, ui) { $(".lg_ui-dialog-titlebar-close", ui.dialog | ui).hide(); },
			buttons: [{
					text: UILG.TXT_BOTON_ACEPTAR(),
					click: function() {
						$(this).dialog("close");
						setConfiguracion();						
				}}, {
					text: UILG.TXT_BOTON_CANCELAR(),
					click: function() {				
						$(this).dialog("close");
					}
				}]
		});
	}

	
	
	/**
	 * Setea los valores iniciales del dialogo de configuración
	 */	
	function initDialogo() {
		$("input[name='lg_idiomas']").each(function() {
			if (UILG.getLang() == $(this).prop('id'))
				$(this).prop("checked", true);
		});
	}
	
	
	
	/**
	 * Setea la configuración ingresada en la UI
	 */
	function setConfiguracion() {
		var newLang = $("input[name='lg_idiomas']:checked").prop('id');
		
		if (newLang != UILG.getLang()) {
			UILG.setLang (newLang);
			var parameters = { lang: newLang };
			UILG.ajaxPost (ID_REQ_SET_CONFIGURACION, parameters, false, resetConfiguracion);
		}		
	}
	

	
	/**
	 * Setea la configuración obtenida del servidor web
	 * Construye el dialogo de configuración
	 */
	function setConfiguracionAjax (data) {
		UILG.setLang (data.lang);
		var tblIdiomas = document.getElementById("lg_conf_idiomas"); 
		$("#lg_conf_idiomas").empty();
		
		for (i in data.idiomas) {
			var idioma = data.idiomas[i];
			var row = tblIdiomas.insertRow(-1);
			var celda = row.insertCell(-1);
			
			var input = document.createElement("INPUT");
			input.type = "radio";
			input.name = "lg_idiomas";
			input.id = idioma.id;
			input.checked = (idioma.id == UILG.getLang());
			
			var img = document.createElement("IMG");
			img.src = idioma.icono;
			var txt = document.createElement("SPAN");
			txt.innerHTML = ' ' + idioma.nombre;
			
			var etiq = document.createElement("LABEL");			
			etiq.htmlFor = idioma.id;			
			etiq.appendChild(img);
			etiq.appendChild(txt);			
			
			celda.appendChild(input);
			celda.appendChild(etiq);			
		}
		
		confOK = true;
	}
	
	
	
	/**
	 * Vuelve a mostrar el juego en otro idioma
	 */
	function resetConfiguracion() {
		UILG.ocultarDialogos();
		UILG.dialogoEspera();
		MainLG.resetLang();
		MainLG.renderJuego();
	}
	
	
	
	return my;
} (UILG));
