package br.cad.pay.boleto;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import br.cad.pay.boleto.bancos.BancoDoBrasil;
import br.cad.pay.boleto.bancos.Bradesco;
import br.cad.pay.boleto.bancos.Caixa;
import br.cad.pay.boleto.bancos.Itau;
import br.cad.pay.boleto.bancos.Santander;
import br.cad.pay.boleto.transformer.GeradorDeBoleto;


/**
 * 
 * <h1>BoletoExamplo.java</h1>
 * 
 * @create 20 de fev de 2020
 * @update William César Rodrigues <br/>
 *         william.rodrigues@live.fae.edu
 * @version 1.0.0
 *
 */
public class BoletoExamploTest {	
	private static final Log log = LogFactory.getLog(BoletoExamploTest.class);
	
	private static final File TEMP_DIRECTORY = new File(System.getProperty("java.io.tmpdir"));
	
	static Datas datas = Datas.novasDatas()
		.comDocumento(20, 2, 2020)
		.comProcessamento(20, 2, 2020)
		.comVencimento(1, 1, 2025);

	static Endereco enderecoBeneficiario = Endereco.novoEndereco()
		.comLogradouro("Av das Empresas, 555")
		.comBairro("Bairro Grande")
		.comCep("01234-555")
		.comCidade("São Paulo")
		.comUf("SP");
				
	static Endereco enderecoPagador = Endereco.novoEndereco()
		.comLogradouro("Av dos testes, 111 apto 333")
		.comBairro("Bairro Teste")
		.comCep("01234111")
		.comCidade("São Paulo")
		.comUf("SP");

				// Quem paga o boleto
	static Pagador pagador = Pagador.novoPagador()
		.comNome("Fulano da Silva")
		.comDocumento("11122233312")
		.comEndereco(enderecoPagador);
	
	public void createDir() {
		File newDirectory = new File(TEMP_DIRECTORY, "boletos");
		
		assertFalse(newDirectory.exists());
		
		assertTrue(newDirectory.mkdir());
	}

	@Test
	public void bancodobrasil() {
		createDir();
		
		log.info("Criando boletos do Bando do Brasil");
		
		Banco banco = new BancoDoBrasil();
		
		// Quem emite o boleto
		Beneficiario beneficiario = Beneficiario.novoBeneficiario()
			.comNomeBeneficiario("Fulano de Tal")
			.comAgencia("1824")
			.comDigitoAgencia("4")
			.comCodigoBeneficiario("76000")
			.comDigitoCodigoBeneficiario("5")
			.comNumeroConvenio("1207113")
			.comCarteira("18")
			.comEndereco(enderecoBeneficiario)
			.comNossoNumero("00000000009000206");
		// FIXME: Estranho o nosso numero com 17 caracteres verificar o manual do banco do brasil

		Boleto boleto = Boleto.novoBoleto()
			.comBanco(banco)
			.comDatas(datas)
			.comBeneficiario(beneficiario)
			.comPagador(pagador)
			.comValorBoleto("200.00")
			.comNumeroDoDocumento("1234")
			.comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
			.comLocaisDePagamento("local 1", "local 2");

		GeradorDeBoleto gerador = new GeradorDeBoleto(boleto);

		String nameFile = "%s/%s/boleto-bancodobrasil.%s";
		
		// Para gerar um boleto em PDF
		gerador.geraPDF(String.format(nameFile, TEMP_DIRECTORY, "boletos", "pdf"));

		// Para gerar um boleto em PNG
		gerador.geraPNG(String.format(nameFile, TEMP_DIRECTORY, "boletos", "png"));

		// Para gerar um array de bytes a partir de um PDF
		gerador.geraPDF();

		// Para gerar um array de bytes a partir de um PNG
		gerador.geraPNG();
		
		deleteDir();
	}
	
	@Test
	public void bradesco() {
		createDir();
				
		log.info("Criando boletos do Bando Bradesco");
		
		Banco banco = new Bradesco();
		
		// Quem emite o boleto
		Beneficiario beneficiario = Beneficiario.novoBeneficiario()
			.comNomeBeneficiario("Fulano de Tal")
			.comAgencia("1824")
			.comDigitoAgencia("4")
			.comCodigoBeneficiario("76000")
			.comDigitoCodigoBeneficiario("5")
			.comNumeroConvenio("1207113")
			.comCarteira("09")
			.comEndereco(enderecoBeneficiario)
			.comNossoNumero("9000206");

		Boleto boleto = Boleto.novoBoleto()
			.comBanco(banco)
			.comDatas(datas)
			.comBeneficiario(beneficiario)
			.comPagador(pagador)
			.comValorBoleto("200.00")
			.comNumeroDoDocumento("1234")
			.comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
			.comLocaisDePagamento("local 1", "local 2");

		GeradorDeBoleto gerador = new GeradorDeBoleto(boleto);

		String nameFile = "%s/%s/boleto-bradesco.%s";
		
		// Para gerar um boleto em PDF
		gerador.geraPDF(String.format(nameFile, TEMP_DIRECTORY, "boletos", "pdf"));

		// Para gerar um boleto em PNG
		gerador.geraPNG(String.format(nameFile, TEMP_DIRECTORY, "boletos", "png"));

		// Para gerar um array de bytes a partir de um PDF
		gerador.geraPDF();

		// Para gerar um array de bytes a partir de um PNG
		gerador.geraPNG();
		
		deleteDir();
	}
	
