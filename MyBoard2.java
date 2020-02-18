import java.util.*;

public class MyBoard2<E extends Data> implements DataBoard<E> {
    /*  OVERVIEW
     *      MyBoard2 è un contenitore di oggeti generici che implementa l'interfaccia DataBoard.
     *      I dati sono divisi in categorie e all'interno di ogni categoria sono mantenuti ordinati
     *      per numero decrescente di like
     *
     * AF(c) = < { f | c.userFriends.containsKey(f) },
     *           { cat | c.userCat.containsKey(cat) },
     *           { d | c.userData.containsKey(d) },
     *           { (fr,cat) | c.userFriends.containsKey(fr) && c.userFriends.get(fr).contains(cat) },
     *           g(d) = c.userData.get(d).getCategory(),
     *           l(d) = c.userData.get(d).getNumLike(),
     *           c.userPwd >
     *
     * I(c) =   (f != null per ogni f ∈ c.userFriends.keySet() ) &&
     *          (f1 != f2 per ogni f1,f2 ∈ c.userFriends.keySet() ) &&
     *          (cat != null per ogni cat ∈ c.userCat.keySet() ) &&
     *          (cat1 != cat2 per ogni cat1,cat2 ∈ c.userCat.keySet() ) &&
     *          (d != null per ogni d ∈ c.userData.keySet() ) &&
     *          (d1 != d2 per ogni d1,d2 ∈ c.userData.keySet() ) &&
     *          (c.userData.get(d).getCategory() ∈ c.userCat.keySet() per ogni d ∈ c.userData.keySet() ) &&
     *          (c.userData.get(d).getNumLike() >= 0 per ogni d ∈ c.userData.keySet() ) &&
     *          (per ogni cat ∈ c.userCat.keySet(),
     *           per ogni i,j: 0 <= i < j < c.userCat.get(cat).size(),
     *           c.userData.get(userCat.get(cat).get(i)).getNumLike() >= c.userData.get(userCat.get(cat).get(j)).getNumLike()) &&
     *          c.userPwd != null
     */

    private String userPwd;
    private HashMap<String, ArrayList<String>> userFriends;
    private HashMap<String, ArrayList<E>> userCat;
    private HashMap<E, dataInfo> userData;

    private class dataInfo {
        private String category;
        private int numLike;

        public dataInfo() {
            category = null;
            numLike = 0;
        }

        public dataInfo(String s) {
            category = s;
            numLike = 0;
        }

        public String getCategory() { return category; }
        public int getNumLike() { return numLike; }
        public void insertLike() { numLike++; }
    }

    //Costruttore: richiede una password iniziale
    public MyBoard2(String pwd) throws NullPointerException {
        if(pwd == null) throw new NullPointerException();
        userPwd = pwd;
        userFriends = new HashMap<>();
        userCat = new HashMap<>();
        userData = new HashMap<>();
    }

    //Effettua i controlli d'identità
    private void login(String pwd) throws NotAllowedActionException {
        if(!userPwd.equals(pwd)) throw new NotAllowedActionException("Password errata!");
    }

    //Imposta una nuova password se vengono rispettati i controlli di identità
    public boolean setPassword(String oldPwd, String newPwd) throws NullPointerException, NotAllowedActionException{
        login(oldPwd);
        if(newPwd == null) throw new NullPointerException();
        userPwd = newPwd;
        return true;
    }

    // Crea una categoria di dati
    public void createCategory(String category, String pwd) throws NotAllowedActionException,
            NullPointerException, CategoryAlreadyExistsException {
        login(pwd);
        if(category == null) throw new NullPointerException();
        if(!userCat.containsKey(category))
            userCat.put(category, new ArrayList<E>());  //inserisce la categoria
        else throw new CategoryAlreadyExistsException();
    }

    // Rimuove una categoria di dati
    public void removeCategory(String category, String pwd) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException {
        login(pwd);
        if(category == null) throw new NullPointerException();
        if(!userCat.containsKey(category)) throw new CategoryNotFoundException();
        else {
            for(E d : userCat.get(category)) {
                userData.remove(d);     //rimuove tutti i dati
            }
            userCat.remove(category);   //rimuove la categoria e i dati associati
            for(String f : userFriends.keySet()) {
                userFriends.get(f).remove(category);
            }
        }

    }

    // Aggiunge un amico ad una categoria di dati
    public void addFriend(String category, String pwd, String friend) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException {
        login(pwd);
        if((category == null) || (friend == null)) throw new NullPointerException();
        if(userCat.containsKey(category)){
            if(!userFriends.containsKey((friend))) {
                userFriends.put(friend, new ArrayList<String>());   //inserisce l'amico se non è già presente
            }
            userFriends.get(friend).add(category);  //inserisce l'amico nella categoria
        } else throw new CategoryNotFoundException();
    }

    // Rimuove un amico da una categoria di dati
    public void removeFriend(String category, String pwd, String friend) throws NotAllowedActionException,
            NullPointerException, FriendNotFoundException, CategoryNotFoundException, NotSharedCategoryException {
        login(pwd);
        if((category == null) || (friend == null)) throw new NullPointerException();
        if(!userFriends.containsKey(friend)) throw new FriendNotFoundException();
        if(!userCat.containsKey(category)) throw new CategoryNotFoundException();
        if(!userFriends.get(friend).remove(category)) throw new NotSharedCategoryException();   //rimozione
    }

    // Inserisce un dato in bacheca
    public boolean put(String pwd, E dato, String category) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException, DataAlreadyExistsException {
        login(pwd);
        if((category == null) || (dato == null)) throw new NullPointerException();
        if(!userCat.containsKey(category)) throw new CategoryNotFoundException();
        if(userData.containsKey(dato)) throw new DataAlreadyExistsException();

        userData.put(dato, new dataInfo(category));
        //al nuovo dato vengono associati 0 like

        userCat.get(category).add(dato);
        //la proprietà di ordinamento si mantiene perché il dato è inserito in coda

        return true;
    }

