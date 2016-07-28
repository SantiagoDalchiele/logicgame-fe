/**
 * Define la logica de manejo de login en el sistema
 * 
 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction
 * @see uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad
 * @see UILG-ajax.js, UILG-configuracion.js, UILG-dialogos.js, UILG-main.js
 * @see i18next.js, jquery.js
 */
var UILG = (function (my) {
	
	/** 
	 * Nombre de los requerimientos enviados por AJAX
	 * @see uy.com.uma.logicgame.be.web.actions.JuegoAbstractAction 
	 */
	var ID_REQ_LOGIN = "login.do";
	var ID_REQ_LOGOUT = "logout.do";
	var ID_REQ_ESTA_LOGEADO = "estaLogeado.do";
	
	/** Mensajes parametrizados por lenguaje */
	var ERROR_USUARIO_NULO;
	var ERROR_USUARIO_INCORRECTO;
	var ERROR_CLAVE_NULA;
	var ERROR_USUARIO_INEXISTENTE;
	var ERROR_CLAVE_INCORRECTA;
	
	/** 
	 * Valores de resultado del login
	 * @see uy.com.uma.logicgame.api.persistencia.IManejadorSeguridad 
	 */
	var LOGIN_EXITOSO = 1;
	var LOGIN_USUARIO_INEXISTENTE = 2;
	var LOGIN_CLAVE_INCORRECTA = 3;
	
	
	/** Indica si el usuario ya se encuentra logeado o no en el sistema */
	var estaLogeado = false;
	
	
	
	/**
	 * Inicializa las etiquetas según el lenguaje del navegador
	 * Inicializa el estado de la variable estaLogeado
	 */
	my.initLogin = function() {
		var navlang = navigator.language || navigator.browserLanguage;
		var lang = navlang.split("-")[0];
		
		i18n.init(lang, function(err, t) {
			$('#lg_user').prop('placeholder', t('ui.login.usuario'));
			$('#lg_password').prop('placeholder', t('ui.login.clave'));
			$('#lg_btt_login').prop('value', t('ui.login.boton'));
			$('#lg_btt_facebook').prop('value', t('ui.login.botonFacebook'));
			$('#lg_btt_resetClave').prop('value', t('ui.login.botonResetClave'));
			$('#lg_btt_registrarse').prop('value', t('ui.login.botonRegistro'));
	
			ERROR_USUARIO_NULO			= t('ui.login.errorUsuarioNulo');
			ERROR_USUARIO_INCORRECTO	= t('ui.login.errorUsuarioIncorrecto');
			ERROR_CLAVE_NULA			= t('ui.login.errorClaveNula');
			ERROR_USUARIO_INEXISTENTE	= t('ui.login.errorUsuarioInexistente');
			ERROR_CLAVE_INCORRECTA		= t('ui.login.errorClaveIncorrecta');
    	});
		
		$("#lg_user").val("");
		$("#lg_password").val("");
		UILG.ajaxPost (ID_REQ_ESTA_LOGEADO, {}, false, procesarEstaLogeado);
	}
	
	
	
	/**
	 * Retorna el estado de la variable estaLogeado true/false
	 */
	my.estaLogeado = function() {
		return estaLogeado;
	}
	
	
	
	/**
	 * Controla que se ingresen usuario y clave y realiza el login.  Si el login es exitoso despliega el juego
	 */
	my.doLogin = function() {
		var user = $("#lg_user").val();
		var pass = $("#lg_password").val();
		
		if ((user == null) || (user == ''))
			UILG.error (1, 27, ERROR_USUARIO_NULO, '');
		else if ((pass == null) || (pass == ''))
			UILG.error (1, 27, ERROR_CLAVE_NULA, '');
		else if ((!UILG.PATRON_IDS().test(user)) && (!UILG.PATRON_EMAILS().test(user)))
			UILG.error (1, 93, ERROR_USUARIO_INCORRECTO);
		else if (!UILG.PATRON_CLAVES().test(pass))
			UILG.error (1, 101, ERROR_CLAVE_INCORRECTA, '');
		else {
			var parameters = { idUsuario: user, clave: pass };
			UILG.ajaxPost (ID_REQ_LOGIN, parameters, false, procesarLogin);
		}
	}
	
	
	
	/**
	 * TODO implementar
	 */
	my.doLoginWithFacebook = function() {
		
	}
	
	
	
	/**
	 * TODO implementar
	 */
	my.resetClave = function() {
		
	}
	
	
	
	/**
	 * Cierra sesión
	 */
	my.cerrarSession = function() {
		UILG.ocultarDialogos();
		UILG.ajaxPost (ID_REQ_LOGOUT, {}, false, null);
		$("#lg_user").val("");
		$("#lg_password").val("");
		$("#lg_panel_ui_juego").hide();
		$("#lg_panel_login").show();
		$("#lg_user").focus();
	}
	
	
	
	/**
	 * Setea el estado de la variable estaLogeado
	 */
	function procesarEstaLogeado(data) {
		estaLogeado = (data.resultado == 1);
	}
	
	
	
	/**
	 * Procesa el resultado del login del usuario
	 */
	function procesarLogin(data) {
		if (data.resultado == LOGIN_EXITOSO) {
			$("#lg_panel_login").hide();
			UILG.initConfiguracion();
			
			if (UILG.getConfiguracionOK()) {
				UILG.ocultarDialogos();
				UILG.dialogoEspera();
				MainLG.resetLang();
				MainLG.renderJuego();
			}
		} else if (data.resultado == LOGIN_USUARIO_INEXISTENTE)
			UILG.error (1, 33, ERROR_USUARIO_INEXISTENTE, '');
		else if (data.resultado == LOGIN_CLAVE_INCORRECTA)	
			UILG.error (1, 34, ERROR_CLAVE_INCORRECTA, '');
	}
	
	
	
	return my;
}(UILG));