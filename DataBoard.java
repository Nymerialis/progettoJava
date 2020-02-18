import java.util.Iterator;
import java.util.List;

public interface DataBoard<E extends Data> {
    /* OVERVIEW
     *      DataBoard è un contenitore di oggetti generici che estendono il tipo di dato Data.
     *      La collezione consente di memorizzare e visualizzare dati.
     *      Il proprietario della bacheca, superati i controlli d'identità, può organizzare
     *      i dati in categorie e decidere quali categorie di dati condividere con ciascun amico.
     *      I dati possono essere visualizzati dagli amici autorizzati ma modificati solamente
     *      dal proprietario della bacheca. Gli amici possono associare uno o più "like" al contenuto condiviso.
     *      Non sono ammessi duplicati di amici, categorie o dati.
     *
     * Elemento tipico: < F, C, D, FC, c: D -> C, l: D -> ℤ, pwd > dove
     *       C è un insieme di categorie con cat != null per ogni cat ∈ C e cat1 != cat2 per ogni cat1, cat2 ∈ C;
     *       F è un insieme di amici con f != null per ogni f ∈ F e f1 != f2 per ogni f1, f2 ∈ F;
     *       D è un insieme di dati con d != null per ogni d ∈ D e d1 != d2 per ogni d1, d2 ∈ D;
     *       FC è un sottoinsieme di FxC;
     *       c è una funzione che associa ad ogni dato una categoria;
     *       l è una funzione che associa ad ogni dato il numero di like ricevuti;
     *       pwd è la password scelta dal proprietario della bacheca e pwd != null.
     */


    // Crea una categoria di dati
    // se vengono rispettati i controlli di identità
    public void createCategory(String category, String pwd) throws NotAllowedActionException,
            NullPointerException, CategoryAlreadyExistsException;
    /* @REQUIRES: this.pwd == pwd && category != null && category ∉ C
     * @MODIFIES: this.C
     * @EFFECTS: post(this.C) = pre(this.C) U { category }
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se category == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se category ∈ C solleva CategoryAlreadyExistsException, nuova eccezione, checked
     */


    // Rimuove una categoria di dati
    // se vengono rispettati i controlli di identità
    public void removeCategory(String category, String pwd) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException;
    /* @REQUIRES: this.pwd == pwd && category != null && category ∈ C
     * @MODIFIES: this
     * @EFFECTS: post(this.C) = pre(this.C) \ { category }
     *           post(this.FC) = { (fr, cat) ∈ pre(this.FC) | cat != category }
     *           post(this.D) = { d ∈ pre(this.D) | c(d) != category }
     *           post(this.c) = c': post(this.D) -> C tale che c'(x) = c(x)
     *           post(this.l) = l': post(this.D) -> C tale che l'(x) = l(x)
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se category == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se category ∉ C solleva CategoryNotFoundException, nuova eccezione, checked
     */


    // Aggiunge un amico ad una categoria di dati
    // se vengono rispettati i controlli di identità
    public void addFriend(String category, String pwd, String friend) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException;
    /* @REQUIRES: this.pwd == pwd && friend != null && category != null && category ∈ C
     * @MODIFIES: this
     * @EFFECTS: post(this.F) = pre(this.F) U { friend }
     *           post(this.FC) = pre(this.FC) U { (friend, category) }
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se friend == null or category == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se category ∉ C solleva CategoryNotFoundException, nuova eccezione, checked
     */


    // Rimuove un amico da una categoria di dati
    // se vengono rispettati i controlli di identità
    public void removeFriend(String category, String pwd, String friend) throws NotAllowedActionException,
            NullPointerException, FriendNotFoundException, CategoryNotFoundException, NotSharedCategoryException;
    /* @REQUIRES: this.pwd == pwd && friend != null && friend ∈ F && category != null &&
     *            category ∈ C && (friend, category) ∈ FC
     * @MODIFIES: this.FC
     * @EFFECTS: post(this.FC) = pre(this.FC) \ { (friend, category) }
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se friend == null or category == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se friend ∉ F solleva FriendNotFoundException, nuova eccezione, checked
     *          se category ∉ C solleva CategoryNotFoundException, nuova eccezione, checked
     *          se (friend, category) ∉ FC solleva NotSharedCategoryException, nuova eccezione, checked
     */


