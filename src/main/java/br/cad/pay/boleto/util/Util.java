package br.cad.pay.boleto.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings({ "unchecked"})
public class Util {
	private static final Log log = LogFactory.getLog(Util.class);
	
	public Util() {

	}

	// Passado uma string e o tamanho para completar com zeros a esquerda
	public static String zeroEsquerda(String s1, int tamString) {
		while (s1.length() < tamString) {
			s1 = "0" + s1;
		}
		return s1;
	}
	
	public static String removeZeroLeft(String txt) {
		if(txt!=null) {
			return txt.replaceFirst("^0+(?!$)", "");
		}else{
			return null;
		}
	}
	
	public static String removerEspacos(String frase) {
		int espacos = 0, c = 0;
		boolean inicio = true;
		int tamanho = frase.length() - 1;
		char carac;
		String fraseSemEspacos = "";

		// repetir enquanto o contador for menor que o tamanho da frase
		while (c <= tamanho) {
			// pega o caracter atual da frase (iniciando da posicao zero ate a
			// ultima)
			carac = frase.charAt(c);
			// verifica se esta no inicio (lembrando que o inicio é inicializado
			// como true)
			if (inicio) {
				// verifica se o caracter atual não é um espaç em branco
				if (!ehEspaco(carac)) {
					// acumula o caracter na frase e inicio fica false, pois
					// agora iremos para o meio da frase
					fraseSemEspacos += carac;
					inicio = false;
				}
			}
			// else para o meio da frase, ou seja, quando inicio for false
			else {
				// variavel espacos serve para verificar os espacos entre os
				// intervalos das palavras na frase
				// se o caracter atual for um espaço incrementa 1 em espacos
				if (ehEspaco(carac)) {
					espacos++;
				}
				// se o caracter atual nao for um espaco, espacos é zerado
				else {
					espacos = 0;
				}
				// se variavel espacos for menor que 2, ou seja, sendo qq
				// caracter ou apenas o primeiro espaco, acumula o caracter na
				// frase
				if (espacos < 2) {
					fraseSemEspacos += carac;
				}
			}
			// incrementa o contador que acompanha o tamanho da frase com todos
			// os espacos desordenados
			c++;
		}
		// agora a verificacao do final da frase para retirar os espacos no fim,
		// caso sejam maior que zero, ou seja, se existirem esses espacos no fim
		// da frase
		if (espacos > 0) {
			// tamanho fica sendo agora o tamanho da frase sem espacos
			// abaixo ficou tamanho - 1, porque a verificacao do while deixa
			// apenas um espaco no fim, fazendo esta verificacao tirar apenas um
			// unico espaco no fim da frase
			tamanho = fraseSemEspacos.length() - 1;
			// finalizando a frase sem espacos, tirando seu ultimo espaco no
			// final da frase, indo do inicio ate o ultimo caractere valido
			fraseSemEspacos = fraseSemEspacos.substring(0, tamanho);
		}
		return fraseSemEspacos;
	}
	
	// metodo que verifica se o caracter é espaço ou não
	public static boolean ehEspaco(char c) {
		if (c == ' ')
			return true;
		else
			return false;
	}
	
	// Gera uma string com espaços em branco
	public static String whiteSpaces(int tamString) {
		return newString(" ", tamString);
	}
	
	public static String newString(String charToRepeat, int tamString) {
		int x = 0;
		String s1 = "";
		while (x < tamString) {
			s1 += charToRepeat;
			x++;
		}
		return s1;
	}	

	// Passado uma string e um caracter elimina este da string
	public String delCaracter(String s1, String c1) {
		if (s1.indexOf(c1) != -1) {
			s1 = s1.substring(0, s1.indexOf(c1)) + s1.substring(s1.indexOf(c1) + 1);
		}
		return s1;
	}

	public static String capFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String capWord(String str) {
		return WordUtils.capitalize(str);
	}

