package it.eurotn.panjea.vega.rich.bd.importazione;

import it.eurotn.panjea.rich.bd.AbstractBaseBD;

public abstract class AbstractVegaImportaBD extends AbstractBaseBD implements IVegaImportaBD {

    private RestClient restClient;

    /**
     * @return Returns the restClient.
     */
    public RestClient getRestClient() {
        return restClient;
    }

    /**
     * @param restClient
     *            The restClient to set.
     */
    public void setRestClient(RestClient restClient) {
        this.restClient = restClient;
    }
}
