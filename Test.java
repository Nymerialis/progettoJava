import java.util.Iterator;

public class Test{
    private MyBoard1<MyData> Maria;
    private MyBoard2<MyData> Mario;
    private String password;

    public Test() {
        password = "Mrossi89";
        Maria = new MyBoard1<>(password);
        Mario = new MyBoard2<>(password);
    }

    public void test1() throws NotAllowedActionException, CategoryAlreadyExistsException,
            CategoryNotFoundException, DataNotFoundException, FriendNotFoundException,
            NotSharedCategoryException, DataAlreadyExistsException {

        System.out.println("TEST1");

        //----------------------------------------------------------------------
        System.out.println("*setPassword*");
        try {
            Maria.setPassword(password, null);
        } catch (NullPointerException e) {
            System.out.println("La bacheca non accetta una password null");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*createCategory*");
        try {
            Maria.createCategory("Lavoro", "passwordSbagliata");
        } catch (NotAllowedActionException e) {
            System.out.println("La bacheca effettua correttamente i controlli d'identità");
        }

        try {
            Maria.createCategory(null, password);
        } catch (NullPointerException e) {
            System.out.println("La bacheca non accetta una categoria null");
        }

        Maria.createCategory("Lavoro", password);
        Maria.createCategory("CorsoDiTeatro", password);
        Maria.createCategory("Maneggio", password);

        try {
            Maria.createCategory("Lavoro", password);
        } catch (CategoryAlreadyExistsException e) {
            System.out.println("La bacheca non ammette categorie duplicate");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*removeCategory*");
        try {
            Maria.removeCategory("AmiciImmaginari", password);
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può eliminare una categoria che non esiste");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*addFriend*");
        //creo alcune categorie e le assegno a degli amici
        Maria.addFriend("Lavoro", password, "LuigiRepartoMarketing");
        Maria.addFriend("Lavoro", password, "AnnaSegretaria");
        Maria.addFriend("Lavoro", password, "Giovanna");
        Maria.addFriend("CorsoDiTeatro", password, "MaestroDiRecitazione");
        Maria.addFriend("CorsoDiTeatro", password, "Giovanna");
        Maria.addFriend("CorsoDiTeatro", password, "Lorenzo");
        Maria.addFriend("CorsoDiTeatro", password, "Alessio");
        Maria.addFriend("Maneggio", password, "Istruttore");
        Maria.addFriend("Maneggio", password, "Lorenzo");

        try {
            Maria.addFriend("AmiciImmaginari", password, "Giovanna");
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può inserire un amico in una categoria che non esiste");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*removeFriend*");
        try {
            Maria.removeFriend("AmiciImmaginari", password, "Giovanna");
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può rimuovere un amico da una categoria che non esiste");
        }

        try {
            Maria.removeFriend("Lavoro", password, "BabboNatale");
        } catch (FriendNotFoundException e) {
            System.out.println("Non si può rimuovere un amico che non esiste");
        }

        try {
            Maria.removeFriend("Lavoro", password, "Lorenzo");
        } catch (NotSharedCategoryException e) {
            System.out.println("Non si può rimuovere un amico da una categoria a cui non ha accesso");
        }

        Maria.removeFriend("CorsoDiTeatro", password, "Alessio");
        System.out.println("Alessio è stato rimosso con successo dalla categoria \"CorsoDiTeatro\"");


        //----------------------------------------------------------------------
        //creo alcuni dati
        MyData post1 = new MyData("RIUNIONE DIRIGENTI",
                "La prossima riunione è stata fissata per Lunedì ore 9.00 sala Conferenze");
        MyData post2 = new MyData("STAMPANTI GUASTE",
                "La stampante dell'ufficio 7 è guasta.\n" +
                        "Utilizzare la stampante dell'ufficio 3.");
        MyData post3 = new MyData("CAMPIONATO REGIONALE",
                "Il prossimo campionato si terrà dal 14 al 16 Marzo a Pontedera.\n" +
                        "La quota di iscrizione è di 40 euro.\n" +
                        "Le iscrizioni terminano il 29/02.");
        MyData post4 = new MyData("LEZIONI SOSPESE!", "La pista è inagibile a causa della pioggia.\n" +
                "Avviseremo quando sarà possibile riprendere le normali attività del maneggio.");
        MyData post5 = new MyData("FESTEGGIAMENTI!", "Il progetto Eagle è stato un grande successo!\n" +
                "Complimenti a tutti per l'ottimo lavoro di squadra! Continuiamo così!\n" +
                "Domani mattina ci sarà un piccolo rinfresco per festeggiare.");
        MyData post6 = new MyData("SPETTACOLO DI FINE ANNO", "Abbiamo finalmente fissato la data:\n" +
                "andremo in scena il 15/06. Avvertite amici e parenti!");


        //----------------------------------------------------------------------
        System.out.println("\n*put*");
        try {
            Maria.put(password, null, "Lavoro");
        } catch (NullPointerException e) {
            System.out.println("Non si può inserire un dato null");
        }

        try {
            Maria.put(password, post1, "AmiciImmaginari");
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può inserire un dato in una categoria che non esiste");
        }

        Maria.put(password, post1, "Lavoro");
        Maria.put(password, post2, "Lavoro");
        Maria.put(password, post5, "Lavoro");

        try {
            Maria.put(password, post1, "CorsoDiTeatro");
        } catch (DataAlreadyExistsException e) {
            System.out.println("Non sono ammessi dati duplicati, neanche in categorie diverse");
        }

        Maria.put(password, post4, "Maneggio");
        Maria.put(password, post3, "Maneggio");
        Maria.put(password, post6, "CorsoDiTeatro");


        //----------------------------------------------------------------------
        System.out.println("\n*insertLike*");
        try {
            Maria.insertLike("Giovanna", post4);
        } catch(NotSharedCategoryException e) {
            System.out.println("Ogni amico può mettere \"like\" solo ai dati a cui ha accesso");
        }

        Iterator<MyData> showToGiovanna = Maria.getFriendIterator("Giovanna");
        while(showToGiovanna.hasNext()) {
            Maria.insertLike("Giovanna", showToGiovanna.next());
        }
        for(int i = 0; i < 5; i++) {
            Maria.insertLike("LuigiRepartoMarketing", post5);
        }
        Maria.insertLike("AnnaSegretaria", post1);
        Maria.insertLike("Lorenzo", post3);
        Maria.insertLike("Lorenzo", post3);
        Maria.insertLike("Lorenzo", post3);


        //----------------------------------------------------------------------
        System.out.println("\n*get, getFriendIterator*");
        System.out.println("Osservazione: è possibile inserire in bacheca una copia,\n" +
                "creata con get, di un dato già esistente perché get restituisce\n" +
                "un nuovo dato con valori identici.\n" +
                "Chi visualizza i dati troverà due volte lo stesso contenuto.\n" +
                "In questo esempio, le categorie \"Maneggio\" e \"CorsoDiTeatro\" contengono \n" +
                "una copia dello stesso post. Lorenzo ha accesso ad entrambe le categorie.");

        MyData post4copy = Maria.get(password, post4);
        Maria.put(password, post4copy , "CorsoDiTeatro");

        System.out.println("*************Post visibili da Lorenzo*************");
        Iterator<MyData> showToLorenzo = Maria.getFriendIterator("Lorenzo");
        while(showToLorenzo.hasNext()) {
            Maria.print(showToLorenzo.next());
        }
        System.out.println("**************************************************");


        //----------------------------------------------------------------------
        System.out.println("\n*remove*");
        Maria.remove(password, post4copy);
        System.out.println("Il dato è stato rimosso con successo");

        try {
            Maria.remove(password, post4copy);
        } catch(DataNotFoundException e) {
            System.out.println("Non è possibile rimuovere un dato che non esiste");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*getDataCategory*");
        System.out.println("Il proprietario della bacheca chiede di visualizzare tutti i dati\n" +
                "nella categoria \"Lavoro\":");
        System.out.println("**********************Lavoro**********************");
        for(MyData e : Maria.getDataCategory(password, "Lavoro")) {
            Maria.print(e);
        }
        System.out.println("**************************************************");


        //----------------------------------------------------------------------
        System.out.println("\n*getIterator*");
        System.out.println("*****Tutti i dati ordinati per numero di like*****");
        Iterator<MyData> allData = Maria.getIterator(password);
        while(allData.hasNext()) {
            Maria.print(allData.next());
        }
        System.out.println("**************************************************");


        //----------------------------------------------------------------------
        System.out.println("\n********Tutti i post condivisi con Alessio********");
        Iterator<MyData> showToAlessio = Maria.getFriendIterator("Alessio");
        while(showToAlessio.hasNext()) {
            Maria.print(showToAlessio.next());
        }
        System.out.println("**************************************************");
        System.out.println("Alessio non vede nessun post perché non appartiene a nessuna categoria");
    }


    public void test2() throws NotAllowedActionException, CategoryAlreadyExistsException,
            CategoryNotFoundException, DataNotFoundException, FriendNotFoundException,
            NotSharedCategoryException, DataAlreadyExistsException {

        System.out.println("TEST2");

        //----------------------------------------------------------------------
        System.out.println("*setPassword*");
        try {
            Mario.setPassword(password, null);
        } catch (NullPointerException e) {
            System.out.println("La bacheca non accetta una password null");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*createCategory*");
        try {
            Mario.createCategory("Lavoro", "passwordSbagliata");
        } catch (NotAllowedActionException e) {
            System.out.println("La bacheca effettua correttamente i controlli d'identità");
        }

        try {
            Mario.createCategory(null, password);
        } catch (NullPointerException e) {
            System.out.println("La bacheca non accetta una categoria null");
        }

        Mario.createCategory("Lavoro", password);
        Mario.createCategory("CorsoDiTeatro", password);
        Mario.createCategory("Maneggio", password);

        try {
            Mario.createCategory("Lavoro", password);
        } catch (CategoryAlreadyExistsException e) {
            System.out.println("La bacheca non ammette categorie duplicate");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*removeCategory*");
        try {
            Mario.removeCategory("AmiciImmaginari", password);
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può eliminare una categoria che non esiste");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*addFriend*");
        //creo alcune categorie e le assegno a degli amici
        Mario.addFriend("Lavoro", password, "LuigiRepartoMarketing");
        Mario.addFriend("Lavoro", password, "AnnaSegretaria");
        Mario.addFriend("Lavoro", password, "Giovanna");
        Mario.addFriend("CorsoDiTeatro", password, "MaestroDiRecitazione");
        Mario.addFriend("CorsoDiTeatro", password, "Giovanna");
        Mario.addFriend("CorsoDiTeatro", password, "Lorenzo");
        Mario.addFriend("CorsoDiTeatro", password, "Alessio");
        Mario.addFriend("Maneggio", password, "Istruttore");
        Mario.addFriend("Maneggio", password, "Lorenzo");

        try {
            Mario.addFriend("AmiciImmaginari", password, "Giovanna");
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può inserire un amico in una categoria che non esiste");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*removeFriend*");
        try {
            Mario.removeFriend("AmiciImmaginari", password, "Giovanna");
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può rimuovere un amico da una categoria che non esiste");
        }

        try {
            Mario.removeFriend("Lavoro", password, "BabboNatale");
        } catch (FriendNotFoundException e) {
            System.out.println("Non si può rimuovere un amico che non esiste");
        }

        try {
            Mario.removeFriend("Lavoro", password, "Lorenzo");
        } catch (NotSharedCategoryException e) {
            System.out.println("Non si può rimuovere un amico da una categoria a cui non ha accesso");
        }

        Mario.removeFriend("CorsoDiTeatro", password, "Alessio");
        System.out.println("Alessio è stato rimosso con successo dalla categoria \"CorsoDiTeatro\"");


        //----------------------------------------------------------------------
        //creo alcuni dati
        MyData post1 = new MyData("RIUNIONE DIRIGENTI",
                "La prossima riunione è stata fissata per Lunedì ore 9.00 sala Conferenze");
        MyData post2 = new MyData("STAMPANTI GUASTE",
                "La stampante dell'ufficio 7 è guasta.\n" +
                        "Utilizzare la stampante dell'ufficio 3.");
        MyData post3 = new MyData("CAMPIONATO REGIONALE",
                "Il prossimo campionato si terrà dal 14 al 16 Marzo a Pontedera.\n" +
                        "La quota di iscrizione è di 40 euro.\n" +
                        "Le iscrizioni terminano il 29/02.");
        MyData post4 = new MyData("LEZIONI SOSPESE!", "La pista è inagibile a causa della pioggia.\n" +
                "Avviseremo quando sarà possibile riprendere le normali attività del maneggio.");
        MyData post5 = new MyData("FESTEGGIAMENTI!", "Il progetto Eagle è stato un grande successo!\n" +
                "Complimenti a tutti per l'ottimo lavoro di squadra! Continuiamo così!\n" +
                "Domani mattina ci sarà un piccolo rinfresco per festeggiare.");
        MyData post6 = new MyData("SPETTACOLO DI FINE ANNO", "Abbiamo finalmente fissato la data:\n" +
                "andremo in scena il 15/06. Avvertite amici e parenti!");


        //----------------------------------------------------------------------
        System.out.println("\n*put*");
        try {
            Mario.put(password, null, "Lavoro");
        } catch (NullPointerException e) {
            System.out.println("Non si può inserire un dato null");
        }

        try {
            Mario.put(password, post1, "AmiciImmaginari");
        } catch (CategoryNotFoundException e) {
            System.out.println("Non si può inserire un dato in una categoria che non esiste");
        }

        Mario.put(password, post1, "Lavoro");
        Mario.put(password, post2, "Lavoro");
        Mario.put(password, post5, "Lavoro");

        try {
            Mario.put(password, post1, "CorsoDiTeatro");
        } catch (DataAlreadyExistsException e) {
            System.out.println("Non sono ammessi dati duplicati, neanche in categorie diverse");
        }

        Mario.put(password, post4, "Maneggio");
        Mario.put(password, post3, "Maneggio");
        Mario.put(password, post6, "CorsoDiTeatro");


        //----------------------------------------------------------------------
        System.out.println("\n*insertLike*");
        try {
            Mario.insertLike("Giovanna", post4);
        } catch(NotSharedCategoryException e) {
            System.out.println("Ogni amico può mettere \"like\" solo ai dati a cui ha accesso");
        }

        Iterator<MyData> showToGiovanna = Mario.getFriendIterator("Giovanna");
        while(showToGiovanna.hasNext()) {
            Mario.insertLike("Giovanna", showToGiovanna.next());
        }
        for(int i = 0; i < 5; i++) {
            Mario.insertLike("LuigiRepartoMarketing", post5);
        }
        Mario.insertLike("AnnaSegretaria", post1);
        Mario.insertLike("Lorenzo", post3);
        Mario.insertLike("Lorenzo", post3);
        Mario.insertLike("Lorenzo", post3);


        //----------------------------------------------------------------------
        System.out.println("\n*get, getFriendIterator*");
        System.out.println("Osservazione: è possibile inserire in bacheca una copia,\n" +
                "creata con get, di un dato già esistente perché get restituisce\n" +
                "un nuovo dato con valori identici.\n" +
                "Chi visualizza i dati troverà due volte lo stesso contenuto.\n" +
                "In questo esempio, le categorie \"Maneggio\" e \"CorsoDiTeatro\" contengono \n" +
                "una copia dello stesso post. Lorenzo ha accesso ad entrambe le categorie.");

        MyData post4copy = Mario.get(password, post4);
        Mario.put(password, post4copy , "CorsoDiTeatro");

        System.out.println("*************Post visibili da Lorenzo*************");
        Iterator<MyData> showToLorenzo = Mario.getFriendIterator("Lorenzo");
        while(showToLorenzo.hasNext()) {
            Mario.print(showToLorenzo.next());
        }
        System.out.println("**************************************************");


        //----------------------------------------------------------------------
        System.out.println("\n*remove*");
        Mario.remove(password, post4copy);
        System.out.println("Il dato è stato rimosso con successo");

        try {
            Mario.remove(password, post4copy);
        } catch(DataNotFoundException e) {
            System.out.println("Non è possibile rimuovere un dato che non esiste");
        }


        //----------------------------------------------------------------------
        System.out.println("\n*getDataCategory*");
        System.out.println("Il proprietario della bacheca chiede di visualizzare tutti i dati\n" +
                "nella categoria \"Lavoro\":");
        System.out.println("**********************Lavoro**********************");
        for(MyData e : Mario.getDataCategory(password, "Lavoro")) {
            Mario.print(e);
        }
        System.out.println("**************************************************");


        //----------------------------------------------------------------------
        System.out.println("\n*getIterator*");
        System.out.println("*****Tutti i dati ordinati per numero di like*****");
        Iterator<MyData> allData = Mario.getIterator(password);
        while(allData.hasNext()) {
            Mario.print(allData.next());
        }
        System.out.println("**************************************************");


        //----------------------------------------------------------------------
        System.out.println("\n********Tutti i post condivisi con Alessio********");
        Iterator<MyData> showToAlessio = Mario.getFriendIterator("Alessio");
        while(showToAlessio.hasNext()) {
            Mario.print(showToAlessio.next());
        }
        System.out.println("**************************************************");
        System.out.println("Alessio non vede nessun post perché non appartiene a nessuna categoria");
    }


}