	@Test
	public void santander() {
		createDir();
		
		log.info("Criando boletos do Bando Santander");
		
		Banco banco = new Santander();
		
		// Quem emite o boleto
		Beneficiario beneficiario = Beneficiario.novoBeneficiario()
			.comNomeBeneficiario("Fulano de Tal")
			.comAgencia("1824")
			.comDigitoAgencia("")
			.comCodigoBeneficiario("37179799")
			.comDigitoCodigoBeneficiario("1")
			.comNumeroConvenio("1207113")
			.comCarteira("101")
			.comEndereco(enderecoBeneficiario)
			.comNossoNumero("9000206");

		Boleto boleto = Boleto.novoBoleto()
			.comBanco(banco)
			.comDatas(datas)
			.comBeneficiario(beneficiario)
			.comPagador(pagador)
			.comValorBoleto("200.00")
			.comNumeroDoDocumento("1234")
			.comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
			.comLocaisDePagamento("local 1", "local 2");

		GeradorDeBoleto gerador = new GeradorDeBoleto(boleto);

		String nameFile = "%s/%s/boleto-santander.%s";
		
		// Para gerar um boleto em PDF
		gerador.geraPDF(String.format(nameFile, TEMP_DIRECTORY, "boletos", "pdf"));

		// Para gerar um boleto em PNG
		gerador.geraPNG(String.format(nameFile, TEMP_DIRECTORY, "boletos", "png"));

		// Para gerar um array de bytes a partir de um PDF
		gerador.geraPDF();

		// Para gerar um array de bytes a partir de um PNG
		gerador.geraPNG();
		
		deleteDir();
	}
	
	@Test
	public void itau() {
		createDir();
		
		log.info("Criando boletos do Bando Itaú");
		
		Banco banco = new Itau();
		
		// Quem emite o boleto
		Beneficiario beneficiario = Beneficiario.novoBeneficiario()
			.comNomeBeneficiario("Fulano de Tal")
			.comAgencia("1824")
			.comDigitoAgencia("")
			.comCodigoBeneficiario("79799")
			.comDigitoCodigoBeneficiario("1")
			.comNumeroConvenio("7113")
			.comCarteira("112")
			.comEndereco(enderecoBeneficiario)
			.comDigitoNossoNumero("0")
			.comNossoNumero("9000206");

		Boleto boleto = Boleto.novoBoleto()
			.comBanco(banco)
			.comDatas(datas)
			.comBeneficiario(beneficiario)
			.comPagador(pagador)
			.comValorBoleto("200.00")
			.comNumeroDoDocumento("1234")
			.comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
			.comLocaisDePagamento("local 1", "local 2");

		GeradorDeBoleto gerador = new GeradorDeBoleto(boleto);

		String nameFile = "%s/%s/boleto-santander.%s";
		
		// Para gerar um boleto em PDF
		gerador.geraPDF(String.format(nameFile, TEMP_DIRECTORY, "boletos", "pdf"));

		// Para gerar um boleto em PNG
		gerador.geraPNG(String.format(nameFile, TEMP_DIRECTORY, "boletos", "png"));

		// Para gerar um array de bytes a partir de um PDF
		gerador.geraPDF();

		// Para gerar um array de bytes a partir de um PNG
		gerador.geraPNG();
		
		deleteDir();
	}
	
	@Test
	public void caixaeconomica() {
		createDir();
				
		log.info("Criando boletos da Caixa Economica Federal");
		
		Banco banco = new Caixa();
		
		// Quem emite o boleto
		Beneficiario beneficiario = Beneficiario.novoBeneficiario()
			.comNomeBeneficiario("Fulano de Tal")
			.comAgencia("1824")
			.comDigitoAgencia("4")
			.comCodigoBeneficiario("5893222")
			.comDigitoCodigoBeneficiario("8")
			.comNumeroConvenio("1207113")
			.comCarteira("1")
			.comEndereco(enderecoBeneficiario)
			.comNossoNumero("09000206");

		Boleto boleto = Boleto.novoBoleto()
			.comBanco(banco)
			.comDatas(datas)
			.comBeneficiario(beneficiario)
			.comPagador(pagador)
			.comValorBoleto("200.00")
			.comNumeroDoDocumento("1234")
			.comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")
			.comLocaisDePagamento("local 1", "local 2");

		GeradorDeBoleto gerador = new GeradorDeBoleto(boleto);

		String nameFile = "%s/%s/boleto-bradesco.%s";
		
		// Para gerar um boleto em PDF
		gerador.geraPDF(String.format(nameFile, TEMP_DIRECTORY, "boletos", "pdf"));

		// Para gerar um boleto em PNG
		gerador.geraPNG(String.format(nameFile, TEMP_DIRECTORY, "boletos", "png"));

		// Para gerar um array de bytes a partir de um PDF
		gerador.geraPDF();

		// Para gerar um array de bytes a partir de um PNG
		gerador.geraPNG();
		
		deleteDir();
	}
	
	public void deleteDir() {
		File theDir = new File("/tmp/boletos/");

		if(theDir.exists()) {
			try{
				Files.walk(Paths.get("/tmp/boletos/")).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
			}catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
