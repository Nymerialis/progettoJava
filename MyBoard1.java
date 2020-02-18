import java.util.*;

public class MyBoard1<E extends Data> implements DataBoard<E> {
    /*  OVERVIEW
     *      MyBoard1 è un contenitore di oggeti generici che implementa l'interfaccia DataBoard
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
     *          c.userPwd != null
     */

    private String userPwd;
    private TreeMap<String, ArrayList<String>> userFriends;
    private TreeMap<String, ArrayList<E>> userCat;
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
    public MyBoard1(String pwd) throws NullPointerException {
        if(pwd == null) throw new NullPointerException();
        userPwd = pwd;
        userFriends = new TreeMap<>();
        userCat = new TreeMap<>();
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

        userCat.get(category).add(dato);
        userData.put(dato, new dataInfo(category));

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
    public List<E> getDataCategory(String pwd, String category) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException {
        login(pwd);
        if(category == null) throw new NullPointerException();
        if(!userCat.containsKey(category)) throw new CategoryNotFoundException();
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
    }

    // Restituisce un iteratore (senza remove) che genera tutti i dati in bacheca
    // ordinati per numero decrescente di like
    public Iterator<E> getIterator(String pwd) throws NotAllowedActionException {
        login(pwd);
        ArrayList<E> allData = new ArrayList<>();
        for(ArrayList<E> tmp : userCat.values()) {
            allData.addAll(tmp);
        }
        Collections.sort(allData, new CompareLike());
        return new MyIterator(allData);
    }

    // Confronta il numero di like associati a due dati
    private class CompareLike implements Comparator<E> {
        @Override
        public int compare(E a, E b) {
            return userData.get(b).getNumLike() - userData.get(a).getNumLike();
        }
    }

    // Restituisce un iteratore (senza remove) che genera tutti i dati in bacheca condivisi con un amico
    public Iterator<E> getFriendIterator(String friend) throws NullPointerException, FriendNotFoundException {
        if(friend == null) throw new NullPointerException();
        if(!userFriends.containsKey(friend)) throw new FriendNotFoundException();
        ArrayList<E> allSharedData = new ArrayList<>();
        for(String cat : userFriends.get(friend)) {
            allSharedData.addAll(userCat.get(cat));
        }
        return new MyIterator(allSharedData);
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