	public static String uncapFirst(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String getGetterMethodName(String property) {
		return "get" + capFirst(property);
	}

	public static String getGetterIsMethodName(String property) {
		return "is" + capFirst(property);
	}

	public static String getSetterMethodName(String property) {
		return "set" + capFirst(property);
	}	

	/**
	 * Verifica se a string esta nula ou vazia
	 * 
	 * @param str
	 * string a ser verificada
	 * @return true caso a string seja nula ou vazia, ou possua somente espa�os em branco
	 */
	public static boolean emptyString(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * Transforma um String em Inteiro<br>
	 * Caso a String n�o seja um Inteiro v�lido, retorna 0
	 * 
	 * @param str
	 * String a ser transformada
	 * @return o Inteiro correspondente � String
	 */
	public static int toInteger(String str) {
		return toInteger(str, 0);
	}
	
	/**
	 * Tenta transformar um obj em inteiro, caso n�o consiga retorna null
	 * @param obj
	 * @return
	 */
	public static Integer toIntegerFromObj(Object obj) {
		try{
				return obj!=null ? new Integer(obj.toString()) : null;
		}catch(Exception e){}
		return null;
	}

	/**
	 * Transforma um objeto em String<br>
	 * Caso obj seja nulo retorna nulo.
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		return obj != null ? obj.toString() : null;
	}

	/**
	 * Transforma um String em Inteiro<br>
	 * Caso a String n�o seja um Inteiro v�lido, retorna o defaultValue
	 * 
	 * @param str
	 * String a ser transformada
	 * @param defaultValue
	 * valor a ser retornado caso a String n�o seja um Inteiro v�lido
	 * @return o Inteiro transformado
	 */
	public static int toInteger(String str, int defaultValue) {
		if (str == null)
			return defaultValue;
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defaultValue;
	}

	/**
	 * Verifica se a string � um Integer
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (str == null)
			return false;
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Verifica se a string � um Long
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLong(String str) {
		if (str == null)
			return false;
		try {
			Long.parseLong(str);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Substitui enters no texto pela TAG HTML 'br'
	 * 
	 * @param text
	 * @return
	 */
	public static String replaceNewLinesToHTML(String text) {
		if(text==null)
			return null;
		String saida;
		saida = text.replaceAll("\r\n", "<br/>");
		saida = saida.replaceAll("\n", "<br/>");
		return saida;
	}
	
	
	
//	/**
//	 * Retorna o text plain do HTML informado, limpando as tags HTML
//	 * @param html
//	 * @return
//	 */
//	public static String htmlClean(String html) {
//		if(html==null) {
//			return null;
//		}
//		
//		return new HtmlCleaner().clean(html).getText().toString();
//	}
	
	
	/**
	 * Limpa o html e depois limita os caracteres
	 * 
	 * @param html
	 * @param maxLength
	 * @return
	 */
//	public static String htmlCleanLimitaCaracteres(String html, int maxWidth) {
//		if(html!=null) {
//			String ret = htmlClean(html);
//			return StringUtils.abbreviate(ret, maxWidth);
//		}
//		return html;
//	}
	
	/**
	 * Remove todas as tags da String
	 * @param html
	 * @return
	 */
	public static String htmlFullClean(String html) {
		if(html==null) {
			return null;
		}
		return html.replaceAll("\\<[^>]*>","");
	}
	
	/**
	 * remove as tags html e depois limita os caracteres
	 * 
	 * @param html
	 * @param maxLength
	 * @return
	 */
	public static String htmlFullCleanLimitaCaracteres(String html, int maxWidth) {
		if(html!=null) {
			String ret = htmlFullClean(html);
			return StringUtils.abbreviate(ret, maxWidth);
		}
		return html;
	}	

	public static <OBJECT> boolean notIn(OBJECT obj, OBJECT... values) {
		if (obj == null || values == null)
			return false;
		for (OBJECT value : values) {
			if (obj.equals(value))
				return false;
		}
		return true;
	}

	public static <OBJECT> boolean in(OBJECT obj, OBJECT... values) {
		if (obj == null || values == null)
			return false;
		for (OBJECT value : values) {
			if (obj.equals(value))
				return true;
		}
		return false;
	}

	/**
	 * Retorna uma instancia java.io.File do arquivo que esta na pasta WEB-INF/classes
	 * 
	 * @param file
	 * caminho relativo � pasta WEB-INF/classes do projeto compilado, ou 'conf' referente ao projeto.
	 * @return
	 */
	public static File getWebClassesFile(String file) throws FileNotFoundException {
		//URL url = URLClassLoader.getSystemResource(file);
		URL url = Util.class.getClassLoader().getResource(file);
		if (url == null)
			throw new FileNotFoundException("File not found '" + file + "'");
		return new File(url.getFile());
	}
	
	public static InputStream getWebClassesFileStream(String file) throws FileNotFoundException {
		//File filex = new File(Util.class.getResource(file).getFile());
		//System.out.println(filex);
		//return new FileInputStream(filex);
		
		return Util.class.getClassLoader().getResourceAsStream(file);
		//System.out.println("Passando aqui");
		//return URLClassLoader.getSystemResourceAsStream(file);
	}
	

	/** retorna a url para um arquivo que esta na pasta WEB-INF/classes
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static URL getWebClassesURL(String file) throws FileNotFoundException {
		URL url = Util.class.getClassLoader().getResource(file);
		if (url == null)
			throw new FileNotFoundException("File not found '" + file + "'");
		return url;
	}
	
	public static String getPropertyName(Method method) {
		String name = method.getName();
		if (name.startsWith("get"))
			name = name.replaceFirst("get", "");
		if (name.startsWith("is"))
			name = name.replaceFirst("is", "");
		return Util.uncapFirst(name);
	}

	public static String generateFilename(String originalFilename) {
		String ext = "";
		if (originalFilename != null) {
			int i = originalFilename.lastIndexOf(".");
			if (i != -1 && i < originalFilename.length()) {
				ext = originalFilename.substring(i);
			}
		}
		return System.nanoTime() + ext;
	}

	public static int randomInt(int ini, int end) {
		return (int) Math.round(Math.random() * (end - ini)) + ini;
	}

	public static String limitaCaracteres(String text, int quantidade) {
		if(text==null) {
			return null;
		}
		if (quantidade < 1)
			return text;
		int size = text.length();

		if (quantidade >= size)
			return text;

		String tmp = text.substring(quantidade);
		int proximoEspaco = tmp.indexOf(" ");
		if (proximoEspaco != -1) {
			return text.substring(0, Math.min(quantidade + proximoEspaco, size)) + "...";
		} else {
			return text.substring(0, Math.min(quantidade + 15, size)) + "...";
		}
	}

	/**
	 * Retorna uma sub string de tamanho m�ximo maxlen, dividindo a string original por espa�os
	 * 
	 * @param str
	 * @param maxlen
	 * @return
	 */
	public static String subStringWhiteSpaces(String str, int maxLen) {
		if (str == null)
			return null;
		if (maxLen < 1)
			return "";

		int size = str.length();

		if (maxLen >= size)
			return str;

		String ini = str.substring(0, maxLen);
		String end = str.substring(maxLen, str.length());

		if (end.charAt(0) == ' ')
			return ini;

		int whitespace = ini.lastIndexOf(' ');
		if (whitespace == -1)
			return ini;

		return ini.substring(0, whitespace);

	}
	
	private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	
	public static String formatCurrency(Object value) {
		return value!=null ? currencyFormat.format(value) : null;
	}
	
	public static String formatCurrencyClean(Object value) {
		String text = formatCurrency(value);
		if(text!=null) {
			text = text.replaceAll("\\.", "");
			text = text.replaceAll(",", "");
			text = text.replaceAll("R", "");
			text = text.replaceAll("\\$", "");
			text = text.replaceAll(" ", "");
		}
		return text;
	}	
	
//	public static void main(String args[]) {
//		System.out.println(formatCurrencyClean(Util.newBigDecimal(110.35f)));
//		System.out.println(formatCurrencyClean(Util.newBigDecimal(110)));
//	}
	
	

	public static Double parseDecimal(String d) {
		try {
			return decimalFormat.parse(d).doubleValue();
		} catch (Exception e) {
			return 0D;
		}
	}
	
	public static Double parseValorMonetario(String valor) {
		if(StringUtils.isNotBlank(valor)) {
			if(valor.indexOf(",") < valor.indexOf(".") && valor.indexOf(",") != -1 && valor.indexOf(".") != -1) {
				try {
					return Double.parseDouble(valor.replace(",", ""));
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(valor.indexOf(",") == -1 && valor.indexOf(".") != -1) {
				try {
					return Double.parseDouble(valor);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					return decimalFormat.parse(valor).doubleValue();
				} 
				catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		
		return 0d;
	}
	
	public static String printValorMonetario(String valor) {
		return decimalFormat.format(parseValorMonetario(valor));
	}

	public static String ListToString(List<? extends Object> lista) {
		if (lista != null)
			return lista.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		else
			return "";
	}
	
	public static String zeroEsquerda(int x, int casas) {
		if(x < Math.pow(10, casas)) {
			String r = String.valueOf(x);
			for(int i=r.length();i<casas;i++) {
				r="0"+r;
			}
			return r;
		}
		return String.valueOf(x);
	}

	
	public static String toStringFromArray(Object[] array, String separator) {
		if(array==null)
			return null;
		String ret = "";
		
		for(int i=0;i<array.length;i++) {
			Object obj = array[i];
			if(obj!=null)
				ret += obj;
			if(i < array.length -1)
				ret += separator;
		}
		return ret;
	}	
	
	private static final String RAND_VAR_CHARS = "abcdefghijlmnopqrstuvxz0123456_ABCDEFGHIJKLMNOPQRSTUVXZ";
	private static final int RAND_VAR_CHARS_LENGTH = RAND_VAR_CHARS.length();
	private static final char RAND_VAR_CHAR_ARRAY[] = RAND_VAR_CHARS.toCharArray();
	
	public static String generateRandomVar(int length) {
		StringBuffer sb = new StringBuffer(length);
		for(int i=0;i<length;i++) {
			int index = Util.randomInt(0, RAND_VAR_CHARS_LENGTH - 1);
			sb.append(RAND_VAR_CHAR_ARRAY[index]);
		}
		return sb.toString();
	}
	
	public static String encodeString(String text, String charsetName) {
		if(text!=null) {
			byte bbb[] = text.getBytes();
			
			try {
				return new String(bbb, charsetName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}			
			return text;
		}
		return text;
	}
	
	public static String convertEncode(String text, String EncodeOrigem, String encodeDestino){
		if(text!=null) {

			try {
				Charset origemCharset = Charset.forName(EncodeOrigem);
				Charset destinoCharset = Charset.forName(encodeDestino);
				
				return new String(destinoCharset.encode(origemCharset.decode(ByteBuffer.wrap(text.getBytes()))).array());
				
			} catch (IllegalCharsetNameException e1) {
				e1.printStackTrace();
			} catch (UnsupportedCharsetException e2) {
					e2.printStackTrace();
			}
		}
		return text;
	}

	public static String getPrimeiroNome(String nome) {
		if(StringUtils.isNotBlank(nome)) {
			int index = nome.indexOf(" ");
			if(index > 0) {
				return nome.substring(0, index);
			}
		}
		return nome;
	}
	public static String getSobrenome(String nome) {
		if(StringUtils.isNotBlank(nome)) {
			int index = nome.indexOf(" ");
			if(index > 0) {
				return nome.substring(index + 1, nome.length());
			}
		}
		return "";		
	}
	
//	public static String getUserFromEmail(String email) {
//		if(StringUtils.isNotBlank(email) && validaEmail(email)) {
//			int index = email.indexOf('@');
//			if(index > 0) {
//				return email.substring(0, index);
//			}
//		}
//		return null;
//	}
//	public static String getDomainFromEmail(String email) {
//		if(StringUtils.isNotBlank(email) && validaEmail(email)) {
//			int index = email.indexOf('@');
//			if(index > 0) {
//				return email.substring(index + 1, email.length());
//			}
//		}
//		return null;
//	}
	
	
	/**
	 * Valida email de acordo com as RFCs. Implementadas do java
	 * 
	 * @param email
	 * @return boolean
	 */
//	public static boolean validaEmail(String email) {
//		
////		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
////
////		Matcher m = p.matcher(email);
////		boolean matchFound = m.matches();
////		
////		return matchFound;
//	    
//	    boolean result = true;
//        try {
//        	InternetAddress emailAddr = new InternetAddress(email);
//           	emailAddr.validate();
//        } catch (AddressException ex) {
//        	result = false;
//        } catch (Exception e) {
//        	result = false;
//        }
//        return result;
//	}
	
	public static String removerAcentosString(String input) {
		if (input == null)
			return null;
		try {
			input = Normalizer.normalize(input, Normalizer.Form.NFD);
			input = input.replaceAll("[^\\p{ASCII}]", "");
			return input;
		} catch (Exception e) {
			return input;
		}
	}
	
	public static boolean temCaracterEspecial(String input){
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find();
	}
	
	
	public static String criaLinkHtml(String input) {

		if (StringUtils.isNotBlank(input)) {

			Pattern pattern = Pattern
					.compile("\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)"
							+ "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov"
							+ "|mil|biz|info|mobi|name|aero|jobs|museum"
							+ "|travel|edu|[a-z]{2}))(:[\\d]{1,5})?"
							+ "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?"
							+ "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
							+ "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)"
							+ "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?"
							+ "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*"
							+ "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

			Matcher matcher = pattern.matcher(input);
			while (matcher.find()) {
				
				//Verificar se falta http
				if(!matcher.group().matches("^(https?|ftp)://.*$")){
					input = input.replaceAll(matcher.group(),
							"<a href='http://" + matcher.group() + "' target='_blank'>" + matcher.group()
									+ "</a>");
				}else{
					input = input.replaceAll(matcher.group(),
							"<a href='" + matcher.group() + "'>" + matcher.group()
									+ "</a>");
				}
				
			}

		}
		return input;
	}
	
	

	public static ArrayList<String> findAnchorHtml(String text) {
	
		ArrayList<String> links = new ArrayList<String>();
		Pattern p = Pattern.compile("<a[^>]*>(.*?)</a>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(text);
		
		while(m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")){
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			links.add(urlStr);
		}
		return links;
	}
	
	
	/**
	 * Shift circular a esquerda em um array de bytes
	 *  
	 * @param bytes
	 * @param leftShifts quantidade de shifts
	 */
	public static byte[] shiftBitsLeft(byte[] bytes, final int leftShifts) {
		assert leftShifts >= 1 && leftShifts <= 7;

		final int rightShifts = 8 - leftShifts;

		byte previousByte = bytes[bytes.length - 1]; 
		bytes[bytes.length - 1] = (byte) (((bytes[bytes.length - 1] & 0xff) >> leftShifts) | ((bytes[0] & 0xff) << rightShifts));
		for (int i = bytes.length - 2; i >= 0; i--) {
			byte tmp = bytes[i];
			bytes[i] = (byte) (((bytes[i] & 0xff) >> leftShifts) | ((previousByte & 0xff) << rightShifts));
			previousByte = tmp;
		}
		return bytes;
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Shift circular a direita em um array de bytes
	 * 
	 * @param bytes
	 * @param rightShifts quantidade de shifts
	 */
	public static byte[] shiftBitsRight(byte[] bytes, final int rightShifts) {
		assert rightShifts >= 1 && rightShifts <= 7;

		final int leftShifts = 8 - rightShifts;

		byte previousByte = bytes[0];
		bytes[0] = (byte) (((bytes[0] & 0xff) << rightShifts) | ((bytes[bytes.length - 1] & 0xff) >> leftShifts));
		for (int i = 1; i < bytes.length; i++) {
			byte tmp = bytes[i];
			bytes[i] = (byte) (((bytes[i] & 0xff) << rightShifts) | ((previousByte & 0xff) >> leftShifts));
			previousByte = tmp;
		}
		return bytes;
	}
	
	
	/**
	 * Utilizado para decodificar os bits de hor�rio de login
	 * 
	 * @param b
	 * @return
	 */
	public static String decodeLogonBits(byte b) {
		StringBuffer sb = new StringBuffer();
		sb.append((b & 0x01) > 0 ? "1" : "0");
		sb.append((b & 0x02) > 0 ? "1" : "0");
		sb.append((b & 0x04) > 0 ? "1" : "0");
		sb.append((b & 0x08) > 0 ? "1" : "0");
		sb.append((b & 0x10) > 0 ? "1" : "0");
		sb.append((b & 0x20) > 0 ? "1" : "0");
		sb.append((b & 0x40) > 0 ? "1" : "0");
		sb.append((b & 0x80) > 0 ? "1" : "0");
		return sb.toString();
	}	
	
	/**
	 * Array com os Dias da semana come�ando no Domingo 
	 */
	public static String DAYS_OF_WEEK[] = {"DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB"};
	
	/**
	 * Retorna uma representa��o textual dos hor�rios do usu�rio a partir do array de bytes no padr�o do AD
	 * 
	 * @param logonHours
	 * @return
	 */
	public static String getTextHoursFromLogonBytes(byte[] logonHours) {
		if(logonHours==null || logonHours.length!=21) {
			return null;
		}
		
		byte bs[] = new byte[logonHours.length];
		System.arraycopy(logonHours, 0, bs, 0, logonHours.length);
		
		//Aplica 3 shifts a esquerda para acertar os dados vindo do AD
		Util.shiftBitsLeft(bs, 3);
		
		StringBuffer sb = new StringBuffer();
		for (int k = 0; k < 7; k++) {
			sb.append(DAYS_OF_WEEK[k]);
			sb.append(": ");
			for (int i = 0; i < 3; i++) {
				sb.append(decodeLogonBits(bs[(k * 3) + i]) + " ");
			}
			sb.append("\n");
		}
		return sb.toString();	
	}
	
	
	/** 
	 *  1 - Valor a arredondar. 
	 *  2 - Quantidade de casas depois da v�rgula. 
	 *  3 - Arredondar para cima ou para baixo? 
	 *          Para cima = 0 (ceil) 
	 *          Para baixo = 1 ou qualquer outro inteiro (floor) 
	**/  
	public static double arredondar(double valor, int casas, int ceilOrFloor) {  
        double arredondado = valor;  
        arredondado *= (Math.pow(10, casas));  
        if (ceilOrFloor == 0) {  
            arredondado = Math.ceil(arredondado);             
        } else {  
            arredondado = Math.floor(arredondado);  
        }  
        arredondado /= (Math.pow(10, casas));  
        return arredondado;  
    }  
 	
	
	/**
	 * Verifica se o objeto passado � primitivo
	 * Caso seja passado null retorna false
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isPrimitive(Object obj) {
		if(obj!=null) {
			if( obj.getClass().isPrimitive() || 
				obj instanceof String ||
				obj instanceof Integer ||
				obj instanceof Double ||
				obj instanceof Long ||
				obj instanceof Float ||
				obj instanceof Date ||
				obj instanceof Boolean) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Cria um linkedList com os objetos passados como par�metro
	 * @param e
	 * @return
	 */
	public static <E> List<E> asList(E ... e) {
		LinkedList<E> list = new LinkedList<E>();
		for(E item: e) {
			list.add(item);
		}
		return list;
	}
	
	/**
	 * Verifica se um telefone est� no padrao de mascara  (99) 99999-9999 ou (99) 9999-9999.
	 * @param numeroTelefone
	 * @return boolean
	 */
	public static boolean isTelefone(String numeroTelefone) {
		if(StringUtils.isNotBlank(numeroTelefone))
	        return numeroTelefone.matches("\\(\\d{2}\\) \\d{4,5}-\\d{4}");
		return false;
    }
	
	public static String somenteNumeros(String s) {
		String resultado = "";
		
		for(int i = 1; i <= s.length(); i++) {
			if(!Pattern.matches("([^0-9])+", s.substring(i - 1, i)))
				resultado += s.substring(i - 1, i);
		}
		
		return resultado;
	}
	
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	public static String formatarData(Object data, String pattern) {
		try{
			if(data==null) return "";
			
			if (data instanceof Date) {
				if(pattern==null || "date".equals(pattern)) return simpleDateFormat.format(data);
				if("datetime".equals(pattern)) return simpleDateTimeFormat.format(data);
				return new SimpleDateFormat(pattern).format(data);
			}
			
			if(data instanceof Temporal) {
				if(pattern==null) {
					if(data instanceof LocalDate) return dateFormat.format((Temporal) data);
					if(data instanceof LocalDateTime) return dateTimeFormat.format((Temporal) data);
				}
				if("date".equals(pattern)) return dateFormat.format((Temporal) data);
				if("datetime".equals(pattern)) return dateTimeFormat.format((Temporal) data);
				return DateTimeFormatter.ofPattern(pattern).format((Temporal) data);
			}
			
			return new SimpleDateFormat(pattern).format(data);
		}catch(Exception e) {
			if(data!=null)
				return data.toString();
			else
				return "";
		}
	}
	
	
	public static String stripXSS(String value) {
		if (value != null) {
			// NOTE: It's highly recommended to use the ESAPI library and
			// uncomment the following line to
			// avoid encoded attacks.
			//value = ESAPI.encoder().canonicalize(value);

			// Avoid null characters
			value = value.replaceAll("", "");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:",
					Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
							| Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
		}
		return value;
	}
	
	
	public static <T> T findFirst(List<T> list, Predicate<? super T> predicate) {
		return list.stream().filter(predicate).findFirst().orElse(null);
	}
	
	/**
	 * Criar um BigDecimal zerado, com duas casas decimais
	 * @return BigDecimal
	 * @author EvandroOlivio
	 */
	public static BigDecimal newBigDecimal() {
		return newBigDecimal(0).setScale(2, RoundingMode.HALF_UP);
	}
	
	/**
	 * Criar um BigDecimal pelo parametro
	 * @param valor
	 * @return BigDecimal
	 * @author EvandroOlivio
	 */
	public static BigDecimal newBigDecimal(int valor) {
		return (new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
	}
	
	/**
	 * Criar um BigDecimal pelo parametro
	 * @param valor
	 * @return BigDecimal
	 * @author EvandroOlivio
	 */
	public static BigDecimal newBigDecimal(double valor) {
		return (new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
	}	

	/**
	 * Criar um BigDecimal pelo parametro
	 * @param valor
	 * @return BigDecimal
	 * @author EvandroOlivio
	 */
	public static BigDecimal newBigDecimal(float valor) {
		return (new BigDecimal(valor).setScale(2, RoundingMode.HALF_UP));
	}

	/**
	 * Formata BigDecimal blindando a passagem de valor null e formatando com 2 decimais
	 * @return BigDecimal
	 * @author EvandroOlivio
	 */
	public static BigDecimal newBigDecimal(BigDecimal valor) {
		return (valor==null) ? newBigDecimal() : valor.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * Criar um BigDecimal através do parâmetro, com um número prefixado de casas decimais
	 * @param valor
	 * @return BigDecimal
	 * @author EvandroOlivio
	 */
	public static BigDecimal newBigDecimal(double valor, int scale) {
		if (scale <= 0) scale = 0;
		return (new BigDecimal(valor).setScale(scale, RoundingMode.HALF_UP));
	}
	
	public static boolean maiorQueZero(BigDecimal valor) {
		return BigDecimal.ZERO.compareTo(valor) == 1;
	}
	
	public static boolean maiorIgualZero(BigDecimal valor) {
		return BigDecimal.ZERO.compareTo(valor) == 1 || BigDecimal.ZERO.compareTo(valor) == 0;
	}
	
	/**
	 * Verifica se o valor 1 é maior que o valor 2
	 * 
	 * @param valor1
	 * @param valor2
	 * @return
	 */
	public static boolean maior(BigDecimal valor1, BigDecimal valor2) {
		return valor1.compareTo(valor2) == 1;
	}
	
	/**
	 * Verifica se o valor 1 é igual que o valor 2
	 * 
	 * @param valor1
	 * @param valor2
	 * @return
	 */
	public static boolean igual(BigDecimal valor1, BigDecimal valor2) {
		return valor1.compareTo(valor2) == 0;
	}
	
	/**
	 * Verifica se o valor 1 é igual que o valor 2
	 * 
	 * @param valor1
	 * @param valor2
	 * @return
	 */
	public static boolean menor(BigDecimal valor1, BigDecimal valor2) {
		return valor1.compareTo(valor2) == -1;
	}
	
	public static String urlDecode(String text) {
		if(text!=null) {
			try {
				return URLDecoder.decode(text, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return text;
	}
	public static String urlEncode(String text) {
		if(text!=null) {
			try {
				return URLEncoder.encode(text, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return text;
	}
	
//	public static String urlAddParam(HttpServletRequest request) {
//		Map<String, String> staticParams = new HashMap<>();
//		String queryString = request.getQueryString()!=null ? request.getQueryString() : "";
//		
//		for(String key: staticParams.keySet()) {
//			queryString = addParam(queryString, key, staticParams.get(key));
//		};
//		
//		return queryString;
//	}
//	private static String addParam(String queryString, String name, Object value) {
//		if(value!=null && queryString!=null) {
//			if(queryString.length()!=0 && !queryString.endsWith("&")) {
//				queryString += "&";
//			}
//			queryString += name + "=" + value;
//		}
//		return queryString;
//	}
	
	public static LocalDateTime fromLocalDate(LocalDate localDate) {
		if(localDate!=null) {
			return LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0);
		}
		return null;
	}
	
	public static LocalDateTime fromDate(Date date) {
		if(date!=null) {
			return date.toInstant()
				      .atZone(ZoneId.systemDefault())
				      .toLocalDateTime();
		}
		return null;
	}	
	
	public static Map<String, Object> urlDecodeParams(String paramEncode) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		paramEncode = Util.urlDecode(paramEncode);
		String qs[] = paramEncode.split("&");
		
		for(int i=0;i<qs.length;i++) {
			String p = qs[i].trim();
			if(p.length() > 0) {
				String ps[] = p.split("=");
				if(ps.length==2) {
					params.put(ps[0], ps[1]);
				}
			}
		}
		
		return params;
	}

	public static byte[] createChecksum(Path path) throws NoSuchAlgorithmException, IOException  {
		InputStream fis = null;
		try {
			fis = Files.newInputStream(path);
	
			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;
	
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			return complete.digest();
		}finally{
			if(fis!=null)
				fis.close();	
		}
		
	}


	/**
	 * Calcula o MD5 de um arquivo.
	 * 
	 * @param path
	 * @return MD5 do arquivo e null caso tenha gerado algum erro
	 * @throws Exception
	 */
	public static String getMD5Checksum(Path path) {
		try{
			byte[] b = createChecksum(path);
			String result = "";
	
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}
			return result;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}	
	
	public static String getMD5(String plaintext) {
		if(plaintext==null) {
			return null;
		}
		if(StringUtils.isBlank(plaintext)) {
			return "";
		}
		
		try{
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1, digest);
			String hashtext = bigInt.toString(16);
			// Now we need to zero pad it if you actually want the full 32 chars.
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}	
			return hashtext;
		}catch(NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static final String toHex(byte[] data) {
		final StringBuffer sb = new StringBuffer(data.length * 2);
		for (int i = 0; i < data.length; i++) {
			sb.append(DIGITS[(data[i] >>> 4) & 0x0F]);
			sb.append(DIGITS[data[i] & 0x0F]);
		}
		return sb.toString();
	}
	
	public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String DIGITS_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * Gera um sequencial do tamanho pedido, utilizando letras de A-Z e número 0-1, començando de 1, preenche com zero a esquerda
	 * @param num número de sequencia a ser gerado
	 * @param length tamanho da String
	 * @return
	 */
	public static String sequencialAZ09(int num, int length) {
		String ret = "";
		int size = ((num - 1) / DIGITS_ALPHABET.length()) + 1;
		for(int i=0;i < Math.min(size, length);i++) {
			int index = num;
			
			if(num > DIGITS_ALPHABET.length()) {
				index = ((num - 1) / DIGITS_ALPHABET.length()) + 1;
				num = num - (DIGITS_ALPHABET.length() * ( i + size - 1));
			}
			
			ret += DIGITS_ALPHABET.charAt(index - 1);
		}
		
		//preenche com zero a esquerda
		if(length>ret.length()) {
			for(int i=ret.length();i<length;i++) {
				ret = "0" + ret;
			}
		}
		return ret;
	}
	
	/**
	 * Verifica se o parâmetro é nulo ou não
	 * 
	 * @param Object
	 * @return boolean
	 */
	public static boolean isNull(Object objeto) {
		return objeto == null;
	}

	/**
	 * Verifica se o parâmetro não é nulo
	 * 
	 * @param Object
	 * @return boolean
	 */
	public static boolean isNotNull(Object objeto) {
		return objeto != null;
	}
	
	public static String mask(String value, String mask) {
		try {
			MaskFormatter formatter = new MaskFormatter(mask);
			formatter.setValueContainsLiteralCharacters(false); 
		
			return formatter.valueToString(value);
		} catch (ParseException e){
			e.printStackTrace();
			return value; 
		}
	}
	
	public static String parseDate(String date, String pattern) {
		try {
			return formatarData(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), pattern);
		} catch (DateTimeParseException dtpe){
			return formatarData(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")), pattern);
		}
	}

	private static final DecimalFormat decimalFormat = new DecimalFormat("#,###,###,###.00");

	public static String formatDecimal(Double d) {
		return decimalFormat.format(d);
	}
	
	public static String formatDecimal(BigDecimal d) {
		return decimalFormat.format(d);
	}
	
	public static String formatDecimal(Integer d) {
		return decimalFormat.format(d);
	}
	
	public static String formatDecimal(Long d) {
		return decimalFormat.format(d);
	}
	
	public static String valueByExtensive(Object valor) {
		if(valor instanceof Integer) {
			return valorPorExtenso(formatDecimal((Integer) valor));
		}
		else if(valor instanceof Double) {
			return valorPorExtenso(formatDecimal((Double) valor));
		}
		else if(valor instanceof Long) {
			return valorPorExtenso(formatDecimal((Long) valor));
		}
		else if(valor instanceof BigDecimal) {
			return valorPorExtenso(formatDecimal((BigDecimal) valor));
		}
		
		log.error("Tipo do valor não implementado ou não aceito para valor por extenso");
		return valorPorExtenso(String.valueOf(valor));
	}
	
	/**
	 * http://www.pb.utfpr.edu.br/omero/Java/Fontes/Aplicacoes/Um.java
	 * @param vlr (String)
	 * @return (String)
	 */
	public static String valorPorExtenso(String valor) {
		DecimalFormat dff = (DecimalFormat) DecimalFormat.getInstance();
		Double vlr;
		try {
			vlr = (Double) dff.parse(valor+"D").doubleValue();
		} catch (ParseException e) {
			return "Valor inválido";
		}
		
		if (vlr == 0)
			return ("zero");

		long inteiro = (long) Math.abs(vlr); // parte inteira do valor
		double resto = vlr - inteiro; // parte fracionária do valor

		String vlrS = String.valueOf(inteiro);
		if (vlrS.length() > 15)
			return ("Erro: valor superior a 999 trilhões.");

		String s = "", saux, vlrP;
		String centavos = String.valueOf((int) Math.round(resto * 100));

		String[] unidade = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito",
				"dezenove" };
		String[] centena = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos" };
		String[] dezena = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa" };
		String[] qualificaS = { "", "mil", "milhão", "bilhão", "trilhão" };
		String[] qualificaP = { "", "mil", "milhões", "bilhões", "trilhões" };

		// definindo o extenso da parte inteira do valor
		int n, unid, dez, cent, tam, i = 0;
		boolean umReal = false, tem = false;
		while (!vlrS.equals("0")) {
			tam = vlrS.length();
			// retira do valor a 1a. parte, 2a. parte, por exemplo, para
			// 123456789:
			// 1a. parte = 789 (centena)
			// 2a. parte = 456 (mil)
			// 3a. parte = 123 (milhões)
			if (tam > 3) {
				vlrP = vlrS.substring(tam - 3, tam);
				vlrS = vlrS.substring(0, tam - 3);
			} else { // última parte do valor
				vlrP = vlrS;
				vlrS = "0";
			}
			if (!vlrP.equals("000")) {
				saux = "";
				if (vlrP.equals("100"))
					saux = "cem";
				else {
					n = Integer.parseInt(vlrP, 10); // para n = 371, tem-se:
					cent = n / 100; // cent = 3 (centena trezentos)
					dez = (n % 100) / 10; // dez = 7 (dezena setenta)
					unid = (n % 100) % 10; // unid = 1 (unidade um)
					if (cent != 0)
						saux = centena[cent];
					if ((dez != 0) || (unid != 0)) {
						if ((n % 100) <= 19) {
							if (saux.length() != 0)
								saux = saux + " e " + unidade[n % 100];
							else
								saux = unidade[n % 100];
						} else {
							if (saux.length() != 0)
								saux = saux + " e " + dezena[dez];
							else
								saux = dezena[dez];
							if (unid != 0) {
								if (saux.length() != 0)
									saux = saux + " e " + unidade[unid];
								else
									saux = unidade[unid];
							}
						}
					}
				}
				if (vlrP.equals("1") || vlrP.equals("001")) {
					if (i == 0) // 1a. parte do valor (um real)
						umReal = true;
					else
						saux = saux + " " + qualificaS[i];
				} else if (i != 0)
					saux = saux + " " + qualificaP[i];
				if (s.length() != 0)
					s = saux + ", " + s;
				else
					s = saux;
			}
			if (((i == 0) || (i == 1)) && s.length() != 0)
				tem = true; // tem centena ou mil no valor
			i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão,
						// ...
		}

		if (s.length() != 0) {
			if (umReal)
				s = s + " real";
			else if (tem)
				s = s + " reais";
			else
				s = s + " de reais";
		}

		// definindo o extenso dos centavos do valor
		if (!centavos.equals("0")) { // valor com centavos
			if (s.length() != 0) // se não é valor somente com centavos
				s = s + " e ";
			if (centavos.equals("1"))
				s = s + "um centavo";
			else {
				n = Integer.parseInt(centavos, 10);
				if (n <= 19)
					s = s + unidade[n];
				else { // para n = 37, tem-se:
					unid = n % 10; // unid = 37 % 10 = 7 (unidade sete)
					dez = n / 10; // dez = 37 / 10 = 3 (dezena trinta)
					s = s + dezena[dez];
					if (unid != 0)
						s = s + " e " + unidade[unid];
				}
				s = s + " centavos";
			}
		}
		return (s);
	}
	
	/**
	 * Variação do método acima, pegando o DecimalFormat do locale "pt_BR" e não mais o default da máquina
	 * @param vlr (String)
	 * @return (String)
	 */
	public static String valorPorExtensoBR(String valor) {
		Locale locale = new Locale("pt", "BR");
		DecimalFormat dff = (DecimalFormat) DecimalFormat.getInstance(locale);
		
		Double vlr;
		try {
			vlr = (Double) dff.parse(valor+"D").doubleValue();
		} catch (ParseException e) {
			return "Valor inválido";
		}
		
		if (vlr == 0)
			return ("zero");

		long inteiro = (long) Math.abs(vlr); // parte inteira do valor
		double resto = vlr - inteiro; // parte fracionária do valor

		String vlrS = String.valueOf(inteiro);
		if (vlrS.length() > 15)
			return ("Erro: valor superior a 999 trilhões.");

		String s = "", saux, vlrP;
		String centavos = String.valueOf((int) Math.round(resto * 100));

		String[] unidade = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito",
				"dezenove" };
		String[] centena = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos" };
		String[] dezena = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa" };
		String[] qualificaS = { "", "mil", "milhão", "bilhão", "trilhão" };
		String[] qualificaP = { "", "mil", "milhões", "bilhões", "trilhões" };

		// definindo o extenso da parte inteira do valor
		int n, unid, dez, cent, tam, i = 0;
		boolean umReal = false, tem = false;
		while (!vlrS.equals("0")) {
			tam = vlrS.length();
			// retira do valor a 1a. parte, 2a. parte, por exemplo, para
			// 123456789:
			// 1a. parte = 789 (centena)
			// 2a. parte = 456 (mil)
			// 3a. parte = 123 (milhões)
			if (tam > 3) {
				vlrP = vlrS.substring(tam - 3, tam);
				vlrS = vlrS.substring(0, tam - 3);
			} else { // última parte do valor
				vlrP = vlrS;
				vlrS = "0";
			}
			if (!vlrP.equals("000")) {
				saux = "";
				if (vlrP.equals("100"))
					saux = "cem";
				else {
					n = Integer.parseInt(vlrP, 10); // para n = 371, tem-se:
					cent = n / 100; // cent = 3 (centena trezentos)
					dez = (n % 100) / 10; // dez = 7 (dezena setenta)
					unid = (n % 100) % 10; // unid = 1 (unidade um)
					if (cent != 0)
						saux = centena[cent];
					if ((dez != 0) || (unid != 0)) {
						if ((n % 100) <= 19) {
							if (saux.length() != 0)
								saux = saux + " e " + unidade[n % 100];
							else
								saux = unidade[n % 100];
						} else {
							if (saux.length() != 0)
								saux = saux + " e " + dezena[dez];
							else
								saux = dezena[dez];
							if (unid != 0) {
								if (saux.length() != 0)
									saux = saux + " e " + unidade[unid];
								else
									saux = unidade[unid];
							}
						}
					}
				}
				if (vlrP.equals("1") || vlrP.equals("001")) {
					if (i == 0) // 1a. parte do valor (um real)
						umReal = true;
					else
						saux = saux + " " + qualificaS[i];
				} else if (i != 0)
					saux = saux + " " + qualificaP[i];
				if (s.length() != 0)
					s = saux + ", " + s;
				else
					s = saux;
			}
			if (((i == 0) || (i == 1)) && s.length() != 0)
				tem = true; // tem centena ou mil no valor
			i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão,
						// ...
		}

		if (s.length() != 0) {
			if (umReal)
				s = s + " real";
			else if (tem)
				s = s + " reais";
			else
				s = s + " de reais";
		}

		// definindo o extenso dos centavos do valor
		if (!centavos.equals("0")) { // valor com centavos
			if (s.length() != 0) // se não é valor somente com centavos
				s = s + " e ";
			if (centavos.equals("1"))
				s = s + "um centavo";
			else {
				n = Integer.parseInt(centavos, 10);
				if (n <= 19)
					s = s + unidade[n];
				else { // para n = 37, tem-se:
					unid = n % 10; // unid = 37 % 10 = 7 (unidade sete)
					dez = n / 10; // dez = 37 / 10 = 3 (dezena trinta)
					s = s + dezena[dez];
					if (unid != 0)
						s = s + " e " + unidade[unid];
				}
				s = s + " centavos";
			}
		}
		return (s);
	}
	
	/**
	 * Método para validar se um Boolean é true
	 * @param value
	 * @return true caso value seja true, ou false caso value seja false ou null
	 */
	public static boolean checkTrue(Boolean value) {
		return value!=null ? value : false;
	}
	
	public static boolean isFinalDeSemana(LocalDate date) {
		return date == null ? false : (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
	}
	
}

