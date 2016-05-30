package it.eurotn.panjea.anagrafica.classedocumento.manager;

import it.eurotn.panjea.anagrafica.classedocumento.IClasseTipoDocumento;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseDdt;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseFattura;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseIncassoPagamento;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseMovimentoGenerico;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseMovimentoMagazzinoGenerico;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseOfferta;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseOrdine;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClassePreventivo;
import it.eurotn.panjea.anagrafica.classedocumento.manager.interfaces.ClasseTipoDocumentoManager;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

/**
 * 
 * @author Fattazzo <g.fattarsi@eurotn.it>
 */
@Stateless(name = "Panjea.ClasseTipoDocumentoManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.ClasseTipoDocumentoManagerBean")
public class ClasseTipoDocumentoManagerBean implements ClasseTipoDocumentoManager {

	/**
	 * @return lista di classi abilitate.
	 */
	@Override
	public List<IClasseTipoDocumento> caricaClassiTipoDocumento() {
		List<IClasseTipoDocumento> list = new ArrayList<IClasseTipoDocumento>();
		list.add(new ClasseMovimentoGenerico());
		list.add(new ClasseMovimentoMagazzinoGenerico());
		list.add(new ClasseDdt());
		list.add(new ClasseFattura());
		list.add(new ClasseOrdine());
		list.add(new ClasseIncassoPagamento());
		list.add(new ClasseOfferta());
		list.add(new ClassePreventivo());
		return list;
	}

}
