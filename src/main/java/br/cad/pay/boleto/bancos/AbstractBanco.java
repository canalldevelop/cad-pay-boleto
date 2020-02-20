package br.cad.pay.boleto.bancos;

import static br.com.caelum.stella.boleto.utils.StellaStringUtils.prefixNotNullStringOrDefault;
import br.cad.pay.boleto.Banco;
import br.cad.pay.boleto.Beneficiario;
import br.cad.pay.boleto.Boleto;
import br.cad.pay.boleto.bancos.gerador.GeradorDeDigito;
import br.cad.pay.boleto.bancos.gerador.GeradorDeDigitoPadrao;

public abstract class AbstractBanco implements Banco {

	private static final long serialVersionUID = 1L;

	protected final GeradorDeDigito geradorDeDigito = new GeradorDeDigitoPadrao();

	@Override
	public GeradorDeDigito getGeradorDeDigito() {
		return geradorDeDigito;
	}

	@Override
	public String getNossoNumeroECodigoDocumento(Boleto boleto) {
		Beneficiario beneficiario = boleto.getBeneficiario();
		StringBuilder builder = new StringBuilder().append(beneficiario.getCarteira());
		builder.append("/").append(getNossoNumeroFormatado(beneficiario));
		return builder.toString();
	}

	@Override
	public String getAgenciaECodigoBeneficiario(Beneficiario beneficiario) {
		StringBuilder builder = new StringBuilder();
		builder.append(beneficiario.getAgenciaFormatada());
		builder.append(prefixNotNullStringOrDefault(beneficiario.getDigitoAgencia(), "", "-"));
		builder.append("/");
		builder.append(getCodigoBeneficiarioFormatado(beneficiario));
		builder.append(prefixNotNullStringOrDefault(beneficiario.getDigitoCodigoBeneficiario(), "", "-"));	
		return builder.toString();
	}

}