    // Inserisce un dato in bacheca
    // se vengono rispettati i controlli di identità
    public boolean put(String pwd, E dato, String category) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException, DataAlreadyExistsException;
    /* @REQUIRES: this.pwd == pwd && dato != null && category != null && category ∈ C && data ∉ D
     * @MODIFIES: this
     * @EFFECTS: post(this.D) = pre(this.D) U { dato }
     *           post(this.c) = c': post(this.D) -> C tale che
     *                             c'(x) = c(x)         se x != dato
     *                             c'(x) = categoria    se x == dato
     *           post(this.l) = l': post(this.D) -> ℕ tale che
     *                             l'(x) = l(x)         se x != dato
     *                             l'(x) = 0            se x == dato
     * @RETURNS: true se il dato viene inserito con successo
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se dato == null or category == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se category ∉ C solleva CategoryNotFoundException, nuova eccezione, checked
     *          se dato ∈ D solleva DataAlreadyExistsException, nuova eccezione, checked
     */


    // Restituisce una copia del dato in bacheca
    // se vengono rispettati i controlli di identità
    public E get(String pwd, E dato) throws NotAllowedActionException, NullPointerException, DataNotFoundException;
    /* @REQUIRES: this.pwd == pwd && dato != null && dato ∈ D
     * @RETURNS: x tale che x == dato
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se dato == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se dato ∉ D solleva DataNotFoundException, nuova eccezione, checked
     */


    // Rimuove il dato dalla bacheca
    // se vengono rispettati i controlli di identità
    public E remove(String pwd, E dato) throws NotAllowedActionException, NullPointerException, DataNotFoundException;
    /* @REQUIRES: this.pwd == pwd && dato != null && dato ∈ D
     * @MODIFIES: this
     * @EFFECTS: post(this.D) = pre(this.D) \ { dato }
     *           post(this.c) = c': post(this.D) -> C tale che c'(x) = c(x)
     *           post(this.l) = l': post(this.D) -> C tale che l'(x) = l(x)
     * @RETURNS: dato
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se dato == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se dato ∉ D solleva DataNotFoundException, nuova eccezione, checked
     */


    // Crea la lista dei dati in bacheca di una determinata categoria
    // se vengono rispettati i controlli di identità
    public List<E> getDataCategory(String pwd, String category) throws NotAllowedActionException,
            NullPointerException, CategoryNotFoundException;
    /* @REQUIRES: this.pwd == pwd && category != null && category ∈ C
     * @RETURNS: { x ∈ D | c(x) = category }
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     *          se category == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se category ∉ C solleva CategoryNotFoundException, nuova eccezione, checked
     */


    // Aggiunge un like a un dato
    // se vengono rispettati i controlli di identità
    void insertLike(String friend, E dato) throws NullPointerException, FriendNotFoundException,
            DataNotFoundException, NotSharedCategoryException;
    /* @REQUIRES: friend != null && friend ∈ F && dato != null && dato ∈ D && (friend, c(dato)) ∈ FC
     * @MODIFIES: this
     * @EFFECTS: post(this.l) = l': this.D -> C tale che
     *                                  l'(x) = l(x)        se x != dato
     *                                  l'(x) = l(x) + 1    se x == dato
     * @THROWS: se friend == null or dato == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se friend ∉ F solleva FriendNotFoundException, nuova eccezione, checked
     *          se dato ∉ D solleva DataNotFoundException, nuova eccezione, checked
     *          se (friend, c(dato)) ∉ FC solleva NotSharedCategoryException, nuova eccezione, checked
     */


    // Restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca ordinati per numero decrescente di like
    // se vengono rispettati i controlli di identità
    public Iterator<E> getIterator(String pwd) throws NotAllowedActionException;
    /* @REQUIRES: this.pwd == pwd
     * @RETURNS: <data_0, ..., data_k> tale che { data_i | i=0..k } = D e
     *          per ogni i,j. 0 <= i < j <= k : l(data_i) >= l(data_j)
     * @THROWS: se this.pwd != pwd solleva NotAllowedActionException, nuova eccezione, checked
     */


    // Restituisce un iteratore (senza remove) che genera tutti i dati in
    // bacheca condivisi
    public Iterator<E> getFriendIterator(String friend) throws NullPointerException, FriendNotFoundException;
    /* @REQUIRES: friend != null && friend ∈ F
     * @RETURNS: { x ∈ D | (friend, c(x)) in FC }
     * @THROWS: se friend == null solleva NullPointerException, eccezione disponibile in Java, unchecked
     *          se friend ∉ F solleva FriendNotFoundException, nuova eccezione, checked
     */

}