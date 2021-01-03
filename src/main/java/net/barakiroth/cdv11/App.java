package net.barakiroth.cdv11;

import net.barakiroth.cdv11.exceptions.Cdv11StringFormatException;
import net.barakiroth.cdv11.exceptions.DateBasedCdv11StringFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        final App app = new App();
        app.run();
    }

    private void run() {

        Fnr fnr;
        try {
            fnr = new Fnr("21042221454");
        } catch (Cdv11StringFormatException e) {
            e.printStackTrace();
            fnr = null;
        } catch (DateBasedCdv11StringFormatException e) {
            e.printStackTrace();
            fnr = null;
        }
    }
}