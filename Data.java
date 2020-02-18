abstract class Data implements Cloneable{
    /* OVERVIEW
     *      Data è un tipo di dato astratto che implementa l'interfaccia Cloneable
     *
     * Elemento tipico: < CONTENT >
     */

    //Stampa a video il contenuto del dato
    abstract public void display();

    @Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch(CloneNotSupportedException e) {
            return null;
        }
    }

}