    // Restituisce una copia del dato in bacheca
    public E get(String pwd, E dato) throws NotAllowedActionException, NullPointerException,
            DataNotFoundException {
        login(pwd);
        if(dato == null) throw new NullPointerException();
        if(!userData.containsKey(dato)) throw new DataNotFoundException();
        return (E) dato.clone();    //crea la copia
    }

    // Rimuove il dato dalla bacheca
    public E remove(String pwd, E dato) throws NotAllowedActionException, NullPointerException,
            DataNotFoundException {
        login(pwd);
        if(dato == null) throw new NullPointerException();
        if(!userData.containsKey(dato)) throw new DataNotFoundException();

        userCat.get(userData.get(dato).getCategory()).remove(dato);
        userData.remove(dato);

        return dato;
    }

    // Crea la lista dei dati in bacheca di una determinata categoria
    // ordinati per numero decrescente di like
    public List<E> getDataCategory(String pwd, String category) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException {
        login(pwd);
        if(category == null) throw new NullPointerException();
        if(!userCat.containsKey(category)) throw new CategoryNotFoundException();

        //i dati sono già ordinati per numero decrescente di like
        return new ArrayList<>(userCat.get(category));  //crea una copia dei dati
    }

    // Aggiunge un like a un dato
    public void insertLike(String friend, E dato) throws NullPointerException, FriendNotFoundException,
            DataNotFoundException, NotSharedCategoryException {
        if((dato == null) || (friend == null)) throw new NullPointerException();
        if(!userFriends.containsKey(friend)) throw new FriendNotFoundException();
        if(!userData.containsKey(dato)) throw new DataNotFoundException();
        if(!userFriends.get(friend).contains(userData.get(dato).getCategory())) throw new NotSharedCategoryException();

        userData.get(dato).insertLike();  //aggiunge un like

        //mantiene la proprietà di ordinamento
        ArrayList<E> aux = userCat.get(userData.get(dato).getCategory());
        int i = aux.indexOf(dato);
        while( (i > 0) && (userData.get(aux.get(i-1)).getNumLike() < userData.get(dato).getNumLike()) ) {
            //sposta il dato di una posizione a sinistra
            aux.set(i, aux.get(i-1));
            aux.set(i-1, dato);
            i--;
        }
    }

    // Restituisce un iteratore (senza remove) che genera tutti i dati in bacheca
    // ordinati per numero decrescente di like
    public Iterator<E> getIterator(String pwd) throws NotAllowedActionException {
        login(pwd);
        //crea l'iteratore sfruttando la funzione ausiliaria auxMerge
        return new MyIterator(auxMerge(fromSetToArrayList(userCat.keySet())));
    }

    // Restituisce un iteratore (senza remove) che genera tutti i dati in bacheca
    // condivisi con un amico ordinati per numero decrescente di like
    public Iterator<E> getFriendIterator(String friend) throws NullPointerException, FriendNotFoundException {
        if(friend == null) throw new NullPointerException();
        if(!userFriends.containsKey(friend)) throw new FriendNotFoundException();
        //crea l'iteratore sfruttando la funzione ausuliaria auxMerge
        return new MyIterator(auxMerge(userFriends.get(friend)));
    }

    // Trasferisce un insieme di stringhe contenuti in una struttura di tipo Set<>
    // in una di tipo ArrayList<>
    private ArrayList<String> fromSetToArrayList(Set<String> a) {
        ArrayList<String> aux = new ArrayList<>();
        for(String s : a) {
            aux.add(s);
        }
        return aux;
    }

    // Funzione ausiliaria che sfrutta l'ordinamento per numero decrescente di like
    // dei dati di ogni categoria per unirli in unico array ordinato
    private ArrayList<E> auxMerge(ArrayList<String> categories) {
        ArrayList<ArrayList<E>> aux = new ArrayList<>();
        ArrayList<E> sortedData = new ArrayList<>();

        //seleziona gli array di dati di cui fare la merge
        for(String s : categories) {
            if(!userCat.get(s).isEmpty()) {
                aux.add(new ArrayList<>(userCat.get(s)));
            }
        }

        //merge
        while(!aux.isEmpty()) {
            //cerca il dato con il massimo numero di like sfruttando l'ordinamento degli array
            int indexMax = 0;
            int max = userData.get(aux.get(0).get(0)).getNumLike();
            int i = 1;
            while(i < aux.size()) {
                if(userData.get(aux.get(i).get(0)).getNumLike() > max) {
                    max = userData.get(aux.get(i).get(0)).getNumLike();
                    indexMax = i;
                }
                i++;
            }

            //rimuove il dato selezionato dalla struttura ausiliaria
            // e lo inserisce in fondo alla lista dei dati ordinati
            sortedData.add(aux.get(indexMax).remove(0));
            if(aux.get(indexMax).isEmpty()) {
                aux.remove(indexMax);
            }
        }

        return sortedData;
    }

    // Restituisce il numero di like associati al dato
    public int getNumLike(E dato) throws DataNotFoundException {
        if(!userData.containsKey(dato)) throw new DataNotFoundException();
        return userData.get(dato).getNumLike();
    }

    // Stampa a video il contenuto del dato e il numero di like associati
    public void print(E dato) throws DataNotFoundException {
        if(!userData.containsKey(dato)) throw new DataNotFoundException();
        dato.display();
        System.out.println("Like ricevuti: " + userData.get(dato).getNumLike());
        System.out.println("\t\t***\t\t");
    }
}
