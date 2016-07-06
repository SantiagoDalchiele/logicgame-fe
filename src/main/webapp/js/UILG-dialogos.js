/**
 * Define métodos para el manejo de dialogos en la interface grafica
 * 
 * @see UILG-core.js
 * @see i18next.js, jquery.js
 */
var UILG = (function (my) {

	
	/** Mensajes parametrizados por lenguaje */
	var TXT_BOTON_ACEPTAR;
	var TXT_BOTON_CANCELAR;
	
	/** 
	 * Tipo de error que el usuario necesita logearse al sistema
	 * @see uy.com.uma.logicgame.fe.web.ActionsHelper
	 */ 
	var TIPO_ERROR_NO_LOGEADO = 2666;
	
	
	
	
	/**
	 * Retorna las constantes (internacionalizadas)
	 */
	my.TXT_BOTON_ACEPTAR = function() { return TXT_BOTON_ACEPTAR; }
	my.TXT_BOTON_CANCELAR = function() { return TXT_BOTON_CANCELAR; }
	
	
	
	/**
	 * Actualiza todos los literales dependientes del idioma
	 */
	my.resetLangDialogos = function() {
		i18n.init(UILG.getLenguaje(), function(err, t) {			
			TXT_BOTON_ACEPTAR = t("ui.botonAceptar");
			TXT_BOTON_CANCELAR = t("ui.botonCancelar");
		});
	}
	
	
	
	/**
	 * Oculta los dialogos
	 */
	my.ocultarDialogos = function() {
		$("#panelEspera").hide();
	}
	
	
	
	/**
	 * Muestra un dialogo usando JQuery UI
	 */
	my.dialogo = function (titulo, texto, esModal, okFunc, cancel) {
		var botones = [];
		
		if (okFunc != null) {
			var aceptar = {};
			aceptar.text = TXT_BOTON_ACEPTAR;
			aceptar.click = function() { $(this).dialog("close"); okFunc() };
			botones.push(aceptar);
		}
		
		if (cancel) {
			var cancelar = {};
			cancelar.text = TXT_BOTON_CANCELAR;
			cancelar.click = function() { $(this).dialog("close"); };
			botones.push(cancelar);
		}
		
		my.ocultarDialogos();		
		$("#texto_dialogoBasico").text(texto);		
		$("#dialogoBasico").dialog({
			modal: esModal,
			title: titulo,
			open: function(event, ui) { $(".ui-dialog-titlebar-close", ui.dialog | ui).show(); },
			buttons: botones
		});
	}
	
	
	
	/**
	 * Muestra un mensaje de error
	 */
	my.error = function (tipo, nro, mensaje, detalle) {
		if (tipo == TIPO_ERROR_NO_LOGEADO) {
			$("#lg_user").val("");
			$("#lg_password").val("");
			$("#panel_ui_juego").hide();
			$("#panel_login").show();
			$("#lg_user").focus();
		} else {
			if (nro != 0)
				mensaje = nro + " - " + mensaje;
			
			my.ocultarDialogos();
			$("#error_msg").text(mensaje);
			$("#error_detalle_texto").text(detalle);
			$("#dialogoError").dialog({
				modal: true,
				open: function(event, ui) { $(".ui-dialog-titlebar-close", ui.dialog | ui).show(); },
			});
		}
	}
	
	
	
	/**
	 * Muestra el dialogo de espera
	 */
	my.dialogoEspera = function() {
		$(window).resize(function(){
			$("#panelEspera").center(false, false, true);
		}); 
		
		$("#panelEspera").center(false, false, true);		
		$("#panelEspera").show();
	}
	
	
	
	return my;
}(UILG));
