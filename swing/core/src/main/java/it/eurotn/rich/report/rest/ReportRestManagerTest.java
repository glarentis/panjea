package it.eurotn.rich.report.rest;

public class ReportRestManagerTest {

    public static void main(String[] args) {
        ClientConfig config = new ClientConfig();
        config.setPassword("jasperadmin");
        config.setUserName("jasperadmin");
        config.setUrl("localhost");
        config.setPort("8080");
        ReportRestManager manager = new ReportRestManager(config);
        manager.login();
        try {

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
