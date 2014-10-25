package blossom.restful.service.business.document.model;

public class DocumentsSingleton {

    private static DocumentsSingleton INSTANCE;

    private DocumentsSingleton() {
    }

    public static DocumentsSingleton getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new DocumentsSingleton();
        }
        return INSTANCE;
    }
}
