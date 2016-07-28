/**
 * Define la logica de manejo de registro de usuarios en el sistema
 * 
 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction
 * @see uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad
 * @see UILG-ajax.js, UILG-dialogos.js
 * @see i18next.js, jquery.js
 */
var UILG = (function (my) {

	/** 
	 * Nombre de los requerimientos enviados por AJAX
	 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction 
	 */
	var ID_REQ_REGISTRO = "registro.do";
	
	/** 
	 * Constantes que enumeran el resultado de la función de registro de usuarios
	 * @see uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad 
	 */
	var REGISTRO_OK = 1;
	var REGISTRO_USUARIO_EXISTENTE = 4;
	var REGISTRO_CORREO_EXISTENTE = 5;
	
	/** Mensajes parametrizados por lenguaje */
	var ERROR_USUARIO_REGISTRO_NULO;
	var ERROR_USUARIO_INCORRECTO;
	var ERROR_CORREO_INVALIDO;
	var ERROR_CLAVE_CORTA;
	var ERROR_CLAVE_INCORRECTA;
	var ERROR_CONFIRMACION_CLAVE;
	var ERROR_USUARIO_EXISTENTE;
	var ERROR_CORREO_EXISTENTE;
	
	
	
	/**
	 * Inicializa las etiquetas según el lenguaje del navegador
	 */
	my.initRegistro = function() {
		var navlang = navigator.language || navigator.browserLanguage;
		var lang = navlang.split("-")[0];
		
		i18n.init(lang, function(err, t) {
			$('#lg_reg_user').prop('placeholder', t('ui.registro.usuario'));
			$('#lg_reg_correo').prop('placeholder', t('ui.registro.correo'));
			$('#lg_reg_password').prop('placeholder', t('ui.registro.clave'));
			$('#lg_reg_password2').prop('placeholder', t('ui.registro.confirma'));
			$('#lg_btt_cancel_registro').prop('value', t('ui.botonCancelar'));
			$('#lg_btt_registro').prop('value', t('ui.registro.botonRegistro'));
	
			ERROR_USUARIO_REGISTRO_NULO	= t('ui.registro.errorUsuarioNulo');
			ERROR_USUARIO_INCORRECTO	= t('ui.registro.errorUsuarioIncorrecto');
			ERROR_CORREO_INVALIDO		= t('ui.registro.errorCorreoInvalido');
			ERROR_CLAVE_CORTA			= t('ui.registro.errorClaveCorta');
			ERROR_CLAVE_INCORRECTA		= t('ui.registro.errorIncorrecta');
			ERROR_CONFIRMACION_CLAVE	= t('ui.registro.errorConfirmacionClave');
			ERROR_USUARIO_EXISTENTE		= t('ui.registro.errorUsuarioExistente');
			ERROR_CORREO_EXISTENTE		= t('ui.registro.errorCorreoExistente');
    	});
	}
	
	
	
	/**
	 * Muestra el panel de registro de usuarios
	 */
	my.registrarse = function() {
		$("#lg_reg_user").val("");
		$("#lg_reg_correo").val("");
		$("#lg_reg_password").val("");
		$("#lg_reg_password2").val("");
		$("#lg_panel_login").hide();
		$("#lg_panel_registro").show();
	}
	
	
	
	/**
	 * Realiza el registro de usuario
	 */
	my.doRegistro = function() {
		var user = $("#lg_reg_user").val();
		var correo = $("#lg_reg_correo").val();
		var mail = ("" + correo);
		var clave = $("#lg_reg_password").val();
		var confClave = $("#lg_reg_password2").val();
		
		if ((user == null) || (user.trim() == ""))
			UILG.error (1, 190, ERROR_USUARIO_REGISTRO_NULO, '');
		else if ((correo == null) || (correo.trim() == "") || (mail.indexOf('@') == -1))
			UILG.error (1, 192, ERROR_CORREO_INVALIDO, '');
		else {
			if (!UILG.PATRON_IDS().test(user))
				UILG.error (1, 93, ERROR_USUARIO_INCORRECTO);
			else if (!UILG.PATRON_EMAILS().test(correo))
				UILG.error (1, 206, ERROR_CORREO_INVALIDO);
			else if ((clave == null) || (clave.length < 5))
				UILG.error (1, 194, ERROR_CLAVE_CORTA, '');
			else if (clave != confClave)
				UILG.error (1, 196, ERROR_CONFIRMACION_CLAVE, '');
			else if (!UILG.PATRON_CLAVES().test(clave))
				UILG.error (1, 101, ERROR_CLAVE_INCORRECTA, '');
			else {
				var navLang = (navigator.language) ? navigator.language : navigator.userLanguage;				
				var lang = navLang.split("-")[0];
				var parameters = { lang: lang, idUsuario: user, correo: correo, clave: clave };
				UILG.ajaxPost (ID_REQ_REGISTRO, parameters, false, procesarRegistro);
			}
		}
	}
	
	/**
	 * Cancela el registro de usuarios y muestra la pantalla de login
	 */
	my.cancelRegistro = function() {
		$("#lg_panel_registro").hide();
		$("#lg_panel_login").show();
		$("#lg_user").focus();
	}
	
	
	
	/**
	 * Procesa el resultado del registro del usuario
	 */
	function procesarRegistro(data) {
		if (data.resultado == REGISTRO_USUARIO_EXISTENTE) 
			UILG.error(1, 367, ERROR_USUARIO_EXISTENTE, '');
		else if (data.resultado == REGISTRO_CORREO_EXISTENTE)
			UILG.error(1, 367, ERROR_CORREO_EXISTENTE, '');
		else {
			$("#lg_user").val(data.idUsuario);
			$("#lg_panel_registro").hide();
			$("#lg_panel_login").show();
			$("#lg_password").focus();
		}
	}
	
	
	
	/** Definición de métodos públicos */
	return my;
}(UILG));
